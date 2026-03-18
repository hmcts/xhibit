package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class MediumEventMDPanel extends CourtLogEventPanel {
    private final Dimension labelDim = new Dimension(75, XHIBITConstant.getLineHeight());

    private final MediumEventMDModel model;

    private JLabel enteredDateLabel;

    private XDatePanel enteredDatePanel = null;

    public MediumEventMDPanel(XDialog parent, MediumEventMDModel model) throws CSRecoverableException {
        super(parent, model);
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getEnteredDateLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getEnteredDatePanel(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private XDatePanel getEnteredDatePanel() {
        if (enteredDatePanel == null) {
            enteredDatePanel = new XDatePanel(this, null);
        }
        return enteredDatePanel;
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        try {
            String dateString = (String) propertyMap.get(model.getSchema());
            model.setDateBy(XDateFormat.parse(dateString));
        } catch (ParseException ex) {
            // Ignore. Will be rectified when new date saved.
        }
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
        enteredDatePanel.stepValidate();

        // validate date is in the future for certain events
        if (model.getEventType().equals("20501")) {
            Calendar tomorrow = getLogAuditPanel().getDate();
            tomorrow.set(Calendar.HOUR_OF_DAY, 23);
            tomorrow.set(Calendar.MINUTE, 59);
            tomorrow.set(Calendar.SECOND, 59);
            if (enteredDatePanel.getDate().before(tomorrow)) {
                enteredDatePanel.requestFocus();
                throw new CSValidationException("validation.date.afterlog",
                        new String[] { enteredDatePanel.getText() }, "Date is in the past");
            }
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        if (model.isSelectionRequired() || !(enteredDatePanel.getText().trim().length() == 0)) {
            propertyMap.put(model.getSchema(), XDateFormat.format(model.getDateBy(), XDateFormat.DATEFORMAT));
        }

        log.debug(" meMDOptions : " + XDateFormat.format(model.getDateBy(), XDateFormat.DATEFORMAT));
    }

    protected boolean isMandatoryFieldsCompleted() {
        if (model.isSelectionRequired()) {
            return (enteredDatePanel.getText().length() > 0);
        }

        return true;
    }

    private JLabel getEnteredDateLabel() {
        if (enteredDateLabel == null) {
            enteredDateLabel = new JLabel();
            enteredDateLabel.setMaximumSize(labelDim);
            enteredDateLabel.setMinimumSize(labelDim);
            enteredDateLabel.setPreferredSize(labelDim);
            enteredDateLabel.setText(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "MediumEventMDDateLbl"));
        }
        return enteredDateLabel;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            enteredDatePanel.setDate(model.getDateBy());
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();
        model.setDateBy(enteredDatePanel.getDate());
    }
}
