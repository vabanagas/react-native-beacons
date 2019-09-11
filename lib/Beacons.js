/**
 * @format
 * @flow
 * @jsdoc
 */

import {NativeModules, NativeEventEmitter} from 'react-native';

const RNBeacons = NativeModules.Beacons;
const RNBeaconsEmitter = new NativeEventEmitter(RNBeacons);

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

/**
 * `React Native Beacons` is a simple library for detecting beacons on iOS and Android
 */
const Beacons = {
  /**
   * Requests permission to use location services whenever the app is running.
   */
  requestAlwaysAuthorization(): void {
    return RNBeacons.requestAlwaysAuthorization();
  },
  /**
   * Requests permission to use location services while the app is in the foreground.
   */
  requestWhenInUseAuthorization(): void {
    return RNBeacons.requestWhenInUseAuthorization();
  },
  /**
   * Starts the delivery of events for the specified beacon region.
   * @param {string} uuid
   * @param {string} identifier
   */
  startRangingBeaconsInRegion(uuid: string, identifier: string): void {
    return RNBeacons.startRangingBeaconsInRegion(uuid, identifier);
  },
  /**
   * Stops the delivery of events for the specified beacon region.
   * @param {string} uuid
   * @param {string} identifier
   */
  stopRangingBeaconsInRegion(uuid: string, identifier: string): void {
    return RNBeacons.stopRangingBeaconsInRegion(uuid, identifier);
  },
  /**
   * Adds a listener to be invoked when beacons are ranged.
   * @param listener - Function to invoke when the specified event is emitted
   */
  addListener(listener: (result: Result) => void): any {
    return RNBeaconsEmitter.addListener('didRangeBeacons', listener);
  },
  /**
   * Removes all of the registered listeners, including those registered as listener maps.
   */
  removeListeners(): void {
    return RNBeaconsEmitter.removeAllListeners('didRangeBeacons');
  },
  PROXIMITY_UNKOWN: 'unknown',
  PROXIMITY_FAR: 'far',
  PROXIMITY_NEAR: 'near',
  PROXIMITY_IMMEDIATE: 'immediate',
};

export default Beacons;
