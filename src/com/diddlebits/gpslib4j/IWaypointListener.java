package com.diddlebits.gpslib4j;

/**
* 
*/
public interface IWaypointListener extends ITransferListener {
	/**
	* This method is called whenever a waypoint is received from the GPS.
	*/
	public void waypointReceived(IWaypoint wp);
}