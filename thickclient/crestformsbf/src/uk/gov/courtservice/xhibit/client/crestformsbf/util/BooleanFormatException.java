package uk.gov.courtservice.xhibit.client.crestformsbf.util;

/**
 * NumberFormatException equiverlent for booleans
 * 
 * @author William Fardell, Xdevelopment
 * @version 1.0
 */

public class BooleanFormatException extends RuntimeException {
    public BooleanFormatException(String value) {
        super("Value " + value + " is not a valid Boolean.");
    }
}
