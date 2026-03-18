package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeType;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
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
 * Description: Panel to assign and record additional defendant on offence
 * information
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class AddDefendantsToOffencePanel extends XPanel {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(AddDefendantsToOffencePanel.class);
    
    private AddDefendantsToOffence_Title topPanel;

    private DefendantOffenceDetails middlePanel;

    private SelectDefendantOnOffencePanel bottomPanel;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private Insets defaultInsets = new Insets(8, 4, 8, 4);

    private AddDefendantsToOffenceDialog parent;

    private OkCancelPanel okCancelPanel;

    protected ChargesControllerModel model;

    private Collection notOnOffence;

    private DefaultComboBoxModel defendantsModel;

    private boolean booLinkCountDefValue;

    private ChargesControllerHelper.MODE mode;

    private String caseType;
    
    private String receiptType;

    private DefendantOnOffenceComplexValue defOnOffComplexValue;
    
    /**
     * Creates an add defendants to offence panel
     * 
     * @param parent
     *            the controlling AddDefendantsToOffenceDialog
     * @param model
     *            the ChargesControllerModel
     * @param buttonPanel
     *            the OkCancelPanel
     * @param booLinkCountDefValue
     *            true if linking a defendant to an existing offence. False if
     *            linking a defendant to a new offence.
     * @param mode
     *            Add or Edit mode.
     * @param caseType
     *            Type of Case - Trial or other.
     * @param notOnOffence
     *            List of defenants to be displayed for selection.
     * @throws CSRecoverableException
     */
    public AddDefendantsToOffencePanel(AddDefendantsToOffenceDialog parent, ChargesControllerModel model,
            OkCancelPanel okCancelPanel, boolean booLinkCountDefValue, ChargesControllerHelper.MODE mode,
            Collection notOnOffence) throws CSRecoverableException {
        
        if (parent == null || model == null || okCancelPanel == null || mode == null || notOnOffence == null){
            throw new IllegalArgumentException 
            ("AddDefendantsToOffencePanel - parent, model, okCancelPanel, mode and " +
                    " notOnOffence parameters must contain values.");
        }
        
        this.parent = parent;
        this.model = model;
        this.okCancelPanel = okCancelPanel;
        this.booLinkCountDefValue = booLinkCountDefValue;
        this.mode = mode;
        this.caseType = model.getACM().getCaseType();
        try {
        	this.receiptType = model.getACM().getScheduledHearingValue().getCaseBasicValue().getReceiptType();
        } catch (NullPointerException npe) {
        	// Shouldnt really happen as we have a case!!!
        	npe.printStackTrace();
        }
        this.notOnOffence = notOnOffence;
        stepInitialise();
        init();
    }

    private void init() {
        this.setLayout(gridBagLayout1);
        this.add(getTopPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        
        if (model.getChargeValue() != null 
                && model.getChargeValue().getChargeType() != null 
                && model.getChargeValue().getChargeType().equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
            // Defendant offence details are not applicable to fail to appear charge types
        } else {
            this.add(getMiddlePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 20, 0));
        }
        
        this.add(getBottomPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 20, 0));
    }

    private AddDefendantsToOffence_Title getTopPanel() {
        if (topPanel == null) {
            topPanel = new AddDefendantsToOffence_Title(this, parent.xac, model.getOffenceValue());
        }
        return topPanel;
    }

    private DefendantOffenceDetails getMiddlePanel() {
        if (middlePanel == null) {
            middlePanel = new DefendantOffenceDetails(this, false, mode, caseType, defOnOffComplexValue);
        }
        return middlePanel;
    }

    private SelectDefendantOnOffencePanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new SelectDefendantOnOffencePanel(this, getComboBoxModel(), mode, defOnOffComplexValue, model
                    .getDefendantValue(), model.getAllDefendantsOnCaseSeqNosSortedMap(), caseType, receiptType);
        }
        return bottomPanel;
    }

    /**
     * Creates a combobox model based on the unselected defendants for the
     * 
     * @return
     */
    public DefaultComboBoxModel getComboBoxModel() {
        if (defendantsModel == null) {
            if (notOnOffence != null) {
                defendantsModel = new DefaultComboBoxModel(
                        notOnOffence.toArray(new DefendantValue[notOnOffence.size()]));
            }
        }
        return defendantsModel;
    }

    /**
     * XPanel implementation to get data required for the screen.
     */
    public void stepInitialise() {
        /**
         * TODO Retrieve Sequence Nos to populate next available sequence number
         * if editing. Next Seq No needs to be stored against each defendant on
         * the case. Key defendantId Call SP to retrieve all current Seq Nos for
         * defendants on the case available for selection. Need to pass a
         * collection of available defendants not currently on offence.
         * 
         * If EDIT MODE then cannot change defendant so need to store current
         * and next Seq No for single current defendant Call same SP passing
         * defendnant on case Id/defendant Id of current defendant Perhaps in a
         * HashMap
         */

        // Retrieve current defendant on offence information if in EDIT mode.
        if (mode == ChargesControllerHelper.MODE.EDIT) {
            defOnOffComplexValue = model.getOffenceValue().getDefendantOnOffence(
                    model.getDefendantValue().getDefendantID());
        }

        okCancelPanel.getOkAction().setEnabled(false);
        stepUpdateViewState();
    }
    
    private void moveModelToScreen() {
        getTopPanel().moveModelToScreen();
        getMiddlePanel().moveModelToScreen();
        getBottomPanel().moveModelToScreen();
    }

    private DefendantOnOffenceValue createDefendantOnOffenceValue() {
        DefendantOnOffenceValue dov  = getMiddlePanel().createDefendantOnOffenceValue(model.getOffenceValue().getOffenceID(), 
                ((DefendantValue) getBottomPanel().getDefendantsCombo().getSelectedItem()).getDefendantID());
        
        // Sequence No.
        if (getBottomPanel().getSeqNoText().getText() != null && getBottomPanel().getSeqNoText().getText().length() > 0){
            dov.setSequenceNo(new Integer(getBottomPanel().getSeqNoText().getText()));
        }
        
        // InterimD20
        if(getBottomPanel().isInterimD20Selected()){
        	dov.setInterimD20("Y");
        }
        else if (!getBottomPanel().isInterimD20Selected()) {
        	dov.setInterimD20("N");
        }

        return dov;
    }

    private DefendantOnOffenceComplexValue createDefendantOnOffenceComplexValue() {
        DefendantOnOffenceComplexValue docv = getMiddlePanel().createDefendantOnOffenceComplexValue();
        
        //Selected Defendant On Case Id retrieved from Bottom Panel combo
        docv.setDefendantOnCaseId(((DefendantValue)getBottomPanel().getDefendantsCombo().getSelectedItem())
                .getDefOnCaseBasicValue().getId());
        
        //Set Seq No. from Bottom Panel text field
        if (getBottomPanel().getSeqNoText().getText() != null){
            docv.setSeqNo(new Integer(getBottomPanel().getSeqNoText().getText()));
        }   
        
        // InterimD20
        if(getBottomPanel().isInterimD20Selected()){
        	docv.setInterimD20("Y");
        }
        else if (!getBottomPanel().isInterimD20Selected()) {
        	docv.setInterimD20("N");
        }
        
        return docv;
    }

   
    public void stepActivate() {
        moveModelToScreen();
    }

    public void stepDeactivate() {
        // No screen information is moving to VOs or model at this stage
    }

    /**
     * XPanel implementation called by the controlling dialog when it is closing
     * 
     * @param update
     *            true if the screen is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        model.setCancelClicked(false);
        if (update) {
            if (booLinkCountDefValue) {
                LinkCountDefValue linkCountDefValue = new LinkCountDefValue();
                linkCountDefValue.setCaseID(model.getACM().getCaseId());
                linkCountDefValue.setCourtID(new Integer(model.getCourtId()));
                linkCountDefValue.setIsInCourt(model.isUserInCourtRoom());
                linkCountDefValue.setCourtLogDate(Calendar.getInstance());
                linkCountDefValue.setAddDefendantToCount(true);
                linkCountDefValue.setChargeType(getChargeType());
                
                Collection<DefendantOnOffenceValue> defendantsOnOffence = new ArrayList<DefendantOnOffenceValue>();
                DefendantOnOffenceValue defendantOnOffenceValue=createDefendantOnOffenceValue();
                defendantOnOffenceValue.setObsInd("N");
                defendantsOnOffence.add(defendantOnOffenceValue);
                linkCountDefValue.setDefendantOnOffenceValues(defendantsOnOffence);               
               
                if (mode == ChargesControllerHelper.MODE.ADD) {
                    // Usually if this Panel is part of a single process
                    XhibitDelegateHelper.getChargeDelegate().linkCountsAndDefendants(linkCountDefValue,
                    		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
    
                    // The Crest Indictment Log only needs to be updated for
                    // Indictments.
                    if (model.getChargeValue().getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
                        Collection<DefendantValue> defendants = new ArrayList<DefendantValue>();
                        defendants.add((DefendantValue) getBottomPanel().getDefendantsCombo().getSelectedItem());
    
                        // Log that defendants have been assigned to a count.
                        CaseBasicValue caseBasicValue = model.getACM().getScheduledHearingValue().getCaseBasicValue();
                        CrestIndictmentLog.getInstance().addDefendantsToCountLog(caseBasicValue, defendants,
                               model.getOffenceValue());
                    }
                
                } else { // EDIT MODE
                    XhibitDelegateHelper.getChargeDelegate().updateDefendantOnOffence(linkCountDefValue,
                    		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                }
            } else {
                log.debug("stepDeinitialise booLinkCountDefValue is false");
                /* AddS41OffenceAction and AddC4SOffenceAction requires DefendantOnOffenceComplexValues
                 *  made available for existing process in action after offence details population.
                 */
                HashMap<Integer, DefendantOnOffenceComplexValue> defendantsOnOffencesComplexValues = new HashMap<Integer, DefendantOnOffenceComplexValue>();
               
                DefendantValue defendantValue = (DefendantValue)getBottomPanel().getDefendantsCombo().getSelectedItem();
                defendantsOnOffencesComplexValues.put(defendantValue.getDefendantID(), createDefendantOnOffenceComplexValue());
                
                Vector<Integer> ids = new Vector<Integer>();
                ids.add(defendantValue.getDefendantID());
                parent.setDefendantID(ids);
                parent.setDefendantsOnOffenceComplexValues(defendantsOnOffencesComplexValues);
            }
        } else {// update false
            if (booLinkCountDefValue == false) {
                boolean messageBoxReply = XMessageBox.alert(parent, getString("addDefendants.cancel.title"), true,
                        XMessageBox.ICONWARNING, getString("addDefendants.cancel.msg"), XMessageBox.OKCANCEL,
                        XMessageBox.DEFAULTCANCEL);

                if (!messageBoxReply) {
                    throw new UserCancelException();
                }
            }
            model.setCancelClicked(true);
        }
    }

    public void stepUpdateViewState() {
        // Enable OK if mandatory dates are completed
        okCancelPanel.getOkAction().setEnabled(
                getMiddlePanel().getDateOfChargeDate().isMandatoryFieldsCompleted()
                        && getMiddlePanel().getDateOfArrestDate().isMandatoryFieldsCompleted());
    }

    /**
     * Validates the table model. If the CRN field is empty and Auto CRN is not
     * selected an error message will be displayed
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        
        // Get Middle Panel to validate it's dates against each other and
        // offence end date
        getMiddlePanel().validateDates(model.getOffenceValue());
        getBottomPanel().validateSeqNo();
    }

    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }
    
    private String getChargeType() {
        if (model.getChargeValue() == null) {
            return convertChargeType(model.getSelectedChargeType()).getChargeType();
        } else {
            return model.getChargeValue().getChargeType();
        }
    }

    private ChargeType convertChargeType(int tab) {
        ChargeType rtn;
        switch (tab) {
            case ChargesController.INDICTMENTS_TAB:
                rtn = ChargeTypes.INDICTMENT;
                break;
            case ChargesController.SECTION41S_TAB:
                rtn = ChargeTypes.SECTION_41;
                break;
            case ChargesController.COMMITTALS_TAB:
                rtn = ChargeTypes.COMMITAL_FOR_SENTENCE;
                break;
            case ChargesController.BREACHES_TAB:
                rtn = ChargeTypes.BREACH;
                break;
            default:
                rtn = ChargeTypes.INDICTMENT;
                break;
        }
        return rtn;
    }
    
    public CourtLogCRUDValue getCRUDValue(Integer caseId) {
        CourtLogCRUDValue cv = new CourtLogCRUDValue();
        cv.setCaseId(caseId);
        //cv.setScheduledHearingId(xac.getApplicationCaseModel().getScheduledHearingId());
        // populate the event id
        cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
        return cv;
    }
}