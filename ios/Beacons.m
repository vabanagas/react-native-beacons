#import "Beacons.h"

#import <CoreLocation/CoreLocation.h>

@implementation Beacons

RCT_EXPORT_MODULE();

#pragma mark Initialization

- (instancetype)init
{
  if (self = [super init]) {
    NSLog(@"initialize called");
    
    self.locationManager = [[CLLocationManager alloc] init];
    
    self.locationManager.delegate = self;
    self.locationManager.pausesLocationUpdatesAutomatically = NO;
  }
  
  return self;
}

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"didRangeBeacons"];
}

#pragma mark


RCT_EXPORT_METHOD(requestAlwaysAuthorization)
{
  if ([self.locationManager respondsToSelector:@selector(requestAlwaysAuthorization)]) {
    [self.locationManager requestAlwaysAuthorization];
  }
}

RCT_EXPORT_METHOD(requestWhenInUseAuthorization)
{
  if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
    [self.locationManager requestWhenInUseAuthorization];
  }
}

RCT_EXPORT_METHOD(startRangingBeaconsInRegion:(NSString *)uuid identifier:(NSString *)identifier)
{
  NSLog(@"Starting Beacon Monitoring");
  
  // Create region instance
  NSUUID *beaconUUID = [[NSUUID alloc] initWithUUIDString:uuid];
  CLBeaconRegion *region = [[CLBeaconRegion alloc] initWithProximityUUID: beaconUUID identifier:identifier];
  
  // Start Monitoring
  [self.locationManager startRangingBeaconsInRegion: region];
}

RCT_EXPORT_METHOD(stopRangingBeaconsInRegion:(NSString *)uuid identifier:(NSString *)identifier)
{
  NSLog(@"Stopping Beacon Monitoring");
  
  // Create region instance
  NSUUID *beaconUUID = [[NSUUID alloc] initWithUUIDString:uuid];
  CLBeaconRegion *region = [[CLBeaconRegion alloc] initWithProximityUUID: beaconUUID identifier:identifier];
  
  [self.locationManager stopRangingBeaconsInRegion: region];
}

-(NSString *)stringForProximity:(CLProximity)proximity {
  switch (proximity) {
    case CLProximityUnknown:    return @"unknown";
    case CLProximityFar:        return @"far";
    case CLProximityNear:       return @"near";
    case CLProximityImmediate:  return @"immediate";
    default:                    return @"";
  }
}

-(void)locationManager:(CLLocationManager *)manager rangingBeaconsDidFailForRegion:(CLBeaconRegion *)region withError:(NSError *)error
{
  NSLog(@"Failed ranging region: %@", error);
}

-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
  NSLog(@"Location manager failed: %@", error);
}

-(void)locationManager:(CLLocationManager *)manager didRangeBeacons:
(NSArray *)beacons inRegion:(CLBeaconRegion *)region
{
  if (beacons.count == 0) {
    return;
  }
  NSMutableArray *beaconArray = [[NSMutableArray alloc] init];
  
  for (CLBeacon *beacon in beacons) {
    [beaconArray addObject:@{
                             @"uuid": [beacon.proximityUUID UUIDString],
                             @"major": beacon.major,
                             @"minor": beacon.minor,
                             
                             @"rssi": [NSNumber numberWithLong:beacon.rssi],
                             @"proximity": [self stringForProximity: beacon.proximity],
                             @"accuracy": [NSNumber numberWithDouble: beacon.accuracy],
                             @"distance": [NSNumber numberWithDouble: beacon.accuracy],
                             }];
  }
  
  NSDictionary *event = @{
                          @"region": @{
                              @"identifier": region.identifier,
                              @"uuid": [region.proximityUUID UUIDString],
                              },
                          @"beacons": beaconArray
                          };
  
  [self sendEventWithName:@"didRangeBeacons" body:event];
}

+(BOOL)requiresMainQueueSetup
{
  return YES;
}

@end
