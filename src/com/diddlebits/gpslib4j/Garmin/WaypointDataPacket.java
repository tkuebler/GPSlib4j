package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

/** 
* This class encapsulates a Waypoint-packet. The Garmin-protocol contains a huge amount of different
* Waypoint-Packet specifications. Only the one labelled D108 is implemented so far.
*/ 
public class WaypointDataPacket extends GarminPacket implements IWaypoint{
	/**
	* Holds information about which waypoint-format this Garmin-unit uses. The default is 108.
	*/
	protected static int datatypeversion = 108;
	
	/** Class of waypoint. */
	protected short wpt_class; 
	/** Color of waypoint when displayed on the GPS.*/
	protected short color; 
	/** Display options.*/
	protected short dspl; 
	/** Attributes. */
	protected short attr; 
	/** Waypoint symbol. */
	protected int smbl;
	/** Subclass of waypoint */
	protected short[] subclass;
	/** proximity position */
	protected int posn;
	/** Latitude of waypoint. */
	protected PositionDegrees lat;
	/** Longitude of waypoint. */
	protected PositionDegrees lon;
	/** Altitude. */
	protected float alt;
	/** Depth. */
	protected float depth;
	/** Proximity distance in meters. */
	protected float dist;
	/** State.*/
	protected char[] state;
	/** Country code.*/
	protected char[] cc;
	/** Waypoint name. */
	protected String name;
	/** Waypoint comment. */
	protected String comment;
	/** facility name. */
	protected String facility;
	/** City name. */
	protected String city;
	/** Address number */ 
	protected String address;
	/** Intersecting road label.*/ 
	protected String cross_road;
	
	/**
	* Throws a PacketNotRecognizedException if the Waypoint-dataformat is not implemented.
	*/ 
	
	public WaypointDataPacket(int[] p) {
		super(p);
		
		if (getID() != Pid_Wpt_Data) {			
			throw(new PacketNotRecognizedException(Pid_Wpt_Data, getID()));
		}		
		
		switch (datatypeversion) {
			case 108 :
				initD108();
				break;
			default :
				System.out.println("Waypoint-type " + datatypeversion + " not supported.");
				throw(new PacketNotRecognizedException(Pid_Wpt_Data, getID()));
		}
	}
	
	public WaypointDataPacket(GarminPacket p) {
		this( (int[]) p.packet.clone() );
	}
	
	/**
	* Configures this packet as a D108 (Waypoint).
	*/
	private void initD108() {
		wpt_class = readByte(3);
		color = readByte(4);
		dspl  = readByte(5);
		attr = readByte(6);
		//smbl = readSymbol(7)
		//subclass = readByteArray(9,18);
		
		long l;
		l  = readLong(27);		
		// Calculate from semicircles to degrees.
		lat = new PositionDegrees( (l * 180) / Math.pow(2.0d, 31.0d) );
		l = readLong(31);
		lon = new PositionDegrees( (l * 180) / Math.pow(2.0d, 31.0d) );		
		alt = readFloat(35);
		depth = readFloat(39);
		dist = readFloat(43);
		
		name = readNullTerminatedString(49);
	}
		
	
	/**
	* Sets which version of the packet that this class should treat.
	* <br/><b> Note: </b>Setting this value will affect all instances of the class. 
	*/
	public static void setDatatypeVersion(int v) {
		datatypeversion = v;
	}
	
	/**
	* This method returns the latitude of the waypoint.
	*/
	public PositionRadians getLatitude() {
		return lat.convertToRadians();
	}
	
	/**
	* This method returns the longitude of the waypoint.
	*/
	public PositionRadians getLongitude() {
		return lon.convertToRadians();
	}


	/**
	* This method returns the name of the waypoint.
	*/	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "waypoint:[" +
				"class=" + wpt_class +
				", color=" + color +
				", dspl=" + dspl +
				", attr=" + attr +
				", smbl=" + smbl +
				", subclass=" + subclass +
				", posn=" + posn +
				", latitude=" + lat.toString() +
				", longitude=" + lon.toString() +
				", alt=" + alt +
				", dpth" + depth +
				", dist" + dist +
				", name" + name +
				"]";
	}
}