package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.ITimeDate;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates the information of a Garmin-Date-Time-packet.
 */

public class TimeDataPacket600 extends GarminPacket implements ITimeDate {
    protected Date date;

    /**
     * Treats the packet p as a packet containing Time-data. Throws
     * PacketNotRecognizedException if p is not a Time-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     */
    public TimeDataPacket600(GarminRawPacket p) {
        super();

        if (p.getID() != GarminRawPacket.Pid_Date_Time_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Date_Time_Data, p.getID()));
        }

        if (p.getDataLength() != 8) {
            throw (new InvalidPacketException(p.packet, 2));
        }

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        date = visitor.timeField(STRUCT_DATE, GPSFields.TimeField, date);
    }

    public Date getTime() {
        return date;
    }

    public String getPacketType() {
        return "time";
    }
}