package com.diddlebits.gpslib4j;

public interface INamed extends IGPSData {
    /**
     * @return The identity of the waypoint.
     */
    public String getIdent();
}
