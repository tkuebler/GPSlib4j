package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.IntegerSpecification;
import com.diddlebits.gpslib4j.InvalidFieldValue;
import com.diddlebits.gpslib4j.RegexpStringValidator;
import com.diddlebits.gpslib4j.StringValidator;

public class ProtocolDataPacket extends GarminPacket {

    protected char[] tags;

    protected int[] data;

    private static StringValidator TagValidator;

    protected static IntegerSpecification DataSpecification = new IntegerSpecification(
            0, 0xFFFF, false);

    public ProtocolDataPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        super();
        initFromRawPacket(p);
    }

    /**
     * Treats the packet p as a packet containing data about which protocols the
     * GPS support. Throws PacketNotRecognizedException if p is not a
     * product-data-packet.
     * 
     * @throws PacketNotRecognizedException
     * @throws InvalidPacketException
     */
    public void initFromRawPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        if (p.getID() != GarminRawPacket.Pid_Protocol_Array) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Protocol_Array, p.getID()));
        }

        if (p.getDataLength() % 3 != 0) {
            throw (new InvalidFieldValue("length", String.valueOf(p
                    .getDataLength()), "Not a multiple of 3"));
        }

        // Don't use initFromRawPacket since it cannot handle loops for the
        // moment
        p.pointer2start();
        tags = new char[p.getDataLength() / 3 - 1];
        data = new int[p.getDataLength() / 3 - 1];

        int array_index = 0;
        while (p.getPointer() <= p.getDataLength() - 3) {
            tags[array_index] = (char) p.readByte();
            data[array_index] = p.readWord();
            array_index++;
        }
    }

    public ProtocolDataPacket(ProductDataPacket product) {
        // TODO: implement the table 28 in "Garmin Device Interface
        // Specification"
        tags = new char[0];
        data = new int[0];
    }

    /**
     * This method will return the exact version of a protocol. If the protocol
     * is not supported by the GPS, the method returns -1.
     */
    public int getVersion(char tag, int protocol) {
        for (int i = 0; i < tags.length; i++) {
            if (tags[i] == tag) {
                // must match all digits but the less significant
                if (data[i] / 10 == protocol / 10)
                    return data[i];
            }
        }
        return -1;
    }

    public void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        for (int i = 0; i < tags.length; ++i) {
            String tag = new String();
            tag += tags[i];
            tag = visitor.stringField(ACHAR, "tag" + i, tag, GetTagValidator());
            tags[i] = tag.charAt(0);

            data[i] = (int) visitor.intField(UINT16, "data" + i, data[i],
                    DataSpecification, 0x10000);
        }
    }

    public String getPacketType() {
        return "protocol";
    }

    public StringValidator GetTagValidator() throws InvalidFieldValue {
        if (TagValidator == null) {
            TagValidator = new RegexpStringValidator("tag", "[LAD]", 1, false);
        }
        return TagValidator;
    }
}