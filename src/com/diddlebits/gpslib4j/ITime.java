package com.diddlebits.gpslib4j;

/**
* This interface is implemented by all packets capable of returning the time of day.
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/
public interface ITime {
	/** Returns the hour of the day. */
	public int getHours();
	
	/** Returns the minute of the hour. */
	public short getMinutes();
	
	/** Returns the second of the minute. */
	public short getSeconds();	
};