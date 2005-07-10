package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.InvalidFieldValue;
import com.diddlebits.gpslib4j.RegexpStringValidator;
import com.diddlebits.gpslib4j.StringValidator;

public class CommonGarminStringValidators {
    private static CommonGarminStringValidators Instance;

    public static CommonGarminStringValidators Get() throws InvalidFieldValue {
        if (Instance == null) {
            Instance = new CommonGarminStringValidators();
        }
        return Instance;
    }

    public CommonGarminStringValidators() throws InvalidFieldValue {
        comment = new RegexpStringValidator("comment", "^[-A-Z0-9 ]*$");
        waypointIdent = new RegexpStringValidator("waypoint indentifier",
                "^[A-Z0-9 ]*");
        ident = new RegexpStringValidator("identifer", "^[-A-Z0-9 ]*");
        countryCode = new RegexpStringValidator("country code", "^[-A-Z0-9 ]*$");
    }

    private StringValidator comment;

    public StringValidator getComment() {
        if (comment == null) {
        }
        return comment;
    }

    private StringValidator waypointIdent;

    public StringValidator getWaypointIdent() {
        if (waypointIdent == null) {
        }
        return waypointIdent;
    }

    private StringValidator ident;

    public StringValidator getIdent() {
        if (ident == null) {
        }
        return ident;
    }

    private StringValidator countryCode;

    public StringValidator getCountryCode() {
        if (countryCode == null) {
        }
        return countryCode;
    }

}
