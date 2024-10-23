import React from 'react';
import {fireEvent, render} from '@testing-library/react-native';
import * as Flexa from '../../initialize'
import FlexaButton from '../../components/FlexaButton';

describe('FlexaButton tests', () => {
  it('matches snapshot.',  () => {
    const paymentSpy = jest.fn();
    // @ts-ignore
    Flexa.payment = paymentSpy;
    const paymentCallback = () => null
    const result = render(
      <FlexaButton assetAccounts={[]} paymentCallback={paymentCallback} />
    );

    expect(result).toMatchSnapshot();
  });

  it('initiates a payment call after being pressed.',  () => {
    const paymentSpy = jest.fn();
    // @ts-ignore
    Flexa.payment = paymentSpy;
    const paymentCallback = () => null
    const result = render(
      <FlexaButton assetAccounts={[]} paymentCallback={paymentCallback} />
    );

    fireEvent(result.getByTestId('FlexaButtonTouchableOpacity'), 'press');

    expect(result).not.toBeNull();
    expect(paymentSpy).toHaveBeenCalled();
    expect(paymentSpy).toHaveBeenCalledWith([], paymentCallback);
  });
});
