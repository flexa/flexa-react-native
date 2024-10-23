import {
  init,
  payment,
  FlexaContext,
  logout,
  processUniversalLink,
  getLoginState,
  updateAssetAccounts,
  useFlexaLinks
} from '../index';

describe('SDK exported functions tests', () => {
  it('Should have an exported init function', () => {
    expect(init).toBeDefined();
    expect(init).toBeInstanceOf(Function);
  });
  it('Should have an exported payment function', () => {
    expect(payment).toBeDefined();
    expect(payment).toBeInstanceOf(Function);

  });
  it('Should have an exported SDK open context provider and consumer', () => {
    expect(FlexaContext).toBeDefined();
    expect(FlexaContext.FlexaContextProvider).toBeDefined();
    expect(FlexaContext.FlexaContextConsumer).toBeDefined();
  });

  it("Should export logout, getLoginState, processUniversalLink, updateAssetAccounts, useFlexaLinks functions", () => {
    expect(logout).toBeDefined();
    expect(logout).toBeInstanceOf(Function);
    expect(processUniversalLink).toBeDefined();
    expect(processUniversalLink).toBeInstanceOf(Function);
    expect(getLoginState).toBeDefined();
    expect(getLoginState).toBeInstanceOf(Function);
    expect(updateAssetAccounts).toBeDefined();
    expect(updateAssetAccounts).toBeInstanceOf(Function);
    expect(useFlexaLinks).toBeInstanceOf(Function);
  })
});
