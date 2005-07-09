package com.diddlebits.gpslib4j.Garmin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.diddlebits.gpslib4j.*;

/**
 * Implementation of the GPS interface for Garmin.
 */
public class GarminGPS extends GPS implements Runnable {
    /** Communication input stream */
    GarminInputStream input;

    /** Communication output stream */
    GarminOutputStream output;

    /** A human-readable description of the GPS-unit. */
    protected String description;

    /** The listening thread */
    Thread listener;

    /**
     * The listening thread will be active as long as this variable remains
     * true.
     */
    protected boolean active;

    /** What is the current request */
    int currentTask = -1;

    /** The number of records we are going to receive */
    int records = 0;

    /** A vector containing references to all the GarminListeners. */
    protected Vector GarminListeners;

    public GarminGPS(BufferedInputStream i, BufferedOutputStream o) {
        input = new GarminInputStream(i);
        output = new GarminOutputStream(o);
        listener = new Thread(this);
        listener.start();
        active = true;
        GarminListeners = new Vector();

        // Request product information.
        try {
            // abort any current transfer
            // output.write(GarminPacket.createCommandPacket(GarminPacket.Cmnd_Abort_Transfer));
            // request product info
            output.write(GarminRawPacket.createBasicPacket(
                    GarminRawPacket.Pid_Product_Rqst, new int[] {}));
        } catch (IOException e) {
        }
    }

    /**
     * Adds the specified GarminListener to receive all packets sent from the
     * GPS.
     */
    public void addGarminListener(GarminListener l) {
        // Only allow a listener to be registered once.
        if (GarminListeners.contains(l))
            return;

        GarminListeners.add(l);
    }

    /**
     * Removes the specified GarminListener from the list of listeners.
     */
    public void removeGarminListener(GarminListener l) {
        while (GarminListeners.removeElement(l)) {
        }
    }

    /**
     * Goes through the list of GarminListeners and transmits p to them.
     */
    protected void fireGarminPacket(GarminPacket p) {
        for (int i = 0; i < GarminListeners.size(); i++) {
            ((GarminListener) GarminListeners.elementAt(i))
                    .garminPacketReceived(p);
        }
    }

