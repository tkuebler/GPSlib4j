package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This packet is transmitted between devices before a large transfer of
 * data-units, ie. a transfer of waypoints.
 * 
 */
public class RecordsPacket extends GarminPacket {
    /** The number of records to come, that this packet announces. */
    protected int number;

    public RecordsPacket(GarminRawPacket p) throws PacketNotRecognizedException, InvalidFieldValue, InvalidPacketException {
        super();

        if (p.getID() != GarminRawPacket.Pid_Records) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Records, p.getID()));
        }

        if (p.getDataLength() != 2) {
            throw (new InvalidPacketException(p.packet, 2));
        }

        initFromRawPacket(p);
    }

    /** Returns the number of records that this packet announces. */
    public int getNumber() {
        return number;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        visitor.intField(UINT16, GPSFields.NumberField, number, 0, 0xFFFF, 0x10000);
    }

    public String getPacketType() {
        return "records";
    }
}