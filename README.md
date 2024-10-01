# Flexa Components for React Native

With Flexa Components, you can quickly and easily add new layers of functionality to your wallet app.

In the current release, Flexa offers a privacy-focused payments experience for in-person and online spending anywhere Flexa is accepted (SpendKit), along with a simple scanner module for parsing QR code–formatted payment requests (ScanKit).

## Modules

| Module        | Description                                            |
| ------------- | ------------------------------------------------------ |
| Flexa   | Core functionality required by all other modules       |
| FlexaScanKit  | Camera-based parsing of QR codes for payments and more |
| FlexaSpendKit | Instant retail payments, powered by Flexa              |

## Developer Setup (Pre-Installation guide)

#### SSH Key Configuration:

1. **Generate SSH Key (if you haven't already):**

   If you don't have an SSH key already, you can generate one using the following command:

```bash
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

Replace "your_email@example.com" with your actual email address.

2. **Copy SSH Key to Clipboard:**

   After generating the SSH key, you'll need to copy the public key to your clipboard. You can do this using the following command:

```bash
pbcopy < ~/.ssh/id_rsa.pub
 ```

This command copies the contents of your public key to the clipboard.

3. **Add SSH Key to GitHub:**

   Log in to your GitHub account and go to Settings > SSH and GPG keys. Then, click on the "New SSH Key" button. Paste the copied SSH key into the "Key" field and give it a relevant title, such as "My SSH Key for Cocoapods Repo". Click "Add SSH Key" to save it.

4. **Test SSH Connection:**

   To ensure that your SSH key is set up correctly, you can test the connection to GitHub:

```bash
ssh -T git@github.com
```

If everything is set up correctly, you should see a message like "Hi username! You've successfully authenticated...".

### Android:

In your `android/local.properties` directory, add the Github username and a classic personal access token key which
can be generated on Github -> Settings -> Developer settings -> Generate a personal access token (classic) :

```text
gpr.user=<githubUsername>
gpr.key=<personalAccessToken>
```

After adding that, add the function to read from the local.properties file and add the maven repository for Flexa Android packages in your `android/build.gradle` file:

```
// Load local.properties file
def localProperties = new Properties()
def localPropertiesFile = rootProject.file('local.properties')
if (localPropertiesFile.exists()) {
    localPropertiesFile.withInputStream { stream ->
        localProperties.load(stream)
    }
}
...

buildscript {
    ext {
    ...
    }
}

allprojects {
    repositories {

    ...

      maven {
          name = "GitHubPackages"
          url = uri("https://maven.pkg.github.com/flexa/flexa-android")
          credentials {
              username = localProperties.getProperty("gpr.user")
              password = localProperties.getProperty("gpr.key")
          }
      }
    }
}
```

Or

You can build a mavenLocal repo to use it in this stage. Follow the following steps in order to build it:

1. Clone the following android sdk repo:

```
https://github.com/flexa/flexa-android.git
```

2. Run the next buildScript from the SDK root folder:

```groovy
./gradlew core:assembleDebug && ./gradlew core:publishToMavenLocal && ./gradlew && ./gradlew scankit:assembleDebug && ./gradlew scankit:publishToMavenLocal && ./gradlew spendkit:assembleDebug && ./gradlew spendkit:publishToMavenLocal
```

3. Add `mavenLocal()` repository to your React Native App `./android/build.gradle`:

```groovy
   allprojects {
    repositories {
      ...
      mavenLocal()
    }
   }
```

### iOS:

Add the private cocoapods repo from flexa in your Podfile `./ios/Podfile`:

```ruby
source 'git@github.com:flexa/flexa-cocoapods.git'
```

#### Using pod repo add:

You can use the `pod repo add` command to add the source. Here's how:

```bash
pod repo add flexa-cocoapods git@github.com:flexa/flexa-cocoapods.git
```

## Installation

To install the private developer GitHub package registry you would need to download the release compressed package and
store it in a local directory and run the following command:

```sh
yarn add @flexa/flexa-react-native@"./<localdir>/<flexa-flexa-react-native-<version>.tgz"
```

or with a GitHub personal access token (classic)

```sh
npm config set "@flexa:registry" "https://npm.pkg.github.com/" && \
npm config set "//npm.pkg.github.com/:_authToken" "YOUR_AUTH_TOKEN" && \
yarn add @flexa/flexa-react-native
```

When published on NPM, the package will be available to install:

```sh
npm install @flexa/flexa-react-native
```
or
```sh
yarn add @flexa/flexa-react-native
```

and finally install the pods for the ios platform

```sh
cd ios
pod install
```

## Usage

### Initialization

Execute the following SDK init function on the top level parent component of your App.
You can obtain a publishableKey for the app integration from Flexa.

```js
import { init } from '@flexa/flexa-react-native';

publishableKey = "publishable_test_xyz"

init(publishableKey, appAccounts, webViewThemingData) // appAccounts and webViewThemingData are optional

```
or wrap the main App component with the SpendContextProvider component

```js
<FlexaContext.FlexaContextProvider
  publishableKey={publishableKey}
  appAccounts={appAccounts} //optional
  webViewThemingData={webViewThemingData} //optional
>
  <App />
</FlexaContext.FlexaContextProvider>
```


Please check out [Theming](#theming) for the webViewThemingData

### Universal Links

To implement a seamless Flexa login with Linking in React Native, the following settings should be applied:

#### iOS

Make sure to add the correct entitlements using

XCode -> select Project -> Signing and Capabilities -> Add Capability -> Associated Domains

and then add `applinks:<app-name>.flexa.link` in the Associated Domains Capability

In your `ios/<projectDirectory>/AppDelegate.mm`

first import the React RCTLinkingManager

```#import <React/RCTLinkingManager.h>```

and then add the following functions before the `@end` of the file

```objective-c
- (BOOL)application:(UIApplication *)application
   openURL:(NSURL *)url
   options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options
{
  return [RCTLinkingManager application:application openURL:url options:options];
}

- (BOOL)application:(UIApplication *)application continueUserActivity:(nonnull NSUserActivity *)userActivity
 restorationHandler:(nonnull void (^)(NSArray<id<UIUserActivityRestoring>> * _Nullable))restorationHandler
{
 return [RCTLinkingManager application:application
                  continueUserActivity:userActivity
                    restorationHandler:restorationHandler];
}
```

#### Android

In your `android/app/src/main/AndroidManifest.xml` add the following intent-filter under the .MainActivity

```xml
        <intent-filter android:autoVerify="true">
          <action android:name="android.intent.action.VIEW" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <data
                  android:host="<app-name>.flexa.link"
                  android:scheme="https" />
        </intent-filter>
```

#### React Native

In order to enable the processing of the universal links by the Flexa SDK, you would need to add the following functionality
in the App.tsx or parent level Component

```js
  useEffect(() => {
    const handleUrlEvents = urlEvent => {
      if (urlEvent.url) processUniversalLink(urlEvent.url);
    };
    const linkSubscription = Linking.addEventListener('url', handleUrlEvents);

    Linking.getInitialURL().then((url) => url && processUniversalLink(url));
    return () => linkSubscription.remove();
  }, []);
```

### Payments only View

The following example shows how to open the Flexa SDK screen for the Flexa payments.

The first argument in the payment() function is a list of appAccounts passed from the parent app.

A callback is passed which will return a TransactionRequest object when the Flexa SDK Pay is clicked.


```js
import { payment, TransactionRequest } from '@flexa/flexa-react-native';

const paymentCallback = (transactionRequest: TransactionRequest) => {
  //execute the transaction depending on parent app logic here
  const {transaction, transactionSent } = transactionRequest;

  /* transaction contains
    destinationAddress: string; eip155:1:0x123... destination address for payment
    amount: string; // the fee price in decimals string representation
    feePriorityPrice: string; the fee priority price in decimals string representation
    feePrice: string; the fee price in decimals string representation
    size: string; // transaction size bigint (i.e. gasLimit)
    assetId: string; // assetId CAIP19 notation of the asset that is to be sent
    accountId: string; // which accountId was used for the payment (i.e which wallet to send from)
  */

  const TX_SIGNATURE = yourTransactionSendFunction({ ...transaction })

  // This helps Flexa confirm the transaction quickly for self-custody wallets. It is a callback sent back to the SDK with the transaction signature i.e hash
  transactionSent(TX_SIGNATURE)

}

const manualPayment = async () => {
  const appAccounts = [
    {
      displayName: 'Wallet 1',
      accountId: '0x1..', // this can be a uuid or a sha256 of the wallet address
      custodyModel: CUSTODY_MODEL.LOCAL,
      availableAssets: [
        {
          assetId: 'eip155:1/slip44:60',
          symbol: 'ETH',
          displayName: "Ether",
          balance: 0.5,
          balanceAvailable: 0.5, // add it if different from the balance due to pending transactions etc.
          icon: undefined
        },
        { assetId: 'eip155:1/erc20:0xdac17f958d2ee523a2206206994597c13d831ec7', symbol: 'USDT', displayName: "USDT", balance: 200, icon: undefined },
        { assetId: 'eip155:1/erc20:0xff20817765cb7f73d4bde2e66e067e58d11095c2', symbol: 'AMP', displayName: "AMP", balance: 300, icon: undefined },
      ],
    },
    {
      displayName: 'Wallet 2',
      accountId: '0x2..',
      custodyModel: 'LOCAL', // this can be LOCAL or MANAGED depending on the wallet type (self custody, or custodial)
      availableAssets: [
        { assetId: 'eip155:1/slip44:60', symbol: 'ETH', displayName: "Ether", balance: 0.25, icon: undefined },
        { assetId: 'eip155:1/erc20:0x6b175474e89094c44da98b954eedeac495271d0f', symbol: 'DAI', displayName: "DAI", balance: 120, icon: undefined },
        { assetId: 'eip155:1/erc20:0x0d8775f648430679a709e98d2b0cb6250d2887ef', symbol: 'BAT', displayName: "BAT", balance: 4000, icon: undefined },
      ],
    },
  ];

  await payment(appAccounts, paymentCallback);
};

```

You can also use the built-in FlexaButton component like the following code sample

```js
<FlexaButton
        appAccounts={appAccounts}
        paymentCallback={paymentCallback}
        width={32}
        height={32}
        borderRadius={4}
/>
```

### Theming

Flexa SDK allows great customization of the SDK look and feel within the screens.

It works both with the native iOS and Android screens

The following format should be passed `webViewThemingData` with the following example structure:

```js
webViewThemingData = {
  android: {
    light: {
      backgroundColor: "rgba(252, 248, 253, 0.5)",
      sortTextColor: "#333333",
      titleColor: "#007bff",
      primary: "#28a745",
      cardColor: "#ffffff",
      borderRadius: "8px"
    },
    dark: {
      backgroundColor: "rgba(19, 19, 22, 0.96)",
      sortTextColor: "#ffffff",
      titleColor: "#ffcc00",
      primary: "#dc3545",
      cardColor: "#2d2d2d",
      borderRadius: "12px"
    },
  },
  iOS: {
    light: {
      backgroundColor: "rgba(0, 0, 0, 0.04)",
      sortTextColor: "#666666",
      titleColor: "#ff6600",
      primary: "#007bff",
      cardColor: "#ffffff",
      borderRadius: "10px"
    },
    dark: {
      backgroundColor: "rgba(0, 0, 0, 0.96)",
      sortTextColor: "#cccccc",
      titleColor: "#ffffff",
      primary: "#28a745",
      cardColor: "#2d2d2d",
      borderRadius: "8px"
    },
  },
}

```

## Permissions

The Flexa SDK requires Camera (QR Scanner) and Internet (Flexa Payments, advanced scanner) access.

For iOS, you can additionally enable the FaceID permission in XCode :

in the `ios/<project>/Info.plist` add the following key/string pair:

```
<key>NSFaceIDUsageDescription</key>
<string>We need this to confirm the transaction</string>
```

## Privacy

Flexa will **never** attempt to access your users’ private keys, wallet addresses, a history of any actions taken in-app, or other sensitive wallet details. There is no method that enables you to provide any of this information to Flexa, and Flexa does not automatically extract any of this information from your app.

In order to enable payments for your users, federal regulations require Flexa to collect some personal information. This information typically consists of a user’s full name and date of birth. For higher-value payments, it can also include a photo ID document and photograph. This information is used only for verification purposes, and Flexa will never share this information with you or with any of the business your users pay.

Please note that making any modifications to your app or any of Flexa’s code with the intent to gather, retain, or otherwise access this personal information is expressly prohibited by the Flexa Developer Agreement, and will result in a permanent ban from using Flexa software for your business and any related individuals.

## Contributing

We welcome and appreciate contributions to Flexa Components from the open source community.

- For larger changes, please open an issue describing your objectives so that we can coordinate efforts.
- Or, if you would like to make a minor edit (such as a single-line modification or to fix a typo), please feel free to open a pull request with your changes and we will review it promptly.

## License

Flexa Components for React Native is [available under the MIT License](LICENSE.md).
