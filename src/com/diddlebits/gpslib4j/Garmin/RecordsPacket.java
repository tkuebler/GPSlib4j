package com.diddlebits.gpslib4j.Garmin;

/**
* This packet is transmitted between devices before a large transfer of data-units, ie. a transfer of waypoints.
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/
public class RecordsPacket extends GarminPacket {
	/** The number of records to come, that this packet announces. */
	protected int number;
	
	public RecordsPacket(int[] p) {
		super(p);
		
		if (getID() != Pid_Records) {
			throw(new PacketNotRecognizedException(Pid_Records, getID()));
		}
		
		if (getDataLength() != 2) {
			throw(new InvalidPacketException(packet, 2));
		}
		
		number = readWord(3);		
	}
	
	public RecordsPacket(GarminPacket p) {
		this(p.packet);
	}
	
	/** Returns the number of records that this packet announces. */
	public int getNumber() {
		return number;
	}
}