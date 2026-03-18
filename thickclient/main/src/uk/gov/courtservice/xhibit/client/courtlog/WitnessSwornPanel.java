package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.NumericValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

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
 * @version $Revision: 1.47 $
 */
public class WitnessSwornPanel extends CourtLogEventPanel {
    private static final String NO_WITNESS_ID = "-1";

    private static final int MAX_NUMBER_TEXT_LEN = 10;

    // Witness Sworn Options
    private final Collection witnessSwornCollection = new Vector();

    // Prosecution witness names
    private final Collection prosecutionWitnessCollection = new Vector();

    // only set in the constructor
    private final WitnessSwornModel model;

    private final Integer caseId;

    private final Integer courtId;

    private String wsoSchema;

    private String wsoType;

    private String wsoName;

    private String wsoNumber;

    private String wsoID;

    private JComboBox subEventCb;

    private JComboBox subSubEventCb;

    private JLabel nameLabel;

    private JLabel numberLabel;

    private JTextField numberText;

    private JTextField nameText;

    private JLabel witnessSwornOptionsLabel;

    private JLabel witnessNamesLabel;

    private JLabel eventNameLabel;

    private JButton addBtn;

    /**
     * Set up the main screen components and populate the drop downs
     * 
     * @param parent
     *            The XDialog that invoked this
     * @param model
     *            WitnessSwornModel holding the majority of the data
     * @throws CSRecoverableException
     */
    public WitnessSwornPanel(XDialog parent, WitnessSwornModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;
        this.caseId = model.getXac().getApplicationCaseModel().getCaseId();
        this.courtId = XhibitSingleton.getInstance().getCourtId();

        stepInitialise();
        jbInit();
    }

    public void stepInitialise() throws CSRecoverableException {
        super.getCourtLogEventLevelPanel().stepInitialise();

        setupXSDSchemaFields();

        setupWitnessSwornCollection();

        setupProsecutionWitnessCollection();

        setupEditableModel();
    }

