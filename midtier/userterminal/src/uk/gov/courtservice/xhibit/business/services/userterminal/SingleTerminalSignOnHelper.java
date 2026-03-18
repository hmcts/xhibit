package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBasicValue;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanNotFoundException;

/**
 * <p>
 * Title: SingleTerminalSignOnHelper
 * </p>
 * <p>
 * Description: Helper class for single terminal sign on functionality
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

public class SingleTerminalSignOnHelper {
   private static String LOGGED_IN="Y";
   private static String NOT_LOGGED_IN="N";
   
   private static Logger log = Logger.getLogger(SingleTerminalSignOnHelper.class);
   
   private static String PROP_SESSION_EXPIRY = "sessionExpiryMinutes";
   
   private static String DEFAULT_LOGIN_EXPIRY_PERIOD = "0";
	/**
     * Method to create a new userTerminalSession. Throws a UserLoggedInElsewhereException
     * if the user is already logged into another terminal according to AUD_USER_LOGINS table
     * @param userId
     * @param terminalId
     * @return
     * @throws UserLoggedInElsewhereException
     */
    public static Long createUserSession(String userId, String terminalId) 
        throws UserLoggedInElsewhereException{
        //Remove records < today's date
    	AudUserLoginsBasicValue bv=null;
    	//String otherTerminal=null;
    	
        SingleTerminalSignOnHelper.removeOldRecords(userId,terminalId);
        AudUserLoginsBasicValue[] currentLogons = getAudUserLoginBasicValue(userId);
        if (currentLogons != null)
              bv = isUserLoggedOnElsewhere(currentLogons, terminalId); 
        
        if (bv != null) {
            if (!(bv.getTerminalName().equalsIgnoreCase(terminalId))){ 
                //// Nov 2014 - Do nothing now as requirement to disallow people to logon to more than 1 terminal has been removed
                log.debug("User "+userId+" is logged on to another terminal:"+terminalId);
                //UserLoggedInElsewhereException exceptionToThrow = constructLoggedInElsewhereException(userId,bv);
            	//throw exceptionToThrow;	
            }else
           	   	bv = updateUserLoginsBasicValue(userId,terminalId);
        }
        else
        	bv = constructUserLoginsBasicValue(userId,terminalId);
        //Audit the login
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put(AuditTrailEvent.USER_LOGON, bv);
        AuditTrailService auditService = CSServices.getAuditTrailService();
        AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
        event.setSuccess(true);
        auditService.createAuditRecord(event); 
        
        if(bv==null || bv.getPrimaryKey()==null){
            return null;
        }               
        return bv.getPrimaryKey();
    }
    
    /**
     * Private method to construct AudUserLoginsBasicValue object
     * @param userId
     * @param terminalId
     * @return
     */
    private static AudUserLoginsBasicValue constructUserLoginsBasicValue(String userId, String terminalId){
        AudUserLoginsBasicValue bv = new AudUserLoginsBasicValue();
        bv.setDateLoggedIn(new Date());
        bv.setTerminalName(terminalId);
        bv.setUserId(userId);
        bv.setLoggedIn("Y");
        Calendar cal = Calendar.getInstance();
        bv.setDateLoggedIn(new Date(cal.getTime().getTime()));
        bv.setLastUpdateDate(new Date(cal.getTime().getTime()));
        return AudUserLoginsBeanHelper2.create(bv);
    }
    
    /**
     * Private method to construct AudUserLoginsBasicValue object
     * @param userId
     * @param terminalId
     * @return
     */
    private static AudUserLoginsBasicValue updateUserLoginsBasicValue(String userId, String terminalId){
        AudUserLoginsBasicValue bv=null;
        
        bv = getCurrentUserLoginBasicValue(userId, terminalId);          
        bv.setLoggedIn("Y");
        Calendar cal = Calendar.getInstance();
        bv.setDateLoggedIn(new Date(cal.getTime().getTime()));
        bv.setLastUpdateDate(new Date(cal.getTime().getTime()));
        return AudUserLoginsBeanHelper2.update(bv);
    }
    
    
    /**
     * Check if this user is already logged in on another terminal (Apart from this one)
     * @param userId
     * @param terminalId
     * @return
     */
    public static AudUserLoginsBasicValue isUserLoggedOnElsewhere(AudUserLoginsBasicValue[] userLogons, String terminalId){
        AudUserLoginsBasicValue bv, foundBv = null;      
        
        for (int i=0; i<userLogons.length; i++) {
           bv = userLogons[i];
           if ((bv.getLoggedIn().equalsIgnoreCase(LOGGED_IN)) &&
        	    (!bv.getTerminalName().equalsIgnoreCase(terminalId))){
        	       foundBv = bv;
           }
        }
        return foundBv;
    }
    
    /**
     * Gets the login details of a user on a particular terminal
     * @param userId
     * @param terminalId
     * @return
     */
    public static AudUserLoginsBasicValue[] getAudUserLoginBasicValue(String userId){
        AudUserLoginsBasicValue[] values = AudUserLoginsBeanHelper2.findRSByUserIdValue(userId);
         
        return values;
    }     
    
    /**
     * Gets the login details of a user on a particular terminal
     * @param userId
     * @param terminalId
     * @return
     */
    public static AudUserLoginsBasicValue getCurrentUserLoginBasicValue(String userId,String terminalId){
        AudUserLoginsBasicValue[] values = AudUserLoginsBeanHelper2.findRSByUserIdValue(userId);
        AudUserLoginsBasicValue bv = null;
        AudUserLoginsBasicValue currentLogon=null;
        
        boolean found=false;
        int i=0;
        
        while ((i<values.length) && (!found)) {
           bv = values[i];
           if (bv.getTerminalName().equalsIgnoreCase(terminalId)){
        	       currentLogon=bv;
        	       found=true;
           }
          i++;
        }
        return currentLogon;
    }   
    
    
    /**
     * This method is used to remove a user session when a user logs off
     * 
     * @param userLoginId
     */
    public static void removeUserTerminalSession(Long userLoginId){
    	AudUserLoginsBasicValue value = null;
    	try{
        	value = AudUserLoginsBeanHelper2.findByPrimaryKeyValue(userLoginId);
        }catch(AudUserLoginsBeanNotFoundException ex){
        	log.warn("Failed to find audUserLoginId: "+userLoginId+". "+ex.toString());
        }
        if(value == null){
            //Nothing to remove
            return;
        }
        value.setLoggedIn(NOT_LOGGED_IN);
        AudUserLoginsBeanHelper2.update(value);
        
        
        //Audit the logoff
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put(AuditTrailEvent.USER_LOGOFF, value);
        AuditTrailService auditService = CSServices.getAuditTrailService();
        AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
        event.setSuccess(true);
        auditService.createAuditRecord(event);
    }
    
    /**
     * This method is used to remove all records for this user which are from before
     * todays date. This helps to clear any sessions which weren't removed if a user
     * did not terminate the application correctly.
     * @param userId
     */
    public static void removeOldRecords(String userId, String terminalId){
        
    	Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE),0,0);
        
        AudUserLoginsBasicValue[] values = AudUserLoginsBeanHelper2.findRSByUserIdValue(userId);
        for(int i = 0; i<values.length; i++){
        	AudUserLoginsBasicValue bv = values[i];
            Date loginDate = bv.getDateLoggedIn();
            if(loginDate != null && (loginDate.before(cal2.getTime()) )){
            	//This is an old session from yesterday so delete it
                AudUserLoginsBeanHelper2.remove(bv);
            }else if(sessionHasExpired(bv)){
            	//This is an expired session so mark it as LOGGED_IN = 'N'
            	bv.setLoggedIn(NOT_LOGGED_IN);
            	AudUserLoginsBeanHelper2.update(bv);
            }
        }
    }
    
    private static int getSessionExpiryPeriod(){
    	return Integer.parseInt(System.getProperty(PROP_SESSION_EXPIRY, DEFAULT_LOGIN_EXPIRY_PERIOD));
    }
    
    /**
     * 
     * @param bv
     * @return boolean - whether the session has expired or not
     */
    private static boolean sessionHasExpired(AudUserLoginsBasicValue bv){
    	Date lastUpdateDate = bv.getLastUpdateDate();
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, 0 - getSessionExpiryPeriod());
    	
    	if(lastUpdateDate.before(cal.getTime()))
    		return true;
    	else
    		return false;
    }
    
    private static String getTerminalLocation(String terminalName){
    	XhbTerminalBasicValue terminal = null;
    	try{
    		terminal = XhbTerminalBeanHelper2.findByUniqueTerminalNameValue(terminalName);
    	}catch (XhbTerminalBeanNotFoundException ex){
    		log.warn("Terminal "+terminalName+" not found");
    	}
    	
    	if (terminal != null){
    		return terminal.getLocation();
    	}else{
    		return "";
    	}
    }
    
    private static UserLoggedInElsewhereException constructLoggedInElsewhereException(String userId,AudUserLoginsBasicValue bv){
    	String terminalName = bv.getTerminalName();
    	String terminalLocation = getTerminalLocation(terminalName);
    	
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy H:m:s");
    	String dateLoggedIn = df.format(bv.getDateLoggedIn());
    	String lastUpdateDate = df.format(bv.getLastUpdateDate());
    	
    	return new UserLoggedInElsewhereException(userId,terminalName,terminalLocation,dateLoggedIn,lastUpdateDate);
    	
    }
}

