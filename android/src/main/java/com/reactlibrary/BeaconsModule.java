package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class BeaconsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public BeaconsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Beacons";
    }

    @ReactMethod
    public void requestAlwaysAuthorization() {
        // TODO: Request permissions
    }

    @ReactMethod
    public void requestWhenInUseAuthorization() {
        // TODO: Request permissions
    }

    @ReactMethod
    public void startRangingBeaconsInRegion(String uuid, String identifier) {
        // TODO: Start beacon ranging
    }

    @ReactMethod
    public void stopRangingBeaconsInRegion(String uuid, String identifier) {
        // TODO: Stop beacon ranging
    }
}
