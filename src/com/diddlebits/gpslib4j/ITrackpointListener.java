package com.diddlebits.gpslib4j;

/**
* 
*/
public interface ITrackpointListener extends ITransferListener {
	/**
	* This method is called whenever a waypoint is received from the GPS.
	*/
	public void trackpointReceived(ITrackpoint tp);
}