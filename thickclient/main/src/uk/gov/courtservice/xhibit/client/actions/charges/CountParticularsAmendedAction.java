package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventDialog;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class CountParticularsAmendedAction extends XAction {
    private static final String eventCode = "40104";

    public CountParticularsAmendedAction() {
        populateFromBundle("CountParticularsAmended");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        ChargesController chargesController;
        XhibitApplicationController xac;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        Integer seqNo = ccm.getOffenceValue().getCrestOffenceSeqNo();
        Integer indSeqNo = ccm.getChargeValue().getCrestChargeSeqNo();
        String caseNumber = ccm.getCCV().getCaseBasicValue().getCaseType()
                + ccm.getCCV().getCaseBasicValue().getCaseNumber();

        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        // String description = ccm.getOffenceValue().getOffenceDescription();
        // String freeText = seqNo.toString() + " " + description;
        String freeText = MessageFormat.format(XHIBITConstant
                .getResource(resources, "CountParticularsAmended.freeText"),
                new Object[] { seqNo, indSeqNo, caseNumber });

        SimpleEventModel model = new SimpleEventModel();

        String panelText = XHIBITConstant.getResource(resources, "CountParticularsAmended.panelText");
        model.setPanelText(panelText);
        model.setEventType(eventCode);
        model.setXac(xac);

        SimpleEventDialog sed = new SimpleEventDialog((java.awt.Frame) getController(), model);
        sed.getBodyPanel().setFreeText(freeText);
        sed.setVisible(true);

        if (sed.isOkClicked()) {
            // Log the offence amendment.
            CrestIndictmentLog.getInstance()
                    .countParticularsAmendedLog(
                            xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(),
                            ccm.getOffenceValue());

            // Refresh Charges tab
            chargesController.loadCharges();
        }
    }
}