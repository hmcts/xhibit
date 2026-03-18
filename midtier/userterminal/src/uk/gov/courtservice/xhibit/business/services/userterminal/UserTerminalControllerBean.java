package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.security.auth.Subject;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.client.SessionPropertiesMap;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryTerminal;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBasicValue;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.activedirectory.ActiveDirectoryControllerLocal;
import uk.gov.courtservice.xhibit.business.services.activedirectory.ActiveDirectoryControllerLocalHome;

import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;

/**
 * <p>
 * Title: User session controller bean class
 * </p>
 * <p>
 * Description: This class provides the remote services for user session. The
 * class relies on a Weblogic specific utility to resolve group membership. An
 * alternative would be to use JNDI to connect to the AD server
 * </p>
 * 
 * @ejb.bean name="UserTerminalController" description="User Session Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="UserTerminalControllerHome"
 *           local-jndi-name="UserTerminalControllerLocalHome"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * @author Rakesh Lakhani
 * @version $Id: UserTerminalControllerBean.java,v 1.9 2005/01/14 12:53:18
 *          sz0t7n Exp $
 */
public class UserTerminalControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 7719593240067655208L;

    private ActiveDirectoryControllerLocal activeDirectoryController;

    /**
     * Reads the environment entries and looks up the home interface
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        activeDirectoryController = (ActiveDirectoryControllerLocal) CSServices.getEJBServices().createLocalSession(
                ActiveDirectoryControllerLocalHome.class);
    }

    /**
     * This method synchronizes AD terminals with that in the XHIBIT database
     * 
     * @ejb.interface-method view-type="remote"
     * @return String[] A list of terminals that failed validation.
     */
    public String[] synchronizeLocations() {
        // Get the terminal locations in Active directory
        //TerminalLocation[] terminalLocations = getActiveDirectoryTerminalLocations();
        //log.debug("Terminals retrieved: " + terminalLocations.length);

        // This is run under Oracle specific isolation level for WLS that will
        // issue a select for update
        return null;//TerminalHelper.synchronize(terminalLocations);
        // log.debug("Terminal locations synchronized");
    }

    /**
     * This method synchronizes AD terminals with that in the XHIBIT database
     * 
     * @ejb.interface-method view-type="remote"
     */
    public String[] synchronizeLocation(String terminalName) {
        //log.debug("Single Terminal location synchronisation");

        // Get the terminal locations in Active directory
        //TerminalLocation[] terminalLocations = getActiveDirectoryTerminalLocation(terminalName);
        //log.debug("Terminals retrieved: " + terminalLocations.length);

        // This is run under Oracle specific isolation level for WLS that will
        // issue a select for update
        return null;//TerminalHelper.synchronize(terminalLocations);
    }

    /**
     * This method synchronizes AD terminals with that in the XHIBIT database
     * 
     * @ejb.interface-method view-type="remote"
     */
    public String[] synchronizeByCourt(String courtShortName) {
        //log.debug("Court Terminal location synchronisation");

        // Get the terminal locations in Active directory
        //TerminalLocation[] terminalLocations = getActiveDirectoryTerminalLocationByCourtShortName(courtShortName);
        //log.debug("Terminals retrieved: " + terminalLocations.length);

        // This is run under Oracle specific isolation level for WLS that will
        // issue a select for update
        return null;//TerminalHelper.synchronize(terminalLocations);
    }


    // Convert into local form
    private static TerminalLocation[] convert(ActiveDirectoryTerminal[] terminals) {
        if (terminals == null) {
            return null;
        }
        TerminalLocation[] locations = new TerminalLocation[terminals.length];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new TerminalLocation(terminals[i].getPrincipalName(), terminals[i].getLocation());
        }
        return locations;
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public XhbTerminalBasicValue[] getXHIBITTerminalLocations() {
        TerminalQuery query = new TerminalQuery();
        return query.getData();
    }
    /**
     * @ejb.interface-method view-type="remote"
     * @param courtId
     *            the id of the court.
     * @return XhbTerminalBasicValue

     */
    public XhbTerminalBasicValue[] getTerminalsBySite(String courtSiteId) {
        TerminalQuery query = new TerminalQuery(courtSiteId);
        return query.getData();
    }
    
    /**
     * @ejb.interface-method view-type="both"
     * @param terminalId
     *            the id of the terminal.

     */
    public void doDeleteTerminal(String terminalId) {
        DeleteTerminal del = new DeleteTerminal(terminalId);
        del.ProcessSQL();
    }
    
    /**
     * @ejb.interface-method view-type="both"
     * @param terminalId
     *            the id of the terminal.

     */
    public void doDeleteTerminalDefault(String terminalId) {
        DeleteTerminalDefault del = new DeleteTerminalDefault(terminalId);
        del.ProcessSQL();
    }


    /**
     * @ejb.interface-method view-type="both"
     * 
     */
    public SessionPropertiesMap getXHIBITTerminalLocation(String userName, String terminalName) throws TerminalNotFoundException {
        return TerminalHelper.getXHIBITTerminalLocation(userName, getUser(userName), terminalName);
    }
    
    /**
     * @ejb.interface-method view-type="both"
     * 
     */
    public SessionPropertiesMap getXHIBITTerminalLocation(String terminalName) throws TerminalNotFoundException {
        return TerminalHelper.getXHIBITTerminalLocation(terminalName);
    }

    /**
     * Get the functionalities of the current subject
     * 
     * @ejb.interface-method view-type="remote"
     * 
     */
    public String[] getFunctionalities(Subject subject) {
        return UserHelper.getFunctionalities(subject);
    }

    /**
     * @ejb.interface-method view-type="both"
     * 
     */
    public String getUser(String userName) {
        // Get the display name for the active directory user
        //return "Test";
        //return activeDirectoryController.getUser(ctx.getCallerPrincipal().getName()).getName();
        return activeDirectoryController.getUser(userName).getDisplayName();
    }

    /**
     * Checks whether the current subject is in the group
     * 
     * @param group
     * @return true if the current user is in the specified group
     * 
     * @ejb.interface-method view-type="remote"
     * 
     */
    public boolean isUserInGroup(String group, Subject subject) {
        //return true;
        return UserHelper.isUserInGroup(group, subject);
    }

    /**
     * Checks whether the current subjects principals is in the group
     * 
     * @param group
     * @return true if the current user is in the specified group
     * @ejb.interface-method view-type="remote"
     */
    public String getSubjectDetails(Subject subject) {
        return UserHelper.getSubjectDetails(subject);
    }

    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void doTask(String taskName) {
    	log.debug("doTask(): "+taskName);
        log.info("doTask() about to synchronizeLocations - BEGIN");
        synchronizeLocations();
        log.info("doTask() done synchronizeLocations - END");
    }

    /**
     * Gets the Court structure for the given court id. i.e. it gets the court
     * sites and their respective court rooms.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtId
     *            the id of the court.
     * @return CourtStructureValue
     */
    public CourtStructureValue getCourtStructure(Integer courtId) {
        return XhbCourtBeanHelper2.getCourtStructure(courtId);
    }

    /**
     * Gets the Court structure for the given court id. i.e. it gets the court
     * sites and their respective court rooms.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtId
     *            the id of the court.
     * @return CourtStructureValue
     */
    public XhbCourtBasicValue[] getAllCourts() {
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
     * Creates a new entry in the AUD_USER_LOGINS table
     * 
     * @param terminal ID to be added
     * @ejb.interface-method view-type="remote"
     * 
     */
    public Long createNewUserTerminalSession(String userName, String terminalId) throws UserLoggedInElsewhereException{
        //check for pilot court
    	XhbTerminalBasicValue[] xhbTerminalBasicValues= PilotCourtCheckHelper.getTerminalByName(terminalId);
    	Integer courtId= xhbTerminalBasicValues[0].getCourtId();
    	PilotCourtCheckHelper.checkForValidPilotCourt(courtId);
        return SingleTerminalSignOnHelper.createUserSession(userName, terminalId);
    }
    
    /**
     * Removes given userLoginId from AUD_USER_LOGINS
     * 
     * @param terminal ID to be added
     * @ejb.interface-method view-type="remote"
v
     */
    public void removeUserTerminalSession(Long userLoginId){
        SingleTerminalSignOnHelper.removeUserTerminalSession(userLoginId);        
    }
    
    /**
     * Logs an unsuccessful login attempt
     * 
     * @param terminal ID to be added
     * @ejb.interface-method view-type="remote"
     * 
     */
    public void logUnsuccessfulLoginAttempt(String terminalId,Exception e, String userDisplayName){
        //String userId = ctx.getCallerPrincipal().getName().toLowerCase();
        AudUserLoginsBasicValue bv = new AudUserLoginsBasicValue(null,userDisplayName,null,terminalId,null,null);
        
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put(AudUserLoginsBasicValue.class.getName(), bv);
        hashtable.put(Exception.class.getName(),e);
        AuditTrailService auditService = CSServices.getAuditTrailService();
        AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
        event.setSuccess(false);
        auditService.createAuditRecord(event);
    }
    
    /**
     * Update the AUD_USER_LOGIN.LAST_UPDATE_DATE field with the current date
     * 
     * @param userLoginId to be updated
     * @ejb.interface-method view-type="both"
     * 
     */
    public void updateUserLoginSession(long userLoginId){
    	try{
    		AudUserLoginsBasicValue bv = AudUserLoginsBeanHelper2.findByPrimaryKeyValue(userLoginId);
    	
    		Calendar cal = Calendar.getInstance();
    		bv.setLastUpdateDate(new Date(cal.getTime().getTime()));
    	
    		AudUserLoginsBeanHelper2.update(bv);
    	}catch(AudUserLoginsBeanNotFoundException ex){
    		log.warn("userLoginID: "+userLoginId+" not found to update");
    	}
    }
    
    /**
     * Retrieve a given TerminalId value
     * 
     * @param terminalId to be retrieved
     * @ejb.interface-method view-type="both"
     * 
     */
    public XhbTerminalBasicValue getTerminalById(int terminalId){
    	log.debug("Finding terminal ID: "+terminalId);
    	return XhbTerminalBeanHelper2.findByPrimaryKeyValue(terminalId);
    }
    
    /**
     * Update a given Terminal
     * 
     * @param terminal to be updated
     * @ejb.interface-method view-type="both"
     */
    public void updateTerminal(XhbTerminalBasicValue terminal){
    	log.debug("Updating Terminal ID: "+terminal.getTerminalId());
    	XhbTerminalBeanHelper2.updateLocal(terminal);
    }
    
    /**
     * Get a list of Courts 
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @e
     */
    public XhbCourtBasicValue[] getCourtSelectionList(){
    	return XhbCourtBeanHelper2.findAllValue();    	
    }
    
    /**
     * Get a list of Court Sites for a given COurt
     * 
     * @ejb.interface-method view-type="both"
     * 
     */
    public XhbCourtSiteBasicValue[] getCourtSiteSelectionList(int courtId){
    	return XhbCourtSiteBeanHelper2.findByCourtIdValue(courtId);    	
    }
    
    /**
     * Get a list of Court Rooms for a given Court Site
     * 
     * @ejb.interface-method view-type="both"
     * 
     */
    public XhbCourtRoomBasicValue[] getCourtRoomSelectionList(int courtSiteId){
    	return XhbCourtRoomBeanHelper2.findByCourtSiteIdValue(courtSiteId);    	
    }
    
    /**
     * Get a Court 
     * 
     * @ejb.interface-method view-type="both"
     * 
     */
    public XhbCourtBasicValue getCourtBV(int courtId){
    	return XhbCourtBeanHelper2.findByPrimaryKeyValue(courtId);    	
    }
    
    /**
     * Get a Court Site
     * 
     * @ejb.interface-method view-type="both"
     */
    public XhbCourtSiteBasicValue getCourtSiteBV(int courtSiteId){
    	return XhbCourtSiteBeanHelper2.findByPrimaryKeyValue(courtSiteId);    	
    }
    
    /**
     * Get a Court Room
     * 
     * @ejb.interface-method view-type="both"
     * 
     */
    public XhbCourtRoomBasicValue getCourtRoomBV(int courtRoomId){
    	return XhbCourtRoomBeanHelper2.findByPrimaryKeyValue(courtRoomId);    	
    }
    
    /**
     * Get the terminal id from xhb_terminal_default. Often this is different from the terminalid in xhb_terminal 
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param terminalName
     * @param courtSiteId
     * @return
     */
    public int getTerminalDefaultId(String terminalName) {
        int terminalId = 0;
        XhbTerminalDefaultBasicValue terminalDefault = XhbTerminalDefaultBeanHelper2.findByTerminalNameValue(terminalName);
        terminalId = terminalDefault.getTerminalId();
        return terminalId;
    }
    
    
    /**
     * Update a terminal and terminalDefault using given values
     * 
     * @ejb.interface-method view-type="both"
     * 
     */
    public void updateTerminal(int terminalId, int courtId, int courtSiteId, int courtRoomId, String courtSiteName){
    	XhbTerminalBasicValue terminal = XhbTerminalBeanHelper2.findByPrimaryKeyValue(terminalId);
    	terminal.setCourtId(courtId);
    	terminal.setCourtSiteId(courtSiteId);
    	
    	if(courtRoomId > 0){
    		terminal.setCourtRoomId(courtRoomId);
    		terminal.setCourtroomOrSite("R");
    	}else{
    		terminal.setCourtroomOrSite("S");
    		terminal.setCourtRoomId(null);
    	}    	    	          
    	
    	terminal.setLocation(getLocationString(courtId, courtSiteId, courtRoomId, courtSiteName));
    	
    	if(log.isDebugEnabled()){
    		log.debug("Updating Terminal: "+courtId+". courtId= "+courtId+", courtSite= "+courtSiteId+", courtRoom= "+courtRoomId+", locationString= "+terminal.getLocation());
    	}
    	
    	XhbTerminalBeanHelper2.update(terminal);
        
        // Now update the terminal default
        XhbTerminalDefaultBasicValue terminalDefault = XhbTerminalDefaultBeanHelper2.findByPrimaryKeyValue(terminalId);
        terminalDefault.setCourtId(courtId);
        terminalDefault.setCourtSiteId(courtSiteId);
        
        if(courtRoomId > 0){
            terminalDefault.setCourtRoomId(courtRoomId);
            terminalDefault.setCourtroomOrSite("R");
        }else{
            terminalDefault.setCourtroomOrSite("S");
            terminalDefault.setCourtRoomId(null);
        }                         
        
        terminalDefault.setLocation(getLocationString(courtId, courtSiteId, courtRoomId, courtSiteName));
        
        if(log.isDebugEnabled()){
            log.debug("Updating TerminalDefault: "+courtId+". courtId= "+courtId+", courtSite= "+courtSiteId+", courtRoom= "+courtRoomId+", locationString= "+terminalDefault.getLocation());
        }
        XhbTerminalDefaultBeanHelper2.update(terminalDefault);
    }
 
    /**
     * Create a new terminal using given values
     * 
     * @ejb.interface-method view-type="both"
     * 
     */
    
    public int createNewTerminal(String terminalName, int courtId, int courtSiteId, int courtRoomId, String courtSiteName){
    	XhbTerminalBasicValue terminal = new XhbTerminalBasicValue();
    	terminal.setCourtId(courtId);
    	terminal.setCourtSiteId(courtSiteId);
    	
    	if(courtRoomId > 0){
    		terminal.setCourtRoomId(courtRoomId);
    		terminal.setCourtroomOrSite("R");
    	}else{
    		terminal.setCourtroomOrSite("S");
    		terminal.setCourtRoomId(null);
    	}
    	    	
    	terminal.setTerminalName(terminalName);
    	
    	terminal.setLocation(getLocationString(courtId, courtSiteId, courtRoomId, courtSiteName));
    	
    	if(log.isDebugEnabled()){
    		log.debug("Creating Terminal: "+courtId+". courtId= "+courtId+", courtSite= "+courtSiteId+", courtRoom= "+courtRoomId+", locationString= "+terminal.getLocation());
    	}
    	    	
    	terminal.setTerminalIp("N/A");
    	terminal.setRoaming("N");
    	
    	terminal = XhbTerminalBeanHelper2.create(terminal);
        
        // Now update the terminal default
        XhbTerminalDefaultBasicValue terminalDefault = new XhbTerminalDefaultBasicValue();
        terminalDefault.setCourtId(courtId);
        terminalDefault.setCourtSiteId(courtSiteId);
        terminalDefault.setTerminalIp("N/A");
        
        if(courtRoomId > 0){
            terminalDefault.setCourtRoomId(courtRoomId);
            terminalDefault.setCourtroomOrSite("R");
        }else{
            terminalDefault.setCourtroomOrSite("S");
            terminalDefault.setCourtRoomId(null);
        }
        
        terminalDefault.setTerminalName(terminalName);
        
        terminalDefault.setLocation(getLocationString(courtId, courtSiteId, courtRoomId, courtSiteName));
        
        if(log.isDebugEnabled()){
            log.debug("Creating TerminalDefault: "+courtId+". courtId= "+courtId+", courtSite= "+courtSiteId+", courtRoom= "+courtRoomId+", locationString= "+terminalDefault.getLocation());
        }
        XhbTerminalDefaultBeanHelper2.create(terminalDefault);
    	
    	
    	return terminal.getTerminalId();
    }
    
    private String getLocationString(int courtId, int courtSiteId, int courtRoomId, String courtSiteName){
    	//Set Location String
    	XhbCourtBasicValue court = XhbCourtBeanHelper2.findByPrimaryKeyValue(courtId);
    	XhbCourtSiteBasicValue courtSite = XhbCourtSiteBeanHelper2.findByPrimaryKeyValue(courtSiteId);
    	
    	String locationString = "/"+court.getDisplayName().replace(' ', '_').toLowerCase()+"/"+
		courtSite.getDisplayName().replace(' ', '_').toLowerCase();
    	
    	
    	String suffix = "/";
    	if(courtRoomId > 0){
    		XhbCourtRoomBasicValue courtRoom = XhbCourtRoomBeanHelper2.findByPrimaryKeyValue(courtRoomId);
    		suffix += courtRoom.getDisplayName().replace(' ', '_').toLowerCase();
    	}else{
    		if(courtSiteName != null){
    			suffix += courtSiteName.replace(' ', '_').toLowerCase();
    		}
    	}    	    
    	
    	return locationString += suffix;
    }
    
    //////////////////////////////////////
    //// Case Resync Admin methods ///////
    //////////////////////////////////////
    /**
     * Exposed method to other services to resync a case.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * 
     * @param courtId -
     *            the court id
     * @param caseType -
     *            the case type
     * @param caseNumber -
     *             the case number
     * @return - HashMap<String key, String value>
     *          - Will contain a return message
     *  
     */
    public HashMap<String, String> doResync(String courtId, String caseType, String caseNumber) {
        String methodName = "doResync( " + courtId + ", " + caseType + ", " + caseNumber + " )";
        log.debug(methodName + " entered ");
        
        HashMap<String, String> retMap = new HashMap<String, String>();
        
        try {
            // values cannot be null
            if (courtId == null || caseType == null || caseNumber == null) {
                log.error(" Unexpected null arguments, courtId and casetypeAndNumber cannot null ");
                throw new IllegalArgumentException("Unexpected null arguments, courtid, casetype and casenumber cannot be null");
            }
            // Get the case object for the data entered
            XhbCaseBasicValue cbv = new XhbCaseBasicValue();
            cbv = getCaseByCaseTypeAndNumber(new Integer(courtId), caseType, new Integer(caseNumber));
            if ((cbv != null) && (cbv.getCaseId() != null)) {
                resyncCase(cbv);
                retMap.put("returnMessage", "Success");
            } else {
                retMap.put("returnMessage", "The case (" + caseType + caseNumber + ") does not exist for Case Type, Case Number and Court specified.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnMessage", "Error: please check logs for details.");
        }
        
        log.debug(methodName + " exited ");
        return retMap;
    }
    
    
    /**
     * Utility method to get the case basic value object. Requires
     * case type, case number and court id.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     *
     * @param courtId -
     *            the court id
     * @param caseTypeAndNumber -
     *            the case type and number
     * @return - a CaseBasicValue object
     */
    public XhbCaseBasicValue getCaseByCaseTypeAndNumber(Integer courtId, String caseType, Integer caseNumber) throws CaseResyncControllerException {
        String methodName = "getCaseByCaseTypeAndNumber( " + courtId + ", " + caseType + ", " + caseNumber + " )";
        log.debug(methodName + " entered ");

        XhbCaseBasicValue caseBasicVO = null;

        try {
            log.debug(" Finding case using caseType (" + caseType + ") and caseNumber(" + caseNumber + ") and courtId (" + courtId + ") ");
            // find case using the case type and number

            caseBasicVO = XhbCaseBeanHelper.findByNumberTypeAndCourtValue(caseNumber, caseType, courtId);

        } catch (NumberFormatException nfex) {
            CSServices.getDefaultErrorHandler().handleError(nfex, this.getClass());
            throw new CaseResyncControllerException(
                    "case_resync.case_type_and_number_invalid", "The caseTypeAndNumber(" + caseType + ", " + caseNumber + ") were invalid.", nfex);
        } catch (ObjectNotFoundException e) {
            // This is not an error, the case does not exist so just return an empty object and let this be dealt with by the calling method
            log.error("No case found for caseType (" + caseType + ") and caseNumber(" + caseNumber + ") and courtId (" + courtId + ") ");
        }

        log.debug(methodName + " exited. ");
        return caseBasicVO;
    }
    
    
    /**
     * We know the case will be not null and valid if this method gets called.
     * Do an antual resync of a case, i.e. set charge_import_indicator to RF, commit
     * and then set to S and commit.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * 
     * @param cbv -
     *            the case object (not null)
     * @return - void
     */
    public void resyncCase(XhbCaseBasicValue cbv) {
        String methodName = "doResync( " + cbv.toString() + ")";
        log.debug(methodName + " entered ");
        
        if (cbv.getChargeImportIndicator().equals("LF")) {
            // Set the charge_import_indicator field to RF to ensure that next time case resyncs
            // it does a full refresh
            cbv.setChargeImportIndicator("R");
            
            if(log.isDebugEnabled()){
                log.debug("Updating Case to R: "+cbv.getCaseId());
            }
            
            XhbCaseBeanHelper2.update(cbv);

        } else {
            
            // Set the charge_import_indicator field to RF to ensure that next time case resyncs
            // it does a full refresh
            cbv.setChargeImportIndicator("RF");
            
            if(log.isDebugEnabled()){
                log.debug("Updating Case to RF: "+cbv.getCaseId());
            }
            
            XhbCaseBeanHelper2.update(cbv);
    
            // Set the charge_import_indicator field to S to force a resync
            cbv.setChargeImportIndicator("S");
            
            if(log.isDebugEnabled()){
                log.debug("Updating Case to S: "+cbv.getCaseId());
            }
        }
        
        XhbCaseBeanHelper2.update(cbv);
        
        log.debug(methodName + " exited. ");
    }
    
    
    /**
     * Get the synchronisation status of a case.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * 
     * @param courtId -
     *            the court id
     * @param caseType -
     *            the case type
     * @param caseNumber -
     *             the case number
     * @return - HashMap<String key, String value>
     *          - Will contain either the case status and/or a return message
     *  
     */
    public HashMap<String, String> getCaseStatus(String courtId, String caseType, String caseNumber) {
        String methodName = "getCaseStatus( " + courtId + ", " + caseType + ", " + caseNumber + " )";
        log.debug(methodName + " entered ");
        
        HashMap<String, String> retMap = new HashMap<String, String>();
        
        try {
            // values cannot be null
            if (courtId == null || caseType == null || caseNumber == null) {
                log.error(" Unexpected null arguments, courtId and casetypeAndNumber cannot null ");
                throw new IllegalArgumentException("Unexpected null arguments, courtid, casetype and casenumber cannot be null");
            }
            // Get the case object for the data entered
            XhbCaseBasicValue cbv = new XhbCaseBasicValue();
            cbv = getCaseByCaseTypeAndNumber(new Integer(courtId), caseType, new Integer(caseNumber));
            if ((cbv != null) && (cbv.getCaseId() != null)) {
                String caseStatus = cbv.getChargeImportIndicator();
                retMap.put("caseStatus",  caseStatus);
            } else {
                retMap.put("returnMessage", "The case (" + caseType + caseNumber + ") does not exist for Case Type, Case Number and Court specified.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnMessage", "Error: please check logs for details.");
        }
        
        log.debug(methodName + " exited ");
        return retMap;
    }
    
    
    
    /////////////////////////////////////////////
    //// Unauthorised Case Status methods ///////
    /////////////////////////////////////////////
    /**
     * Get the synchronisation status of a case.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * 
     * @param courtId -
     *            the court id
     * @param caseType -
     *            the case type
     * @param caseNumber -
     *             the case number
     * @return - HashMap<String key, String value>
     *          - Will contain either the list of defendants and/or a return message
     *  
     */
    public HashMap<String, Object> findDefendants(String courtId, String caseType, String caseNumber) {
        String methodName = "findDefendants( " + courtId + ", " + caseType + ", " + caseNumber + " )";
        log.debug(methodName + " entered ");
        
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        
        try {
            // values cannot be null
            if (courtId == null || caseType == null || caseNumber == null) {
                log.error("Unexpected null arguments, courtId and casetypeAndNumber cannot null.");
                throw new IllegalArgumentException("Unexpected null arguments, courtid, casetype and casenumber cannot be null");
            }
            // Get the case object for the data entered
            XhbCaseBasicValue cbv = new XhbCaseBasicValue();
            cbv = getCaseByCaseTypeAndNumber(new Integer(courtId), caseType, new Integer(caseNumber));
            if ((cbv != null) && (cbv.getCaseId() != null)) {
                XhbDefendantOnCaseBasicValue[] xdocbv = XhbDefendantOnCaseBeanHelper2.findByCaseIdValue(cbv.getCaseId());
                
                // Now for each defendant on case we need to get the defendant name and pass this back
                ArrayList<HashMap> defendants = new ArrayList();
                if (xdocbv != null) {
                    String defendantName = "";
                    for (int i=0; i<xdocbv.length; i++) {
                        XhbDefendantBasicValue xdbv = XhbDefendantBeanHelper2.findByPrimaryKeyValue(xdocbv[i].getDefendantId());
                        String firstName = "";
                        if (xdbv.getFirstName() != null) {
                            firstName = xdbv.getFirstName();
                        }
                        String middleName = "";
                        if (xdbv.getMiddleName() != null) {
                            firstName = xdbv.getMiddleName();
                        }
                        String surname = "";
                        if (xdbv.getSurname() != null) {
                            firstName = xdbv.getSurname();
                        }
                        defendantName = firstName + " " + middleName + " " + surname;
                        
                        // Now add the defendantId and defendantName to the list
                        HashMap<String, String> thisDefendant = new HashMap<String, String>();
                        thisDefendant.put("defendantName", defendantName);
                        thisDefendant.put("defendantId", xdocbv[i].getDefendantId().toString());
                        defendants.add(thisDefendant);
                    }
                }
                retMap.put("defendants", defendants);
            } else {
                retMap.put("returnMessage", "The case (" + caseType + caseNumber + ") does not exist for Case Type, Case Number and Court specified.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnMessage", "Error: please check logs for details.");
        }
        
        log.debug(methodName + " exited ");
        return retMap;
    }

    /**
     * Set the defendant on case as authorised.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * 
     * @param courtId -
     *            the court id
     * @param caseType -
     *            the case type
     * @param caseNumber -
     *             the case number
     * @param defendantId -
     *             the defendant id
     * @return - HashMap<String key, String value>
     *          - Will contain a return message
     *  
     */
    public HashMap<String, String> setAsAuthorised(String courtId, String caseType, String caseNumber, String defendantIdString) {
        String methodName = "setAsAuthorised( " + courtId + ", " + caseType + ", " + caseNumber + " , " + defendantIdString + " )";
        log.debug(methodName + " entered ");
        
        HashMap<String, String> retMap = new HashMap<String, String>();
        
        try {
            // values cannot be null
            if (courtId == null || caseType == null || caseNumber == null) {
                log.error(" Unexpected null arguments, courtId and casetypeAndNumber cannot null ");
                throw new IllegalArgumentException("Unexpected null arguments, courtid, casetype and casenumber cannot be null");
            }
            // Get the case object for the data entered
            XhbCaseBasicValue cbv = new XhbCaseBasicValue();
            cbv = getCaseByCaseTypeAndNumber(new Integer(courtId), caseType, new Integer(caseNumber));
            if ((cbv != null) && (cbv.getCaseId() != null)) {
                XhbDefendantOnCaseBasicValue xdocbv = new XhbDefendantOnCaseBasicValue();
                // Set the case/defendant combo as authorised
                xdocbv = getDefendantOnCaseByCaseAndDefendant(cbv.getCaseId(), new Integer(defendantIdString));
                
                if ((xdocbv != null) && (xdocbv.getDefendantOnCaseId() != null)) {
                    // Set the results_verified field to E to ensure that it doesnt appear on the report
                    xdocbv.setResultsVerified("E");
                    
                    if(log.isDebugEnabled()){
                        log.debug("Updating Results Verified to : "+xdocbv.getResultsVerified());
                    }
                    
                    XhbDefendantOnCaseBeanHelper2.update(xdocbv);
                    retMap.put("returnMessage", "Success");
                } else {
                    retMap.put("returnMessage", "The defendant on case does not exist for Case Type, Case Number (" + caseType + caseNumber + ") , Defendant and Court specified.");
                }
            } else {
                retMap.put("returnMessage", "The case (" + caseType + caseNumber + ") does not exist for Case Type, Case Number and Court specified.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnMessage", "Error: please check logs for details.");
        }
        
        log.debug(methodName + " exited ");
        return retMap;
    }
    
    /**
     * Get the synchronisation status of a case.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * 
     * @param courtId -
     *            the court id
     * @param caseType -
     *            the case type
     * @param caseNumber -
     *             the case number
     * @return - HashMap<String key, String value>
     *          - Will contain either the authStatus and/or a return message
     *  
     */
    public HashMap<String, String> checkAuthStatus(String courtId, String caseType, String caseNumber, String defendantId) {
        String methodName = "checkAuthStatus( " + courtId + ", " + caseType + ", " + caseNumber + ", " + defendantId + " )";
        log.debug(methodName + " entered ");
        
        String returnMessage = "";
        String authStatus = "";
        HashMap<String, String> retMap = new HashMap<String, String>();
        
        try {
            // values cannot be null
            if (courtId == null || caseType == null || caseNumber == null || defendantId == null) {
                log.error(" Unexpected null arguments, courtId and casetypeAndNumber or defendantId cannot null ");
                throw new IllegalArgumentException("Unexpected null arguments, courtid, casetype, casenumber and defendantId cannot be null");
            }
            // Get the case object for the data entered
            XhbCaseBasicValue cbv = new XhbCaseBasicValue();
            cbv = getCaseByCaseTypeAndNumber(new Integer(courtId), caseType, new Integer(caseNumber));
            if ((cbv != null) && (cbv.getCaseId() != null)) {
                XhbDefendantOnCaseBasicValue xdocbv = new XhbDefendantOnCaseBasicValue();
                // Get all defendants for this case
                xdocbv = XhbDefendantOnCaseBeanHelper.findByDefendantAndCaseValue(new Integer(defendantId), new Integer(cbv.getCaseId()));

                if (xdocbv.getResultsVerified() != null) {
                    // We dont want null so return empty string if null
                    authStatus = xdocbv.getResultsVerified();
                }
                retMap.put("authStatus", authStatus);
            } else {
                retMap.put("returnMessage", "The case (" + caseType + caseNumber + ") does not exist for Case Type, Case Number and Court specified.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnMessage", "Error: please check logs for details.");
        }
        
        log.debug(methodName + " exited ");
        return retMap;
    }
    
    /**
     * Utility method to get the case basic value object. Requires
     * case type, case number and court id.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     *
     * @param courtId -
     *            the court id
     * @param caseTypeAndNumber -
     *            the case type and number
     * @return - a CaseBasicValue object
     */
    public XhbDefendantOnCaseBasicValue getDefendantOnCaseByCaseAndDefendant(Integer caseId, Integer defendantId) throws CaseResyncControllerException {
        String methodName = "getDefendantOnCaseByCaseAndDefendant( " + caseId + ", " + defendantId + " )";
        log.debug(methodName + " entered ");

        XhbDefendantOnCaseBasicValue defendantOnCaseBasicVO = null;

        try {
            log.debug(" Finding defendant on case using caseId (" + caseId + ") and defendantId (" + defendantId +")");
            // find case using the case type and number

            defendantOnCaseBasicVO = XhbDefendantOnCaseBeanHelper.findByDefendantAndCaseValue(defendantId, caseId);

        } catch (NumberFormatException nfex) {
            CSServices.getDefaultErrorHandler().handleError(nfex, this.getClass());
            throw new CaseResyncControllerException(
                    "unauth_case.case_id_or_defendant_id_invalid", "The caseId or defendantId(" + caseId + ", " + defendantId + ") were invalid.", nfex);
        } catch (ObjectNotFoundException e) {
            // This is not an error, the case does not exist so just return an empty object and let this be dealt with by the calling method
            log.error("No defendant on case found for caseId (" + caseId + ") and defendantId(" + defendantId + ") ");
        }

        log.debug(methodName + " exited. ");
        return defendantOnCaseBasicVO;
    }
    
}