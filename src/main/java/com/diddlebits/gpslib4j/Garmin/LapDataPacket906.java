package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a lap-packet.
 */
public class LapDataPacket906 extends GarminPacket implements ILap {
    private static final long serialVersionUID = 2269583804182557006L;

    protected boolean bound = false;

    /** start time */
    protected Date start_time;

    /** total time */
    protected long total_time;

    /** total_distance */
    protected float total_distance;

    /** begin point */
    protected Position start_position;

    /** end point */
    protected Position end_position;

    /** calories */
    protected int calories;

    /** track index */
    protected short track_index;

    /** unused */
    protected short unused;

    protected static FloatSpecification DistanceSpecification = new FloatSpecification(
            0.0, 1e24, 0.1, false);

    protected static IntegerSpecification TotalTimeSpecification = new IntegerSpecification(
            0, 0xFFFFFFFEl, true);

    protected static IntegerSpecification CaloriesSpecification = new IntegerSpecification(
            0, 0xFFFF, false);

    protected static IntegerSpecification IndexSpecification = new IntegerSpecification(
            0, 255, false);

    public LapDataPacket906() {
        super();
    }

    /**
     * Throws a PacketNotRecognizedException if the lap-dataformat is not
     * implemented.
     * 
     * @throws PacketNotRecognizedException
     * @throws InvalidFieldValue
     */
    public void initFromRawPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        if (p.getPID() != GarminRawPacket.Pid_Lap) {
            throw (new PacketNotRecognizedException(GarminRawPacket.Pid_Lap, p
                    .getPID()));
        }

        super.initFromRawPacket(p);
    }

    public Date getStartTime() {
        return start_time;
    }

    public long getTotalTime() {
        return total_time;
    }

    public float getTotalDistance() {
        return total_distance;
    }

    public Position getStartPosition() {
        return start_position;
    }

    public Position getEndPosition() {
        return end_position;
    }

    public int getCalories() {
        return calories;
    }

    public short getTrackIndex() {
        return track_index;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        start_time = visitor.timeField(LONG_DATE, GPSFields.StartTimeField,
                start_time);
        total_time = visitor.intField(UINT32, GPSFields.TotalTimeField,
                total_time, TotalTimeSpecification, 0xFFFFFFFFl);
        total_distance = (float) visitor.floatField(FLOAT32,
                GPSFields.TotalDistanceField, total_distance,
                DistanceSpecification);
        start_position = visitor.positionField(GPSFields.StartPositionField,
                start_position);
        end_position = visitor.positionField(GPSFields.EndPositionField,
                end_position);
        calories = (int) visitor.intField(UINT16, GPSFields.CaloriesField,
                calories, CaloriesSpecification, 0x10000);
        track_index = (short) visitor.intField(UINT8, GPSFields.IndexField,
                track_index, IndexSpecification, 256);
        visitor.intField(UINT8, GPSFields.UnusedField, track_index,
                IndexSpecification, 0);
    }

    public String getPacketType() {
        return "lap";
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Lap;
    }
}