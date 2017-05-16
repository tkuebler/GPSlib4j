/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.diddlebits.test;

import com.diddlebits.gpslib4j.Position;

import junit.framework.TestCase;

/**
 * @author tkuebler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PositionTest extends TestCase {
	
	Position position;

	/*
	 * Class under test for void Position(double, double)
	 */
	public void testPositiondoubledouble() {
		position = new Position(12312.0, 123121.231);
	}

	public void testLatitude2DM() {
		System.out.println(Position.Latitude2DM(123412.0));
	}

	public void testLongitude2DM() {
		System.out.println(Position.Longitude2DM(12312.0123));
	}

	/*
	 * Class under test for String toString()
	 */
	public void testToString() {
	}

}
