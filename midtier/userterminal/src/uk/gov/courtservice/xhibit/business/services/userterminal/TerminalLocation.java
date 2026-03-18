package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.io.Serializable;

import org.apache.log4j.Logger;

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
 * @author unascribed
 * @version 1.0
 */

public class TerminalLocation implements Serializable {

    private static final long serialVersionUID = -9067476177564399631L;

    // Formatted location
    private String location;

    // Name of the terminal
    private String name;

    // Logger
    private static Logger logger = Logger.getLogger(TerminalLocation.class);

    /**
     * Initializes the terminal
     * 
     * @param Name
     *            of the Terminal
     * @param Location
     *            of the terminal in /Court/CourtSite/CourtRoom format
     */
    public TerminalLocation(String newName, String newLocation) {
        if (newName == null) {
            throw new IllegalArgumentException("Terminal name can't be null");
        }
        logger.debug("Terminal name:" + newName);
        name = newName.toLowerCase();

        if (newLocation == null) {
            throw new IllegalArgumentException("Location can't be null");
        }
        logger.debug("Terminal location:" + newLocation);
        location = newLocation;
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

    /**
     * Pretty print
     * 
     * @return
     */
    public String toString() {
        StringBuffer prettyPrint = new StringBuffer();

        prettyPrint.append("Name:" + name + "; ");
        prettyPrint.append("Location:" + location + ";");
        return prettyPrint.toString();
    }
}