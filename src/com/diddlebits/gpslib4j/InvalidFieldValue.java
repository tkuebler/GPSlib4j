package com.diddlebits.gpslib4j;

/**
 * Exception raised when the value of a field is not allowed.
 */
public class InvalidFieldValue extends Exception {
    public InvalidFieldValue(String fieldName, String fieldValue, String message) {
        super(fieldName + "='" + fieldValue + "': " + message);
    }

    private static final long serialVersionUID = -4929108398855549141L;
}
