package com.diddlebits.gpslib4j.test;
import javax.comm.*;

import java.io.*;

/** 
* This class is made to establish simple communication with the GPS. (Ev. 2)
* 
* This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/).
*/

public class dumpInput {	

	public static void main(String args[]) {
		CommPortIdentifier portID;
		SerialPort port;
		if (args.length != 0) {
			System.out.println("Trying to acquire " + args[0]);			
			
			try {
				portID = CommPortIdentifier.getPortIdentifier(args[0]);
			} catch (NoSuchPortException e) {
				System.out.println(args[0] + " could not be opened. No such port.");
				return;
			}
			
			try {
				if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					port = (SerialPort) portID.open("com.diddlebits.gpslib4j", 3000);
					port.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
					System.out.println("Port configuration:");
					System.out.println("Baud-rate: " + port.getBaudRate());
					System.out.println("Parity: " + port.getParity());
					System.out.println("Data bits: " + port.getDataBits());
					System.out.println("Stop bits: " + port.getStopBits());
					System.out.println("Framing enabled: " + port.isReceiveFramingEnabled());
				} else {
					System.out.println("Parallel ports not yet supported.");
					return;
				}
			} catch (PortInUseException e) {
				System.out.println("Port is already in use by " + e.currentOwner);			
				return;
			} catch (UnsupportedCommOperationException e) {
				System.out.println("Problems configuring seriel port: " + e.getMessage());
				return;
			}
		} else {
			System.out.println("Specify port!");
			return;
		}
		
		BufferedInputStream input;
		BufferedOutputStream output;
		try {
			input = new BufferedInputStream(port.getInputStream());
			output = new BufferedOutputStream(port.getOutputStream());
		} catch (IOException e) {
			System.out.println("Error getting serialport-streams!");
			return;
		}
		
		System.out.println("Got " + port.getName());

		// Attempt to get the GPS to shut down.
		System.out.println("Transmitting to GPS...");
		int[] turn_off = new int[8];
		turn_off[0] = 16;
		turn_off[1] = 6;
		turn_off[2] = 2;
		turn_off[3] = 0xFF;
		turn_off[4] = 0x00;
		turn_off[5] = 0xF9;
		turn_off[6] = 16;
		turn_off[7] = 3;
		/*
		turn_off[0] = 16;
		turn_off[1] = 10;
		turn_off[2] = 2;
		turn_off[3] = 8;
		turn_off[4] = 0;
		turn_off[5] = 236;
		turn_off[6] = 16;
		turn_off[7] = 3;
		*/;
		
		try {
			for (int i = 0 ; i < turn_off.length ; i++) {
				output.write(turn_off[i]);
			}
			output.flush();
		} catch (IOException e) {
			System.out.println("Error writing to GPS! Bailing out!");
			System.out.println(e.getMessage());
			return;
		}

		System.out.println("Sleeping for 3 secs...");
		try {
			Thread.sleep(3000);			
		} catch (InterruptedException e) {
			
		}

		try {			
			int read;
			while (input.available() > 0) {
				read = input.read();
				if (read == 16)
					System.out.print('\n');
				System.out.print( toHex(read) + " ");

			}
		} catch (IOException e) {
			System.out.println("IOException! " + e.getMessage());
			return;
		}
	}
	
	public static String toHex(int a) {
		if (a < 16)
			return "0" + Integer.toHexString(a);
		else
			return Integer.toHexString(a);
	}
}