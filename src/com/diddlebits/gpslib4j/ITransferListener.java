package com.diddlebits.gpslib4j;

/**
* The methods in this interface are used whenever the GPS should transfer a series of data. 
*/
public interface ITransferListener {
	/** 
	* This method is called when a transfer is initiated. <br/>
	* Number is the amount of data that will be transferred, ie. the amount of waypoints.
	* If it's not possible to tell how much data that will be transferred, number will be -1.
	*/
	public void transferStarted(int number);
	
	/**
	* This method is called when the transfer is complete.
	*/
	public void transferComplete();
}