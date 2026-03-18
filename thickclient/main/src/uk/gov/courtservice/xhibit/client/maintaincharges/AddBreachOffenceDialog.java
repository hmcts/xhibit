package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:AddBreachOffenceDialog
 * </p>
 * <p>
 * Description: Dialog that allows the user to input a Common Reference Number
 * (CRN) or allows the user to automatically generate the CRN when adding an
 * offence to a breach
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 04-09-2003 AW Daley Initial Version based on AddBreachWizardDialog
 */
public class AddBreachOffenceDialog extends XDialog implements BreachController {
    private static final Logger LOG = CSServices.getLogger(AddBreachOffenceDialog.class);

    private final static String NO_DEFENDANT_USER_MSG = "gui.user.addbreachoffence.nodefendant";

    private final static String NO_DEFENDANT_LOG_MSG = "gui.log.addbreachoffence.nodefendant";

    private XhibitApplicationController xac;

    private BreachValue breachValue;

    BreachWizardModel model = new BreachWizardModel();

    private ChargesControllerModel ccm;

    private AddedOffencesToBreachPanel addedOffencesPanel;

    /**
     * Creates the Add Breach Offence Dialog
     * 
     * @param ccm
     *            ChargesControllerModel containing data retrieved by the
     *            Charges Controller.
     * @throws CSRecoverableException
     */
    public AddBreachOffenceDialog(ChargesControllerModel ccm) throws CSRecoverableException {
        super(ccm.getACM().getXhibitApplicationController(), "", true);
        
        this.ccm = ccm;
        this.xac = ccm.getACM().getXhibitApplicationController();

        super.setTitle(ResourceBundleHelper.getResource(XhibitBundles.Breaches, "addBreachOffenceDialogTitle"));

        breachValue = ccm.getBreachValue();

        model.setBreachValue(breachValue);
        model.setAddedOffences(new ArrayList<OffenceValue>());
        model.setCaseID(ccm.getACM().getCaseId());
        model.setCaseType(ccm.getACM().getCaseType());
        model.setChargeType(ChargeTypes.BREACH);
        model.setChargeID(ccm.getChargeValue().getChargeID());
        model.setCourtId(XhibitSingleton.getInstance().getCourtId());
        model.setDefendantID(ccm.getChargeValue().getDefendantID());
        model.setDefendantOnCaseID(ccm.getDefendantValue().getDefOnCaseBasicValue().getId());
        
        addedOffencesPanel = new AddedOffencesToBreachPanel((BreachController) this, model);

        addBodyPanel(addedOffencesPanel);
        sizeAndPosition();
    }

    /**
     * BreachController implementation - not used here.
     */
    public void stepUpdateViewState() {
    }

    /**
     * BreachController implementation called when the dialog is being closed
     * when the OK action is fired.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeinitialise() throws CSRecoverableException {
        // offenceValue collection
        if (model.getAddedOffences() != null && model.getAddedOffences().size() > 0) {
            Iterator i = model.getAddedOffences().iterator();
            while (i.hasNext()) {
                OffenceValue offenceValue = (OffenceValue) i.next();
                Vector defendants = new Vector();
                if (ccm.getChargeValue().getDefendantID() == null) {
                    LOG.debug("Breach does not have a defendant. Defendant id = null");
                    throw new CSRecoverableException(NO_DEFENDANT_USER_MSG, NO_DEFENDANT_LOG_MSG);
                } else {
                    defendants.add(ccm.getChargeValue().getDefendantID());
                }
                offenceValue.setDefendantIDs(defendants);

                ccm.getDelegate().addOffence(offenceValue,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }
        }
    }

    /**
     * Sets the size and position of the dialog.
     */
    private void sizeAndPosition() {
        this.setSize(500, 400);
        this.centreDialog();
    }

    /**
     * BreachController implementation that gets a reference to the main
     * application.
     * 
     * @return XhibitApplicationController
     */
    public XhibitApplicationController getXac() {
        return xac;
    }
}