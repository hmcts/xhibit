package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;
import java.util.GregorianCalendar;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.Contract;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateDefendantDialog;
import uk.gov.courtservice.xhibit.client.util.Refreshable;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
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
 * @version 1.1 This version of OpemAmendDefendantAction provides for the two
 *          amend defendant scenarios. The second scenario is where there is a
 *          req to update the Def' data on Crest Form A.
 */

public class OpenAmendDefendantAction extends XAction {

    private static final long serialVersionUID = 1L;
    
    private boolean useDummyData = false;

    public OpenAmendDefendantAction() {
        populateFromBundle("OpenAmendDefendantAction");

        String useDummyDataIndicator = XHIBITConstant.getProperty(XhibitProperties.XhibitClientProject,
                "OpenAmendDefendantAction.useDummyData");
        if (useDummyDataIndicator != null) {
            if (useDummyDataIndicator.indexOf(XHIBITConstant.propertyNotFoundStringStart) != -1) {
                // nothing found to indicate we should use dummy data.
            } else {
                useDummyData = true;
                XHIBITConstant
                        .debug("OpenAmendDefendantAction.useDummyData property key found in "
                                + XhibitProperties.XhibitClientProject
                                + " property file - Update Defendant component NOT interacting with XHIBIT 2 MID Tier, rather using dummy data value!");
            }
        }
    }

    public void xActionPerformed(ActionEvent e) throws Exception {
        DefendantControllerBeanBusinessDelegate dcBD = XhibitDelegateHelper.getDefendantDelegate();
        UpdateDefendantModel udm = null;

        internalDebug = true;
        try {
            if (!useDummyData) {
                try {
                    // this is the 'normal scenario'
                    udm = (UpdateDefendantModel) this.getModel();
                    if (internalDebug) {
                        XHIBITConstant.debug("OpenAmendDefendantAction: the id of the defendant to update is: "
                                + udm.getDefendantID());
                        XHIBITConstant
                                .debug("OpenAmendDefendantAction: the case getCaseStatusValue().getCaseID() for this update is:  "
                                        + udm.getCaseStatusValue().getCaseID().toString());
                    }
                    DefendantValue dv = dcBD.getDefendantDetails(udm.getDefendantID(), udm.getCaseStatusValue()
                            .getCaseID());

                    if (dv == null) {
                        XHIBITConstant.error("OpenAmendDefendantAction: DefendantValue is null !!!!");
                    } else {
                        if (internalDebug)
                            XHIBITConstant.debug("OpenAmendDefendantAction: DefendantValue firstName is  "
                                    + dv.getFirstName());
                    }
                    udm.setDefendantValue(dv);
                    this.setModel(udm);
                } catch (Exception dce) {
                    dce.printStackTrace();
                    CSRecoverableException csre = new CSRecoverableException("key",
                            "OpenAmendDefendantAction: exception whilst getting the full set of defendant details.",
                            dce);
                    throw (csre);
                }

                if (udm.isCrestFormAFieldsOnly()) {
                    // do this bit extra, loading of defendant on case
                    try {
                        // this is the 'normal scenario'
                        udm = (UpdateDefendantModel) this.getModel();
                        if (internalDebug) {
                            XHIBITConstant.debug("OpenAmendDefendantAction: the id of the defendant to update is: "
                                    + udm.getDefendantID());
                            XHIBITConstant
                                    .debug("OpenAmendDefendantAction: the case getCaseStatusValue().getCaseID() for this update is:  "
                                            + udm.getCaseStatusValue().getCaseID().toString());
                        }
                        DefendantOnCaseValue dcv = dcBD.getDefendantOnCaseDetails(udm.getDefendantID(), udm
                                .getCaseStatusValue().getCaseID());

                        if (dcv == null) {
                            XHIBITConstant.error("OpenAmendDefendantAction: DefendantOnCaseValue is null !!!!");
                        } else {
                            if (internalDebug)
                                XHIBITConstant.debug("OpenAmendDefendantAction: DefendantCaeValue" + dcv.toString());
                        }
                        udm.setDefendantOnCaseValue(dcv);
                        this.setModel(udm);
                    } catch (Exception dce) {
                        Contract.fail(dce);
                    }
                }
            } else { // Using DUMMY Data...
                udm = (UpdateDefendantModel) this.getModel();
                XHIBITConstant.debug("Dummy updateDefendantModel=" + udm);
                if (udm == null) {
                    udm = new UpdateDefendantModel(new Integer(0), new Integer(0), true);
                    XHIBITConstant.debug("Dummy updateDefendantModel now=" + udm);
                }
                this.setModel(udm);
                // dummy data - put a meaning full UpdateDefendantModel in
                // this'model.
                DefendantValue dv = new DefendantValue(new Integer(0), new Integer(0), "", "", "", "",
                        new GregorianCalendar(), new Integer(0), new GregorianCalendar(),
                        new Integer(0), new AddressValue(), "N", null);
                udm.setDefendantValue(dv);
                XHIBITConstant
                        .debug("Using a dummy (and empty!) defendant as data - no mid tier involved here as the OpenAmendDefendantAction.useDummyData property key found in "
                                + XhibitProperties.XhibitClientProject + " property file!");
            }
            try {
                UpdateDefendantDialog x = new UpdateDefendantDialog(this);
                if ((x.isApplyClicked() || x.isOkClicked()) && x.stateChanged()) {
                    if (getController() instanceof XhibitApplicationController) {
                        XhibitApplicationController xac = (XhibitApplicationController) getController();
                        if (xac.getBodyPanel().getClass() == CourtLogController.class) {
                            CourtLogController clc = (CourtLogController) xac.getBodyPanel();
                            clc.refreshEvents();
                        }
                    }
                    if( getCaller() instanceof Refreshable ) {
                        ((Refreshable)getCaller()).refresh();
                    }
                }
            } catch (Exception eee) {
                eee.printStackTrace();
                CSRecoverableException csre = new CSRecoverableException("key",
                        "OpenAmendDefenantAction: Exception whilst instantiation of UpdateDendantDialog()", eee);
                throw (csre);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            CSRecoverableException csre = new CSRecoverableException("OpenAmendDefendantAction.xActionPerfomFailed",
                    "OpenAmendDefendantAction: Exception in xActionPerformed", ee);
            throw (csre);
        }
    }
}