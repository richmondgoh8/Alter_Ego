package com.rlc4u.myalterego;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Training extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private Sensor accelerometerSensor;

    private boolean deviceIsFlat;
    private int pushCounter = 0;
    private int deductableCount;
    private TextView trainingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        pushCounter = 0;
        deductableCount = 0;
        trainingText = findViewById(R.id.trainingText);

        if (proximitySensor == null) {
            Log.e("Warning", "Proximity sensor not available.");
            finish(); // Close app
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000);
        sensorManager.registerListener(accelerometerSensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proximitySensorListener);
        sensorManager.unregisterListener(accelerometerSensorListener);
    }


    // Create listener
    SensorEventListener proximitySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // More code goes here
            if (sensorEvent.values[0] < proximitySensor.getMaximumRange() && deviceIsFlat) {
                pushCounter += 1;
                trainingText.setText("Push Ups: " + String.valueOf(pushCounter));
                updateStrength(pushCounter);
                // Detected something nearby
                //getWindow().getDecorView().setBackgroundColor(Color.RED);
            } else {
                // Nothing is nearby
                //getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    SensorEventListener accelerometerSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //float[] g = new float[3];
            float[] g = sensorEvent.values.clone();
            double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);

            // Normalize the accelerometer vector
            g[0] = g[0] / (float)norm_Of_g;
            g[1] = g[1] / (float)norm_Of_g;
            g[2] = g[2] / (float)norm_Of_g;

            int inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));
            if (inclination < 25 || inclination > 155)
            {
                // device is flat
                deviceIsFlat = true;
                //getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            }
            else
            {
                //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                deviceIsFlat = false;
                // device is not flat
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    public void updateStrength(int pushCount) {
        if (deductableCount > 0) {
            pushCount -= deductableCount;
        }
        if (pushCount == 5) {
            Minion myMinion = new Minion("1", "NA", 1, 0, 100, 100, 100, 0, 1, 0, 2131, 2131);
            DBHandler db = new DBHandler(this);
            db.retrieveMinion(myMinion);
            db.updateMinion("minionStrength", myMinion.getStrengthMeter() + 1);
            db.updateMinion("minionCurrency", myMinion.getCurrency() + 1);
            deductableCount += 5;
        }

    }


}

