package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.Position;
import com.diddlebits.gpslib4j.PositionDegrees;

/**
 * A class that encapsulates the basic functionality of a packet.
 */
public class GarminRawPacket extends GarminPacket {
    // Basic Link Protocol - layer 1 of garmin protocol
    // L000 values. (Names taken from protocol specification.)
    public static final int Pid_Ack_Byte = 6; // 06

    public static final int Pid_Nak_Byte = 21; // 15

    public static final int Pid_Protocol_Array = 253; // FD

    public static final int Pid_Product_Rqst = 254; // FE

    public static final int Pid_Product_Data = 255; // FF

    // Link Protocol 1
    // L001 values.
    public static final int Pid_Command_Data = 10; // 0A

    public static final int Pid_Xfer_Cmplt = 12; // 0C

    public static final int Pid_Date_Time_Data = 14; // 0E

    public static final int Pid_Position_Data = 17; // 11

    public static final int Pid_Prx_Wpt_Data = 19; // 13

    public static final int Pid_Records = 27; // 1B

    public static final int Pid_Rte_Hdr = 29;

    public static final int Pid_Rte_Wpt_Data = 30;

    public static final int Pid_Trk_data = 34; // 22

    public static final int Pid_Wpt_Data = 35; // 23

    public static final int Pid_Pvt_Data = 51; // 33

    public static final int Pid_Trk_Hdr = 99; // 63

    public static final int Pid_Lap = 149; // 95

    // Packet Boundaries.
    /**
     * Data link escape. Packet boundary.
     */
    public static final int DLE = 16; // 10

    /**
     * End of text. Packet boundary.
     */
    public static final int ETX = 3; // 03

    /**
     * The packet in byte-form. It is required that the array-length is trimmed
     * to the size of the packet.
     */
    protected int[] packet;

    /**
     * pointer - the current position of the pointer to which byte is being read
     */

    protected int pointer = 3;

    /**
     * Creates a new GarminPacket with the contents of p. Throws
     * InvalidPacketException if packet is malformed.
     */
    public GarminRawPacket(int[] p) {
        this(p, false);
    }

    /**
     * Creates a new GarminPacket with the contents of p. if calcChecksum is
     * true, the packet will have it's checksum recalculated. Throws
     * InvalidPacketException if packet is malformed.
     */

    public GarminRawPacket(int[] p, boolean calcChecksum) {
        packet = (int[]) p.clone();

        if (calcChecksum) {
            packet[packet.length - 3] = calcChecksum();
        }

        if (isLegal() != -1) {
            System.out.println("Error in byte: " + isLegal());
            throw (new InvalidPacketException(p, isLegal()));
        }
    }

    /**
     * This method is capable of making the data-packets from L000 (basic link
     * protocol). <br/><i>type </i> can be one of the following constants:
     * <ul>
     * <li>Pid_Ack_Byte
     * <li>Pid_Nak_Byte
     * <li>Pid_Protocol_Array
     * <li>Pid_Product_Rqst
     * <li>Pid_Product_Data
     * </ul>
     * The argument <i>data </i> is an array of int that will be put in the
     * data-field of the packet.
     */
    public static GarminRawPacket createBasicPacket(int type, int[] data) {
        switch (type) {
        case Pid_Ack_Byte:
        case Pid_Nak_Byte:
        case Pid_Protocol_Array:
        case Pid_Product_Rqst:
        case Pid_Product_Data:
            int[] packet = new int[data.length + 6];
            packet[0] = DLE;
            packet[1] = type;
            packet[2] = data.length;
            System.arraycopy(data, 0, packet, 3, data.length);
            packet[packet.length - 3] = 0;
            packet[packet.length - 2] = DLE;
            packet[packet.length - 1] = ETX;
            return new GarminRawPacket(packet, true);
        default:
            return null;
        }
    }

