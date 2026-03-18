package uk.gov.courtservice.xhibit.business.services.messaging.im;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.TerminalLocationException;

/**
 * <p>
 * Title: TerminalLocation
 * </p>
 * <p>
 * Description: Represents the terminal name and location.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj / Neil Entwistle
 * @version $Id: TerminalLocation.java,v 1.5 2006/06/05 12:29:13 bzjrnl Exp $
 */
public class TerminalLocation {
    // Logger
    private static final String HIDDEN_PREFIX = "_";

    private static final Logger log = CSServices.getLogger(TerminalLocation.class);

    /**
     * Constant to indicate the terminal is attached to a court site
     */
    public static final String COURT_SITE = "S";

    /**
     * Constant to indicate the terminal is attached to a court room
     */
    public static final String COURT_ROOM = "R";

    /**
     * Location delimitter
     */
    public static final String LOCATION_DELIM = "/";

    /**
     * Underscore delimitter
     */
    public static final String UNDERSCORE_DELIM = "_";

    /**
     * Space string
     */
    public static final String STR_SPACE = " ";

    // Formatted location
    private String location;

    // Name of the terminal
    private String name;

    // Name of the court
    private String courtName;

    // Name of the court site
    private String courtSiteName;

    // Name of the court room
    private String courtRoomName;

    /**
     * Initializes the terminal
     * 
     * @param Name
     *            of the Terminal
     * @param Location
     *            of the terminal in /Court/CourtSite/CourtRoom format
     */
    public TerminalLocation(String newName, String newLocation) throws TerminalLocationException {
        log.debug("Terminal location:" + location);
        log.debug("Terminal location:" + newLocation);

        if (newName == null) {
            throw new IllegalArgumentException("Terminal name can't be null");
        }

        if (newLocation == null) {
            throw new IllegalArgumentException("Location can't be null");
        }

        name = newName.toLowerCase();

        location = newLocation;

        // Now parse the location
        parseLocation();

        log.debug("Terminal location initialized: " + this);
    }

    /**
     * Returns the terminal name
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the court name to which the terminal is attached
     * 
     * @return
     */
    public String getCourtName() {
        return courtName;
    }

    /**
     * Returns the court site to which the terminal is attached
     * 
     * @return
     */
    public String getCourtSiteName() {
        return courtSiteName;
    }

    /**
     * Returns the court room to which the terminal is attached
     * 
     * @return
     */
    public String getCourtRoomName() {
        return courtRoomName;
    }

    /**
     * Returns the court room to which the terminal is attached
     * 
     * @return
     */
    public String getDisplayCourtRoomName() {
        return courtRoomName;
    }

    /**
     * Returns the terminal location
     * 
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * Checks the equality of two terminals
     * 
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        return obj != null && obj instanceof TerminalLocation && name.equals(((TerminalLocation) obj).getName());
    }

    public boolean isHidden() {
        return (getCourtRoomName().startsWith(HIDDEN_PREFIX));
    }

    /**
     * Pretty print
     * 
     * @return
     */
    public String toString() {
        StringBuffer prettyPrint = new StringBuffer();

        prettyPrint.append("Name:" + name + ";");
        prettyPrint.append("Court:" + courtName + ";");
        prettyPrint.append("Court Site:" + courtSiteName + ";");
        prettyPrint.append("Court Room:" + courtRoomName + ";");

        return prettyPrint.toString();
    }

    /**
     * This method parses the location string to find the court, court site and
     * court room names. The location string is in the format
     * /Court/CourtSite/CourtRoom. If the CourtRoom element is empty, it is
     * assumed that the terminal is directly attached to the court site.
     */
    private void parseLocation() throws TerminalLocationException {
        log.debug("parseLocation entered");

        StringTokenizer tok = new StringTokenizer(location, LOCATION_DELIM);

        // Get the number of tokens
        if (tok.countTokens() != 3) {
            throw new TerminalLocationException(TerminalLocationException.INVALID_FORMAT, location);
        }

        // Set the court name
        courtName = tok.nextToken();

        // Set the court site name
        courtSiteName = tok.nextToken();

        // Set the court room name
        courtRoomName = tok.nextToken();

        log.debug("parseLocation exited");
    }
}