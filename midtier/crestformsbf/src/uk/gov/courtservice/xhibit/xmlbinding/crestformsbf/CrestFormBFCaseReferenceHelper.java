package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.xmlbinding.util.CrestFormBFUtil;

/**
 * <p>
 * Title: Utility class for populating Case Reference in a Crest Form Schema.
 * </p>
 * <p>
 * Description: Case Reference made up of case_type + case_number
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
public class CrestFormBFCaseReferenceHelper {
    /**
     * Method populating an castor bound xml schema from an entity bean.
     * 
     * @param caseRef
     *            The castor bound Case object to populate.
     * @param doc
     *            The Defendant On Case entity bean to use for population.
     */
    public static void populateCaseReference(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Case caseRef,
            XhbDefendantOnCase doc) {
        // get the case type + case number
        String caseType = doc.getXhbCase().getCaseType();
        Integer caseNumber = doc.getXhbCase().getCaseNumber();
        // construct a string containing the case type and number in the correct
        // format
        String trueCaseNumber = caseType + CrestFormBFUtil.EIGHT_DIGIT.format(caseNumber);
        // set the case number for the supplied CFCaseReference castor object
        caseRef.setCaseReference(trueCaseNumber);
    }
}
