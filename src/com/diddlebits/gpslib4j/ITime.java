package com.diddlebits.gpslib4j;

/**
* This interface is implemented by all packets capable of returning the time of day.
*/
public interface ITime {
	/** Returns the hour of the day. */
	public int getHours();
	
	/** Returns the minute of the hour. */
	public short getMinutes();
	
	/** Returns the second of the minute. */
	public short getSeconds();	
};