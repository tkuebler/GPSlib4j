package com.diddlebits.gpslib4j;

/**
* This interface is implemented by all packets capable of returning a date.
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/
public interface IDate {
	/** Returns the day of the month. */
	public short getDay();
	
	/** Returns the month. */
	public short getMonth();
	
	/** returns the year. */
	public int getYear();	
};