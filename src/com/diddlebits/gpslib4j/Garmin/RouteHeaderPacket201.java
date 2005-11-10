package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.IntegerSpecification;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class RouteHeaderPacket201 extends GarminPacket implements IRouteHeader {
    private static final long serialVersionUID = 6916510619042068716L;

    protected short routeNumber;

    protected String cmnt;

    protected static IntegerSpecification NumberSpecification = new IntegerSpecification(
            0, 0xFF, false);

    public RouteHeaderPacket201() {
        super();
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        routeNumber = (short) visitor.intField(UINT8, GPSFields.IndexField,
                routeNumber, NumberSpecification, 0x100);
        cmnt = visitor.stringField(ACHAR, GPSFields.CommentField, cmnt,
                GarminStringValidatorsFactory.CreateComment(20, true));
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

    public void setIdent(String value) throws InvalidFieldValue {
        routeNumber = (short) NumberSpecification.convertFromString(value);
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Rte_Hdr;
    }
}