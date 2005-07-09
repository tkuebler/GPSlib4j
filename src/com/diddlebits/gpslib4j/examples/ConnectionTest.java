package com.diddlebits.gpslib4j.examples;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;

import com.diddlebits.gpslib4j.*;

/**
 * A simple test-program that queries the GPS for a description of itself. <br/>
 * A good way to initially test the connection to the GPS. <br/>Attempts to turn
 * off the GPS after retrieving the description.
 */

public class ConnectionTest implements IGPSlistener, IWaypointListener,
        ITrackpointListener, ILapListener, IRouteListener {
    /** The communication port being used. */
    String portname;

    BufferedReader inuser;

    GPS gps;

    CommPort port;

    BufferedInputStream input;

    BufferedOutputStream output;

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
        inuser = new BufferedReader(new InputStreamReader(System.in));
    }

    public void download() {

        String gpsBrand = ChooseBrand();
        CommPortIdentifier zPort = ChoosePort();

        try {
            port = zPort.open("ConnectionTest", 5000);
        } catch (PortInUseException e) {
            System.out.println("Port already in use by " + e.currentOwner);
            return;
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            input = new BufferedInputStream(port.getInputStream());
            output = new BufferedOutputStream(port.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error opening port " + portname);
            return;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        transfering = true;
        try {
            gps = GPS.CreateInterface(gpsBrand, input, output);
        } catch (FeatureNotSupportedException e) {
            System.out.println("Error creating a driver");
            return;
        }

        gps.addGPSlistener(this);
        gps.addWaypointListener(this);
        gps.addTrackListener(this);
        gps.addLapListener(this);
        gps.addRouteListener(this);

        System.out.println("Connecting to GPS.");
        // String description = ((GarminGPS) gps).getDescription();
        waitForResponse();

        System.out.println("Connected.");

        testPosition();
        testRoute();
        testWaypoint();
        testTrack();
        testLap();
        testShutdown();
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
    }

    /**
     * Ok. Ridiculous method, but I'm hoping that more GPS-types will be added
     * later. Lists the types of GPS that are available and lets the user pick
     * one.
     */
    public String ListGPS() {
        int index = -1;
        while (index == -1) {
            System.out.println("\nAvailable GPS-types:");
            System.out.println(" 1. Garmin");
            System.out.print("Select GPS: ");
            String input = readFromUser();

            try {
                index = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                index = -1;
                continue;
            }

            if (index != 1) {
                index = -1;
                continue;
            }
        }
        switch (index) {
        case 1:
            return "GARMIN";
        default:
            return "unknown";
        }
    }

    private String ChooseBrand() {
        Vector brands = GPS.GetBrands();
        int index = -1;
        while (index < 0) {
            System.out.println("Available brands: ");
            int j = 1;
            for (Enumeration i = brands.elements(); i.hasMoreElements();) {
                System.out.print("  " + j++ + ". ");
                System.out.println(i.nextElement());
            }

            if (GPS.GetBrands().size() > 1) {
                System.out.print("Select brand: ");
                String input = readFromUser();

                try {
                    index = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    index = -1;
                    continue;
                }

                if (index < 1 || index > GPS.GetBrands().size()) {
                    index = -1;
                    continue;
                }
            } else {
                index = 1;
                System.out
                        .println("Take the only one available for the moment: "
                                + (String) brands.elementAt(index - 1));
                System.out.println("");
            }
        }
        return (String) brands.elementAt(index - 1);
    }

    private CommPortIdentifier ChoosePort() {
        Vector names = null;
        int index = -1;
        while (index == -1) {
            int j = 1;
            names = new Vector();
            System.out.println("Available ports: ");
            CommPortIdentifier c;
            for (Enumeration i = CommPortIdentifier.getPortIdentifiers(); i
                    .hasMoreElements();) {
                c = (CommPortIdentifier) i.nextElement();
                System.out.print("  " + j++ + ". " + c.getName());
                names.add(c);
                if (c.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    System.out.print("\t SERIAL\n");
                if (c.getPortType() == CommPortIdentifier.PORT_PARALLEL)
                    System.out.print("\t PARALLEL\n");
            }

            System.out.print("Select port: ");
            String input = readFromUser();

            try {
                index = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                index = -1;
                continue;
            }

            if ((index < 1) || (index > names.size())) {
                index = -1;
                continue;
            }

        }

        return (CommPortIdentifier) names.elementAt(index - 1);
    }

    public String readFromUser() {
        try {
            return inuser.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
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
     * @see com.diddlebits.gpslib4j.ITrackpointListener#trackpointHeaderReceived(com.diddlebits.gpslib4j.ITrackpoint)
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