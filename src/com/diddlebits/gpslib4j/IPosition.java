package com.diddlebits.gpslib4j;

/**
* This interface is implemented by all packets capable of returning a position.
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
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