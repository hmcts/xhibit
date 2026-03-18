package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCodeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;

/**
 * <p>
 * Title: AuditTrailVerdictEvent
 * </p>
 * <p>
 * Description: This is the AuditTrailEvent which is used when a verdict is updated
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
public class AuditTrailVerdictEvent extends AuditTrailEventImpl {
    private static final String OBS_IND = "Y";
    private static final String DEF_ON_CHARGE = "C";
    private static final String DEF_ON_OFFENCE = "O";
    
    private XhbVerdictBasicValue value;
    
    /**
     * This constructor should be passed a XHBVerdictBasicValue which will hold
     * all the data needed for this event
     * 
     * @param evtData
     */
    public AuditTrailVerdictEvent(Object evtData){
        super(AuditTrailEvent.VERDICT_UPDATED,evtData);
        
        if(!(evtData instanceof XhbVerdictBasicValue)){
            throw new IllegalArgumentException();
        }
        
        value = (XhbVerdictBasicValue)evtData;
        
        setUpEventData();
    }
    
    /**
     * This method uses the object which was passed to the constructor to
     * obtain the values needed for the event
     * 
     */
    public void setUpEventData(){
        retrieveLoginData();
        
        setCaseId(getCaseIdFromValue());
        
        setEventSpecificData(setupEventSpecificData());
    }
    
    /**
     * This method returns a string containing the event specific data for this event
     * @return
     */
    private String setupEventSpecificData(){
        StringBuffer s = new StringBuffer();
        String verdictCode = null;
        
        if(value.getObsInd() != null && value.getObsInd().equals(OBS_IND)){
            s.append(EventDataHelper.getDeleted());
        }
        
        if(value.getRefVerdictId() != null){
            XhbRefSystemCode sc = XhbRefSystemCodeBeanHelper2.findByPrimaryKey(value.getRefVerdictId());
            verdictCode = sc.getCode();
        }
        s.append(EventDataHelper.getString(EventDataHelper.VERDICT_CODE, verdictCode, true));
        
        if (value.getDefOnChargeOrOffence() != null 
                && value.getDefOnChargeOrOffence().equals(DEF_ON_CHARGE)
                && value.getDefendantChargeId() != null) {
            s.append(EventDataHelper.getString(EventDataHelper.DEF_CHARGE_ID, value.getDefendantChargeId().toString(), false));
        } else if (value.getDefOnChargeOrOffence() != null
                && value.getDefOnChargeOrOffence().equals(DEF_ON_OFFENCE)
                && value.getDefendantOnOffenceId() != null) {
            s.append(EventDataHelper.getString(EventDataHelper.DEF_ON_OFFENCE_ID, value.getDefendantOnOffenceId().toString(), false));
        }
        
        String jurorsAssenting = value.getJurorsAssenting() == null ? "" : value.getJurorsAssenting().toString();
        String jurorsDissenting = value.getJurorsDissenting() == null ? "" : value.getJurorsDissenting().toString();
        
        s.append(EventDataHelper.getString(EventDataHelper.JURORS_ASSENTING, jurorsAssenting, false));
        s.append(EventDataHelper.getString(EventDataHelper.JURORS_DISSENTING, jurorsDissenting, false));
        
        s.append(EventDataHelper.getString(EventDataHelper.VERDICT_DATE, String.format("%1$tF", value.getVerdictDate()), false));
        
        if(value.getOtherVerdictText() != null){
            s.append(EventDataHelper.getString(EventDataHelper.OTHER_VERDICT_TEXT, value.getOtherVerdictText(), false));
        }
        
        return s.toString();
    }
    
    /**
     * This method gets the caseId from the BasicObject passed in to the constructor.
     * The verdict can either be against a defendant on offence or against a defendant
     *  on charge. The CaseId must be worked out accordingly.
     * 
     * @return Integer - CaseId
     */
    private Integer getCaseIdFromValue(){
        Integer caseId = null;
        if(value.getDefOnChargeOrOffence() != null){
            if(value.getDefOnChargeOrOffence().equals(DEF_ON_OFFENCE)){
                //pick up case_id from def on offence
                if(value.getDefendantOnOffenceId() != null){
                    XhbDefendantOnOffence defOnOffence = 
                        XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(value.getDefendantOnOffenceId());
                    caseId = defOnOffence.getXhbDefendantOnCase().getCaseId();
                }
            }else if(value.getDefOnChargeOrOffence().equals(DEF_ON_CHARGE)){
                //Pick up case_id from def on offence
                if(value.getDefendantChargeId() != null){
                    XhbDefendantCharge defOnCharge = 
                        XhbDefendantChargeBeanHelper2.findByPrimaryKey(value.getDefendantChargeId());
                    caseId = defOnCharge.getXhbDefendantOnCase().getCaseId();
                }
            }
        }
        return caseId;
    }
}
