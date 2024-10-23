import {useEffect, useState} from "react";
import {Linking} from "react-native";
import {processUniversalLink} from "../initialize";

/**
 * React hook to process universal (deeplinks), it can be used in the top-level component to process Flexa links,
 * or just return the url if it is not used by the Flexa SDK
 *
 * @example
 *
 * const url = useFlexaLinks()
 *
 */
const useFlexaLinks = () => {
  const [link, setLink] = useState<string | null>(null);

  useEffect(() => {
    const handleUrlEvents = (urlEvent: any) => {
      if (urlEvent.url) {
        processUniversalLink(urlEvent.url);
        setLink(urlEvent.url)
      }
    };

    const linkSubscription = Linking.addEventListener('url', handleUrlEvents);

    Linking.getInitialURL().then((url) => {
      url && processUniversalLink(url)
      url && setLink(url)
    });

    return () => {
      linkSubscription.remove();
      setLink(null)
    }

  }, []);

  return link;
}

export default useFlexaLinks;
