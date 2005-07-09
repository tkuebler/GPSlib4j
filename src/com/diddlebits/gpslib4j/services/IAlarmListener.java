package com.diddlebits.gpslib4j.services;

/**
* This interface allows a class to listen on an AreaAlarm. 
*/
public interface IAlarmListener {
	/**
	* This method is called by the AreaAlarm when the GPS exists the area.
	*/
	public void exitedAlarm();

	/**
	* This method is called by the AreaAlarm when the GPS enters the area.
	*/	
	public void enteredAlarm();
}