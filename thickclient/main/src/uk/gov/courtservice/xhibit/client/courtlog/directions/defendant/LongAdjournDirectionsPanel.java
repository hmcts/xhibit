package uk.gov.courtservice.xhibit.client.courtlog.directions.defendant;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.LongAdjournmentModel;
import uk.gov.courtservice.xhibit.client.courtlog.LongAdjournmentPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsForDefendantPanel;
import uk.gov.courtservice.xhibit.client.util.LogAuditPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class LongAdjournDirectionsPanel extends XDirectionsForDefendantPanel {
    private LongAdjournmentModel[] models;

    private LongAdjournmentPanel lap;

    private LogAuditPanel logAuditPanel;

    public LongAdjournDirectionsPanel(LongAdjournmentModel[] models, LogAuditPanel logAuditPanel)
            throws CSRecoverableException {
        setModel(models);
        this.logAuditPanel = logAuditPanel;
        init();
    }

    public LogAuditPanel getLogAuditPanel() {
        return logAuditPanel;
    }

    public void stepActivate() throws CSRecoverableException {
        // reset warning view first
        clearWarning(null);
        super.stepActivate();
    }

    public void setWarningVisible(ButtonGroup bg, boolean visible) {
        super.setWarningVisible(bg, visible);

        lap.setWarningVisible(visible);
    }

    public void moveModelToScreen() throws CSRecoverableException {
        if (models != null) {
            if (!isModelsIdentical()) {
                showWarning(null);
            }
            // Regardless of whether the models are identical or not, use
            // the first
            // defendant for displaying on the screen
            LongAdjournmentModel lam = models[0];
            lap.setModel(lam);
            lap.stepActivate();
        }
    }

    public void moveScreenToModel() throws CSRecoverableException {
        if (models != null && getModified()) {
            for (int i = 0; i < models.length; i++) {
                lap.setModel(models[i]);
                lap.stepValidate();
                lap.stepDeactivate();
                // lap.stepDeinitialise(true);
            }
        }
    }

    public CourtLogCRUDValue getCRUD(LongAdjournmentModel model) throws CSRecoverableException {
        if (model.getSubEventCode() != null && model.getSubEventId() > 0) {
            lap.setModel(model);
            // if (!model.equals(models[0]))
            // {
            // lap.stepActivate();
            // lap.stepValidate();
            // lap.stepDeactivate();
            // }
            model.setInEditMode(false);
            lap.stepDeinitialise(true);
            model.setInEditMode(true);
            return lap.getAvailableCRUDValue();
        }
        return null;
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsPanel
         *       abstract method
         */
    }

    public Integer getEventType() {
        if (models != null && models[0].getEventType() != null) {
            return PDHConstants.DEF_LONG_ADJOURNMENT;
        }
        return null;
    }

    public void setModel(LongAdjournmentModel[] models) {
        this.models = models;
    }

    private void init() throws CSRecoverableException {
        this.setBorder(BorderFactory.createTitledBorder(ResourceBundleHelper.getResource(XhibitBundles.Directions,
                "LongAdjournmentBorder")));
        this.setLayout(new GridBagLayout());
        this.add(getWarningLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.VERTICAL, PDHConstants.PDH_INSETS, 0, 0));
        lap = new LongAdjournmentPanel(this, models[0]);
        this.add(lap, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.VERTICAL, PDHConstants.PDH_INSETS, 0, 0));
        lap.addPropertyChangeListener(XPanel.property_modified, new ModifyPropertyListener());
    }

    private boolean isModelsIdentical() {
        if (models.length <= 1)
            return true;

        for (int i = 0; i < models.length - 1; i++) {
            LongAdjournmentModel lam1 = models[i];
            LongAdjournmentModel lam2 = models[i + 1];
            // option
            if ((lam1.getSubEventCode() == null && lam2.getSubEventCode() != null)
                    || (lam1.getSubEventCode() != null && !lam1.getSubEventCode().equals(lam2.getSubEventCode()))) {
                return false;
            }
            // adjourn date
            if ((lam1.getAdjournmentDate() == null && lam2.getAdjournmentDate() != null)
                    || (lam1.getAdjournmentDate() != null && !lam1.getAdjournmentDate().equals(
                            lam2.getAdjournmentDate()))) {
                return false;
            }
            // psr request
            if (lam1.isPsrRequired() != lam2.isPsrRequired()) {
                return false;
            }
            // reserved to judge
            if ((lam1.isJudgeReserved() != lam2.isJudgeReserved())
                    || (lam1.isNotReserved() != lam2.isNotReserved())
                    || (lam1.isJudgeReserved() && ((lam1.getJudgeName() == null && lam2.getJudgeName() == null) || (lam1
                            .getJudgeName() != null && !lam1.getJudgeName().equals(lam2.getJudgeName()))))) {
                return false;
            }
        }
        return true;
    }

    public void stepValidate() {
    }

    public void stepUpdateViewState() {
    }

    public void setModified(boolean modified) {
        if (!isActivating()) {
            super.setModified(modified);
            // System.err.println("MODIFIED FIRED IN DIRECTION
            // LAP::"+modified);
            if (lap != null && modified)
                clearWarning(null);
            if (lap != null && !modified)
                lap.setModified(modified);
        }
    }

    class ModifyPropertyListener implements java.beans.PropertyChangeListener, java.io.Serializable {
        public ModifyPropertyListener() {
        }

        public void propertyChange(java.beans.PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                modified();
            }
        }
    }

}