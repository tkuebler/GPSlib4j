package com.diddlebits.gpslib4j.Garmin;

/** 
 This interface should be implemented by classes that are interested in getting all the Garmin-packets
 transmitted by a Garmin-GPS. Listener's will receive all packets including ACKs and NAKs. Only exception
 are malformed packets.
 
 This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/

public interface GarminListener {
	/** This method will be called for each packet received from the GPS. */
	public void GarminPacketReceived(GarminPacket p);
}