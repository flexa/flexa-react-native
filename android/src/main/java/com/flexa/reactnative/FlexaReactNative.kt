package com.flexa.reactnative

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.flexa.core.Flexa
import com.flexa.core.shared.AppAccount
import com.flexa.core.shared.FlexaClientConfiguration
import com.flexa.core.theme.FlexaTheme
import com.flexa.core.theme.SpendColorScheme
import com.flexa.identity.buildIdentity
import com.flexa.identity.shared.ConnectResult
import com.flexa.spend.*
import com.flexa.reactnative.theme.ThemeAdapter


@ReactModule(name = "FlexaReactNative")
class FlexaReactNative(private val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  private val listeners = HashMap<Event, Callback>(Event.values().size)

  /**
   * Initializes the Flexa SDK
   */
  @ReactMethod
  fun init(publishableKey: String, appAccounts: ReadableArray, themingData: String) {
    val adapter = ThemeAdapter()
    val themeData = adapter.stringToThemeData(themingData)
    val lightBackground = adapter.getColor(themeData?.android?.light?.backgroundColor)
    val darkBackground = adapter.getColor(themeData?.android?.dark?.backgroundColor)
    val lightCard = adapter.getColor(themeData?.android?.light?.cardColor)
    val darkCard = adapter.getColor(themeData?.android?.dark?.cardColor)

    val lightColorScheme = SpendColorScheme(
      background = lightBackground?: Color.White,
      surface = lightBackground?: Color.White,
      onPrimary = lightCard?: Color.Gray.copy(.3F)
    )
    val darkColorScheme = SpendColorScheme(
      background = darkBackground?: Color(0xFF363636),
      surface = darkBackground?: Color(0xFF363636),
      onPrimary = darkCard?: Color(0xFF575757)
    )

    val appAccountsResult = appAccounts.toAppAccounts()

    Flexa.init(
      FlexaClientConfiguration(
        context = reactApplicationContext.applicationContext,
        publishableKey = publishableKey,
        webViewThemeConfig = themingData,
        theme = FlexaTheme(
          lightColorsScheme = lightColorScheme,
          darkColorsScheme = darkColorScheme
        ),
        appAccounts = appAccountsResult as ArrayList<AppAccount>
      )
    )
  }

  @ReactMethod
  fun getLoginState(listener: Callback) {
    Flexa.buildIdentity().build().collect { connectResult ->
      val output = Arguments.createMap()
      when (connectResult) {
        is ConnectResult.Connected -> {
          output.putString("status", "connected")
          output.putString("idToken", connectResult.idToken)
        }

        else -> {
          output.putString("status", "notConnected")
        }
      }
      kotlin.runCatching { listener.invoke(output) }
    }
  }

  /**
   * Logout the user from Flexa
   */
  @ReactMethod
  fun logout() {
    Flexa.buildIdentity().build().disconnect {
      listeners[Event.DISCONNECT]?.invoke()
      listeners.remove(Event.DISCONNECT)

      reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        .emit(Event.DISCONNECT.name, null)
    }
  }

  /**
   * Starts Payment in Flexa
   */
  @ReactMethod
  fun payment(appAccounts: ReadableArray, onSuccessCB: Callback, onFailure: Callback?) {
    currentActivity?.let { activity ->
      val appAccountsResult = appAccounts.toAppAccounts()
      Flexa.updateAppAccounts(appAccountsResult)
      Flexa.buildSpend()
        .onTransactionRequest{ res ->
          if (res.isSuccess) {
            val transaction = res.getOrThrow()
            onSuccessCB.invoke(transaction.toWritableMap())
          } else {
            onFailure?.invoke(res.exceptionOrNull())
          }
        }
        .open(activity)
    }
  }

  @ReactMethod
  fun updatePaymentCallback(appAccounts: ReadableArray, onSuccessCB: Callback, onFailure: Callback?) {
    currentActivity?.let { activity ->
      val appAccountsResult = appAccounts.toAppAccounts()
      Flexa.updateAppAccounts(appAccountsResult)
      Flexa.buildSpend()
        .onTransactionRequest{ res ->
          if (res.isSuccess) {
            val transaction = res.getOrThrow()
            onSuccessCB.invoke(transaction.toWritableMap())
          } else {
            onFailure?.invoke(res.exceptionOrNull())
          }
        }
    }
  }

  @ReactMethod
  fun updateAppAccounts(appAccounts: ReadableArray) {
    val appAccountsResult = appAccounts.toAppAccounts()
    Flexa.updateAppAccounts(appAccountsResult as ArrayList<AppAccount>)
  }

  /**
   * Notifies Flexa for a specific transaction
   */
  @ReactMethod
  fun transactionSent(
    commerceSessionId: String,
    txSignature: String,
  ) {
    Flexa.buildSpend().transactionSent(commerceSessionId, txSignature)
  }

  @ReactMethod
  fun processUniversalLink(
    urlString: String,
  ) {
    currentActivity?.let {
      Flexa.buildIdentity().open(it, urlString)
    }
  }

  @ReactMethod
  fun dismissAllModals(cb: Callback) {
    cb.invoke(true)
  }

  override fun getName(): String {
    return "FlexaReactNative"
  }

  enum class Event {
    DISCONNECT
  }
}
