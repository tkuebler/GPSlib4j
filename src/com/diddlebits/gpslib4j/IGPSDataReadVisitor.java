package com.diddlebits.gpslib4j;

import java.util.Date;

/**
 * Visitor used to get the values of a IGPSData object.
 * </P>
 * To use it:
 * 
 * <PRE>
 * 
 * class MyReadVisitor implements IGPSDataReadVisitor {...} ... MyReadVisitor
 * visitor=new MyReadVisitor(); IWaypointData wp=...; wp.visit(visitor);
 * 
 * </PRE>
 */
public interface IGPSDataReadVisitor {
    /**
     * Called when an entry starts.
     */
    void startEntry(String type);

    /**
     * Called when an entry is done.
     */
    void endEntry();

    /**
     * Called for each boolean fields.
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     */
    void boolField(String name, boolean isDefined, boolean value);

    /**
     * Called for each integer fields.
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     * @param minValue
     *            The minimum value.
     * @param maxValue
     *            The maximum value.
     */
    void intField(String name, boolean isDefined, long value, long minValue,
            long maxValue);

    /**
     * Called for each float fields.
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     * @param minValue
     *            The minimum value.
     * @param maxValue
     *            The maximum value.
     */
    void floatField(String name, boolean isDefined, double value,
            double minValue, double maxValue);

    /**
     * Called for each string fields.
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     * @param maxLength
     *            The maximum number of characters in the string.
     * @param validator
     *            An object used to validate the string.
     */
    void stringField(String name, boolean isDefined, String value,
            int maxLength, StringValidator validator);

    /**
     * Called for each position fields.
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     */
    void positionField(String name, boolean isDefined, Position value);

    /**
     * Called for each time fields.
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     */
    void timeField(String name, boolean isDefined, Date value);

    /**
     * Called for each enum fields (colors, icons, ...).
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     * @param definition
     *            The helper object to know about the different values of the
     *            enumerated type.
     */
    void enumField(String name, boolean isDefined, int value,
            GPSEnumDefinition definition);
}
