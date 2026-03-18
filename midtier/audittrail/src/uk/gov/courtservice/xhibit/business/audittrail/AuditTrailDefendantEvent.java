package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: AuditTrailDefendantEvent
 * </p>
 * <p>
 * Description: This is the AuditTrailEvent which is used when defendant
 * details are updated
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
public class AuditTrailDefendantEvent extends AuditTrailEventImpl {
    private DefendantValue value;
    
    /**
     * This constructor should be passed a DefendantValue which will hold
     * all the data needed for this event
     * 
     * @param evtData
     */
    public AuditTrailDefendantEvent(Object evtData){
        super(AuditTrailEvent.DEFENDANT_UPDATED,evtData);
        
        if(!(evtData instanceof DefendantValue)){
            throw new IllegalArgumentException();
        }
        
        value = (DefendantValue) evtData;
        
        setUpEventData();
    }    
    
    /**
     * This method uses the object which was passed to the constructor to
     * obtain the values needed for the event
     * 
     */
    public void setUpEventData(){
        retrieveLoginData();
        
        XhbDefendantOnCase defOnCase = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(value.getDefOnCaseBasicValue().getId());
        XhbDefendant def = defOnCase.getXhbDefendant();
        setCaseId(defOnCase.getCaseId());
        
        StringBuffer s = new StringBuffer();
        
        s.append(EventDataHelper.getString(EventDataHelper.FIRST_NAME, def.getFirstName(), true));
        s.append(EventDataHelper.getString(EventDataHelper.MIDDLE_NAME, def.getMiddleName(), false));
        s.append(EventDataHelper.getString(EventDataHelper.INITIALS, def.getInitials(), false));
        s.append(EventDataHelper.getString(EventDataHelper.SURNAME, def.getSurname(), false));
        s.append(EventDataHelper.getString(EventDataHelper.ADDRESS1, def.getXhbAddress().getAddress1(), false));
        s.append(EventDataHelper.getString(EventDataHelper.ADDRESS2, def.getXhbAddress().getAddress2(), false));
        s.append(EventDataHelper.getString(EventDataHelper.ADDRESS3, def.getXhbAddress().getAddress3(), false));
        s.append(EventDataHelper.getString(EventDataHelper.ADDRESS4, def.getXhbAddress().getAddress4(), false));
        s.append(EventDataHelper.getString(EventDataHelper.TOWN, def.getXhbAddress().getTown(), false));
        s.append(EventDataHelper.getString(EventDataHelper.POST_CODE, def.getXhbAddress().getPostcode(), false));
        s.append(EventDataHelper.getString(EventDataHelper.NATIONALITY, defOnCase.getNationality(), false));
        s.append(EventDataHelper.getString(EventDataHelper.DOB, String.format("%1$tF", def.getDateOfBirth()), false));
        s.append(EventDataHelper.getString(EventDataHelper.SEX, def.getGender().toString(), false));
        s.append(EventDataHelper.getString(EventDataHelper.JUVENILE, defOnCase.getIsJuvenile(), false));
        s.append(EventDataHelper.getString(EventDataHelper.MASKED, defOnCase.getIsMasked(), false));
        s.append(EventDataHelper.getString(EventDataHelper.MASKED_NAME, defOnCase.getMaskedName(), false));
        s.append(EventDataHelper.getString(EventDataHelper.LAST_CONVICTION_DATE, String.
                format("%1$tF",def.getLastConvictionDate()), false));
        s.append(EventDataHelper.getString(EventDataHelper.ASN, defOnCase.getAsn(), false));
        s.append(EventDataHelper.getString(EventDataHelper.URN, defOnCase.getPtiurn(), false));
        
        setEventSpecificData(s.toString());
    }
}
