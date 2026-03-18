package uk.gov.courtservice.framework.services.audittrail;

import java.util.Date;

/**
 * <p>
 * Title: AuditTrailEvent
 * </p>
 * <p>
 * Description: This is the interface for an AuditTRailEvent, from which all
 * events will inherit
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

public interface AuditTrailEvent {
        
    public static final String USER_LOGON = "logon";
    public static final String USER_LOGOFF = "logoff";
    public static final String ROLE_CHANGE = "roleChange";
    public static final String PLEA_UPDATED = "pleaUpdated";
    public static final String VERDICT_UPDATED = "verdictUpdated";
    public static final String DISPOSAL_UPDATED = "disposalUpdated";
    public static final String COURT_UPDATED = "courtUpdated";
    public static final String DEFENDANT_UPDATED = "defendantUpdated";
    public static final String SYSTEM_ERROR = "systemError";
    
    public void retrieveLoginData();
    
    public void setEvtType(String evtType);

    public String getEvtType();

    public void setEvtData(Object evtData);

    public Object getEvtData();

    public void setSuccess(boolean success);

    public boolean isSuccess();

    public void setTimestamp(Date timestamp);

    public Date getTimestamp() ;

    public void setWorkstationId(String workstationId);

    public String getWorkstationId();

    public void setUserId(String userId);

    public String getUserId();

    public void setCourtHouseId(String courtHouseId);

    public String getCourtHouseId();

    public void setCaseId(Integer caseId);

    public Integer getCaseId();    
    
    public void setEventSpecificData(String eventSpecificData);

    public String getEventSpecificData();

}
