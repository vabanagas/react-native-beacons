package com.reactlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import javax.annotation.Nullable;

public class BeaconsModule extends ReactContextBaseJavaModule implements BeaconConsumer {

    private final ReactApplicationContext reactContext;
    private BeaconManager beaconManager;
    private Context context;

    public BeaconsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public void initialize() {
        this.context = this.reactContext.getApplicationContext();
        this.beaconManager = BeaconManager.getInstanceForApplication(this.context);
        // need to bind at instantiation so that service loads (to test more)
        this.beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        if(!this.beaconManager.isBound(this)) {
            this.beaconManager.bind(this);
        }
    }

    @Override
    public Context getApplicationContext() {
        return this.context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        this.context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.context.bindService(intent, serviceConnection, i);
    }

    @Override
    public String getName() {
        return "Beacons";
    }

    @ReactMethod
    public void startRangingBeaconsInRegion(String uuid, String identifier) {
        try {
            Region region = new Region(uuid, null, null, null);
            this.beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.i("ReactNative", e.getLocalizedMessage());
        }
    }

    @ReactMethod
    public void stopRangingBeaconsInRegion(String uuid, String identifier) {
        try {
        Region region = new Region(uuid, null, null, null);
        this.beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.i("ReactNative", e.getLocalizedMessage());
        }
    }

    private void sendEvent(ReactApplicationContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    private String getProximity(double distance) {
        if (distance == -1.0) {
            return "unknown";
        } else if (distance < 1) {
            return "immediate";
        } else if (distance < 3) {
            return "near";
        } else {
            return "far";
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    WritableMap map = new WritableNativeMap();
                    map.putString("identifier", region.getUniqueId());
                    map.putString("uuid", region.getId1() != null ? region.getId1().toString() : "");
                    WritableArray a = new WritableNativeArray();
                    for (Beacon beacon : beacons) {
                        WritableMap b = new WritableNativeMap();
                        b.putString("uuid", beacon.getId1().toString());
                        if (beacon.getIdentifiers().size() > 2) {
                            b.putInt("major", beacon.getId2().toInt());
                            b.putInt("minor", beacon.getId3().toInt());
                        }
                        b.putInt("rssi", beacon.getRssi());
                        if(beacon.getDistance() == Double.POSITIVE_INFINITY
                                || Double.isNaN(beacon.getDistance())
                                || beacon.getDistance() == Double.NaN
                                || beacon.getDistance() == Double.NEGATIVE_INFINITY){
                            b.putDouble("distance", 999.0);
                            b.putString("proximity", "far");
                        }else {
                            b.putDouble("distance", beacon.getDistance());
                            b.putString("proximity", getProximity(beacon.getDistance()));
                        }
                        a.pushMap(b);
                    }
                    map.putArray("beacons", a);
                    sendEvent(reactContext, "didRangeBeacons", map);
                }
            }
        });
    }
}
