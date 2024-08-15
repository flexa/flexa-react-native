import React, { useEffect } from 'react';
import { init, payment } from '../index';
import type { AppAccount } from '../initialize';

const FlexaContext = React.createContext({});

/**
 * This Context Provider can be added to the top level parent component of the App.
 *
 * It initializes the Flexa by providing the following parameters
 * @publishableKey {String} - publishableKey is required to communicate with the Flexa API
 * @appAccounts {AppAccounts[]} - Initiate the Flexa with preloaded appAccounts
 * @webViewThemingData {object} - optional - custom styles and theming options for the webViews of the Flexa SDK
 */
function FlexaContextProvider({
  publishableKey = '',
  appAccounts = [],
  children = null,
  webViewThemingData,
}: {
  publishableKey?: string;
  appAccounts?: AppAccount[];
  webViewThemingData?: object;
  children?: any;
}) {
  useEffect(() => {
    init(publishableKey, appAccounts, webViewThemingData);
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
