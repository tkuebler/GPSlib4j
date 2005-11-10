package com.diddlebits.gpslib4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.diddlebits.gpslib4j.Garmin.GarminGPS;
import com.diddlebits.gpslib4j.Garmin.InvalidPacketException;
import com.diddlebits.gpslib4j.Garmin.ProtocolNotRecognizedException;
import com.diddlebits.gpslib4j.Garmin.ProtocolNotSupportedException;

/**
 * This is the abstract base-class that encapsulates the functionality of a
 * generic GPS-unit:
 * <ul>
 * <li>the ability to register listeners</li>
 * <li>a way to get the list of GPS supported</li>
 * <li>a factory to build a concrete object</li>
 * <li>the interface to request data from the GPS</li>
 * </ul>
 */
public abstract class GPS implements Runnable {

    protected HashMap zListeners;

    /** The listening thread */
    protected Thread listener;

    /**
     * The listening thread will be active as long as this variable remains
     * true.
     */
    protected boolean active;

    static protected Vector Brands;

    /**
     * @return The list of supported GPS brands.
     */
    public static Vector GetBrands() {
        if (Brands == null) {
            Brands = new Vector();
            Brands.add(new String("GARMIN"));
        }
        return Brands;
    }

    /**
     * Factory method to create a GPS interface with a real GPS connected.
     * 
     * @param brand
     *            One of the string returned by GetBrand()
     * @param i
     *            The input stream to use to communicate with the GPS
     * @param o
     *            The output stream to use to communication with the GPS
     * @param toNotifyWhenInit
     *            The object to notify when the connection is done
     * @return The concrete GPS interface.
     * @throws FeatureNotSupportedException
     *             If the brand is unknown.
     */
    public static GPS CreateInterface(String brand, BufferedInputStream i,
            BufferedOutputStream o, ITransferListener toNotifyWhenInit)
            throws FeatureNotSupportedException {
        GPS result;
        if (brand.toUpperCase().compareTo("GARMIN") == 0) {
            result = new GarminGPS();
        } else {
            throw new FeatureNotSupportedException();
        }
        result.addTransferListener(toNotifyWhenInit);
        result.connectGPS(i, o);
        return result;
    }

    /**
     * Start the commmunication with the GPS.
     */
    public void connectGPS(BufferedInputStream i, BufferedOutputStream o)
            throws FeatureNotSupportedException {
        // start the thread that makes the synchrone communication asynchrone
        listener = new Thread(this);
        listener.start();
        active = true;
    }

    protected GPS() {
        zListeners = new HashMap();
    }

    /**
     * Adds the specified IGPSlistener to receive data from the GPS.
     */
    public void addGPSlistener(IGPSlistener l) {
        if (zListeners.get("GPS") == null)
            zListeners.put("GPS", new Vector());
        // Only allow a listener to be registered once.
        if (((Vector) zListeners.get("GPS")).contains(l))
            return;

        ((Vector) zListeners.get("GPS")).add(l);
        return;
    }

    /**
     * Removes the the GPS-listener l from the list of GPS-listeners.
     */
    public void removeGPSListener(IGPSlistener l) {
        while (((Vector) zListeners.get("GPS")).removeElement(l)) {
        }
        return;
    }

    /**
     * Adds l to the list of listeners interested in transfer-events. Members of
     * this list can't be directly added, but have to be added through addition
     * of other listeners.
     */
    public void addTransferListener(ITransferListener l) {
        if (zListeners.get("TRANSFER") == null)
            zListeners.put("TRANSFER", new Vector());
        // Only allow a listener to be registered once.
        if (((Vector) zListeners.get("TRANSFER")).contains(l))
            return;

        ((Vector) zListeners.get("TRANSFER")).add(l);
        return;
    }

    /**
     * Removes the the transfer-listener l from the list of transfer-listeners.
     */
    public void removeTransferListener(ITransferListener l) {
        while (((Vector) zListeners.get("TRANSFER")).removeElement(l)) {
        }
        return;
    }

    /**
     * Adds l to the list of listeners interested in waypoint-data. Also adds l
     * to the list of transfer-listeners.
     */
    public void addWaypointListener(IWaypointListener l) {
        // Only allow a listener to be registered once.
        if (zListeners.get("WAYPOINT") == null)
            zListeners.put("WAYPOINT", new Vector());
        if (((Vector) zListeners.get("WAYPOINT")).contains(l))
            return;

        addTransferListener(l);

        ((Vector) zListeners.get("WAYPOINT")).add(l);
        return;
    }

    /**
     * Removes the the Waypoint-listener l from the list of Waypoint-listeners.
     */
    public void removeWaypointListener(IWaypointListener l) {
        while (((Vector) zListeners.get("WAYPOINT")).removeElement(l)) {
        }
        removeTransferListener(l);
        return;
    }

    /**
     * Adds l to the list of listeners interested in waypoint-data. Also adds l
     * to the list of transfer-listeners.
     */
    public void addTrackListener(ITrackpointListener l) {
        // Only allow a listener to be registered once.
        if (zListeners.get("TRACK") == null)
            zListeners.put("TRACK", new Vector());
        if (((Vector) zListeners.get("TRACK")).contains(l))
            return;

        addTransferListener(l);

        ((Vector) zListeners.get("TRACK")).add(l);
        return;
    }

