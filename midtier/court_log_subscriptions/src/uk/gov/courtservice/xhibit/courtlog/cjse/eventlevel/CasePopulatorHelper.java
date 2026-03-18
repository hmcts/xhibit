package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

// jdk
import java.util.HashSet;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.CaseFileIDs;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;

/**
 * <p>
 * Title: CasePopulatorHelper
 * </p>
 * <p>
 * Description:Helper class used by the CaseLevelPopulator for EventParameter
 * population of the Case level attributes i.e. CaseFileID
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author S Sangha
 * @version 1.0
 */

public class CasePopulatorHelper {
    private static final Logger log = CSServices.getLogger(CasePopulatorHelper.class);

    // Case Reference details
    private HashSet _caseFileIDs = new HashSet();

    public CasePopulatorHelper() {
    }

    /**
     * Populates the case level attriubtes.
     * 
     * @param eventParameters
     *            The event parameters to populate.
     * @param caseIds
     *            The case Ids to poulate for
     */
    public void populateCaseAttributes(EventParameters eventParameters, Integer[] caseIds) {
        logStart(caseIds);

        // check event parameters have been passed in
        if (eventParameters == null) {
            throw new IllegalArgumentException("An EventParameters instance must be passed to the "
                    + "populateCaseAttributes() method");
        }

        // check at least one caseId has been passed in
        if (caseIds == null || caseIds.length == 0) {
            return;
        }

        // populate caseFileIds for all cases
        for (int i = 0; i < caseIds.length; i++) {
            log.debug("populateCaseAttributes() processing case: " + caseIds[i]);
            XhbCase theCase = XhbCaseBeanHelper2.findByPrimaryKey(caseIds[i]);
            _caseFileIDs.add(getCaseFileID(theCase));
        }

        copyAttribuesToEventParameters(eventParameters);
    }

    /**
     * Populates the case level attriubtes.
     * 
     * @param eventParameters
     *            The event parameters to populate.
     * @param aCase
     *            An XhbCase
     */
    public void populateCaseAttributes(EventParameters eventParameters, XhbCase aCase) {
        _caseFileIDs.add(getCaseFileID(aCase));
        copyAttribuesToEventParameters(eventParameters);
    }

    // ---------------------------- Private Methods
    // -----------------------------//

    // log start of method
    private void logStart(Integer[] caseIds) {
        if (log.isDebugEnabled()) // check debug is on before executing
        // this loop
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < caseIds.length; i++) {
                sb.append(caseIds[i] + " ");
            }
            log.debug("populateCaseAttributes() with caseIds: " + sb);
        }
    }

    // copies CaseFileID(s) to the EventParameters
    private void copyAttribuesToEventParameters(EventParameters eventParameters) {
        // remove any null values in the HashSet
        _caseFileIDs.remove(null);
        _caseFileIDs.remove("");
        // ensure at least one caseFileID exists
        if (_caseFileIDs.size() > 0) {
            CaseFileIDs theCaseFileIDs = new CaseFileIDs();
            // convert to a String
            theCaseFileIDs.setCaseFileID((String[]) _caseFileIDs.toArray(new String[] {}));
            // set as event parameters
            eventParameters.setCaseFileIDs(theCaseFileIDs);
        }
    }

    // determine the case title, based on the case type and case number
    private String getCaseFileID(XhbCase theCase) {
        String caseFileID = theCase.getCaseType() + theCase.getCaseNumber();
        return caseFileID;
    }
}