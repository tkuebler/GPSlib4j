package com.diddlebits.gpslib4j.examples;
import com.diddlebits.gpslib4j.services.*;
import com.diddlebits.gpslib4j.Garmin.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.comm.*;

import com.diddlebits.gpslib4j.*;

/** This code is based on Henirk Aasted Sorensen's dk.itu.haas.GPS library (http://www.aasted.org/gps/). */

public class AreaAlarmDemo extends JFrame implements ActionListener, IGPSlistener, IAlarmListener {
	GPS gps;		
	AreaAlarm alarm;
	Position pos1 = null, pos2 = null;
	IPosition current = null;	
	JLabel indicator; 
	JButton mark1;
	JButton mark2;
	BufferedInputStream input;
	BufferedOutputStream output;

	
	public static void main(String args[]) {
		new AreaAlarmDemo();
	}
	
	public void actionPerformed(ActionEvent e) {
		if ( (e.getActionCommand().equals("mark1")) && (current != null)) {
			mark1.setEnabled(false);
			pos1 = new Position(current);
			current = null;
		}
		
		if ( (e.getActionCommand().equals("mark2")) && (current != null)) {
			mark2.setEnabled(false);
			pos2 = new Position(current);
			current = null;			
		}
		
		if ( (pos1 != null) && (pos2 != null) ) {
			indicator.setText("Not inside area.");
			alarm = new AreaAlarm(gps, pos1, pos2);
			alarm.addAlarmListener(this);
			gps.removeGPSListener(this);
		}
	}
		
	
	public void exitedAlarm() {
		indicator.setText("Not inside area.");
	}

	public void enteredAlarm() {
		indicator.setText("Inside area.");
	}
	
	public AreaAlarmDemo() {		
		super("AreaAlarm-demonstration");
		CommPort port;
		
		try {
			port = CommPortIdentifier.getPortIdentifier("COM1").open("AreaAlarmDemo", 3000);
		} catch (NoSuchPortException e) {
			System.out.println("COM1 not found!");
			return;
		} catch (PortInUseException e) {
			System.out.println("Port already in use by " + e.currentOwner);
			return;
		}
		
		try {
			input = new BufferedInputStream(port.getInputStream());
			output = new BufferedOutputStream(port.getOutputStream());			
		} catch (IOException e) {
			System.out.println("Error opening COM1");
			return;
		}		
				
		gps = new GarminGPS(input, output);
		gps.setAutoTransmit(true);
		gps.addGPSlistener(this);
		
		
		indicator = new JLabel("Nothing recorded yet.", JLabel.CENTER);
		getContentPane().setLayout(new BorderLayout());
		
		mark1 = new JButton("Mark position 1");
		mark1.setActionCommand("mark1");
		mark1.addActionListener(this);
		mark2 = new JButton("Mark position 2");
		mark2.setActionCommand("mark2");
		mark2.addActionListener(this);
		
		
		getContentPane().add(mark1, BorderLayout.NORTH);
		getContentPane().add(mark2, BorderLayout.SOUTH);
		getContentPane().add(indicator, BorderLayout.CENTER);
				
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(320,200);
		//setLocationRelativeTo(null);				
		show();
		
		System.out.println(gps.getDescription());
	}		
		
	public void timeReceived(ITime t) {}
	public void dateReceived(IDate d) {}
	
	public void positionReceived(IPosition pos) {
		System.out.println("Received!");
		current = pos;
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.IGPSlistener#productInfoReceived()
	 */
	public void productInfoReceived(IProductData pd) {
		// TODO Auto-generated method stub
		
	}
}