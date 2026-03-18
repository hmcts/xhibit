package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResultBeanHelper2;
import uk.gov.courtservice.xhibit.common.results.vos.CourtLogSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictTypeEvent;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * @author Unknown
 * @version $Revision: 1.20 $
 */
public class VerdictCourtLogHelper extends AbstractCourtLogHelper {
    private static final VerdictCourtLogHelper instance = new VerdictCourtLogHelper();

    private static final String OTHER_TEXT = "O";

    private VerdictCourtLogHelper() {
    }

    public static VerdictCourtLogHelper getInstance() {
        return instance;
    }

    protected void addCjseCourtLogParameters(CourtLogCRUDValue entry, VerdictSaveValue value) {
        log.debug("addCjseCourtLogParameters(CourtLogCRUDValue entry, VerdictSaveValue value) : START");

        // APPEALS are CASE or OFFENCE level events so only add additional
        // parameters
        // to appeals values for the OFFENCE ones.
        if (!value.isAppealCase() || value.isOnOffence()) {
            // these are CRN level events, or DEFENDANT level events if this
            // is
            // a Breach so need to add defendantOnOffenceId or
            // defendantOnCaseId
            // respectively for the CJSE level event
            super.addCjseCourtLogParameters(entry, value);
        }

        log.debug("addCjseCourtLogParameters(CourtLogCRUDValue entry, VerdictSaveValue value) : END");
    }

    protected Map createPropertyMap(CourtLogSaveValue courtLogSaveValue) {
        log.debug("createPropertyMap(VerdictSaveValue value) : START");
        final VerdictSaveValue value = (VerdictSaveValue) courtLogSaveValue;

        Map propertyMap = new HashMap();
        XhbRefAppResultBasicValue refAppResultValue = null;
        Integer refAppResultId = value.getRefAppResultId();

        if (refAppResultId != null) {
            log.debug("refAppResultId!=null");
            refAppResultValue = XhbRefAppResultBeanHelper2.findByPrimaryKeyValue(refAppResultId);
        }

        setVerdictCodeProperty(value, propertyMap);
        setDefendantProperty(value, propertyMap);
        setIndictmentProperty(value, propertyMap);
        setCountProperty(value, propertyMap);
        setRatioProperty(value, propertyMap);
        setAppealDescProperty(value, propertyMap, refAppResultValue);
        setVerdictDescProperty(value, propertyMap, refAppResultValue);
        setOffenceDescProperty(value, propertyMap, refAppResultValue);

        // quick include of the other verdict text...
        if (VerdictTypeEvent.OTHER_EVENT.equals(value.getCourtLogEvent())) {
            propertyMap.put("Other_Verdict_Text", value.getOtherVerdictText());
        }

        // now log all of the properties that have been set to the property map
        logPropertyMap(propertyMap);

        log.debug("createPropertyMap(VerdictSaveValue value) : END");
        return propertyMap;
    }

    /**
     * Extracted method used to log all of the properties set to the court log
     * property map, this will only occur if debugging has been enabled.
     * 
     * @param propertyMap
     */
    private void logPropertyMap(Map propertyMap) {
        if (log.isDebugEnabled()) {
            final Iterator keys = propertyMap.keySet().iterator();
            while (keys.hasNext()) {
                final Object mapKey = keys.next();
                final Object mapValue = propertyMap.get(mapKey);

                log.debug("propertyMap[" + mapKey + "] = " + mapValue);
            }
        }
    }

    private String getOtherFreeText(VerdictSaveValue value) {
        log.debug("getOtherFreeText(VerdictSaveValue value) : START");
        // for BUG-FIX 53210
        if (OTHER_TEXT.equalsIgnoreCase(value.getRefVerdictCode())) {
            log.debug("getOtherFreeText(VerdictSaveValue value) : END - OTHER_TEXT = getRefVerdictCode");
            // set the new free text to the decode
            return value.getRefVerdictDesc();
        } else {
            log.debug("getOtherFreeText(VerdictSaveValue value) : END - OTHER_TEXT != getRefVerdictCode");
            // it's ok if this is null
            return value.getOtherVerdictText();
        }
    }

