package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.client.SessionPropertiesMap;
import uk.gov.courtservice.framework.jdbc.exception.ExceptionTranslator;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminal;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;

/**
 * <p>
 * Title: XHIBITTerminalLocations
 * </p>
 * <p>
 * Description: This class represents the terminal locations in XHIBIT
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
public class TerminalHelper {
    private static final String PROCEDURE_NAME = "{ call xhb_terminal_pkg.maintain_terminal ( ?, ? ) }";

    private static final Logger log = Logger.getLogger(TerminalHelper.class);

    /**
     * Initializes the home objects
     */
    private TerminalHelper() {
        // Reduce Access Permisions
    }

    /**
     * This method synchronizes the AD terminal locations with that in XHIBIT
     * 
     * @param Terminal
     *            locations in AD
     */
    public static String[] synchronize(TerminalLocation[] newLocations) {
        log.debug("Entered: synchronize");

        Connection con = null;
        PreparedStatement ps = null;

        ArrayList missingTerminalLocationList = new ArrayList();
        try {
            if (newLocations != null && newLocations.length > 0) {
                try {
                    con = CSServices.getServiceLocator().getDataSource().getConnection();

                    ps = con.prepareStatement(PROCEDURE_NAME);
                    ps.setQueryTimeout(0);

                    for (int i = 0; i < newLocations.length; i++) {
                        ps.setObject(1, newLocations[i].getName());
                        ps.setObject(2, newLocations[i].getLocation());
                        try {
                            ps.execute();
                        } catch (SQLException ex) {
                            if (ex.getErrorCode() == -20001 || ex.getErrorCode() == 20001) {
                                missingTerminalLocationList.add(newLocations[i]);
                            } else {
                                throw ex;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    throw ExceptionTranslator.getInstance().translate(ex);
                } finally {
                    closeResources(null, ps, con);
                }
            }
        } finally {
            if (missingTerminalLocationList.size() > 0) {
                log.fatal(getMissingTerminalLocationLog(missingTerminalLocationList));
            }
        }
        log.debug("Exited: synchronize");
        return getMissingTerminalLocationErrors(missingTerminalLocationList);
    }

    private static String getMissingTerminalLocationLog(ArrayList missingTerminalLocationList) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("The following terminals could not be synchronized, they have an invalid description:");
        Iterator terminalLocations = missingTerminalLocationList.iterator();
        while (terminalLocations.hasNext()) {
            TerminalLocation location = (TerminalLocation) terminalLocations.next();
            buffer.append("\n    ");
            buffer.append(location.getName());
            buffer.append(": ");
            buffer.append(location.getLocation());
        }
        return buffer.toString();
    }

    private static String[] getMissingTerminalLocationErrors(ArrayList missingTerminalLocationList) {
        String[] errors = new String[missingTerminalLocationList.size()];
        for (int i = 0; i < errors.length; i++) {
            TerminalLocation location = (TerminalLocation) missingTerminalLocationList.get(i);
            errors[i] = "Terminal \"" + location.getName() + "\" not synchronised. It has an invalid description \""
                    + location.getLocation() + "\".";
        }
        return errors;
    }

    private static void closeResources(ResultSet rs, Statement stmt, Connection con) {
        // Close the resultset
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable th) {
                log.error("Error closing ResultSet", th);
            }
        }

        // Close the statement
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Throwable th) {
                log.error("Error closing Statement", th);
            }
        }

        // Close the connection
        if (con != null) {
            try {
                con.close();
            } catch (Throwable th) {
                log.error("Error closing Connection", th);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Resources released");
        }
    }

    public static SessionPropertiesMap getXHIBITTerminalLocation(String userName, String displayName, String terminalName)
            throws TerminalNotFoundException {
        String methodName = "getXHIBITTerminalLocation";
        log.debug(methodName + "(" + terminalName + "):: Started");

        XhbTerminal terminal;
        try {
            terminal = XhbTerminalBeanHelper2.findByTerminalName(terminalName.toLowerCase());
        } catch (XhbTerminalBeanNotFoundException e) {
            throw new TerminalNotFoundException(terminalName);
        }

        // Map of properties
        SessionPropertiesMap map = new SessionPropertiesMap();

        XhbCourt court = getCourt(terminal);
        if (court != null) {
            map.put(UserTerminalProperties.COURT_ID, court.getCourtId());
            map.put(UserTerminalProperties.COURT_NAME, court.getDisplayName());
            map.put(UserTerminalProperties.COUNTRY, court.getCountry());
            map.put(UserTerminalProperties.LANGUAGE, court.getLanguage());
        }

        if (terminal.getCourtSiteId() == null && terminal.getCourtRoomId() != null) {
            map.put(UserTerminalProperties.COURT_SITE_ID, terminal.getXhbCourtRoom().getCourtSiteId());
        } else {
            map.put(UserTerminalProperties.COURT_SITE_ID, terminal.getCourtSiteId());
        }

        if (terminal.getCourtRoomId() != null) {
            map.put(UserTerminalProperties.COURT_ROOM_ID, terminal.getCourtRoomId());
        }

        map.put(UserTerminalProperties.TERMINAL_LOCATION, terminal.getLocation());
        map.put(UserTerminalProperties.TERMINAL_ID, terminal.getTerminalId());
        map.put(UserTerminalProperties.TERMINAL_NAME, terminal.getTerminalName());
        map.put(UserTerminalProperties.ROAMING, terminal.getRoaming());

        map.put(UserTerminalProperties.USER_NAME, userName);
        map.put(UserTerminalProperties.DISPLAY_NAME, displayName);
        return map;
    }

    public static SessionPropertiesMap getXHIBITTerminalLocation(String terminalName)
    throws TerminalNotFoundException {
        String methodName = "getXHIBITTerminalLocation";
        log.debug(methodName + "(" + terminalName + "):: Started");

        XhbTerminal terminal;
        try {
            terminal = XhbTerminalBeanHelper2.findByTerminalName(terminalName.toLowerCase());
        } catch (XhbTerminalBeanNotFoundException e) {
            throw new TerminalNotFoundException(terminalName);
        }

        //      Map of properties
        SessionPropertiesMap map = new SessionPropertiesMap();

        XhbCourt court = getCourt(terminal);
        if (court != null) {
            map.put(UserTerminalProperties.COURT_ID, court.getCourtId());
            map.put(UserTerminalProperties.COURT_NAME, court.getDisplayName());
            map.put(UserTerminalProperties.COUNTRY, court.getCountry());
            map.put(UserTerminalProperties.LANGUAGE, court.getLanguage());
        }

        if (terminal.getCourtSiteId() == null && terminal.getCourtRoomId() != null) {
            map.put(UserTerminalProperties.COURT_SITE_ID, terminal.getXhbCourtRoom().getCourtSiteId());
        } else {
            map.put(UserTerminalProperties.COURT_SITE_ID, terminal.getCourtSiteId());
        }

        if (terminal.getCourtRoomId() != null) {
            map.put(UserTerminalProperties.COURT_ROOM_ID, terminal.getCourtRoomId());
        }

        map.put(UserTerminalProperties.TERMINAL_LOCATION, terminal.getLocation());
        map.put(UserTerminalProperties.TERMINAL_ID, terminal.getTerminalId());
        map.put(UserTerminalProperties.TERMINAL_NAME, terminal.getTerminalName());
        map.put(UserTerminalProperties.ROAMING, terminal.getRoaming());

        // No username here...map.put(UserTerminalProperties.USER_NAME, userName);
        return map;
    }

    private static XhbCourt getCourt(XhbTerminal terminal) {
        if (terminal.getCourtId() != null) {
            return terminal.getXhbCourt();
        } else if (terminal.getCourtRoomId() != null) {
            return terminal.getXhbCourtRoom().getXhbCourtSite().getXhbCourt();
        } else if (terminal.getCourtSiteId() != null) {
            return terminal.getXhbCourtSite().getXhbCourt();
        } else {
            return null;
        }
    }
}