    /** This method is listening for input from the GPS. */
    public void run() {
        GarminRawPacket pack = null;

        while (active) {
            // try to get something from the GPS
            try {
                if (input.available() == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
                pack = new GarminRawPacket(input.readPacket());
            } catch (IOException e) {
                active = false;
                return;
            } catch (InvalidPacketException e) {
                // Send back a NAK-packet.
                try {
                    output.write(GarminRawPacket.createBasicPacket(
                            GarminRawPacket.Pid_Nak_Byte, new int[] {
                                    pack.getID(), 0 }));
                } catch (IOException ex) {
                    active = false;
                    return;
                }
            }

            // notifiy the raw packet listeners
            fireGarminPacket(pack);

            // parse and send the packet to the listeners
            try {
                int answer = distribute(pack);
                if (answer >= 0) {
                    // Send back a packet.
                    try {
                        output.write(GarminRawPacket.createBasicPacket(answer,
                                new int[] { pack.getID(), 0 }));
                    } catch (IOException e) {
                        active = false;
                    }
                }
            } catch (Exception e) {
                try {
                    output.write(GarminRawPacket.createBasicPacket(
                            GarminRawPacket.Pid_Nak_Byte, new int[] {
                                    pack.getID(), 0 }));
                } catch (IOException io) {
                    active = false;
                }
                // notify the listeners about the error
                fireError(e);
            }
        } // End of while
    }

    /**
     * This method is used to identify the type of packet received, parse it and
     * distribute it to the correct listeners.
     * 
     * @return True if an acknoledge is requested
     */
    protected int distribute(GarminRawPacket p)
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        switch (p.getID()) {
        case GarminRawPacket.Pid_Position_Data:
            firePositionData(GarminFactory.Get().createPosition(p));
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Date_Time_Data:
            ITimeDate time = GarminFactory.Get().createTimeDate(p);
            fireTimeDateData(time);
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Pvt_Data:
            IPosition pvtp = GarminFactory.Get().createPVT(p);
            firePositionData(pvtp);
            return -1;
        case GarminRawPacket.Pid_Records:
            records = GarminFactory.Get().createRecords(p).getNumber();
            fireTransferStart(records);
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Wpt_Data:
            fireWaypointData(GarminFactory.Get().createWaypoint(p));
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Trk_data:
            fireTrackpointData(GarminFactory.Get().createTrackpoint(p));
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Trk_Hdr:
            fireTrackpointHeader(GarminFactory.Get().createTrackpointHeader(p));
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Rte_Hdr:
            fireRouteHeader(GarminFactory.Get().createRouteHeader(p));
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Rte_Wpt_Data:
            fireRouteWaypoint(GarminFactory.Get().createRouteWaypoint(p));
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Lap:
            fireLapData(GarminFactory.Get().createLap(p));
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Xfer_Cmplt:
            System.out.println("Transfer Complete");
            currentTask = -1;
            fireTransferComplete();
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Product_Data:
            ProductDataPacket pp = GarminFactory.Get()
                    .createProductDataAndInitFromIt(p);
            description = pp.getDescription();
            description += "\nSoftware version: " + pp.getSWVersion();
            description += "\nProduct ID: " + pp.getProductID();
            fireTransferComplete();
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Protocol_Array:
            description += "\nProtocols supported:\n";
            description += (GarminFactory.Get()
                    .createProtocolArrayAndInitFromIt(p)).toString();
            return GarminRawPacket.Pid_Ack_Byte;
        case GarminRawPacket.Pid_Ack_Byte:
            // ack packet
            // System.out.println("ACK received");
            if (currentTask >= 0) {
                // case of empty data type
                fireTransferComplete();
            }
            return -1;
        default:
            System.out.println("Unknown packet type received: id=" + p.getID()
                    + ", data=" + p.getRawPacket());
            throw new ProtocolNotRecognizedException('C', p.getID());
        }
    }

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public void requestPosition() throws FeatureNotSupportedException,
            IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferPosn);
        output.write(GarminRawPacket.createCommandPacket(currentTask));
    }

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public void requestTime() throws FeatureNotSupportedException, IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferTime);
        output.write(GarminRawPacket.createCommandPacket(currentTask));
    }

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public void requestDate() throws FeatureNotSupportedException, IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferTime);
        output.write(GarminRawPacket.createCommandPacket(currentTask));
    }

    /**
     * Asks the GPS to transmit all the waypoints in it's memory. The result
     * will be returned through the WaypointListener-interface.
     */
    public void requestWaypoints() throws FeatureNotSupportedException,
            IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferWpt);
        output.write(GarminRawPacket.createCommandPacket(currentTask));
    }

    /**
     * Asks the GPS to transmit all the routes in it's memory. The result will
     * be returned through the WaypointListener-interface.
     */
    public void requestRoutes() throws FeatureNotSupportedException,
            IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferRte);
        output.write(GarminRawPacket.createCommandPacket(currentTask));
    }

    /**
     * Asks the GPS to transmit all the tracks in it's memory. The result will
     * be returned through the TrackListener-interface.
     */
    public void requestTracks() throws FeatureNotSupportedException,
            IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferTrk);
        output.write(GarminRawPacket.createCommandPacket(currentTask));
    }

    /**
     * Asks the GPS to transmit all the laps in it's memory. The result will be
     * returned through the LapsListener-interface.
     */
    public void requestLaps() throws FeatureNotSupportedException, IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferLaps);
        output.write(GarminRawPacket.createCommandPacket(currentTask));
    }

    /**
     * Asks the GPS to either start or stop transmitting data periodically.
     * <br/> The data will be that which is accessible through the
     * IGPSlistener-interface. Throws a FeatureNotSupportException if it isn't
     * possible to do this on the GPS.
     */
    public void setAutoTransmit(boolean t) throws FeatureNotSupportedException,
            IOException {
        if (t) {
            currentTask = GarminFactory.Get().getCommandId(
                    GarminFactory.CmndStartPvtData);
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } else {
            currentTask = GarminFactory.Get().getCommandId(
                    GarminFactory.CmndStopPvtData);
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        }
    }

    /**
     * Stops communication with GPS.<br/> Most likely, your program won't be
     * able to shut down before you've called this method. If b is set to true,
     * the GPS will be asked to turn off. If b is set to false, the GPS will
     * remain turned on.
     */
    public void shutdown(boolean b) throws FeatureNotSupportedException,
            IOException {
        if (b) {
            currentTask = GarminFactory.Get().getCommandId(
                    GarminFactory.CmndTurnOffPwr);
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        }
        active = false;
    }

    /**
     * Returns a string telling the brand of the Garmin-gps, software version
     * and the protocols supported.
     */
    public String getDescription() {
        return description;
    }
}