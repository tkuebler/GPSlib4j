package com.diddlebits.gpslib4j.test;
import javax.comm.*;

import com.diddlebits.gpslib4j.*;

import java.io.*;

import com.diddlebits.gpslib4j.Garmin.*;

/**
* Class made to be able to test the classes of the com.diddlebits.gpslib4j-package.
*/
public class ClassTester implements IGPSlistener, GarminListener, IWaypointListener {	
	public static void main(String args[]) {
		new ClassTester();

	}
	

	public ClassTester() {				
		SerialPort port;
		BufferedInputStream input;
		BufferedOutputStream output;
							
		System.out.println("Using COM1.");
		// Open port.
		try {
			port = (SerialPort) CommPortIdentifier.getPortIdentifier("COM1").open("com.diddlebits.gpslib4j", 3000);
		} catch (NoSuchPortException e) {
			System.out.println("No such port!\n" + e.getMessage());
			return;
		} catch (PortInUseException e) {
			System.out.println("Port already in use! (??!)\n" + e.getMessage());
			return;
		}
		
		try {
			input = new BufferedInputStream(port.getInputStream());
			output = new BufferedOutputStream(port.getOutputStream());			
		} catch (IOException e) {
			System.out.println("IOException... ");
			return;
		}
		
		GPS gps = new GarminGPS(input, output, this);
		gps.addGPSlistener(this);
		gps.addWaypointListener(this);
		( (GarminGPS) gps).addGarminListener(this);
		
		try {
            gps.requestWaypoints();
        } catch (FeatureNotSupportedException e1) {
            System.out.println("Waypoint feature not supported");
        } catch (IOException e1) {
            System.out.println("Communication error while requesting the waypoints");
        }
		
		// gps.setAutoTransmit(true);		
		
		try {Thread.sleep(1000); } catch (InterruptedException e) {}
		
		// gps.setAutoTransmit(false);
        try {
    		gps.shutdown(false);
        } catch (FeatureNotSupportedException e1) {
            System.out.println("Shutdown not supported");
        } catch (IOException e1) {
            System.out.println("Communication error while requesting a shutdown");
        }
	}
	
	/** Invoked when the GPS transmits position-data. */
	public void positionReceived(IPosition pos) {
		System.out.println("Received Position.");
	}
	public void garminPacketReceived(GarminPacket p) {
	}
	
	public void waypointReceived(IWaypoint wp) {		
		System.out.println("Received waypoint.");
		System.out.println(" - Identifier: " + wp.getIdent());
		System.out.println(" - Position: " + wp.getPosition().toString());
	}
		
	public void transferStarted(int number) {
		System.out.println("A transfer has started... Expecting " + number + " data-units.");	
	}
	
	public void transferComplete() {
		System.out.println("Transfer completed.");
	}


    public void errorReceived(Exception e)
    {
        System.out.println("Error received:");
        System.out.println(e.toString());
    }


    /** Invoked when the GPS transmits time-data. */
    public void timeDateReceived(ITimeDate d)
    {
        System.out.println("Date: " + d.getTime().toString());
    }
}