package uk.gov.courtservice.xhibit.business.vos.services.userterminal;

import uk.gov.courtservice.framework.client.SessionPropertyKey;

/**
 * <p>
 * Title: Static variables for Terminal Information
 * </p>
 * <p>
 * Description: These are used to identify the items in the Map returned from
 * the call to
 * <code>TerminalLocationControllerBeanBusinessDelegate.getXHIBITTerminalLocation(computerName)</code>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: UserTerminalProperties.java,v 1.4 2005/11/29 09:20:39 xztnfq
 *          Exp $
 */
public class UserTerminalProperties implements SessionPropertyKey {
	private static final long serialVersionUID = -4344364982772923498L;
	public static final UserTerminalProperties TERMINAL_ID = new UserTerminalProperties("terminalId");

    public static final UserTerminalProperties TERMINAL_NAME = new UserTerminalProperties("terminalName");

    public static final UserTerminalProperties TERMINAL_LOCATION = new UserTerminalProperties("terminalLocation");

    public static final UserTerminalProperties COURT_ID = new UserTerminalProperties("courtId");

    public static final UserTerminalProperties COURT_NAME = new UserTerminalProperties("courtName");

    public static final UserTerminalProperties COURT_SITE_ID = new UserTerminalProperties("courtSiteId");

    public static final UserTerminalProperties COURT_ROOM_ID = new UserTerminalProperties("courtRoomId");

    public static final UserTerminalProperties USER_NAME = new UserTerminalProperties("userName");
    
    public static final UserTerminalProperties DISPLAY_NAME = new UserTerminalProperties("displayName");

    public static final UserTerminalProperties ROAMING = new UserTerminalProperties("roaming");

    public static final UserTerminalProperties COUNTRY = new UserTerminalProperties("country");

    public static final UserTerminalProperties LANGUAGE = new UserTerminalProperties("language");

    private String propertyName;

    private UserTerminalProperties(String propertyName) {
        this.propertyName = propertyName;
    }

    public String toString() {
        return propertyName;
    }

    public boolean equals(Object value) {
        if (value instanceof UserTerminalProperties) {
            return this.propertyName.equals(((UserTerminalProperties) value).propertyName);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.propertyName.hashCode();
    }

}