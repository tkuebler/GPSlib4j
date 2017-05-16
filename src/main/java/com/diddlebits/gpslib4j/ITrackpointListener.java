package com.diddlebits.gpslib4j;

public interface ITrackpointListener extends ITransferListener {
    /**
     * This method is called whenever a trackpoint header is received from the
     * GPS.
     */
    public void trackpointHeaderReceived(ITrackpointHeader tp);

    /**
     * This method is called whenever a trackpoint is received from the GPS.
     */
    public void trackpointReceived(ITrackpoint tp);
}