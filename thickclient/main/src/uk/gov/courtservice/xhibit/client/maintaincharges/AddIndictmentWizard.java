package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Wizard for creating a new Indictment.
 * </p>
 * <p>
 * Description: Wizard for creating a new Indictment using the
 * IndictmentDetailsPanel to gather the basic details and the AddedOffencesPanel
 * to get the first Count.
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

public class AddIndictmentWizard extends XWizardDialog implements BreachController {
    private static final boolean COUNT_REQUIRED_FOR_NEW_INDICTMENT = true;

    private static final int DETAILS_PANEL = 0;

    private static final int OFFENCE_PANEL = 1;

    private XhibitApplicationController xac;

    private IndictmentWizardModel model = new IndictmentWizardModel();

    private ChargesControllerModel ccm;

    private AddedOffencesPanel addedOffencesPanel;

    private IndictmentDetailsPanel indictmentDetailsPanel;

    private String wizardTitle;

    private String detailsTitle;

    private String offenceTitle;

    /**
     * Constructor to create the AddIndictmentWizard
     * 
     * @param ccm
     *            ChargesControllerModel which contains details of the state of
     *            the charges screen.
     * @throws CSRecoverableException
     */
    public AddIndictmentWizard(ChargesControllerModel ccm) throws CSRecoverableException {
        super(ccm.getACM().getXhibitApplicationController(), "", true);
        this.ccm = ccm;
        this.xac = ccm.getACM().getXhibitApplicationController();

        wizardTitle = ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, "addIndictmentWizardTitle");
        detailsTitle = wizardTitle + " - "
                + ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, "enterIndictmentDetailsTitle");
        offenceTitle = wizardTitle + " - "
                + ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, "addOffencesTitle");

        // Required if forcing the addition on offences/counts
        model.setOffenceRequired(COUNT_REQUIRED_FOR_NEW_INDICTMENT);

        model.setAddedOffences(new ArrayList<OffenceValue>());
        model.setCaseID(ccm.getACM().getCaseId());
        model.setCaseType(ccm.getACM().getCaseType());
        model.setChargeType(ChargeTypes.INDICTMENT);

        // create panels to add to wizard
        indictmentDetailsPanel = new IndictmentDetailsPanel(this, model);
        addedOffencesPanel = new AddedOffencesPanel(this, model);
        addedOffencesPanel.setPreferredSize(new Dimension(500, 200));

        // Array to contain all of the panels for the wizard. The panels will
        // be displayed in the wizard in the order that they are added to the
        // List.
        ArrayList al = new ArrayList();
        al.add(indictmentDetailsPanel);
        al.add(addedOffencesPanel);
        addBodyPanels(al);

        setWizardPanelImage("xwizardimage.jpg");
        pack();
    }

    /**
     * BreachController implementation of life cycle method, called when the the
     * user moves from one panel to another in the wizard to set the title and
     * enable/disable the wizard buttons.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() {
        // Need to check what the current screen is
        switch (currentPanel) {
        case DETAILS_PANEL:
            this.setTitle(detailsTitle);
            getButtonPanel().getBack().setEnabled(false);
            getButtonPanel().getNext().setEnabled(model.isIndictmentDetailsPopulated());

            if (addedOffencesPanel == null) {
                getButtonPanel().getFinish().setEnabled(false);
            } else {
                // enable if at least one offence exists.
                getButtonPanel().getFinish().setEnabled(addedOffencesPanel.getOffenceCount() > 0);
            }
            break;

        case OFFENCE_PANEL:
            this.setTitle(offenceTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            if (addedOffencesPanel == null) {
                getButtonPanel().getFinish().setEnabled(false);
            } else {
                if (addedOffencesPanel.getOffenceCount() > 0) {
                    getButtonPanel().getFinish().setEnabled(true);
                    getButtonPanel().getFinish().requestFocus();
                } else {
                    getButtonPanel().getFinish().setEnabled(false);
                }
            }
            break;
        }
    }

    /**
     * BreachController implementation that is executed when the wizard is
     * closing after the Finish action is fired.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeinitialise() throws CSRecoverableException {
        ChargeValue chargeValue = new ChargeValue();
        chargeValue.setCaseID(ccm.getACM().getCaseId());
        chargeValue.setCourtID(new Integer(ccm.getCourtId()));
        chargeValue.setCourtLogDate(Calendar.getInstance());
        chargeValue.setChargeType(ChargeTypes.INDICTMENT);
        chargeValue.setDateIndRec(model.getIndReceivedDate());
        chargeValue.setProsPaperServedDate(model.getProsPapersServedDate());

        // Setting the Indictment Responsibility to null, means Mercator will
        // get the correct value from CREST.
        chargeValue.setIndResp(null);

        // offenceValue collection
        if (model.getAddedOffences() != null && model.getAddedOffences().size() > 0) {
            // Need to set offences as a vector not arraylist
            List offenceList = model.getAddedOffences();
            Integer seqNo = null;
            for (int i = 0; i < offenceList.size(); i++) {
                seqNo = new Integer(i + 1);
                ((OffenceValue) offenceList.get(i)).setCrestOffenceSeqNo(seqNo);
            }

            // SG - must be Vector else get ClassCastException in midtier
            chargeValue.setOffenceValues(new Vector(model.getAddedOffences()));
        }

        ccm.getDelegate().addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
        		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

        CrestIndictmentLog.getInstance().addIndictmentLog(
                xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(), chargeValue);
    }

    /**
     * BreachController implementation that returns the
     * XhibitApplicationController i.e. the main application.
     * 
     * @return the XhibitApplicationController.
     */
    public XhibitApplicationController getXac() {
        return xac;
    }
}