#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(FlexaReactNative, NSObject)

RCT_EXTERN_METHOD(init: (NSString *)publishableKey assetAccounts: (NSArray *)assetAccounts themingData: (NSString *)themingData)
RCT_EXTERN_METHOD(payment: (NSArray *)assetAccounts callback: (RCTResponseSenderBlock)callback reject: (RCTResponseSenderBlock)reject)
RCT_EXTERN_METHOD(updatePaymentCallback: (NSArray *)assetAccounts callback: (RCTResponseSenderBlock)callback reject: (RCTResponseSenderBlock)reject)
RCT_EXTERN_METHOD(updateAssetAccounts: (NSArray *)assetAccounts)
RCT_EXTERN_METHOD(logout: (RCTResponseSenderBlock)callback)
RCT_EXTERN_METHOD(getLoginState: (RCTResponseSenderBlock)callback)
RCT_EXTERN_METHOD(processUniversalLink: (NSString *)url callback:(RCTResponseSenderBlock)callback)
RCT_EXTERN_METHOD(transactionSent: (NSString *)sessionId signature: (NSString *)signature)
RCT_EXTERN_METHOD(dismissAllModals: (RCTResponseSenderBlock)callback)

+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

@end
