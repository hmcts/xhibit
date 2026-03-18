package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.integration.vos.services.caseretrieval.CaseAccessValue;

public class AuditTrailSystemEvent extends AuditTrailEventImpl {
    private CaseAccessValue value;
    
    public AuditTrailSystemEvent(Object evtData){
        super(AuditTrailEvent.SYSTEM_ERROR,evtData);
        
        if(!(evtData instanceof CaseAccessValue)){
            throw new IllegalArgumentException();
        }
        
        value = (CaseAccessValue)evtData;
        
        setUpEventData();
    }
    
    public void setUpEventData(){
        retrieveLoginData();
        
        setCaseId(value.getCaseId());
        
        setEventSpecificData(EventDataHelper.getString(EventDataHelper.ERROR, EventDataHelper.getValue(EventDataHelper.ERROR_CS_RF), true));
    }
}
