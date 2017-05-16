package com.diddlebits.gpslib4j;

/**
 * Defines the callback methods when route data is received from the GPS.
 */
public interface IRouteListener extends ITransferListener {
    /**
     * This method is called whenever a route header is received from the GPS.
     */
    public void routeHeaderReceived(IRouteHeader tp);

    /**
     * This method is called whenever a route waypoint is received from the GPS.
     */
    public void routeWaypointReceived(IRouteWaypoint tp);
}