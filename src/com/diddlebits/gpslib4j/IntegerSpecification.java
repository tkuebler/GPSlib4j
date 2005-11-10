package com.diddlebits.gpslib4j;

public class IntegerSpecification {
    private long min;

    private long max;

    private boolean canBeNull;

    public IntegerSpecification(long xMin, long xMax, boolean xCanBeNull) {
        min = xMin;
        max = xMax;
        canBeNull = xCanBeNull;
    }

    public IntegerSpecification(GPSEnumDefinition definition) {
        min = definition.getMinValue();
        max = definition.getMaxValue();
        canBeNull = definition.isCanBeNull();
    }

    public boolean isCanBeNull() {
        return canBeNull;
    }

    public long getMax() {
        return max;
    }

    public long getMin() {
        return min;
    }

    public void warningIfInvalid(String fieldName, long value) {
        String result = checkSyntax(value);
        if (result != null) {
            System.out.println("Invalid integer content for field " + fieldName
                    + ": " + result);
        }
    }

    private String checkSyntax(long value) {
        if (value < min) {
            return "Out of range (" + value + "<" + min + ")";
        } else if (value > max) {
            return "Out of range (" + value + ">" + max + ")";
        }
        return null;
    }

    public void throwIfInvalid(String name, long value)
            throws InvalidFieldValue {
        String result = checkSyntax(value);
        if (result != null) {
            throw new InvalidFieldValue(name, Long.toString(value), result);
        }
    }

    public int getMaxStringLength() {
        int result;
        if (Math.abs(min) < Math.abs(max))
            result = (int) Math.floor(Math.log(Math.abs(min)) / Math.log(10)) + 1;
        else
            result = (int) Math.floor(Math.log(Math.abs(max)) / Math.log(10)) + 1;

        if (min < 0) {
            result += 1;
        }
        return result;
    }

    public long convertFromString(String value) throws InvalidFieldValue {
        short tmp;
        try {
            tmp = Short.parseShort(value);
        } catch (NumberFormatException e) {
            throw new InvalidFieldValue(GPSFields.IndexField, value, e
                    .getMessage());
        }
        throwIfInvalid(GPSFields.IndexField, tmp);
        return tmp;
    }
}
