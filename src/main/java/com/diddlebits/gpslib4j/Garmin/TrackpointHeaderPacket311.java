package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.ITrackpointHeader;
import com.diddlebits.gpslib4j.IntegerSpecification;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointHeaderPacket311 extends GarminPacket implements
        ITrackpointHeader {
    private static final long serialVersionUID = 6974659240274260921L;

    protected int index = -1;

    protected static IntegerSpecification IndexSpecification = new IntegerSpecification(
            0, 0xFFFFFFFEl, false);

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     * @throws PacketNotRecognizedException
     */

    public TrackpointHeaderPacket311() {
        super();
    }

    public int getIndex() {
        return index;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        index = (int) visitor.intField(UINT16, GPSFields.IndexField, index,
                IndexSpecification, 0xFFFFFFFFl);
    }

    public String getPacketType() {
        return "trackpoint header";
    }

    public String getIdent() {
        return String.valueOf(index);
    }

    public void setIdent(String value) throws InvalidFieldValue {
        index = (short) IndexSpecification.convertFromString(value);
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Trk_Hdr;
    }
}