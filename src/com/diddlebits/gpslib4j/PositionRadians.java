package com.diddlebits.gpslib4j;

/**
 * Class used to store radians, usually latitude or longitude. Contains methods
 * for converting to the format degress,minutes.
 */
public class PositionRadians {
    private final double value;

    /**
     * @param v The amount in degrees.
     * @return A new PositionRadian instance.
     */
    public static PositionRadians CreateFromDegrees(double v) {
        return new PositionRadians(v * Math.PI / 180.0d);
    }
    
    /**
     * Initializes the PositionRadians-object. After the object is constructed,
     * it can't change is value.
     */
    public PositionRadians(double v) {
        value = v;
    }

    public double getRadians() {
        return value;
    }

    /**
     * Returns the degrees part of this object, when converted to coordinates.
     */
    public int getDegrees() {
        return (int) (value * (180.0d / Math.PI));
    }

    /**
     * Returns the minutes part of this object, when converted to coordinates.
     */
    public double getMinutes() {
        double v = value * (180.0d / Math.PI);
        v -= getDegrees();
        return 60 * v; // 60 minutes in one degree.
    }

    /**
     * Tests if the two PositionRadians contains the same value.
     */
    public boolean equals(PositionRadians p) {
        if (value == p.value)
            return true;
        else
            return false;
    }

    /**
     * Tests if this PositionRadians is greater than p.
     */
    public boolean greaterThan(PositionRadians p) {
        if (value > p.value)
            return true;
        else
            return false;
    }

    /**
     * Tests if this PositionRadians is smaller than p.
     */

    public boolean smallerThan(PositionRadians p) {
        if (value < p.value)
            return true;
        else
            return false;
    }

    public String toString() {
        return String.valueOf(getDegrees()) + "\' "
                + String.valueOf((int) getMinutes()) + "\"";
    }
}