package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title: Panel contain offences added to breach.
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
 * @author Bal Bhamra
 * @version 1.0
 */
public class AddedOffencesToBreachPanel extends XPanel {
    private final static String CHARGE_CONTROLLER_BUSINESS_DELEGATE_NAME = "ChargeController";

    private final static Logger log = CSServices.getLogger(AddedOffencesToBreachPanel.class);

    private ResourceBundle errorResources = XHIBITConstant.getResourceBundle(XhibitBundles.ErrorText);

    private static final String INVALID_MODEL_ERROR_TITLE = "crn.invalidModelErrorTitle";

    private static final String INVALID_MODEL_ERROR_MESSAGE = "crn.invalidModelErrorMessage";

    private static final int PANEL_WIDTH = 300;

    private BreachController controller;

    private java.util.List offenceList;

    private AddedOffencesToBreachModel addedOffencesModel;

    private AddedOffencesPanelModel addedOffencesPanelModel;

    private XTable addedOffencesTable;

    private JPanel buttonPanel;

    private JButton addOffenceBtn;

    private JButton removeOffenceBtn;

    private XAction add;

    private XAction remove;

    public ChargeWizardModel model;

    private Object[] longValues = null;

    public AddedOffencesToBreachPanel(BreachController controller, ChargeWizardModel model)
            throws CSRecoverableException {
        super();
        if (controller == null || model == null){
            throw new IllegalArgumentException 
            ("AddedOffencesToBreachPanel - controller and model parameters must contain values");
        }
        this.controller = controller;
        this.model = model;
        this.offenceList = model.getAddedOffences();

        stepInitialise();
        jbInit();
        stepUpdateViewState();
    }

    private void jbInit() {
        this.setLayout(new GridBagLayout());
        add(new JScrollPane(getAddedOffencesTable()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        add(getButtonPanel(), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    protected XTable getAddedOffencesTable() {
        Dimension cfsTableDimension;

        if (addedOffencesTable == null) {
            addedOffencesTable = XTableFactory.getInstance().createMultiLineTable(addedOffencesModel);
            longValues = new Object[] {"20", XTable.COLUMN_WIDTH_UNDEFINED};

            cfsTableDimension = new Dimension(PANEL_WIDTH, getTableHeight(addedOffencesModel));

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

    private int getTableHeight(AbstractTableModel tableModel) {
        return 25 + (XHIBITConstant.TABLE_ROW_HEIGHT * tableModel.getRowCount());
    }

    private void actionListRowSelectionChanged(ListSelectionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("* * * * in actionListRowSelectionChanged   * * * * Row");
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

    private JButton getAddOffenceBtn() {
        if (addOffenceBtn == null) {
            addOffenceBtn = new JButton();
            addOffenceBtn.setAction(add);
        }
        return addOffenceBtn;
    }

    private JButton getRemoveOffenceBtn() {
        if (removeOffenceBtn == null) {
            removeOffenceBtn = new JButton();
            removeOffenceBtn.setAction(remove);
        }
        return removeOffenceBtn;
    }

    public int getOffenceCount() {
        if (offenceList != null) {
            return offenceList.size();
        } else {
            return 0;
        }
    }

    public BreachController getController() {
        return controller;
    }

    public void stepInitialise() throws CSRecoverableException {
        OffenceValue[] offences = new OffenceValue[offenceList.size()];
        addedOffencesModel = new AddedOffencesToBreachModel((OffenceValue[]) offenceList.toArray(offences));

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

    public void stepActivate() throws CSRecoverableException {
        OffenceValue[] offences = new OffenceValue[offenceList.size()];
        addedOffencesModel = new AddedOffencesToBreachModel((OffenceValue[]) offenceList.toArray(offences));
        getAddedOffencesTable().clearSelection();
        getAddedOffencesTable().setModel(addedOffencesModel);
        getAddedOffencesTable().initColumnSizes(longValues, 300);

        stepUpdateViewState();
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        // enable actions on parent stepUpdateViewState
        controller.stepUpdateViewState();
    }

    /**
     * Validates the table model. If the CRN field is empty and Auto CRN is not
     * selected an error message will be displayed
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if (addedOffencesTable.isEditing())
            addedOffencesTable.getCellEditor().stopCellEditing();
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            controller.stepDeinitialise();
        }
    }
}