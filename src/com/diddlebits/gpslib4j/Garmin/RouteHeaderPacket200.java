package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.IntegerSpecification;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class RouteHeaderPacket200 extends GarminPacket implements IRouteHeader {
    private static final long serialVersionUID = 3780219741317781687L;

    protected short routeNumber;

    protected static IntegerSpecification NumberSpecification = new IntegerSpecification(
            0, 0xFF, false);

    public RouteHeaderPacket200() {
        super();
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        routeNumber = (short) visitor.intField(UINT8, GPSFields.IndexField,
                routeNumber, NumberSpecification, 0x100);
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

    public void setIdent(String value) throws InvalidFieldValue {
        routeNumber = (short) NumberSpecification.convertFromString(value);
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Rte_Hdr;
    }
}