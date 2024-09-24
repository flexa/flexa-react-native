#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(FlexaReactNative, NSObject)

RCT_EXTERN_METHOD(init: (NSString *)publishableKey appAccounts: (NSArray *)appAccounts themingData: (NSString *)themingData)
RCT_EXTERN_METHOD(payment: (NSArray *)appAccounts callback: (RCTResponseSenderBlock)callback reject: (RCTResponseSenderBlock)reject)
RCT_EXTERN_METHOD(updatePaymentCallback: (NSArray *)appAccounts callback: (RCTResponseSenderBlock)callback reject: (RCTResponseSenderBlock)reject)
RCT_EXTERN_METHOD(updateAppAccounts: (NSArray *)appAccounts)
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
