package uk.gov.courtservice.xhibit.business.services.defendant;


import java.util.List;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;

public class DefendantOnOffenceHelper {
    
	private DefendantOnOffenceDatabaseManager dbManager = new DefendantOnOffenceDatabaseManager();

    public DefendantOnOffenceHelper() {
        // default constructor
    }

    public void setDefendantOnOffenceBasicValue(XhbDefendantOnOffenceBasicValue value, XhbDefendantOnOffence local) {
        value.setDefendantOnCaseId(local.getXhbDefendantOnCase().getDefendantOnCaseId());
        value.setOffenceId(local.getOffenceId());
        value.setAppealAgainstType(local.getAppealAgainstType());
        value.setObsInd(local.getObsInd());
        value.setIsStayed(local.getIsStayed());
        value.setCrnId(local.getCrnId());
        value.setVcoFlag(local.getVcoFlag());
        value.setVcoDate(local.getVcoDate());
        value.setArrestDate(local.getArrestDate());
        value.setChargeDate(local.getChargeDate());
        value.setIsCommittedOnBail(local.getIsCommittedOnBail());
        value.setSeqNo(local.getSeqNo());
        value.setDefendantOnCaseId(local.getDefendantOnCaseId());
        value.setInterimD20(local.getInterimD20());
        
        value.setCreationDate(local.getCreationDate());
        value.setCreatedBy(local.getCreatedBy());
    }

    public List<Integer> findDefendantIdsByChargeIdAndRefOffenceId(final Integer chargeId, final Integer refOffenceId, final Integer addressId) {
    	return dbManager.findDefendantIdsByChargeIdAndRefOffenceId(chargeId, refOffenceId, addressId);
    }

}
