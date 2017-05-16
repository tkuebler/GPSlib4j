package com.diddlebits.gpslib4j.Garmin;

/**
 * Generated when we received a packet that is not supported by gpslib4j for the
 * moment
 */
public class ProtocolNotSupportedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -6822945963244015021L;

    public ProtocolNotSupportedException(char tag, int protocol) {
        super("\nProtocol not supported by gpslib4j:\n" + "tag: " + tag + '\n'
                + "protocol: " + protocol + '\n');
    }
}
