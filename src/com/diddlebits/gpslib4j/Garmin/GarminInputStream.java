package com.diddlebits.gpslib4j.Garmin;
import java.io.*;

/**
* This class provides the functionality of automatically removing the double DLEs from the GPS-inputstream.
* The double-DLEs can be found in the size-,data-, and checksum-fields. 
* The only method providing the filtering-functionality is read().
*/
public class GarminInputStream extends FilterInputStream {
	/*
	* Last value read.
	*/
	private int prev;
	
	/**
	* Takes the stream to the GPS-unit as an argument.
	*/
	public GarminInputStream(InputStream i) {
		super(i);
		in = i;
		prev = 0;
	}		
			
	/**
	* Reads a packet from the stream. <br/>
	* <b> Note: </b> Method assumes that it's called between packets, ie. when the first byte of a packet is the
	* next in the stream. If this condition is met, the method will leave the stream in the same state.
	*/	
	public int[] readPacket() throws InvalidPacketException, IOException {
		int c;
		int[] packet;
		int id, size;
		c = read();
		
		if (c != GarminPacket.DLE) {
			throw (new InvalidPacketException( new int[] { GarminPacket.DLE }, 0));
		}
				
		id = read();
		size = read();
		packet = new int[size + 6];
		packet[0] = GarminPacket.DLE;
		packet[1] = id;
		packet[2] = size;
		for (int i = 0 ; i < size + 3 ; i++) 
			packet[3 + i] = read();
			
		if (packet[packet.length - 2] != GarminPacket.DLE) {
			throw (new InvalidPacketException(packet, packet.length - 2));
		}
		
		if (packet[packet.length - 1] != GarminPacket.ETX) {
			throw (new InvalidPacketException(packet, packet.length - 1));
		}
		return packet;
	}
	
	/**
	* Returns the next byte from the stream. Makes sure to remove DLE stuffing.	
	*/	
	public int read() throws IOException{
		int c = in.read();
		if ( prev == 16 && c == 16)
			return prev = in.read();
		else 
			return prev = c;
	}
}