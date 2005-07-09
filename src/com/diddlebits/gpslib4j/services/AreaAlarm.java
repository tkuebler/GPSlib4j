package com.diddlebits.gpslib4j.services;
import java.io.IOException;
import java.util.Vector;

import com.diddlebits.gpslib4j.*;

/**
* This class implements an AreaAlarm-service. The class allows the user to specify two positions, which
* will be used as opposite corners in a rectangular area. Whenever the GPS enters or exits the area all 
* listeners are notified through the IAlarmListener-interface.
*/
public class AreaAlarm implements IGPSlistener {
	private GPS gps; 
	// The positions defining the rectangle to be used. 
	private PositionRadians bottom_longitude,
							top_longitude,
							left_latitude,
							right_latitude;

	private Vector alarmListeners;				
	private boolean inside; 
	
	public AreaAlarm(GPS g, Position p1, Position p2) throws FeatureNotSupportedException, IOException {		
		gps = g;
		gps.setAutoTransmit(true);
		gps.addGPSlistener(this);
		
		alarmListeners = new Vector();
		
		PositionRadians l1, l2;
		l1 = p1.getLatitude();
		l2 = p2.getLatitude();
		if (l1.equals(l2))
			throw (new RuntimeException("No area. Latitude of point 1 equals latitude of point 2."));
			
		if (l1.greaterThan(l2)) {
			right_latitude = l1;
			left_latitude = l2;
		} else {
			right_latitude = l2;
			left_latitude = l1;
		}

		l1 = p1.getLongitude();
		l2 = p2.getLongitude();
		if (l1.equals(l2))
			throw (new RuntimeException("No area. Longitude of point 1 equals longitude of point 2."));
		
		if (l1.greaterThan(l2)) {
			top_longitude = l1;
			bottom_longitude = l2;
		} else {
			top_longitude = l2;
			bottom_longitude = l1;
		}
	}
	
	/**
	* Adds l to the list of listeners interested in receiving notification when the GPS enters or exits the area.
	*/
	public void addAlarmListener(IAlarmListener l) {
		// Only allow a listener to be registered once.		
		if (alarmListeners.contains(l))
			return;
		
		alarmListeners.add(l);
		return;						
	}

	/**
	* Removes the the Alarm-listener l from the list of Waypoint-listeners.
	*/
	public void removeAlarmListener(IAlarmListener l) {
		while (alarmListeners.removeElement(l)) {}
	}
		

	/**
	* This method propagates the information that the gps has exited the area to all listeners.
	*/
	protected void fireOutside() {
		for (int i = 0 ; i < alarmListeners.size() ; i++) {
			((IAlarmListener) alarmListeners.elementAt(i)).exitedAlarm();
		}				
	}
	
	/**
	* This method propagates the information that the gps has entered the area to all listeners.
	*/
	protected void fireInside() {
		for (int i = 0 ; i < alarmListeners.size() ; i++) {
			((IAlarmListener) alarmListeners.elementAt(i)).enteredAlarm();
		}				
	}
	
	public void positionReceived(IPosition pos) {
		System.out.println("Areaalarm: Received pos. Analyzing!");
        Position position = pos.getPosition();
		if (inside == false) {
			if ( 	(position.getLatitude().greaterThan(left_latitude)) && 
					(position.getLatitude().smallerThan(right_latitude)) &&
					(position.getLongitude().greaterThan(bottom_longitude)) &&
					(position.getLongitude().smallerThan(top_longitude))) {
				inside = true;
				fireInside();				
			}
		} else {
			if (   !((position.getLatitude().greaterThan(left_latitude)) && 
					(position.getLatitude().smallerThan(right_latitude)) &&
					(position.getLongitude().greaterThan(bottom_longitude)) &&
					(position.getLongitude().smallerThan(top_longitude)))) {
				inside = false;
				fireOutside();
			}
		}		
	}

    public void timeDateReceived(ITimeDate d) {
    }
}
