package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class RouteWaypointPacket210 extends GarminPacket implements
        IRouteWaypoint {
    private static final long serialVersionUID = -4776310485094954856L;

    protected int wpClass;

    protected String subClass;

    protected String ident;

    private static GPSEnumDefinition ClassEnum;

    public RouteWaypointPacket210() {
        super();
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        wpClass = visitor.enumField(UINT16, GPSFields.ClassField, wpClass,
                GetClassEnum());
        if (wpClass == 3 || wpClass == 0xFF) {
            char tmp[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF,
                    0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
            subClass = new String(tmp);
        }
        subClass = visitor.stringField(ACHAR, GPSFields.SubClassField,
                subClass, GarminStringValidatorsFactory.CreateUnchecked(18,
                        true));
        ident = visitor.stringField(VCHAR, GPSFields.IdentField, ident,
                GarminStringValidatorsFactory.CreateWaypointIdent(50, true));
    }

    public static GPSEnumDefinition GetClassEnum() {
        if (ClassEnum == null) {
            ClassEnum = new GPSEnumDefinition("Class", false);
            ClassEnum.addValue("line", 0, null);
            ClassEnum.addValue("link", 1, null);
            ClassEnum.addValue("net", 2, null);
            ClassEnum.addValue("direct", 3, null);
            ClassEnum.addValue("snap", 0xFF, null);
        }
        return ClassEnum;
    }

    public String getPacketType() {
        return "route header";
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getSubClass() {
        return subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
    }

    public int getWpClass() {
        return wpClass;
    }

    public void setWpClass(int wpClass) {
        this.wpClass = wpClass;
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Rte_Wpt_Data;
    }
}