package com.diddlebits.gpslib4j;

/**
* This interface is implemented by all packets capable of returning the time of day.
*/
public interface IProductData {
	/** Returns a description of GPS unit */
	public String getDescription();
	
	/** Returns the software version. */
	public String getVersion();
	
	/** Returns the product ID */
	public String getId();
};