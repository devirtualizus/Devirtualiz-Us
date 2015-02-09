import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;

/**
* Class which detect a push up exercise by using proximity and accelerometer captors
* @author Devirtualiz'Us
*/
public class PushUpDetector implements DetectorInterface {

	private SensorManager sensorManager;
	private SoundManager sound;

	private float x, y, z;
	private float[] coordAccel;
	private long startTime = System.currentTimeMillis(), currentTime;
	private static final float x_limit_bas = (float) -2.5, x_limit_haut = (float) 2.5;
	private static final float y_limit_bas = (float) -2.5, y_limit_haut = (float) 2.5;
	private static final float z_limit_bas = (float) 7.5, z_limit_haut = (float) 12.5;
	private static final long time_beetween_2_pushup = 700;
	private Boolean firstPushUpOK = false;

	//this value is increment every time the listener detect a movement
	private int count;
	
	public pushUpDetector()
	{
		sound=new SoundManager();
		sound.initSounds(this);
		count = 0;
	}

	@Override
	public void startDetection() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void stopDetection() {
		sensorManager.unregisterListener(this);
		_count = 0;
	}

	@Override
	public int getDetectorValues(SensorEvent event) {
		// initiate the limit values at the first iteration
		if ((event.values[0] > 0)
				&& (x < x_limit_haut)
				&& (x > x_limit_bas)
				&& (y < y_limit_haut)
				&& (y > y_limit_bas)
				&& (z > z_limit_bas)
				&& (z < z_limit_haut)
				&& (System.currentTimeMillis() - startTime > time_beetween_2_pushup)) {
			firstPushUpOK = true;
		}
		// conditions to detect a push up
		if ((event.values[0] > 0)
				&& firstPushUpOK
				&& (x < x_limit_haut)
				&& (x > x_limit_bas)
				&& (y < y_limit_haut)
				&& (y > y_limit_bas)
				&& (z > z_limit_bas)
				&& (z < z_limit_haut)
				&& (System.currentTimeMillis() - startTime > time_beetween_2_pushup)) {
			count++;
			
			if(count%10==0&&Reglage.getBoolSound()){
				sound.playSound(this, R.raw.sound1);
			}
			startTime = System.currentTimeMillis();
		}
		return count;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			synchronized (this) {
				int countReadyToUse = this.getDetectorValues(event);
			}
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			synchronized (this) {
				coordAccel = new float[3];

				coordAccel = this.getAccelerometerValues(event.values);

				x = coordAccel[0];
				y = coordAccel[1];
				z = coordAccel[2];
			}
		}
	}

	//Functions which are not necessary in our development
	@Override
	public int getDetectorValues(Location location) {
		return 0;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
