# react-native-beacons

Beacon detection for React Native via iBeacon

## Getting started

`$ npm install @vabanagas/react-native-beacons --save`

### Mostly automatic installation

`$ react-native link @vabanagas/react-native-beacons`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `@vabanagas/react-native-beacons` and add `Beacons.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libBeacons.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.BeaconsPackage;` to the imports at the top of the file
  - Add `new BeaconsPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':@vabanagas/react-native-beacons'
  	project(':@vabanagas/react-native-beacons').projectDir = new File(rootProject.projectDir, 	'../node_modules/@vabanagas/react-native-beacons/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':@vabanagas/react-native-beacons')
  	```


## Usage
```javascript
import Beacons from '@vabanagas/react-native-beacons';

componentDidMount() {
	// Request permissions
	Beacons.requestAlwaysAuthorization();
	// Start ranging beacons
	Beacons.startRangingBeaconsInRegion('REGION_UUID', 'REGION_IDENTIFIER')
	// Add an event listener
	Beacons.addListener(data => {
		const { region, beacons } = data;
		// Do something with region/beacon data
	}
}

componentWillUnmount() {
	// Remove event listeners
	Beacons.removeListeners();
	// Stop ranging beacons
	Beacons.stopRangingBeaconsInRegion('REGION_UUID', 'REGION_IDENTIFIER');
}
```

## TODO
- Android support