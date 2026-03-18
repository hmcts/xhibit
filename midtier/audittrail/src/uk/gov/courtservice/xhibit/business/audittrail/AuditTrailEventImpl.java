package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBasicValue;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminal;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanNotFoundException;
import weblogic.security.Security;
import weblogic.security.spi.WLSUser;

/**
 * <p>
 * Title: AuditTrailEventImp
 * </p>
 * <p>
 * Description: This class is the super class for all Audit Trail Events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public abstract class AuditTrailEventImpl implements AuditTrailEvent{
    private Logger log = Logger.getLogger(AuditTrailEventImpl.class);    
    
    private String evtType;
    private Object evtData;
    private boolean success;
    
    private Date timestamp;
    private String workstationId;
    private String userId;
    private String courtHouseId;
    private Integer caseId;
    private String eventSpecificData;
    
    /**
     * Constructor which accepts a string representing the eventType and the Object data itself
     * 
     * @param evtType
     * @param evtData
     */
    public AuditTrailEventImpl(String evtType,Object evtData){
        this.evtType = evtType;
        this.evtData = evtData;
        
        Calendar cal = Calendar.getInstance();
        timestamp = cal.getTime();        
    }      
    
    public void setEvtType(String evtType) {
        this.evtType = evtType;
    }

    public String getEvtType() {
        return evtType;
    }

    public void setEvtData(Object evtData) {
        this.evtData = evtData;
    }

    public Object getEvtData() {
        return evtData;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }       

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setCourtHouseId(String courtHouseId) {
        this.courtHouseId = courtHouseId;
    }

    public String getCourtHouseId() {
        return courtHouseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getCaseId() {
        return caseId;
    }
    
    protected abstract void setUpEventData();

    public void setEventSpecificData(String eventSpecificData) {
        this.eventSpecificData = eventSpecificData;
    }

    public String getEventSpecificData() {
        return eventSpecificData;
    }
    
    /**
     * This method for Setting the UserID, TerminalName and CourtId based on the current user.
     */    
    public void retrieveLoginData(){
        Subject sub = Security.getCurrentSubject();
        //Should always only be one
        
        if(sub == null){
            log.warn("Subject ID is null");
            return;
        }
        
        String loggedInUser = getLoggedInUser(sub);        
        String loggedInTerminal = getLoggedInTerminal(loggedInUser);       
        String loggedInCourtId = getLoggedInCourtId(loggedInTerminal,loggedInUser);
        
        setUserId(loggedInUser);
        setWorkstationId(loggedInTerminal);
        setCourtHouseId(loggedInCourtId);
    }
    
    /**
     * This method accepts a Subject object and returns the name of the principal, 
     * which corresponds to the current logged in UserID
     * 
     * @param sub - Subject
     * @return String - userID
     */
    private String getLoggedInUser(Subject sub){
        String loggedInUser = null;
        Iterator<WLSUser> it =  sub.getPrincipals(WLSUser.class).iterator();
        if(it.hasNext()){
            loggedInUser = it.next().getName().toLowerCase();
        }
        
        if(loggedInUser == null){
            log.warn("UserId is null");            
        }
        
        return loggedInUser;
    }
    
    /**
     * This method accepts a userID and returns the terminalName by getting
     * the maximum value from the AUD_USER_LOGINS table. Bearing in mind that a user
     * can only be logged into one terminal at any one time
     * 
     * @param String user 
     * @return String terminalName
     */
    private String getLoggedInTerminal(String user){
        String loggedInTerminal = null;
        AudUserLoginsBasicValue[] values = AudUserLoginsBeanHelper2.findMaxByUserIdValue(user);
        if(values.length!=1){
            //Should always return one row
            log.debug("Incorrect number of sessions found for user: "+user+" - "+values.length);
        }else{
            loggedInTerminal = values[0].getTerminalName();
        }
        return loggedInTerminal;
    }
    
    /**
     * This method accepts a terminalName and userId. It then works out the court
     * ID from the XHB_TERMINALS table
     * 
     * @param loggedInTerminal
     * @param loggedInUser
     * @return
     */    
    protected String getLoggedInCourtId(String loggedInTerminal, String loggedInUser){
        if(loggedInTerminal==null){
            log.warn("Terminal ID = null for user: "+loggedInUser);
        }else{
        
            XhbTerminal terminal;
            try {
                terminal = XhbTerminalBeanHelper2.findByTerminalName(loggedInTerminal.toLowerCase());
            } catch (XhbTerminalBeanNotFoundException e) {
                log.warn("Terminal not found: "+loggedInTerminal);
                terminal = null;
            }
            
            if(terminal != null){
                
                return getCourtIdFromTerminal(terminal);
            }
        }
        
        return null;
    }
    
    /**
     * This method accepts an XHBTerminal object and interrogates it 
     * to return the correct CourtId
     * @param terminal
     * @return
     */
    private String getCourtIdFromTerminal(XhbTerminal terminal){
        if(terminal.getCourtSiteId() != null){
            return terminal.getXhbCourtSite().getCrestCourtId();
        }
        if(terminal.getCourtRoomId() != null){
            return terminal.getXhbCourtRoom().getXhbCourtSite().getCrestCourtId();
        }
        if(terminal.getCourtId() != null){
            return terminal.getXhbCourt().getCrestCourtId();
        }
        return null;
    }

}