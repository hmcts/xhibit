package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingListSummaryValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.LinkedHearingRecordValues;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: HearingRecordControllerHelper
 * </p>
 * <p>
 * Description: The controller helper. Mainly used for print form A.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */
public class HearingRecordControllerHelper {

    public HearingRecordControllerHelper() {
    }

    public String[] formatHearingRecordForPrinting(HearingRecordModel model) throws UserCancelException,
            CSRecoverableException {
        String[] xslfo = new String[] {};

        // PRE00090 - ensure we use the main frame
        PrintHearingRecordDialog phrd = new PrintHearingRecordDialog(model.getXac(), model);
        phrd.setVisible(true);

        if (phrd.isCancelClicked()) {
            throw new UserCancelException();
        }

        HearingRecordValue hearingRecordVal;
        LinkedHearingRecordValues linkedHrValues = new LinkedHearingRecordValues();
        Collection c = new ArrayList();

        // Print only the one that is selected
        if (phrd.isOkClicked() && model.getHearingsToPrint() == HearingRecordModel.PRINT_SELECTED) {
            hearingRecordVal = getHSCDelegate().retrieveHearingRecord(model.getHearingId(), model.getDefendantId(),
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

            // Set the court clerk to the one from the session if not
            // populated
            if (hearingRecordVal.getCourtClerkExporter() == null
                    || hearingRecordVal.getCourtClerkExporter().equalsIgnoreCase("")
                    || HearingRecordConstants.EXPORT_FAILED.equals(hearingRecordVal.getExportStatus())) {
                hearingRecordVal.setCourtClerkExporter(XhibitSingleton.getInstance().getUserSession().getUserName());
            }
            c.add(hearingRecordVal);
        }
        // Print all linked and all for all defendants.
        else if (phrd.isOkClicked() && model.getHearingsToPrint() == HearingRecordModel.PRINT_ALL) {
            for (int i = 0; i < model.getHearingSummaryVal().getHearingListSummaryValues().size(); i++) {
                HearingListSummaryValue hlsVal = (HearingListSummaryValue) ((ArrayList) model.getHearingSummaryVal()
                        .getHearingListSummaryValues()).get(i);

                for (int j = 0; j < hlsVal.getHrDefendantValues().size(); j++) {
                    HRDefendantValue hrdVal = (HRDefendantValue) ((ArrayList) hlsVal.getHrDefendantValues()).get(j);

                    hearingRecordVal = getHSCDelegate().retrieveHearingRecord(hlsVal.getHearingID(),
                            hrdVal.getDefendantID(),
                            XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

                    // Set the court clerk to the one from the session if
                    // not populated
                    if (hearingRecordVal.getCourtClerkExporter() == null
                            || hearingRecordVal.getCourtClerkExporter().equalsIgnoreCase("")
                            || HearingRecordConstants.EXPORT_FAILED.equals(hearingRecordVal.getExportStatus())) {
                        hearingRecordVal.setCourtClerkExporter(XhibitSingleton.getInstance().getUserSession()
                                .getUserName());
                    }
                    c.add(hearingRecordVal);
                }
            }
        }

        linkedHrValues.setHearingRecordValues(c);

        /* TEMP CODE START */
        // Test to print and save the xml to file.
        // TestPrintFormA test = new TestPrintFormA();
        // test.print(linkedHrValues);
        /* TEMP CODE END */

        xslfo = getFormattedDocument(linkedHrValues);

        // Return the XSL formatting object
        return xslfo;
    }

    // extracted from formatHearingRecordForPrinting
    // made it protected so it can be tested
    protected String[] getFormattedDocument(LinkedHearingRecordValues linkedHrValues) throws CSRecoverableException {
        // Pass the returned data to the Print Controller to create an XSL
        // formatting object
        // (this was previously inside a try/catch, catching 'Exception' and
        // ignoring it -
        // dangerous - removed the try/catch so exceptions are at least thrown)
        return new String[] { CSServices.getPrintServices().getFormattedDocument(linkedHrValues, Locale.getDefault()) };
    }

    private HearingScheduleControllerBeanBusinessDelegate getHSCDelegate() {
        return XhibitDelegateHelper.getHearingDelegate();
    }

}