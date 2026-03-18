package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2 - Import Defendants Panel
 * </p>
 * <p>
 * Description: Displays a table of joinder defendants that can be mapped to
 * selected defendants.
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

public class ImportDefendantsPanel extends JoinderIndictmentPanel {
    private DefendantMappingTableModel tableModel = null;

    private DefaultCellEditor defaultCellEditor = null;

    private JTable table = null;

    private PanelTitleLabel panelTitleLabel = null;

    private JTextArea instructionsTextArea = null;

    private HashSet currentJoinAliasIds = null;

    private boolean initialised = false;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean subActivateReentrancy = false;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean subDeactivateReentrancy = false;

    /**
     * Default constructor.
     * 
     * @param wizardDialog
     *            the wizard dialog hosting this panel.
     */
    public ImportDefendantsPanel(XWizardDialog wizardDialog) {
        super(wizardDialog);
        // this.stepInitialise();
    }

    /**
     * Initialise the table and set the default data in the model. Other GUI
     * components also become initialised.
     */
    public void stepInitialise() {
        // Instantiate the table model and table - table model is initially
        // populated with the
        // joinder defendants.
        String joinderCase = model.getJoinderCaseType() + model.getJoinderCaseNumber();
        String selectedCase = model.getSelectedCaseType() + model.getSelectedCaseNumber();
        tableModel = new DefendantMappingTableModel(joinderCase, selectedCase);// (model,(DefendantValue[])model.getJoinderDefendants().toArray());
        this.table = XTableFactory.getInstance().createMultiLineTable(this.tableModel);
        this.table.getTableHeader().setReorderingAllowed(false);
        JComboBox comboBox = new JComboBox();
        comboBox.setRenderer(new ImportDefendantBoxRenderer());
        defaultCellEditor = new DefaultCellEditor(comboBox);
        table.getColumnModel().getColumn(DefendantMappingTableModel.SELECTED_DEFENDANTS).setCellEditor(
                defaultCellEditor);

        // Render each column of the table.
        int columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new DefendantMapTableCellRenderer());
        }

        // Add the components onto the wizard.
        setLayout(gbLayout);
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        add(getPanelTitleLabel(), gbConstraints);

        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        add(getInstructionsTextArea(), gbConstraints);

        gbConstraints = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
        add(new JScrollPane(table), gbConstraints);
    }

    /**
     * Creates the title label for this panel.
     * 
     * @return the Panel Title Label
     */
    private PanelTitleLabel getPanelTitleLabel() {
        if (panelTitleLabel == null) {
            panelTitleLabel = new PanelTitleLabel(ResourceBundleHelper.getResource(resources,
                    JoinderConstants.PANEL_TITLE3));
            Dimension dimension = panelTitleLabel.getPreferredSize();
            int height = dimension.height;
            panelTitleLabel.setMinimumSize(new Dimension(400, height));
            panelTitleLabel.setPreferredSize(new Dimension(400, height));
        }
        return panelTitleLabel;

    }

    /**
     * Creates the text area that displays the instructions for use of this
     * panel. The text area is formatted to look like a JLabel.
     * 
     * @return the Instructions Text Area
     */
    private JTextArea getInstructionsTextArea() {
        if (instructionsTextArea == null) {
            instructionsTextArea = new JTextArea(ResourceBundleHelper.getResource(resources,
                    JoinderConstants.IMPORT_DEFENDANTS_INSTRUCTIONS));
            instructionsTextArea.setLineWrap(true);
            instructionsTextArea.setWrapStyleWord(true);
            instructionsTextArea.setEditable(false);
            instructionsTextArea.setBorder(null);
            instructionsTextArea.setForeground((Color) UIManager.get("Label.foreground"));
            instructionsTextArea.setBackground((Color) UIManager.get("Label.background"));
            instructionsTextArea.setFont((Font) UIManager.get("Label.font"));
        }
        return instructionsTextArea;
    }

    /**
     * Will check to see if there is a match with the given defendant id and any
     * of the given defendant ids in the collection. Returns true if a match is
     * found.
     * 
     * @param defendantId
     *            the id of the defendant to see if there is a match.
     * @param defendants
     *            the collection of defendants to see if any one of them has a
     *            match with the given defendant id.
     * 
     * @return whether a match was found
     */
    // THIS METHOD IS AN OLD IMPLEMENTATION IS NOW NOT BEING CALLED
    // ANYWHERE.
    // LEAVE COMMENTED OUT UNTIL NEEDED.
    //
    /*
     * private boolean isDefendantMatch (Integer defendantId,Collection
     * defendants) { Iterator defendantIterator = defendants.iterator();
     * DefendantValue defendantValue = null; while (defendantIterator.hasNext()) {
     * defendantValue = (DefendantValue) defendantIterator.next(); if
     * (defendantValue.getDefendantID().equals(defendantId)) { return true; } }
     * return false; }
     */

    /**
     * Will store the mapped joinder/selected defendants if there are any in the
     * model.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivateOnNext() throws CSRecoverableException {
        // Gets all the aliases defendant ids selected on this screen and adds
        // them to the list of aliases for this join.
        model.getDefendantMap().getAllAliasIds().addAll(currentJoinAliasIds);

        Object aliasValue = null;
        DefendantValue joinderDefVal = null;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            aliasValue = tableModel.getValueAt(i, DefendantMappingTableModel.SELECTED_DEFENDANTS);
            if ((aliasValue != null) && (aliasValue instanceof DefendantValue)) {
                joinderDefVal = (DefendantValue) tableModel
                        .getValueAt(i, DefendantMappingTableModel.JOINDER_DEFENDANTS);
                model.getDefendantMap().addMap(joinderDefVal, (DefendantValue) aliasValue);
            }
        }
    }

    /**
     * Sets reentrancy variables
     */
    public void stepDeactivateOnAll() {
        subActivateReentrancy = false;
        subDeactivateReentrancy = true;
    }

    /**
     * XPanel implementation to validate user input before leaving this screen.
     * Each defendant alias can only be associated with one defendant.
     * 
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSRecoverableException {
        if (subDeactivateReentrancy) {
            // stepValidate is only called before stepDeactivate on next
            // and is used to prevent the user adding further joins on the
            // same defendant
            // This is to prevent reentrancy issues caused by the setVisible
            // changes in JDK1.5
            log.debug("stepValidate reentrancy");
            return;
        }
        log.debug("Import Defendants stepValidate");

        checkOnScreenDuplicateAliases();

        checkAliasBeenPreviouslyAssigned();
    }

    /**
     * Check if the user has selected the same alias more than once on this join
     * (screen).
     * 
     * @throws UserCancelException
     */
    private void checkOnScreenDuplicateAliases() throws UserCancelException {
        // aliases that have been assigned to a defendant.
        HashSet assignedAliases = new HashSet();

        Object aliasValue = null;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            aliasValue = tableModel.getValueAt(i, DefendantMappingTableModel.SELECTED_DEFENDANTS);
            if ((aliasValue != null) && (aliasValue instanceof DefendantValue)) {
                DefendantValue aliasDefendant = (DefendantValue) aliasValue;
                if (assignedAliases.contains(aliasDefendant.getDefendantID())) {
                    log.debug("aliasDefendant repeated: " + aliasDefendant.getDefendantID());

                    // Alias has already been assigned to another defendant
                    // so
                    // display an error message to the user.
                    String defendantName = getDefendantName(aliasDefendant);
                    String message = ResourceBundleHelper.getResource(XhibitBundles.JoinderResources,
                            "joinder.alias.selection.msg", new Object[] { defendantName });

                    log.debug("aliasDefendant repeat error message: " + message);

                    String title = ResourceBundleHelper.getResource(XhibitBundles.JoinderResources,
                            "joinder.alias.selection.title");

                    JOptionPane.showMessageDialog(wizardDialog, message, title, JOptionPane.ERROR_MESSAGE);

                    // throw exception to prevent navigation to the next
                    // panel
                    // in the wizard.
                    throw new UserCancelException();
                } else {
                    // Alias has not previously been associated with a
                    // defendant
                    // so add it map of used aliases.
                    assignedAliases.add(aliasDefendant.getDefendantID());
                }
            }
        }
    }

    /**
     * Used to see if an alias has been used during a previous join in the
     * wizard.
     * 
     * @throws UserCancelException
     */
    private void checkAliasBeenPreviouslyAssigned() throws UserCancelException {
        currentJoinAliasIds = new HashSet();

        DefendantMap defendantMap = model.getDefendantMap();
        Object aliasValue = null;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            aliasValue = tableModel.getValueAt(i, DefendantMappingTableModel.SELECTED_DEFENDANTS);
            if ((aliasValue != null) && (aliasValue instanceof DefendantValue)) {
                DefendantValue defendant = (DefendantValue) aliasValue;
                if (defendantMap.getAllAliasIds().contains(defendant.getDefendantID())) {
                    // Alias has been used in previous join!
                    String aliasName = getDefendantName(defendant);
                    Integer originalId = defendantMap.getOriginalId(defendant.getDefendantID());
                    DefendantValue originalDefendant = defendantMap.getOriginalValue(originalId);
                    String originalName = getDefendantName(originalDefendant);

                    String message = ResourceBundleHelper.getResource(XhibitBundles.JoinderResources,
                            "joinder.alias.used.previously.msg", new Object[] { aliasName, originalName });
                    String title = ResourceBundleHelper.getResource(XhibitBundles.JoinderResources,
                            "joinder.alias.selection.title");
                    JOptionPane.showMessageDialog(wizardDialog, message, title, JOptionPane.ERROR_MESSAGE);

                    // throw exception to prevent navigation to the next
                    // panel
                    // in the wizard.
                    throw new UserCancelException();
                } else {
                    // Alias has not been used in previous join, so add it
                    // to
                    // the list for this join.
                    currentJoinAliasIds.add(defendant.getDefendantID());
                }
            }
        }
    }

    /**
     * Override the method in JoinderIndictmentPanel to always enable the next
     * button of the wizard dialog.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        // super.stepUpdateViewState();
        log.debug("ImportDefendantsPanel - stepUpdateViewState");
        wizardDialog.getButtonPanel().getNext().setEnabled(true);
    }

    /**
     * Store the data from the JoinderIndictmentModel.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        super.stepActivate();
        // Refer to JoinderIndictmentPanel superclass for reentrancy details
        if (subActivateReentrancy) {
            log.debug("ImportDefendantsPanel - stepActivate reentrancy");
            return;
        } else {
            subActivateReentrancy = true;
            subDeactivateReentrancy = false;
        }

        log.debug("ImportDefendantsPanel - stepActivate");
        if (!initialised) {
            this.stepInitialise();
            this.tableModel.setData(model.getJoinderDefendants().toArray());
            initialised = true;

            // if the user has clicked back to come back to this screen and
            // the
            // currentJoinAliasIds is not null then we have already been
            // to this screen before, so remove them from the all alias
            // collection
            // in the model.
            if (wizardDialog.getLatestEvent() == XWizardDialog.PREV_EVENT && currentJoinAliasIds != null
                    && currentJoinAliasIds.size() > 0) {
                model.getDefendantMap().getAllAliasIds().removeAll(currentJoinAliasIds);
            }
        }

        // Get the selected defendants, and preselect (i.e put their value in
        // the table)
        // the ones who have the same id as any of the joinder defendants.
        // For any matches, the model will make their respective cells
        // uneditable.
        Object[] joinderDefendants = model.getJoinderDefendants().toArray();
        // Object[] selectedDefendants =
        // model.getSelectedDefendantsFromIndictment().toArray();
        HashSet selectedDefendants = new HashSet(model.getSelectedDefendantsFromIndictment());
        DefendantMap defendantMap = model.getDefendantMap();
        Integer joinderDefendantId = null;
        DefendantValue selectedDefendantValue = null;

        for (int i = 0; i < joinderDefendants.length; i++) {
            joinderDefendantId = ((DefendantValue) joinderDefendants[i]).getDefendantID();
            if (defendantMap.getOriginalId(joinderDefendantId) != null) {
                joinderDefendantId = defendantMap.getOriginalId(joinderDefendantId);
            }

            Iterator iterator = selectedDefendants.iterator();
            while (iterator.hasNext()) {
                selectedDefendantValue = (DefendantValue) iterator.next();
                if (selectedDefendantValue.getDefendantID().equals(joinderDefendantId)) {
                    tableModel.setValueAt(selectedDefendantValue, i, DefendantMappingTableModel.SELECTED_DEFENDANTS);
                    iterator.remove();
                    break;
                }
            }
        }

        // Set the editor for table selected defendant column.
        // Vector selectedDefendantList = new Vector
        // (model.getSelectedDefendantsFromIndictment());
        // selectedDefendantList.add(0,""); // We want a space at the front
        JComboBox selectedDefendantBox = (JComboBox) this.defaultCellEditor.getComponent();
        selectedDefendantBox.removeAllItems();
        selectedDefendantBox.addItem(""); // We want a space at the front.
        Iterator i = selectedDefendants.iterator();
        while (i.hasNext()) {
            selectedDefendantValue = (DefendantValue) i.next();
            selectedDefendantBox.addItem(selectedDefendantValue);
        }

        this.table.tableChanged(new TableModelEvent(tableModel));
        this.revalidate();
        this.repaint();

        if (selectedDefendantBox.getItemCount() <= 1 && wizardDialog.getLatestEvent() == XWizardDialog.NEXT_EVENT) {
            JOptionPane.showMessageDialog(wizardDialog, ResourceBundleHelper.getResource(resources,
                    "joinder.alias.none.msg"), ResourceBundleHelper.getResource(resources, "joinder.alias.none.title"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void reset() {
        super.reset();
        this.removeAll();
        this.initialised = false;
    }

    /**
     * Gets the defendant name for the given defendant value.
     * 
     * @param defendant
     *            the defendant value.
     * @return the defendant name.
     */
    private String getDefendantName(DefendantValue defendant) {
        return defendant.getFirstName() + " " + defendant.getSurName();
    }

    /**
     * 
     * <p>
     * Title: Defendant Map Table Cell Renderer
     * </p>
     * <p>
     * Description: Displays the defendant value in a specific format as below:
     * <P>
     * <defendant first name> <defendant surname>
     * </p>
     * Note that if the value of the defendant is null, then this is rendered as
     * an empty string.
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
    public class DefendantMapTableCellRenderer extends DefaultTableCellRenderer {
        public DefendantMapTableCellRenderer() {
            super();
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean hasFocus,
                boolean isSelected, int row, int column) {
            if (value == null) {
                return super.getTableCellRendererComponent(table, "", hasFocus, isSelected, row, column);
            }
            DefendantValue defVal = (DefendantValue) value;
            String newValue = getDefendantName(defVal);
            return super.getTableCellRendererComponent(table, newValue, hasFocus, isSelected, row, column);
        }
    }

    /**
     * 
     * <p>
     * Title: Import Defendant Box Renderer
     * </p>
     * <p>
     * Description: Custom cell renderer for the JComboBox, to display defendant
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
    protected class ImportDefendantBoxRenderer extends BasicComboBoxRenderer {
        /**
         * Default Constructor.
         */
        public ImportDefendantBoxRenderer() {
            super();
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
            if (value == null || value.equals("")) {
                return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
            }

            DefendantValue defendantValue = (DefendantValue) value;
            String defName = getDefendantName(defendantValue);
            return super.getListCellRendererComponent(list, defName, index, isSelected, cellHasFocus);
        }
    }
}