    private void setVerdictCodeProperty(VerdictSaveValue value, Map propertyMap) {
        log.debug("setVerdictCodeProperty(VerdictSaveValue value, Map propertyMap) : START");

        if (VerdictTypeEvent.GUILTY_UNANIMOUS_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.NOT_GUILTY_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.ALTERNATE_OFFENCE_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.LESSER_OFFENCE_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.OTHER_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.GUILTY_BY_JUDGE_ALONE_DVC_VA_EVENT.equals(value.getCourtLogEvent())) {
            propertyMap.put("E" + value.getCourtLogEvent() + "_Ref_Verdict_Code", value.getRefVerdictCode());
        }

        log.debug("setVerdictCodeProperty(VerdictSaveValue value, Map propertyMap) : END");
    }

    private void setDefendantProperty(VerdictSaveValue value, Map propertyMap) {
        log.debug("setDefendantProperty(VerdictSaveValue value, Map propertyMap) : START");

        if (!(value.isMiscAppeal() || value.isOnCase())) {
            propertyMap.put("Defendant_Name", value.getDefendantName());
        }

        log.debug("setDefendantProperty(VerdictSaveValue value, Map propertyMap) : END");
    }

    private void setIndictmentProperty(VerdictSaveValue value, Map propertyMap) {
        log.debug("setIndictmentProperty(VerdictSaveValue value, Map propertyMap) : START");

        if (!value.isAppealCase()) {
            propertyMap.put("Crest_Charge_Seq_No", String.valueOf(value.getCrestChargeSeqNo().intValue()));
        }

        log.debug("setIndictmentProperty(VerdictSaveValue value, Map propertyMap) : END");
    }

    private void setCountProperty(VerdictSaveValue value, Map propertyMap) {
        log.debug("setCountProperty(VerdictSaveValue value, Map propertyMap) : START");

        if (!value.isMiscAppeal()) {
            if (value.isOnOffence()) {
                propertyMap.put("Crest_Offence_Seq_No", String.valueOf(value.getCrestOffenceSeqNo().intValue()));
            } else {
                propertyMap.put("Crest_Offence_Seq_No", null);
            }
        }

        log.debug("setCountProperty(VerdictSaveValue value, Map propertyMap) : END");
    }

    private void setRatioProperty(VerdictSaveValue value, Map propertyMap) {
        log.debug("setRatioProperty(VerdictSaveValue value, Map propertyMap) : START");
        log.debug("setRatioProperty() - value.getCourtLogEvent() = " + value.getCourtLogEvent());
        log.debug("setRatioProperty() - value.getVerdictBasicValue().getJurorsAssenting() = "
                + value.getVerdictBasicValue().getJurorsAssenting());
        log.debug("setRatioProperty() - value.getVerdictBasicValue().getJurorsDissenting() = "
                + value.getVerdictBasicValue().getJurorsDissenting());

        if (VerdictTypeEvent.GUILTY_MAJORITY_EVENT.equals(value.getCourtLogEvent())) {
            propertyMap.put("Ratio", getRatio(value));
        } else if ((VerdictTypeEvent.OTHER_EVENT.equals(value.getCourtLogEvent())
                || value.getVerdictValue().hasAssentingDissenting() || value.getVerdictValue()
                .getJurorsAssentingOption())
                && value.getVerdictBasicValue().getJurorsAssenting() != null
                && value.getVerdictBasicValue().getJurorsDissenting() != null) {
            propertyMap.put("Ratio", getRatio(value));
            propertyMap.put("Unanimous", new Boolean(value.getDissenting() == 0));
        }

        log.debug("setRatioProperty(VerdictSaveValue value, Map propertyMap) : END");
    }

    /**
     * Extracted small, well defined method that constructs a ratio String based
     * upon the passed in <code>VerdictSaveValue</code> object.
     * 
     * @param value
     * @return The formatted ratio <code>String</code>.
     */
    private String getRatio(VerdictSaveValue value) {
        return (value.getAssenting() + "/" + value.getDissenting());
    }

