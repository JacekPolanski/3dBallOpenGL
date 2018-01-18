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
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity implements SensorEventListener, GestureDetector.OnGestureListener {
    protected SurfaceView glSurfaceView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float yVelocity = 0.0f;
    private float xVelocity = 0.0f;
    private int xPosition = 0;
    private int yPosition = 0;

    private boolean isBounceXPositive = false;
    private boolean isBounceXNegative = false;
    private boolean isBounceYPositive = false;
    private boolean isBounceYNegative = false;
    private static final int BOUNCE_VALUE = 50;
    private int bounceXValue = BOUNCE_VALUE;
    private int bounceYValue = 2 * BOUNCE_VALUE;

    private GestureDetectorCompat mDetector;

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

        mDetector = new GestureDetectorCompat(this,this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (xPosition > (int) GameRenderer.BOARD_WIDTH * 100) {
            isBounceXPositive = true;
        } else if (xPosition < - (int) GameRenderer.BOARD_WIDTH * 100) {
            isBounceXNegative = true;
        }

        if (yPosition > (int) GameRenderer.BOARD_LENGTH * 100 - 50) {
            isBounceYPositive = true;
        } else if (yPosition < - (int) GameRenderer.BOARD_LENGTH * 100 + 50) {
            isBounceYNegative = true;
        }

//        if (yPosition > (int) glSurfaceView.getWall1Position()[1] * 100) {
//            if (xPosition > (int) glSurfaceView.getWall1Position()[0] * 100) {
//                isBounceXPositive = true;
//            } else if (xPosition < (int) glSurfaceView.getWall1Position()[0] * 100) {
//                isBounceXNegative = true;
//            }
//            isBounceYPositive = true;
//        } else if (yPosition < (int) glSurfaceView.getWall1Position()[1] * 100) {
//            if (xPosition > (int) glSurfaceView.getWall1Position()[0] * 100) {
//                isBounceXPositive = true;
//            } else if (xPosition < (int) glSurfaceView.getWall1Position()[0] * 100) {
//                isBounceXNegative = true;
//            }
//            isBounceYNegative = true;
//        }

        float xAcceleration = event.values[1];
        float yAcceleration = event.values[0];
        float frameTime = 0.666f;

        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        float x = (xVelocity / 2) * frameTime;
        float y = (yVelocity / 2) * frameTime;

        if (!isBounceXPositive && !isBounceXNegative) {
            xPosition += x;
        }
        if (!isBounceYPositive && !isBounceYNegative) {
            yPosition += y;
        }

        bounceBall();

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

    private void bounceBall()
    {
        if (isBounceXPositive) {
            xPosition--;
            if (bounceXValue == 0) {
                isBounceXPositive = false;
                bounceXValue = BOUNCE_VALUE;
            } else {
                bounceXValue--;
            }
        }

        if (isBounceXNegative) {
            xPosition++;
            if (bounceXValue == 0) {
                isBounceXNegative = false;
                bounceXValue = BOUNCE_VALUE;
            } else {
                bounceXValue--;
            }
        }

        if (isBounceYPositive) {
            yPosition--;
            if (bounceYValue == 0) {
                isBounceYPositive = false;
                bounceYValue = BOUNCE_VALUE;
            } else {
                bounceYValue--;
            }
        }

        if (isBounceYNegative) {
            yPosition++;
            if (bounceYValue == 0) {
                isBounceYNegative = false;
                bounceYValue = BOUNCE_VALUE;
            } else {
                bounceYValue--;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        glSurfaceView.flipFollowingCameraOnOff();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        glSurfaceView.setCameraHeight(distanceY/10);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        yVelocity = 0.0f;
        xVelocity = 0.0f;
        xPosition = 0;
        yPosition = 0;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
