package com.diddlebits.gpslib4j.Garmin;

//* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/). */

import java.io.*;
import java.util.Vector;

import com.diddlebits.gpslib4j.*;

public class GarminGPS extends GPS implements Runnable {
	GarminInputStream input;
	GarminOutputStream output;
	
	/** A human-readable description of the GPS-unit. */ 
	protected String description;
	
	Thread listener;
	/** The listening thread will be active as long as this variable remains true. */
	protected boolean active; 
	/** A vector containing references to all the GarminListeners. */
	protected Vector GarminListeners;
	
	/** */ 
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
			//output.write(GarminPacket.createCommandPacket(GarminPacket.Cmnd_Abort_Transfer));
			// request product info
			output.write(GarminPacket.createBasicPacket(GarminPacket.Pid_Product_Rqst, new int[] {}));
		} catch(IOException e) {}
	}
	
	
	/**
	* Adds the specified GarminListener to receive all packets sent from the GPS. 	
	*/
	public void addGarminListener(GarminListener l) {
		// Only allow a listener to be registered once.
		if (GarminListeners.contains(l))
			return;
			
		GarminListeners.add(l);
		return;
	}

	/**
	* Removes the specified GarminListener from the list of listeners.
	*/ 
	public void removeGarminListener(GarminListener l) {
		while (GarminListeners.removeElement(l)) {}
		return;
	}

	/** 
	* Goes through the list of GarminListeners and transmits p to them.
	*/	
	protected void fireGarminPacket(GarminPacket p) {
		for (int i = 0 ; i < GarminListeners.size() ; i++) {
			((GarminListener) GarminListeners.elementAt(i)).GarminPacketReceived(p);
		}	
	}
	
	/** This method is listening for input from the GPS. */ 
	public void run() {
		GarminPacket pack = null;		

		while (active) {
			try {
				if (input.available() == 0) {
					try {
						Thread.sleep(500);
					} catch(InterruptedException e) {}
					continue;
				}
				pack = new GarminPacket(input.readPacket());				
			} catch (IOException e) {
				active = false;				
				return;
			} catch (InvalidPacketException e) {
				// Send back a NAK-packet.
				try {
					output.write( GarminPacket.createBasicPacket(GarminPacket.Pid_Nak_Byte, new int[] {pack.getID(), 0}));
				} catch (IOException ex) {
					active = false;
					return;
				}
			}
			
			// Send back ACK-packet.
			try {
				output.write( GarminPacket.createBasicPacket(GarminPacket.Pid_Ack_Byte, new int[] {pack.getID(), 0}));
			} catch (IOException e) {
				active = false;
			}

			fireGarminPacket(pack);
			Distribute(pack);
			
		} // End of while
	}
	
	/** This method is used to identify the type of packet received, and distribute it to the correct 
	* listeners. 
	*/
	protected void Distribute(GarminPacket p) {		
		switch (p.getID()) {
			case GarminPacket.Pid_Position_Data :
				firePositionData(new PositionDataPacket(p));
				return;
			case GarminPacket.Pid_Date_Time_Data :
				TimeDataPacket tdp = new TimeDataPacket(p);
				fireDateData(tdp);
				fireTimeData(tdp);
				return;
			case GarminPacket.Pid_Pvt_Data :
				PVTDataPacket pvtp = new PVTDataPacket(p);		
				fireTimeData(pvtp);
				firePositionData(pvtp);
				return;				
			case GarminPacket.Pid_Records :
				fireTransferStart((new RecordsPacket(p)).getNumber());
				return;
			case GarminPacket.Pid_Wpt_Data : 
				fireWaypointData( new WaypointDataPacket(p));
				return;
			case GarminPacket.Pid_Trk_data : 
				fireTrackpointData( new TrackpointDataPacket(p));
				return;
			case GarminPacket.Pid_Trk_Hdr : 
				fireTrackpointData( new TrackpointDataPacket(p));
				return;
			case GarminPacket.Pid_Lap : 
				fireLapData( new LapDataPacket(p));
				return;
			case GarminPacket.Pid_Xfer_Cmplt :
				System.out.println("Transfer Complete");
				fireTransferComplete();
				return;
			case GarminPacket.Pid_Product_Data :
				fireProductData(new ProductDataPacket(p));
				fireTransferComplete();
				return;
			case GarminPacket.Pid_Protocol_Array :
				description += "\nProtocols supported:\n";
				description += (new ProtocolDataPacket(p)).toString();
				return;
			case GarminPacket.Pid_Ack_Byte :
				// ack packet
				System.out.println("ACK received");
				return;
			default :
				System.out.println("Unknown packet type received: id=" + p.getID() + ", data=" + p.getRawPacket());
				return;
		}
	}

	/** Makes a request for the specified data to the GPS. Data will be returned to all listeners through the IGPSlistener-interface. */
	public void requestPosition() {
		try {
			output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Transfer_Posn));
		} catch (IOException e) {}
	}
	/** Makes a request for the specified data to the GPS. Data will be returned to all listeners through the IGPSlistener-interface. */
	public void requestTime() {
		try {
			output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Transfer_Time));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/** Makes a request for the specified data to the GPS. Data will be returned to all listeners through the IGPSlistener-interface. */
	public void requestDate() {
		try {
			output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Transfer_Time));
		} catch (IOException e) {}		
	}

	/** 
	* Asks the GPS to transmit all the waypoints in it's memory. The result will be returned through the WaypointListener-interface. 
	*/
	public void requestWaypoints() {
		try {
			output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Transfer_Wpt));
		} catch (IOException e) {}
	}
		
	/** 
	* Asks the GPS to transmit all the tracks in it's memory. The result will be returned through the TrackListener-interface. 
	*/
	public void requestTracks() {
		try {
			output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Transfer_Trk));
		} catch (IOException e) {}
	}
	
	/** 
	* Asks the GPS to transmit all the laps in it's memory. The result will be returned through the LapsListener-interface. 
	*/
	public void requestLaps() {
		try {
			output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Transfer_Laps));
		} catch (IOException e) {}
	}
	
	/** 
	* Asks the GPS to either start or stop transmitting data periodically. <br/>
	* The data will be that which is accessible through the IGPSlistener-interface. 
	* Throws a FeatureNotSupportException if it isn't possible to do this on the GPS.
	*/
	public void setAutoTransmit(boolean t) {
		if (t) {
			try {
			output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Start_Pvt_Data));
			} catch (IOException e) {}		
		} else {
			try {
				output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Stop_Pvt_Data));
			} catch (IOException e) {}					
		}
	}
	
	/** Stops communication with GPS.<br/>
	* Most likely, your program won't be able to shut down before you've called this method. 
	* If b is set to true, the GPS will be asked to turn off.	
	* If b is set to false, the GPS will remain turned on.
	*/
	public void shutdown(boolean b) {
		if (b) { 
			try {
				output.write( GarminPacket.createCommandPacket(GarminPacket.Cmnd_Turn_Off_Pwr));
			} catch (IOException e) {}
		}
		active = false;		
	}
	
	/** 
	* Returns a string telling the brand of the Garmin-gps, software version and the protocols supported.
	*/ 
	public String getDescription() {
		return description;
	}
}