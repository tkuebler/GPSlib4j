package com.diddlebits.gpslib4j.Garmin;
import com.diddlebits.gpslib4j.*;
/**
* This class encapsulates the PVT (Position, velocity and time) packet.
* After receiving a Cmnd_Start_Pvt-packet, the GPS will continually transmit
* packets of the PVT-type. 
*/ 

public class PVTDataPacket extends GarminPacket implements IPosition, ITime { 

	protected float alt; 			// Altitude
	protected float epe; 			// Estimated position error
	protected float eph; 			// Horizontal epe
	protected float epv; 			// Vertical epe
	protected int fix;			 	// Position fix
	protected double  tow;		 	// Time of week (seconds).
	protected PositionRadians lat;	// Latitude
	protected PositionRadians lon; 	// Longitude
	protected float veast; 		 	// Velocity east.
	protected float vnorth;		 	// Velocity north.
	protected float vup;			// Velocity up.
	protected float msl_hght;		// 
	protected int leap_scnds;		// Time difference between GPS and GMT (UTC)
	protected long wn_days;		 	// Week number days. 
	
	/**
	* Treats the packet p as a packet containing PVT-data.
	* Throws PacketNotRecognizedException if p is not a PVT-packet.
	* Throws InvalidPacketException if the packet contains too little data.
	*/	
	public PVTDataPacket(int[] p) {
		super(p);
		
		if (getID() != Pid_Pvt_Data) {
			throw(new PacketNotRecognizedException(Pid_Pvt_Data, getID()));
		}
		
		if (getDataLength() != 64) {
			throw(new InvalidPacketException(packet, 2));
		}
				
		alt = readFloat(3);
		epe = readFloat(7);
		eph = readFloat(11);
		epv = readFloat(15);
		fix = readWord(19);
		tow = readDouble(21);
		lat = new PositionRadians( readDouble(29));
		lon = new PositionRadians( readDouble(37));
		veast = readFloat(45);
		vnorth = readFloat(49);
		vup = readFloat(53);
		msl_hght = readFloat(57);
		leap_scnds = readWord(61);
		wn_days = readLong(63);
	}
	
	/** 
	* This method is a copy-constructor allowing to "upgrade" a GarminPacket to a PVTDataPacket.
	* Throws PacketNotRecognizedException if p is not a PVT-data-packet.
	*/	
	public PVTDataPacket(GarminPacket p) {
		this( p.packet );		
	}
	
	
	/** Returns the hour of the day. */
	public int getHours() {
		int hour = (int) tow;
		hour = hour % (24 * 60 * 60); // Remove all preceding days.
		hour = hour / 3600;
		return hour;
	}
	
	/** Returns the minute of the hour. */
	public short getMinutes() {
		int minute = (int) tow;
		minute = minute % (24 * 60 * 60); // Remove all preceding days.
		minute = minute / 60;
		minute = minute % 60;
		return (short) minute;
	}
	
	/** Returns the second of the minute. */
	public short getSeconds() {
		return (short) (tow % 60);
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
}