package com.diddlebits.gpslib4j;

/**
 * This is a class meant for containing latitude/longitude positions.
 */

public class Position {
    private PositionRadians lat, lon;

    /**
     * Makes a new position. Initializes the latitude and the longitude to 0.
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Initializes the Position with la as the latitude and lo as the longitude.
     */
    public Position(double la, double lo) {
        lat = new PositionRadians(la);
        lon = new PositionRadians(lo);
    }

    public Position(PositionRadians la, PositionRadians lo) {
        lat = la;
        lon = lo;
    }

    /**
     * Initializes the position object from an IPosition reference.
     */
    public Position(IPosition pos) {
        Position other = pos.getPosition();
        lat = other.getLatitude();
        lon = other.getLongitude();
    }

    /**
     * Sets the latitude of this position.
     */
    public void setLatitude(PositionRadians l) {
        lat = l;
    }

    /**
     * Sets the longitude of this position.
     */
    public void setLongitude(PositionRadians l) {
        lon = l;
    }

    /**
     * Returns the latitude of this position.
     */
    public PositionRadians getLatitude() {
        return lat;
    }

    /**
     * Returns the longitude of this position.
     */
    public PositionRadians getLongitude() {
        return lon;
    }

    public String toString() {

        if (lat == null || lon == null)
            return "error printing position packet, long or lat is null";
        return String.valueOf(lat.getDegrees()) + "' "
                + String.valueOf(lat.getMinutes()) + "\" x "
                + String.valueOf(lon.getDegrees()) + "' "
                + String.valueOf(lon.getMinutes());
    }

}