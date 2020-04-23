package com.example.homework20;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

public class LightSensorActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isSensorPresent;
    private ContentResolver mContentResolver;
    private Window mWindow;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightsensor_layout);
        Context context = getApplicationContext();

        // Check whether has the write settings permission or not.
        boolean settingsCanWrite = Settings.System.canWrite(context);

        if(!settingsCanWrite) {
            // If do not have write settings permission then open the Can modify system settings panel.
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(intent);
        }
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }
        initScreenBrightness();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            mSensorManager.unregisterListener(this);
        }
    }

    public void initScreenBrightness() {
        mContentResolver = getContentResolver();
        mWindow = getWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager = null;
        mSensor = null;
        mContentResolver = null;
        mWindow = null;
    }

    public void changeScreenBrightness(float brightness) {
        //system setting brightness values ranges between 0-255
        //We scale up by multiplying by 255
        //This change brightness for over all system settings
        Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, (int) (brightness * 255));
        //screen brightness values ranges between 0 - 1
        //This only changes brightness for the current window
        WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
        mLayoutParams.screenBrightness = brightness;
        mWindow.setAttributes(mLayoutParams);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float light = event.values[0];
        //We only use light sensor value between 0 - 100
        //Before sending, we take the inverse of the value
        //So that they remain in range of 0 - 1
        if (light > 0 && light < 100) {
            changeScreenBrightness(1 / light);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}