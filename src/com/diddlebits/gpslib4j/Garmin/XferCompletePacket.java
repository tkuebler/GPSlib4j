package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.IntegerSpecification;
import com.diddlebits.gpslib4j.InvalidFieldValue;

public class XferCompletePacket extends GarminPacket {

    private int commandId;

    protected static IntegerSpecification NumberSpecification = new IntegerSpecification(
            0, 0xFFFF, false);

    public XferCompletePacket() {
        super();
        commandId = 0;
    }

    public XferCompletePacket(int commandId) {
        this();
        this.commandId = commandId;
    }

    protected void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        commandId = (int) visitor.intField(GarminPacket.UINT16, "Command ID",
                commandId, NumberSpecification, -1);
    }

    public String getPacketType() {
        return "xfer_complt";
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Xfer_Cmplt;
    }
}
