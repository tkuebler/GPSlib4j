package com.diddlebits.gpslib4j;

public interface INamed extends IGPSData {
    /**
     * @return The identity of the object.
     */
    public String getIdent();

    /**
     * Change the identity of the object.
     */
    void setIdent(String ident) throws InvalidFieldValue;
}
