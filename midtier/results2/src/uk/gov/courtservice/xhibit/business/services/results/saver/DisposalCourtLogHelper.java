package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.common.results.vos.CourtLogSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;

public class DisposalCourtLogHelper extends AbstractCourtLogHelper {
    private static final DisposalCourtLogHelper instance = new DisposalCourtLogHelper();

    private static final String DISPOSAL_CODE_BOJDGD = "BOJDGD";

    private static final String DISPOSAL_CODE_BOJDGD_PROMPT = "Date";

    private DisposalCourtLogHelper() {
    }

    public static DisposalCourtLogHelper getInstance() {
        return instance;
    }

    private static void setMandatoryProperties(DisposalSaveValue value, Map propertyMap) {
        final Integer courtLogEvent = value.getCourtLogEvent();

        propertyMap.put("E" + courtLogEvent + "_Defendant_Name", value.getDefendantName());
        propertyMap.put("E" + courtLogEvent + "_Case_Type", value.getCourtLogCaseType());
        propertyMap.put("E" + courtLogEvent + "_Case_No", value.getCourtLogCaseNumber());
        propertyMap.put("E" + courtLogEvent + "_Ref_Disposal_Title", value.getRefDisposalTitle());
        propertyMap.put("E" + courtLogEvent + "_Ref_Disposal_Code", value.getRefDisposalCode());
        propertyMap.put("E" + courtLogEvent + "_Disposal_Detail", value.getDisposalDetail());
    }

    private void setRelatedDisposalProperties(DisposalSaveValue value, Map propertyMap) {
        final Integer courtLogEvent = value.getCourtLogEvent();

        propertyMap.put("E" + courtLogEvent + "_Crest_Offence_Seq_No", value.getCrestOffenceSeqNo());
        propertyMap.put("E" + courtLogEvent + "_Crest_Charge_Seq_No", value.getCrestChargeSeqNo());
        propertyMap.put("E" + courtLogEvent + "_Charge_Type", value.getChargeType());
    }

    private void setMagistrateGeneralDisposalProperties(DisposalSaveValue value, Map propertyMap) {
        final Integer courtLogEvent = value.getCourtLogEvent();

        propertyMap.put("E" + courtLogEvent + "_Magistrate_General_Disposal", new Boolean(true));
    }

    private void setVariationForMagistrateGeneralDisposalProperties(DisposalSaveValue value, Map propertyMap) {
        final Integer courtLogEvent = value.getCourtLogEvent();

        propertyMap.put("E" + courtLogEvent + "_Variation_For_Magistrate_General_Disposal", new Boolean(true));
    }

	protected Map createPropertyMap(CourtLogSaveValue courtLogSaveValue) {
        final DisposalSaveValue value = (DisposalSaveValue) courtLogSaveValue;
        final Map propertyMap = new HashMap();

        setMandatoryProperties(value, propertyMap);

        if (value.isRelatedDisposal()) {
            setRelatedDisposalProperties(value, propertyMap);
        } else if (value.isMagistrateGeneralDisposal()) {
            setMagistrateGeneralDisposalProperties(value, propertyMap);
        } else if (value.isVariationForMagistrateGeneralDisposal()) {
            setVariationForMagistrateGeneralDisposalProperties(value, propertyMap);
        }
   
        if (value.getDisposalCode().equals(DISPOSAL_CODE_BOJDGD)) {
            propertyMap.put("Disposal_Data", value.getDisposalLineDataByPrompt(DISPOSAL_CODE_BOJDGD_PROMPT));
        }

        return propertyMap;
    }
}
