package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLogDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: Crest Indictment Log Action
 * </p>
 * <p>
 * Description: Will display a dialog for viewing/editting indictment log
 * information.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public class CrestIndictmentLogAction extends XAction {
    ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.IndictmentLogResources);

    public CrestIndictmentLogAction() {
        populateFromBundle("CrestIndictmentLog");
    }

    public void xActionPerformed(ActionEvent e) {
        try {
            XhibitApplicationController xac = (XhibitApplicationController) getController();

            CaseBasicValue currentCase = xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue();

            CaseBasicValue latestCase = XhibitDelegateHelper.getCaseDelegate().getCase(currentCase.getId());

            if (ChargesControllerHelper.canCaseBeUpdated(latestCase)) {
                if (latestCase.getVersion().intValue() > currentCase.getVersion().intValue()) {
                    currentCase = latestCase;
                }

                // do we have indictments on each latestCase, if so add.
                if ((latestCase.getIndictmentLog() !=null) && 
                    (latestCase.getIndictmentLog().size() >0)) {
                    currentCase.setIndictmentLog(latestCase.getIndictmentLog());
                }
                // end
                
                String logText = CrestIndictmentLog.getInstance().getLatestLogStoredInMemory(currentCase);

                CrestIndictmentLogDialog dialog = new CrestIndictmentLogDialog(xac, currentCase, logText, 400, 400);

                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog((Component) getController(), XHIBITConstant.getResource(resources,
                        "crest.updating.message"), XHIBITConstant.getResource(resources, "crest.updating.title"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        // Deal with different exceptions here...Deal with all for now
        catch (Exception ex) {
            XHIBITConstant.handleError(ex);
        }
    }
}