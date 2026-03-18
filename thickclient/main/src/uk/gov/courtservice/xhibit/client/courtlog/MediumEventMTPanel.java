package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.LimitedTextDocument;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.NumericKeyListener;

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
public class MediumEventMTPanel extends CourtLogEventPanel {
    private static final String MIN_TRIAL_EST = "1";

    private static final int MAX_NUMBER_TEXT_LEN = 10;

    int fontInc = 5;

    private final Dimension labelDim = new Dimension(85, XHIBITConstant.getLineHeight());

    private final Dimension textFieldDim = new Dimension(275, XHIBITConstant.getLineHeight());

    private final MediumEventMTModel model;

    private JLabel textFieldLabel;

    private JTextField enteredTextField;

    public MediumEventMTPanel(XDialog parent, MediumEventMTModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.setMinimumSize(new Dimension(360, 290));

        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getTextFieldLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getEnteredTextField(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        model.setEnteredText((String) propertyMap.get(model.getSchema()));
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
        if (isNumberRequired(model.getEventType())) {
            // Validate number
            try {
                // changed from int to long as we must allow for 9999999999
                long estimate = Long.parseLong(getEnteredTextField().getText());

                // Numeric estimate value must be > 0. Neil Entwistle 12/06/2003
                if (estimate <= 0) {
                    Object[] items = new Object[2];
                    items[0] = getEnteredTextField().getText();
                    items[1] = MIN_TRIAL_EST;
                    throw new CSValidationException("validation.mininclusive", items,
                            "validation.mininclusive: Invalid Estimate");
                }
            } catch (NumberFormatException e) {
                // Estimate validation exception
                getEnteredTextField().requestFocus();

                Object[] items = new Object[1];
                items[0] = getEnteredTextField().getText();

                throw new CSValidationException("validation.datatype", items, "validation.datatype: Invalid Estimate",
                        e);
            }
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        if (model.isSelectionRequired() || !(getEnteredTextField().getText().trim().length() == 0)) {
            propertyMap.put(model.getSchema(), model.getEnteredText());
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        super.stepUpdateViewState();
        enableTextField(getEnteredTextField(), !(isChargeEvent(model.getEventType())));
    }

    protected boolean isMandatoryFieldsCompleted() {
        if (model.isSelectionRequired()) {
            return (getEnteredTextField().getText().trim().length() > 0);
        }

        return true;
    }

    private JTextField getEnteredTextField() {
        if (enteredTextField == null) {
            enteredTextField = new JTextField();
            enteredTextField.setMinimumSize(textFieldDim);
            enteredTextField.setPreferredSize(textFieldDim);
            if (isNumberRequired(model.getEventType())) {
                // Columns set to 8 to allow 10 numbers to be entered (with a
                // bit spare)
                enteredTextField.setColumns(8);
                // Add a NumericKeyListener to restrict the inputs to
                // numeric values, tab and back delete only
                enteredTextField.addKeyListener(new NumericKeyListener());
                // set the limit on the length of the text field
                enteredTextField.setDocument(new LimitedTextDocument(MAX_NUMBER_TEXT_LEN));
            }
            enteredTextField.setToolTipText(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, getToolTipName(model
                    .getEventType())));
            enteredTextField.setHorizontalAlignment(isNumberRequired(model.getEventType()) ? SwingConstants.RIGHT
                    : SwingConstants.LEFT);
            enteredTextField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return enteredTextField;
    }

    private JLabel getTextFieldLabel() {
        if (textFieldLabel == null) {
            textFieldLabel = new JLabel();
            textFieldLabel.setMinimumSize(labelDim);
            textFieldLabel.setText(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, getLabelName(model
                    .getEventType())));
        }
        return textFieldLabel;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            enteredTextField.setText(model.getEnteredText());
        } else {
            if (isChargeEvent(model.getEventType())) {
                enteredTextField.setText(model.getEnteredText());
            }
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();
        model.setEnteredText(getEnteredTextField().getText());
    }

    private String getLabelName(String param) {
        String result = "MediumEventMTNameLbl";

        if (isNumberRequired(param)) {
            return "MediumEventMTNumberLbl";
        }

        return result;
    }

    private String getToolTipName(String param) {
        String result = "ttMediumEventMTName";

        if (isNumberRequired(param)) {
            return "ttMediumEventMTNumber";
        }

        return result;
    }

    private boolean isNumberRequired(String param) {
        return ("20613".equalsIgnoreCase(param) 
                || "20920".equalsIgnoreCase(param)
                || "20931".equalsIgnoreCase(param)
                || "20932".equalsIgnoreCase(param)
                || "31000".equalsIgnoreCase(param)
                || "32000".equalsIgnoreCase(param));
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setBackground((state ? Color.white : SystemColor.text));
    }

    private boolean isChargeEvent(String eventType) {
        return ("20926".equalsIgnoreCase(eventType));
    }

    public boolean isStringContainsLetter(String str) {
        char[] characters = str.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            if (Character.isLetter(characters[i]))
                return true;
        }
        return false;
    }
}
