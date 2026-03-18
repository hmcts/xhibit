package uk.gov.courtservice.xhibit.business.services.version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.version.ComponentValue;
import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;

/**
 * <p>
 * Title: Results2ControllerBean
 * </p>
 * <p>
 * Description: Session Bean providing functionality for accessing and updating
 * case results
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @ejb.bean name="VersionController" description="Version Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="VersionControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @author Rakesh Lakhani
 * @version $Id: VersionControllerBean.java,v 1.9 2013/11/21 15:14:34 atwells Exp $
 */
public class VersionControllerBean implements SessionBean {
    private static final Logger log = CSServices.getLogger(VersionControllerBean.class);

    protected SessionContext ctx;

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbActivate() {
        log.debug("ejbActivate()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbRemove() {
        log.debug("ejbRemove()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbPassivate() {
        log.debug("ejbPassivate()");
    }

    /**
     * Sets the session context.
     * 
     * @param ctx
     *            SessionContext Context for session
     */
    public void setSessionContext(SessionContext ctx) {
        log.debug("setSessionContext(" + ctx + ")");
        this.ctx = ctx;
    }

    public void ejbCreate() throws CreateException {
        log.debug("ejbCreate()");
    }

    /**
     * Get the results reference data for the specified court
     * 
     * @return the results reference data for the specified result type
     * @ejb.interface-method view-type="both"
     */
    public VersionValue[] getVersions() {
        VersionQuery vq = new VersionQuery();
        return vq.getData();
    }

    /**
     * Check the version of a component against what is stored in the midtier.
     * It is upto the calling component to decide what to do if the versions do
     * not match.
     * 
     * @param version
     *            String containing the exact version to be checked
     * @param component
     *            Static that references the component, eg ThickClient,
     *            ThinClient
     * @return boolean true for successful match.
     * @ejb.interface-method view-type="both"
     */
    public boolean checkVersionCompatibility(String version, ComponentValue component) {
        return VersionHelper.checkVersionCompatibility(version, component);
    }
    
    
    /////////////////////////////////////////////////////////
    //// Remote Access Terminal Data - Access methods ///////
    /////////////////////////////////////////////////////////
        
    /**
     * Get all the courts
     * 
     * @return list of all courts.
     * @ejb.interface-method view-type="both"
     */
    public XhbCourtBasicValue[] getAllCourts() {
        String methodName = "getAllCourts";
        log.debug(methodName + " entered ");
        // Get all courts, then remove obsolete values
        LinkedList allCourts = new LinkedList(Arrays.asList(XhbCourtBeanHelper2.findAllValue()));
        LinkedList filteredCourts = new LinkedList(); 
        Iterator iter = allCourts.iterator();
        while (iter.hasNext()) {
            XhbCourtBasicValue court = (XhbCourtBasicValue) iter.next();
            if (court.getObsInd() == null || !court.getObsInd().equalsIgnoreCase("Y")) {
                filteredCourts.add(court);
            }
        }
        return (XhbCourtBasicValue[]) filteredCourts.toArray(new XhbCourtBasicValue[filteredCourts.size()]);
    }
    
    
    /**
     * Retrieve a given terminal value from the new table XHB_TERMINAL_DEFAULT
     * Its possible more than one value (or none) will be returned so this needs to be handled
     * either here, or further up the call stack
     * 
     * @param terminalId of the terminal to be retrieved
     * @ejb.interface-method view-type="both"
     */
    public XhbTerminalDefaultBasicValue[] getTerminalDefaultByName(String terminalId){
        log.debug("Finding terminal ID: "+terminalId);
        GetTerminalDefault gtd = new GetTerminalDefault(terminalId);
        return gtd.getData();
    }
    
    /**
     * Retrieve a given terminal value from the table XHB_TERMINAL
     * Its possible more than one value (or none) will be returned so this needs to be handled
     * either here, or further up the call stack
     * 
     * @param terminalId of the terminal to be retrieved
     * @ejb.interface-method view-type="both"
     */
    public XhbTerminalBasicValue[] getTerminalByName(String terminalId){
        log.debug("Finding terminal ID: "+terminalId);
        GetTerminal gt = new GetTerminal(terminalId);
        return gt.getData();
    }
    
    /**
     * Update a given row from the table XHB_TERMINAL
     * 
     * 
     * @param terminalId of the terminal to be retrieved
     * @ejb.interface-method view-type="both"
     */
    public void updateTerminalByName(String terminalName, String location, Integer courtId, Integer courtRoomId, Integer courtSiteId, String roomOrSite){
        log.debug("updating terminal with terminal ID: "+terminalName);
        //UpdateTerminal ut = new UpdateTerminal(terminalId, location, courtRoomId, courtSiteId, roomOrSite, courtId);
        UpdateTerminal ut = new UpdateTerminal();
        ut.updateTerminal(terminalName, location, courtId, courtRoomId, courtSiteId, roomOrSite);
    }
    
    /**
     * Retrieve a given TerminalId value from the new table XHB_TERMINAL_COURT_DEFAULT
     * 
     * @param terminalId of the terminal to be retrieved
     * @param courtId of the terminal to be retrieved
     * @ejb.interface-method view-type="both"
     */
    public XhbTerminalCourtDefaultBasicValue[] getTerminalCourtDefaultByCourtId(int courtId){
        log.debug("Finding by court ID: "+courtId);
        GetTerminalCourtDefaultbyCourtId gtcdc = new GetTerminalCourtDefaultbyCourtId(courtId);
        return gtcdc.getData();
    }

    /**
     * Retrieve a given TerminalId value from the new table XHB_TERMINAL_COURT_DEFAULT
     * 
     * @param terminalId of the terminal to be retrieved
     * @param courtSiteId of the terminal to be retrieved
     * @ejb.interface-method view-type="both"
     */
    public XhbTerminalCourtDefaultBasicValue[] getTerminalCourtDefaultByCourtSiteId(int terminalId, int courtSiteId){
        log.debug("Finding by terminal ID: "+terminalId+" and court site ID: "+courtSiteId);
        GetTerminalCourtDefaultbyCourtId gtcdcs = new GetTerminalCourtDefaultbyCourtId(courtSiteId);
        return gtcdcs.getData();
    }
    
    
    //public void update

}
