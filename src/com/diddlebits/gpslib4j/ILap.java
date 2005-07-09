/*
 * Created on Oct 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.diddlebits.gpslib4j;

import java.util.Date;

/**
 * Defines the common methods of lap packets.
 * 
 * @author tkuebler
 */
public interface ILap extends IGPSData {
    public Date getStartTime();

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
