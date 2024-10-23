import React, { useEffect } from 'react';
import {init, payment, useFlexaLinks} from '../index';
import type { AssetAccount } from '../initialize';

const FlexaContext = React.createContext({});

/**
 * This Context Provider can be added to the top level parent component of the App.
 *
 * It initializes the Flexa by providing the following parameters
 * @publishableKey {String} - publishableKey is required to communicate with the Flexa API
 * @assetAccounts {AssetAccounts[]} - Initiate the Flexa with preloaded assetAccounts
 * @webViewThemingData {object} - optional - custom styles and theming options for the webViews of the Flexa SDK
 */
function FlexaContextProvider({
  publishableKey = '',
  assetAccounts = [],
  children = null,
  webViewThemingData,
}: {
  publishableKey?: string;
  assetAccounts?: AssetAccount[];
  webViewThemingData?: object;
  children?: any;
}) {
  useFlexaLinks()
  useEffect(() => {
    init(publishableKey, assetAccounts, webViewThemingData);
  });
  return <FlexaContext.Provider value={{ payment }} children={children} />;
}

/**
 * This Context Consumer is added to make the following props available to the child component
 *
 * @payment() - opens the Flexa payments screen
 *
 *
 */
// @ts-ignore
function FlexaContextConsumer({ children }) {
  return <FlexaContext.Consumer children={children} />;
}

export default { FlexaContextProvider, FlexaContextConsumer };
