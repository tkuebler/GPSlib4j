package com.diddlebits.gpslib4j;

/**
* This interface is implemented by all packets capable of returning the time of day.
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/
public interface IProductData {
	/** Returns a description of GPS unit */
	public String getDescription();
	
	/** Returns the software version. */
	public String getVersion();
	
	/** Returns the product ID */
	public String getId();
};