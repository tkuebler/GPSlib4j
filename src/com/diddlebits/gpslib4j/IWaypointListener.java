package com.diddlebits.gpslib4j;

/**
 * The methods in this interface are used whenever the GPS should transfer
 * waypoint.
 */
public interface IWaypointListener extends ITransferListener {
    /**
     * This method is called whenever a waypoint is received from the GPS.
     */
    public void waypointReceived(IWaypoint wp);
}