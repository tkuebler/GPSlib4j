package com.diddlebits.gpslib4j;

/**
 * This interface is used to receive notification each time the GPS transmits
 * one of the common data, ie. position, time and date.
 * <ul>
 * <li> The GPS does not necessarily transmit these things periodially by
 * itself! Some GPS-units needs a request before transmitting anything. Use the
 * method GPS.setAutoTransmission(true) if you want the GPS to periodically send
 * this data.
 * <li> Don't perform any long calculations or big operations in these methods.
 * They're called by a dispatching thread, and putting it to too much work will
 * slow performance on the communication with the GPS.
 * </ul>
 */

public interface IGPSlistener {
    /** Invoked when the GPS transmits date-data. */
    public void timeDateReceived(ITimeDate d);

    /** Invoked when the GPS transmits position-data. */
    public void positionReceived(IPosition pos);
}