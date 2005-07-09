package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointDataPacket302 extends GarminPacket implements
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

    /** Temperature. */
    protected float temp;

    /** new track segment * */
    protected boolean new_trk;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     */
    public TrackpointDataPacket302(GarminRawPacket p) {
        super();

        initFromRawPacket(p);
    }

    public boolean isHeader() {
        if (index >= 0)
            return true;
        return false;
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

    public float getTemp() {
        return temp;
    }

    public float getAltitude() {
        return alt;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        position = visitor.positionField(GPSFields.PositionField, position);
        time = visitor.timeField(LONG_DATE, GPSFields.TimeField, time);
        alt = (float) visitor.floatField(FLOAT32, GPSFields.AltitudeField, alt, -1e24, 1e24);
        depth = (float) visitor
                .floatField(FLOAT32, GPSFields.DepthField, depth, -1e24, 1e24);
        temp = (float) visitor.floatField(FLOAT32, GPSFields.TemperatureField, depth, -1e24, 1e24);
        new_trk = visitor.boolField("new track", new_trk);
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