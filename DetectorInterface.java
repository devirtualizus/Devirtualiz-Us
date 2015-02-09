import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;

/**
* This interface aims to enable the development of new modules that may be included into the Android application Devirtualiz'Us.
* @see SensorEventListener
* @author Devirtualiz'Us
*/
public interface DetectorInterface extends SensorEventListener{
	
	/**
	* The main function in your detection.
	* You must implement your algorithm that uses the values retrieved by the sensor selected in startDetection(). 
	* These values are accessible in the parameter event.
	* @return a counter which is incremented if you detect the movement you want
	* @author Devirtualiz'Us
	*/
	public int getDetectorValues(SensorEvent event);
	
	/**
	* To implement if you want to use location listener instead of event listener
	* @param event Values detected by the captor you initiated in startDetection()
	* @return: a counter which is incremented if you detect the movement you want
	* @author: Devirtualiz'Us
	*/
	public int getDetectorValues(Location location);
	
	/**
	 * To start detection
	 * Usually, you'll have to initiate and configure your sensorManager inside this function
	 * @param location Values detected by the captor you initiated in startDetection()
	 * @author: Devirtualiz'Us
	 */
	public void startDetection();
	
	/**
	 * To stop detection
	 * Usually, you'll have to unregistered the sensorManager that you have initiated in startDetection()
	 * @author: Devirtualiz'Us
	 */
	public void stopDetection();
}
