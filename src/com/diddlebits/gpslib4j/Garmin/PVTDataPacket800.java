package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates the PVT (Position, velocity and time) packet. After
 * receiving a Cmnd_Start_Pvt-packet, the GPS will continually transmit packets
 * of the PVT-type.
 */

public class PVTDataPacket800 extends GarminPacket implements IPosition {

    protected float alt; // Altitude

    protected float epe; // Estimated position error

    protected float eph; // Horizontal epe

    protected float epv; // Vertical epe

    protected int fix; // Position fix

    protected double tow; // Time of week (seconds).

    protected Position position; // Position

    protected float veast; // Velocity east.

    protected float vnorth; // Velocity north.

    protected float vup; // Velocity up.

    protected float msl_hght; // 

    protected int leap_scnds; // Time difference between GPS and GMT (UTC)

    protected long wn_days; // Week number days.

    protected static GPSEnumDefinition PositionFixEnum;

    /**
     * Treats the packet p as a packet containing PVT-data. Throws
     * PacketNotRecognizedException if p is not a PVT-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     */
    public PVTDataPacket800(GarminRawPacket p) {
        super();

        if (p.getID() != GarminRawPacket.Pid_Pvt_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Pvt_Data, p.getID()));
        }

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        visitor.floatField(FLOAT32, GPSFields.AltitudeField, alt, -1e24, 1e24);
        visitor.floatField(FLOAT32, GPSFields.EPEField, epe, -1e24,
                1e24);
        visitor.floatField(FLOAT32, GPSFields.HEPEField, eph,
                -1e24, 1e24);
        visitor.floatField(FLOAT32, GPSFields.VEPEField, epv,
                -1e24, 1e24);
        visitor.enumField(UINT16, GPSFields.PositionFixTypeField, fix,
                GetPositionFixEnum());
        visitor.floatField(FLOAT32, GPSFields.TimeOfWeekField, tow, 0.0,
                7.0 * 24.0 * 3600.0);
        visitor.positionField(GPSFields.PositionField, position);
        visitor.floatField(FLOAT32, GPSFields.VelocityEastField, veast, -1e24, 1e24);
        visitor.floatField(FLOAT32, GPSFields.VelocityNorthField, vnorth, -1e24, 1e24);
        visitor.floatField(FLOAT32, GPSFields.VelocityUpField, vup, -1e24, 1e24);
        visitor.floatField(FLOAT32, GPSFields.MslHeightField,
                msl_hght, -1e24, 1e24);
        visitor.intField(SINT16, GPSFields.LeapSecondsField, leap_scnds,
                -0x8000, 0x7FFF, 0x8000);
        visitor.intField(UINT32, GPSFields.WeekNumberdaysField, wn_days, 0, 0xFFFF,
                0x10000);
    }

    private static GPSEnumDefinition GetPositionFixEnum() {
        if (PositionFixEnum == null) {
            PositionFixEnum = new GPSEnumDefinition("PositionFix");
            PositionFixEnum.addValue("unusable", 0, null);
            PositionFixEnum.addValue("invalid", 1, null);
            PositionFixEnum.addValue("2D", 2, null);
            PositionFixEnum.addValue("3D", 3, null);
            PositionFixEnum.addValue("2D diff", 4, null);
            PositionFixEnum.addValue("3D diff", 5, null);
        }
        return PositionFixEnum;
    }

    public String getPacketType() {
        return "pvt";
    }

    public Position getPosition() {
        return position;
    }

    public float getAlt() {
        return alt;
    }

    public float getEpe() {
        return epe;
    }

    public float getEph() {
        return eph;
    }

    public float getEpv() {
        return epv;
    }

    public int getFix() {
        return fix;
    }

    public int getLeapScnds() {
        return leap_scnds;
    }

    public float getMslHght() {
        return msl_hght;
    }

    public double getTow() {
        return tow;
    }

    public float getVeast() {
        return veast;
    }

    public float getVnorth() {
        return vnorth;
    }

    public float getVup() {
        return vup;
    }

    public long getWnDays() {
        return wn_days;
    }
}