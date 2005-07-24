package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;

/**
 * This class is a read only visitor that forwards to a IGPSDataReadVisitor.
 */
public class GarminGPSDataReadVisitor extends GarminGPSDataVisitor {
    /** The visitor we are forwarding to */
    private IGPSDataReadVisitor visitor;

    GarminGPSDataReadVisitor(IGPSDataReadVisitor xVisitor) {
        visitor = xVisitor;
    }

    public boolean boolField(String name, boolean value) {
        if (!IsInternalField(name)) {
            visitor.boolField(GetPureFieldName(name), true, value);
        }
        return value;
    }

    public long intField(int type, String name, long value, long minValue,
            long maxValue, long nullValue) {
        if (!IsInternalField(name)) {
            visitor.intField(GetPureFieldName(name), value != nullValue, value,
                    minValue, maxValue);
        }
        return value;
    }

    public double floatField(int type, String name, double value,
            FloatSpecification spec) {
        if (!IsInternalField(name)) {
            visitor.floatField(GetPureFieldName(name), value <= 9e24, value,
                    spec);
        }
        return value;
    }

    public String stringField(int type, String name, String value,
            StringValidator validator) {
        if (!IsInternalField(name)) {
            visitor.stringField(GetPureFieldName(name), value!=null && value.length() > 0,
                    value, validator);
        }
        return value;
    }

    public Position positionField(String name, Position value) {
        if (!IsInternalField(name)) {
            visitor.positionField(GetPureFieldName(name), value != null, value);
        }
        return value;
    }

    public Date timeField(int type, String name, Date value) {
        if (!IsInternalField(name)) {
            visitor.timeField(GetPureFieldName(name), value != null, value);
        }
        return value;
    }

    public int enumField(int type, String name, int value,
            GPSEnumDefinition definition) {
        if (!IsInternalField(name)) {
            visitor.enumField(GetPureFieldName(name), true, value, definition);
        }
        return value;
    }

    public void startEntry(String type) {
        visitor.startEntry(type);
    }

    public void endEntry() {
        visitor.endEntry();
    }
}
