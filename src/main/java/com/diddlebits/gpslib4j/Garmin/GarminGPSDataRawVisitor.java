package com.diddlebits.gpslib4j.Garmin;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.diddlebits.gpslib4j.FloatSpecification;
import com.diddlebits.gpslib4j.GPSEnumDefinition;
import com.diddlebits.gpslib4j.IntegerSpecification;
import com.diddlebits.gpslib4j.InvalidFieldValue;
import com.diddlebits.gpslib4j.Position;
import com.diddlebits.gpslib4j.StringValidator;

public class GarminGPSDataRawVisitor extends GarminGPSDataVisitor {

    private int[] packet;

    private int pos;

    public GarminGPSDataRawVisitor(GarminPacket source) {
        super();
        packet = new int[256 + 6];
        packet[0] = GarminRawPacket.DLE;
        packet[1] = source.getPacketId();
        packet[2] = 0; // length, will be filled later
        pos = 3;
    }

    public void startEntry(String type) {
        pos = 3;
    }

    public void endEntry() throws InvalidFieldValue {
    }

    public boolean boolField(String name, boolean value)
            throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        packet[pos++] = value ? 1 : 0;
        return value;
    }

    public long intField(int type, String name, long value,
            IntegerSpecification spec, long nullValue) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        if (spec != null && value != nullValue) {
            spec.throwIfInvalid(name, value);
        }
        switch (type) {
        case GarminPacket.UINT8:
            packet[pos++] = (int) value & 0xFF;
            break;

        case GarminPacket.UINT16:
            packet[pos++] = (int) value & 0xFF;
            packet[pos++] = (int) (value & 0xFF00) >> 8;
            break;

        case GarminPacket.SINT16:
            // TODO: not sure it will work...
            packet[pos++] = (int) value & 0xFF;
            packet[pos++] = (int) (value & 0xFF00) >> 8;
            break;

        case GarminPacket.UINT32:
            packet[pos++] = (int) value & 0xFF;
            packet[pos++] = (int) (value & 0xFF00) >> 8;
            packet[pos++] = (int) (value & 0xFF0000) >> 16;
            packet[pos++] = (int) (value & 0xFF000000) >> 24;
            break;

        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
        return value;
    }

    public double floatField(int type, String name, double value,
            FloatSpecification spec) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        if (spec != null) {
            spec.throwIfInvalid(name, value);
        }
        switch (type) {
        case GarminPacket.FLOAT32:
            int intBits = Float.floatToIntBits((float) value);
            packet[pos++] = intBits & 0xFF;
            packet[pos++] = (intBits & 0xFF00) >> 8;
            packet[pos++] = (intBits & 0xFF0000) >> 16;
            packet[pos++] = (intBits & 0xFF000000) >> 24;
            break;

        case GarminPacket.FLOAT64:
            long longBits = Double.doubleToLongBits(value);
            packet[pos++] = (int) (longBits & 0xFFl);
            packet[pos++] = (int) ((longBits & 0xFF00l) >> 8);
            packet[pos++] = (int) ((longBits & 0xFF0000l) >> 16);
            packet[pos++] = (int) ((longBits & 0xFF000000l) >> 24);
            packet[pos++] = (int) ((longBits & 0xFF00000000l) >> 32);
            packet[pos++] = (int) ((longBits & 0xFF0000000000l) >> 40);
            packet[pos++] = (int) ((longBits & 0xFF000000000000l) >> 48);
            packet[pos++] = (int) ((longBits & 0xFF00000000000000l) >> 56);
            break;

        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
        return value;
    }

    public String stringField(int type, String name, String value,
            StringValidator validator) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        if (validator != null) {
            validator.throwIfInvalid(name, value);
        }
        int cpt;
        switch (type) {
        case GarminPacket.ACHAR:
            if (value != null) {
                for (cpt = 0; cpt < validator.getMaxLength()
                        && cpt < value.length(); ++cpt) {
                    packet[pos++] = value.charAt(cpt);
                }
                for (cpt = value.length(); cpt < validator.getMaxLength(); ++cpt) {
                    packet[pos++] = 0;
                }
            } else {
                for (cpt = 0; cpt < validator.getMaxLength(); ++cpt) {
                    packet[pos++] = 0;
                }
            }
            break;

        case GarminPacket.VCHAR:
            if (value != null) {
                for (cpt = 0; cpt < validator.getMaxLength()
                        && cpt < value.length(); ++cpt) {
                    packet[pos++] = value.charAt(cpt);
                }
            }
            packet[pos++] = 0;
            break;
        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
        return value;
    }

    public Position positionField(String name, Position value)
            throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        long longLat;
        long longLon;
        if (value != null) {
            longLat = Math.round(value.getLatitudeDegrees()
                    * Math.pow(2.0d, 31.0d) / 180.0);
            longLon = Math.round(value.getLongitudeDegrees()
                    * Math.pow(2.0d, 31.0d) / 180.0);
        } else {
            longLat = 0x7FFFFFFF;
            longLon = 0x7FFFFFFF;
        }
        intField(GarminRawPacket.UINT32, name, longLat, null, 0);
        intField(GarminRawPacket.UINT32, name, longLon, null, 0);
        return value;
    }

    public Date timeField(int type, String name, Date value)
            throws InvalidFieldValue {
        // TODO Auto-generated method stub
        if (IsVirtualField(name))
            return value;
        switch (type) {
        case GarminPacket.LONG_DATE:
            long secs;
            if (value != null) {
                secs = value.getTime();
            } else {
                secs = 0xFFFFFFFFl;
            }
            intField(GarminRawPacket.UINT32, name, secs, null, 0xFFFFFFFFl);
            break;

        case GarminPacket.STRUCT_DATE:
            Calendar calendar = GregorianCalendar.getInstance(TimeZone
                    .getTimeZone("UTC"));
            calendar.setTime(value);
            packet[pos++] = calendar.get(Calendar.MONTH);
            packet[pos++] = calendar.get(Calendar.DAY_OF_MONTH);
            packet[pos++] = calendar.get(Calendar.YEAR);
            packet[pos++] = calendar.get(Calendar.HOUR);
            packet[pos++] = calendar.get(Calendar.MINUTE);
            packet[pos++] = calendar.get(Calendar.SECOND);
            break;
        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
        return value;
    }

    public int enumField(int type, String name, int value,
            GPSEnumDefinition definition) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        definition.enumToString(value);
        intField(type, name, value, null, -1);
        return value;
    }

    public GarminRawPacket getRawPacket() throws InvalidPacketException {
        int[] copy = new int[pos + 3];
        System.arraycopy(packet, 0, copy, 0, pos);
        copy[2] = pos - 3;
        copy[pos] = 0;
        copy[pos + 1] = GarminRawPacket.DLE;
        copy[pos + 2] = GarminRawPacket.ETX;
        if (packet[2] > 255) {
            throw new InvalidPacketException(packet, 2);
        }
        return new GarminRawPacket(copy, true);
    }
}
