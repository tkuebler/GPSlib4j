package com.diddlebits.gpslib4j;

public abstract class StringValidator {
    /**
     * Check if the string is valid and raise an exception if not
     */
    public void throwIfInvalid(String fieldName, String txt)
            throws InvalidFieldValue {
        if (!checkSyntax(txt)) {
            throw new InvalidFieldValue(fieldName, txt,
                    "Invalid string content (" + getSyntax() + ")");
        }
    }

    /**
     * Check the content of txt.
     * 
     * @param txt
     *            What to check
     * @return True if good.
     */
    public abstract boolean checkSyntax(String txt);

    /**
     * @return A text describing the syntax of what's allowed.
     */
    public abstract String getSyntax();
}
