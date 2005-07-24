package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

public class PositionDataPacket700 extends GarminPacket implements IPosition {
    private Position position;

    /**
     * Treats the packet p as a packet containing position-data. Throws
     * PacketNotRecognizedException if p is not a position-data-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     * 
     * @throws PacketNotRecognizedException
     * @throws InvalidFieldValue
     */
    public PositionDataPacket700(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        super();
        if (p.getID() != GarminRawPacket.Pid_Position_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Position_Data, p.getID()));
        }

        initFromRawPacket(p);
    }

    /**
     * This method returns the position.
     */
    public Position getPosition() {
        return position;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        position = visitor.positionField(GPSFields.PositionField, position);
    }

    public String getPacketType() {
        return "position";
    }
}