package uk.gov.courtservice.xhibit.client.schedule.othercase;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.CaseHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Open a case by case number performing the necessary security checks
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class OpenCaseHelper {

  

    private static uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue getLargestSHV(
            uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue shv1,
            uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue shv2) {
        return shv1.getScheduledHearingDate().getTime().compareTo(shv2.getScheduledHearingDate().getTime()) > 0 ? shv1
                : shv2;
    }

    /**
     * Search for a case and open if found open it in the passed in XAC.
     * 
     * @param xac
     * @param strCaseNumber
     * @param readOnly
     * @throws CSValidationException
     * @throws UserCancelException
     * @throws CSRecoverableException
     */
    public static void OpenCase(XhibitApplicationController xac, String strCaseNumber, boolean readOnly)
            throws CSValidationException, UserCancelException, CSRecoverableException {
        if (xac == null || strCaseNumber == null || strCaseNumber.trim().length() <= 0) {
            throw new IllegalArgumentException("Null passed in for XAC or case number");
        }
        CaseHelper ch = new CaseHelper();
        Collection listShv = ch.getScheduledHearings(strCaseNumber.substring(0, 1), new Integer(strCaseNumber
                .substring(1)), XhibitSingleton.getInstance().getCourtId());

        if (listShv == null)
            throw new CSValidationException("gui.search.casenotfound", new Object[] { strCaseNumber },
                    "Invalid case number entered: " + strCaseNumber);
        if (listShv.isEmpty())
            throw new CSValidationException("gui.search.casenotfound", new Object[] { strCaseNumber },
                    "Invalid case number entered: " + strCaseNumber);

        Iterator iter = listShv.iterator();
        uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue largestShv = null;
        while (iter.hasNext()) {
            uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue item = (uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue) iter
                    .next();
            if (largestShv == null) {
                largestShv = item;
            } else {
                largestShv = getLargestSHV(largestShv, item);
            }
        }

        Integer shvId = largestShv.getScheduledHearingID();
        ScheduledHearingValue[] ts_Shv = XhibitDelegateHelper.getHearingDelegate().getScheduledHearings(
                new Integer[] { shvId });

        if (ts_Shv != null && ts_Shv.length > 0) {
            // If update requested, and case is in another court.
            if (!readOnly && !XhibitSingleton.getInstance().isUserInCourtroom()
                    || !(ts_Shv[0].getCourtRoomId().equals(XhibitSingleton.getInstance().getCourtRoomId()))) {
                if (!FunctionList.hasAccess(FunctionList.ECourtLogOut)) {
                    CSValidationException csv = new CSValidationException("security.insufficientaccess.update",
                            new String[] { "a Court Log in another court room" },
                            "Attempting to access case out of court");
                    XHIBITConstant.handleError(csv);
                    readOnly = true;
                }

                // Check if case is in another court & update requested, prompt
                // user.
                if (!readOnly) {
                    int rc = JOptionPane.showConfirmDialog(xac, XHIBITConstant.getResource(
                            XhibitBundles.TodaysSchedule, "OpenUpdateMessage"), XHIBITConstant.getResource(
                            XhibitBundles.TodaysSchedule, "OpenUpdateTitle"), JOptionPane.YES_NO_OPTION);
                    if (rc != JOptionPane.YES_OPTION)
                        throw new UserCancelException();
                }
            }
            xac.openCase(ts_Shv[0], !readOnly);
        } else {
            throw new CSValidationException("gui.search.casenotfound", new Object[] { strCaseNumber },
                    "Invalid case number entered: " + strCaseNumber);
        }
    }
}