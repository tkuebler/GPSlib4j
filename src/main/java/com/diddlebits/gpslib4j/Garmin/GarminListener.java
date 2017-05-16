package com.diddlebits.gpslib4j.Garmin;

/**
 * This interface should be implemented by classes that are interested in
 * getting all the Garmin-packets transmitted by a Garmin-GPS. Listener's will
 * receive all packets including ACKs and NAKs. Only exception are malformed
 * packets.
 */

public interface GarminListener {
    /** This method will be called for each packet received from the GPS. */
    public void garminPacketReceived(GarminPacket p);
}