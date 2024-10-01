package com.flexa.reactnative

import android.util.Patterns
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.flexa.core.shared.AppAccount
import com.flexa.core.shared.AvailableAsset
import com.flexa.core.shared.CustodyModel
import com.flexa.spend.Transaction

fun Transaction.toWritableMap(): WritableMap = Arguments.createMap().apply {
  putString("commerceSessionId", commerceSessionId)
  putString("amount", amount)
  putString("appAccountId", appAccountId)
  putString("assetId", assetId)
  putString("destinationAddress", destinationAddress)
  putString("feeAmount", feeAmount)
  putString("feeAssetId", feeAssetId)
  putString("feePrice", feePrice)
  putString("feePriorityPrice", feePriorityPrice)
  putString("size", size)
}

@Throws(ClassCastException::class, NullPointerException::class)
fun ReadableArray.toAppAccounts(): ArrayList<AppAccount> {
  val accounts = ArrayList<AppAccount>(this.size())
  for (rawWallet in this.toArrayList()) {
    val wallet = rawWallet as HashMap<*, *>
    val accountId = wallet["accountId"].toString()
    val cm = wallet["custodyModel"].toString()
    val custodyModel = CustodyModel.valueOf(cm.uppercase())
    val accountDisplayName = try {
      wallet["displayName"].toString()
    } catch (e: NullPointerException) {
      null
    }
    val accountIcon = try {
      wallet["icon"].toString()
    } catch (e: NullPointerException) {
      null
    }
    val rawAssets = wallet["availableAssets"] as ArrayList<HashMap<String, Double>>

    val assets = ArrayList<AvailableAsset>(rawAssets.size)
    for (rawAsset in rawAssets) {
      val symbol = try {
        rawAsset["symbol"].toString()
      } catch (e: NullPointerException) {
        null
      }
      val balance = rawAsset["balance"] as Double
      val balanceAvailable = try {
        rawAsset["balanceAvailable"] as Double
      } catch (e: NullPointerException) {
        null
      }
      val icon = try {
        val i = rawAsset["icon"].toString()
        if (Patterns.WEB_URL.matcher(i).matches()) i else null
      } catch (e: NullPointerException) {
        null
      }
      val displayName = try {
        rawAsset["displayName"].toString()
      } catch (e: NullPointerException) {
        null
      }
      val assetId = rawAsset["assetId"].toString()
      assets.add(
        AvailableAsset(
          assetId = assetId,
          balance = balance,
          balanceAvailable = balanceAvailable,
          icon = icon,
          displayName = displayName,
          symbol = symbol,
          accentColor = null
        )
      )
    }
    accounts.add(
      AppAccount(
        accountId = accountId,
        custodyModel = custodyModel,
        displayName = accountDisplayName,
        icon = accountIcon,
        availableAssets = assets
      )
    )
  }
  return accounts
}
