package com.diddlebits.gpslib4j.Garmin;

import java.awt.Color;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a Waypoint-packet. The Garmin-protocol contains a
 * huge amount of different Waypoint-Packet specifications. Only the one
 * labelled D108 is implemented so far.
 */
public class WaypointDataPacket108 extends GarminPacket implements IWaypoint {
    /** Class of waypoint. */
    protected short wpt_class;

    /** Color of waypoint when displayed on the GPS. */
    protected short color;

    /** Display options. */
    protected short dspl;

    /** Attributes. */
    protected short attr = 0x60;

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

    /** Waypoint comment. */
    protected String comment;

    /** facility name. */
    protected String facility;

    /** City name. */
    protected String city;

    /** Address number */
    protected String addr;

    /** Intersecting road label. */
    protected String crossRoad;

    protected String ident;

    private static GPSEnumDefinition WaypointClassEnum;

    private static GPSEnumDefinition ColorEnum;

    private static GPSEnumDefinition SymbolEnum;

    /**
     * Throws a PacketNotRecognizedException if the Waypoint-dataformat is not
     * implemented.
     */

    public WaypointDataPacket108(GarminRawPacket p)
            throws PacketNotRecognizedException {
        super();

        if (p.getID() != GarminRawPacket.Pid_Wpt_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Wpt_Data, p.getID()));
        }

        initFromRawPacket(p);
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        wpt_class = (short) visitor.enumField(UINT8, GPSFields.ClassField, wpt_class,
                GetWaypointClassEnum());
        color = (short) visitor
                .enumField(UINT8, GPSFields.ColorField, color, GetColorEnum());
        dspl = (short) visitor.enumField(UINT8, GPSFields.DisplayField, dspl,
                WaypointDataPacket103.GetDisplayEnum());
        visitor.intField(UINT8, GPSFields.AttrField, 0x60, 0x60, 0x60, 0x60);
        smbl = visitor.enumField(UINT16, GPSFields.SymbolField, smbl, GetSymbolEnum());
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
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident, 100,
                CommonGarminStringValidators.Get().getWaypointIdent());
        if (wpt_class != 0x00) {
            // only user waypoints can have comments
            comment = "";
        }
        comment = visitor.stringField(VCHAR, GPSFields.CommentField, comment, 100,
                CommonGarminStringValidators.Get().getComment());
        facility = visitor.stringField(VCHAR, GPSFields.FacilityField, facility, 100, null);
        city = visitor.stringField(VCHAR, GPSFields.CityField, city, 100, null);
        addr = visitor.stringField(VCHAR, GPSFields.AddressField, addr, 100, null);
        crossRoad = visitor.stringField(VCHAR, GPSFields.CrossRoadField, crossRoad, 100,
                null);
    }

    public static GPSEnumDefinition GetWaypointClassEnum() {
        if (WaypointClassEnum == null) {
            WaypointClassEnum = new GPSEnumDefinition("WaypointClass");
            WaypointClassEnum.addValue("user", 0x00, null);
            WaypointClassEnum.addValue("aviation airport", 0x40, null);
            WaypointClassEnum.addValue("aviation intersection", 0x41, null);
            WaypointClassEnum.addValue("aviation NDB", 0x42, null);
            WaypointClassEnum.addValue("aviation VOR", 0x43, null);
            WaypointClassEnum.addValue("aviation airport runway", 0x44, null);
            WaypointClassEnum.addValue("aviation airport intersection", 0x45,
                    null);
            WaypointClassEnum.addValue("aviation airport ndb", 0x46, null);
            WaypointClassEnum.addValue("map point", 0x80, null);
            WaypointClassEnum.addValue("map area", 0x81, null);
            WaypointClassEnum.addValue("map intersection", 0x82, null);
            WaypointClassEnum.addValue("map address", 0x83, null);
            WaypointClassEnum.addValue("map line", 0x84, null);
        }
        return WaypointClassEnum;
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
            ColorEnum.addValue("default", 255, Color.BLACK);
        }
        return ColorEnum;
    }

    public static GPSEnumDefinition GetSymbolEnum() {
        if (SymbolEnum == null) {
            SymbolEnum = new GPSEnumDefinition("Symbol");
            SymbolEnum.addValue("white anchor symbol", 0, null);
            SymbolEnum.addValue("white bell symbol", 1, null);
            SymbolEnum.addValue("green diamond symbol", 2, null);
            SymbolEnum.addValue("red diamond symbol", 3, null);
            SymbolEnum.addValue("diver down flag 1", 4, null);
            SymbolEnum.addValue("diver down flag 2", 5, null);
            SymbolEnum.addValue("white dollar symbol", 6, null);
            SymbolEnum.addValue("white fish symbol", 7, null);
            SymbolEnum.addValue("white fuel symbol", 8, null);
            SymbolEnum.addValue("white horn symbol", 9, null);
            SymbolEnum.addValue("white house symbol", 10, null);
            SymbolEnum.addValue("white knife & fork symbol", 11, null);
            SymbolEnum.addValue("white light symbol", 12, null);
            SymbolEnum.addValue("white mug symbol", 13, null);
            SymbolEnum.addValue("white skull and crossbones symbol", 14, null);
            SymbolEnum.addValue("green square symbol", 15, null);
            SymbolEnum.addValue("red square symbol", 16, null);
            SymbolEnum.addValue("white buoy waypoint symbol", 17, null);
            SymbolEnum.addValue("waypoint dot", 18, null);
            SymbolEnum.addValue("white wreck symbol", 19, null);
            SymbolEnum.addValue("null symbol (transparent)", 20, null);
            SymbolEnum.addValue("man overboard symbol", 21, null);
            SymbolEnum.addValue("amber map buoy symbol", 22, null);
            SymbolEnum.addValue("black map buoy symbol", 23, null);
            SymbolEnum.addValue("blue map buoy symbol", 24, null);
            SymbolEnum.addValue("green map buoy symbol", 25, null);
            SymbolEnum.addValue("green/red map buoy symbol", 26, null);
            SymbolEnum.addValue("green/white map buoy symbol", 27, null);
            SymbolEnum.addValue("orange map buoy symbol", 28, null);
            SymbolEnum.addValue("red map buoy symbol", 29, null);
            SymbolEnum.addValue("red/green map buoy symbol", 30, null);
            SymbolEnum.addValue("red/white map buoy symbol", 31, null);
            SymbolEnum.addValue("violet map buoy symbol", 32, null);
            SymbolEnum.addValue("white map buoy symbol", 33, null);
            SymbolEnum.addValue("white/green map buoy symbol", 34, null);
            SymbolEnum.addValue("white/red map buoy symbol", 35, null);
            SymbolEnum.addValue("white dot symbol", 36, null);
            SymbolEnum.addValue("radio beacon symbol", 37, null);
            SymbolEnum.addValue("boat ramp symbol", 150, null);
            SymbolEnum.addValue("campground symbol", 151, null);
            SymbolEnum.addValue("restrooms symbol", 152, null);
            SymbolEnum.addValue("shower symbol", 153, null);
            SymbolEnum.addValue("drinking water symbol", 154, null);
            SymbolEnum.addValue("telephone symbol", 155, null);
            SymbolEnum.addValue("first aid symbol", 156, null);
            SymbolEnum.addValue("information symbol", 157, null);
            SymbolEnum.addValue("parking symbol", 158, null);
            SymbolEnum.addValue("park symbol", 159, null);
            SymbolEnum.addValue("picnic symbol", 160, null);
            SymbolEnum.addValue("scenic area symbol", 161, null);
            SymbolEnum.addValue("skiing symbol", 162, null);
            SymbolEnum.addValue("swimming symbol", 163, null);
            SymbolEnum.addValue("dam symbol", 164, null);
            SymbolEnum.addValue("controlled area symbol", 165, null);
            SymbolEnum.addValue("danger symbol", 166, null);
            SymbolEnum.addValue("restricted area symbol", 167, null);
            SymbolEnum.addValue("null symbol", 168, null);
            SymbolEnum.addValue("ball symbol", 169, null);
            SymbolEnum.addValue("car symbol", 170, null);
            SymbolEnum.addValue("deer symbol", 171, null);
            SymbolEnum.addValue("shopping cart symbol", 172, null);
            SymbolEnum.addValue("lodging symbol", 173, null);
            SymbolEnum.addValue("mine symbol", 174, null);
            SymbolEnum.addValue("trail head symbol", 175, null);
            SymbolEnum.addValue("truck stop symbol", 176, null);
            SymbolEnum.addValue("user exit symbol", 177, null);
            SymbolEnum.addValue("flag symbol", 178, null);
            SymbolEnum.addValue("circle with x in the center", 179, null);
            SymbolEnum.addValue("open 24 hours symbol", 180, null);
            SymbolEnum.addValue("U Fishing Hot Spots? Facility", 181, null);
            SymbolEnum.addValue("Bottom Conditions", 182, null);
            SymbolEnum.addValue("Tide/Current Prediction Station", 183, null);
            SymbolEnum.addValue("U anchor prohibited symbol", 184, null);
            SymbolEnum.addValue("U beacon symbol", 185, null);
            SymbolEnum.addValue("U coast guard symbol", 186, null);
            SymbolEnum.addValue("U reef symbol", 187, null);
            SymbolEnum.addValue("U weedbed symbol", 188, null);
            SymbolEnum.addValue("U dropoff symbol", 189, null);
            SymbolEnum.addValue("U dock symbol", 190, null);
            SymbolEnum.addValue("U marina symbol", 191, null);
            SymbolEnum.addValue("U bait and tackle symbol", 192, null);
            SymbolEnum.addValue("U stump symbol", 193, null);
            SymbolEnum.addValue("interstate hwy symbol", 8192, null);
            SymbolEnum.addValue("us hwy symbol", 8193, null);
            SymbolEnum.addValue("state hwy symbol", 8194, null);
            SymbolEnum.addValue("mile marker symbol", 8195, null);
            SymbolEnum.addValue("TracBack (feet) symbol", 8196, null);
            SymbolEnum.addValue("golf symbol", 8197, null);
            SymbolEnum.addValue("small city symbol", 8198, null);
            SymbolEnum.addValue("medium city symbol", 8199, null);
            SymbolEnum.addValue("large city symbol", 8200, null);
            SymbolEnum.addValue("intl freeway hwy symbol", 8201, null);
            SymbolEnum.addValue("intl national hwy symbol", 8202, null);
            SymbolEnum.addValue("capitol city symbol (star)", 8203, null);
            SymbolEnum.addValue("amusement park symbol", 8204, null);
            SymbolEnum.addValue("bowling symbol", 8205, null);
            SymbolEnum.addValue("car rental symbol", 8206, null);
            SymbolEnum.addValue("car repair symbol", 8207, null);
            SymbolEnum.addValue("fast food symbol", 8208, null);
            SymbolEnum.addValue("fitness symbol", 8209, null);
            SymbolEnum.addValue("movie symbol", 8210, null);
            SymbolEnum.addValue("museum symbol", 8211, null);
            SymbolEnum.addValue("pharmacy symbol", 8212, null);
            SymbolEnum.addValue("pizza symbol", 8213, null);
            SymbolEnum.addValue("post office symbol", 8214, null);
            SymbolEnum.addValue("RV park symbol", 8215, null);
            SymbolEnum.addValue("school symbol", 8216, null);
            SymbolEnum.addValue("stadium symbol", 8217, null);
            SymbolEnum.addValue("dept. store symbol", 8218, null);
            SymbolEnum.addValue("zoo symbol", 8219, null);
            SymbolEnum.addValue("convenience store symbol", 8220, null);
            SymbolEnum.addValue("live theater symbol", 8221, null);
            SymbolEnum.addValue("ramp intersection symbol", 8222, null);
            SymbolEnum.addValue("street intersection symbol", 8223, null);
            SymbolEnum.addValue("inspection/weigh station symbol", 8226, null);
            SymbolEnum.addValue("toll booth symbol", 8227, null);
            SymbolEnum.addValue("elevation point symbol", 8228, null);
            SymbolEnum.addValue("exit without services symbol", 8229, null);
            SymbolEnum.addValue("Geographic place name, man-made", 8230, null);
            SymbolEnum.addValue("Geographic place name, water", 8231, null);
            SymbolEnum.addValue("Geographic place name, land", 8232, null);
            SymbolEnum.addValue("bridge symbol", 8233, null);
            SymbolEnum.addValue("building symbol", 8234, null);
            SymbolEnum.addValue("cemetery symbol", 8235, null);
            SymbolEnum.addValue("church symbol", 8236, null);
            SymbolEnum.addValue("civil location symbol", 8237, null);
            SymbolEnum.addValue("crossing symbol", 8238, null);
            SymbolEnum.addValue("historical town symbol", 8239, null);
            SymbolEnum.addValue("levee symbol", 8240, null);
            SymbolEnum.addValue("military location symbol", 8241, null);
            SymbolEnum.addValue("oil field symbol", 8242, null);
            SymbolEnum.addValue("tunnel symbol", 8243, null);
            SymbolEnum.addValue("beach symbol", 8244, null);
            SymbolEnum.addValue("forest symbol", 8245, null);
            SymbolEnum.addValue("summit symbol", 8246, null);
            SymbolEnum.addValue("large ramp intersection symbol", 8247, null);
            SymbolEnum.addValue("large exit without services smbl", 8248, null);
            SymbolEnum.addValue("police/official badge symbol", 8249, null);
            SymbolEnum.addValue("gambling/casino symbol", 8250, null);
            SymbolEnum.addValue("snow skiing symbol", 8251, null);
            SymbolEnum.addValue("ice skating symbol", 8252, null);
            SymbolEnum.addValue("tow truck (wrecker) symbol", 8253, null);
            SymbolEnum.addValue("border crossing (port of entry)", 8254, null);
            SymbolEnum.addValue("geocache location", 8255, null);
            SymbolEnum.addValue("found geocache", 8256, null);
            SymbolEnum.addValue("Rino contact symbol, 'smiley'", 8257, null);
            SymbolEnum.addValue("Rino contact symbol, 'ball cap'", 8258, null);
            SymbolEnum.addValue("Rino contact symbol, 'big ear'", 8259, null);
            SymbolEnum.addValue("Rino contact symbol, 'spike'", 8260, null);
            SymbolEnum.addValue("Rino contact symbol, 'goatee'", 8261, null);
            SymbolEnum.addValue("Rino contact symbol, 'afro'", 8262, null);
            SymbolEnum.addValue("Rino contact symbol, 'dreads'", 8263, null);
            SymbolEnum.addValue("Rino contact symbol, 'female 1'", 8264, null);
            SymbolEnum.addValue("Rino contact symbol, 'female 2'", 8265, null);
            SymbolEnum.addValue("Rino contact symbol, 'female 3'", 8266, null);
            SymbolEnum.addValue("Rino contact symbol, 'ranger'", 8267, null);
            SymbolEnum.addValue("Rino contact symbol, 'kung fu'", 8268, null);
            SymbolEnum.addValue("Rino contact symbol, 'sumo'", 8269, null);
            SymbolEnum.addValue("Rino contact symbol, 'pirate'", 8270, null);
            SymbolEnum.addValue("Rino contact symbol, 'biker'", 8271, null);
            SymbolEnum.addValue("Rino contact symbol, 'alien'", 8272, null);
            SymbolEnum.addValue("Rino contact symbol, 'bug'", 8273, null);
            SymbolEnum.addValue("Rino contact symbol, 'cat'", 8274, null);
            SymbolEnum.addValue("Rino contact symbol, 'dog'", 8275, null);
            SymbolEnum.addValue("Rino contact symbol, 'pig'", 8276, null);
            SymbolEnum.addValue("water hydrant symbol", 8282, null);
            SymbolEnum.addValue("blue flag symbol", 8284, null);
            SymbolEnum.addValue("green flag symbol", 8285, null);
            SymbolEnum.addValue("red flag symbol", 8286, null);
            SymbolEnum.addValue("blue pin symbol", 8287, null);
            SymbolEnum.addValue("green pin symbol", 8288, null);
            SymbolEnum.addValue("red pin symbol", 8289, null);
            SymbolEnum.addValue("blue block symbol", 8290, null);
            SymbolEnum.addValue("green block symbol", 8291, null);
            SymbolEnum.addValue("red block symbol", 8292, null);
            SymbolEnum.addValue("bike trail symbol", 8293, null);
            SymbolEnum.addValue("red circle symbol", 8294, null);
            SymbolEnum.addValue("green circle symbol", 8295, null);
            SymbolEnum.addValue("blue circle symbol", 8296, null);
            SymbolEnum.addValue("blue diamond symbol", 8299, null);
            SymbolEnum.addValue("red oval symbol", 8300, null);
            SymbolEnum.addValue("green oval symbol", 8301, null);
            SymbolEnum.addValue("blue oval symbol", 8302, null);
            SymbolEnum.addValue("red rectangle symbol", 8303, null);
            SymbolEnum.addValue("green rectangle symbol", 8304, null);
            SymbolEnum.addValue("blue rectangle symbol", 8305, null);
            SymbolEnum.addValue("blue square symbol", 8308, null);
            SymbolEnum.addValue("red letter 'A' symbol", 8309, null);
            SymbolEnum.addValue("red letter 'B' symbol", 8310, null);
            SymbolEnum.addValue("red letter 'C' symbol", 8311, null);
            SymbolEnum.addValue("red letter 'D' symbol", 8312, null);
            SymbolEnum.addValue("green letter 'A' symbol", 8313, null);
            SymbolEnum.addValue("green letter 'C' symbol", 8314, null);
            SymbolEnum.addValue("green letter 'B' symbol", 8315, null);
            SymbolEnum.addValue("green letter 'D' symbol", 8316, null);
            SymbolEnum.addValue("blue letter 'A' symbol", 8317, null);
            SymbolEnum.addValue("blue letter 'B' symbol", 8318, null);
            SymbolEnum.addValue("blue letter 'C' symbol", 8319, null);
            SymbolEnum.addValue("blue letter 'D' symbol", 8320, null);
            SymbolEnum.addValue("red number '0' symbol", 8321, null);
            SymbolEnum.addValue("red number '1' symbol", 8322, null);
            SymbolEnum.addValue("red number '2' symbol", 8323, null);
            SymbolEnum.addValue("red number '3' symbol", 8324, null);
            SymbolEnum.addValue("red number '4' symbol", 8325, null);
            SymbolEnum.addValue("red number '5' symbol", 8326, null);
            SymbolEnum.addValue("red number '6' symbol", 8327, null);
            SymbolEnum.addValue("red number '7' symbol", 8328, null);
            SymbolEnum.addValue("red number '8' symbol", 8329, null);
            SymbolEnum.addValue("red number '9' symbol", 8330, null);
            SymbolEnum.addValue("green number '0' symbol", 8331, null);
            SymbolEnum.addValue("green number '1' symbol", 8332, null);
            SymbolEnum.addValue("green number '2' symbol", 8333, null);
            SymbolEnum.addValue("green number '3' symbol", 8334, null);
            SymbolEnum.addValue("green number '4' symbol", 8335, null);
            SymbolEnum.addValue("green number '5' symbol", 8336, null);
            SymbolEnum.addValue("green number '6' symbol", 8337, null);
            SymbolEnum.addValue("green number '7' symbol", 8338, null);
            SymbolEnum.addValue("green number '8' symbol", 8339, null);
            SymbolEnum.addValue("green number '9' symbol", 8340, null);
            SymbolEnum.addValue("blue number '0' symbol", 8341, null);
            SymbolEnum.addValue("blue number '1' symbol", 8342, null);
            SymbolEnum.addValue("blue number '2' symbol", 8343, null);
            SymbolEnum.addValue("blue number '3' symbol", 8344, null);
            SymbolEnum.addValue("blue number '4' symbol", 8345, null);
            SymbolEnum.addValue("blue number '5' symbol", 8346, null);
            SymbolEnum.addValue("blue number '6' symbol", 8347, null);
            SymbolEnum.addValue("blue number '7' symbol", 8348, null);
            SymbolEnum.addValue("blue number '8' symbol", 8349, null);
            SymbolEnum.addValue("blue number '9' symbol", 8350, null);
            SymbolEnum.addValue("blue triangle symbol", 8351, null);
            SymbolEnum.addValue("green triangle symbol", 8352, null);
            SymbolEnum.addValue("red triangle symbol", 8353, null);
            SymbolEnum.addValue("airport symbol", 16384, null);
            SymbolEnum.addValue("intersection symbol", 16385, null);
            SymbolEnum.addValue("non-directional beacon symbol", 16386, null);
            SymbolEnum.addValue("VHF omni-range symbol", 16387, null);
            SymbolEnum.addValue("heliport symbol", 16388, null);
            SymbolEnum.addValue("private field symbol", 16389, null);
            SymbolEnum.addValue("soft field symbol", 16390, null);
            SymbolEnum.addValue("tall tower symbol", 16391, null);
            SymbolEnum.addValue("short tower symbol", 16392, null);
            SymbolEnum.addValue("glider symbol", 16393, null);
            SymbolEnum.addValue("ultralight symbol", 16394, null);
            SymbolEnum.addValue("parachute symbol", 16395, null);
            SymbolEnum.addValue("VOR/TACAN symbol", 16396, null);
            SymbolEnum.addValue("VOR-DME symbol", 16397, null);
            SymbolEnum.addValue("first approach fix", 16398, null);
            SymbolEnum.addValue("localizer outer marker", 16399, null);
            SymbolEnum.addValue("missed approach point", 16400, null);
            SymbolEnum.addValue("TACAN symbol", 16401, null);
            SymbolEnum.addValue("Seaplane Base", 16402, null);
        }
        return SymbolEnum;
    }

    public String getPacketType() {
        return "waypoint";
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

    public static GPSEnumDefinition getColorEnum() {
        return ColorEnum;
    }

    public static GPSEnumDefinition getSymbolEnum() {
        return SymbolEnum;
    }

    public static GPSEnumDefinition getWaypointClassEnum() {
        return WaypointClassEnum;
    }

    public String getAddr() {
        return addr;
    }

    public float getAlt() {
        return alt;
    }

    public short getAttr() {
        return attr;
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

    public float getDist() {
        return dist;
    }

    public short getDspl() {
        return dspl;
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

    public short getWpt_class() {
        return wpt_class;
    }
}