package com.diddlebits.gpslib4j.examples;
import com.diddlebits.gpslib4j.services.*;
import com.diddlebits.gpslib4j.Garmin.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

import javax.comm.*;

import com.diddlebits.gpslib4j.*;

public class AreaAlarmDemo extends JFrame implements ActionListener, IGPSlistener, IAlarmListener {
    private static final long serialVersionUID=-6016081676813267654L;

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
			try {
                alarm = new AreaAlarm(gps, pos1, pos2);
                indicator.setText("Not inside area.");
                alarm.addAlarmListener(this);
                gps.removeGPSListener(this);
            } catch (FeatureNotSupportedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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
        gps.addGPSlistener(this);
		try {
            gps.setAutoTransmit(true);
        } catch (FeatureNotSupportedException e) {
            System.out.println("AutoTransmit mode not supported by this GPS");
        } catch (IOException e) {
            System.out.println("Communication error");
        }
		
		
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
		setLocationRelativeTo(null);				
		setVisible(true);
		
		System.out.println(gps.getDescription());
	}		
		
	public void positionReceived(IPosition pos) {
		System.out.println("Received!");
		current = pos;
	}

    public void timeDateReceived(ITimeDate d)
    {
    }
}