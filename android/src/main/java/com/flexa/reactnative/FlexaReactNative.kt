package com.flexa.reactnative

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.flexa.core.Flexa
import com.flexa.core.shared.AssetAccount
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
  fun init(publishableKey: String, assetAccounts: ReadableArray, themingData: String) {
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

    val assetAccountsResult = assetAccounts.toAssetAccounts()

    Flexa.init(
      FlexaClientConfiguration(
        context = reactApplicationContext.applicationContext,
        publishableKey = publishableKey,
        webViewThemeConfig = themingData,
        theme = FlexaTheme(
          lightColorsScheme = lightColorScheme,
          darkColorsScheme = darkColorScheme
        ),
        assetAccounts = assetAccountsResult as ArrayList<AssetAccount>
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
  fun payment(assetAccounts: ReadableArray, onSuccessCB: Callback, onFailure: Callback?) {
    currentActivity?.let { activity ->
      val assetAccountsResult = assetAccounts.toAssetAccounts()
      Flexa.updateAssetAccounts(assetAccountsResult)
      Flexa.buildSpend()
        .onTransactionRequest{ res ->
          if (res.isSuccess) {
            val transaction = res.getOrThrow()
            onSuccessCB.invoke(transaction.toWritableMap())
          } else {
            onFailure?.invoke(null)
          }
        }
        .open(activity)
    }
  }

  @ReactMethod
  fun updatePaymentCallback(assetAccounts: ReadableArray, onSuccessCB: Callback, onFailure: Callback?) {
    currentActivity?.let { activity ->
      val assetAccountsResult = assetAccounts.toAssetAccounts()
      Flexa.updateAssetAccounts(assetAccountsResult)
      Flexa.buildSpend()
        .onTransactionRequest{ res ->
          if (res.isSuccess) {
            val transaction = res.getOrThrow()
            onSuccessCB.invoke(transaction.toWritableMap())
          } else {
            onFailure?.invoke(null)
          }
        }
    }
  }

  @ReactMethod
  fun updateAssetAccounts(assetAccounts: ReadableArray) {
    val assetAccountsResult = assetAccounts.toAssetAccounts()
    Flexa.updateAssetAccounts(assetAccountsResult as ArrayList<AssetAccount>)
  }

  /**
   * Notifies Flexa for a specific transaction success
   */
  @ReactMethod
  fun transactionSent(
    commerceSessionId: String,
    txSignature: String,
  ) {
    Flexa.buildSpend().transactionSent(commerceSessionId, txSignature)
  }

    /**
     * Notifies Flexa for a transaction error
     */
    @ReactMethod
    fun transactionFailed(commerceSessionId: String) {
      Flexa.buildSpend().transactionFailed(commerceSessionId)
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
