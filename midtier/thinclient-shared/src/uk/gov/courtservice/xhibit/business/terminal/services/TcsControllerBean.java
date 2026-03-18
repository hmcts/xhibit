package uk.gov.courtservice.xhibit.business.terminal.services;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSiteSummary;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSummary;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.TerminalSummary;
import uk.gov.courtservice.xhibit.business.terminal.query.TerminalQueries;

/**
 * <p>
 * Title: The PS Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides all business
 * services for the thin client.
 * </p>
 * 
 * @ejb.bean name="TcsController" description="TCS Controller Bean"
 *           type="Stateless" view-type="remote" jndi-name="TcsControllerHome"
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment 2003
 * @version $Revision: 1.11 $
 */
public class TcsControllerBean extends CSSessionBean implements SessionBean {
    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @ejb.create-method
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Get all courts.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CourtSummary[] getAllCourts() {
        log.debug("getAllCourts()");
        return TerminalQueries.getCourts();
    }

    /**
     * Get all courts.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CourtSiteSummary[] getAllCourtSites() {
        log.debug("getAllCourts()");
        return TerminalQueries.getCourtSites();
    }

    /**
     * Get a terminal for a court.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public TerminalSummary[] getTerminalsForCourt(Integer courtId) {
        log.debug("getTerminalsForCourt() - courtId = " + courtId);
        return TerminalQueries.getTerminalsForCourt(courtId);
    }

    /**
     * Get a terminal for an id.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public TerminalSummary getTerminalById(Integer terminalId) {
        log.debug("getTerminalById() - terminalId = " + terminalId);
        return TerminalQueries.getTerminalByPrimaryKey(terminalId);
    }
}
