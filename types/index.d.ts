declare module 'react-native-beacons' {
  /**
   * React Native Beacons is a simple library for detecting beacons on iOS and Android
   */

  type Result = {
    region: {
      identifier: string,
      uuid: string,
      major?: string,
      minor?: string,
    },
    beacons: Array<{
      uuid: string,
      major: string,
      minor: string,
      rssi: number,
      proximity: string,
      accuracy: number,
      distance: number,
    }>,
  };

  export interface BeaconsStatic {
    /**
     * Starts the delivery of events for the specified beacon region.
     */
    startRangingBeaconsInRegion(uuid: string, identifier: string): void;

    /**
     * Stops the delivery of events for the specified beacon region.
     */
    stopRangingBeaconsInRegion(uuid: string, identifier: string): void;

    /**
     * Adds a listener to be invoked when beacons are ranged.
     */
    addListener(listener: (result: Result) => void): EmitterSubscription;

    /**
     * Removes all of the registered listeners, including those registered as listener maps.
     */
    removeListeners(): void;
  }

  const Beacons: BeaconsStatic;

  export default Beacons;
}