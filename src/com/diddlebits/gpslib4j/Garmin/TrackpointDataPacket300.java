package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointDataPacket300 extends GarminPacket implements
        ITrackpoint {
    private static final long serialVersionUID = -3699921181493293499L;

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
     * 
     * @throws InvalidFieldValue
     * @throws PacketNotRecognizedException
     */

    public TrackpointDataPacket300() {
        super();
    }

    public int getIndex() {
        return index;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public int getPacketId() {
        return GarminRawPacket.Pid_Trk_data;
    }
}