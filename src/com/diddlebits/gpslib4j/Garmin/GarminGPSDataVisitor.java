package com.diddlebits.gpslib4j.Garmin;

import java.util.Date;

import com.diddlebits.gpslib4j.*;

/**
 * Base Garmin specialized visitor.
 */
public abstract class GarminGPSDataVisitor {
    /** Offset between the Garmin time and the system time */
    protected static long TimeOffset = 631065600;

    GarminGPSDataVisitor() {
    }

    public abstract void startEntry(String type);

    public abstract void endEntry() throws InvalidFieldValue;

    public abstract boolean boolField(String name, boolean value)
            throws InvalidFieldValue;

    public abstract long intField(int type, String name, long value,
            long minValue, long maxValue, long nullValue)
            throws InvalidFieldValue;

    public abstract double floatField(int type, String name, double value,
            double minValue, double maxValue) throws InvalidFieldValue;

    public abstract String stringField(int type, String name, String value,
            int maxLength, StringValidator validator) throws InvalidFieldValue;

    public abstract Position positionField(String name, Position value)
            throws InvalidFieldValue;

    public abstract Date timeField(int type, String name, Date value)
            throws InvalidFieldValue;

    public abstract int enumField(int type, String name, int value,
            GPSEnumDefinition definition) throws InvalidFieldValue;

    static protected String GetPureFieldName(String name) {
        if (name.startsWith("+") || name.startsWith("-")) {
            return name.substring(1);
        } else {
            return name;
        }
    }

    static protected boolean IsVirtualField(String name) {
        return name.startsWith("-");
    }

    static protected boolean IsInternalField(String name) {
        return name.startsWith("+");
    }
}
