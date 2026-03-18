package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title: Panel to allow offences/counts to be added.
 * </p>
 * <p>
 * Description: Used by: <br/>i. the AddIndictmentWizard to add one Count to an
 * Indictment and <br/>ii. the AddBreachWizard to allow one or more offences to
 * be added to a Breach.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class AddedOffencesPanel extends XPanel {
    private static final int PANEL_WIDTH = 300;

    private BreachController controller;

    private List<OffenceValue> offenceList;

    private AddedOffencesModel addedOffencesModel;

    private AddedOffencesPanelModel addedOffencesPanelModel;

    private JScrollPane offencesScrollPane = null;

    private XTable addedOffencesTable;

    private JPanel buttonPanel;

    private JButton addOffenceBtn;

    private JButton removeOffenceBtn;

    private XAction add;

    private XAction remove;

    private ChargeWizardModel model;

    private Object[] longValues = null;

    /**
     * Constructor for the AddedOffencesPanel
     * 
     * @param controller
     *            screen/dialog that is creating this panel.
     * @param model
     *            the ChargeWizardModel
     * @throws CSRecoverableException
     */
    public AddedOffencesPanel(BreachController controller, ChargeWizardModel model) throws CSRecoverableException {
        super();
        
        if (controller == null || model == null){
            throw new IllegalArgumentException 
            ("AddedOffencesPanel - controller and model parameters must contain values");
        }
        this.controller = controller;
        this.model = model;
        this.offenceList = model.getAddedOffences();

        stepInitialise();
        jbInit();
        stepUpdateViewState();
    }

    /**
     * Paints the controls on the screen.
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());
        add(getOffencesScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        add(getButtonPanel(), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    /**
     * Lazy instantiates the scroll pane for the offences table.
     * 
     * @return the scroll pane for the offences table.
     */
    private JScrollPane getOffencesScrollPane() {
        if (offencesScrollPane == null) {
            offencesScrollPane = new JScrollPane(getAddedOffencesTable());
        }
        return offencesScrollPane;
    }

    /**
     * Lazy instantiates the offences table.
     * 
     * @return the offences table.
     */
    protected XTable getAddedOffencesTable() {
        if (addedOffencesTable == null) {
            addedOffencesTable = XTableFactory.getInstance().createMultiLineTable(addedOffencesModel);
            longValues = new Object[] { "50", XTableFactory.COLUMN_WIDTH_UNDEFINED, };
            Dimension cfsTableDimension = new Dimension(PANEL_WIDTH, getTableHeight(addedOffencesModel));

            addedOffencesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            addedOffencesTable.initColumnSizes(longValues, 300);
            addedOffencesTable.getTableHeader().setReorderingAllowed(false);
            addedOffencesTable.setPreferredScrollableViewportSize(new Dimension(PANEL_WIDTH, 150));

            ListSelectionModel rowSM = addedOffencesTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    try {
                        actionListRowSelectionChanged(e);
                    } catch (CSRecoverableException ex) {
                        XHIBITConstant.handleError(ex);
                    }
                }
            });
        }
        return addedOffencesTable;
    }

    /**
     * Calculates the height of the offences table.
     * 
     * @param tableModel
     * @return the height of the offences table
     */
    private int getTableHeight(AbstractTableModel tableModel) {
        return 25 + (XHIBITConstant.TABLE_ROW_HEIGHT * tableModel.getRowCount());
    }

    private void actionListRowSelectionChanged(ListSelectionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("** * * in actionListRowSelectionChanged   * * **");
        // Ignore extra messages.
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (!lsm.isSelectionEmpty()) {
            addedOffencesPanelModel.setSelectedRowIndex(lsm.getMinSelectionIndex());
            remove.setEnabled(true);
        } else {
            remove.setEnabled(false);
        }
        stepUpdateViewState();
    }

    /**
     * Lazy instantiates the button panel containing the add and remove offence
     * buttons.
     * 
     * @return the button panel.
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.add(getAddOffenceBtn(), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST,
                    GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

            buttonPanel.add(getRemoveOffenceBtn(), new GridBagConstraints(0, 1, 1, 1, 0, 0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        }
        return buttonPanel;
    }

    /**
     * Lazy instantiates the add offence button.
     * 
     * @return the add offence button.
     */
    private JButton getAddOffenceBtn() {
        if (addOffenceBtn == null) {
            addOffenceBtn = new JButton(add);
        }
        return addOffenceBtn;
    }

    /**
     * Lazy instantiates the remove offence button.
     * 
     * @return the remove offence button.
     */
    private JButton getRemoveOffenceBtn() {
        if (removeOffenceBtn == null) {
            removeOffenceBtn = new JButton(remove);
        }
        return removeOffenceBtn;
    }

    /**
     * Gets the number of offence added using this panel.
     * 
     * @return the number of offence added using this panel.
     */
    public int getOffenceCount() {
        if (offenceList == null) {
            return 0;
        } else {
            return offenceList.size();
        }
    }

    public BreachController getController() {
        return controller;
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first created.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        addedOffencesModel = new AddedOffencesModel(offenceList.toArray());

        // create model for actions
        addedOffencesPanelModel = new AddedOffencesPanelModel(offenceList, this, model);
        addedOffencesPanelModel.setChargeWizardModel(model);

        // set up actions
        add = XhibitActions.getAction(controller.getXac(), XhibitActions.AddWizardBreachOffence);
        add.setModel(addedOffencesPanelModel);
        add.setEnabled(true);

        remove = XhibitActions.getAction(controller.getXac(), XhibitActions.RemoveWizardBreachOffence);
        remove.setModel(addedOffencesPanelModel);
        remove.setEnabled(false);
    }

    /**
     * XPanel implementation of life cycle method, called each time the screen
     * is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        addedOffencesModel = new AddedOffencesModel(offenceList.toArray());
        getAddedOffencesTable().clearSelection();
        getAddedOffencesTable().setModel(addedOffencesModel);
        getAddedOffencesTable().initColumnSizes(longValues, PANEL_WIDTH);
        stepUpdateViewState();
    }

    /**
     * XPanel implementation of life cycle method, called when the state of a
     * widget changes.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        if (model.getChargeType().getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
            if (addedOffencesModel.getRowCount() == 0) {
                getAddOffenceBtn().requestFocus();
                getAddOffenceBtn().setFocusPainted(true);
                getAddOffenceBtn().setEnabled(true);
            } else {
                getAddOffenceBtn().setEnabled(false);
                getAddOffenceBtn().setFocusPainted(false);
            }
        }

        // enable actions on parent stepUpdateViewState
        controller.stepUpdateViewState();
    }

    /**
     * XPanel implementation of life cycle method, called when leaving this
     * screen to validate the data. No validation currently required.
     */
    public void stepValidate() {
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left. No processing currently required.
     */
    public void stepDeactivate() {
        // offenceList is set with offences already!
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * closed. Note - This is only called if the user has been to the other
     * screens in the wizard and populated the necessary data.
     * 
     * @param update
     *            will be true if the Finish action is fired, false if the
     *            Cancel action is fired.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            controller.stepDeinitialise();
        }
    }
}