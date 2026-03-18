package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: CaseCalledOnPanel
 * </p>
 * <p>
 * Description: Manages the generation of Case Called On events for preliminary
 * hearings
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class CaseCalledOnPanel extends XDirectionsPanel {
    private JCheckBox caseCalledOnCbx = null;

    private JLabel requiredLbl;

    private CaseLevelEventsValue clev;

    /**
     * Constructor
     * 
     * @param model -
     *            CaseLevelEventsModel
     * @throws CSRecoverableException
     */
    public CaseCalledOnPanel(CaseLevelEventsModel model) throws CSRecoverableException {
        setModel(model);
        init();
        stepActivate();
    }

    /**
     * Returns the event type for this court log event
     * 
     * @return Integer - the court log event type
     */
    public Integer getEventType() {
        return PHConstants.CASE_CALLED_ON;
    }

    /**
     * Saves the model
     * 
     * @param newModel
     */
    public void setModel(CaseLevelEventsModel newModel) {
        clev = newModel.getCaseLevelEventsValue();
    }

    /**
     * Moves data from the model to the screen
     */
    public void moveModelToScreen() {
        if ((clev != null) && (clev.getCaseCalledOn() != null)) {
            getCaseCalledOnCbx().setSelected(clev.getCaseCalledOn().booleanValue());
        }
    }

    /**
     * Moves data from the screen to the model
     * 
     * @throws CSValidationException
     */
    public void moveScreenToModel() throws CSValidationException {
        if (clev != null) {
            clev.setCaseCalledOn(new Boolean(getCaseCalledOnCbx().isSelected()));
        }
    }

    /**
     * Populates a CRUD - not used
     * 
     * @param crud
     * @param defOnCaseId
     */
    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
    }

    /**
     * Puts the widgets on the screen
     */
    private void init() {
        this.setLayout(new GridBagLayout());
        this.add(getRequiredLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, PHConstants.INSETS, 0, 0));
        this.add(getCaseCalledOnCbx(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, PHConstants.INSETS, 0, 0));
    }

    /**
     * Validates data on the screen - not used
     */
    public void stepValidate() {
    }

    /**
     * Enables/disables screen widgets
     */
    public void stepUpdateViewState() {
    }

    /**
     * A checkbox widget that determines whether or not the "case called on"
     * event should be created
     * 
     * @return JCheckBox
     */
    public JCheckBox getCaseCalledOnCbx() {
        if (caseCalledOnCbx == null) {
            caseCalledOnCbx = new JCheckBox();
            caseCalledOnCbx.addItemListener(new ItemChangeListener(this));
        }
        return caseCalledOnCbx;
    }

    /**
     * The label for the checkbox widget
     * 
     * @return
     */
    private JLabel getRequiredLbl() {
        if (requiredLbl == null) {
            requiredLbl = new JLabel();
            requiredLbl.setText(XHIBITConstant.getResource(XhibitBundles.PreliminaryHearing, "lblCaseCalledOn"));
        }
        return requiredLbl;
    }
}
