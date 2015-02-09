
import java.util.ArrayList;
import android.R.bool;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;

/**
* Class which detect an abs exercise by using proximity and accelerometer captors
* @author Devirtualiz'Us
*/
public class AbsDetector implements DetectorInterface {

	private SensorManager sensorManager;
	
	private ArrayList<Boolean> directions = new ArrayList<Boolean>();
	private ArrayList<Coord> tabCoord = new ArrayList<Coord>();
	private Boolean hautOK = false, basOK = false;
	private final int limite_basse = 3;
	private final int limite_haute = 9;
	private Boolean isProx = false;

	//this value is increment every time the listener detect a movement
	private int count;
	
	public AbsDetector(){
		count = 0;
		limite_basse = 3;
		limite_haute = 9;
	}

	@Override
	public void startDetection() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	@Override
	public void stopDetection() {
		sensorManager.unregisterListener(this);
		count = 0;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			synchronized (this) {
				this.getProximityValue(event);
			}
		}

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			synchronized (this) {
				 int countReadyToUse = this.getDetectorValues(event);
			}
		}
	}

	@Override
	public int getDetectorValues(SensorEvent event) {

		// initialisation
		if ((hautOK == false) && (basOK == false) && (event.values[1] < 2)) {
			basOK = true;
		}

		// low position
		if ((event.values[1] < limite_basse) && hautOK && isProx) {
			hautOK = false;
			basOK = true;
		}

		// high position : count one push
		if ((event.values[1] > limite_haute) && basOK && isProx) {
			basOK = false;
			hautOK = true;
			count++;

			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			// vibrate every 10 counts
			if ((count % 10) == 0)
				v.vibrate(300);
			else
				// Vibrate for 500 milliseconds
				v.vibrate(50);
		}
		return count;
	}

	public void getProximityValue(SensorEvent event) {
		if (event.values[0] == 0)
			isProx = true;
		else
			isProx = false;
	}

	//Functions which are not necessary in our development
	@Override
	public int getDetectorValues(Location location) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
