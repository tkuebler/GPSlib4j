package com.diddlebits.gpslib4j.Garmin;

/**
* This method is thrown from the constructors of the packet-classes, whenever the int[]-array is not formatted according to the Garmin-packet-specs. 
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/
public class InvalidPacketException extends RuntimeException {
	
	private int[] packet;
	private int index;
	
	/**
	* Creates an InvalidPacketException. pack is a reference to the byte-array that caused the exception.
	* i is the index of the byte that was in error.
	*/
	public InvalidPacketException(int[] pack, int i) {
		packet = pack;
		index = i;
	}
	
	/**
	* Returns the packet that caused the exception to be thrown.
	* <b>Note:</b> The return-value is a reference to the original array. Changes will likely propagate
	* to other parts of the program. 
	*/
	public int[] getPacket() {
		return packet;
	}
	
	/**
	* 
	*/
	
	/**
	* Returns the index of the first erroneously configured byte.
	*/
	public int getIndex() {
		return index;
	}
	
	/**
	* Returns a formatted string showing which byte is wrong.
	*/
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("\nByte\tvalue\n");
		for (int i = 0 ; i < packet.length ; i++) {
			res.append(" " + i+"\t"+packet[i]);
			if (i == index)
				res.append(" <- Erroneous");
			res.append('\n');
		}
		return res.toString();
	}
}