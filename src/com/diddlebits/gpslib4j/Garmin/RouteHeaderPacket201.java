package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class RouteHeaderPacket201 extends GarminPacket implements IRouteHeader {
    protected short routeNumber;

    protected String cmnt;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     */

    public RouteHeaderPacket201(GarminRawPacket p) {
        super();

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        routeNumber = (short) visitor.intField(UINT8, GPSFields.IndexField,
                routeNumber, 0, 0xFF, 0x100);
        cmnt = visitor.stringField(ACHAR, GPSFields.CommentField, cmnt, 20,
                CommonGarminStringValidators.Get().getComment());
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

    public String getCmnt() {
        return cmnt;
    }

    public void setCmnt(String cmnt) {
        this.cmnt = cmnt;
    }

    public String getIdent() {
        return String.valueOf(routeNumber);
    }
}