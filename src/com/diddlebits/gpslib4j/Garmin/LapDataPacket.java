package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

/** 
* This class encapsulates a lap-packet. 
*/ 
public class LapDataPacket extends GarminPacket implements ILap {
	/**
	* Holds information about which lap-format this Garmin-unit uses. The default is 108.
	*/
	protected static int datatypeversion = 906;
	protected boolean bound = false;
	
	/** start time */
	protected long start_time;
	/** total time */
	protected long total_time;
	/** total_distance */
	protected float total_distance;
	/** begin point */
	Position start_position;
	/** end point */
	Position end_position;
	/** calories */
	protected int calories;
	/** track index */
	protected short track_index;
	/** unused */
	protected short unused;
	
	/**
	* Throws a PacketNotRecognizedException if the lap-dataformat is not implemented.
	*/ 
	
	public LapDataPacket(int[] p) {
		super(p);
		
		if (getID() != Pid_Lap) {			
			throw(new PacketNotRecognizedException(Pid_Lap, getID()));
		}		
		
		switch (datatypeversion) {
			case 906 :
				initD906();
				break;
			default :
				System.out.println("lap-type " + datatypeversion + " not supported.");
				throw(new PacketNotRecognizedException(Pid_Lap, getID()));
		}
	}
	
	public LapDataPacket(GarminPacket p) {
		this( (int[]) p.packet.clone() );
	}
	
	/**
	* Configures and binds the data as a D108 (lap).
	*/
	private void initD906() {
		start_time = readLong();	
		total_time = readLong();	
		total_distance = readFloat(); 
		start_position = readPosition(); 
		end_position = readPosition(); 
		calories = readWord();
		track_index = readByte(); 
		unused = readByte();
		bound = true;
	}
		
	
	/**
	* Sets which version of the packet that this class should treat.
	* <br/><b> Note: </b>Setting this value will affect all instances of the class. 
	*/
	public static void setDatatypeVersion(int v) {
		datatypeversion = v;
	}
	
	public String toString() {
		return "lap:[" +
				"start_time=" + start_time +
				", total_time=" + total_time +
				", total_distance=" + total_distance +
				", start_position=" + start_position.toString() +
				", end_position=" + end_position.toString() +
				", calories=" + calories +
				", track_index=" + track_index +
				"]";
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.ILap#getStartTime()
	 */
	public long getStartTime() {
		return start_time;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.ILap#getTotalTime()
	 */
	public long getTotalTime() {
		return total_time;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.ILap#getTotalDistance()
	 */
	public float getTotalDistance() {
		return total_distance;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.ILap#getStartPosition()
	 */
	public Position getStartPosition() {
		return start_position;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.ILap#getEndPosition()
	 */
	public Position getEndPosition() {
		return end_position;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.ILap#getCalaries()
	 */
	public int getCalories() {
		return calories;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.ILap#getTrackIndex()
	 */
	public short getTrackIndex() {
		return track_index;
	}

}