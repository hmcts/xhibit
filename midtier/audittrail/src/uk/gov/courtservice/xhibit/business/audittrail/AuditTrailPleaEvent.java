package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCodeBeanHelper2;

/**
 * <p>
 * Title: AuditTrailPleaEvent
 * </p>
 * <p>
 * Description: This is the AuditTrailEvent which is used when a Plea is updated
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
public class AuditTrailPleaEvent extends AuditTrailEventImpl {
    private static final String DEF_ON_OFFENCE = "O";
    private static final String DEF_ON_CHARGE = "C";
    private static final String OBS_IND = "Y";
    
    private XhbPleaBasicValue value;
    
    /**
     * This constructor should be passed a XHBPleaBasicValue which will hold
     * all the data needed for this event
     * 
     * @param evtData
     */
    public AuditTrailPleaEvent(Object evtData){
        super(AuditTrailEvent.PLEA_UPDATED,evtData);
        
        if (!(evtData instanceof XhbPleaBasicValue)){
            throw new IllegalArgumentException();
        }
        
        value = (XhbPleaBasicValue) evtData;
        
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

        String eventSpecData = setupEventSpecificData();
        
        setEventSpecificData(eventSpecData);
    }
    
    /**
     * This method returns a String containing the event specific data
     * @return
     */
    private String setupEventSpecificData(){
        String pleaCode = null;
        StringBuffer s = new StringBuffer();
        
        if (value.getObsInd() != null && value.getObsInd().equals(OBS_IND)){
            s.append(EventDataHelper.getDeleted());
        }
        
        if(value.getRefPleaId() != null){
            XhbRefSystemCode sc = XhbRefSystemCodeBeanHelper2.findByPrimaryKey(value.getRefPleaId());
            pleaCode = sc.getCode();
        }
        
        s.append(EventDataHelper.getString(EventDataHelper.PLEA_CODE, pleaCode, true));
        
        if(value.getDefOnChargeOrOffence().equals(DEF_ON_CHARGE)){
            s.append(EventDataHelper.getString(EventDataHelper.DEF_CHARGE_ID, value.getDefendantChargeId().toString(), false));
        }else if(value.getDefOnChargeOrOffence().equals(DEF_ON_OFFENCE)){
            s.append(EventDataHelper.getString(EventDataHelper.DEF_ON_OFFENCE_ID, value.getDefendantOnOffenceId().toString(), false));
        }
        
        s.append(EventDataHelper.getString(EventDataHelper.ARRAIGNMENT_DATE, String.format("%1$tF", value.getArraignmentDate()), false));
        
        if(value.getOtherPleaText() != null){
            s.append(EventDataHelper.getString(EventDataHelper.PLEA_ADDITIONAL_INFO, value.getOtherPleaText(), false));
        }
        
        return s.toString();
    }
    
    /**
     * This method gets the caseId from the BasicObject passed in to the constructor.
     * The plea can either be against a defendant on offence or against a defendant
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
