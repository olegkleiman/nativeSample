package com.deno.okey.nativesample;

import android.content.Context;
import android.graphics.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;

import java.util.List;
import java.util.Map;

//@ReactModule(name = ToastModule.NAME)
public class ToastModule extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    private static final String GRAVITY_TOP_KEY = "TOP";
    private static final String GRAVITY_BOTTOM_KEY = "BOTTOM";
    private static final String GRAVITY_CENTER = "CENTER";
    public static final String NAME = "ToastAndroid";

    public ToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = MapBuilder.newHashMap();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        constants.put(GRAVITY_TOP_KEY, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        constants.put(GRAVITY_BOTTOM_KEY, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        constants.put(GRAVITY_CENTER, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        return constants;
    }

    @ReactMethod
    public void hasFlash() {
//        Camera camera = RNCamera.getInstance().acquireCameraInstance();
//        if (null == camera) {
//            promise.reject("No camera found.");
//            return;
//        }
//
//        List<String> flashModes = camera.getParameters().getSupportedFlashModes();
//        promise.resolve(null != flashModes && !flashModes.isEmpty());
    }

    @ReactMethod
    public void getWifiSSID(final String message) { //Callback callback) {
        Context context = this.getReactApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//        callback.invoke(null, "SSID");
    }

    @ReactMethod
    public void show(final String message, final int duration) {

        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getReactApplicationContext(), message, duration).show();
            }
        });
    }

//    @ReactMethod
//    public void showWithGravity(final String message, final int duration, final int gravity) {
//        UiThreadUtil.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast toast = Toast.makeText(getReactApplicationContext(), message, duration);
//                toast.setGravity(gravity, 0, 0);
//                toast.show();
//            }
//        });
//    }

    @ReactMethod
    public void showWithGravityAndOffset(
            final String message,
            final int duration,
            final int gravity,
            final int xOffset,
            final int yOffset) {
        UiThreadUtil.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getReactApplicationContext(), message, duration);
                        toast.setGravity(gravity, xOffset, yOffset);
                        toast.show();
                    }
                });
    }
}