    /**
     * Removes the the Waypoint-listener l from the list of Waypoint-listeners.
     */
    public void removeTrackListener(ITrackpointListener l) {
        while (((Vector) zListeners.get("TRACK")).removeElement(l)) {
        }
        removeTransferListener(l);
        return;
    }

    /**
     * Adds l to the list of listeners interested in waypoint-data. Also adds l
     * to the list of transfer-listeners.
     */
    public void addRouteListener(IRouteListener l) {
        // Only allow a listener to be registered once.
        if (zListeners.get("ROUTE") == null)
            zListeners.put("ROUTE", new Vector());
        if (((Vector) zListeners.get("ROUTE")).contains(l))
            return;

        addTransferListener(l);

        ((Vector) zListeners.get("ROUTE")).add(l);
        return;
    }

    /**
     * Removes the the Waypoint-listener l from the list of Waypoint-listeners.
     */
    public void removeRouteListener(IRouteListener l) {
        while (((Vector) zListeners.get("ROUTE")).removeElement(l)) {
        }
        removeTransferListener(l);
        return;
    }

    /**
     * Adds l to the list of listeners interested in waypoint-data. Also adds l
     * to the list of transfer-listeners.
     */
    public void addLapListener(ILapListener l) {
        // Only allow a listener to be registered once.
        if (zListeners.get("LAP") == null)
            zListeners.put("LAP", new Vector());
        if (((Vector) zListeners.get("LAP")).contains(l))
            return;

        addTransferListener(l);

        ((Vector) zListeners.get("LAP")).add(l);
        return;
    }

    /**
     * Removes the the Waypoint-listener l from the list of Waypoint-listeners.
     */
    public void removeLapListener(ILapListener l) {
        while (((Vector) zListeners.get("LAP")).removeElement(l)) {
        }
        removeTransferListener(l);
        return;
    }