    /**
     * Calculates the checksum for the packet. Does <b>not </b> insert it into
     * the correct position of the int[] packet array. <br/>The method assumes
     * that the packet is a valid Garmin-packet with all values containing their
     * final values.
     */
    public int calcChecksum() {
        int sum = 0;
        for (int i = 1; i <= packet.length - 4; i++) {
            sum += packet[i];
        }

        sum = sum % 256;
        sum = sum ^ 255;
        sum += 1;
        return sum;
    }

    /**
     * Checks if the packet is valid with regards to header,
     * footer,data-field-length and checksum. Returns the index of the illegal
     * byte. If packet is ok, -1 is returned.
     */
    public int isLegal() {
        if (packet[0] != DLE)
            return 0;

        int size = packet[2];

        if (size + 6 != packet.length)
            return 2;

        if (packet[packet.length - 3] != calcChecksum())
            return packet.length - 3;

        if (packet[packet.length - 2] != DLE)
            return packet.length - 2;

        if (packet[packet.length - 1] != ETX)
            return packet.length - 1;

        return -1;
    }

    /**
     * Returns the ID (ie. type) of the packet.
     */
    public int getID() {
        return packet[1];
    }

    /**
     * This is a factory-method capable of creating instances the commandpackets
     * from A010. (Device Command Protocol 1) returns null if it can't make a
     * packet from the argument supplied. <br/><i>type <i/>can be one of the
     * following constants:
     * <ul>
     * <li>Cmnd_Turn_Off_Pwr
     * <li>Cmnd_Transfer_Posn
     * <li>Cmnd_Transfer_Time
     * <li>Cmnd_Abort_Transfer
     * <li>Cmnd_Transfer_Alm
     * <li>Cmnd_Transfer_Prx
     * <li>Cmnd_Transfer_Rte
     * <li>Cmnd_Transfer_Trk
     * <li>Cmnd_Transfer_Wpt
     * <li>Cmnd_Start_Pvt_Data
     * <li>Cmnd_Stop_Pvt_Data
     * </ul>
     */
    public static GarminRawPacket createCommandPacket(int type) {
        System.out.println("Sending command " + type);
        return new GarminRawPacket(new int[] { GarminRawPacket.DLE,
                GarminRawPacket.Pid_Command_Data, 2, type, 0, 0,
                GarminRawPacket.DLE, GarminRawPacket.ETX }, true);
    }

    /**
     * Method that translates a packet-id into a human-readable string.
     */
    public static String IdToString(int id) {
        // TODO: integrate that into GarminFactory (depends on the model)
        switch (id) {
        case Pid_Ack_Byte:
            return "Acknowledge packet";
        case Pid_Command_Data:
            return "Command packet";
        case Pid_Date_Time_Data:
            return "Date and time data";
        case Pid_Nak_Byte:
            return "Not acknowledged packet";
        case Pid_Product_Data:
            return "Product data.";
        case Pid_Product_Rqst:
            return "Product request";
        case Pid_Protocol_Array:
            return "Protocol array packet";
        case Pid_Position_Data:
            return "position data";
        case Pid_Pvt_Data:
            return "PVT data";
        case Pid_Records:
            return "Start of record transfer";
        case Pid_Wpt_Data:
            return "waypoint data";
        case Pid_Trk_data:
            return "track data";
        case Pid_Rte_Hdr:
            return "route header";
        case Pid_Rte_Wpt_Data:
            return "route waypoint";
        case Pid_Lap:
            return "lap data";
        default:
            return "unknown data";
        }
    }

    /**
     * Returns the amount of bytes in the data-field of this packet.
     */
    public int getDataLength() {
        return packet[2];
    }

