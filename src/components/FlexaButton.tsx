import * as React from 'react';
import { TouchableOpacity, Image, StyleSheet, ViewStyle } from 'react-native';
import { payment } from '../index';
import type { AppAccount } from '../initialize';

type FlexaButtonProps = {
  appAccounts: AppAccount[];
  paymentCallback: Function;
  customAuth?: Function;
  width?: number;
  height?: number;
  borderRadius?: number;
  style?: ViewStyle;
};
const FlexaButton = ({
  appAccounts,
  paymentCallback,
  customAuth,
  width = 64,
  height = 64,
  borderRadius = 6,
  style = {},
}: FlexaButtonProps) => {
  const handleClick = async () => {
    customAuth && (await customAuth());
    await payment(appAccounts, paymentCallback);
  };
  return (
    <TouchableOpacity
      style={[styles.button, { width, height }, { ...style }]}
      onPress={handleClick}
    >
      <Image
        style={{
          flex: 1,
          width: width,
          height: height,
          borderRadius: borderRadius,
        }}
        resizeMode="contain"
        source={require('./flexaLogo.png')}
      />
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default FlexaButton;
