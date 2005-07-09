package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointDataPacket300 extends GarminPacket implements
        ITrackpoint {

    protected int index = -1;

    /** Latitude and longitude of trackpoint. */
    protected Position position;

    /** Time of track point sample * */
    protected Date time;

    /** new track segment * */
    protected boolean new_trk;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     */

    public TrackpointDataPacket300(GarminRawPacket p) {
        super();

        initFromRawPacket(p);
    }

    public int getIndex() {
        return index;
    }

    public Position getPosition() {
        return position;
    }

    public Date getTime() {
        return time;
    }

    public boolean isNewTrk() {
        return new_trk;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        position = visitor.positionField(GPSFields.PositionField, position);
        time = visitor.timeField(LONG_DATE, GPSFields.TimeField, time);
        new_trk = visitor.boolField(GPSFields.NewTrackField, new_trk);
    }

    public String getPacketType() {
        return "trackpoint";
    }
}