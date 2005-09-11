package com.diddlebits.gpslib4j;

public abstract class StringValidator {
    protected boolean emptyAllowed;

    StringValidator(boolean xEmptyAllowed) {
        emptyAllowed = xEmptyAllowed;
    }

    /**
     * Check if the string is valid and raise an exception if not
     */
    public void throwIfInvalid(String fieldName, String txt)
            throws InvalidFieldValue {
        String ret = checkSyntax(txt);
        if (ret != null) {
            throw new InvalidFieldValue(fieldName, txt,
                    "Invalid string content: " + ret);
        }
    }

    /**
     * Check the content of txt.
     * 
     * @param txt
     *            What to check
     * @return NULL if good, an error message if not.
     */
    public abstract String checkSyntax(String txt);

    /**
     * @return A text describing the syntax of what's allowed.
     */
    public abstract String getSyntax();

    /**
     * @return The maximum length of the string.
     */
    public abstract int getMaxLength();

    /**
     * @return True if an empty string is allowed.
     */
    public boolean isEmptyAllowed() {
        return emptyAllowed;
    }

    public void warningIfInvalid(String fieldName, String txt) {
        String ret = checkSyntax(txt);
        if (ret != null) {
            System.out.println("Invalid string content for field " + fieldName
                    + ": " + ret);
        }
    }
}
