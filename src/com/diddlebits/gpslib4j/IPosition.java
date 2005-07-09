package com.diddlebits.gpslib4j;

/**
 * This interface is implemented by all packets capable of returning a position.
 */
public interface IPosition extends IGPSData {
    /**
     * This method returns the latitude of the position.
     */
    public Position getPosition();
};