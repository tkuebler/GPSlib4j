/*
 * Created on Oct 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.diddlebits.gpslib4j;

/**
 * @author tkuebler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
 */
public interface ILap {
	public long getStartTime();
	/** total time */
	public long getTotalTime();
	/** total_distance */
	public float getTotalDistance();
	/** begin point */
	public Position getStartPosition();
	/** end point */
	public Position getEndPosition();
	/** calories */
	public int getCalories();
	/** track index */
	public short getTrackIndex();

}
