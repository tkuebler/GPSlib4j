package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

/**
* This class encapsulates the information of a Garmin-Date-Time-packet.
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/

public class TimeDataPacket extends GarminPacket implements ITime, IDate{
	/** Month (1-12) */
	protected short month; 	
	/** Day (1-31) */
	protected short day;
	/** Year. */
	protected int year;
	/** Hour of the day. */
	protected int hour;
	/** Minute of the hour. */
	protected short minute;
	/** Second of the minute. */
	protected short second;		
	
	/**
	* Treats the packet p as a packet containing Time-data.
	* Throws PacketNotRecognizedException if p is not a Time-packet.
	* Throws InvalidPacketException if the packet contains too little data.
	*/	
	public TimeDataPacket (int[] p) {
		super(p);
		
		if (getID() != Pid_Date_Time_Data) {
			throw(new PacketNotRecognizedException(Pid_Date_Time_Data, getID()));
		}		
		
		if (getDataLength() != 8) {
			throw(new InvalidPacketException(packet, 2));
		}
		
		month = readByte(3);
		day = readByte(4);
		year = readWord(5);
		hour = readWord(7);
		minute = readByte(9);
		second = readByte(10);
	}
	
	/**
	* Treats the packet p as a packet containing Time-data.
	* Throws PacketNotRecognizedException if p is not a Time-packet.
	* Throws InvalidPacketException if the packet contains too little data.
	*/	
	public TimeDataPacket (GarminPacket p) {
		this(p.packet);
	}
	
	/** Returns the day of the month. */
	public short getDay() {
		return day;
	}
	
	/** Returns the month. */
	public short getMonth() {
		return month;
	}
	
	/** returns the year. */
	public int getYear() {
		return year;
	}
	
	/** Returns the hour of the day. */
	public int getHours() {
		return hour;
	}
	
	/** Returns the minute of the hour. */
	public short getMinutes() {
		return minute;
	}
	
	/** Returns the second of the minute.*/
	public short getSeconds() {
		return second;
	}
	
	/**
	* Returns the value of this packet in a human-readable format.
	*/
	public String toString() {
		StringBuffer res = new StringBuffer();
		if (hour < 10)
			res.append("0");
		res.append(hour +":");
		if (minute < 10)
			res.append("0");
		res.append(minute +":");
		if (second < 10)
			res.append("0");			
		res.append(second + " on ");
		res.append(day + "/");
		res.append(month + "-" + year);
		return res.toString();
	}
}