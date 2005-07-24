package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

/**
 * A class that encapsulates the basic functionality of a packet.
 */
public abstract class GarminPacket {
    /** the low level data types used by Garmin */
    public final static int UINT8 = 0;

    public final static int UINT16 = 1;

    public final static int SINT16 = 2;

    public final static int UINT32 = 3;

    public final static int ACHAR = 4;

    public final static int VCHAR = 5;

    public final static int FLOAT32 = 6;

    public final static int FLOAT64 = 7;

    public final static int LONG_DATE = 8;

    public final static int STRUCT_DATE = 9;

    public GarminPacket() {
    }

    /**
     * Visit all the fields using a special visitor that knows if a field is
     * null or not in function of its value
     * 
     * @param visitor
     *            The generic visitor.
     */
    public void visit(IGPSDataReadVisitor visitor) throws InvalidFieldValue {
        GarminGPSDataVisitor adapter = new GarminGPSDataReadVisitor(visitor);
        adapter.startEntry(getPacketType());
        visit(adapter);
        adapter.endEntry();
    }

    /**
     * Visit all the fields using a special visitor that knows if a field is
     * null or not in function of its value
     * 
     * @param visitor
     *            The generic visitor.
     */
    public void visit(IGPSDataWriteVisitor visitor) throws InvalidFieldValue {
        GarminGPSDataVisitor adapter = new GarminGPSDataWriteVisitor(visitor);
        adapter.startEntry(getPacketType());
        visit(adapter);
        adapter.endEntry();
    }

    protected abstract void visit(GarminGPSDataVisitor visitor)
            throws InvalidFieldValue;

    public abstract String getPacketType();

    public String toString() {
        ToStringGPSDataReadVisitor visitor = new ToStringGPSDataReadVisitor();
        try {
            visit(visitor);
        } catch (InvalidFieldValue e) {
            return "!INVALID!";
        }
        return visitor.getString();
    }

    protected void initFromRawPacket(GarminRawPacket source)
            throws PacketNotRecognizedException, InvalidFieldValue {

        GarminGPSDataVisitor visitor = new GarminGPSDataParserVisitor(source);
        source.pointer2start();
        visit(visitor);
    }
    
    public Object clone() throws CloneNotSupportedException {
        //if the childs are having mutable attributes, we are screwed...
        return super.clone();
    }
}