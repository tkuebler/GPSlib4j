package com.diddlebits.gpslib4j.Garmin;
import java.io.*;

/**
* This class take care of adding DLE-stuffing to all packets sent to the GPS.
* <b> NOTE: </b> Only the method write(GarminPacket) performs addition of DLE-stuffing. The remaining 
* methods write directly to the GPS without format-control.
*/

public class GarminOutputStream extends FilterOutputStream {	
	public GarminOutputStream(OutputStream o) {
		super(o);
	}
	    
	public synchronized void write (GarminPacket packet) throws IOException, InvalidPacketException {
		if (packet.isLegal() != -1) {			
			throw (new InvalidPacketException(packet.getPacket(), packet.isLegal()));
		}
		write(packet.getByte(0));
		write(packet.getByte(1));
		
		int c;
		// Iterate through size-field, data-field and checksum-field. Add stuffing where necessary.
		for (int i = 0 ; i < packet.getByte(2) + 2 ; i++) {
			c = packet.getByte(i + 2);			
			write(c);
			if (c == GarminPacket.DLE)
				write(c);			
		}
		
		write(GarminPacket.DLE);
		write(GarminPacket.ETX);		
		flush();
	}	
}