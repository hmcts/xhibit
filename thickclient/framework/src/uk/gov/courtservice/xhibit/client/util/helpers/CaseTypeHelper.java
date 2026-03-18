package uk.gov.courtservice.xhibit.client.util.helpers;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version $Id: CaseTypeHelper.java,v 1.6 2006/06/05 12:30:42 bzjrnl Exp $
 */

public class CaseTypeHelper {
    public static final int Normal_CaseType = 0;

    public static final int CriminalAppeal_CaseType = 1;

    public static final int MiscelleanousAppeal_CaseType = 2;

    public static final int Undefined_CaseType = 3;

    public static final int Combined_CaseType = 4;

    public static final int Sentence_CaseType = 5;

    public static final int Bail_CaseType = 6;

    public static final int Trial_CaseType = 7;

    private static final Logger log = CSServices.getLogger(CaseTypeHelper.class);

    private static final boolean internalDebug = false;

    public static boolean internalDebugCaseTypes = false;

    private CaseTypeHelper() {
    }

    // Hearing type using scheduled hearing value
    public static int determineHearingTypeScreen(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        if (shv == null)
            throw new UnknownCaseTypeException("hearingtype.nocasetype", "parameter supplied was null");
        return determineCaseType(shv.getCaseType(), shv.getCaseSubType());
    }

    public static boolean isNormal_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isNormalCase(determineHearingTypeScreen(shv));
    }

    public static boolean isCriminalAppeal_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isCriminalAppealCase(determineHearingTypeScreen(shv));
    }

    public static boolean isMiscelleanousAppeal_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isMiscAppealCase(determineHearingTypeScreen(shv));
    }

    public static boolean isSentence_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isSentenceCase(determineHearingTypeScreen(shv));
    }

    public static boolean isTrial_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isTrialCase(determineHearingTypeScreen(shv));
    }

    public static boolean isBail_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isBailCase(determineHearingTypeScreen(shv));
    }

    public static boolean isUndefined_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isUndefinedCase(determineHearingTypeScreen(shv));
    }

    public static boolean isCombinedCourt_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return isCombinedCourtCase(determineHearingTypeScreen(shv));
    }

    // Hearing type using hearing header value
    public static int determineHearingTypeScreen(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        if (hhv == null)
            throw new UnknownCaseTypeException("hearingtype.nocasetype",
                    "determineHearingTypeScreen(HearingHeaderValue hhv) -  parameter supplied was null");
        // return determineCaseType(hhv.getCaseType(), hhv.getCaseSubType());
        return determineCaseType(hhv.getHhCase().getCaseType(), hhv.getHhCase().getCaseSubType());
    }

    public static boolean isNormal_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isNormalCase(determineHearingTypeScreen(hhv));
    }

    public static boolean isCriminalAppeal_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isCriminalAppealCase(determineHearingTypeScreen(hhv));
    }

    public static boolean isMiscelleanousAppeal_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isMiscAppealCase(determineHearingTypeScreen(hhv));
    }

    public static boolean isSentence_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isSentenceCase(determineHearingTypeScreen(hhv));
    }

    public static boolean isTrial_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isTrialCase(determineHearingTypeScreen(hhv));
    }

    public static boolean isBail_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isBailCase(determineHearingTypeScreen(hhv));
    }

    public static boolean isUndefined_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isUndefinedCase(determineHearingTypeScreen(hhv));
    }

    public static boolean isCombinedCourt_CaseType(HearingHeaderValue hhv) throws UnknownCaseTypeException {
        return isCombinedCourtCase(determineHearingTypeScreen(hhv));
    }

    // standard methods used by all other helper methods
    public static boolean isNormalCase(int staticCaseType) {
        if (staticCaseType == Sentence_CaseType || staticCaseType == Bail_CaseType || staticCaseType == Trial_CaseType
                || staticCaseType == Normal_CaseType) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCriminalAppealCase(int staticCaseType) {
        return (staticCaseType == CriminalAppeal_CaseType);
    }

    public static boolean isMiscAppealCase(int staticCaseType) {
        return (staticCaseType == MiscelleanousAppeal_CaseType);
    }

    public static boolean isSentenceCase(int staticCaseType) {
        return (staticCaseType == Sentence_CaseType);
    }

    public static boolean isTrialCase(int staticCaseType) {
        return (staticCaseType == Trial_CaseType);
    }

    public static boolean isBailCase(int staticCaseType) {
        return (staticCaseType == Bail_CaseType);
    }

    public static boolean isUndefinedCase(int staticCaseType) {
        return (staticCaseType == Undefined_CaseType);
    }

    public static boolean isCombinedCourtCase(int staticCaseType) {
        return (staticCaseType == Combined_CaseType);
    }

    public static int determineCaseType(String caseType, String caseSubType) throws UnknownCaseTypeException {
        if (caseType != null) {
            if (internalDebugCaseTypes)
                log.debug("Case Type=" + caseType);
            char caseTypeChar = caseType.toUpperCase().charAt(0);
            switch (caseTypeChar) {
            case 'A':
                if (caseSubType != null) {
                    switch (caseSubType.toUpperCase().charAt(0)) {
                    case 'S':
                    case 'C':
                    case 'B':
                        if (internalDebugCaseTypes)
                            log.debug("Case SubType=" + caseSubType.charAt(0));
                        return CriminalAppeal_CaseType;
                    case 'O':
                        return MiscelleanousAppeal_CaseType;
                    default:
                        if (internalDebugCaseTypes)
                            log.error("Case SubType=" + caseSubType.charAt(0)
                                    + " - this subtype value has not been coded");
                        throw new UnknownCaseTypeException("hearingtype.casesubtypeunknown",
                                new Object[] { caseSubType }, "Sub type not known:" + caseSubType);
                    }
                } else {
                    log.error("Case SubType == null - can not determine type of case");
                    throw new UnknownCaseTypeException("hearingtype.nocasesubtype", "sub type is null");
                }
            case 'B':
                return Bail_CaseType;
            case 'C':
                return Combined_CaseType;
            case 'S':
                return Sentence_CaseType;
            case 'T':
                return Trial_CaseType;
            case 'U':
                return Undefined_CaseType;
            default:
                log.error("caseType() == " + caseType + " - value not coded, assuming XHIBITConstant.Normal_CaseType");
                return Normal_CaseType;
            }
        } else {
            throw new UnknownCaseTypeException("hearingtype.nocasetype", "case type is null");
        }
    }

}