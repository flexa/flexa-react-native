import React from 'react';
import { Button, View } from 'react-native';
import { fireEvent, render } from '@testing-library/react-native';
import * as Flexa from '../../initialize';
import FlexaContext from '../../context/FlexaContext';

describe('FlexaContext tests', () => {
  it('FlexaContext.FlexaContextProvider initiates the Flexa SDK with the correct parameters.', () => {
    const initSpy = jest.fn();
    // @ts-ignore
    Flexa.init = initSpy;
    const result = render(
      <FlexaContext.FlexaContextProvider publishableKey="publishableKey" />
    );
    expect(result).not.toBeNull();
    expect(initSpy).toHaveBeenCalled();
    expect(initSpy).toHaveBeenCalledWith('publishableKey', [], undefined);
  });

  it('FlexaContext.FlexaContextConsumer has props for payment.', async () => {
    const paymentSpy = jest.fn((paymentCB) => {
      paymentCB({ success: true });
    });
    // @ts-ignore
    Flexa.payment = paymentSpy;
    const result = render(
      <FlexaContext.FlexaContextProvider publishableKey="publishableKey">
        <FlexaContext.FlexaContextConsumer>
          {(props: any) => (
            <View>
              <Button
                title="ConsumerButton"
                testID="paymentButton"
                onPress={() => props.payment(() => null)}
              />
            </View>
          )}
        </FlexaContext.FlexaContextConsumer>
      </FlexaContext.FlexaContextProvider>
    );

    expect(result).not.toBeNull();

    const button = result.getByText('ConsumerButton');

    fireEvent.press(button);

    expect(paymentSpy).toHaveBeenCalled();
    expect(paymentSpy).toHaveReturned();
  });
});
