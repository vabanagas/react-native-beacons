#import <CoreLocation/CoreLocation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface Beacons : RCTEventEmitter <RCTBridgeModule, CLLocationManagerDelegate>

@property (strong, nonatomic) CLLocationManager *locationManager;

@end
