package com.diddlebits.gpslib4j.Garmin;

/**
* This exception is thrown whenever a method expects one type of packet, but receives another.
* An example is if a PositionDataPacket is initialized with the byte-array containing time-data.
* This exception is a runtime exception because it's assumed that there will in most cases be type-checking
* of packets. 
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/
public class PacketNotRecognizedException extends RuntimeException {
	
	/**
	* expected is the type of packet that the method was expecting. actual is the type of the packet that 
	* it received.
	*/
	public PacketNotRecognizedException(int expected, int actual) {
		super(	"\nPacket initialization error:\n" + "Expected: " + 
				GarminPacket.idToString(expected) + '\n' + 
				"received: " + GarminPacket.idToString(actual) + '\n');
	}
}