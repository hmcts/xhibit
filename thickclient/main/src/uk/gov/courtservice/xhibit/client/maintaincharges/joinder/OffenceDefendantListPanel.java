package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title: XHIBIT 2 - Offence Defendant List Panel
 * </p>
 * <p>
 * Description: This class represents a panel that displays offence/defendants
 * information. It also provides functionality for reordering the counts.
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

public class OffenceDefendantListPanel extends JoinderIndictmentPanel implements ListSelectionListener {
    /**
     * Identifier for the panel to display selected indictment offence
     * informaion.
     */
    public static final int SELECTED_INDICTMENT = 0;

    /**
     * Identifier for the panel to display joinder indictment offence
     * information.
     */
    public static final int JOINDER_INDICTMENT = 1;

    /** The table used to display the offence/defendants data. */
    protected XTable indictmentTable = null;

    /**
     * The button responsible for reordering the sequence of an offence, moving
     * up 1.
     */
    protected JButton moveUpButton = null;

    /**
     * The button responsible for reordering the sequence of an offence, moving
     * down 1.
     */
    protected JButton moveDownButton = null;

    /**
     * The button for re-running the wizard to merge another case onto the
     * current joinder case.
     */
    protected JButton joinMoreButton = null;

    /** The title of this panel. */
    private String panelTitle = null;

    /**
     * The predicate that determines whether to provide functionality for
     * reoerdering the offences for the stored indictment charge
     */
    private boolean reorder = false;

    /**
     * Reference to an identifier, informing the panel what type of offences to
     * display. Selected indictment offences are displayed by default.
     */
    private int chargeType = SELECTED_INDICTMENT;

    /** Determines whether the next button should be enabled/disabled. */
    private boolean enableNext = true;

    boolean initialised = false;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean subActivateReentrancy = false;

    /**
     * Default constructor. Will instantiate a panel, displaying a table of
     * offence/defendant related information specific to the stored selected
     * indictment charge in the model.
     * 
     * @param wizardDialog
     *            the dialog used to host this panel.
     * @param title
     *            the title of this panel.
     */
    public OffenceDefendantListPanel(XWizardDialog wizardDialog, String title) {
        super(wizardDialog);
        this.panelTitle = title;
    }

    /**
     * Instantiate a panel of offence/defendant data specific to the stored
     * indictment charge in the model. Can also provide functionality for
     * reordering the offences.
     * 
     * @param wizardDialog
     *            the wizard dialog for this panel.
     * @param the
     *            title of this panel.
     * @param reorder
     *            provides functionality for reordering the offences if this is
     *            set to <B>true</B>.
     */
    public OffenceDefendantListPanel(XWizardDialog wizardDialog, String title, int chargeType, boolean reorder,
            boolean enableNext) {
        super(wizardDialog);
        this.panelTitle = title;
        this.reorder = reorder;
        this.chargeType = chargeType;
        this.enableNext = enableNext;
        this.stepInitialise();
    }

