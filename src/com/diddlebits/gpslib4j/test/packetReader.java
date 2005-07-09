package com.diddlebits.gpslib4j.test;
import javax.comm.*;
import java.io.*;

/**
* Simple scanner capable of reading the packets sent by the GPS.
*/

public class packetReader {
	
	// L000 values. (Names taken from protocol specification.
	public static final int Pid_Ack_Byte = 6;
	public static final int Pid_Command_Packet = 10;
	public static final int Pid_Nak_Byte = 21;
	public static final int Pid_Protocol_Array = 253;
	public static final int Pid_Product_Rqst = 254;
	public static final int Pid_Product_Data = 255;
	
	// L001 values.
	public static final int Pid_Command_Data = 10;
	public static final int Pid_Date_Time_Data = 14;
	public static final int Pid_Position_Data = 17;
	public static final int Pid_Pvt_Data = 51;
	
	// A010 - Device Command Protocol.
	public static final int Cmnd_Transfer_Posn = 2;
	public static final int Cmnd_Transfer_Time = 5;
	public static final int Cmnd_Turn_Off_Pwr = 8;
	public static final int Cmnd_Start_Pvt_Data = 49;
	
	// Packet Boundaries.
	public static final int DLE = 16; // Data link escape.
	public static final int ETX = 3;  // End of text.
	
	public static void main(String args[]) {
		
		// Command to turn of GPS
		int[] turn_off = new int[8];
		turn_off[0] = DLE;
		turn_off[1] = Pid_Command_Packet;
		turn_off[2] = 2; // Length of data
		turn_off[3] = Cmnd_Turn_Off_Pwr;
		turn_off[4] = 0;
		turn_off[6] = DLE;
		turn_off[7] = ETX;
		
		calcChecksum(turn_off);
		System.out.println("Turn off checksum: " + turn_off[5]);
		
		// Command to ask GPS for time data.
		int[] transfer_time = new int[8];
		transfer_time[0] = DLE;
		transfer_time[1] = Pid_Command_Packet;
		transfer_time[2] = 2;
		transfer_time[3] = Cmnd_Transfer_Time;
		transfer_time[4] = 0;
		transfer_time[6] = DLE;
		transfer_time[7] = ETX;
		
		calcChecksum(transfer_time);
		System.out.println("Transfer time" + transfer_time[5]);
		
		// Command to make a product request to the GPS.
		int[] product_request = new int[6];
		product_request[0] = DLE;
		product_request[1] = Pid_Product_Rqst;
		product_request[2] = 0;
		product_request[4] = DLE;
		product_request[5] = ETX;
		
		calcChecksum(product_request);				
		System.out.println("Product request:" + product_request[5]);
		
		
		// Command to ask GPS for position data
		int[] transfer_position = new int[8];
		transfer_position[0] = DLE;
		transfer_position[1] = Pid_Command_Packet;
		transfer_position[2] = 2;
		transfer_position[3] = Cmnd_Transfer_Posn;
		transfer_position[4] = 0;
		transfer_position[6] = DLE;
		transfer_position[7] = ETX;
		
		calcChecksum(transfer_position);

		// Command to ask the GPS to start transmitting PVT data
		int[] start_PVT = new int[8];
		start_PVT[0] = DLE;
		start_PVT[1] = Pid_Command_Packet;
		start_PVT[2] = 2;
		start_PVT[3] = Cmnd_Start_Pvt_Data;
		start_PVT[4] = 0; 
		start_PVT[6] = DLE;
		start_PVT[7] = ETX;
		
		calcChecksum(start_PVT);
		
		
		// Acknowledge-packet.
		int[] ack = new int[8];
		ack[0] = DLE;
		ack[1] = Pid_Ack_Byte;
		ack[2] = 2;
		ack[4] = 0;
		ack[6] = DLE;
		ack[7] = ETX;		
		
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
	
		System.out.println("Sending:");

		
		printPacket(turn_off);
		sendPacket(output, turn_off);
		
		/*
		printPacket(transfer_position);
		sendPacket(output, transfer_position);
		*/
		/*
		printPacket(start_PVT);
		sendPacket(output, start_PVT);
		*/
		/*
		printPacket(product_request);
		sendPacket(output, product_request);
		*/
		System.out.println("--");

		int[] packet;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}