    private void setupXSDSchemaFields() {
        // Set XSD schema fields
        wsoSchema = XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E" + model.getEventType()
                + "_WitnessSwornOptions");
        wsoType = XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E" + model.getEventType() + "_Type");
        wsoName = XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E" + model.getEventType() + "_Name");
        wsoNumber = XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E" + model.getEventType() + "_Number");
        wsoID = XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E" + model.getEventType() + "_ID");
    }

    private void setupProsecutionWitnessCollection() {
        // Get the witness details and use them to populate
        // prosecutionWitnessCollection
        prosecutionWitnessCollection.add(new GeneralPurposeObject(0, "select", XHIBITConstant.getResource(
                XhibitBundles.WitnessSworn, "select")));

        // SG
        WitnessSummary[] witnessSummaryArray = XhibitDelegateHelper.getWitnessSelectorDelegate().getSignedInWitnesses(
                caseId, courtId);

        for (int x = 0; x < witnessSummaryArray.length; x++) {
            prosecutionWitnessCollection.add(new GeneralPurposeObject(prosecutionWitnessCollection.size(),
                    witnessSummaryArray[x].getId().toString(), witnessSummaryArray[x].getName()));
        }
    }

    private void setupWitnessSwornCollection() {
        Vector lovTypes = (Vector) RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd", wsoType);
        witnessSwornCollection.add(new GeneralPurposeObject(0, "select", XHIBITConstant.getResource(
                XhibitBundles.WitnessSworn, "select")));

        for (int x = 0; x < lovTypes.size(); x++) {
            String name = (String) lovTypes.get(x);
            String value = name;

            try {
                value = XHIBITConstant.getResource(XhibitBundles.SimpleEvent, name);
            } catch (MissingResourceException mre) {
                log.warn(mre, mre);
            }

            // Append the word "sworn" to the end of the displayble text.
            // It is done this way to mirror the way the XSL that print the
            // event works.
            witnessSwornCollection.add(new GeneralPurposeObject(x + 1, name, value + " "
                    + XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "lblSworn")));
        }
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        // Save the data in the model
        HashMap hashMap = (HashMap) propertyMap.get(wsoSchema);
        model.setSubEventCode((String) hashMap.get(wsoType));
        model.setSubEventId(findSelectedEntry((Vector) witnessSwornCollection, model.getSubEventCode()));
        model.setSubSubEventCode((String) hashMap.get(wsoName));
        model.setWitnessName((String) hashMap.get(wsoName));
        model.setWitnessNumber((String) hashMap.get(wsoNumber));
        model.setWitnessId((String) hashMap.get(wsoID));
    }

    private void setupEditableModel() throws CSRecoverableException {
        if (model.isInEditMode()) {
            CourtLogCRUDValue courtLogCRUDValue = getCLCDelegate().getEntry(new Long(model.getEventId().longValue()));

            populateModel(courtLogCRUDValue);

            if (isProsecutionWitnessOption(model.getSubEventId()) || isRespondentWitnessOption(model.getSubEventId())) {
                // If the witness was added dynamically, i.e. not sourced from
                // the list of witnesses, then
                // add their details to the list
                if (NO_WITNESS_ID.equalsIgnoreCase(model.getWitnessId())) {
                    addToPullDownList((Vector) prosecutionWitnessCollection, model.getWitnessId(), model
                            .getWitnessName());
                }

                model.setSubSubEventId(findSelectedEntry((Vector) prosecutionWitnessCollection, model.getWitnessId()));
            }

            if (isDefendantOption(model.getSubEventId()) || isAppellantOption(model.getSubEventId())) {
                super.getCourtLogEventLevelPanel().populateEditableModel(courtLogCRUDValue);
            }
        }
    }

    private void jbInit() {
        final Insets defaultInsets = XHIBITConstant.nonContainerInsets; // new
        // Insets(2,
        // 2, 2,
        // 2);

        this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(5, 2, 25, 2), 0, 0));
        this.add(getWitnessSwornOptionsLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getSubEventCb(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getWitnessNamesLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getSubSubEventCb(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getAddBtn(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(getNameLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getNameText(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getNumberLabel(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getNumberText(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));

        getCourtLogEventLevelPanel().getDefendantCbLabel()
                .setText(
                        XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E" + model.getEventType()
                                + "_Defendant_Names"));
    }

    private JTextField getNameText() {
        if (nameText == null) {
            nameText = new JTextField();
            nameText.setColumns(25);
            nameText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
            Dimension nameTextDim = new Dimension(275, 21);
            nameText.setMaximumSize(nameTextDim);
            nameText.setMinimumSize(nameTextDim);
            nameText.setPreferredSize(nameTextDim);
            nameText.setToolTipText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Enter_the_name_of"));
        }

        return nameText;
    }

    private JTextField getNumberText() {
        if (numberText == null) {
            numberText = new JTextField();
            numberText.setEnabled(false);
            numberText.setColumns(8);
            numberText.setDocument(new NumericValidatingDocumentDecorator(new LimitedTextValidatingDocumentDecorator(
                    MAX_NUMBER_TEXT_LEN)));
            numberText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });

            Dimension numTextDim = new Dimension(50, 21);
            numberText.setMaximumSize(numTextDim);
            numberText.setMinimumSize(numTextDim);
            numberText.setPreferredSize(numTextDim);
            numberText.setToolTipText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Enter_the_number_of"));
            numberText.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        return numberText;
    }

    private JButton getAddBtn() {
        if (addBtn == null) {
            addBtn = new JButton();
            addBtn.setBorder(BorderFactory.createRaisedBevelBorder());
            Dimension addButtonDim = new Dimension(60, 27);
            addBtn.setMaximumSize(addButtonDim);
            addBtn.setMinimumSize(addButtonDim);
            addBtn.setPreferredSize(addButtonDim);
            addBtn.setToolTipText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Add_a_witness"));
            addBtn.setMnemonic('A');
            addBtn.setText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Add"));
            addBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addBtn_actionPerformed();
                }
            });
        }

        return addBtn;
    }

    void addBtn_actionPerformed() {
        AddEditWitnessModel aewModel = new AddEditWitnessModel();
        aewModel.setWitSwornModel(model);
        AddEditWitnessDialog aew = new AddEditWitnessDialog(getParentFrame(), aewModel);

        aew.show();

        if (aewModel.isSaveClicked()) {
            prosecutionWitnessCollection.add(new GeneralPurposeObject(-1, NO_WITNESS_ID, aewModel.getWitnessName()));

            populateComboBox(getSubSubEventCb(), (Vector) prosecutionWitnessCollection);

            getSubSubEventCb().setSelectedIndex(getSubSubEventCb().getItemCount() - 1);
        }
    }

    private JComboBox getSubEventCb() {
        if (subEventCb == null) {
            subEventCb = new JComboBox();
            Dimension subEventComboDim = new Dimension(275, 21);
            subEventCb.setMaximumSize(subEventComboDim);
            subEventCb.setMinimumSize(subEventComboDim);
            subEventCb.setPreferredSize(subEventComboDim);
            subEventCb.setToolTipText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Select_a_witness"));
            populateComboBox(getSubEventCb(), (Vector) witnessSwornCollection);
            subEventCb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }

        return subEventCb;
    }

    private JComboBox getSubSubEventCb() {
        if (subSubEventCb == null) {
            subSubEventCb = new JComboBox();
            subSubEventCb.setEnabled(false);
            Dimension subSubEventComboDim = new Dimension(275, 21);
            subSubEventCb.setMaximumSize(subSubEventComboDim);
            subSubEventCb.setMinimumSize(subSubEventComboDim);
            subSubEventCb.setPreferredSize(subSubEventComboDim);
            subSubEventCb.setToolTipText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Select_a_name_from"));
            populateComboBox(getSubSubEventCb(), (Vector) prosecutionWitnessCollection);
            subSubEventCb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }

        return subSubEventCb;
    }

    private JLabel getWitnessSwornOptionsLabel() {
        if (witnessSwornOptionsLabel == null) {
            witnessSwornOptionsLabel = new JLabel();
            witnessSwornOptionsLabel.setText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn,
                    "Witness_Sworn_Options"));
        }

        return witnessSwornOptionsLabel;
    }

    private JLabel getWitnessNamesLabel() {
        if (witnessNamesLabel == null) {
            witnessNamesLabel = new JLabel();
            witnessNamesLabel.setText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E" + model.getEventType()
                    + "_Witness_Names"));
        }

        return witnessNamesLabel;
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "E"
                    + model.getEventType() + "_Witness_Sworn"));
        }

        return eventNameLabel;
    }

    private JLabel getNameLabel() {
        if (nameLabel == null) {
            nameLabel = new JLabel();
            nameLabel.setText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Name"));
        }

        return nameLabel;
    }

    private JLabel getNumberLabel() {
        if (numberLabel == null) {
            numberLabel = new JLabel();
            numberLabel.setText(XHIBITConstant.getResource(XhibitBundles.WitnessSworn, "Number"));
        }

        return numberLabel;
    }

    public void stepActivate() throws CSRecoverableException {
        makeColumnZeroWidth(getWitnessNamesLabel().getPreferredSize().width, getCourtLogEventLevelPanel());

        super.stepActivate();
    }

    private void makeColumnZeroWidth(int width, JPanel child) {
        Component c = null;
        for (int i = 0; i < child.getComponents().length; i++) {
            if (child.getComponent(i) instanceof JLabel) {
                c = child.getComponent(i);
                break;
            }
        }

        if (c != null && c instanceof JComponent && ((JComponent) c).getPreferredSize().width != width) {
            Dimension d = new Dimension(width, XHIBITConstant.getLineHeight());
            ((JComponent) c).setMinimumSize(d);
            ((JComponent) c).setPreferredSize(d);
            ((JComponent) c).setSize(d);
            child.revalidate();
            child.repaint();
        }
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            // Specific fields
            getSubEventCb().setSelectedIndex(model.getSubEventId());
            getSubSubEventCb().setSelectedIndex(model.getSubSubEventId());

            // Only populate Name field if it's not a prosecution/respondent
            // witness option
            // or defendant/appellant option as these are sourced from the
            // selected name in
            // prosecutionWitnessCollection or defendantNamesCollection
            if (isProsecutionWitnessOption(getSubEventCb().getSelectedIndex())
                    || isRespondentWitnessOption(getSubEventCb().getSelectedIndex())) {
                getNumberText().setText(model.getWitnessNumber());
            } else if (isDefendantOption(getSubEventCb().getSelectedIndex())
                    || isAppellantOption(getSubEventCb().getSelectedIndex())) {
                // No Action
            } else {
                getNameText().setText(model.getWitnessName());

                // Only populate Number field if it's a defence/appellant
                // witness option
                if (isDefenceWitnessOption(getSubEventCb().getSelectedIndex())
                        || isAppellantWitnessOption(getSubEventCb().getSelectedIndex())) {
                    getNumberText().setText(model.getWitnessNumber());
                }
            }
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        super.stepUpdateViewState();

        int selectedSubEvent = getSubEventCb().getSelectedIndex();

        boolean enableState = isProsecutionWitnessOption(selectedSubEvent)
                || isRespondentWitnessOption(selectedSubEvent);
        getWitnessNamesLabel().setEnabled(enableState);
        getSubSubEventCb().setEnabled(enableState);
        getAddBtn().setEnabled(enableState);

        enableState = isDefendantOption(selectedSubEvent) || isAppellantOption(selectedSubEvent);
        getCourtLogEventLevelPanel().setEnabled(enableState);

        enableState = isDefenceWitnessOption(selectedSubEvent) || isAppellantWitnessOption(selectedSubEvent)
                || isTrialInterpreterOption(selectedSubEvent) || isAppealInterpreterOption(selectedSubEvent);
        getNameLabel().setEnabled(enableState);
        enableTextField(getNameText(), enableState);

        enableState = isDefenceWitnessOption(selectedSubEvent) || isAppellantWitnessOption(selectedSubEvent)
                || isProsecutionWitnessOption(selectedSubEvent) || isRespondentWitnessOption(selectedSubEvent);
        getNumberLabel().setEnabled(enableState);
        enableTextField(getNumberText(), enableState);
    }

    protected boolean isMandatoryFieldsCompleted() {
        boolean result = true;

        int selectedSubEvent = this.subEventCb.getSelectedIndex();

        if (isProsecutionWitnessOption(selectedSubEvent) || isRespondentWitnessOption(selectedSubEvent)) {
            if (getNumberText().getText().length() == 0)
                result = false;
        } else if (isDefenceWitnessOption(selectedSubEvent) || isAppellantWitnessOption(selectedSubEvent)) {
            if (getNumberText().getText().length() == 0)
                result = false;
        } else if (isTrialInterpreterOption(selectedSubEvent) || isAppealInterpreterOption(selectedSubEvent)) {
            if (getNameText().getText().length() == 0)
                result = false;
        } else if (isDefendantOption(selectedSubEvent) || isAppellantOption(selectedSubEvent)) {
            // NoAction
        } else {
            result = false;
        }

        return result;
    }

    public void stepValidate() throws CSRecoverableException {
        super.stepValidate();
        // Validate number
        try {
            // DC: check to see if th witness number, where applicable, is >
            // 0
            if (isDefenceWitnessOption(getSubEventCb().getSelectedIndex())
                    || isProsecutionWitnessOption(getSubEventCb().getSelectedIndex())
                    || isRespondentWitnessOption(getSubEventCb().getSelectedIndex())
                    || isAppellantWitnessOption(getSubEventCb().getSelectedIndex())) {
                // changed from int to long as we must allow for 9999999999
                long number = Long.parseLong(getNumberText().getText());
                if (number <= 0) {
                    // Witness Number validation exception
                    getNumberText().requestFocus();

                    throw new CSValidationException("validation.minexclusive", new Object[] {
                            getNumberText().getText(), "0" }, "Witness number <= 0");
                }
            }
        } catch (NumberFormatException e) {
            // Witness Number validation exception( this should never happen
            // as
            // the field is now subject to a numeric text document decorator
            // )
            getNumberText().requestFocus();
            Object[] items = new Object[] { getNumberText().getText() };
            throw new CSValidationException("validation.datatype", items, "validation.datatype: Invalid Number", e);
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        // Generic fields
        GeneralPurposeObject gpo01, gpo02;

        // Specific Fields
        // Witness Sworn Options
        model.setSubEventId(getSubEventCb().getSelectedIndex());
        gpo01 = ((GeneralPurposeObject) ((Vector) witnessSwornCollection).get(model.getSubEventId()));
        model.setSubEventCode(gpo01.getCode());

        // Witness Names
        model.setSubSubEventId(getSubSubEventCb().getSelectedIndex());
        gpo02 = ((GeneralPurposeObject) ((Vector) prosecutionWitnessCollection).get(model.getSubSubEventId()));
        model.setSubSubEventCode(gpo02.getCode());

        if (isProsecutionWitnessOption(model.getSubEventId()) || isRespondentWitnessOption(model.getSubEventId())) {
            // DC: check to see if item is "select", if it is, do not assign
            // gpo02s to model
            if (gpo02.getId() == 0) {
                model.setWitnessName(getNameText().getText());
                model.setWitnessId("-1");
            } else {
                model.setWitnessId(gpo02.getCode());
                model.setWitnessName(gpo02.getDesc());
            }

            model.setWitnessNumber(getNumberText().getText());
        } else if (isDefenceWitnessOption(model.getSubEventId()) || isAppellantWitnessOption(model.getSubEventId())) {
            model.setWitnessName(getNameText().getText());
            model.setWitnessNumber(getNumberText().getText());
            model.setWitnessId("-1");
        } else if (isDefendantOption(model.getSubEventId()) || isAppellantOption(model.getSubEventId())) {
            super.getCourtLogEventLevelPanel().stepDeinitialise(true);

            model.setWitnessName(model.getDefendantName());
            model.setWitnessNumber("");
            model.setWitnessId("-1");
        } else {
            model.setWitnessName(getNameText().getText());
            model.setWitnessNumber("");
            model.setWitnessId("-1");
        }

        model.printModel();
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        HashMap cleOptionsType = new HashMap();

        cleOptionsType.put(wsoType, model.getSubEventCode());
        cleOptionsType.put(wsoName, model.getWitnessName());
        cleOptionsType.put(wsoNumber, model.getWitnessNumber());
        cleOptionsType.put(wsoID, model.getWitnessId());

        propertyMap.put(wsoSchema, cleOptionsType);
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // This is done in move screen to model instead.
        // super.stepDeinitialise(true);
        if (update) {
            CourtLogCRUDValue courtLogCRUDValue = createCRUDFromModel();
            persistCourtLogCRUDValue(courtLogCRUDValue);
        } else {
            model.getWitNumbers().clear();
        }
    }

    private void populateComboBox(JComboBox comboBox, Vector comboBoxData) {
        comboBox.removeAllItems();

        for (int x = 0; x < comboBoxData.size(); x++) {
            comboBox.addItem(comboBoxData.get(x));
        }
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setBackground((state ? Color.white : SystemColor.text));

        if (state == false) {
            textField.setText("");
        }
    }

    private int findSelectedEntry(Vector itemList, String code) {
        int returnCode = 0;

        for (int x = 0; x < itemList.size(); x++) {
            GeneralPurposeObject pdlo = (GeneralPurposeObject) itemList.get(x);

            if (pdlo.getCode().equalsIgnoreCase(code)) {
                returnCode = x;
            }
        }

        return returnCode;
    }

    private boolean isProsecutionWitnessOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20904_Prosecution_witness_sworn".equalsIgnoreCase(gpo.getCode()) || "E20904_Prosecution_witness_expert_sworn"
                .equalsIgnoreCase(gpo.getCode()));
    }

    private boolean isRespondentWitnessOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20603_Respondent_Witness_Sworn".equalsIgnoreCase(gpo.getCode()));
    }

    private boolean isDefenceWitnessOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20904_Defence_witness_character_sworn".equalsIgnoreCase(gpo.getCode())
                || "E20904_Defence_witness_expert_sworn".equalsIgnoreCase(gpo.getCode())
                || "E20904_Defence_witness_professional_sworn".equalsIgnoreCase(gpo.getCode()) || "E20904_Defence_witness_fact_sworn"
                .equalsIgnoreCase(gpo.getCode()));
    }

    private boolean isAppellantWitnessOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20603_Appellant_Witness_Character_Sworn".equalsIgnoreCase(gpo.getCode())
                || "E20603_Appellant_Witness_Expert_Sworn".equalsIgnoreCase(gpo.getCode())
                || "E20603_Appellant_Witness_Professional_Sworn".equalsIgnoreCase(gpo.getCode()) || "E20603_Appellant_Witness_Fact_Sworn"
                .equalsIgnoreCase(gpo.getCode()));
    }

    private boolean isDefendantOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20904_Defendant_sworn".equalsIgnoreCase(gpo.getCode()));
    }

    private boolean isTrialInterpreterOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20904_Interpreter_sworn".equalsIgnoreCase(gpo.getCode()));
    }

    private boolean isAppellantOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20603_Appellant_Sworn".equalsIgnoreCase(gpo.getCode()));
    }

    private boolean isAppealInterpreterOption(int param) {
        GeneralPurposeObject gpo = (GeneralPurposeObject) ((Vector) witnessSwornCollection).get(param);
        return ("E20603_Interpreter_Sworn".equalsIgnoreCase(gpo.getCode()));
    }

    private void addToPullDownList(Vector pullDown, String code, String desc) {
        pullDown.add(new GeneralPurposeObject(pullDown.size(), code, desc));
    }

    private class GeneralPurposeObject {
        private int id;

        private String code;

        private String desc;

        public GeneralPurposeObject(int id, String code, String desc) {
            this.id = id;
            this.code = code;
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public String toString() {
            return desc;
        }
    }
}
