package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.CourtLogSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaTypeEvent;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventLevelHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

public class PleaCourtLogHelper extends AbstractCourtLogHelper {
    private static final PleaCourtLogHelper instance = new PleaCourtLogHelper();

    private PleaCourtLogHelper() {
    }

    public static PleaCourtLogHelper getInstance() {
        return instance;
    }

    protected void addCjseCourtLogParameters(CourtLogCRUDValue entry, PleaSaveValue value) {
        if (value.isOnCharge()) {
            EventLevelHelper.addCjseCourtLogParameters(entry, value.getDefendantOnCaseId(), null);
        } else {
            EventLevelHelper.addCjseCourtLogParameters(entry, null, value.getDefendantOnOffenceId());
        }
    }

    protected Map createPropertyMap(CourtLogSaveValue courtLogSaveValue) {
        final PleaSaveValue value = (PleaSaveValue) courtLogSaveValue;
        final Map propertyMap = new HashMap();
        final Integer eventType = value.getCourtLogEvent();

        // set up the standard properties, required by all of the events...
        propertyMap.put("Crest_Offence_Seq_No", value.getCrestOffenceSeqNo());
        propertyMap.put("Crest_Charge_Seq_No", value.getCrestChargeSeqNo());
        propertyMap.put("Charge_Type", value.getChargeType());
        propertyMap.put("defendant_name", value.getDefendantName());

        // set up the rest of the properties...
        if (PleaTypeEvent.PLEA_FOR_INDICTMENTS_EVENT.equals(eventType)) {
            propertyMap.put("Ref_Plea_Code", value.getRefPleaCode());
            propertyMap.put("Ref_Plea_Description", value.getRefPleaDesc());
            propertyMap.put("Arraingment_Date", XDateFormat.format(value.getPleaValue().getArraignmentDate(),
                    XDateFormat.DATEFORMAT));
            propertyMap.put("Other_Plea_Text", value.getOtherPleaText());
        } else if (PleaTypeEvent.PLEA_FOR_INDICTMENTS_LESSER_OFFENCE_EVENT.equals(eventType)) {
            propertyMap.put("Ref_Plea_Code", value.getRefPleaCode());
            propertyMap.put("Ref_Plea_Description", value.getRefPleaDesc());
            propertyMap.put("Arraingment_Date", XDateFormat.format(value.getPleaValue().getArraignmentDate(),
                    XDateFormat.DATEFORMAT));
            propertyMap.put("Alt_Offence", getAlternateOffenceText(value));
        } else if (PleaTypeEvent.PLEA_FOR_SUMMARY_OFFENCE_EVENT.equals(eventType)) {
            propertyMap.put("Ref_Plea_Code", value.getRefPleaCode());
            propertyMap.put("Ref_Plea_Description", value.getRefPleaDesc());
            propertyMap.put("Offence_Description", value.getOffenceDescription());
        } else if (PleaTypeEvent.PLEA_FOR_BREACH_EVENT.equals(eventType)) {
            propertyMap.put("Admitted", value.getBreachAdmitted());
            propertyMap.put("Date_Put", XDateFormat.format(value.getDatePut(), XDateFormat.DATEFORMAT));
        } else if (PleaTypeEvent.PLEA_FOR_FAIL2APPEAR_EVENT.equals(eventType)){
            propertyMap.put("Admitted", value.getBreachAdmitted());
            propertyMap.put("Date_Put", XDateFormat.format(value.getDatePut(), XDateFormat.DATEFORMAT));
        }
        // else if (PleaTypeEvent.PLEA_DELETE_EVENT.equals(eventType)) do
        // nothing more...

        return propertyMap;
    }

    private String getAlternateOffenceText(final PleaSaveValue value) {
        String alternateOffenceText;
        if (value.isCrestLessOffPleaUncoded()) {
            alternateOffenceText = getRecordSheetDescription(value.getAltRefOffenceDesc());
        } else {
            alternateOffenceText = value.getAltRefOffenceDesc();
        }
        return alternateOffenceText;
    }

    private String getRecordSheetDescription(final String altRefOffenceDesc) {
        String recordSheetDescription = altRefOffenceDesc;
        if (recordSheetDescription == null) {
            recordSheetDescription = "";
        } else {
            final int position = altRefOffenceDesc.indexOf('*');
            if (position >= 0) {
                recordSheetDescription = altRefOffenceDesc.substring(position + 1, altRefOffenceDesc.length());
            }
        }
        return recordSheetDescription;
    }
}
