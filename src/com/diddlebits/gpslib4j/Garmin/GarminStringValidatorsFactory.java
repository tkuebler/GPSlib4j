package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.InvalidFieldValue;
import com.diddlebits.gpslib4j.RegexpStringValidator;
import com.diddlebits.gpslib4j.StringValidator;

public class GarminStringValidatorsFactory {
    public static StringValidator CreateComment(int maxLength,
            boolean emptyAllowed) throws InvalidFieldValue {
        return new RegexpStringValidator("comment", "^[-A-Z0-9 ]*$", maxLength,
                emptyAllowed);
    }

    public static StringValidator CreateWaypointIdent(int maxLength,
            boolean emptyAllowed) throws InvalidFieldValue {
        return new RegexpStringValidator("waypoint indentifier", "^[A-Z0-9 ]*",
                maxLength, emptyAllowed);
    }

    public static StringValidator CreateIdent(int maxLength,
            boolean emptyAllowed) throws InvalidFieldValue {
        return new RegexpStringValidator("identifer", "^[-A-Z0-9 ]*",
                maxLength, emptyAllowed);
    }

    public static StringValidator CreateCountryCode(int maxLength,
            boolean emptyAllowed) throws InvalidFieldValue {
        return new RegexpStringValidator("country code", "^[-A-Z0-9 ]*$",
                maxLength, emptyAllowed);
    }

    public static StringValidator CreateUnchecked(int maxLength,
            boolean emptyAllowed) throws InvalidFieldValue {
        return new RegexpStringValidator("free format", "^.*$", maxLength,
                emptyAllowed);
    }

}
