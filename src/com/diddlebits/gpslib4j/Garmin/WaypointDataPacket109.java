package com.diddlebits.gpslib4j.Garmin;

import java.awt.Color;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a Waypoint-packet. The Garmin-protocol contains a
 * huge amount of different Waypoint-Packet specifications. Only the one
 * labelled D108 is implemented so far.
 */
public class WaypointDataPacket109 extends GarminPacket implements IWaypoint {
    /** Class of waypoint. */
    protected short wpt_class;

    /** Color of waypoint when displayed on the GPS. */
    protected short color;

    /** How to display the waypoint. */
    protected short display;

    /** Waypoint symbol. */
    protected int smbl;

    /** Subclass of waypoint */
    protected String subclass;

    /** proximity position */
    protected int posn;

    /** Position of waypoint. */
    protected Position position;

    /** Altitude. */
    protected float alt;

    /** Depth. */
    protected float depth;

    /** Proximity distance in meters. */
    protected float dist;

    /** State. */
    protected String state;

    /** Country code. */
    protected String cc;

    /** outbound link ete in seconds. */
    protected long ete;

    /** Waypoint comment. */
    protected String comment;

    /** facility name. */
    protected String facility;

    /** City name. */
    protected String city;

    /** Address number */
    protected String address;

    /** Intersecting road label. */
    protected String cross_road;

    protected String ident;

    protected String addr;

    protected String crossRoad;

    private static GPSEnumDefinition ColorEnum;

    /**
     * Throws a PacketNotRecognizedException if the Waypoint-dataformat is not
     * implemented.
     * @throws InvalidFieldValue 
     */

    public WaypointDataPacket109(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        super();

        if (p.getID() != GarminRawPacket.Pid_Wpt_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Wpt_Data, p.getID()));
        }

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        visitor.intField(UINT8, "dtyp", 1, 1, 1, 1);
        wpt_class = (short) visitor.enumField(UINT8, GPSFields.ClassField, wpt_class,
                WaypointDataPacket108.GetWaypointClassEnum());

        // virtual fields (not used to parse the packet, but shown to the user)
        display = (short) visitor.enumField(UINT8, "-"+GPSFields.DisplayField, display,
                WaypointDataPacket103.GetDisplayEnum());
        color = (short) visitor.enumField(UINT8, "-"+GPSFields.ColorField, color,
                GetColorEnum());

        // internal fields (parsed, but not show to the user)
        short dspl_color = (short) visitor.intField(UINT8, GPSFields.DisplayColorField,
                (color & 0x1F) | (display << 5), 0, 0xEF, 0xFF);
        display = (short) (dspl_color >> 5 & 0x3);
        color = (short) (dspl_color & 0x1F);

        visitor.intField(UINT8, "attr", 0x70, 0x70, 0x70, 0x70);
        smbl = visitor.enumField(UINT16, GPSFields.SymbolField, smbl, WaypointDataPacket108
                .GetSymbolEnum());
        subclass = visitor.stringField(ACHAR, GPSFields.SubClassField, subclass, 18, null);
        position = visitor.positionField(GPSFields.PositionField, position);
        alt = (float) visitor.floatField(FLOAT32, GPSFields.AltitudeField, alt, -1e24, 1e24);
        depth = (float) visitor
                .floatField(FLOAT32, GPSFields.DepthField, depth, -1e24, 1e24);
        dist = (float) visitor.floatField(FLOAT32, GPSFields.ProxymityField, dist,
                -1e24, 1e24);
        state = visitor.stringField(ACHAR, GPSFields.StateField, state, 2, null);
        cc = visitor.stringField(ACHAR, GPSFields.CountryCodeField, cc, 2,
                CommonGarminStringValidators.Get().getCountryCode());
        ete = visitor.intField(UINT32, GPSFields.ETEField, ete, 0, 0xFFFFFFFE, 0xFFFFFFFF);
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident, 100,
                CommonGarminStringValidators.Get().getWaypointIdent());
        comment = visitor.stringField(VCHAR, GPSFields.CommentField, comment, 100, null);
        facility = visitor.stringField(VCHAR, GPSFields.FacilityField, facility, 100, null);
        city = visitor.stringField(VCHAR, GPSFields.CityField, city, 100, null);
        addr = visitor.stringField(VCHAR, GPSFields.AddressField, addr, 100, null);
        crossRoad = visitor.stringField(VCHAR, GPSFields.CrossRoadField, crossRoad, 100,
                null);
    }

    public static GPSEnumDefinition GetColorEnum() {
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
            ColorEnum.addValue("default", 0x1F, Color.BLACK);
        }
        return ColorEnum;
    }

    /**
     * This method returns the position of the waypoint.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * This method returns the name of the waypoint.
     */
    public String getIdent() {
        return ident;
    }

    public String getPacketType() {
        return "waypoint";
    }
}