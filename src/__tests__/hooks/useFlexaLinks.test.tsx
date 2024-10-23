import React from 'react';
import { View, Text, Linking } from "react-native";
import { act, render } from "@testing-library/react-native";
import * as Flexa from "../../initialize";
import useFlexaLinks from "../../hooks/useFlexaLinks";

describe('useFlexaLinks() hook tests', () => {
  it('should call processUniversalLink(url) on url event', async () => {
    const processUniversalLinkSpy = jest.fn();
    let urlListenerCallback = jest.fn();
    //@ts-ignore
    Flexa.processUniversalLink = processUniversalLinkSpy;
    //@ts-ignore
    Linking.addEventListener = jest.fn((event, callback) => {
      if (event === 'url') {
        urlListenerCallback.mockImplementation(callback);
      }
    });

    const TestComponent = () => {
      useFlexaLinks();
      return (
        <View>
          <Text>test</Text>
        </View>
      );
    };

    const result = render(<TestComponent />);

    await act(async () => {
      urlListenerCallback({ url: 'test.com' });
    });

    expect(result).not.toBeNull();
    expect(processUniversalLinkSpy).toHaveBeenCalled();
    expect(processUniversalLinkSpy).toHaveBeenCalledWith("test.com");
    jest.restoreAllMocks()
  });
});
