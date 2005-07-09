package com.diddlebits.gpslib4j;

/**
 * Base class for
 * 
 * @author patrick
 * 
 */
public interface IGPSData {
    public String toString();

    /**
     * Calls the read only visitor's method accordingly to the fields of the
     * object.
     * 
     * @param visitor
     * @throws InvalidFieldValue
     */
    public void visit(IGPSDataReadVisitor visitor) throws InvalidFieldValue;

    /**
     * Calls the read/write visitor's method accordingly to the fields of the
     * object.
     * 
     * @param visitor
     * @throws InvalidFieldValue
     */
    public void visit(IGPSDataWriteVisitor visitor) throws InvalidFieldValue;
}
