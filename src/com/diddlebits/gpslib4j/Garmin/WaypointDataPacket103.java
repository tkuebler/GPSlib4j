package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a Waypoint-packet. The Garmin-protocol contains a
 * huge amount of different Waypoint-Packet specifications. Only the one
 * labelled D108 is implemented so far.
 */
public class WaypointDataPacket103 extends GarminPacket implements IWaypoint {
    protected String ident;

    /** Position of waypoint. */
    protected Position position;

    /** Waypoint comment. */
    protected String comment;

    /** Waypoint symbol. */
    protected int smbl;

    /** Display options. */
    protected short dspl;

    private static GPSEnumDefinition DisplayEnum;

    private static IconEnumDefinition SymbolEnum;

    protected static IntegerSpecification DummySpecification = new IntegerSpecification(
            0, 0, false);

    /**
     * Throws a PacketNotRecognizedException if the Waypoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     */
    public WaypointDataPacket103() {
        super();
    }

    /**
     * @param p
     * @throws PacketNotRecognizedException
     * @throws InvalidFieldValue
     */
    public void initFromRawPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        if (p.getID() != GarminRawPacket.Pid_Wpt_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Wpt_Data, p.getID()));
        }

        super.initFromRawPacket(p);
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

    public String getComment() {
        return comment;
    }

    public short getDspl() {
        return dspl;
    }

    public int getSmbl() {
        return smbl;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        ident = visitor.stringField(ACHAR, GPSFields.IdentField, ident,
                GarminStringValidatorsFactory.CreateWaypointIdent(6, false));
        position = visitor.positionField(GPSFields.PositionField, position);
        visitor.intField(UINT32, GPSFields.UnusedField, 0, DummySpecification,
                0);
        comment = visitor.stringField(ACHAR, GPSFields.CommentField, comment,
                GarminStringValidatorsFactory.CreateComment(40, true));
        smbl = visitor.enumField(UINT8, GPSFields.SymbolField, smbl,
                GetSymbolEnum());
        dspl = (short) visitor.enumField(UINT8, GPSFields.DisplayField, dspl,
                GetDisplayEnum());
    }

    public static GPSEnumDefinition GetSymbolEnum() {
        if (SymbolEnum == null) {
            SymbolEnum = new IconEnumDefinition("Symbol", false, "icon/icon");
            SymbolEnum.addValue("waypoint dot", 0);
            SymbolEnum.addValue("white house", 1);
            SymbolEnum.addValue("white fuel", 2);
            SymbolEnum.addValue("car", 3);
            SymbolEnum.addValue("white fish", 4);
            SymbolEnum.addValue("boat ramp", 5);
            SymbolEnum.addValue("white anchor", 6);
            SymbolEnum.addValue("white wreck", 7);
            SymbolEnum.addValue("user exit", 8);
            SymbolEnum.addValue("white skull and crossbones", 9);
            SymbolEnum.addValue("flag", 10);
            SymbolEnum.addValue("campground", 11);
            SymbolEnum.addValue("circle with X in the center", 12);
            SymbolEnum.addValue("deer", 13);
            SymbolEnum.addValue("first aid", 14);
            SymbolEnum.addValue("tracBack (feet)", 15);
        }
        return SymbolEnum;
    }

    public static GPSEnumDefinition GetDisplayEnum() {
        if (DisplayEnum == null) {
            DisplayEnum = new GPSEnumDefinition("Display", false);
            DisplayEnum.addValue("name", 0, null);
            DisplayEnum.addValue("none", 1, null);
            DisplayEnum.addValue("comment", 2, null);
        }
        return DisplayEnum;
    }

    public String getPacketType() {
        return "waypoint";
    }
}