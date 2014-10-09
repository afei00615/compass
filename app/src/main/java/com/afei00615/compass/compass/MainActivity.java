package com.afei00615.compass.compass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afei00615.compass.compass.view.NeedleView;


public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;

    private final static String TAG = MainActivity.class.getSimpleName();

    /**
     * 加速计传感器
     */
    private Sensor accelerometerSensor;
    private float[] accelerometerValues = new float[3];
    /**
     * 磁力计传感器
     */
    private Sensor magneticSensor;
    private float[] magneticValues = new float[3];

    private float[] rationValue = new float[9];
    private float[] value = new float[3];
    private NeedleView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mView = new NeedleView(this);
        setContentView(mView);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,magneticSensor,SensorManager.SENSOR_DELAY_GAME);
//        mSensorManager.registerListener(this,
//                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerValues = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magneticValues = event.values;
        }
        mSensorManager.getRotationMatrix(rationValue,null,accelerometerValues,magneticValues);
        mSensorManager.getOrientation(rationValue, value);
//        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            int degrees = (int) Math.toDegrees(value[0]);
        int horizontal = (int)Math.toDegrees(value[2]);
//            Log.d(TAG, "" + value[0] + " " + value[1] + " " + value[2]);
//        Log.i(TAG,"" + degrees + " " + horizontal);
        if(degrees <0){
            degrees = 360+degrees;
        }
            mView.setPoint(-degrees,horizontal);
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
