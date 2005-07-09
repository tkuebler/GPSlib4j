package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class RouteHeaderPacket200 extends GarminPacket implements IRouteHeader {
    protected short routeNumber;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     */

    public RouteHeaderPacket200(GarminRawPacket p) {
        super();

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        routeNumber = (short) visitor.intField(UINT8, GPSFields.IndexField,
                routeNumber, 0, 0xFF, 0x100);
    }

    public String getPacketType() {
        return "route header";
    }

    public short getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(short routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getIdent() {
        return String.valueOf(routeNumber);
    }
}