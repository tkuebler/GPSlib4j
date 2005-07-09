package com.diddlebits.gpslib4j;

import java.util.Date;

/**
 * Visitor used to set the values of a IGPSData object.
 * </P>
 * To use it:
 * 
 * <PRE>
 * 
 * class MyWriteVisitor implements IGPSDataWriteVisitor {...} ... MyWriteVisitor
 * visitor=new MyWriteVisitor(); IWaypointData wp=...; wp.visit(visitor);
 * 
 * </PRE>
 */
public interface IGPSDataWriteVisitor {
    class NullField extends Exception {
        private static final long serialVersionUID = 3682038501477008806L;
    }

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
     * @return The new value.
     * @throws NullField
     *             if the field has to be set to NULL.
     */
    boolean boolField(String name, boolean isDefined, boolean value)
            throws NullField;

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
     * @return The new value.
     * @throws NullField
     *             if the field has to be set to NULL.
     */
    long intField(String name, boolean isDefined, long value, long minValue,
            long maxValue) throws NullField;

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
     * @return The new value.
     * @throws NullField
     *             if the field has to be set to NULL.
     */
    double floatField(String name, boolean isDefined, double value,
            double minValue, double maxValue) throws NullField;

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
     *            The maximum number of characters in the string
     * @param validator
     *            An object used to validate the string.
     * @return The new value or NULL.
     */
    String stringField(String name, boolean isDefined, String value,
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
     * @return The new value or NULL.
     */
    Position positionField(String name, boolean isDefined, Position value);

    /**
     * Called for each time fields.
     * 
     * @param name
     *            The fieldName.
     * @param isDefined
     *            True if the field is defined.
     * @param value
     *            The value of the field.
     * @return The new value or NULL.
     */
    Date timeField(String name, boolean isDefined, Date value);

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
     * @return The new value.
     * @throws NullField
     *             if the field has to be set to NULL.
     */
    int enumField(String name, boolean isDefined, int value,
            GPSEnumDefinition definition) throws NullField;
}
