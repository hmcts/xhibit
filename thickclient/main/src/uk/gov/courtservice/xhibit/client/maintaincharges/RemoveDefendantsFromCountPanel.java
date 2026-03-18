package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:Remove Defendants Panel
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Krishna
 * @version 1.0
 */

public class RemoveDefendantsFromCountPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private RemoveDefendantsFromCount_Title topPanel;

    private RemoveDefendantsPanel middlePanel;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private Insets defaultInsets = new Insets(8, 4, 8, 4);

    private RemoveDefendantsFromCountDialog parent;

    private OkCancelPanel okCancelPanel;

    protected ChargesControllerModel model;

    ChargeValue indictment;

    private Collection defandants;

    private Integer scheduledHearingId;

    /**
     * Creates an add defendants to offence panel
     * 
     * @param parent
     *            the controlling AddDefendantsToOffenceDialog
     * @param model
     *            the ChargesControllerModel
     * @param buttonPanel
     *            the OkCancelPanel
     * @param notOnOffence
     *            List of defenants to be displayed for selection.
     * @throws CSRecoverableException
     */
    public RemoveDefendantsFromCountPanel(RemoveDefendantsFromCountDialog parent, ChargesControllerModel model,
            OkCancelPanel okCancelPanel, Collection defandants, Integer scheduledHearingId)
            throws CSRecoverableException {

        if (parent == null || model == null || okCancelPanel == null || defandants == null) {
            throw new IllegalArgumentException("RemoveDefendantsFromCountPanel - parent, model, okCancelPanel  and "
                    + " defandants parameters must contain values.");
        }

        this.parent = parent;
        this.model = model;
        this.okCancelPanel = okCancelPanel;

        this.defandants = defandants;
        this.scheduledHearingId = scheduledHearingId;
        stepInitialise();
        init();
    }

    private void init() throws CSRecoverableException {
        this.setLayout(gridBagLayout1);
        this.add(getTopPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));

        this.add(getMiddlePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 20, 0));
    }

    private RemoveDefendantsFromCount_Title getTopPanel() {
        if (topPanel == null) {
            topPanel = new RemoveDefendantsFromCount_Title(this, parent.xac, model.getOffenceValue());
        }
        return topPanel;
    }

    private RemoveDefendantsPanel getMiddlePanel() {
        if (middlePanel == null) {
            middlePanel = new RemoveDefendantsPanel(this, model, defandants);
        }
        return middlePanel;
    }

    /**
     * XPanel implementation to get data required for the screen.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        okCancelPanel.getOkAction().setEnabled(false);
        indictment = model.getChargeValue();
        stepUpdateViewState();
    }

    private void moveModelToScreen() throws CSRecoverableException {
        getTopPanel().moveModelToScreen();
        getMiddlePanel().stepActivate();
    }

    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();
    }

    public void stepDeactivate() throws ChargeControllerException {
        // No screen information is moving to VOs or model at this stage
        Vector<SelectDefendantsToRemoveRowModel> defendantDetails = middlePanel.getDefendantDetails();
        Iterator<SelectDefendantsToRemoveRowModel> iter = defendantDetails.iterator();
        SelectDefendantsToRemoveRowModel row;
        while (iter.hasNext()) {
            row = iter.next();
            row.printModel();
            if (row.isRemoveFromCount().booleanValue() == true) {
                Integer defendantId = row.getDefendantId();
                XhbDefendantOnOffenceBasicValue dobv = model.getOffenceValue().getDefendantOnOffence(defendantId);
                // set the obsind to Y
                dobv.setObsInd("Y");
                XhbDefendantOnOffenceBasicValue[] defOnOffenceBasicValues = new XhbDefendantOnOffenceBasicValue[1];
                defOnOffenceBasicValues[0] = dobv;
                if (dobv.getIsStayed() != null && dobv.getIsStayed().equals("Y")) {
                    JOptionPane.showMessageDialog(parent.getXac(),
                            getString("removeDefendantsFromCount.stayedstatus.cannotremove.msg"),
                            getString("removeDefendantsFromCount.title"), JOptionPane.INFORMATION_MESSAGE);
                } else {                    
                    LinkCountDefValue linkCountDefValue = new LinkCountDefValue();
                    linkCountDefValue.setCaseID(model.getACM().getCaseId());
                    linkCountDefValue.setCourtID(new Integer(model.getCourtId()));
                    linkCountDefValue.setIsInCourt(model.isUserInCourtRoom());
                    linkCountDefValue.setCourtLogDate(Calendar.getInstance());
                    linkCountDefValue.setAddDefendantToCount(false);
                    //linkCountDefValue.setChargeType(getChargeType());
                    DefendantOnOffenceValue defendantOnOffenceValue = new DefendantOnOffenceValue(model.getOffenceValue().getOffenceID(), defendantId, null);
                    defendantOnOffenceValue.setObsInd("Y");
                    Collection<DefendantOnOffenceValue> defendantsOnOffence = new ArrayList<DefendantOnOffenceValue>();
                    defendantsOnOffence.add(defendantOnOffenceValue);
                    linkCountDefValue.setDefendantOnOffenceValues(defendantsOnOffence);                    
                    XhibitDelegateHelper.getChargeDelegate().updateDefendantOnOffence(linkCountDefValue,
                    		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                    XhibitDelegateHelper.getChargeDelegate().addRemoveDefendantsFromCountLog(defOnOffenceBasicValues,
                            getCRUDValue(model.getChargeValue().getCaseID()));
                }
            }
        }
    }

    /**
     * XPanel implementation called by the controlling dialog when it is closing
     * 
     * @param update
     *            true if the screen is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        
        if(update){

            //Write to CRESTIndictmentLog (RFC2867)
            Vector<SelectDefendantsToRemoveRowModel> defendantDetails = middlePanel.getDefendantDetails();
            Iterator<SelectDefendantsToRemoveRowModel> iter = defendantDetails.iterator();
            SelectDefendantsToRemoveRowModel row;
            while (iter.hasNext()) {
                row = iter.next();
                if (row.isRemoveFromCount().booleanValue() == true) {
                    String defendantName = row.getDefendantName();
                    CaseBasicValue caseBasicValue = model.getACM().getScheduledHearingValue().getCaseBasicValue();
                    CrestIndictmentLog.getInstance().removeDefendantsFromCountLog(caseBasicValue, defendantName,
                            model.getOffenceValue(), model.getChargeValue());
                }else{
                    // Nothing to do here
                    }
                }
        }
    }

    public void stepUpdateViewState() {
        okCancelPanel.getOkAction().setEnabled(getMiddlePanel().isAtleastOneDefendantSelected());
    }

    /**
     * Validates the table model.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        //Empty
    }

    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.RemoveFromCountResources, key);
    }

    public CourtLogCRUDValue getCRUDValue(Integer caseId) {
        CourtLogCRUDValue cv = new CourtLogCRUDValue();
        cv.setCaseId(caseId);
        cv.setScheduledHearingId(scheduledHearingId);
        // populate the event id
        cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
        return cv;
    }

}