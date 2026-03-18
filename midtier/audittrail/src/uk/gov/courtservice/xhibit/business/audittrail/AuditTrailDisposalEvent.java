package uk.gov.courtservice.xhibit.business.audittrail;

import java.util.HashMap;


import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBeanHelper2;

/**
 * <p>
 * Title: AuditTrailDisposalEvent
 * </p>
 * <p>
 * Description: This is the AuditTrailEvent which is used when a Disposal is amended
 * or removed
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
public class AuditTrailDisposalEvent extends AuditTrailEventImpl {
    private static final String OBS_IND = "Y";
    
    private XhbDisposal2BasicValue disposalValue = null;
    private XhbDisposalLineBasicValue[] disposalLineValues = null;
    private HashMap<Integer, XhbDisposalLineBasicValue> disposalLineMap = new HashMap<Integer, XhbDisposalLineBasicValue>();
    private XhbRefDisposalLineBasicValue[] refDisposalLines = null;
    private XhbRefDisposalTypeBasicValue refDisposalType = null;
    
    /**
     * This constructor can be passed one of the two following values:
     * <ul>
     * <li>A Hashtable, when a disposal is updated</li>
     * <li>A XHBDisposal2BasicValue when a disposal is deleted</li>
     * </ul
     * 
     * @param evtData
     */
    public AuditTrailDisposalEvent(Object evtData){
        super(AuditTrailEvent.DISPOSAL_UPDATED,evtData);

        if(evtData instanceof XhbDisposal2BasicValue){
            constructEventData((XhbDisposal2BasicValue) evtData);
        }else{
            throw new IllegalArgumentException("Expected either Hashtable or disposal2 as argument");
        }
        
        setUpEventData();
    }
    
    /**
     * This method gets the event data if the constructor was passed a single 
     * XhbDisposal2BasicValue object
     * @param disposal
     */
    private void constructEventData(XhbDisposal2BasicValue disposal){
        disposalValue = disposal;      
        disposalLineValues = XhbDisposalLineBeanHelper2.findByDisposal2IdValue(disposal.getPrimaryKey());
        
        for(int i = 0; i < disposalLineValues.length; i++){
            if((disposalLineValues[i].getObsInd() == null) || (!disposalLineValues[i].getObsInd().equals(OBS_IND))){
                disposalLineMap.put(disposalLineValues[i].getRefDisposalLineId(), disposalLineValues[i]);
            }
        }
        
        Integer refDispId = disposalValue.getRefDisposalTypeId();
        refDisposalType = XhbRefDisposalTypeBeanHelper2.findByPrimaryKeyValue(refDispId);
        if(refDisposalType != null){
            String dispCode = refDisposalType.getDisposalCode();
            Integer tempVersion = refDisposalType.getTemplateVersion();
            Integer courtId = refDisposalType.getCourtId();
            
            refDisposalLines =  XhbRefDisposalLineBeanHelper2.findRSByCodeCourtIdAndTemplateValue(dispCode, courtId, tempVersion);            
        }
        
        
    }
    
    /**
     * This method uses the object which was passed to the constructor to
     * obtain the values needed for the event
     */
    public void setUpEventData(){
        retrieveLoginData();
        
        setCaseId(getCaseIdFromValue());
        
        String eventSpecificData = setupEventSpecificData();
        
        setEventSpecificData(eventSpecificData);
    }
    
    /**
     * 
     * @return
     */
    private String setupEventSpecificData(){
        StringBuffer s = new StringBuffer();
        boolean firstValue = true;
        
        //Check for deleted
        if(disposalValue.getObsInd().equals(OBS_IND)){
            s.append(EventDataHelper.getDeleted());
        }
                        
        if(disposalValue.getDefendantOnCaseId() != null){
            s.append(EventDataHelper.getString(EventDataHelper.DEF_ON_CASE_ID, disposalValue.getDefendantOnCaseId().toString(), firstValue));
            firstValue = false;
        }
        
        if(disposalValue.getDefendantOnOffenceId() != null){
            s.append(EventDataHelper.getString(EventDataHelper.DEF_ON_OFFENCE_ID, disposalValue.getDefendantOnOffenceId().toString(), firstValue));
            firstValue = false;
        }
        
        //get disposal2 data
        s.append(EventDataHelper.getString(EventDataHelper.DISPOSAL_TYPE, refDisposalType.getDisposalCode(), firstValue));
        
        s.append(getDisposalLineData());
        
        return s.toString();
    }
    
    private String getDisposalLineData(){
        StringBuffer s = new StringBuffer();               
        
        s.append(EventDataHelper.getValue(EventDataHelper.DISPOSAL_LINES_START));
        
        if(refDisposalLines != null){
            for(int i = 0;i < refDisposalLines.length;i++){
                XhbDisposalLineBasicValue bv = disposalLineMap.get(refDisposalLines[i].getRefDisposalLineId());
                String line = bv == null ? "" : bv.getLineData();
                s.append(", ");
                s.append(line);
            }
        }
        
        s.append(EventDataHelper.getValue(EventDataHelper.DISPOSAL_LINES_END));
        
        return s.toString();
    }
    
    private Integer getCaseIdFromValue(){
        if(disposalValue.getDefendantOnCaseId() != null){            
            XhbDefendantOnCase defOnCase = 
                XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(disposalValue.getDefendantOnCaseId());
            
            return defOnCase.getCaseId();
        }
        
        if(disposalValue.getDefendantOnOffenceId() != null){
            XhbDefendantOnOffence defOnOffence = 
                XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(disposalValue.getDefendantOnOffenceId());
            return defOnOffence.getXhbDefendantOnCase().getCaseId();
        }
        
        return null;
        
    }
}
