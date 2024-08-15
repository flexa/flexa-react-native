import Foundation
import Flexa

@objc(FlexaReactNative)
class FlexaReactNative: NSObject {

    @objc func `init`(_ publishableKey: String, appAccounts: [[String : Any]], themingData: String) -> Void {
        Flexa.initialize(
            FXClient(publishableKey: publishableKey, appAccounts: self.buildAppAccountsArray(from: appAccounts), themingDataString: themingData)
        )
        NSLog("Flexa.initialize() invoked")
        return
    }

    @objc func payment(_ appAccounts: [[String : Any]], callback: @escaping RCTResponseSenderBlock, reject: @escaping RCTResponseSenderBlock) -> Void {
        DispatchQueue.main.async {
            Flexa.sections([.spend])
                .appAccounts(self.buildAppAccountsArray(from: appAccounts))
                .onTransactionRequest { result in
                    switch result {
                    case .success(let transaction):
                        callback([transaction.asDictionary])
                    case .failure:
                        reject(nil)
                    }
                }
                .open()
        }
        NSLog("Flexa.payment() invoked")
        return
    }

    @objc
    func processUniversalLink(_ urlString: String, callback: @escaping RCTResponseSenderBlock) {
        DispatchQueue.main.async { [weak self] in
            guard let _ = self,
                  let url = URL(string: urlString) else {
                callback([["processed": false]])
                return
            }

            let result = Flexa.processUniversalLink(url: url)

            callback([["processed": result]])
        }
    }

    @objc func logout(_ callback: @escaping RCTResponseSenderBlock) -> Void {
        FlexaIdentity.disconnect()
        NSLog("Flexa.logout() invoked")
        return
    }

    @objc
    func getLoginState(_ callback: @escaping RCTResponseSenderBlock) {
        Flexa.buildIdentity().build().collect { [weak self] result in
            self?.handleLoginStateResult(result: result, callback: callback)
        }
    }

    @objc func transactionSent(_ sessionId: String, signature: String) {
        Flexa.transactionSent(commerceSessionId: sessionId, signature: signature)
    }

    private func handleLoginStateResult(result: ConnectResult, callback: @escaping RCTResponseSenderBlock) {
        DispatchQueue.main.async {
            var response: [String: Any] = [:]

            switch result {
                case .connected:
                    response["status"] = "connected"

                case .notConnected(let error):
                    response["status"] = "notConnected"
                    if let error = error {
                        response["error"] = error.localizedDescription
                    }
            }

            callback([response])
        }
    }

    private func buildAppAccountsArray(from appAccounts: [[String: Any]]) -> [FXAppAccount] {
        appAccounts.compactMap(buildUserWallet)
    }

    private func buildUserWallet(from dictionary: [String: Any]) -> FXAppAccount? {
        guard let availableAssets = dictionary["availableAssets"] as? [[String: Any]],
              let displayName = dictionary["displayName"] as? String,
              let accountId = dictionary["accountId"] as? String,
              let custodyModelString = dictionary["custodyModel"] as? String,
              let custodyModel = FXAppAccount.CustodyModel(rawValue: custodyModelString.lowercased()) else {
            return nil
        }

        return FXAppAccount(
            accountId: accountId,
            displayName: displayName,
            custodyModel: custodyModel,
            availableAssets: buildUserAssetsArray(from: availableAssets),
            icon:nil
        )
    }

    private func buildUserAssetsArray(from userAssets: [[String: Any]]) -> [FXAvailableAsset] {
        userAssets.compactMap(buildUserAsset)
    }

    private func buildUserAsset(from dictionary: [String: Any]) -> FXAvailableAsset? {
        guard let symbol = dictionary["symbol"] as? String,
              let assetId = dictionary["assetId"] as? String,
              let displayName = dictionary["displayName"] as? String,
              let balance = dictionary["balance"] as? Double else {
            return nil
        }

        let icon = URL(string: (dictionary["icon"] as? String) ?? "")

        return FXAvailableAsset(assetId: assetId ,symbol: symbol, balance: Decimal(balance), displayName: displayName, logoImageUrl: icon)
    }
}

extension FXTransaction {
    var asDictionary: [String: Any] {
        do {
            let data = try self.jsonData()
            return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] ?? [:]
        } catch let error {
            debugPrint(error)
            return [:]
        }
    }
}
