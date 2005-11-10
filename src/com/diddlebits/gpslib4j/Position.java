package com.diddlebits.gpslib4j;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This is an immutable class meant for containing latitude/longitude positions.
 */

public class Position implements Serializable {
    private static final long serialVersionUID = 7429097315442171838L;

    private static final double MIN_PRECISION = 0.00001;

    private double lat, lon;
    private static NumberFormat minutesFormat = new DecimalFormat("0.00");
    private static NumberFormat degreesFormat = new DecimalFormat("00");
    
    /**
     * Makes a new position. Initializes the latitude and the longitude to 0.
     * 
     * note, useless without setter methods.
     */
    public Position() {
        lat = 0;
        lon = 0;
    }

    /**
     * Initialize the position from the longitude and the latitude given in
     * degrees.
     */
    public Position(double la, double lo) {
        lat = la;
        lon = lo;
    }

    /**
     * Initializes the position object from an IPosition reference.
     */
    public Position(IPosition pos) {
        Position other = pos.getPosition();
        lat = other.lat;
        lon = other.lon;
    }

    /**
     * Returns the latitude in degrees of this position.
     */
    public double getLatitudeDegrees() {
        return lat;
    }

    /**
     * Returns the longitude in degrees of this position.
     */
    public double getLongitudeDegrees() {
        return lon;
    }

    /**
     * Returns the latitude in radian of this position.
     */
    public PositionRadians getLatitude() {
        return PositionRadians.CreateFromDegrees(lat);
    }

    /**
     * Returns the longitude in radian of this position.
     */
    public PositionRadians getLongitude() {
        return PositionRadians.CreateFromDegrees(lon);
    }

    public static String Latitude2DM(double lat) {
        char direction = 'N';
        if (lat < 0) {
            lat = -lat;
            direction = 'S';
        }
        int intPart=(int)Math.floor(lat);
        String minutes= minutesFormat.format((lat-intPart)*60.0);
        degreesFormat.setMaximumIntegerDigits(2);
        String degrees = degreesFormat.format(intPart);
        //Object params[]={new Character(direction), new Integer(intPart), new Double(minutes)};
        //return String.format("%c%02d'%02.3f", params);
        return direction + degrees + "'" + minutes;
    }

    public static String Longitude2DM(double lon) {
        char direction = 'E';
        if (lon < 0) {
            lon = -lon;
            direction = 'O';
        }
        int intPart=(int)Math.floor(lon);
        String minutes= minutesFormat.format((lon-intPart)*60.0);
        degreesFormat.setMaximumIntegerDigits(2);
        String degrees = degreesFormat.format(intPart);
        //Object params[]={new Character(direction), new Integer(intPart), new Double(minutes)};
        //return String.format("%c%03d'%02.3f", params);
        return direction + degrees + "'" + minutes;
    }

    public String toString() {
        return Latitude2DM(lat) + " x " + Longitude2DM(lon);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other instanceof Position) {
            Position pos = (Position) other;
            return Math.abs(pos.lat - lat) < MIN_PRECISION
                    && Math.abs(pos.lat - lat) < MIN_PRECISION;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) Math.round((lat * lon) / (80 * 180) * Integer.MAX_VALUE);
    }
}