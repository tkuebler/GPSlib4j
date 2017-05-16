package com.diddlebits.gpslib4j.Garmin;

import java.awt.Color;

import com.diddlebits.gpslib4j.FloatSpecification;
import com.diddlebits.gpslib4j.GPSEnumDefinition;
import com.diddlebits.gpslib4j.GPSFields;
import com.diddlebits.gpslib4j.IAltitude;
import com.diddlebits.gpslib4j.IWaypoint;
import com.diddlebits.gpslib4j.IconEnumDefinition;
import com.diddlebits.gpslib4j.IntegerSpecification;
import com.diddlebits.gpslib4j.InvalidFieldValue;
import com.diddlebits.gpslib4j.Position;
import com.diddlebits.gpslib4j.StringValidator;

/**
 * This class encapsulates a Waypoint-packet. The Garmin-protocol contains a
 * huge amount of different Waypoint-Packet specifications. Only the one
 * labelled D108 is implemented so far.
 */
public class WaypointDataPacket108 extends GarminPacket implements IWaypoint,
        IAltitude {
    private static final long serialVersionUID = 2288225818732681749L;

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
    protected String address;

    /** Intersecting road label. */
    protected String crossRoad;

    protected String ident;

    private static GPSEnumDefinition WaypointClassEnum;

    private static GPSEnumDefinition ColorEnum;

    private static IconEnumDefinition SymbolEnum;

    protected static FloatSpecification AltSpecification = new FloatSpecification(
            -1e24, 1e24, 0.1, true);

    protected static IntegerSpecification DummySpecification = new IntegerSpecification(
            0x60, 0x60, false);

    private static StringValidator IdentValidator;

    public WaypointDataPacket108() {
        super();
    }

    /**
     * Throws a PacketNotRecognizedException if the Waypoint-dataformat is not
     * implemented.
     * 
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
        wpt_class = (short) visitor.enumField(UINT8, GPSFields.ClassField,
                wpt_class, GetWaypointClassEnum());
        color = (short) visitor.enumField(UINT8, GPSFields.ColorField, color,
                GetColorEnum());
        dspl = (short) visitor.enumField(UINT8, GPSFields.DisplayField, dspl,
                WaypointDataPacket103.GetDisplayEnum());
        visitor.intField(UINT8, GPSFields.AttrField, 0x60, DummySpecification,
                0x60);
        smbl = visitor.enumField(UINT16, GPSFields.SymbolField, smbl,
                GetSymbolEnum());
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
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident,
                GetIdentValidator());
        if (wpt_class != 0x00) {
            // only user waypoints can have comments
            comment = "";
        }
        comment = visitor.stringField(VCHAR, GPSFields.CommentField, comment,
                GarminStringValidatorsFactory.CreateComment(100, true));
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

    public static GPSEnumDefinition GetWaypointClassEnum() {
        if (WaypointClassEnum == null) {
            WaypointClassEnum = new GPSEnumDefinition("WaypointClass", false);
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
            ColorEnum.addValue("default", 255, Color.BLACK);
        }
        return ColorEnum;
    }

    public static IconEnumDefinition GetSymbolEnum() {
        if (SymbolEnum == null) {
            SymbolEnum = new IconEnumDefinition("Symbol", false, "icons/icon");
            SymbolEnum.addValue("white anchor", 0);
            SymbolEnum.addValue("white bell", 1);
            SymbolEnum.addValue("green diamond", 2);
            SymbolEnum.addValue("red diamond", 3);
            SymbolEnum.addValue("diver down flag 1", 4);
            SymbolEnum.addValue("diver down flag 2", 5);
            SymbolEnum.addValue("white dollar", 6);
            SymbolEnum.addValue("white fish", 7);
            SymbolEnum.addValue("white fuel", 8);
            SymbolEnum.addValue("white horn", 9);
            SymbolEnum.addValue("white house", 10);
            SymbolEnum.addValue("white knife & fork", 11);
            SymbolEnum.addValue("white light", 12);
            SymbolEnum.addValue("white mug", 13);
            SymbolEnum.addValue("white skull and crossbones", 14);
            SymbolEnum.addValue("green square", 15);
            SymbolEnum.addValue("red square", 16);
            SymbolEnum.addValue("white buoy waypoint", 17);
            SymbolEnum.addValue("waypoint dot", 18);
            SymbolEnum.addValue("white wreck", 19);
            SymbolEnum.addValue("null (transparent)", 20);
            SymbolEnum.addValue("man overboard", 21);
            SymbolEnum.addValue("amber map buoy", 22);
            SymbolEnum.addValue("black map buoy", 23);
            SymbolEnum.addValue("blue map buoy", 24);
            SymbolEnum.addValue("green map buoy", 25);
            SymbolEnum.addValue("green/red map buoy", 26);
            SymbolEnum.addValue("green/white map buoy", 27);
            SymbolEnum.addValue("orange map buoy", 28);
            SymbolEnum.addValue("red map buoy", 29);
            SymbolEnum.addValue("red/green map buoy", 30);
            SymbolEnum.addValue("red/white map buoy", 31);
            SymbolEnum.addValue("violet map buoy", 32);
            SymbolEnum.addValue("white map buoy", 33);
            SymbolEnum.addValue("white/green map buoy", 34);
            SymbolEnum.addValue("white/red map buoy", 35);
            SymbolEnum.addValue("white dot", 36);
            SymbolEnum.addValue("radio beacon", 37);
            SymbolEnum.addValue("boat ramp", 150);
            SymbolEnum.addValue("campground", 151);
            SymbolEnum.addValue("restrooms", 152);
            SymbolEnum.addValue("shower", 153);
            SymbolEnum.addValue("drinking water", 154);
            SymbolEnum.addValue("telephone", 155);
            SymbolEnum.addValue("first aid", 156);
            SymbolEnum.addValue("information", 157);
            SymbolEnum.addValue("parking", 158);
            SymbolEnum.addValue("park", 159);
            SymbolEnum.addValue("picnic", 160);
            SymbolEnum.addValue("scenic area", 161);
            SymbolEnum.addValue("skiing", 162);
            SymbolEnum.addValue("swimming", 163);
            SymbolEnum.addValue("dam", 164);
            SymbolEnum.addValue("controlled area", 165);
            SymbolEnum.addValue("danger", 166);
            SymbolEnum.addValue("restricted area", 167);
            SymbolEnum.addValue("null", 168);
            SymbolEnum.addValue("ball", 169);
            SymbolEnum.addValue("car", 170);
            SymbolEnum.addValue("deer", 171);
            SymbolEnum.addValue("shopping cart", 172);
            SymbolEnum.addValue("lodging", 173);
            SymbolEnum.addValue("mine", 174);
            SymbolEnum.addValue("trail head", 175);
            SymbolEnum.addValue("truck stop", 176);
            SymbolEnum.addValue("user exit", 177);
            SymbolEnum.addValue("flag", 178);
            SymbolEnum.addValue("circle with x in the center", 179);
            SymbolEnum.addValue("open 24 hours", 180);
            SymbolEnum.addValue("U Fishing Hot Spots? Facility", 181);
            SymbolEnum.addValue("Bottom Conditions", 182);
            SymbolEnum.addValue("Tide/Current Prediction Station", 183);
            SymbolEnum.addValue("U anchor prohibited", 184);
            SymbolEnum.addValue("U beacon", 185);
            SymbolEnum.addValue("U coast guard", 186);
            SymbolEnum.addValue("U reef", 187);
            SymbolEnum.addValue("U weedbed", 188);
            SymbolEnum.addValue("U dropoff", 189);
            SymbolEnum.addValue("U dock", 190);
            SymbolEnum.addValue("U marina", 191);
            SymbolEnum.addValue("U bait and tackle", 192);
            SymbolEnum.addValue("U stump", 193);
            SymbolEnum.addValue("interstate hwy", 8192);
            SymbolEnum.addValue("us hwy", 8193);
            SymbolEnum.addValue("state hwy", 8194);
            SymbolEnum.addValue("mile marker", 8195);
            SymbolEnum.addValue("tracBack (feet)", 8196);
            SymbolEnum.addValue("golf", 8197);
            SymbolEnum.addValue("small city", 8198);
            SymbolEnum.addValue("medium city", 8199);
            SymbolEnum.addValue("large city", 8200);
            SymbolEnum.addValue("intl freeway hwy", 8201);
            SymbolEnum.addValue("intl national hwy", 8202);
            SymbolEnum.addValue("capitol city (star)", 8203);
            SymbolEnum.addValue("amusement park", 8204);
            SymbolEnum.addValue("bowling", 8205);
            SymbolEnum.addValue("car rental", 8206);
            SymbolEnum.addValue("car repair", 8207);
            SymbolEnum.addValue("fast food", 8208);
            SymbolEnum.addValue("fitness", 8209);
            SymbolEnum.addValue("movie", 8210);
            SymbolEnum.addValue("museum", 8211);
            SymbolEnum.addValue("pharmacy", 8212);
            SymbolEnum.addValue("pizza", 8213);
            SymbolEnum.addValue("post office", 8214);
            SymbolEnum.addValue("RV park", 8215);
            SymbolEnum.addValue("school", 8216);
            SymbolEnum.addValue("stadium", 8217);
            SymbolEnum.addValue("dept. store", 8218);
            SymbolEnum.addValue("zoo", 8219);
            SymbolEnum.addValue("convenience store", 8220);
            SymbolEnum.addValue("live theater", 8221);
            SymbolEnum.addValue("ramp intersection", 8222);
            SymbolEnum.addValue("street intersection", 8223);
            SymbolEnum.addValue("inspection/weigh station", 8226);
            SymbolEnum.addValue("toll booth", 8227);
            SymbolEnum.addValue("elevation point", 8228);
            SymbolEnum.addValue("exit without services", 8229);
            SymbolEnum.addValue("Geographic place name, man-made", 8230);
            SymbolEnum.addValue("Geographic place name, water", 8231);
            SymbolEnum.addValue("Geographic place name, land", 8232);
            SymbolEnum.addValue("bridge", 8233);
            SymbolEnum.addValue("building", 8234);
            SymbolEnum.addValue("cemetery", 8235);
            SymbolEnum.addValue("church", 8236);
            SymbolEnum.addValue("civil location", 8237);
            SymbolEnum.addValue("crossing", 8238);
            SymbolEnum.addValue("historical town", 8239);
            SymbolEnum.addValue("levee", 8240);
            SymbolEnum.addValue("military location", 8241);
            SymbolEnum.addValue("oil field", 8242);
            SymbolEnum.addValue("tunnel", 8243);
            SymbolEnum.addValue("beach", 8244);
            SymbolEnum.addValue("forest", 8245);
            SymbolEnum.addValue("summit", 8246);
            SymbolEnum.addValue("large ramp intersection", 8247);
            SymbolEnum.addValue("large exit without services smbl", 8248);
            SymbolEnum.addValue("police/official badge", 8249);
            SymbolEnum.addValue("gambling/casino", 8250);
            SymbolEnum.addValue("snow skiing", 8251);
            SymbolEnum.addValue("ice skating", 8252);
            SymbolEnum.addValue("tow truck (wrecker)", 8253);
            SymbolEnum.addValue("border crossing (port of entry)", 8254);
            SymbolEnum.addValue("geocache location", 8255);
            SymbolEnum.addValue("found geocache", 8256);
            SymbolEnum.addValue("Rino contact, 'smiley'", 8257);
            SymbolEnum.addValue("Rino contact, 'ball cap'", 8258);
            SymbolEnum.addValue("Rino contact, 'big ear'", 8259);
            SymbolEnum.addValue("Rino contact, 'spike'", 8260);
            SymbolEnum.addValue("Rino contact, 'goatee'", 8261);
            SymbolEnum.addValue("Rino contact, 'afro'", 8262);
            SymbolEnum.addValue("Rino contact, 'dreads'", 8263);
            SymbolEnum.addValue("Rino contact, 'female 1'", 8264);
            SymbolEnum.addValue("Rino contact, 'female 2'", 8265);
            SymbolEnum.addValue("Rino contact, 'female 3'", 8266);
            SymbolEnum.addValue("Rino contact, 'ranger'", 8267);
            SymbolEnum.addValue("Rino contact, 'kung fu'", 8268);
            SymbolEnum.addValue("Rino contact, 'sumo'", 8269);
            SymbolEnum.addValue("Rino contact, 'pirate'", 8270);
            SymbolEnum.addValue("Rino contact, 'biker'", 8271);
            SymbolEnum.addValue("Rino contact, 'alien'", 8272);
            SymbolEnum.addValue("Rino contact, 'bug'", 8273);
            SymbolEnum.addValue("Rino contact, 'cat'", 8274);
            SymbolEnum.addValue("Rino contact, 'dog'", 8275);
            SymbolEnum.addValue("Rino contact, 'pig'", 8276);
            SymbolEnum.addValue("water hydrant", 8282);
            SymbolEnum.addValue("blue flag", 8284);
            SymbolEnum.addValue("green flag", 8285);
            SymbolEnum.addValue("red flag", 8286);
            SymbolEnum.addValue("blue pin", 8287);
            SymbolEnum.addValue("green pin", 8288);
            SymbolEnum.addValue("red pin", 8289);
            SymbolEnum.addValue("blue block", 8290);
            SymbolEnum.addValue("green block", 8291);
            SymbolEnum.addValue("red block", 8292);
            SymbolEnum.addValue("bike trail", 8293);
            SymbolEnum.addValue("red circle", 8294);
            SymbolEnum.addValue("green circle", 8295);
            SymbolEnum.addValue("blue circle", 8296);
            SymbolEnum.addValue("blue diamond", 8299);
            SymbolEnum.addValue("red oval", 8300);
            SymbolEnum.addValue("green oval", 8301);
            SymbolEnum.addValue("blue oval", 8302);
            SymbolEnum.addValue("red rectangle", 8303);
            SymbolEnum.addValue("green rectangle", 8304);
            SymbolEnum.addValue("blue rectangle", 8305);
            SymbolEnum.addValue("blue square", 8308);
            SymbolEnum.addValue("red letter 'A'", 8309);
            SymbolEnum.addValue("red letter 'B'", 8310);
            SymbolEnum.addValue("red letter 'C'", 8311);
            SymbolEnum.addValue("red letter 'D'", 8312);
            SymbolEnum.addValue("green letter 'A'", 8313);
            SymbolEnum.addValue("green letter 'C'", 8314);
            SymbolEnum.addValue("green letter 'B'", 8315);
            SymbolEnum.addValue("green letter 'D'", 8316);
            SymbolEnum.addValue("blue letter 'A'", 8317);
            SymbolEnum.addValue("blue letter 'B'", 8318);
            SymbolEnum.addValue("blue letter 'C'", 8319);
            SymbolEnum.addValue("blue letter 'D'", 8320);
            SymbolEnum.addValue("red number '0'", 8321);
            SymbolEnum.addValue("red number '1'", 8322);
            SymbolEnum.addValue("red number '2'", 8323);
            SymbolEnum.addValue("red number '3'", 8324);
            SymbolEnum.addValue("red number '4'", 8325);
            SymbolEnum.addValue("red number '5'", 8326);
            SymbolEnum.addValue("red number '6'", 8327);
            SymbolEnum.addValue("red number '7'", 8328);
            SymbolEnum.addValue("red number '8'", 8329);
            SymbolEnum.addValue("red number '9'", 8330);
            SymbolEnum.addValue("green number '0'", 8331);
            SymbolEnum.addValue("green number '1'", 8332);
            SymbolEnum.addValue("green number '2'", 8333);
            SymbolEnum.addValue("green number '3'", 8334);
            SymbolEnum.addValue("green number '4'", 8335);
            SymbolEnum.addValue("green number '5'", 8336);
            SymbolEnum.addValue("green number '6'", 8337);
            SymbolEnum.addValue("green number '7'", 8338);
            SymbolEnum.addValue("green number '8'", 8339);
            SymbolEnum.addValue("green number '9'", 8340);
            SymbolEnum.addValue("blue number '0'", 8341);
            SymbolEnum.addValue("blue number '1'", 8342);
            SymbolEnum.addValue("blue number '2'", 8343);
            SymbolEnum.addValue("blue number '3'", 8344);
            SymbolEnum.addValue("blue number '4'", 8345);
            SymbolEnum.addValue("blue number '5'", 8346);
            SymbolEnum.addValue("blue number '6'", 8347);
            SymbolEnum.addValue("blue number '7'", 8348);
            SymbolEnum.addValue("blue number '8'", 8349);
            SymbolEnum.addValue("blue number '9'", 8350);
            SymbolEnum.addValue("blue triangle", 8351);
            SymbolEnum.addValue("green triangle", 8352);
            SymbolEnum.addValue("red triangle", 8353);
            SymbolEnum.addValue("airport", 16384);
            SymbolEnum.addValue("intersection", 16385);
            SymbolEnum.addValue("non-directional beacon", 16386);
            SymbolEnum.addValue("VHF omni-range", 16387);
            SymbolEnum.addValue("heliport", 16388);
            SymbolEnum.addValue("private field", 16389);
            SymbolEnum.addValue("soft field", 16390);
            SymbolEnum.addValue("tall tower", 16391);
            SymbolEnum.addValue("short tower", 16392);
            SymbolEnum.addValue("glider", 16393);
            SymbolEnum.addValue("ultralight", 16394);
            SymbolEnum.addValue("parachute", 16395);
            SymbolEnum.addValue("VOR/TACAN", 16396);
            SymbolEnum.addValue("VOR-DME", 16397);
            SymbolEnum.addValue("first approach fix", 16398);
            SymbolEnum.addValue("localizer outer marker", 16399);
            SymbolEnum.addValue("missed approach point", 16400);
            SymbolEnum.addValue("TACAN", 16401);
            SymbolEnum.addValue("Seaplane Base", 16402);
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

    public static GPSEnumDefinition getColorEnum() {
        return ColorEnum;
    }

    public static GPSEnumDefinition getSymbolEnum() {
        return SymbolEnum;
    }

    public static GPSEnumDefinition getWaypointClassEnum() {
        return WaypointClassEnum;
    }

    public float getAltitude() {
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

    public short getWptClass() {
        return wpt_class;
    }

    public String getAddress() {
        return address;
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