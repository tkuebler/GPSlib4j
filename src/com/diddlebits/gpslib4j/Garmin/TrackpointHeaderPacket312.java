package com.diddlebits.gpslib4j.Garmin;

import java.awt.Color;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointHeaderPacket312 extends GarminPacket implements
        ITrackpointHeader {
    private static final long serialVersionUID = -1388806686711583564L;

    protected boolean dspl;

    protected short color;

    protected String ident;

    private static GPSEnumDefinition ColorEnum;

    private static StringValidator IdentValidator;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     * @throws PacketNotRecognizedException
     */

    public TrackpointHeaderPacket312() {
        super();
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        dspl = visitor.boolField(GPSFields.DisplayField, dspl);
        color = (short) visitor.enumField(UINT8, GPSFields.ColorField, color,
                GetColorEnum());
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident,
                GetIdentValidator());
    }

    public short getColor() {
        return color;
    }

    public boolean isDspl() {
        return dspl;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) throws InvalidFieldValue {
        GetIdentValidator().throwIfInvalid(GPSFields.IdentField, ident);
        this.ident = ident;
    }

    private static GPSEnumDefinition GetColorEnum() {
        if (ColorEnum == null) {
            ColorEnum = new GPSEnumDefinition("Color", false);
            ColorEnum.addValue("black", 0, Color.BLACK);
            ColorEnum.addValue("dark red", 1, new Color(128, 0, 0));
            ColorEnum.addValue("dark green", 2, new Color(0, 128, 0));
            ColorEnum.addValue("dark yellow", 3, new Color(128, 128, 0));
            ColorEnum.addValue("dark blue", 4, new Color(0, 0, 128));
            ColorEnum.addValue("dark magenta", 5, new Color(128, 0, 128));
            ColorEnum.addValue("dark cyan", 6, new Color(0, 128, 128));
            ColorEnum.addValue("light grey", 7, Color.LIGHT_GRAY);
            ColorEnum.addValue("dark grey", 8, Color.DARK_GRAY);
            ColorEnum.addValue("red", 9, Color.RED);
            ColorEnum.addValue("green", 10, Color.GREEN);
            ColorEnum.addValue("yellow", 11, Color.YELLOW);
            ColorEnum.addValue("blue", 12, Color.BLUE);
            ColorEnum.addValue("magenta", 13, Color.MAGENTA);
            ColorEnum.addValue("cyan", 14, Color.CYAN);
            ColorEnum.addValue("white", 15, Color.WHITE);
            ColorEnum.addValue("transparent", 16, Color.BLACK);
        }
        return ColorEnum;
    }

    public String getPacketType() {
        return "trackpoint header";
    }

    protected static StringValidator GetIdentValidator() {
        if (IdentValidator == null) {
            try {
                IdentValidator = GarminStringValidatorsFactory.CreateIdent(50,
                        false);
            } catch (InvalidFieldValue e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return IdentValidator;
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Trk_Hdr;
    }
}