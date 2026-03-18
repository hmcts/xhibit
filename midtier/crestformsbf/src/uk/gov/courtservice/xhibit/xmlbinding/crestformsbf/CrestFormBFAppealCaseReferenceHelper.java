package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReason;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReasonBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.xmlbinding.util.CrestFormBFUtil;

/**
 * <p>
 * Title: Utility class for populating Case Reference in a Crest Form Schema.
 * </p>
 * <p>
 * Description: Case Reference made up of case_type + case_number Also populate
 * ReasonForAppeal and CaseTitle from xhb_case
 * </p>
 * <p>
 * This class populates castor bound xml objects from entity beans.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */
public class CrestFormBFAppealCaseReferenceHelper {

    protected static Logger log = CSServices.getLogger(CrestFormBFAppealCaseReferenceHelper.class);

    /**
     * Method populating an castor bound xml schema from an entity bean.
     * 
     * @param appealCaseRef
     *            The castor bound Case object to populate.
     * @param doc
     *            The Defendant On Case entity bean to use for population.
     */
    public static void populateAppealCaseReference(
            uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.AppealCase appealCaseRef, XhbDefendantOnCase doc) {
        // get the case type + case number
        String caseType = doc.getXhbCase().getCaseType();
        Integer caseNumber = doc.getXhbCase().getCaseNumber();
        // construct a string containing the case type and number in the correct
        // format
        String trueCaseNumber = caseType + CrestFormBFUtil.EIGHT_DIGIT.format(caseNumber);
        // set the case number for the supplied AppealCase castor object
        appealCaseRef.setCaseReference(trueCaseNumber);

        // get the case reason for appeal and case title
        String reasonForAppeal = getJudgeReasonText(doc.getCaseId());

        String caseTitle = doc.getXhbCase().getCaseTitle();

        String caseDesc = doc.getXhbCase().getCaseDescription();

        // set up default values
        appealCaseRef.setReasonForAppeal(" ");
        appealCaseRef.setCaseTitle(" ");
        appealCaseRef.setCaseDesc(" ");

        // set the reason for appeal
        if (reasonForAppeal.length() > 0) {
            appealCaseRef.setReasonForAppeal(reasonForAppeal);
        }

        // set the case title
        if (caseTitle != null) {
            appealCaseRef.setCaseTitle(caseTitle);
        }

        // set up the case description
        if (caseDesc != null) {
            appealCaseRef.setCaseDesc(caseDesc);
        }
    }

    private static String getJudgeReasonText(Integer caseId) {
        Collection reasons = XhbCaseAppReasonBeanHelper2.findCurrentByCaseId(caseId);
        StringBuffer reasonsText = new StringBuffer();
        for (Iterator it = reasons.iterator(); it.hasNext();) {
            XhbCaseAppReason judgeReason = (XhbCaseAppReason) it.next();
            reasonsText.append(judgeReason.getAppReason());
            reasonsText.append(" ");
        }
        return reasonsText.toString();
    }
}