    /**
     * Notifies listeners of the beginning of a stream of data. Tells listeners
     * of the number of data-units in the transfer.
     */
    public void fireTransferStart(int number) {
        for (int i = 0; i < ((Vector) zListeners.get("TRANSFER")).size(); i++) {
            ((ITransferListener) ((Vector) zListeners.get("TRANSFER"))
                    .elementAt(i)).transferStarted(number);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * wp.
     */
    public void fireWaypointData(IWaypoint wp) {
        if (((Vector) zListeners.get("WAYPOINT")) == null)
            zListeners.put("WAYPOINT", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("WAYPOINT")).size(); i++) {
            ((IWaypointListener) ((Vector) zListeners.get("WAYPOINT"))
                    .elementAt(i)).waypointReceived(wp);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * wp.
     */
    public void fireTrackpointData(ITrackpoint tp) {
        if (((Vector) zListeners.get("TRACK")) == null)
            zListeners.put("TRACK", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("TRACK")).size(); i++) {
            ((ITrackpointListener) ((Vector) zListeners.get("TRACK"))
                    .elementAt(i)).trackpointReceived(tp);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * header.
     */
    public void fireTrackpointHeader(ITrackpointHeader tp) {
        if (((Vector) zListeners.get("TRACK")) == null)
            zListeners.put("TRACK", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("TRACK")).size(); i++) {
            ((ITrackpointListener) ((Vector) zListeners.get("TRACK"))
                    .elementAt(i)).trackpointHeaderReceived(tp);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * wp.
     */
    public void fireRouteWaypoint(IRouteWaypoint tp) {
        if (((Vector) zListeners.get("ROUTE")) == null)
            zListeners.put("ROUTE", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("ROUTE")).size(); i++) {
            ((IRouteListener) ((Vector) zListeners.get("ROUTE")).elementAt(i))
                    .routeWaypointReceived(tp);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * header.
     */
    public void fireRouteHeader(IRouteHeader tp) {
        if (((Vector) zListeners.get("ROUTE")) == null)
            zListeners.put("ROUTE", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("ROUTE")).size(); i++) {
            ((IRouteListener) ((Vector) zListeners.get("ROUTE")).elementAt(i))
                    .routeHeaderReceived(tp);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * wp.
     */
    public void fireLapData(ILap lp) {
        if (((Vector) zListeners.get("LAP")) == null)
            zListeners.put("LAP", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("LAP")).size(); i++) {
            ((ILapListener) ((Vector) zListeners.get("LAP")).elementAt(i))
                    .lapReceived(lp);
        }
    }

    /**
     * Notifies listeners of the end of a stream of data.
     */
    public void fireTransferComplete() {
        if (((Vector) zListeners.get("TRANSFER")) == null)
            zListeners.put("TRANSFER", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("TRANSFER")).size(); i++) {
            ((ITransferListener) ((Vector) zListeners.get("TRANSFER"))
                    .elementAt(i)).transferComplete();
        }
    }

    /**
     * Goes through the list of TransferListeners and distributes the error.
     */
    protected void fireError(Exception e) {
        for (int i = 0; i < ((Vector) zListeners.get("TRANSFER")).size(); i++) {
            ((ITransferListener) ((Vector) zListeners.get("TRANSFER"))
                    .elementAt(i)).errorReceived(e);
        }
    }

    /**
     * Goes through the list of GPSlisteners and distributes the new position
     * data.
     */
    protected void firePositionData(IPosition pos) {
        if (((Vector) zListeners.get("GPS")) == null)
            zListeners.put("GPS", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("GPS")).size(); i++) {
            ((IGPSlistener) ((Vector) zListeners.get("GPS")).elementAt(i))
                    .positionReceived(pos);
        }
    }

    /**
     * Goes through the list of GPSlisteners and distributes the new date data.
     */
    protected void fireTimeDateData(ITimeDate dat) {
        if (((Vector) zListeners.get("GPS")) == null)
            zListeners.put("DATE", new Vector());
        for (int i = 0; i < ((Vector) zListeners.get("GPS")).size(); i++) {
            ((IGPSlistener) ((Vector) zListeners.get("GPS")).elementAt(i))
                    .timeDateReceived(dat);
        }
    }

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public abstract void requestPosition() throws FeatureNotSupportedException,
            IOException;

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public abstract void requestTime() throws FeatureNotSupportedException,
            IOException;

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public abstract void requestDate() throws FeatureNotSupportedException,
            IOException;

    /**
     * Requests a descriptive string from the GPS. Should be formatted for
     * human-reading. The string should be constructed by every
     * GPS-implementation upon startup.
     */
    public abstract String getDescription();

    /**
     * Asks the GPS to transmit all the waypoints in it's memory. The result
     * will be returned through the WaypointListener-interface. Throws a
     * FeatureNotSupportException if it isn't possible to do this on the GPS.
     */
    public abstract void requestWaypoints()
            throws FeatureNotSupportedException, IOException;

    /**
     * Asks the GPS to transmit all the waypoints in it's memory. The result
     * will be returned through the WaypointListener-interface. Throws a
     * FeatureNotSupportException if it isn't possible to do this on the GPS.
     */
    public abstract void requestTracks() throws FeatureNotSupportedException,
            IOException;

    /**
     * Asks the GPS to transmit all the routes in it's memory. The result will
     * be returned through the RouteListener-interface. Throws a
     * FeatureNotSupportException if it isn't possible to do this on the GPS.
     */
    public abstract void requestRoutes() throws FeatureNotSupportedException,
            IOException;

    /**
     * Asks the GPS to transmit all the laps in it's memory. The result will be
     * returned through the ILapListener interface. Throws a
     * FeatureNotSupportException if it isn't possible to do this on the GPS.
     */
    public abstract void requestLaps() throws FeatureNotSupportedException,
            IOException;

    /**
     * Asks the GPS to either start or stop transmitting data periodically.
     * <br/> The data will be the that which is accessible through the
     * IGPSlistener-interface. Throws a FeatureNotSupportException if it isn't
     * possible to do this on the GPS.
     */
    public abstract void setAutoTransmit(boolean t)
            throws FeatureNotSupportedException, IOException;

    /**
     * Stops communication with GPS.<br/> Most likely, your program won't be
     * able to shut down before you've called this method. If b is set to true,
     * the GPS will be asked to turn off. If b is set to false, the GPS will
     * remain turned on. Throws a FeatureNotSupportException if b is true, but
     * the GPS-unit doesn't support that function.
     */
    public abstract void shutdown(boolean b)
            throws FeatureNotSupportedException, IOException;

    /**
     * @return a factory for creating data in a format that is specific to the
     *         current GPS.
     */
    public abstract IGPSFactory getFactory();

    /**
     * This method is listening for input from the GPS.
     */
    public void run() {
        while (active) {
            // try to get something from the GPS
            try {
                if (inputAvailable() == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
                handleInput();
            } catch (IOException e) {
                active = false;
                return;
            } catch (InvalidPacketException e) {
                fireError(e);
                // Send back a NAK-packet.
                try {
                    sendNack();
                } catch (IOException ex) {
                    active = false;
                    fireError(ex);
                    return;
                } catch (InvalidPacketException ex) {
                    ex.printStackTrace();
                    active = false;
                    fireError(ex);
                    return;
                }
            }
        } // End of while(active)

        disconnectGPS();
    }

    protected abstract void disconnectGPS();

    protected abstract int inputAvailable() throws IOException;

    protected abstract void sendNack() throws IOException,
            InvalidPacketException;

    /**
     * Read, parse and react to a packet.
     */
    protected abstract void handleInput() throws InvalidPacketException,
            IOException;

    public abstract void sendWaypoints(Collection waypoints)
            throws InvalidPacketException, IOException,
            ProtocolNotRecognizedException, ProtocolNotSupportedException,
            InvalidFieldValue;

    public abstract void sendTrack(ITrackpointHeader header, Collection points)
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException, IOException, InvalidFieldValue,
            InvalidPacketException;
}