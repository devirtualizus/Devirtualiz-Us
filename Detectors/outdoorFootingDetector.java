import java.text.DecimalFormat;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

/**
* Class which detect an outdoor footing by using GPS (Location sensor)
* @see LocationListener
* @author Devirtualiz'Us
*/
public class OutdoorFootingDetector extends Activity implements
		DetectorInterface, LocationListener {

	private LocationManager locationManager;
	private String provider;
	private Location location;
	
	private long timeStart;
	private float distance;
	private float speed;
	//this value is increment every time the listener detect a movement
	private int count;

	public outdoorFootingDetector()
	{
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		provider = locationManager.getBestProvider(new Criteria(), false);
		count = 0;
		speed = 0;
	}

	@Override
	public int getDetectorValues(Location location) {
		if (this.location == null) {
			this.timeStart = location.getTime();
		} else {
			long time = (location.getTime() - this.timeStart) / 1000;
			//distanceTo give us the distance between two locations, return value is a float
			this.distance = this.distance + location.distanceTo(this.location);
			//here, we divide distance by 100 to get 1 count every 100 meters
			count = (int) (distance / 100);
			if (time != 0)
				speed = (float) (3.6 * distance / time);
		}
		//we store the location to use it in the next iteration
		this.location = location;
		return count;
	}

	@Override
	public void startDetection() {
		locationManager.requestLocationUpdates(provider, 1000, 0, this);
		this.distance = 0;
	}

	@Override
	public void stopDetection() {
		locationManager.removeUpdates(this);
		this.location = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		int countReadyToUse = getDetectorValues(location);
	}

	//Functions which are not necessary in our development
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getDetectorValues(SensorEvent event) {
		// TODO Auto-generated method stub
		return 0;
	}

}