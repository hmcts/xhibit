package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.Component;
import java.awt.GridBagConstraints;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2 - Charge Defendant List Panel
 * </p>
 * <p>
 * Description: Displays a list of defendants and charges specific to
 * indictments.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */
public class ChargeDefendantListPanel extends JoinderIndictmentPanel {
    /** The list for containing the defendants. */
    private JList defendantList = null;

    /** The select box of indictments. */
    private JComboBox indictmentBox = null;

    /** The defendants label. */
    private JLabel defendantsLabel = null;

    /** Label saying that the case was found. */
    private JLabel caseFoundLabel = null;

    /** Selected indictment label. */
    private JLabel selectedIndictmentLabel = null;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean subActivateReentrancy = false;

    /**
     * Default constructor. Instantiate a panel of a list of defendants and a
     * list of indictment charges. The information is all READ-ONLY.
     * 
     * @param dialog
     *            the wizard dialog.
     */
    public ChargeDefendantListPanel(XWizardDialog dialog) {
        super(dialog);
        this.stepInitialise();
    }

    /**
     * Initialise the GUI components.
     */
    public void stepInitialise() {
        // Initialise the GUI components.
        defendantList = new JList();
        indictmentBox = new JComboBox();
        PanelTitleLabel selectedCaseFoundLabel = new PanelTitleLabel(ResourceBundleHelper.getResource(resources,
                JoinderConstants.SELECT_CASE1));
        defendantsLabel = new JLabel();
        caseFoundLabel = new JLabel();
        selectedIndictmentLabel = new JLabel();

        // Set the list components their respective renderers.
        defendantList.setCellRenderer(new ChargeDefendantListCellRenderer(ChargeDefendantListCellRenderer.DEFENDANT));
        indictmentBox.setRenderer(new ChargeDefendantListCellRenderer(ChargeDefendantListCellRenderer.CHARGE));

        // Add the components.
        this.setLayout(gbLayout);

        // SELECTED CASE FOUND
        gbConstraints = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        add(selectedCaseFoundLabel, gbConstraints);

        // CASE FOUND
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        add(caseFoundLabel, gbConstraints);

        // DEFENDANTS
        gbConstraints = new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        add(defendantsLabel, gbConstraints);

        // DEFENDANT LIST
        gbConstraints = new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
        add(new JScrollPane(this.defendantList), gbConstraints);

        // SELECT INDICTMENT LABEL
        gbConstraints = new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        add(selectedIndictmentLabel, gbConstraints);

        // INDICTMENT DROP DOWN
        gbConstraints = new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
        add(this.indictmentBox, gbConstraints);
    }

    /**
     * No implementation needed.
     */
    public void stepValidate() throws CSValidationException {
    }

