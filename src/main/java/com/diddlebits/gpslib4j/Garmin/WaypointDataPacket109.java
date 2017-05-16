package com.diddlebits.gpslib4j.Garmin;

import java.awt.Color;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a Waypoint-packet. The Garmin-protocol contains a
 * huge amount of different Waypoint-Packet specifications. Only the one
 * labelled D108 is implemented so far.
 */
public class WaypointDataPacket109 extends GarminPacket implements IWaypoint,
        IAltitude {
    private static final long serialVersionUID = -6809272644367075552L;

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
    protected String crossRoad;

    protected String ident;

    private static GPSEnumDefinition ColorEnum;

    protected static FloatSpecification AltSpecification = new FloatSpecification(
            -1e24, 1e24, 0.1, true);

    protected static IntegerSpecification DummySpecification = new IntegerSpecification(
            1, 1, false);

    protected static IntegerSpecification Dummy2Specification = new IntegerSpecification(
            0x70, 0x70, false);

    protected static IntegerSpecification DisplayColorSpecification = new IntegerSpecification(
            0, 0xEF, false);

    protected static IntegerSpecification ETESpecification = new IntegerSpecification(
            0, 0xFFFFFFFEl, false);

    private static StringValidator IdentValidator;

    /**
     * Throws a PacketNotRecognizedException if the Waypoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     */

    public WaypointDataPacket109() {
        super();
    }

    /**
     * @throws PacketNotRecognizedException
     * @throws InvalidFieldValue
     */
    public void initFromRawPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        if (p.getPID() != GarminRawPacket.Pid_Wpt_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Wpt_Data, p.getPID()));
        }

        super.initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        visitor.intField(UINT8, "dtyp", 1, DummySpecification, 1);
        wpt_class = (short) visitor.enumField(UINT8, GPSFields.ClassField,
                wpt_class, WaypointDataPacket108.GetWaypointClassEnum());

        // virtual fields (not used to parse the packet, but shown to the user)
        display = (short) visitor.enumField(UINT8,
                "-" + GPSFields.DisplayField, display, WaypointDataPacket103
                        .GetDisplayEnum());
        color = (short) visitor.enumField(UINT8, "-" + GPSFields.ColorField,
                color, GetColorEnum());

        // internal fields (parsed, but not show to the user)
        short dspl_color = (short) visitor.intField(UINT8,
                GPSFields.DisplayColorField, (color & 0x1F) | (display << 5),
                DisplayColorSpecification, 0xFF);
        display = (short) (dspl_color >> 5 & 0x3);
        color = (short) (dspl_color & 0x1F);

        visitor.intField(UINT8, "attr", 0x70, Dummy2Specification, 0x70);
        smbl = visitor.enumField(UINT16, GPSFields.SymbolField, smbl,
                WaypointDataPacket108.GetSymbolEnum());
        subclass = visitor.stringField(ACHAR, GPSFields.SubClassField,
                subclass, GarminStringValidatorsFactory.CreateUnchecked(18,
                        true));
        position = visitor.positionField(GPSFields.PositionField, position);
        alt = (float) visitor.floatField(FLOAT32, GPSFields.AltitudeField, alt,
                AltSpecification);
        depth = (float) visitor.floatField(FLOAT32, GPSFields.DepthField,
                depth, AltSpecification);
        dist = (float) visitor.floatField(FLOAT32, GPSFields.ProxymityField,
                dist, AltSpecification);
        state = visitor.stringField(ACHAR, GPSFields.StateField, state,
                GarminStringValidatorsFactory.CreateUnchecked(2, true));
        cc = visitor.stringField(ACHAR, GPSFields.CountryCodeField, cc,
                GarminStringValidatorsFactory.CreateCountryCode(2, true));
        ete = visitor.intField(UINT32, GPSFields.ETEField, ete,
                ETESpecification, 0xFFFFFFFFl);
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident,
                GetIdentValidator());
        comment = visitor.stringField(VCHAR, GPSFields.CommentField, comment,
                GarminStringValidatorsFactory.CreateUnchecked(100, true));
        facility = visitor.stringField(VCHAR, GPSFields.FacilityField,
                facility, GarminStringValidatorsFactory.CreateUnchecked(100,
                        true));
        city = visitor.stringField(VCHAR, GPSFields.CityField, city,
                GarminStringValidatorsFactory.CreateUnchecked(100, true));
        address = visitor.stringField(VCHAR, GPSFields.AddressField, address,
                GarminStringValidatorsFactory.CreateUnchecked(100, true));
        crossRoad = visitor.stringField(VCHAR, GPSFields.CrossRoadField,
                crossRoad, GarminStringValidatorsFactory.CreateUnchecked(100,
                        true));
    }

    public static GPSEnumDefinition GetColorEnum() {
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

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * This method returns the name of the waypoint.
     */
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) throws InvalidFieldValue {
        GetIdentValidator().throwIfInvalid(GPSFields.IdentField, ident);
        this.ident = ident;
    }

    public String getPacketType() {
        return "waypoint";
    }

    public static GPSEnumDefinition getColorEnum() {
        return ColorEnum;
    }

    public String getAddress() {
        return address;
    }

    public float getAltitude() {
        return alt;
    }

    public String getCc() {
        return cc;
    }

    public String getCity() {
        return city;
    }

    public short getColor() {
        return color;
    }

    public String getComment() {
        return comment;
    }

    public String getCrossRoad() {
        return crossRoad;
    }

    public float getDepth() {
        return depth;
    }

    public short getDisplay() {
        return display;
    }

    public float getDist() {
        return dist;
    }

    public long getEte() {
        return ete;
    }

    public String getFacility() {
        return facility;
    }

    public int getPosn() {
        return posn;
    }

    public int getSmbl() {
        return smbl;
    }

    public String getState() {
        return state;
    }

    public String getSubclass() {
        return subclass;
    }

    public short getWptClass() {
        return wpt_class;
    }

    protected static StringValidator GetIdentValidator() {
        if (IdentValidator == null) {
            try {
                IdentValidator = GarminStringValidatorsFactory
                        .CreateWaypointIdent(100, false);
            } catch (InvalidFieldValue e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return IdentValidator;
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Wpt_Data;
    }
}