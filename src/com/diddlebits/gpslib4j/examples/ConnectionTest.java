package com.diddlebits.gpslib4j.examples;

/** This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/). */

import com.diddlebits.gpslib4j.Garmin.*;

import javax.comm.*;

import com.diddlebits.gpslib4j.*;

import java.util.Enumeration;
import java.util.Vector;
import java.io.*;

/**
 * A simple test-program that queries the GPS for a description of itself. <br/>
 * A good way to initially test the connection to the GPS. <br/>Attempts to
 * turn off the GPS after retrieving the description.
 */

public class ConnectionTest implements IGPSlistener, IWaypointListener,
		ITrackpointListener, ILapListener {
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
		System.loadLibrary("rxtxSerial");
		inuser = new BufferedReader(new InputStreamReader(System.in));
	}

	public void download() {

		CommPortIdentifier zPort = ChoosePort();
		//CommPortIdentifier.getPortIdentifiers() ;
		//CommPortIdentifier zPort = null;
		/*
		try {
			zPort = CommPortIdentifier
					.getPortIdentifier("/dev/tty.USA28X2b2P1.1");
		} catch (NoSuchPortException e1) {
			e1.printStackTrace();
		}
		*/
		//String gpsbrand = ListGPS();
		String gpsbrand = "GARMIN";
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
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (gpsbrand.equals("GARMIN")) {

			gps = new GarminGPS(input, output);
			gps.addGPSlistener(this);
			gps.addWaypointListener(this);
			gps.addTrackListener(this);
			gps.addLapListener(this);

		}

		System.out.println("Connecting to GPS.");
		transfering = true;
		String description = ((GarminGPS) gps).getDescription();
		waitForResponse();

		System.out.println("Connected.");
		System.out.println("now requesting waypoint data");
		transfering = true;
		((GarminGPS) gps).requestWaypoints();
		waitForResponse();

		System.out.println("now requesting track data");
		transfering = true;
		((GarminGPS) gps).requestTracks();
		waitForResponse();

		System.out.println("now requesting lap data");
		transfering = true;
		((GarminGPS) gps).requestLaps();
		waitForResponse();

		gps.shutdown(false);
	}

	private void waitForResponse() {
		int timeout = 20;

		while (transfering && (count <= timeout)) {
			// note, need to transfer timeout and cancel interface
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Ok. Ridiculous method, but I'm hoping that more GPS-types will be added
	 * later. Lists the types of GPS that are available and lets the user pick
	 * one.
	 */
	private String ListGPS() {
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
				System.out.print(j++ + ". " + c.getName());
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

			if ((index < 1) || (index >= names.size())) {
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
	 * @see com.diddlebits.gpslib4j.IGPSlistener#timeReceived(com.diddlebits.gpslib4j.ITime)
	 */
	public void timeReceived(ITime t) {
		// TODO Auto-generated method stub
		System.out.println("Time recieved by connection test: " + t);
		count = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diddlebits.gpslib4j.IGPSlistener#dateReceived(com.diddlebits.gpslib4j.IDate)
	 */
	public void dateReceived(IDate d) {
		// TODO Auto-generated method stub
		System.out.println("Date recieved by connection test: " + d);
		count = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diddlebits.gpslib4j.IGPSlistener#positionReceived(com.diddlebits.gpslib4j.IPosition)
	 */
	public void positionReceived(IPosition pos) {
		// TODO Auto-generated method stub
		System.out.println("Position recieved by connection test: " + pos);
		count = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diddlebits.gpslib4j.IWaypointListener#waypointReceived(com.diddlebits.gpslib4j.IWaypoint)
	 */
	public void waypointReceived(IWaypoint wp) {
		// TODO Auto-generated method stub
		System.out.println("Waypoint recieved by connection test: "
				+ wp.toString() + " : "
				+ ((WaypointDataPacket) wp).getRawPacket());
		count = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diddlebits.gpslib4j.ITrackpointListener#trackpointReceived(com.diddlebits.gpslib4j.ITrackpoint)
	 */
	public void trackpointReceived(ITrackpoint tp) {
		// TODO Auto-generated method stub
		System.out.println("Trackpoint recieved by connection test: "
				+ ((TrackpointDataPacket) tp).toString() + " : "
				+ ((TrackpointDataPacket) tp).getRawPacket());
		count = 0;
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.diddlebits.gpslib4j.ITransferListener#transferStarted(int)
	 */
	public void transferStarted(int number) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		System.out.println("Lap recieved by connection test: "
				+ ((LapDataPacket) lp).toString() + " : "
				+ ((LapDataPacket) lp).getRawPacket());
		count = 0;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.IGPSlistener#productInfoReceived()
	 */
	public void productInfoReceived(IProductData dp) {
		// TODO Auto-generated method stub
		System.out.println("Product Info received: "
				+ ((ProductDataPacket) dp).toString() + " : "
				+ ((ProductDataPacket) dp).getRawPacket());
		count = 0;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.IGPSlistener#productInfoReceived()
	 */
	public void productInfoReceived() {
		// TODO Auto-generated method stub
		
	}
}