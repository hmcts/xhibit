package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DateChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: IndictmentByPanel
 * </p>
 * <p>
 * Description: Manages the generation of Indictment By events for preliminary
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

public class IndictmentByPanel extends XDirectionsPanel {
    private XDatePanel indictmentBy = null;

    private JLabel indictmentByLbl = null;

    private CaseLevelEventsValue clev;

    /**
     * Constructor
     * 
     * @param model -
     *            CaseLevelEventsModel
     * @throws CSRecoverableException
     */
    public IndictmentByPanel(CaseLevelEventsModel model) throws CSRecoverableException {
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
        return PHConstants.INDICTMENT_BY;
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
        if ((clev != null) && (clev.getIndictmentBy() != null)) {
            getIndictmentBy().setDate(clev.getIndictmentBy());
        }
    }

    /**
     * Moves data from the screen to the model
     * 
     * @throws CSValidationException
     */
    public void moveScreenToModel() throws CSValidationException {
        if (clev != null && getModified()) {
            clev.setIndictmentBy(getIndictmentBy().getDate());
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
        this.add(getIndictmentByLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, PDHConstants.PDH_INSETS, 0, 0));
        this.add(getIndictmentBy(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, PDHConstants.PDH_INSETS, 0, 0));
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
     * The label for the date widget
     * 
     * @return
     */
    private JLabel getIndictmentByLbl() {
        if (indictmentByLbl == null) {
            indictmentByLbl = new JLabel();
            indictmentByLbl.setText(XHIBITConstant.getResource(XhibitBundles.PreliminaryHearing, "lblIndictmentBy"));
        }
        return indictmentByLbl;
    }

    /**
     * A date widget
     * 
     * @return
     */
    public XDatePanel getIndictmentBy() {
        if (indictmentBy == null) {
            XDatePanel temp = new XDatePanel(this);
            temp.getDateComponent().getDisplay().addKeyListener(new IndictmenyByUpdateStateKeyListener(this));
            temp.getDateComponent().addMChangeListener(new DateChangeListener(this));
            indictmentBy = temp;
        }
        return indictmentBy;
    }
}
