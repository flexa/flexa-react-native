import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package '@flexa/flexa-react-native' doesn't seem to be linked. Make sure: \n\n` +
  Platform?.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

type AvailableAsset = {
  assetId: string;
  icon?: string;
  displayName: string;
  symbol: string;
  balance: number;
  balanceAvailable?: number;
};

const defaultThemingData = {
  android: {
    light: {},
    dark: {},
  },
  iOS: {
    light: {},
    dark: {},
  },
};

export enum CUSTODY_MODEL {
  LOCAL = 'LOCAL',
  MANAGED = 'MANAGED',
}

type CustodyModelType = CUSTODY_MODEL.LOCAL | CUSTODY_MODEL.MANAGED | string;

export type AssetAccount = {
  assetAccountHash: string;
  displayName: string;
  custodyModel: CustodyModelType;
  icon?: string;
  availableAssets: AvailableAsset[];
};

type Transaction = {
  commerceSessionId: string;
  destinationAddress: string;
  amount: string;
  feePriorityPrice: string;
  feePrice: string;
  size: string;
  assetId: string;
  assetAccountHash: string;
};

export type TransactionRequest = {
  transaction: Transaction;
  transactionSent: Function;
};

export const getNativeFlexaModule = () =>
  NativeModules.FlexaReactNative
    ? NativeModules.FlexaReactNative
    : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/**
 * Opens the Flexa for Flexa Payments
 * Pass an assetAccounts array as a parameter and a payment callback function
 *
 * @example
 * try {
 *   await payment(assetAccounts, onPaymentCallback)
 * } catch (e) {
 *   console.error(e)
 * }
 */
export function payment(
  assetAccounts: AssetAccount[],
  onPaymentCallback: Function
): Promise<object> {
  return new Promise((resolve) => {
    const transactionRequest = async (transaction: Transaction) => {
      await onPaymentCallback({
        transaction,
        transactionSent: (txSignature: string) =>
          getNativeFlexaModule().transactionSent(
            transaction.commerceSessionId,
            txSignature
          ),
      });
      getNativeFlexaModule().updatePaymentCallback(assetAccounts, transactionRequest, onPaymentCallback)
    }
    getNativeFlexaModule().payment(
      assetAccounts,
      transactionRequest,
      onPaymentCallback
    );
    resolve({});
  });
}

/**
 * Initializes the Flexa SDK module, please run this on the top layer of the app
 *
 * @example
 *
 * init(
 *  publishableKey: string,
 *  assetAccounts: AssetAccount[],
 *  webViewThemingData: object
 * )
 *
 */
export function init(
  publishableKey: string,
  assetAccounts: AssetAccount[] = [],
  webViewThemingData?: object
): void {
  const themingDataString = JSON.stringify(
    webViewThemingData || defaultThemingData
  );
  const fn = Platform.select({
    ios: () => {
      const iosThemingData = {
        webViewThemeConfig: webViewThemingData || defaultThemingData,
      };
      getNativeFlexaModule().init(
        publishableKey,
        assetAccounts,
        JSON.stringify(iosThemingData)
      );
    },
    android: () => {
      getNativeFlexaModule().init(
        publishableKey,
        assetAccounts,
        themingDataString
      );
    },
  });

  fn?.();
}

export function processUniversalLink(url: string) {
  const fn = Platform.select({
    ios: () =>
      getNativeFlexaModule().processUniversalLink(url, (result: any) => {
        console.log('RNFlexa.processUniversalLink: callback called: ', result);
      }),
    android: () => {
      getNativeFlexaModule().processUniversalLink(url);
    },
  });

  fn?.();
}

export function dismissAllModals() {
  const fn = Platform.select({
    ios: () =>
      new Promise((resolve, reject) => {
        try {
          getNativeFlexaModule().dismissAllModals((result: any) => {
            resolve(result);
          })
        } catch (e) {
          reject(e)
        }
      }),
    android: () =>
      new Promise((resolve, reject) => {
        try {
          getNativeFlexaModule().dismissAllModals((result: any) => {
            resolve(result);
          })
        } catch (e) {
          reject(e)
        }
      })
  });

  return fn?.();
}

/**
 * Updates the Flexa SDK assetAccounts with new availableAssets and balances
 * Pass an assetAccounts array as a parameter
 *
 * @example
 * try {
 *   updateAssetAccounts(assetAccounts)
 * } catch (e) {
 *   console.error(e)
 * }
 */
export function updateAssetAccounts(
  assetAccounts: AssetAccount[],
): void {
  const fn = Platform.select({
    ios: () => {
      getNativeFlexaModule().updateAssetAccounts(
        assetAccounts,
      );
    },
    android: () => {
      getNativeFlexaModule().updateAssetAccounts(
        assetAccounts,
      );
    },
  });

  fn?.();
}
