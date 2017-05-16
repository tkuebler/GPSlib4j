package com.diddlebits.gpslib4j;

/**
 * Common interface for waypoint packets.
 */
public interface IWaypoint extends IPosition, INamed {
    /**
     * @return The user comment.
     */
    public String getComment();
}