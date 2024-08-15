import { init, payment, FlexaContext } from '../index';

describe('SDK exported functions tests', () => {
  it('Should have an exported init function accepting the correct parameters', () => {
    expect(init).toBeDefined();
  });
  it('Should have an exported payment function accepting the correct parameters', () => {
    expect(payment).toBeDefined();
  });
  it('Should have an exported SDK open context provider and consumer accepting the correct parameters', () => {
    expect(FlexaContext).toBeDefined();
    expect(FlexaContext.FlexaContextProvider).toBeDefined();
    expect(FlexaContext.FlexaContextConsumer).toBeDefined();
  });
});
