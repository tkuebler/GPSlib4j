package com.diddlebits.gpslib4j;

/**
* 
*/
public interface ILapListener extends ITransferListener {
	/**
	* This method is called whenever a waypoint is received from the GPS.
	*/
	public void lapReceived(ILap tp);
}