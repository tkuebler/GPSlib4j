package com.diddlebits.gpslib4j.Garmin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.diddlebits.gpslib4j.FeatureNotSupportedException;
import com.diddlebits.gpslib4j.GPS;
import com.diddlebits.gpslib4j.IGPSFactory;
import com.diddlebits.gpslib4j.ILap;
import com.diddlebits.gpslib4j.IPosition;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.IRouteWaypoint;
import com.diddlebits.gpslib4j.ITimeDate;
import com.diddlebits.gpslib4j.ITrackpoint;
import com.diddlebits.gpslib4j.ITrackpointHeader;
import com.diddlebits.gpslib4j.IWaypoint;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * Implementation of the GPS interface for Garmin.
 */
public class GarminGPS extends GPS {
    /** A human-readable description of the GPS-unit. */
    protected String description;

    /** What is the current request */
    protected int currentTask = -1;

    /** The number of records we are going to receive */
    protected int records = 0;

    /** A vector containing references to all the GarminListeners. */
    protected Vector GarminListeners;

    /** Communication input stream */
    protected GarminInputStream input;

    /** Communication output stream */
    protected GarminOutputStream output;

    public GarminGPS() {
        super();
        GarminListeners = new Vector();
    }

    public void connectGPS(BufferedInputStream i, BufferedOutputStream o)
            throws FeatureNotSupportedException {
        if (input != null || output != null) {
            throw new FeatureNotSupportedException();
        }
        input = new GarminInputStream(i);
        output = new GarminOutputStream(o);

        super.connectGPS(i, o);

        // Request product information.
        try {
            // abort any current transfer
            // output.write(GarminPacket.createCommandPacket(GarminPacket.Cmnd_Abort_Transfer));
            // request product info
            output.write(GarminRawPacket.createBasicPacket(
                    GarminRawPacket.Pid_Product_Rqst, new int[] {}));
        } catch (IOException e) {
        } catch (InvalidPacketException e) {
            fireError(e);
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

    /**
     * This method is used to identify the type of packet received, parse it and
     * distribute it to the correct listeners.
     * 
     * @return True if an acknoledge is requested
     * @throws InvalidFieldValue
     * @throws PacketNotRecognizedException
     * @throws InvalidPacketException
     */
    protected int distribute(GarminRawPacket p)
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException, PacketNotRecognizedException,
            InvalidFieldValue, InvalidPacketException {
        switch (p.getPID()) {
        case GarminRawPacket.Pid_Position_Data: {
            IPosition result = GarminFactory.Get().createPosition();
            ((GarminPacket) result).initFromRawPacket(p);
            firePositionData(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Date_Time_Data: {
            ITimeDate result = GarminFactory.Get().createTimeDate();
            ((GarminPacket) result).initFromRawPacket(p);
            fireTimeDateData(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Pvt_Data: {
            IPosition result = GarminFactory.Get().createPVT();
            ((GarminPacket) result).initFromRawPacket(p);
            firePositionData(result);
            return -1;
        }

        case GarminRawPacket.Pid_Records: {
            RecordsPacket result = GarminFactory.Get().createRecords();
            result.initFromRawPacket(p);
            records = result.getNumber();
            fireTransferStart(records);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Wpt_Data: {
            IWaypoint result = GarminFactory.Get().createWaypoint();
            ((GarminPacket) result).initFromRawPacket(p);
            fireWaypointData(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Trk_data: {
            ITrackpoint result = GarminFactory.Get().createTrackpoint();
            ((GarminPacket) result).initFromRawPacket(p);
            fireTrackpointData(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Trk_Hdr: {
            ITrackpointHeader result = GarminFactory.Get()
                    .createTrackpointHeader();
            ((GarminPacket) result).initFromRawPacket(p);
            fireTrackpointHeader(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Rte_Hdr: {
            IRouteHeader result = GarminFactory.Get().createRouteHeader();
            ((GarminPacket) result).initFromRawPacket(p);
            fireRouteHeader(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Rte_Wpt_Data: {
            IRouteWaypoint result = GarminFactory.Get().createRouteWaypoint();
            ((GarminPacket) result).initFromRawPacket(p);
            fireRouteWaypoint(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Lap: {
            ILap result = GarminFactory.Get().createLap();
            ((GarminPacket) result).initFromRawPacket(p);
            fireLapData(result);
            return GarminRawPacket.Pid_Ack_Byte;
        }
        case GarminRawPacket.Pid_Xfer_Cmplt:
            currentTask = -1;
            fireTransferComplete();
            return GarminRawPacket.Pid_Ack_Byte;

        case GarminRawPacket.Pid_Product_Data: {
            ProductDataPacket pp = GarminFactory.Get()
                    .createProductDataAndInitFromIt(p);
            description = pp.getDescription();
            description += "\nSoftware version: " + pp.getSWVersion();
            description += "\nProduct ID: " + pp.getProductID();
            if (!GarminFactory.isWaitingForProtocolData()) {
                // we don't expect protocol array => init is done
                fireTransferComplete();
            }
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Protocol_Array: {
            description += "\nProtocols supported:\n";
            description += (GarminFactory.Get()
                    .createProtocolArrayAndInitFromIt(p)).toString();
            fireTransferComplete();
            return GarminRawPacket.Pid_Ack_Byte;
        }

        case GarminRawPacket.Pid_Ack_Byte: {
            // ack packet
            if (currentTask >= 0) {
                // TODO: find a way to make the difference between an ACK for a
                // command with data following and a ack from a command without
                // data. My GPSMAP 60 is just answering with ACK when I
                // requestLap...
                // fireTransferComplete();
            }
            return -1;
        }

        default:
            System.out.println("Unknown packet type received: id=" + p.getPID()
                    + ", data=" + p.getRawPacket());
            throw new ProtocolNotRecognizedException('C', p.getPID());
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
        try {
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
        }
    }

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public void requestTime() throws FeatureNotSupportedException, IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferTime);
        try {
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
        }
    }

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public void requestDate() throws FeatureNotSupportedException, IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferTime);
        try {
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
        }
    }

    /**
     * Asks the GPS to transmit all the waypoints in it's memory. The result
     * will be returned through the WaypointListener-interface.
     */
    public void requestWaypoints() throws FeatureNotSupportedException,
            IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferWpt);
        try {
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
        }
    }

    /**
     * Asks the GPS to transmit all the routes in it's memory. The result will
     * be returned through the WaypointListener-interface.
     */
    public void requestRoutes() throws FeatureNotSupportedException,
            IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferRte);
        try {
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
        }
    }

    /**
     * Asks the GPS to transmit all the tracks in it's memory. The result will
     * be returned through the TrackListener-interface.
     */
    public void requestTracks() throws FeatureNotSupportedException,
            IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferTrk);
        try {
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
        }
    }

    /**
     * Asks the GPS to transmit all the laps in it's memory. The result will be
     * returned through the LapsListener-interface.
     */
    public void requestLaps() throws FeatureNotSupportedException, IOException {
        currentTask = GarminFactory.Get().getCommandId(
                GarminFactory.CmndTransferLaps);
        try {
            output.write(GarminRawPacket.createCommandPacket(currentTask));
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
        }
    }

    /**
     * Asks the GPS to either start or stop transmitting data periodically.
     * <br/> The data will be that which is accessible through the
     * IGPSlistener-interface. Throws a FeatureNotSupportException if it isn't
     * possible to do this on the GPS.
     */
    public void setAutoTransmit(boolean t) throws FeatureNotSupportedException,
            IOException {
        try {
            if (t) {
                currentTask = GarminFactory.Get().getCommandId(
                        GarminFactory.CmndStartPvtData);
                output.write(GarminRawPacket.createCommandPacket(currentTask));
            } else {
                currentTask = GarminFactory.Get().getCommandId(
                        GarminFactory.CmndStopPvtData);
                output.write(GarminRawPacket.createCommandPacket(currentTask));
            }
        } catch (InvalidPacketException e) {
            throw new FeatureNotSupportedException();
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
            try {
                output.write(GarminRawPacket.createCommandPacket(currentTask));
            } catch (InvalidPacketException e) {
                throw new FeatureNotSupportedException();
            }
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

    public IGPSFactory getFactory() {
        return GarminFactory.Get();
    }

    protected void sendNack() throws IOException, InvalidPacketException {
        output.write(GarminRawPacket.createBasicPacket(
                GarminRawPacket.Pid_Nak_Byte, new int[] {
                        0/* TODO: what value to put here? */, 0 }));
    }

    protected void handleInput() throws InvalidPacketException, IOException {
        GarminRawPacket pack = new GarminRawPacket(input.readPacket());
        // notifiy the raw packet listeners
        fireGarminPacket(pack);

        // parse and send the packet to the listeners
        try {
            int answer = distribute(pack);
            if (answer >= 0) {
                // Send back a packet.
                try {
                    output.write(GarminRawPacket.createBasicPacket(answer,
                            new int[] { pack.getPID(), 0 }));
                } catch (IOException e) {
                    active = false;
                }
            }
        } catch (Exception e) {
            // notify the listeners about the error
            fireError(e);

            try {
                output.write(GarminRawPacket.createBasicPacket(
                        GarminRawPacket.Pid_Nak_Byte, new int[] {
                                pack.getPID(), 0 }));
            } catch (IOException io) {
                active = false;
                fireError(io);
            } catch (InvalidPacketException ex) {
                fireError(ex);
            }
        }
    }

    protected int inputAvailable() throws IOException {
        return input.available();
    }

    protected void disconnectGPS() {
        try {
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        input = null;
        output = null;
    }

    public void sendWaypoints(Collection waypoints)
            throws InvalidPacketException, IOException,
            ProtocolNotRecognizedException, ProtocolNotSupportedException,
            InvalidFieldValue {
        // first pass to check all the way point are of good type
        Iterator it = waypoints.iterator();
        Class wantedClass = GarminFactory.Get().createWaypoint().getClass();
        while (it.hasNext()) {
            Object curObj = it.next();

            if (!wantedClass.isAssignableFrom(curObj.getClass())) {
                throw new IOException("Invalid class. Supported: "
                        + wantedClass.getCanonicalName());
            }
        }

        // now we build the packet list
        ArrayList toSend = new ArrayList(waypoints.size() + 2);
        RecordsPacket records = new RecordsPacket(waypoints.size());
        toSend.add(records);
        toSend.addAll(waypoints);
        XferCompletePacket complete = new XferCompletePacket(
                GarminFactory.CmndTransferWpt);
        toSend.add(complete);
        sendPackets(toSend);
    }

    private synchronized void sendPackets(Collection toSend) throws InvalidFieldValue,
            InvalidPacketException, IOException {
        Iterator it = toSend.iterator();
        while (it.hasNext()) {
            GarminPacket cur = (GarminPacket) it.next();
            GarminRawPacket raw = cur.createRawPacket();
            sendOnePacket(raw);
        }
    }

    private void sendOnePacket(GarminRawPacket raw) throws IOException,
            InvalidPacketException {
        output.write(raw);
        GarminRawPacket answer = new GarminRawPacket(input.readPacket());
        switch (answer.getPID()) {
        case GarminRawPacket.Pid_Ack_Byte:
            // happy camper
            break;

        case GarminRawPacket.Pid_Nak_Byte:
            // TODO: maybe we should try to recover...
            throw new IOException("NACK received from the GPS");

        default:
            throw new IOException("Received an unknown packet from the GPS: "
                    + answer.getPID());
        }
    }

    public void sendTrack(ITrackpointHeader header, Collection points)
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException, IOException, InvalidFieldValue,
            InvalidPacketException {
        // first pass to check all the objects are of good type
        Class wantedClassHeader = GarminFactory.Get().createTrackpointHeader()
                .getClass();
        if (!wantedClassHeader.isAssignableFrom(header.getClass())) {
            throw new IOException("Invalid class. Supported: "
                    + wantedClassHeader.getCanonicalName());
        }

        Iterator it = points.iterator();
        Class wantedClassPoint = GarminFactory.Get().createTrackpoint()
                .getClass();
        while (it.hasNext()) {
            Object curObj = it.next();

            if (!wantedClassPoint.isAssignableFrom(curObj.getClass())) {
                throw new IOException("Invalid class. Supported: "
                        + wantedClassPoint.getCanonicalName());
            }
        }

        // now we build the packet list
        ArrayList toSend = new ArrayList(2 + points.size()
                + (header != null ? 1 : 0));
        RecordsPacket records = new RecordsPacket(points.size()
                + (header != null ? 1 : 0));
        toSend.add(records);
        if (header != null) {
            toSend.add(header);
        }
        toSend.addAll(points);
        XferCompletePacket complete = new XferCompletePacket(
                GarminFactory.CmndTransferWpt);
        toSend.add(complete);
        sendPackets(toSend);
    }
}