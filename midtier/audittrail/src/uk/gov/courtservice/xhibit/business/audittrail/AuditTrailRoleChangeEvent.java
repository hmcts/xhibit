package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;

/**
 * <p>
 * Title: AuditTrailRoleChangeEvent
 * </p>
 * <p>
 * Description: This is the AuditTrailEvent which is used when a Role is added
 * or removed to a group
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
public class AuditTrailRoleChangeEvent extends AuditTrailEventImpl {
    private static final String ENABLED_NO = "N";
    
    XhbSecurityGroupRoleBasicValue value;
    
    /**
     * This constructor should be passed a XhbSecurityGroupRoleBasicValue
     *  which will hold all the data needed for this event
     * 
     * @param evtData
     */
    public AuditTrailRoleChangeEvent(Object evtData){
        super(AuditTrailEvent.ROLE_CHANGE,evtData);
        
        if(!(evtData instanceof XhbSecurityGroupRoleBasicValue)){
            throw new IllegalArgumentException();
        }
        
        value = (XhbSecurityGroupRoleBasicValue) evtData;
        
        setUpEventData();
    }
    
    /**
     * This method uses the object which was passed to the constructor to
     * obtain the values needed for the event
     * 
     */
    public void setUpEventData(){
        retrieveLoginData();
        
        StringBuffer s = new StringBuffer();
        
        if(value.getIsEnabled() == null || value.getIsEnabled().equals(ENABLED_NO)){
            s.append(EventDataHelper.getValue(EventDataHelper.ROLE_REMOVED));
        }else{            
            s.append(EventDataHelper.getValue(EventDataHelper.ROLE_ADDED));
        }
        
        s.append(EventDataHelper.getString(EventDataHelper.ROLE, value.getRoleName(), true));
        s.append(EventDataHelper.getString(EventDataHelper.GROUP, value.getGroupName(), false));
        
        setEventSpecificData(s.toString());
    }
}
