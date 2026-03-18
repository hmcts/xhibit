package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 06-08-2003 AW Daley CountsSelectorPanel replaced with
 * DefendantsCountsTablePanel. Now handles input of CRN when adding a defendant
 * to a count.
 */

public class AddCountsToDefendantsPanel extends XPanel {
    // private static final String INVALID_MODEL_ERROR_TITLE =
    // "crn.invalidModelErrorTitle";
    private static final String INVALID_MODEL_ERROR_MESSAGE = "crn.invalidModelErrorMessage";

    private AddCountsToDefendants_Title topPanel;

    private DefendantsCountsTablePanel middlePanel;

    public ChargesControllerModel model; // child panels need access to

    // this.

    private Vector allCounts;

    private OffenceValue allCountValue;

    private Integer defendantID;

    private String defendantName;

    private boolean alreadyOnCount;

    private Vector indictmentNames;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private Insets defaultInsets = new Insets(8, 4, 8, 4);

    private OkCancelPanel okCancelPanel;

    public AddCountsToDefendantsPanel(OkCancelPanel buttonPanel) // Temp
    // method
    // for
    // testing
    {
        okCancelPanel = buttonPanel;

        stepInitialise();
        jbInit();
    }

    public AddCountsToDefendantsPanel(ChargesControllerModel model, OkCancelPanel buttonPanel)
            throws CSRecoverableException {
        this.model = model;
        okCancelPanel = buttonPanel;

        stepInitialise();
        jbInit();
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);
        this.add(getTopPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(getMiddlePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 20, 0));
    }

    private AddCountsToDefendants_Title getTopPanel() {
        if (topPanel == null) {
            topPanel = new AddCountsToDefendants_Title(this, defendantName, indictmentNames);
        }
        return topPanel;
    }

    public DefendantsCountsTablePanel getMiddlePanel() {
        if (middlePanel == null) {
            OffenceValue[] offencesNotOnCount = new OffenceValue[allCounts.size()];
            middlePanel = new DefendantsCountsTablePanel((OffenceValue[]) allCounts.toArray(offencesNotOnCount), model
                    .getCourtId(), okCancelPanel);
        }
        return middlePanel;
    }

    public void stepInitialise() {
        Vector charges;
        uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue chargeValue;
        Integer chargeID;

        charges = new Vector(model.getCCV().getCharges());
        indictmentNames = new Vector();

        for (int i = 0; i < charges.size(); i++) {
            chargeValue = (ChargeValue) charges.get(i);
            if (chargeValue.getChargeType().equals("I"))// perhaps need Charge
            // Type object
            {
                indictmentNames.addElement(chargeValue);
            }
        }

        chargeID = model.getChargeValue().getChargeID();
        allCounts = new Vector(model.getCCV().getOffenceValues(chargeID));
        defendantID = model.getDefendantValue().getDefendantID();
        defendantName = model.getDefendantValue().getFirstName() + " " + model.getDefendantValue().getSurName();

        okCancelPanel.getOkAction().setEnabled(false);

        stepUpdateViewState();
    }

    public void stepActivate() {
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // DefaultListModel defendantOnCountModel;
        model.setCancelClicked(false);

        if (update) {
            // generate CRNs if not already generated by apply
            if (!middlePanel.isCRNsGenerated())
                middlePanel.generateCRNs();

            Collection defendantsOnCount = null;
            Collection offences = null;

            // If the table model contains selections then get
            // DefendantOnOffence
            // Values from the table model.
            if (!middlePanel.getModel().isSelectionEmpty()) {
                // defendantsOnCount =
                // middlePanel.getModel().getDefenantOnOffenceValues(
                // model.getOffenceValue().getOffenceID());
                defendantsOnCount = middlePanel.getModel().getDefenantOnOffenceValues(
                        model.getDefendantValue().getDefendantID());

                offences = middlePanel.getModel().getSelectedValues();

                Iterator iter = defendantsOnCount.iterator();
                while (iter.hasNext()) {
                    /*
                     * DefendantOnOffenceValue value = (DefendantOnOffenceValue)
                     */iter.next();
                }

                LinkCountDefValue linkCountDefValue = new LinkCountDefValue();
                linkCountDefValue.setCaseID(model.getACM().getCaseId());
                linkCountDefValue.setCourtID(new Integer(model.getCourtId()));
                linkCountDefValue.setIsInCourt(model.isUserInCourtRoom());
                linkCountDefValue.setDefendantOnOffenceValues(defendantsOnCount);
                linkCountDefValue.setCourtLogDate(java.util.Calendar.getInstance());
                linkCountDefValue.setAddDefendantToCount(false);
                linkCountDefValue.setChargeType(model.getChargeValue().getChargeType());

                XhibitDelegateHelper.getChargeDelegate().linkCountsAndDefendants(linkCountDefValue,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

                // Log that counts have been assigned to a defendant.
                CaseBasicValue caseBasicValue = model.getACM().getScheduledHearingValue().getCaseBasicValue();
                CrestIndictmentLog.getInstance().addCountsToDefendantLog(caseBasicValue, offences,
                        model.getDefendantValue());
            } else {
                model.setCancelClicked(true);
            }
        }
    }

    public void stepUpdateViewState() {
    }

    /**
     * Validates the table model. If the CRN field is empty and Auto CRN is not
     * selected an error message will be displayed
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if (!getMiddlePanel().getModel().isValid()) {
            String errorMessage = ResourceBundleHelper
                    .getResource(XhibitBundles.ErrorText, INVALID_MODEL_ERROR_MESSAGE);

            throw new CSValidationException(INVALID_MODEL_ERROR_MESSAGE, errorMessage);
        }

        getMiddlePanel().stopEditingCurrentCell();
    }

    public DefaultListModel populateNotOnCountModel(Vector allCounts) {
        DefaultListModel notOnCountModel = new DefaultListModel();

        // create all count list model
        outerLoop: for (int i = 0; i < allCounts.size(); i++) {
            allCountValue = (OffenceValue) allCounts.get(i);

            Vector countDefendantIDs = new Vector(allCountValue.getDefendantIDs());
            alreadyOnCount = false;

            innerLoop: for (int j = 0; j < countDefendantIDs.size(); j++) {
                if (((Integer) countDefendantIDs.get(j)).intValue() == defendantID.intValue()) {
                    alreadyOnCount = true;
                    break innerLoop;
                }
            }

            if (alreadyOnCount == false) {
                notOnCountModel.addElement(allCountValue);
            }
        }
        return notOnCountModel;
    }
}