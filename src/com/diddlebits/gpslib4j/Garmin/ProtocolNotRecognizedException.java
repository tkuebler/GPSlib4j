package com.diddlebits.gpslib4j.Garmin;

/**
 * Generated when we received a packet that is not recognized
 */
public class ProtocolNotRecognizedException extends Exception {
    private static final long serialVersionUID = 5551210645895398827L;

    public ProtocolNotRecognizedException(char tag, int protocol) {
        super("\nProtocol not recognized by this GPS:\n" + "tag: " + tag + '\n'
                + "protocol: " + protocol + '\n');
    }
}
