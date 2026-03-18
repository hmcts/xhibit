package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: TerminalValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 */

public class TerminalValue extends CSAbstractValue {
	private static final long serialVersionUID = 9090765385058824002L;
    // Name of the terminal
    private String terminalName;

    // IP address of the terminal
    private String terminalIp;

    // Description of the terminal
    private String description;

    // Location of the terminal
    private String location;

    /**
     * Default constructor
     */
    public TerminalValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public TerminalValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Returns terminal name
     * 
     * @return
     */
    public String getTerminalName() {
        return terminalName;
    }

    /**
     * Sets terminal name
     * 
     * @param val
     */
    public void setTerminalName(String val) {
        terminalName = val;
    }

    /**
     * Returns terminal IP
     * 
     * @return
     */
    public String getTerminalIp() {
        return terminalIp;
    }

    /**
     * Sets terminal IP
     * 
     * @param val
     */
    public void setTerminalIp(String val) {
        terminalIp = val;
    }

    /**
     * Returns terminal description
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets terminal description
     * 
     * @param val
     */
    public void setDescription(String val) {
        description = val;
    }

    /**
     * Returns terminal location
     * 
     * @return
     */
    public void setLocation(String val) {
        location = val;
    }

    /**
     * Sets terminal location
     * 
     * @param val
     */
    public String getLocation() {
        return location;
    }
}