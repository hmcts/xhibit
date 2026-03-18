package uk.gov.courtservice.xhibit.client.maintaincharges.log;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Indictment Log Helper.
 * </p>
 * <p>
 * Description: IndictmentLogHelper provides helper methods for the Crest
 * Indictment Log.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class IndictmentLogHelper {

    /**
     * private constructor as all methods are static.
     */
    private IndictmentLogHelper() {
    }

    /**
     * Updates the supplied CaseBasicValue with the supplied log text.
     * 
     * @param caseBasicValue
     *            the CaseBasicValue to be updated.
     * @param logText
     *            the text to be added to the Crest indictment log.
     * @throws CSRecoverableException
     *             if the call to update the case on the mid-tier fails.
     */
    public static void updateIndictmentInfo(CaseBasicValue caseBasicValue, String logText)
            throws CSRecoverableException {
        CrestIndictmentLog.getInstance().setLog(caseBasicValue, logText);
        updateCase(caseBasicValue);
    }

    /**
     * Updates the supplied CaseBasicValue to the mid-tier.
     * 
     * @param caseBasicValue
     *            the CaseBasicValue to be updated.
     * @throws CSRecoverableException
     *             if can't get the CaseControllerBusinessDelegate or the call
     *             on it to update the case fails.
     */
    private static void updateCase(CaseBasicValue caseBasicValue) throws CSRecoverableException {
        try {
            XhibitDelegateHelper.getCaseDelegate().updateCase(caseBasicValue,
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        } catch (Exception ex) {
            throw new CSRecoverableException("gui.user.CrestIndictmentLogDialog.update",
                    "gui.log.CrestIndictmentLogDialog.update", ex);
        }
    }
}