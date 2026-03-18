package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.client.delegate.CSBusinessDelegateFactory;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0 testing the merge
 */

public class UpdateDefendantAction extends XAction {
    private UpdateDefendantModel udm;

    public UpdateDefendantAction() {
        populateFromBundle("UpdateDefendantAction");
    }

    public void xActionPerformed(ActionEvent e) {
        DefendantControllerBeanBusinessDelegate dcBD = XhibitDelegateHelper.getDefendantDelegate();
        DefendantValue defendantValue = null;
        CSBusinessDelegateFactory fac = null;
        CaseStatusValue caseStatusValue = null;

        try {
            try {
                udm = (UpdateDefendantModel) this.getModel();

                XHIBITConstant.debug("UpdateDefendantAction: id of the defendant to store is: " + udm.getDefendantID());
                XHIBITConstant.debug("UpdateDefendantAction: id of the related case is: "
                        + udm.getCaseStatusValue().getCaseID());
                XHIBITConstant.debug("UpdateDefendantAction: in court boolean of case is: "
                        + udm.getCaseStatusValue().isInCourt());
                XHIBITConstant.debug("UpdateDefendantAction: DefendantValue is: " + udm.getDefendantValue().toString());

                defendantValue = udm.getDefendantValue();
                if (defendantValue == null) {
                    XHIBITConstant
                            .error("UpdateDefendantAction: DefendantValue is null for id " + udm.getDefendantID());
                } else {
                    XHIBITConstant.debug("UpdateDefendantAction: DefendantValue firstName is  "
                            + defendantValue.getFirstName());
                }
                caseStatusValue = udm.getCaseStatusValue();
                if (caseStatusValue == null) {
                    XHIBITConstant.error("UpdateDefendantAction: caseStatusValue is null!");
                } else {
                    XHIBITConstant.debug("UpdateDefendantAction: caseStatusValue is: "
                            + udm.getCaseStatusValue().toString());
                }
            } catch (Exception dce) {
                CSRecoverableException csre = new CSRecoverableException(
                        "gui.updateDefendantActtion.defendantvaluecreation",
                        "Exception whilst creating the DefendantValue object", dce);
                XHIBITConstant.handleError(csre);
            }
           
            try {
                XHIBITConstant.debug("@@@@@@@@@@@@@@@@@"
                        + ((XhibitApplicationController) this.getController()).getBodyPanel().getClass().getName());

                if (((XhibitApplicationController) this.getController()).getBodyPanel() instanceof CourtLogController) {
                    XHIBITConstant
                            .debug("The CourtLog is showing and will now be updated to reflect any defendant changes");
                    ((CourtLogController) ((XhibitApplicationController) this.getController()).getBodyPanel())
                            .stepInitialise();
                    ((CourtLogController) ((XhibitApplicationController) this.getController()).getBodyPanel())
                            .stepActivate();

                    XHIBITConstant.debug("The CourtLog has been refreshed");

                }
            } catch (Exception dce) {

                XHIBITConstant.error("Trouble updating the currently open window(s) with the defendant changes");

                // CSRecoverableException csre = new
                // CSRecoverableException("gui.updateDefendantActtion.defendantvalueupdate",
                // "Exception whilst saving the DefendantValue object to the mid
                // tier", dce);
                // XHIBITConstant.handleError(csre);
            }

        } catch (Exception CSrue) {
            CSRecoverableException csre = new CSRecoverableException("", "", CSrue);
            XHIBITConstant.handleError(csre);
        } finally {
            XHIBITConstant.debug("UpdateDefendantAction: end.");
        }
    }
}