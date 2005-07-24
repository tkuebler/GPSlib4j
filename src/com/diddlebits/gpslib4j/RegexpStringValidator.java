package com.diddlebits.gpslib4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexpStringValidator extends StringValidator {
    protected Pattern regexp;

    private int maxLength;

    private boolean emptyAllowed;

    public RegexpStringValidator(String fieldName, String expression,
            int xMaxLength, boolean xEmptyAllowed) throws InvalidFieldValue {
        try {
            regexp = Pattern.compile(expression);
        } catch (PatternSyntaxException e) {
            throw new InvalidFieldValue(fieldName, expression,
                    "Invalid regexp: " + e.toString());
        }
        maxLength = xMaxLength;
        emptyAllowed = xEmptyAllowed;
    }

    public String checkSyntax(String txt) {
        if(txt==null || txt.length()==0) {
            if(emptyAllowed) {
                return null;
            } else {
                return "Cannot be empty";
            }
        }
        
        if(txt.length()>maxLength) {
            return "Too long (>"+maxLength+")";
        }
        
        Matcher matcher = regexp.matcher(txt);
        if(!matcher.matches()) {
            return "Doesn't match expression /"+regexp.toString()+"/";
        }
        return null;
    }

    public String getSyntax() {
        return "/" + regexp.toString() + "/ length<="+maxLength+" emptyAllowed="+emptyAllowed;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public boolean isEmptyAllowed() {
        // TODO Auto-generated method stub
        return emptyAllowed;
    }

}
