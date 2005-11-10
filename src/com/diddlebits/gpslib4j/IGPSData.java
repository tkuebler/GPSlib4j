package com.diddlebits.gpslib4j;

import java.io.Serializable;

/**
 * Base class for
 * 
 * @author patrick
 * 
 */
public interface IGPSData extends Cloneable, Serializable {
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

    /**
     * Will use the Object.clone() method all the time, since all attributes
     * IGPSData childs are immutable.
     */
    Object clone() throws CloneNotSupportedException;
}
