package com.example.jacek.a3dball;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
public class MainActivity extends Activity implements SensorEventListener {
    protected SurfaceView glSurfaceView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float yVelocity = 0.0f;
    private float xVelocity = 0.0f;
    private int xPosition = 0;
    private int yPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        glSurfaceView = new SurfaceView(this);

        setContentView(glSurfaceView);

        glSurfaceView.getWidth();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xAcceleration = event.values[1];
        float yAcceleration = event.values[0];

        float frameTime = 0.666f;
        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        float x = (xVelocity / 2) * frameTime;
        float y = (yVelocity / 2) * frameTime;

        xPosition += x;
        yPosition += y;

        glSurfaceView.setBallPosition(xPosition, yPosition);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        glSurfaceView.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        glSurfaceView.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
