package com.diddlebits.gpslib4j;

/**
* This interface is implemented by all packets capable of returning a position.
*/
public interface IPosition {
	/**
	* This method returns the latitude of the position.
	*/
	public PositionRadians getLatitude();
	
	/**
	* This method returns the longitude of the position.
	*/
	public PositionRadians getLongitude();
};