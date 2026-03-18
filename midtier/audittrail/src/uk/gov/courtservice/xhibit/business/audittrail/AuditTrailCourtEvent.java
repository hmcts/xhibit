package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDesc;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDescBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDescBeanNotFoundException;

/**
 * <p>
 * Title: AuditTrailCourtEvent
 * </p>
 * <p>
 * Description: This is the AuditTrailEvent which is used when a court log
 * event is modified or deleted
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
public class AuditTrailCourtEvent extends AuditTrailEventImpl {    
    private XhbCourtLogEntryBasicValue value;      
    
    /**
     * This constructor should be passed a XhbCourtLogEntryBasicValue which will hold
     * all the data needed for this event
     * 
     * @param evtData
     */
    public AuditTrailCourtEvent(Object evtData){
        super(AuditTrailEvent.COURT_UPDATED,evtData);        
        
        if(!(evtData instanceof XhbCourtLogEntryBasicValue)){
            throw new IllegalArgumentException();
        }
        
        value = (XhbCourtLogEntryBasicValue)evtData;
        
        setUpEventData();
        
    }

    /**
     * This method uses the object which was passed to the constructor to
     * obtain the values needed for the event
     * 
     */
    protected void setUpEventData(){
        retrieveLoginData();
        setCaseId(value.getCaseId());

        StringBuffer s = new StringBuffer();
        try{
            XhbCourtLogEntryBeanHelper2.findByPrimaryKey(value.getEntryId());
        }catch(XhbCourtLogEntryBeanNotFoundException ex){
            //The courtLogEntry has been deleted
            s.append(EventDataHelper.getDeleted());            
        }
        
        s.append(EventDataHelper.getString(EventDataHelper.ENTRY_ID, value.getEntryId().toString(), true));
        s.append(EventDataHelper.getString(EventDataHelper.EVENT_TYPE, getEventDesc(value.getEventDescId()), false));
        
        String deftName = getDeftName();
        
        if (deftName != null){
            s.append(EventDataHelper.getString(EventDataHelper.DEFENDANT, deftName, false));
        }
        
        s.append(EventDataHelper.getString(EventDataHelper.XML, value.getLogEntryXml(), false));
        
        setEventSpecificData(s.toString());        
    }
    
    /**
     * This method returns the defendant's name based on the defendantOnCaseId / defendantOnOffenceId
     * @return String - defendant name
     */
    private String getDeftName(){
        Integer defendantOnCaseId = value.getDefendantOnCaseId();
        
        Integer defendantOnOffenceId = value.getDefendantOnOffenceId();
                
        String deftName = null;
        if(defendantOnCaseId!=null){
            deftName = EventDataHelper.getDeftDetailsFromDocId(defendantOnCaseId);
        }else if(defendantOnOffenceId!=null){
            deftName = EventDataHelper.getDeftDetailsFromDofId(defendantOnOffenceId);
        }
        
        return deftName;
    }

    
    /**
     * This method accepts an integer which represents the eventDescId and 
     * does a lookup on the XHB_COURT_LOG_EVENT_DESC table to return
     * the event type
     * 
     * @param eventDescId Integer
     * @return String - description of this event type
     */
    private String getEventDesc(Integer eventDescId){
        XhbCourtLogEventDesc eventDesc;
        try{
             eventDesc= XhbCourtLogEventDescBeanHelper2.findByPrimaryKey(eventDescId);
        }catch(XhbCourtLogEventDescBeanNotFoundException ex){
            return null;
        }
        return eventDesc.getShortDescription();
    }
}
