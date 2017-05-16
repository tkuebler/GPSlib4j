package com.diddlebits.gpslib4j;

/**
 * Thrown by methods in classes extending the GPS-class, if the implemented
 * GPS-unit does not support the feature requested in the method.
 */
public class FeatureNotSupportedException extends Exception {
    private static final long serialVersionUID = -6810576254175691377L;
}