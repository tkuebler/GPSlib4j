package com.diddlebits.gpslib4j;

/**
* Class used to store degrees, usually latitude or longitude.
*/ 

public class PositionDegrees {	
	protected double value;
	
	public PositionDegrees(double v) {
		value = v;
	}
	
	/**
	* Returns the degrees part of this object, when converted to coordinates.
	*/		
	public int getDegrees() {
		return (int) value;	
	}
	
	/**
	* Converts the degrees to Radians. 
	*/
	public PositionRadians convertToRadians() {
		return new PositionRadians(( value * Math.PI ) / 180.0d);
	}
	
	/**
	* Returns the minutes part of this object, when converted to coordinates.
	*/	
	public double getMinutes() {
		double v = value;
		v -= getDegrees();
		return 60 * v; // 60 minutes in one degree.
	}
	
	public String toString() {
		return String.valueOf(getDegrees()) + "\' " + String.valueOf((int)getMinutes()) + "\"";
	}
}