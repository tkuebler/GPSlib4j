package com.diddlebits.gpslib4j.Garmin;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.diddlebits.gpslib4j.*;

/**
 * Vistor that reads a raw packet and initialise the fields with it.
 * 
 * @see GarminPacket::initFromRawPacket
 */
public class GarminGPSDataParserVisitor extends GarminGPSDataVisitor {
    /** The raw packet we are parsing */
    private GarminRawPacket source;

    public GarminGPSDataParserVisitor(GarminRawPacket xSource) {
        source = xSource;
        source.pointer2start();
    }

    public boolean boolField(String name, boolean value)
            throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        return source.readBoolean();
    }

    public long intField(int type, String name, long value, long minValue,
            long maxValue, long nullValue) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        switch (type) {
        case GarminPacket.UINT8:
            return source.readByte();
        case GarminPacket.UINT16:
            return source.readWord();
        case GarminPacket.SINT16:
            return source.readSignedWord();
        case GarminPacket.UINT32:
            return source.readLong();
        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
    }

    public double floatField(int type, String name, double value,
            double minValue, double maxValue) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        switch (type) {
        case GarminPacket.FLOAT32:
            return source.readFloat();
        case GarminPacket.FLOAT64:
            return source.readDouble();
        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
    }

    public String stringField(int type, String name, String value,
            int maxLength, StringValidator validator) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;

        String result;
        switch (type) {
        case GarminPacket.ACHAR:
            result = source.readFixedLengthString(maxLength);
            break;
        case GarminPacket.VCHAR:
            result = source.readNullTerminatedString();
            break;
        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
        
        if(validator!=null) validator.throwIfInvalid(name, result);
        return result;
    }

    public Position positionField(String name, Position value)
            throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        return source.readPosition();
    }

    public Date timeField(int type, String name, Date value)
            throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        switch (type) {
        case GarminPacket.LONG_DATE:
            int secs = source.readLong();
            return new Date(secs + TimeOffset);
        case GarminPacket.STRUCT_DATE:
            int month = source.readByte();
            int day = source.readByte();
            int year = source.readWord();
            int hour = source.readWord();
            int minute = source.readByte();
            int second = source.readByte();
            Calendar tmp = GregorianCalendar.getInstance();
            tmp.set(year - 1900, month - 1, day, hour, minute, second);
            return tmp.getTime();
        default:
            throw new InvalidFieldValue(name + " type", Integer.toString(type),
                    "Unknown data type (internal error)");
        }
    }

    public int enumField(int type, String name, int value,
            GPSEnumDefinition definition) throws InvalidFieldValue {
        if (IsVirtualField(name))
            return value;
        return (int) intField(type, name, value, definition.getMinValue(),
                definition.getMaxValue(), definition.getMaxValue() + 1);
    }


    public void startEntry(String type) {
        source.pointer2start();
    }

    public void endEntry() throws InvalidFieldValue {
        // check we read the whole packet
        if(source.getPointer()!=source.getLength()-3) {
            throw new InvalidFieldValue("read", String.valueOf(source.getPointer()), "data length="+String.valueOf(source.getLength()-3));
        }
    }
}
