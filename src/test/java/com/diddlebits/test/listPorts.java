package com.diddlebits.test;
import javax.comm.*;

import java.util.Enumeration;

public class listPorts {	
	public static void main(String args[]) {
		if (args.length != 0) {
			System.out.println("Trying to acquire " + args[0]);
			// Try to obtain the COM1-port.
			CommPortIdentifier port;
			
			try {
				port = CommPortIdentifier.getPortIdentifier(args[0]);
			} catch (NoSuchPortException e) {
				System.out.println(args[0] + " could not be opened. No such port.");
				return;
			}
			
			try {
				port.open("com.diddlebits.gpslib4j", 30);
			} catch (PortInUseException e) {
				System.out.println("Port is already in use by " + e.currentOwner);			
			}
		}
		
		Enumeration e = CommPortIdentifier.getPortIdentifiers();		
		for ( ; e.hasMoreElements() ;) {
			CommPortIdentifier id = (CommPortIdentifier) e.nextElement();
			System.out.print( id.getName());
			System.out.print(" Owner: " + id.getCurrentOwner());
			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println(" SERIAL");
			}
			
			if (id.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
				System.out.println(" PARALLEL");
			}
			
		}
	}
}