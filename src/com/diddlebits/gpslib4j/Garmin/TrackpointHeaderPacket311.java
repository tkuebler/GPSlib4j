package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.ITrackpointHeader;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointHeaderPacket311 extends GarminPacket implements
        ITrackpointHeader {
    protected int index = -1;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     */

    public TrackpointHeaderPacket311(GarminRawPacket p) {
        super();

        initFromRawPacket(p);
    }

    public int getIndex() {
        return index;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        index = (int) visitor.intField(UINT16, GPSFields.IndexField, index, 0, 0xFFFFFFFE,
                0xFFFFFFFF);
    }

    public String getPacketType() {
        return "trackpoint header";
    }

    public String getIdent() {
        return String.valueOf(index);
    }
}