    private void setAppealDescProperty(VerdictSaveValue value, Map propertyMap,
            XhbRefAppResultBasicValue refAppResultValue) {
        log.debug("setAppealDescProperty : START");

        if (value.isMiscAppeal()) {
            // Miscellaneous Appeal
            propertyMap.put("Appeal_Desc", value.getRefVerdictDesc());
            propertyMap.put("Appeal_Result_Date", XDateFormat.format(value.getVerdictDate(), XDateFormat.DATEFORMAT));
        } else if (value.isCriminalAppealOnCase()) {
            // Criminal Appeal
            propertyMap.put("Appeal_Desc", value.getRefVerdictDesc());
            propertyMap.put("Appeal_Result_Date", XDateFormat.format(value.getVerdictDate(), XDateFormat.DATEFORMAT));
        } else if (value.isCriminalAppealOnOffence()) {
            // Criminal Appeal
            propertyMap.put("Appeal_Desc", getCriminalAppealDesc(refAppResultValue));
        } else if (value.isOnDisposal()) {
            // Criminal Appeal for Magistrate General Disposal.
            propertyMap.put("Appeal_Desc", getCriminalAppealDesc(refAppResultValue));
            propertyMap.put("Disposal_Detail", value.getDisposalSaveValue().getDisposalDetail());
        } else {
            log.debug("should not get to here");
            propertyMap.put("Appeal_Desc", null);
        }

        log.debug("setAppealDescProperty: END");
    }

    /**
     * This will get the description for an appeal.
     * 
     * @param verBasicValue
     * @return String - the description
     */
    private String getCriminalAppealDesc(XhbRefAppResultBasicValue refAppResultValue) {
        log.debug("getCriminalAppealDesc(XhbRefAppResultBasicValue refAppResultValue) : START");
        StringBuffer appealDesc = new StringBuffer();

        if (refAppResultValue != null) {
            if (refAppResultValue.getAppResultDescr2() == null) {
                appealDesc.append(refAppResultValue.getAppResultDescr1());
            } else {
                appealDesc.append(refAppResultValue.getAppResultDescr1());
                appealDesc.append(' ');
                appealDesc.append(refAppResultValue.getAppResultDescr2());
            }
        }

        log.debug("getCriminalAppealDesc(XhbRefAppResultBasicValue refAppResultValue) : END");
        return appealDesc.toString();
    }

    private void setVerdictDescProperty(VerdictSaveValue value, Map propertyMap,
            XhbRefAppResultBasicValue refAppResultValue) {
        log.debug("setVerdictDescProperty : START");

        if (VerdictTypeEvent.OTHER_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.ALTERNATE_OFFENCE_EVENT.equals(value.getCourtLogEvent()) // PR6033
                || VerdictTypeEvent.GUILTY_UNANIMOUS_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.NOT_GUILTY_EVENT.equals(value.getCourtLogEvent()) 
                || VerdictTypeEvent.GUILTY_BY_JUDGE_ALONE_DVC_VA_EVENT.equals(value.getCourtLogEvent())){
            // MH - check if we have another Description - if so we should
            // use this.
            String otherDescription = getOtherFreeText(value);
            if (otherDescription != null) {
                propertyMap.put("Verdict_Desc", otherDescription);
            } else if (value.getRefVerdictDesc() != null) {
                propertyMap.put("Verdict_Desc", value.getRefVerdictDesc());
            } else {
                propertyMap.put("Verdict_Desc", null);
            }
        }

        log.debug("setVerdictDescProperty : END");
    }

    private void setOffenceDescProperty(VerdictSaveValue value, Map propertyMap,
            XhbRefAppResultBasicValue refAppResultValue) {
        log.debug("setOffenceDescProperty : START");

        if (VerdictTypeEvent.ALTERNATE_OFFENCE_EVENT.equals(value.getCourtLogEvent())
                || VerdictTypeEvent.LESSER_OFFENCE_EVENT.equals(value.getCourtLogEvent())
                || (refAppResultValue != null && "Y".equalsIgnoreCase(refAppResultValue.getLesserOffInd()))) {
            if (value.getAltRefOffenceDesc() != null) {
                propertyMap.put("Offence_Desc", getAlternateOffenceText(value));
            } else {
                propertyMap.put("Offence_Desc", null);
            }
        }

        log.debug("setOffenceDescProperty : END");
    }

    private String getAlternateOffenceText(final VerdictSaveValue value) {
        String alternateOffenceText;
        if (value.isCrestLessOffVerdictUncoded() || value.hasAppealLesserOffence()) {
            alternateOffenceText = getRecordSheetDescription(value.getAltRefOffenceDesc());
        } else {
            alternateOffenceText = value.getAltRefOffenceDesc();
        }
        return alternateOffenceText;
    }

    private String getRecordSheetDescription(final String altRefOffenceDesc) {
        final int position = altRefOffenceDesc.indexOf('*');
        if (position >= 0) {
            return altRefOffenceDesc.substring(position + 1, altRefOffenceDesc.length());
        } else {
            return altRefOffenceDesc;
        }
    }
}
