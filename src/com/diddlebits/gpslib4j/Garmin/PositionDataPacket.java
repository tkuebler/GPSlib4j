package com.diddlebits.gpslib4j.Garmin;

/** This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/). */

import com.diddlebits.gpslib4j.*;

public class PositionDataPacket extends GarminPacket implements IPosition{
	private PositionRadians lat, lon;
	
	/**
	* Treats the packet p as a packet containing position-data.
	* Throws PacketNotRecognizedException if p is not a position-data-packet.
	* Throws InvalidPacketException if the packet contains too little data.
	*/
	public PositionDataPacket(int[] p) {
		super(p);
		if (getID() != Pid_Position_Data) {
			throw(new PacketNotRecognizedException(Pid_Position_Data, getID()));
		}
		
		if (getDataLength() != 16) {
			throw(new InvalidPacketException(packet, 2));
		}
		
		lat = new PositionRadians(readDouble(3));
		lon = new PositionRadians(readDouble(11));
	}	
	
	/** 
	* This method is a copy-constructor allowing to "upgrade" a GarminPacket to a PositionPacket.
	* Throws PacketNotRecognizedException if p is not a position-data-packet.
	*/	
	public PositionDataPacket(GarminPacket p) {
		this( p.packet );		
	}
	
	/**
	* This method returns the latitude of the position.
	*/
	public PositionRadians getLatitude() {
		return lat;
	}
	
	/**
	* This method returns the longitude of the position.
	*/
	public PositionRadians getLongitude() {
		return lon;
	}
	
	/**
	* Returns a String containing the position in a human-readable format.
	*/
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("\nPosition:");
		res.append("\nLatitude: " + lat.toString());
		res.append("\nLongitude: " + lon.toString());		
		return res.toString();
	}
}