    /**
     * Override the method of XPanel to populate the GUI items with data stored
     * in the model.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        super.stepActivate();
        // Refer to JoinderIndictmentPanel superclass for reentrancy details
        if (subActivateReentrancy) {
            log.debug("ChargeDefendantListPanel - stepActivate reentrancy");
            return;
        } else {
            subActivateReentrancy = true;
        }
        log.debug("ACTIVE IN CHARGE DEFENDANT LIST");

        if (model.getSelectedChargeCompValue() != null) {
            // First set the text of the labels.
            defendantsLabel.setText(ResourceBundleHelper.getResource(resources, JoinderConstants.DEFENDANTS));

            caseFoundLabel.setText(ResourceBundleHelper.getResource(resources, JoinderConstants.CASE) + " "
                    + model.getSelectedCaseType() + model.getSelectedCaseNumber().intValue() + " "
                    + ResourceBundleHelper.getResource(resources, JoinderConstants.FOUND));

            selectedIndictmentLabel.setText(ResourceBundleHelper.getResource(resources,
                    JoinderConstants.SELECT_INDICTMENT));

            // Get the charge composite value stored in the model, and
            // obtain a list
            // of defendants associated to the joinder case.
            ChargeCompositeValue chargeCompValue = model.getSelectedChargeCompValue();
            Object[] defendantValues = chargeCompValue.getAllDefendants().toArray();
            this.defendantList.setListData(defendantValues);

            // Retrieve the charges and put all the indictments in the
            // selectBox.
            ChargeValue chargeValue = null;
            ChargeValue[] charges = (ChargeValue[]) chargeCompValue.getCharges().toArray(
                    new ChargeValue[chargeCompValue.getCharges().size()]);
            Sorter.sort(charges, new String[] { "crestChargeSeqNo" }, new Boolean(true));

            this.indictmentBox.removeAllItems(); // Clear this every time
            // and reload.
            for (int i = 0; i < charges.length; i++) {
                chargeValue = charges[i];
                if (!model.isChargeJoined(chargeValue.getChargeID())) {
                    if (chargeValue.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
                        this.indictmentBox.addItem(chargeValue);
                    }
                }
            }

            // NOTE - code is in this order (i.e. button focus and enabling
            // before error message) because otherwise the Next button
            // is disabled but ALSO has focus!!!
            boolean hasIndictments = indictmentBox.getItemCount() > 0;
            if (!hasIndictments) {
                wizardDialog.getButtonPanel().getBack().setEnabled(true);
                wizardDialog.getButtonPanel().getBack().requestFocus();
            }
            indictmentBox.setEnabled(hasIndictments);
            wizardDialog.getButtonPanel().getNext().setEnabled(hasIndictments);

            if (!hasIndictments) {
                String title = ResourceBundleHelper.getResource(resources, JoinderConstants.NO_INDICTMENT_MSG_TITLE);
                String message = ResourceBundleHelper.getResource(resources, JoinderConstants.NO_INDICTMENT_MSG_TEXT);
                JOptionPane.showMessageDialog(wizardDialog, message, title, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        // super.stepUpdateViewState();
    }

    /**
     * Update the selected indictment in the model.
     */
    public void stepDeactivateOnNext() {
        ChargeValue selectedIndictment = (ChargeValue) indictmentBox.getSelectedItem();
        if (selectedIndictment != null) {
            model.setSelectedIndictment(selectedIndictment);
            model.addChargeId(model.getSelectedIndictment().getChargeID());
            String[] caseIndictment = new String[] { model.getSelectedCaseTypeNumber(),
                    selectedIndictment.getCrestChargeSeqNo().toString() };
            model.getCasesAndIndictmentNos().add(caseIndictment);
        }
    }

    /**
     * Sets reentrancy variable
     */
    public void stepDeactivateOnAll() {
        subActivateReentrancy = false;
    }

    /**
     * 
     * <p>
     * Title: Defendant List Cell Renderer
     * </p>
     * <p>
     * Description: Custom cell renderer for the JList, to display defendant
     * attributes.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author Joseph Antoniou
     * @version 1.0
     */
    protected class ChargeDefendantListCellRenderer extends DefaultListCellRenderer {
        /** Renderer type identifier for charge type. */
        public static final int CHARGE = 0;

        /** Renderer type identifier for defendant type. */
        public static final int DEFENDANT = 1;

        /**
         * The renderer type. Determines whether to render the list as a charge
         * type or defendant name.
         */
        private int type = DEFENDANT;

        /**
         * Default Constructor. Will instantiate a renderer and depending on the
         * type, will render the content as a charge type or defendant name.
         * 
         * @param rendererType
         *            the identifier, determining how to render the internal
         *            data.
         */
        public ChargeDefendantListCellRenderer(final int rendererType) {
            super();
            this.type = rendererType;
        }

        /**
         * Overrides the method in superclass, to return the value of a
         * defendant's first name and surname if its a defendant, otherwise the
         * name of the indictment.
         * 
         * @param list
         *            the JList used host this renderer.
         * @param value
         *            the value to be renderered.
         * @param index
         *            the row index of the value.
         * @param isSelected
         *            specifies if the value is selected.
         * @param cellHasFocus
         *            specifies whether the cell has focus.
         * 
         * @return the rendering component.
         */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            if (this.type == ChargeDefendantListCellRenderer.DEFENDANT) {
                DefendantValue defendantValue = (DefendantValue) value;
                String defName = defendantValue.getFirstName() + " " + defendantValue.getSurName();
                return super.getListCellRendererComponent(list, defName, index, isSelected, cellHasFocus);
            }
            if (this.type == ChargeDefendantListCellRenderer.CHARGE) {
                ChargeValue chargeValue = (ChargeValue) value;
                String indictment = chargeValue.getChargeTypeDescription() + " " + chargeValue.getCrestChargeSeqNo();
                return super.getListCellRendererComponent(list, indictment, index, isSelected, cellHasFocus);
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}