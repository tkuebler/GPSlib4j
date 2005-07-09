package com.diddlebits.gpslib4j;

import java.util.Date;

public class ToStringGPSDataReadVisitor implements IGPSDataReadVisitor {
    private StringBuffer string;

    public ToStringGPSDataReadVisitor() {
        string = new StringBuffer(50);
    }

    public void boolField(String name, boolean isDefined, boolean value) {
        if (isDefined) {
            string.append("  " + name + "=");
            if (value) {
                string.append("true");
            } else {
                string.append("false");
            }
            string.append("\n");
        }
    }

    public void intField(String name, boolean isDefined, long value,
            long minValue, long maxValue) {
        if (isDefined) {
            string.append("  " + name + "=" + value + "\n");
        }
    }

    public void floatField(String name, boolean isDefined, double value,
            double minValue, double maxValue) {
        if (isDefined) {
            string.append("  " + name + "=" + value + "\n");
        }
    }

    public void stringField(String name, boolean isDefined, String value,
            int maxLength, StringValidator validator) {
        if (isDefined) {
            string.append("  " + name + "='" + value + "'\n");
        }
    }

    public void positionField(String name, boolean isDefined, Position value) {
        if (isDefined) {
            string.append("  " + name + "=[" + value.toString() + "]\n");
        }
    }

    public void timeField(String name, boolean isDefined, Date value) {
        if (isDefined) {
            string.append("  " + name + "='" + value.toString() + "'\n");
        }
    }

    public void enumField(String name, boolean isDefined, int value,
            GPSEnumDefinition definition) {
        if (isDefined) {
            try {
                string.append("  " + name + "='"
                        + definition.enumToString(value) + "'\n");
            } catch (InvalidFieldValue e) {
                string.append("  " + name + "=?????" + value + "?????\n");
            }
        }
    }

    public String getString() {
        return string.toString();
    }

    public void startEntry(String type) {
        string.append(type + " [\n");
    }

    public void endEntry() {
        string.append("]\n");
    }
}
