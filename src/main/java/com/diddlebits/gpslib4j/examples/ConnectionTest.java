package com.diddlebits.gpslib4j.examples;

import java.io.IOException;

import com.diddlebits.gpslib4j.FeatureNotSupportedException;
import com.diddlebits.gpslib4j.GPS;
import com.diddlebits.gpslib4j.IGPSlistener;
import com.diddlebits.gpslib4j.ILap;
import com.diddlebits.gpslib4j.ILapListener;
import com.diddlebits.gpslib4j.IPosition;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.IRouteListener;
import com.diddlebits.gpslib4j.IRouteWaypoint;
import com.diddlebits.gpslib4j.ITimeDate;
import com.diddlebits.gpslib4j.ITrackpoint;
import com.diddlebits.gpslib4j.ITrackpointHeader;
import com.diddlebits.gpslib4j.ITrackpointListener;
import com.diddlebits.gpslib4j.IWaypoint;
import com.diddlebits.gpslib4j.IWaypointListener;

/**
 * A simple test-program that queries the GPS for a description of itself. <br/>
 * A good way to initially test the connection to the GPS. <br/>Attempts to turn
 * off the GPS after retrieving the description.
 */

public class ConnectionTest implements IGPSlistener, IWaypointListener,
        ITrackpointListener, ILapListener, IRouteListener {
    GPS gps;

    boolean transfering = false;

    int count = 0;

    public static void main(String args[]) {
        ConnectionTest test = new ConnectionTest();
        test.download();
        System.exit(0);
    }

    public ConnectionTest() {
        System.out.println();
        try {
            // System.loadLibrary("rxtxSerial");
        } catch (UnsatisfiedLinkError e) {
            // who cares? must be already loaded...
        }
    }

    public void download() {
        gps = Utils.get().connectGPS();
        if (gps != null) {
            gps.addGPSlistener(this);
            gps.addWaypointListener(this);
            gps.addTrackListener(this);
            gps.addLapListener(this);
            gps.addRouteListener(this);

            testPosition();
            testRoute();
            testWaypoint();
            testTrack();
            // testLap();
            testShutdown();
        }
        Utils.get().closeGPS();
    }

    private void testRoute() {
        System.out.println("now requesting route data");
        transfering = true;
        try {
            gps.requestRoutes();
            waitForResponse();
        } catch (FeatureNotSupportedException e) {
            System.out.println("  not supported with this GPS");
        } catch (IOException e) {
            System.out.println("Communication error");
            System.exit(-1);
        }
    }

    /**
     * 
     */
    private void testShutdown() {
        System.out.println("now requesting a shutdown");
        try {
            gps.shutdown(false);
        } catch (FeatureNotSupportedException e) {
            System.out.println("  not supported with this GPS");
        } catch (IOException e) {
            System.out.println("Communication error");
            System.exit(-1);
        }
    }

    /**
     * 
     */
    private void testLap() {
        System.out.println("now requesting lap data");
        transfering = true;
        try {
            gps.requestLaps();
            waitForResponse();
        } catch (FeatureNotSupportedException e) {
            System.out.println("  not supported with this GPS");
        } catch (IOException e) {
            System.out.println("Communication error");
            System.exit(-1);
        }
    }

    /**
     * 
     */
    private void testTrack() {
        System.out.println("now requesting track data");
        transfering = true;
        try {
            gps.requestTracks();
            waitForResponse();
        } catch (FeatureNotSupportedException e) {
            System.out.println("  not supported with this GPS");
        } catch (IOException e) {
            System.out.println("Communication error");
            System.exit(-1);
        }
    }

    /**
     * 
     */
    private void testWaypoint() {
        System.out.println("now requesting waypoint data");
        transfering = true;
        try {
            gps.requestWaypoints();
            waitForResponse();
        } catch (FeatureNotSupportedException e) {
            System.out.println("  not supported with this GPS");
        } catch (IOException e) {
            System.out.println("Communication error");
            System.exit(-1);
        }
    }

    private void testPosition() {
        System.out.println("now requestion position data");
        transfering = true;
        try {
            gps.requestPosition();
            waitForResponse();
        } catch (FeatureNotSupportedException e) {
            System.out.println("  not supported with this GPS");
        } catch (IOException e) {
            System.out.println("Communication error");
            System.exit(-1);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private void waitForResponse() {
        int timeout = 200;

        while (transfering && (count <= timeout)) {
            // TODO: need to transfer timeout and cancel interface
            count++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        if (transfering) {
            System.out.println("Timeout!!!");
            System.exit(-1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.diddlebits.gpslib4j.IGPSlistener#positionReceived(com.diddlebits.gpslib4j.IPosition)
     */
    public void positionReceived(IPosition pos) {
        System.out.println("Position recieved by connection test: " + pos);
        count = 0;
        transfering = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.diddlebits.gpslib4j.IWaypointListener#waypointReceived(com.diddlebits.gpslib4j.IWaypoint)
     */
    public void waypointReceived(IWaypoint wp) {
        System.out.println("Waypoint recieved by connection test: "
                + wp.toString());
        count = 0;
    }

    /**
     * @see com.diddlebits.gpslib4j.ITrackpointListener#trackpointReceived(com.diddlebits.gpslib4j.ITrackpoint)
     */
    public void trackpointReceived(ITrackpoint tp) {
        System.out.println("Trackpoint received by connection test: "
                + tp.toString());
        count = 0;
    }

    /**
     * @see com.diddlebits.gpslib4j.ITrackpointListener#trackpointHeaderReceived(com.diddlebits.gpslib4j.ITrackpointHeader)
     */
    public void trackpointHeaderReceived(ITrackpointHeader tp) {
        System.out.println("Trackpoint header received by connection test: "
                + tp.toString());
        count = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.diddlebits.gpslib4j.ITransferListener#transferStarted(int)
     */
    public void transferStarted(int number) {
        System.out
                .println("transfer started notification arrived at connection test: "
                        + number);
        transfering = true;
        count = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.diddlebits.gpslib4j.ITransferListener#transferComplete()
     */
    public void transferComplete() {
        System.out
                .println("transfer complete notification arrived at connection test.");
        transfering = false;
        count = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.diddlebits.gpslib4j.ILapListener#lapReceived(com.diddlebits.gpslib4j.ITrackpoint)
     */
    public void lapReceived(ILap lp) {
        System.out.println("Lap recieved by connection test: " + lp.toString());
        count = 0;
    }

    public void errorReceived(Exception e) {
        System.out.println("Error during the communication:");
        System.out.println(e.toString());
        System.exit(1);
        transfering = false;
    }

    public void timeDateReceived(ITimeDate d) {
        System.out.println("Date recieved by connection test: "
                + d.getTime().toString());
    }

    public void routeHeaderReceived(IRouteHeader tp) {
        System.out.println("Route header received by connection test: "
                + tp.toString());

    }

    public void routeWaypointReceived(IRouteWaypoint tp) {
        System.out.println("Route waypoint received by connection test: "
                + tp.toString());
    }
}