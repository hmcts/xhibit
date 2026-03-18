package uk.gov.courtservice.xhibit.business.exceptions.messaging;

/**
 * <p>
 * Title: TerminalLocationException
 * </p>
 * <p>
 * Description: This exception is thrown when the messaging process fail to find
 * a court and court site for the location specified in the terminal location.
 * The failure conditions are
 * 
 * <li>Court specified in the location not available in XHIBIT</li>
 * <li>Site specified in the location not available in XHIBIT</li>
 * <li>Location is not in the format /Court/Site/Location or /Court/SIte</li>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath / Neil Entwistle
 * @version 1.0
 */

public class TerminalLocationException extends Exception {
	
	static final long serialVersionUID = -5184529921705090496L;

    /**
     * Location format is invalid
     */
    public static final int INVALID_FORMAT = 0;

    /**
     * Specified court is not found
     */
    public static final int COURT_NOT_FOUND = 1;

    /**
     * Specified site is not found
     */
    public static final int SITE_NOT_FOUND = 2;

    /**
     * Location specified for the terminal
     */
    private String location;

    /**
     * Court specified for the terminal
     */
    private String court;

    /**
     * Site specified for the terminal
     */
    private String site;

    /**
     * Type of exception
     */
    private int type;

    /**
     * Initialises the type, location, court and court site
     * 
     * @param type
     * @param location
     * @param court
     * @param site
     */
    public TerminalLocationException(int type, String location, String court, String site) {
        this.type = type;
        this.location = location;
        this.court = court;
        this.site = site;

    }

    /**
     * Initialises the type and location
     * 
     * @param type
     * @param location
     * @param court
     * @param site
     */
    public TerminalLocationException(int type, String location, String court) {
        this(type, location, court, "");
    }

    /**
     * Initialises the type and location
     * 
     * @param type
     * @param location
     * @param court
     * @param site
     */
    public TerminalLocationException(int type, String location) {
        this(type, location, "", "");
    }

    /**
     * Returns the exception message
     * 
     * @return Exception message
     */
    public String getMessage() {

        switch (type) {
        case INVALID_FORMAT:
            return "Invalid location format:" + this;
        case COURT_NOT_FOUND:
            return "Court not found:" + this;
        case SITE_NOT_FOUND:
            return "Court site not found:" + this;
        default:
            throw new IllegalStateException("Unknown type");
        }

    }

    /**
     * Pretty print
     * 
     * @return
     */
    public String toString() {

        StringBuffer prettyPrint = new StringBuffer("location=");
        prettyPrint.append(location);
        prettyPrint.append(";court=");
        prettyPrint.append(court);
        prettyPrint.append(";site=");
        prettyPrint.append(site);

        return prettyPrint.toString();

    }
}