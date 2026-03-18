package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBasicValue;

/**
 * <p>
 * Title: AuditTrailLoginEvent
 * </p>
 * <p>
 * Description: This is the AuditTrailEvent which is used for a "Login" event
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

public class AuditTrailLoginEvent extends AuditTrailEventImpl {
    private AudUserLoginsBasicValue value;
    private Exception exception = null;
    
    /**
     * This constructor should be passed a AudUserLoginsBasicValue which will hold
     * all the data needed for this event
     * 
     * @param evtData
     */
    public AuditTrailLoginEvent(Object evtData){
        super(AuditTrailEvent.USER_LOGON,evtData);
        
        if(!(evtData instanceof AudUserLoginsBasicValue)){
            throw new IllegalArgumentException();
        }
        
        value = (AudUserLoginsBasicValue)evtData;
        
        setUpEventData();
    }
    
    public AuditTrailLoginEvent(Object userLoginBasicValue, Object exception){
        super(AuditTrailEvent.USER_LOGON,userLoginBasicValue);
        
        if(!(userLoginBasicValue instanceof AudUserLoginsBasicValue)){
            throw new IllegalArgumentException();
        }
        
        if(!(exception instanceof Exception)){
            throw new IllegalArgumentException();
        }
        
        value = (AudUserLoginsBasicValue) userLoginBasicValue;
        this.exception = (Exception)exception;
        
        setUpEventData();
    }
    
    /**
     * All values which are needed for this event are contained in the
     * BasicValue object which was passed in to the constructor
     */
    public void setUpEventData(){
        setWorkstationId(value.getTerminalName());
        setUserId(value.getUserId());
        setCourtHouseId(getLoggedInCourtId(value.getTerminalName(), value.getUserId()));
        setCaseId(null);
        if(exception == null){
            setEventSpecificData(EventDataHelper.getString(EventDataHelper.SESSION_ID, value.getPrimaryKey().toString(), true));
        }else{
            setEventSpecificData(EventDataHelper.getString(EventDataHelper.ERROR, exception.getMessage(), true));
        }
    }
}
