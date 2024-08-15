import { Platform } from 'react-native';
import { getNativeFlexaModule } from '../initialize';

enum ConnectStatus {
  SDKIsNotInitialized = 'sdkNotInitialized',
  Connected = 'connected',
  Pending = 'pending',
  NotConnected = 'notConnected',
}

interface ConnectResult {
  status: ConnectStatus;
  idToken?: string;
  debugMessage?: string;
}

/**
 * Gets the authentication state of the Flexa with Flexa.
 *
 * @returns{Promise<ConnectResult>} Promise object with a resolution
 *
 *
 * @example
 * try {
 *   const loginState = await getLoginState(onQRCallback)
 * } catch (e) {
 *   console.error(e)
 * }
 */
export const getLoginState = () => {
  return new Promise<ConnectResult>((resolve, reject) => {
    try {
      console.log('Flexa.getLoginState called');

      const fn = Platform.select({
        android: () =>
          getNativeFlexaModule().getLoginState((result: ConnectResult) => {
            console.log('Flexa.getLoginState finished', result);
            resolve(result);
          }),
        ios: () =>
          getNativeFlexaModule().getLoginState((result: any) => {
            console.log('Flexa.getLoginState finished:', result);
            resolve(result);
          }),
      });

      fn?.();
    } catch (error) {
      console.log('Flexa.getLoginState error:', error);
      reject(error);
    }
  });
};

export const logout = () => {
  const fn = Platform.select({
    android: () => getNativeFlexaModule().logout(),
    ios: () => getNativeFlexaModule().logout(() => null),
  });

  fn?.();
};
