package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title: Panel for offences added to fail to appear breach.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Logica
 */
public class AddedOffenceToFail2AppearPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private static final String ADD_BAIL_ACT_OFFENCE_ERROR_MESSAGE = "gui.user.addbailactoffence.morethanoneoffence";
    private static final int PANEL_WIDTH = 300;

    private BreachController controller;
    private List<OffenceValue> offenceList;
    private AddedOffencesToBreachModel addedOffencesModel;
    private AddedOffencesPanelModel addedOffencesPanelModel;
    public BreachWizardModel model;
 
    private XTable addedOffencesTable;
    private JPanel buttonPanel;
    private JButton addOffenceBtn;
    private JButton removeOffenceBtn;

    private XDatePanel originalOrderDate;
    private JPanel originalOrderDatePanel;
    private XAction add;
    private XAction remove;
   
    private Object[] longValues = null;

    public AddedOffenceToFail2AppearPanel(BreachController controller, BreachWizardModel model)
            throws CSRecoverableException {
        super();
        if (controller == null || model == null){
            throw new IllegalArgumentException 
            ("AddedOffenceToFail2AppearPanel - controller and model parameters must contain values");
        }
        this.controller = controller;
        this.model = model;
        this.offenceList = model.getAddedOffences();

        stepInitialise();
        jbInit();
        stepUpdateViewState();
    }
    

    public void stepInitialise() throws CSRecoverableException {
        OffenceValue[] offences = new OffenceValue[offenceList.size()];
        addedOffencesModel = new AddedOffencesToBreachModel(offenceList.toArray(offences));

        // create model for actions
        addedOffencesPanelModel = new AddedOffencesPanelModel(offenceList, this, model);
        addedOffencesPanelModel.setChargeWizardModel(model);

        // set up actions
        add = XhibitActions.getAction(controller.getXac(), XhibitActions.AddWizardBailActOffence);
        add.setModel(addedOffencesPanelModel);
        remove = XhibitActions.getAction(controller.getXac(), XhibitActions.RemoveWizardBreachOffence);
        remove.setModel(addedOffencesPanelModel);
        
        setButtons();
        moveModelToScreen();
    }

    private void jbInit() {
        this.setLayout(new GridBagLayout());
        // adds the main pane area
        add(new JScrollPane(getAddedOffencesTable()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        
        // adds the main buttons area
        add(getButtonPanel(), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        
        // PR6014 allow original order date to be provided
        add(getOriginalOrderDatePanel(), new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JPanel getOriginalOrderDatePanel() {
        if (originalOrderDatePanel == null) {
            originalOrderDatePanel = new JPanel();
            originalOrderDatePanel.add(new JLabel(getString("originalOrderDateLabel")));
            originalOrderDatePanel.add(getOriginalOrderDate());
        }
        return originalOrderDatePanel;
    }
    
    private XDatePanel getOriginalOrderDate() {
        if (originalOrderDate == null) {
            Calendar blankDate = null;
            originalOrderDate = new XDatePanel(this, blankDate);
            originalOrderDate.setRequired(true);
        }
        return originalOrderDate;
    }
    
    public void stepUpdateViewState() throws CSRecoverableException {
        // enable actions on parent stepUpdateViewState
        controller.stepUpdateViewState();
    }
    
    private void setButtons()throws CSRecoverableException  {
        /* Only one Offence can be added to a Failure to Appear Breach */
        if(addedOffencesPanelModel.getOffenceList().size() == 0){
            add.setEnabled(true);
            remove.setEnabled(false);
        } else if(addedOffencesPanelModel.getOffenceList().size() == 1){
            add.setEnabled(false);
            remove.setEnabled(true);
        } else {
            String errorMessage = ResourceBundleHelper.getResource(
                    XhibitBundles.ErrorText, ADD_BAIL_ACT_OFFENCE_ERROR_MESSAGE);
            throw new CSValidationException(ADD_BAIL_ACT_OFFENCE_ERROR_MESSAGE, errorMessage);
        }
    }

   
    protected XTable getAddedOffencesTable() {
        if (addedOffencesTable == null) {
            addedOffencesTable = XTableFactory.getInstance().createMultiLineTable(addedOffencesModel);
            longValues = new Object[] {"20", XTable.COLUMN_WIDTH_UNDEFINED};

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

    private int getOffenceCount() {
        if (offenceList != null) {
            return offenceList.size();
        } else {
            return 0;
        }
    }
    
    public boolean isMandatoryFieldsCompleted() {
        return getOffenceCount() > 0 
            && getOriginalOrderDate().isMandatoryFieldsCompleted();
    }

    public void stepActivate() throws CSRecoverableException {
        OffenceValue[] offences = new OffenceValue[offenceList.size()];
        addedOffencesModel = new AddedOffencesToBreachModel(offenceList.toArray(offences));
        getAddedOffencesTable().clearSelection();
        getAddedOffencesTable().setModel(addedOffencesModel);
        getAddedOffencesTable().initColumnSizes(longValues, 300);
        setButtons();        
        stepUpdateViewState();
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if (addedOffencesTable.isEditing()) {
            addedOffencesTable.getCellEditor().stopCellEditing();
        }
        
        getOriginalOrderDate().stepValidate();
        
        if (getOriginalOrderDate().getDate() != null 
                && !getOriginalOrderDate().getDate().before(Calendar.getInstance())) {
            JOptionPane.showMessageDialog(controller.getXac(), 
                    getString("messageOriginalOrderDate"), 
                    getString("messageTitlePlea"),
                    JOptionPane.ERROR_MESSAGE);
            throw new UserCancelException();
        }
        
        moveScreenToModel();
    }

    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            controller.stepDeinitialise();
        }
    }
    
    private void moveModelToScreen() {
        getOriginalOrderDate().setDate(model.getBreachValue().getOriginalSentenceDate());
    }
    
    private void moveScreenToModel() throws CSValidationException {
        Calendar calendar = getOriginalOrderDate().getDate();
        model.getBreachValue().setOriginalSentenceDate(calendar);
    }
    
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.Breaches, key);
    }
}