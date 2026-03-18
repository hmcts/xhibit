package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0 Change History 10.04.2003 C Davies getEventNameLabel changed to
 *          get correct panel title label depending on which function selected
 *          (ie. Appeal or Trial)
 */
public class DefendantChargesPanel extends CourtLogEventPanel {
    private final DefendantChargesModel model;

    private String wsoSchema, wsoName, wsoCount, wsoIndictment;

    private JLabel eventNameLabel;

    private JLabel indictmentNumberLabel;

    private JLabel countNumberLabel;

    private JLabel defendantNameLabel;

    private JTextField defendantNameText;

    private JTextField countNumberText;

    private JTextField indictmentNumberText;

    public DefendantChargesPanel(XDialog parent, DefendantChargesModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        // Set XSD schema fields
        wsoSchema = XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "E" + model.getEventType()
                + "_ChargesPutOptions");
        wsoName = XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "E" + model.getEventType()
                + "_DefendantName");
        wsoCount = XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "E" + model.getEventType()
                + "_CountNumber");
        wsoIndictment = XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "E" + model.getEventType()
                + "_IndictmentNumber");

        stepInitialise();
        jbInit();
        stepActivate();
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        // Save the data in the model
        HashMap hashMap = (HashMap) propertyMap.get(wsoSchema);
        model.setIndictmentNumber((String) hashMap.get(wsoIndictment));
        model.setCountNumber((String) hashMap.get(wsoCount));
        model.setDefendantName((String) hashMap.get(wsoName));
    }

    private void jbInit() {
        this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(5, 2, 25, 2), 0, 0));
        this.add(getIndictmentNumberLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getIndictmentNumberText(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCountNumberLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCountNumberText(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getDefendantNameLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getDefendantNameText(), new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JTextField getDefendantNameText() {
        if (defendantNameText == null) {
            defendantNameText = new JTextField();
            defendantNameText.setColumns(25);
            defendantNameText.setMaximumSize(new Dimension(275, 21));
            defendantNameText.setMinimumSize(new Dimension(275, 21));
            defendantNameText.setPreferredSize(new Dimension(275, 21));
            defendantNameText.setToolTipText(XHIBITConstant.getResource(XhibitBundles.DefendantCharges,
                    "ttDefendantName"));
        }

        return defendantNameText;
    }

    private JTextField getIndictmentNumberText() {
        if (indictmentNumberText == null) {
            indictmentNumberText = new JTextField();
            indictmentNumberText.setColumns(5);
            indictmentNumberText.setMaximumSize(new Dimension(50, 21));
            indictmentNumberText.setMinimumSize(new Dimension(50, 21));
            indictmentNumberText.setPreferredSize(new Dimension(50, 21));
            indictmentNumberText.setToolTipText(XHIBITConstant.getResource(XhibitBundles.DefendantCharges,
                    "ttIndictmentNumber"));
        }

        return indictmentNumberText;
    }

    private JTextField getCountNumberText() {
        if (countNumberText == null) {
            countNumberText = new JTextField();
            countNumberText.setColumns(5);
            countNumberText.setMaximumSize(new Dimension(50, 21));
            countNumberText.setMinimumSize(new Dimension(50, 21));
            countNumberText.setPreferredSize(new Dimension(50, 21));
            countNumberText.setToolTipText(XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "ttCountNumber"));
        }

        return countNumberText;
    }

    private JLabel getIndictmentNumberLabel() {
        if (indictmentNumberLabel == null) {
            indictmentNumberLabel = new JLabel();
            indictmentNumberLabel.setText(XHIBITConstant.getResource(XhibitBundles.DefendantCharges,
                    "lblIndictmentNumber"));
        }

        return indictmentNumberLabel;
    }

    private JLabel getCountNumberLabel() {
        if (countNumberLabel == null) {
            countNumberLabel = new JLabel();
            countNumberLabel.setText(XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "lblCountNumber"));
        }

        return countNumberLabel;
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "E"
                    + model.getEventType() + "_EventName"));
        }

        return eventNameLabel;
    }

    private JLabel getDefendantNameLabel() {
        if (defendantNameLabel == null) {
            defendantNameLabel = new JLabel();
            defendantNameLabel.setText(XHIBITConstant.getResource(XhibitBundles.DefendantCharges, "lblDefendantName"));
        }

        return defendantNameLabel;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        getIndictmentNumberText().setText(model.getIndictmentNumber());
        getCountNumberText().setText(model.getCountNumber());
        getDefendantNameText().setText(model.getDefendantName());
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        super.stepUpdateViewState();
        enableTextField(getIndictmentNumberText(), false);
        enableTextField(getCountNumberText(), false);
        enableTextField(getDefendantNameText(), false);
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        model.setIndictmentNumber(getIndictmentNumberText().getText());
        model.setCountNumber(getCountNumberText().getText());
        model.setDefendantName(getDefendantNameText().getText());
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        HashMap cleOptionsType = new HashMap();

        cleOptionsType.put(wsoName, model.getDefendantName());
        cleOptionsType.put(wsoCount, model.getCountNumber());
        cleOptionsType.put(wsoIndictment, model.getIndictmentNumber());

        propertyMap.put(wsoSchema, cleOptionsType);
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setBackground((state ? Color.white : SystemColor.text));
    }
}
