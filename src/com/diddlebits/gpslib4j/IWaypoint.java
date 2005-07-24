package com.diddlebits.gpslib4j;

/**
 * Common interface for waypoint packets.
 */
public interface IWaypoint extends IPosition {
    /**
     * @return The identity of the waypoint.
     */
    public String getIdent();

    /**
     * @return The user comment.
     */
    public String getComment();
}