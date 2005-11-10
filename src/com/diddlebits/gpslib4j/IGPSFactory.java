package com.diddlebits.gpslib4j;

import java.io.Serializable;

import com.diddlebits.gpslib4j.Garmin.ProtocolNotRecognizedException;
import com.diddlebits.gpslib4j.Garmin.ProtocolNotSupportedException;

/**
 * Since every GPS model has its own special fields, each model has its own
 * implementation for each data kind (waypoint, tracks, route, ...). Therefore,
 * a factory must be used to create data entries.
 * 
 * To get the factory implementation, use the GPS.getFactory() method.
 * 
 * @see GPS#getFactory()
 */
public interface IGPSFactory extends Serializable {
    public ITimeDate createTimeDate() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public ILap createLap() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public IPosition createPosition() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public ITrackpoint createTrackpoint()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public ITrackpointHeader createTrackpointHeader()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public IWaypoint createWaypoint() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public IPosition createPVT() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public IRouteHeader createRouteHeader()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public IRouteWaypoint createRouteWaypoint()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

}