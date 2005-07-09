package com.diddlebits.gpslib4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexpStringValidator extends StringValidator {
    protected Pattern regexp;

    public RegexpStringValidator(String fieldName, String txt)
            throws InvalidFieldValue {
        try {
            regexp = Pattern.compile(txt);
        } catch (PatternSyntaxException e) {
            throw new InvalidFieldValue(fieldName, txt, "Invalid regexp: "
                    + e.toString());
        }
    }

    public boolean checkSyntax(String txt) {
        Matcher matcher = regexp.matcher(txt);
        return matcher.matches();
    }

    public String getSyntax() {
        return "/" + regexp.toString() + "/";
    }

}