		try {
			while (input.available() > 0) {
				packet = readPacket(input);
				if (packet != null) {
					System.out.println("Received:");
					printPacket(packet);
					
					// Send acknowledge.
					ack[3] = packet[1];
					calcChecksum(ack);
					sendPacket(output,ack);
					
					System.out.println("--");
				} else {
					System.out.println("No packet received.");
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}				
			}
		} catch (IOException e) {
			System.out.println("IOError!");
			return;
		}
	}
	
	/** 
	* Temporary method to avoid clutter in main.
	* Returns null if the packet is illegal!! 
	* This method will take care of removing DLE-stuffing from the packet. 
	*/ 
	public static int[] readPacket(BufferedInputStream input) {
		int[] packet;
		try {
			int read = input.read();
			if (read != DLE) 
				return null;
			int id = input.read();
			int length = input.read();
			packet = new int[length + 6];
			if (length == 16)
				input.read();
			
			packet[0] = DLE;
			packet[1] = id;
			packet[2] = length;
			for (int i = 0 ; i < length + 1 ; i++) {
				packet[i + 3] = input.read();
				if (packet[i + 3] == DLE) { // Remove DLE-stuffing
					input.read();
				}
			}
			
			packet[packet.length - 2] = input.read();
			packet[packet.length - 1] = input.read();
			
		} catch (IOException e) {
			System.out.println("Error while reading packet from GPS.");
			return null;
		}
		return packet;
	}
	
	/**
	* Temporary method to avoid clutter of try/catch in main.
	* This method will make sure to add the needed DLE stuffing to the packet.
	*/
	public static void sendPacket(BufferedOutputStream stream, int[] packet) {
		try {
			stream.write(packet[0]);
			stream.write(packet[1]);				
			
			for (int i = 2 ; i < packet.length - 2; i++) {
				stream.write(packet[i]);				
				if (packet[i] == DLE) // Ensure DLE Stuffing
					stream.write(packet[i]);			
			}
			
			stream.write(packet[packet.length - 2]);
			stream.write(packet[packet.length - 1]);
			
			stream.flush();
		} catch (IOException e) {
			System.out.println("Trouble writing to GPS.");
			return;
		}
	}
	/**
	* Temporary method that will display the raw contents of a packet in byte-form.
	*/
	public static void printRawPacket(int[] packet) {
		for (int i = 0 ; i < packet.length ; i++) {
			System.out.print( packet[i] + " ");
		}
		System.out.print("\n");
	}
	
	/** 
	* Utility method that prints packet-information in an easily readable format.
	* Does _not_ check if checksum is correct.
	*/
	public static void printPacket(int[] packet) {
		// Make formal checks to header and trailer.
		if ( (packet[0] != DLE) || (packet[packet.length - 1] != ETX) || (packet[packet.length - 2] != DLE)) {
			System.out.println("Illegal packet.");
			return;
		}

		System.out.println("Packet ID: " + packetID(packet[1]) + " (" + packet[1] + ")");
		System.out.println("Amount of data: " + packet[2] + " bytes.");
		
		switch (packet[1]) {
			case Pid_Command_Packet :
				System.out.println("Command: " + command(toWord(packet[4], packet[3])));
			break;
			
			case Pid_Date_Time_Data :
				System.out.println("Time: " + datetime(packet));
			break;
			
			case Pid_Product_Data :
				System.out.println("Product data:\n" + productData(packet));
			break;
			
			case Pid_Protocol_Array :
				System.out.println("Protocols supported by this GPS:\n" + protocolArray(packet));
			break;
			
			case Pid_Position_Data :
				positionData(packet);
			break;
			case Pid_Pvt_Data :
				// PVTData(packet);
			break;
			
			default : 
				System.out.println("Data:");
				for (int i = 3 ; i < packet.length - 3 ; i++) {
					System.out.print( toHex(packet[i]) + " ");					
					if (packet[i] == 16)
						i++;
					
				}
			System.out.println("");
			break;
		}
		
		System.out.println("Checksum: " + packet[packet.length - 3]);
	}


	
	/**
	* Small utility method to interpret the contents of a Pid_Protocol_Array-packet.
	*/
	
	public static String protocolArray(int[] packet) {
		StringBuffer res = new StringBuffer();
		char tag;
		int data;
		for (int i = 0 ; i < packet[2] ; i += 3) {
			tag = (char) packet[3 + i];
			data = toWord(packet[i + 5], packet[i + 4]);
			res.append("\'" + tag + "\'" + " " + data + "\n");
		}
		return res.toString();
	}
	
	/**
	* Small utility method to interpret the contents of a Pid_
	*/
	public static String positionData(int[] packet) {
		StringBuffer res = new StringBuffer();
		
		int[] data = new int[8];
		for (int i = 0 ; i < 8 ; i++) {
			data[i] = packet[i + 3];
		}				
		
		double pos = toDouble(data);		
		double deg = toDegrees(pos);
		int degrees = (int) deg;
		double minutes = deg - degrees;
		minutes *= 60;		
		res.append("Pos: " + degrees +"?" + minutes + "'\n");
		
		for (int i = 0 ; i < 8 ; i++) {
			data[i] = packet[i + 11];
		}				
		
		pos = toDouble(data);
		deg = toDegrees(pos);		
		degrees = (int) deg;
		minutes = deg - degrees;
		minutes *= 60;		
		res.append("Pos: " + degrees +"?" + minutes + "'\n");
		
		
		return res.toString();
	}
	
	public static double toDegrees(double pos) {
		return pos * ( 180.0d / Math.PI);
	}
	
	/**
	* Small utility method to interpret the contents of a Pid_Product_Data-packet.
	*/
	public static String productData(int[] packet) {
		StringBuffer res = new StringBuffer();
		res.append("Product ID: ");
		res.append(toWord(packet[4], packet[3]));
		res.append('\n');
		
		res.append("Software version: ");
		res.append(toWord(packet[6], packet[4]));
		res.append('\n');
		
		for (int i = 7 ; packet[i] != 0 ; i++) {			
			res.append( (char) packet[i]);
		}
		return res.toString();
	}
	
	/**
	* Small utility method to pretty-print the contents of a date-time-packet.
	*/
	
	public static String datetime(int[] packet) {
		StringBuffer res = new StringBuffer();
		res.append("Month: ");
		res.append(packet[3]);
		res.append('\n');
		
		res.append("Day: ");
		res.append(packet[4]);
		res.append('\n');
		
		res.append("Year: ");
		res.append(toWord(packet[6], packet[5]));
		res.append('\n');		
		
		res.append("Time: ");
		res.append(toWord(packet[8], packet[7]));
		res.append(':');		
		
		res.append(packet[9]);
		res.append(':');	
		res.append(packet[10]);
		
		return res.toString();
	}
	
	/**
	* Concatenates two int-variables into a word (As specified in the "data types"-section of the Garmin protocol).
	* Assuming that msb contains the 8 most significant bits and lsb contains the 8 least significant bits of a 16 bit number.
	*/
	public static int toWord(int msb, int lsb) {
		int sum = lsb;
		sum += msb << 8;
		return sum;
	}
	
	public static double toDouble(int number[]) {					
		System.out.print("Converting: ");
		for (int i = 0 ; i < 8 ; i++)
			System.out.print(number[i] + " ");
		System.out.println("");
			
		long res = 0; 		
		res += ( (long) number[0] );
		res += ( (long) number[1] ) << 8;
		res += ( (long) number[2] ) << 16;
		res += ( (long) number[3] ) << 24;
		res += ( (long) number[4] ) << 32;
		res += ( (long) number[5] ) << 40;
		res += ( (long) number[6] ) << 48;
		res += ( (long) number[7] ) << 56;		
				
		return Double.longBitsToDouble(res);
	} 
	
	/**
	* Small method that ensures that all byte-values are written correctly. 
	*/
	
	public static String toHex(int a) {
		if (a < 16)
			return "0" + Integer.toHexString(a);
		else
			return Integer.toHexString(a);
	}
	
	/**
	* Utility method that returns the type of command (A010-protocol) in an easily readable string-format.	
	*/
	public static String command(int c) {
		switch(c) {
			// A010 Device Command Protocol 1.
			case Cmnd_Transfer_Posn :
				return "Transfer position";
			case Cmnd_Transfer_Time :
				return "Transfer time";
			case Cmnd_Turn_Off_Pwr :
				return "Turn off GPS";
			default :
				return Integer.toString(c);
		}
	}
	
	/**
	* Utility method that returns the type of packet in an easily readable string-format.
	*/ 
	public static String packetID(int id) {
		switch (id) {			
			// L000 Basic Link Protocol packets.
			case Pid_Ack_Byte :
				return "Acknowledge";
			case Pid_Nak_Byte :
				return "Not acknowledged";
			case Pid_Protocol_Array :
				return "Protocol array";
			case Pid_Product_Rqst :
				return "Product request";
			case Pid_Product_Data :
				return "Product data";
				
			// L001 Link Protocol 1
			case Pid_Command_Data :
				return "Command data";
			case 12 :
				return "Transfer Complete";
			case Pid_Date_Time_Data :
				return "Date and time data";
			case Pid_Position_Data :
				return "Position data";
			case 19 :
				return "Waypoint data";
			case 27 :
				return "Record";
			// More available. (p10)

			default :
				return Integer.toString(id);
		}
	}
	
	/**
	* This method will calculate the checksum of a packet according to the directions in the Garmin
	* protocol specification (p8). 
	* Pre: Assumes that packet is a valid Garmin-packet with all fields containing their final values,
	* except for the checksum-field.
	*/
	public static void calcChecksum(int[] packet) {
		int sum = 0;
		for (int i = 1 ; i <= packet.length - 4 ; i++) {
			sum += packet[i];		
		}		
		
		sum = sum % 256;
		sum = sum ^ 255;						
		sum += 1;
		packet[packet.length - 3] = sum;
	}
}

// class InputStreamDLERemover extends 
