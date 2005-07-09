package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;
import com.diddlebits.gpslib4j.IGPSDataWriteVisitor.NullField;

/**
 * This class is a read/write visitor that forwards to a IGPSDataWriteVisitor.
 */
public class GarminGPSDataWriteVisitor extends GarminGPSDataVisitor {
    /** The visitor we are forwarding to */
    private IGPSDataWriteVisitor visitor;

    GarminGPSDataWriteVisitor(IGPSDataWriteVisitor xVisitor) {
        visitor = xVisitor;
    }

    public boolean boolField(String name, boolean value)
            throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;
        try {
            return visitor.boolField(GetPureFieldName(name), true, value);
        } catch (NullField e) {
            return false;
        }
    }

    public long intField(int type, String name, long value, long minValue,
            long maxValue, long nullValue) throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;
        try {
            long ret = visitor.intField(GetPureFieldName(name),
                    value != nullValue, value, minValue, maxValue);
            if (ret < minValue || ret > maxValue) {
                throw new InvalidFieldValue(GetPureFieldName(name), Long
                        .toString(ret), "Out of range (" + minValue + ".."
                        + maxValue + ")");
            }
            return ret;
        } catch (NullField e) {
            return nullValue;
        }
    }

    public double floatField(int type, String name, double value,
            double minValue, double maxValue) throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;
        try {
            double ret = visitor.floatField(GetPureFieldName(name),
                    value <= 9e24, value, minValue, maxValue);
            if (ret < minValue || ret > maxValue) {
                throw new InvalidFieldValue(GetPureFieldName(name), Double
                        .toString(ret), "Out of range (" + minValue + ".."
                        + maxValue + ")");
            }
            return ret;
        } catch (NullField e) {
            return 1e25;
        }
    }

    public String stringField(int type, String name, String value,
            int maxLength, StringValidator validator) throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;

        String ret = visitor.stringField(GetPureFieldName(name), value != null
                && value.length() > 0, value, maxLength, validator);

        if (ret.length() > maxLength) {
            throw new InvalidFieldValue(GetPureFieldName(name), ret,
                    "Too long (max length=" + maxLength + ")");
        }

        if (validator != null) validator.throwIfInvalid(name, ret);

        return ret;
    }

    public Position positionField(String name, Position value)
            throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;
        return visitor.positionField(GetPureFieldName(name), value != null,
                value);
    }

    public long timeField(String name, long value) throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;
        Date newVal;
        newVal = visitor.timeField(GetPureFieldName(name), value != 0xFFFFFFFF
                && value != 0x7FFFFFFF && value != 0, new Date(value
                + TimeOffset));
        if (newVal != null) {
            return newVal.getTime() - TimeOffset;
        } else {
            return 0x7FFFFFFF;
        }
    }

    public Date timeField(int type, String name, Date value)
            throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;
        return visitor.timeField(GetPureFieldName(name), value != null, value);
    }

    public int enumField(int type, String name, int value,
            GPSEnumDefinition definition) throws InvalidFieldValue {
        if (IsInternalField(name))
            return value;
        try {
            int ret = visitor.enumField(GetPureFieldName(name), true, value,
                    definition);
            definition.enumToString(ret);
            return ret;
        } catch (NullField e) {
            throw new InvalidFieldValue(GetPureFieldName(name), "null",
                    "Enum cannot be null");
        }
    }

    public void startEntry(String type) {
        visitor.startEntry(type);
    }

    public void endEntry() {
        visitor.endEntry();
    }
}
