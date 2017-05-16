package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.InvalidFieldValue;
import com.diddlebits.gpslib4j.StringValidator;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class RouteHeaderPacket202 extends GarminPacket implements IRouteHeader {
    private static StringValidator IdentValidator;

    private static final long serialVersionUID = -7392917966444014063L;

    protected String ident;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     * @throws PacketNotRecognizedException
     */

    public RouteHeaderPacket202() {
        super();
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident,
                GetIdentValidator());
    }

    public String getPacketType() {
        return "route header";
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) throws InvalidFieldValue {
        GetIdentValidator().throwIfInvalid(GPSFields.IdentField, ident);
        this.ident = ident;
    }

    protected static StringValidator GetIdentValidator() {
        if (IdentValidator == null) {
            try {
                IdentValidator = GarminStringValidatorsFactory.CreateIdent(20,
                        false);
            } catch (InvalidFieldValue e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return IdentValidator;
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Rte_Hdr;
    }
}