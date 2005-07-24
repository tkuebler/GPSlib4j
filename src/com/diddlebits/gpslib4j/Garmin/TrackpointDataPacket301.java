package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointDataPacket301 extends GarminPacket implements
        ITrackpoint {

    protected int index = -1;

    /** Position of trackpoint. */
    protected Position position;

    /** Time of track point sample * */
    protected Date time;

    /** Altitude. */
    protected float alt;

    /** Depth. */
    protected float depth;

    /** new track segment * */
    protected boolean new_trk;

    protected static FloatSpecification AltSpecification = new FloatSpecification(
            -1e24, 1e24, 0.1);

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     * @throws PacketNotRecognizedException
     */

    public TrackpointDataPacket301(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
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

    public float getAltitude() {
        return alt;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        position = visitor.positionField(GPSFields.PositionField, position);
        time = visitor.timeField(LONG_DATE, GPSFields.TimeField, time);
        alt = (float) visitor.floatField(FLOAT32, GPSFields.AltitudeField, alt,
                AltSpecification);
        depth = (float) visitor.floatField(FLOAT32, GPSFields.DepthField,
                depth, AltSpecification);
        new_trk = visitor.boolField(GPSFields.NewTrackField, new_trk);
    }

    public String getPacketType() {
        return "trackpoint";
    }

    public float getDepth() {
        return depth;
    }

    public boolean isNewTrk() {
        return new_trk;
    }
}