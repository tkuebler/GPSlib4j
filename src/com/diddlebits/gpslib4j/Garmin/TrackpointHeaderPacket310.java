package com.diddlebits.gpslib4j.Garmin;

import java.awt.Color;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointHeaderPacket310 extends GarminPacket implements
        ITrackpointHeader {
    protected boolean dspl;

    protected short color;

    protected String ident;

    private static GPSEnumDefinition ColorEnum;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is not
     * implemented.
     * @throws InvalidFieldValue 
     * @throws PacketNotRecognizedException 
     */
    public TrackpointHeaderPacket310(GarminRawPacket p) throws PacketNotRecognizedException, InvalidFieldValue {
        super();

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        dspl = visitor.boolField(GPSFields.DisplayField, dspl);
        color = (short) visitor
                .enumField(UINT8, GPSFields.ColorField, color, GetColorEnum());
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident, 50,
                CommonGarminStringValidators.Get().getIdent());
    }

    private static GPSEnumDefinition GetColorEnum() {
        if (ColorEnum == null) {
            ColorEnum = new GPSEnumDefinition("Color");
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
        }
        return ColorEnum;
    }

    public String getPacketType() {
        return "trackpoint header";
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
}