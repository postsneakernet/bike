package bike_analytics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties({"averages"})
public class Ride {
	private List<Averages> _averages;
	private double _distance;
	private double _odometer;
	private double _maxSpeed;
	private double _maxCadence;
	private String _rideDate;
	private String _rideTime;
	
	public double[] calcAverages() {
		double[] avg = new double[2];
		double sumSpeed = 0;
		double sumCadence = 0;
		int count = 0;
		
		for (Averages a : _averages) {
			sumSpeed += a.getSpeed();
			sumCadence += a.getCadence();
			++count;
		}
		
		if (count > 0) {
			avg[0] = sumSpeed / count;
			avg[1] = sumCadence / count;
		}
		
		return avg;
	}
	
	public List<Averages> getAverages() {
		return _averages;
	}
	
	public double getDistance() {
		return _distance;
	}
	
	public double getOdometer() {
		return _odometer;
	}
	
	public double getMaxSpeed() {
		return _maxSpeed;
	}
	
	public double getMaxCadence() {
		return _maxCadence;
	}
	
	public String getRideTime() {
		return _rideTime;
	}
	
	public String getRideDate() {
		return _rideDate;
	}
	
	public void setAverages(List<Averages> a) {
		_averages = a;
	}
	
	public void setDistance(double d) {
		_distance = d;
	}
	
	public void setOdometer(double d) {
		_odometer = d;
	}
	
	public void setMaxSpeed(double d) {
		_maxSpeed = d;
	}
	
	public void setMaxCadence(double d) {
		_maxCadence = d;
	}
	
	public void setRideTime(String s) {
		_rideTime = s;
	}
	
	public void setRideDate(String s) {
		_rideDate = s;
	}
}

class Averages {
	private double _speed;
	private double _cadence;
	
	public double getSpeed() {
		return _speed;
	}
	
	public double getCadence() {
		return _cadence;
	}
	
	public void setSpeed(double d) {
		_speed = d;
	}
	
	public void setCadence(double d) {
		_cadence = d;
	}
}