    /**
     * Initialise the GUI components and store them on the panel.
     */
    public void stepInitialise() {
        // Create the components.
        PanelTitleLabel panelTitleLabel = new PanelTitleLabel(this.panelTitle);
        this.indictmentTable = XTableFactory.getInstance().createMultiLineTable(new DefendantOffenceTableModel());
        this.indictmentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.indictmentTable.getTableHeader().setReorderingAllowed(false);

        this.setLayout(gbLayout);

        // Initialise buttons here..
        JPanel buttonPanel = new JPanel(gbLayout);
        this.moveUpButton = new JButton(new MoveUpAction());
        this.moveUpButton.setIcon(this.getImage("moveup.gif"));
        this.moveDownButton = new JButton(new MoveDownAction());
        this.moveDownButton.setIcon(this.getImage("movedown.gif"));
        this.joinMoreButton = new JButton(new JoinMoreAction());
        this.joinMoreButton.setText("Join More"); // / change later..

        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new java.awt.Insets(0, 0, 0, 0), 0, 0);
        buttonPanel.add(moveUpButton, gbConstraints);

        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new java.awt.Insets(0, 0, 0, 0), 0, 0);
        buttonPanel.add(moveDownButton, gbConstraints);

        // PANEL TITLE LABEL
        add(panelTitleLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // TABLE
        add(new JScrollPane(this.indictmentTable), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // BUTTON PANEL
        add(buttonPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, XHIBITConstant.nonContainerInsets, 0, 0));

        add(this.joinMoreButton, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // Only make the buttons visible if the reordering funcionality is
        // flagged on.
        buttonPanel.setVisible(this.reorder);

        // This button is false by default.
        this.joinMoreButton.setVisible(false);

        if (this.reorder) {
            this.indictmentTable.getSelectionModel().addListSelectionListener(this);
        }

        // this.joinMoreButton = new JButton
    }

    private ImageIcon getImage(String imageName) {
        ImageIcon image = null;
        java.net.URL url = getClass().getClassLoader().getResource(XHIBITConstant.imageRoot + imageName);
        if (url == null) {
            image = new ImageIcon(XHIBITConstant.imageRoot + imageName);
        } else {
            image = new ImageIcon(url);
        }
        return image;
    }

    /**
     * This panel contains only read-only information, despite the re-ordering
     * functionality.
     */
    public void stepDeactivateOnNext() {
        if (this.joinMoreButton.isVisible()) {
            DefendantOffenceTableModel tableModel = (DefendantOffenceTableModel) this.indictmentTable.getModel();
            Collection offences = tableModel.getOffences();
            model.sortOffences(offences);
            model.getJoinderIndictmentValue().getNewChargeValue().setOffenceValues(offences);
        }
    }

    /**
     * Sets reentrancy variables
     */
    public void stepDeactivateOnAll() {
        subActivateReentrancy = false;
    }

    /**
     * No validation necessary,no implementation.
     */
    public void stepValidate() {
    }

    /**
     * Overrides method of JoinderIndictment panel, to display/not display the
     * next button, depending on the setting.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        // super.stepUpdateViewState();
        wizardDialog.getButtonPanel().getNext().setEnabled(this.enableNext);
        this.moveDownButton.setEnabled(false);
        this.moveUpButton.setEnabled(false);
    }

    /**
     * Overrides method in JoinderInditmentPanel to load the indictment
     * information in the table contained in this panel.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        super.stepActivate();
        // Refer to JoinderIndictmentPanel superclass for reentrancy details
        if (subActivateReentrancy) {
            log.debug("OffenceDefendantListPanel - stepActivate reentrancy");
            return;
        } else {
            subActivateReentrancy = true;
        }

        wizardDialog.getButtonPanel().getNext().setEnabled(this.enableNext);
        ChargeValue indictment = null;
        if (this.chargeType == SELECTED_INDICTMENT) {
            indictment = model.getSelectedIndictment();
        } else {
            indictment = model.getJoinderIndictmentValue().getNewChargeValue();
        }
        model.sortOffences(indictment.getOffenceValues());
        ((DefendantOffenceTableModel) this.indictmentTable.getModel()).setData(indictment);
        if (!initialised) {
            this.indictmentTable
                    .initColumnSizes(new Object[] {
                            ResourceBundleHelper.getResource(resources, JoinderConstants.COUNT),
                            ResourceBundleHelper.getResource(resources, JoinderConstants.OFFENCE_DESC),
                            ResourceBundleHelper.getResource(resources, JoinderConstants.DEFENDANT), }, wizardDialog
                            .getWidth());
        }
        this.indictmentTable.tableChanged(new TableModelEvent(this.indictmentTable.getModel()));
        this.revalidate();
        this.repaint();
    }

    public void valueChanged(ListSelectionEvent e) {
        int selectionIdx = this.indictmentTable.getSelectionModel().getMaxSelectionIndex();
        if (selectionIdx == -1) {
            this.moveDownButton.setEnabled(false);
            this.moveUpButton.setEnabled(false);
        } else {
            int rowCount = this.indictmentTable.getModel().getRowCount();
            this.moveDownButton.setEnabled(selectionIdx < (rowCount - 1));
            this.moveUpButton.setEnabled(selectionIdx > 0);
        }
    }

    public JButton getJoinMoreButton() {
        return this.joinMoreButton;
    }

    public void reset() {
        super.reset();
        this.initialised = false;
    }

    /**
     * 
     * <p>
     * Title: Move Up Action
     * </p>
     * <p>
     * Description: Moves an offence up one row in the table, decrementing its
     * offence seq number by 1.
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
    private final class MoveUpAction extends XAction {
        public void xActionPerformed(ActionEvent e) {
            JTable table = OffenceDefendantListPanel.this.indictmentTable;
            int rowIndex = table.getSelectionModel().getMaxSelectionIndex();
            DefendantOffenceTableModel tableModel = (DefendantOffenceTableModel) table.getModel();
            tableModel.moveUp(rowIndex);
            table.tableChanged(new TableModelEvent(tableModel));
            table.getSelectionModel().setSelectionInterval(rowIndex - 1, rowIndex - 1);
        }
    }

    /**
     * 
     * <p>
     * Title: Move Down Action
     * </p>
     * <p>
     * Description: Moves an Offence down one row in the table incrementing its
     * offence seq number by 1.
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
    private final class MoveDownAction extends XAction {
        public void xActionPerformed(ActionEvent e) {
            JTable table = OffenceDefendantListPanel.this.indictmentTable;
            int rowIndex = table.getSelectionModel().getMaxSelectionIndex();
            DefendantOffenceTableModel tableModel = (DefendantOffenceTableModel) table.getModel();
            tableModel.moveDown(rowIndex);
            table.tableChanged(new TableModelEvent(tableModel));
            table.getSelectionModel().setSelectionInterval(rowIndex + 1, rowIndex + 1);
        }
    }

    private final class JoinMoreAction extends XAction {
        public void xActionPerformed(ActionEvent e) {
            try {
                log.debug("model selected case number: " + model.getSelectedCaseTypeNumber());
                model.addChargeCompositeValue(model.getSelectedCaseTypeNumber(), model.getSelectedChargeCompValue());

                wizardDialog.setPanelIndex(0, XWizardDialog.NEXT_EVENT);
                model.reset();
            } catch (CSRecoverableException ex) {
                XHIBITErrorHandler.handleError(ex);
            }
        }
    }
}