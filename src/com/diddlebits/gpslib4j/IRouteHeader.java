package com.diddlebits.gpslib4j;

/**
 * Common interface of every route header packet.
 */
public interface IRouteHeader extends IGPSData {
    /**
     * @return The identifier of the route
     */
    public String getIdent();
}
