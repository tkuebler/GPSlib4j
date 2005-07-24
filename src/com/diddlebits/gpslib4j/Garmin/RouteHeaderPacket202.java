package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class RouteHeaderPacket202 extends GarminPacket implements IRouteHeader {
    protected String ident;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     * @throws PacketNotRecognizedException
     */

    public RouteHeaderPacket202(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        super();

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident,
                GarminStringValidatorsFactory.CreateIdent(20, false));
    }

    public String getPacketType() {
        return "route header";
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
}