package com.diddlebits.gpslib4j;

/**
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/
public interface ILapListener extends ITransferListener {
	/**
	* This method is called whenever a waypoint is received from the GPS.
	*/
	public void lapReceived(ILap tp);
}