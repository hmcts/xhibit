package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.RightJustifyTextRenderer;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XNumericLimitedLengthCellEditor;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class TakenIntoConsiderationPanel extends CourtLogEventPanel {
    private final TakenIntoConsiderationModel model;

    private TakenIntoConsiderationTableModel takenIntoConsiderationTableModel;

    private JScrollPane takenIntoConsiderationTableScrollPane = new JScrollPane();

    private XTable takenIntoConsiderationTable;

    private static final String resources = XhibitBundles.TakenIntoConsideration;

    // this variable is only ever set false when the model is passed an
    // invalid TIC
    private boolean ticsValid = true;

    private String caseType;

    public TakenIntoConsiderationPanel(XDialog parent, TakenIntoConsiderationModel model) throws CSRecoverableException {
        super(parent, model);
        this.model = model;
        this.caseType = model.getXac().getApplicationCaseModel().getCaseType();

        stepInitialise();
        jbInit();
        stepActivate();
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        // Save the data in the model
        HashMap defList = (HashMap) propertyMap.get("E20921_Defendant_List");
        Object o = defList.get("E20921_Defendant_Details");

        Integer caseId = model.getXac().getApplicationCaseModel().getCaseId();
        Collection defendants = new Vector();

        // Get the 'defendant on case' value object
        if (o instanceof HashMap) {
            defendants.add(buildDefendantRow(caseId, (HashMap) o));
        } else if (o instanceof Collection) {
            Iterator iter = ((Collection) o).iterator();
            while (iter.hasNext()) {
                defendants.add(buildDefendantRow(caseId, (HashMap) iter.next()));
            }
        }
        model.setDefendantCollection(defendants);
    }

    //
    // override abstract lifecycle methods from parent (XPanel)
    //

    public void stepInitialise() throws CSRecoverableException {
        log.debug("[stepInitialise]");

        if (model.isInEditMode()) {
            super.stepInitialise();
        } else {
            Integer caseId = model.getXac().getApplicationCaseModel().getCaseId();
            Iterator defendantValues = getCCDelegate().getDefendants(caseId).iterator();
            Collection defendants = new Vector();

            while (defendantValues.hasNext()) {
                DefendantValue defendantValue = (DefendantValue) defendantValues.next();

                // Get the 'defendant on case' value object
                Integer defendantId = defendantValue.getDefendantID();
                DefendantOnCaseValue defendantOnCase = getDefendantOnCaseDetails(defendantId, caseId);

                // Get the other data from the defendent value
                String defendantName = getFullName(defendantValue);

                // Add a new table row model (naming ???) to the defendants
                defendants.add(new TakenIntoConsiderationTableRowModel(defendantOnCase, defendantName));
            }

            model.setDefendantCollection(defendants);
        }

        loadTakenIntoConsiderationTableModel(model.getDefendantCollection());
    }

    private TakenIntoConsiderationTableRowModel buildDefendantRow(Integer caseId, HashMap defendantDetails)
            throws NumberFormatException, DefendantControllerException {
        Integer defendantId = new Integer((String) defendantDetails.get("E20921_Defendant_ID"));
        DefendantOnCaseValue defendantOnCase = getDefendantOnCaseDetails(defendantId, caseId);
        // Get the other data from the event log
        String defendantName = (String) defendantDetails.get("E20921_Defendant_Name");
        Integer defendantTICs = new Integer((String) defendantDetails.get("E20921_Defendant_TIC"));

        // Add a new table row model (naming ???) to the defendants
        return new TakenIntoConsiderationTableRowModel(defendantOnCase, defendantName, defendantTICs);
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        Iterator defendantModels = model.getDefendantCollection().iterator();
        while (defendantModels.hasNext()) {
            TakenIntoConsiderationTableRowModel defendantModel = (TakenIntoConsiderationTableRowModel) defendantModels
                    .next();

            appendProperties(propertyMap, defendantModel);
            defendantModel.getDefendantOnCase().setCourtId(XhibitSingleton.getInstance().getCourtId());
            if (model.isInEditMode()) {
                getDCDelegate().updateDefendantOnCaseDetails(defendantModel.getDefendantOnCase(),
                        XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            } else {
                getDCDelegate().updateDefendantOnCaseDetails(defendantModel.getDefendantOnCase(),
                        XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }
        }
    }

    // this methods will always be called when Ok is pressed
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // ensure that dialog is not closed until tics are valid
        if (!ticsValid) {
            ticsValid = true;
            throw new UserCancelException();
        }

        super.stepDeinitialise(update);
    }

    // end of lifecycle methods

    public JComponent getFirstEnterableComponent() {
        return getTakenIntoConsiderationTable();
    }

    //
    // private methods
    //

    private void loadTakenIntoConsiderationTableModel(Collection param) {
        XHIBITConstant.debug("loadTakenIntoConsiderationTableModel entered");
        takenIntoConsiderationTableModel = new TakenIntoConsiderationTableModel(param, caseType);
    }

    private void jbInit() {
        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(takenIntoConsiderationTableScrollPane, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 0), 0, 0));
        takenIntoConsiderationTableScrollPane.getViewport().add(getTakenIntoConsiderationTable(), null);
        // size the height of the table to max 10 rows + 1 for the header
        int scrollHeight = (Math.min(10, getTakenIntoConsiderationTable().getModel().getRowCount()) + 1)
                * (XHIBITConstant.getLineHeight() + 5);
        Dimension scrollDim = new Dimension(300, scrollHeight);
        takenIntoConsiderationTableScrollPane.setMinimumSize(scrollDim);
        takenIntoConsiderationTableScrollPane.setPreferredSize(scrollDim);
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 0), 0, 0));
    }

    private JTable getTakenIntoConsiderationTable() {
        if (takenIntoConsiderationTable == null) {
            takenIntoConsiderationTable = XTableFactory.getInstance().createDefaultTable(
                    takenIntoConsiderationTableModel);
            takenIntoConsiderationTable.setRowHeight(XHIBITConstant.getLineHeight());
            takenIntoConsiderationTable.setToolTipText(XHIBITConstant.getResource(XhibitBundles.TakenIntoConsideration,
                    "ttTICTable"));

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    takenIntoConsiderationTable.initColumnSizes(new String[] { XTableFactory.COLUMN_WIDTH_UNDEFINED,
                            "TIC" }, takenIntoConsiderationTable.getPreferredSize().width);
                }
            });

            XNumericLimitedLengthCellEditor cellEditor = new XNumericLimitedLengthCellEditor(4);
            RightJustifyTextRenderer cellRenderer = new RightJustifyTextRenderer();
            TableColumn delayCol = takenIntoConsiderationTable.getColumnModel().getColumn(
                    TakenIntoConsiderationTableModel.TIC_COLUMN);
            delayCol.setCellEditor(cellEditor);
            delayCol.setCellRenderer(cellRenderer);

            takenIntoConsiderationTable.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    // this only fires when the user clicks ONCE in a table
                    // cell then edits
                    // if they DOUBLE-click, maybe the cell editor gets
                    // focus rather than the table?
                    stepUpdateViewStateHandleExceptions();
                }
            });
            takenIntoConsiderationTable.getTableHeader().setReorderingAllowed(false);
        }
        return takenIntoConsiderationTable;
    }

    protected boolean isMandatoryFieldsCompleted() {
        return true;
    }

    // i think this method is intended to ensure that any checks in the
    // models 'setValueAt'
    // method are run before Ok is pressed. this is most important if the
    // user goes directly
    // from entering an invalid TIC value to pressing Ok
    private void setEditingValue() {
        JTable table = getTakenIntoConsiderationTable();

        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        model.setDefendantCollection(unloadTakenIntoConsiderationTableModel());
    }

    private Collection unloadTakenIntoConsiderationTableModel() {
        return takenIntoConsiderationTableModel.getDefendantCollection();
    }

    private void appendProperties(Map propertyMap, TakenIntoConsiderationTableRowModel defendantModel) {
        HashMap ticOptionsType = new HashMap();
        ticOptionsType.put("E20921_Defendant_ID", defendantModel.getDefendantId());
        ticOptionsType.put("E20921_Defendant_Name", defendantModel.getFullName());
        ticOptionsType.put("E20921_Defendant_TIC", String.valueOf(defendantModel.getNoOfTics()));

        HashMap map = (HashMap) propertyMap.get("E20921_Defendant_List");
        if (map == null) {
            map = new HashMap();
            map.put("E20921_Defendant_Details", ticOptionsType);
            propertyMap.put("E20921_Defendant_List", map);
        } else {
            Object prop = map.get("E20921_Defendant_Details");
            if (prop instanceof HashMap) {
                ArrayList al = new ArrayList();
                al.add(prop);
                al.add(ticOptionsType);
                map.put("E20921_Defendant_Details", al);
            } else if (prop instanceof Collection) {
                ((Collection) prop).add(ticOptionsType);
            }
        }

        log.debug("E20921_Defendant_TIC: " + defendantModel.getNoOfTics());
    }

    //
    // Entity Management
    //

    private static String getFullName(DefendantValue value) {
        return value.getFirstName() + " " + value.getMiddleName() + " " + value.getSurName();
    }

    private static DefendantOnCaseValue getDefendantOnCaseDetails(Integer defendantId, Integer caseId)
            throws DefendantControllerException {
        return getDCDelegate().getDefendantOnCaseDetails(defendantId, caseId);
    }

    //
    // Delegate Management
    //
    private static CaseControllerBeanBusinessDelegate getCCDelegate() {
        return XhibitDelegateHelper.getCaseDelegate();
    }

    private static DefendantControllerBeanBusinessDelegate getDCDelegate() {
        return XhibitDelegateHelper.getDefendantDelegate();
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
        setEditingValue();
        TakenIntoConsiderationTableModel myModel = (TakenIntoConsiderationTableModel) takenIntoConsiderationTable
                .getModel();
        for (int x = 0; x < myModel.getRowCount(); x++) {
            String defendantName = (String) myModel.getValueAt(x,
                    TakenIntoConsiderationTableModel.DEFENDANT_NAME_COLUMN);
            Integer ticValue = (Integer) myModel.getValueAt(x, TakenIntoConsiderationTableModel.TIC_COLUMN);

            if (ticValue == null) {
                throw new CSValidationException("Court_Log.Invalid_TIC_Value", new Object[] { defendantName },
                        "invalid tic value");
            }
        }
    }
}
