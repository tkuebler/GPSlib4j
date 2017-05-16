package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

public class PositionDataPacket700 extends GarminPacket implements IPosition {
    private static final long serialVersionUID = 2343308973089279701L;

    private Position position;

    public PositionDataPacket700() {
        super();
    }

    /**
     * Treats the packet p as a packet containing position-data. Throws
     * PacketNotRecognizedException if p is not a position-data-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     * 
     * @throws PacketNotRecognizedException
     * @throws InvalidFieldValue
     */
    public void initFromRawPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        if (p.getPID() != GarminRawPacket.Pid_Position_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Position_Data, p.getPID()));
        }

        super.initFromRawPacket(p);
    }

    /**
     * This method returns the position.
     */
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        position = visitor.positionField(GPSFields.PositionField, position);
    }

    public String getPacketType() {
        return "position";
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Position_Data;
    }
}