    /**
     * <i>Debug-method. </i> Returns a String-representation of the bytes in the
     * packet.
     */
    public String getRawPacket() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < packet.length; i++)
            s.append(" " + packet[i]);
        return s.toString();
    }

    /**
     * Returns the packet-byte at position i.
     */
    protected int getByte(int i) {
        return packet[i];
    }

    /**
     * Returns the packet in it's original byte-form. <br/><b>Note: </b> The
     * array returned is a clone of the array contained in the class. Changing
     * the values in the array will not affect the contents of the class.
     */
    protected int[] getPacket() {
        return (int[]) packet.clone();
    }

    /**
     * Returns the length of the entire packet in bytes.
     */
    protected int getLength() {
        return packet.length;
    }

    /**
     * Set the pointer to the start of the packet's data (after the length);
     */
    protected void pointer2start() {
        pointer = 3;
    }

    protected void setPointer(int moveTo) {
        pointer = moveTo;
    }

    protected int getPointer() {
        return pointer;
    }

    /**
     * Method that reads a Garmin-word in the packet and returns it as an int.
     * This method can be used to read both int and word from a Garmin-packet.
     */
    protected int readWord() {
        int sum = packet[pointer++];
        sum += packet[pointer++] << 8;
        return sum;
    }

    /**
     * Method that reads a signed Garmin-word in the packet and returns it as a
     * short.
     */
    protected short readSignedWord() {
        int sum = (short) packet[pointer++];
        sum += packet[pointer++] << 8;
        return (short) sum;
    }

    /**
     * Method that reads a Garmin-long in the packet and returns it as an int.
     */
    protected int readLong() {
        int res = packet[pointer++];
        res += packet[pointer++] << 8;
        res += packet[pointer++] << 16;
        res += packet[pointer++] << 24;

        return res;
    }

    /**
     * Method that reads a Garmin-long in the packet and returns it as an int.
     */
    protected boolean readBoolean() {
        int res = packet[pointer++];
        if (res == 0)
            return false;
        return true;
    }

    /**
     * Method that reads a null-terminated string.
     */
    protected String readNullTerminatedString() {
        StringBuffer res = new StringBuffer(20);
        while ((packet[pointer] != 0) && (pointer != packet.length)) {
            res.append((char) packet[pointer++]);
        }
        ++pointer;
        return res.toString();
    }

    protected String readFixedLengthString(int length) {
        String res = new String();
        int target = pointer + length;
        while (pointer < target) {
            if (packet[pointer] != 0 && packet[pointer] != 255) {
                res += (char) packet[pointer];
            }
            pointer++;
        }
        return res;
    }

    /**
     * Method that reads a Garmin-byte in the packet and returns it as a short.
     */
    protected short readByte() {
        return (short) packet[pointer++];
    }

    /**
     * Method that reads a Garmin-double in the packet and returns it as a
     * double.
     */
    protected double readDouble() {
        long res = 0;

        res += ((long) packet[pointer++]);
        res += ((long) packet[pointer++]) << 8;
        res += ((long) packet[pointer++]) << 16;
        res += ((long) packet[pointer++]) << 24;
        res += ((long) packet[pointer++]) << 32;
        res += ((long) packet[pointer++]) << 40;
        res += ((long) packet[pointer++]) << 48;
        res += ((long) packet[pointer++]) << 56;

        return Double.longBitsToDouble(res);
    }

    /**
     * Method that reads a Garmin-float in the packet and returns it as a float.
     */
    protected float readFloat() {
        int res = 0;
        res += packet[pointer++];
        res += packet[pointer++] << 8;
        res += packet[pointer++] << 16;
        res += packet[pointer++] << 24;

        return Float.intBitsToFloat(res);
    }

    protected Position readPosition() {
        long la = readLong();
        long lo = readLong();

        if (la == 0x7FFFFFFF && lo == 0x7FFFFFFF) {
            return null;
        } else {
            // have to convert from semicircles to degrees
            PositionDegrees lat = new PositionDegrees((la * 180)
                    / Math.pow(2.0d, 31.0d));
            PositionDegrees lon = new PositionDegrees((lo * 180)
                    / Math.pow(2.0d, 31.0d));

            return new Position(lat.convertToRadians(), lon.convertToRadians());
        }
    }

    protected void visit(GarminGPSDataVisitor visitor) {
        // TODO: put something in here
    }

    public String getPacketType() {
        return "raw packet";
    }
}