package com.diddlebits.gpslib4j;

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
public interface IGPSFactory {

    public abstract ITimeDate createTimeDate()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract ILap createLap() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract IPosition createPosition()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract ITrackpoint createTrackpoint()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract ITrackpointHeader createTrackpointHeader()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract IWaypoint createWaypoint()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract IPosition createPVT()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract IRouteHeader createRouteHeader()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

    public abstract IRouteWaypoint createRouteWaypoint()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException;

}