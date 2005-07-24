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

    private static GPSEnumDefinition SymbolEnum;

    /**
     * Throws a PacketNotRecognizedException if the Waypoint-dataformat is not
     * implemented.
     * 
     * @throws InvalidFieldValue
     */
    public WaypointDataPacket103(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        super();

        if (p.getID() != GarminRawPacket.Pid_Wpt_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Wpt_Data, p.getID()));
        }

        initFromRawPacket(p);
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
        visitor.intField(UINT32, GPSFields.UnusedField, 0, 0, 0, 0);
        comment = visitor.stringField(ACHAR, GPSFields.CommentField, comment,
                GarminStringValidatorsFactory.CreateComment(40, true));
        smbl = visitor.enumField(UINT8, GPSFields.SymbolField, smbl,
                GetSymbolEnum());
        dspl = (short) visitor.enumField(UINT8, GPSFields.DisplayField, dspl,
                GetDisplayEnum());
    }

    public static GPSEnumDefinition GetSymbolEnum() {
        if (SymbolEnum == null) {
            SymbolEnum = new GPSEnumDefinition("Symbol");
            SymbolEnum.addValue("dot", 0, null);
            SymbolEnum.addValue("house", 1, null);
            SymbolEnum.addValue("gaz", 2, null);
            SymbolEnum.addValue("car", 3, null);
            SymbolEnum.addValue("fish", 4, null);
            SymbolEnum.addValue("boat", 5, null);
            SymbolEnum.addValue("anchor", 6, null);
            SymbolEnum.addValue("wreck", 7, null);
            SymbolEnum.addValue("exit", 8, null);
            SymbolEnum.addValue("skull", 9, null);
            SymbolEnum.addValue("flag", 10, null);
            SymbolEnum.addValue("camp", 11, null);
            SymbolEnum.addValue("circle X", 12, null);
            SymbolEnum.addValue("deer", 13, null);
            SymbolEnum.addValue("1st aid", 14, null);
            SymbolEnum.addValue("back track", 15, null);
        }
        return SymbolEnum;
    }

    public static GPSEnumDefinition GetDisplayEnum() {
        if (DisplayEnum == null) {
            DisplayEnum = new GPSEnumDefinition("Display");
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