package com.diddlebits.gpslib4j;

/**
 * Defines the callback methods when a lap is received from the GPS.
 */
public interface ILapListener extends ITransferListener {
    /**
     * This method is called whenever a lap is received from the GPS.
     */
    public void lapReceived(ILap tp);
}