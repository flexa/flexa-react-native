import * as React from 'react';
import { StyleSheet, View, Button, Linking } from 'react-native';
import {
  FlexaContext,
  logout,
  payment,
  FlexaButton,
  getLoginState,
  processUniversalLink,
  CUSTODY_MODEL,
  TransactionRequest,
} from '@flexa/flexa-react-native';

const WEBVIEW_THEME = {
  android: {
    light: {
      backgroundColor: 'rgba(252, 248, 253, 0.5)',
      sortTextColor: '#333333',
      titleColor: '#007bff',
      primary: '#28a745',
      cardColor: '#ffffff',
      borderRadius: '8px',
      listItemDistance: '10px',
      combineListItems: 'true',
      bottomBorder: '1px solid #e9ecef',
      bottomBorderColor: '#e9ecef',
      paddingLeft: '16px',
      paddingRight: '16px',
    },
    dark: {
      backgroundColor: 'rgba(19, 19, 22, 0.96)',
      sortTextColor: '#ffffff',
      titleColor: '#ffcc00',
      primary: '#dc3545',
      cardColor: '#2d2d2d',
      borderRadius: '12px',
      listItemDistance: '12px',
      combineListItems: 'false',
      bottomBorder: '1px solid #343a40',
      bottomBorderColor: '#343a40',
      paddingLeft: '20px',
      paddingRight: '20px',
    },
  },
  iOS: {
    light: {
      backgroundColor: 'rgba(0, 0, 0, 0.04)',
      sortTextColor: '#666666',
      titleColor: '#ff6600',
      primary: '#007bff',
      cardColor: '#ffffff',
      borderRadius: '10px',
      listItemDistance: '8px',
      combineListItems: 'false',
      bottomBorder: '1px solid #e9ecef',
      bottomBorderColor: '#e9ecef',
      paddingLeft: '12px',
      paddingRight: '12px',
    },
    dark: {
      backgroundColor: 'rgba(0, 0, 0, 0.96)',
      sortTextColor: '#cccccc',
      titleColor: '#ffffff',
      primary: '#28a745',
      cardColor: '#2d2d2d',
      borderRadius: '8px',
      listItemDistance: '10px',
      combineListItems: 'true',
      bottomBorder: '1px solid #343a40',
      bottomBorderColor: '#343a40',
      paddingLeft: '16px',
      paddingRight: '16px',
    },
  },
};

export default () => {
  const publishableKey = 'publishable_test_XYZ';

  const payCB = (res: TransactionRequest) => {
    console.log('manual payment: ', res);
  };

  const appAccounts = [
    {
      displayName: 'Wallet 1',
      accountId: '0x1..',
      custodyModel: CUSTODY_MODEL.LOCAL,
      availableAssets: [
        {
          assetId: 'eip155:1/slip44:60',
          symbol: 'ETH',
          displayName: 'Ether',
          balance: 0.5,
          balanceAvailable: 0.5, // if different from the balance due to pending transactions etc.
          icon: undefined,
        },
        {
          assetId: 'eip155:1/erc20:0xdac17f958d2ee523a2206206994597c13d831ec7',
          symbol: 'USDT',
          displayName: 'USDT',
          balance: 200,
          icon: undefined,
        },
        {
          assetId: 'eip155:1/erc20:0xff20817765cb7f73d4bde2e66e067e58d11095c2',
          symbol: 'AMP',
          displayName: 'AMP',
          balance: 300,
          icon: undefined,
        },
      ],
    },
    {
      displayName: 'Wallet 2',
      accountId: '0x2..',
      custodyModel: 'LOCAL',
      availableAssets: [
        {
          assetId: 'eip155:1/slip44:60',
          symbol: 'ETH',
          displayName: 'Ether',
          balance: 0.25,
          icon: undefined,
        },
        {
          assetId: 'eip155:1/erc20:0x6b175474e89094c44da98b954eedeac495271d0f',
          symbol: 'DAI',
          displayName: 'DAI',
          balance: 120,
          icon: undefined,
        },
        {
          assetId: 'eip155:1/erc20:0x0d8775f648430679a709e98d2b0cb6250d2887ef',
          symbol: 'BAT',
          displayName: 'BAT',
          balance: 4000,
          icon: undefined,
        },
      ],
    },
  ];

  const manualPayment = async () => {
    await payment(appAccounts, payCB);
  };

  React.useEffect(() => {
    getLoginState().then((r) => console.log('Login state: ', r));
  });

  React.useEffect(() => {
    const handleUrlEvents = (urlEvent: any) => {
      if (urlEvent.url) processUniversalLink(urlEvent.url);
    };
    const linkSubscription = Linking.addEventListener('url', handleUrlEvents);

    Linking.getInitialURL().then((url) => url && processUniversalLink(url));
    return () => linkSubscription.remove();
  }, []);

  return (
    <FlexaContext.FlexaContextProvider
      publishableKey={publishableKey}
      webViewThemingData={WEBVIEW_THEME}
    >
      <View style={styles.container}>
        <View style={styles.buttonWrapper}>
          <Button title="Open Payments" onPress={() => manualPayment()} />
          <FlexaButton
            style={{ alignSelf: 'center' }}
            appAccounts={appAccounts}
            paymentCallback={payCB}
            borderRadius={8}
            width={72}
            height={72}
          />
          <Button title="Logout" onPress={() => logout()} />
        </View>
      </View>
    </FlexaContext.FlexaContextProvider>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  buttonWrapper: {
    justifyContent: 'space-between',
    height: 200,
  },
});
