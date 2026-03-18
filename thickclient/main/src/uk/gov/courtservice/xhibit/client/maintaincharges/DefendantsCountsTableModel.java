package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CrnValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: AddDefendantsToCountTableModel
 * </p>
 * <p>
 * Description: Table model that holds information that allows a user to
 * Defendants not already on a count to a Count.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author AW Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 *
 * 31-07-2003 AW Daley Initial Version
 *
 * 27-08-2003 AW Daley getDefendantIds method added
 * getDefendantsOnOffenceBasicValues method added
 *
 * 29-08-2003 AW Daley XTable accesses the first row in the table by calling
 * getValueAt() in its initColumns method. A check has been added to ensures
 * that if the table is empty getValueAt()returns null
 *
 * 02-09-2003 AW Daley Modified so that the model can handle null CRNs
 */
public class DefendantsCountsTableModel extends XHIBITDefaultTableModel {
    private final static Logger log = CSServices.getLogger(DefendantsCountsTableModel.class);

    public final static int VALUE_COLUMN_NO = 0;

    public final static int ON_COUNT_COLUMN_NO = 1;

    public final static int CRN_COLUMN_NO = 2;

    public final static int AUTO_CRN_COLUMN_NO = 3;

    private final static String CRN_COLUMN_NAME_KEY = "crn";

    private final static String AUTO_CRN_COLUMN_NAME_KEY = "autoCRN";

    public final static String INVALID_CRN_ERROR_MESSAGE_KEY = "crn.invalidCRNErrorMessage";

    public static String INVALID_CRN_ERROR_TITLE_KEY = "crn.invalidCRNErrorTitle";

    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);

    private ResourceBundle errorResources = XHIBITConstant.getResourceBundle(XhibitBundles.ErrorText);

    private String errorMessage;

    private String errorTitle;

    /**
     * Constructor for when model needs to hold OffenceValues
     *
     * @param offences
     * @param columnLabel
     * @param selectColumnLabel
     */
    public DefendantsCountsTableModel(OffenceValue[] offences, String columnLabel, String selectColumnLabel) {
        super();
        this.loadResources(columnLabel, selectColumnLabel);
        setOffences(offences);
    }

    /**
     * Constructor for when model needs to hold DefendantValues
     *
     * @param defendants
     * @param columnLabel
     * @param selectColumnLabel
     */
    public DefendantsCountsTableModel(DefendantValue[] defendants, String columnLabel, String selectColumnLabel) {
        super();
        this.loadResources(columnLabel, selectColumnLabel);
        this.setDefendants(defendants);
    }

    public void setDefendants(DefendantValue[] defendants) {
        // Transforms the data into a format that can be renderered by a table
        DefendantValueTableEntry[] defendantValueEntries = new DefendantValueTableEntry[defendants.length];

        // Create Table entry objects for each defendant
        for (int i = 0; i < defendants.length; i++) {
            DefendantValueTableEntry entry = new DefendantValueTableEntry(defendants[i], false, true);

            defendantValueEntries[i] = entry;
        }

        this.setData(defendantValueEntries);
    }

    public void setOffences(OffenceValue[] offences) {
        // Transforms the data into a format that can be renderered by a table
        OffenceValueTableEntry[] offenceValueEntries = new OffenceValueTableEntry[offences.length];

        // Create Table entry objects for each defendant
        for (int i = 0; i < offences.length; i++) {
            OffenceValueTableEntry entry = new OffenceValueTableEntry(offences[i], false, true);

            offenceValueEntries[i] = entry;
        }

        this.setData(offenceValueEntries);
    }

    /**
     * Overides DefaultXHIBITTableModel method
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        // XTable accesses the first row in the table by calling this method in
        // its initColumns method This check ensures that if the table is empty
        // this method returns null
        if (isTableEmpty())
            return null;

        // Get Row
        DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(rowIndex);

        switch (columnIndex) {
        // Get Defendant Column
        case VALUE_COLUMN_NO:
            return entry.getDisplayValue();

            // get is onCount Column
        case ON_COUNT_COLUMN_NO:
            return entry.isSelected();

            // Get CRN column
        case CRN_COLUMN_NO:
            return entry.getCommonRefNo();

            // Get Auto Generate CRN column
        case AUTO_CRN_COLUMN_NO:
            return entry.isAutoGenerate();

        default:
            log.error("Column index invalid > than number of columns in  the data");

        }

        return null;
    }

    /**
     * Overides DefaultXHIBITTableModel method
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Checks if table has data
        if (isTableEmpty())
            return false;

        // Get Row to edit
        DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(rowIndex);

        switch (columnIndex) {
        // Get Defendant Column
        case VALUE_COLUMN_NO:
            return false;

            // get is onCount Column
        case ON_COUNT_COLUMN_NO:
            return true;

            // Get CRN column
        case CRN_COLUMN_NO:
            return entry.isCommonRefNoEnabled();

            // Get Auto Generate CRN column
        case AUTO_CRN_COLUMN_NO:
            return entry.isAutoGenerateEnabled();

        default:
            log.error("Column index invalid > than number of columns in  the data");
        }

        return false;

    }

    /**
     * Controls which cells in the table are editable.
     *
     * @param rowIndex
     * @param columnIndex
     * @param editable
     */
    public void setCellEditable(int rowIndex, int columnIndex, boolean editable) {
        // Checks if table has data
        if (isTableEmpty())
            return;

        // Get Row to edit
        DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(rowIndex);

        switch (columnIndex) {
        // Get Defendant Column
        case VALUE_COLUMN_NO:
            break;

        // get is onCount Column
        case ON_COUNT_COLUMN_NO:
            break;

        // Get CRN column
        case CRN_COLUMN_NO:
            entry.setCommonRefNoEnabled(editable);
            break;

        // Get Auto Generate CRN column
        case AUTO_CRN_COLUMN_NO:
            entry.setAutoGenerateEnabled(editable);
            break;

        default:
            log.error("Column index invalid > than number of columns in  the data");
        }

    }

    /**
     * Overides DefaultXHIBITTableModel method
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String methodName = "setValueAt";

        // Checks if table has data
        if (isTableEmpty())
            return;

        // Get Row to edit
        DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(rowIndex);

        switch (columnIndex) {
        // set is onCount Column
        case ON_COUNT_COLUMN_NO:
            entry.setOnCount((Boolean) aValue);

            // Also clear CRN field
            entry.clearCommonRefNo();
            break;

        // set CRN column
        case CRN_COLUMN_NO:

            // Validate CRN
            if (((String) aValue).equals("")) {
                entry.setCommonRefNo((String) aValue);
                break;
            }

            if (CrnValue.isValid((String) aValue))
                entry.setCommonRefNo((String) aValue);
            else {
                log.equals(methodName + ": Invalid CRN " + (String) aValue);

                // Display error message. Not sure if this should be done from
                // the model. A clean solution maybe to follow MVC
                // and fire an event to the View.
                JOptionPane.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            break;

        // set Auto Generate CRN column
        case AUTO_CRN_COLUMN_NO:
            entry.setAutoGenerate((Boolean) (aValue));

            // Also clear CRN field
            entry.clearCommonRefNo();
            break;

        default:
            log.error("Column index invalid > than number of columns in  the data");
            return;
        }

        // Set the table state
        setTableSwitches(rowIndex, columnIndex);

        // To ensure that the table updates its view
        this.fireTableDataChanged();
    }

    /**
     * Overides DefaultXHIBITTableModel method Returns the columnn name
     *
     * @param columnIndex
     * @return
     */
    public String getColumnName(int columnIndex) {
        return this.getColumnNames()[columnIndex];
    }

    /**
     * Overides DefaultXHIBITTableModel method Returns the column class
     *
     * @param columnIndex
     * @return
     */
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == ON_COUNT_COLUMN_NO || columnIndex == AUTO_CRN_COLUMN_NO)
            return Boolean.class;
        else
            return String.class;
    }

    /**
     * Reads table column labels and error messages from a resource bundle
     *
     * @param columnLabel
     * @param selectColumnLabel
     */
    private void loadResources(String columnLabel, String selectColumnLabel) {
        super.setColumnNames(new String[] { columnLabel, selectColumnLabel,
                XHIBITConstant.getResource(resources, DefendantsCountsTableModel.CRN_COLUMN_NAME_KEY),
                XHIBITConstant.getResource(resources, DefendantsCountsTableModel.AUTO_CRN_COLUMN_NAME_KEY) });

        errorMessage = XHIBITConstant.getResource(errorResources,
                DefendantsCountsTableModel.INVALID_CRN_ERROR_MESSAGE_KEY);

        errorTitle = XHIBITConstant.getResource(errorResources, DefendantsCountsTableModel.INVALID_CRN_ERROR_TITLE_KEY);
    }

    /**
     * Returns a flag to indicate if the defendant is on the count
     *
     * @param rowIndex
     * @return
     */
    public boolean isOnCount(int rowIndex) {
        // Checks if table has data
        if (isTableEmpty())
            return false;

        // Get Row
        DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(rowIndex);

        return entry.isSelected().booleanValue();
    }

    /**
     * Returns a flag to indicate if the CRN is to be automatically generated
     * for the defendant
     *
     * @param rowIndex
     * @return
     */
    public boolean isAutoGenerate(int rowIndex) {
        // Checks if table has data
        if (isTableEmpty())
            return true;

        // Get Row
        DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(rowIndex);

        return entry.isAutoGenerate().booleanValue();
    }

    /**
     * Sets the state of the table. Set Not On Count State, Set On Count and
     * Auto Generate CRN State or Set On Count and Input CRN State
     *
     * @param rowNumber
     * @param colNumber
     */
    private void setTableSwitches(int rowNumber, int colNumber) {

        // Determine if onCount
        if (!isOnCount(rowNumber)) {
            // Set Not On Count State. Disable CRN and Auto Generate CRN
            // Input
            setCellEditable(rowNumber, CRN_COLUMN_NO, false);
            setCellEditable(rowNumber, AUTO_CRN_COLUMN_NO, false);

        } else if (isAutoGenerate(rowNumber)) {
            // Set On Count and Auto Generate CRN State. Disable CRN input
            // and enable Auto Generate CRN Input
            setCellEditable(rowNumber, AUTO_CRN_COLUMN_NO, true);
            setCellEditable(rowNumber, CRN_COLUMN_NO, false);
        } else {
            // Set On Count and Input CRN State. Enable CRN input and enable
            // Auto Generate CRN Input
            setCellEditable(rowNumber, AUTO_CRN_COLUMN_NO, true);
            setCellEditable(rowNumber, CRN_COLUMN_NO, true);
        }
    }

    /**
     * Returns the DefendantOnOffenceValues containing the CRN
     *
     * @param id
     * @return
     */
    public Collection getDefenantOnOffenceValues(Integer id) {
        ArrayList defendantOnOffenceValues = new ArrayList();

        for (int i = 0; i < this.getRowCount(); i++) {
            // Get Row t
            DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(i);

            if (entry.isSelected().booleanValue())
                defendantOnOffenceValues.add(entry.getDefendantOnOffenceValue(id));
        }

        return defendantOnOffenceValues;
    }

    public HashMap getDefendantsOnOffenceBasicValues(Integer offenceId) {
        HashMap defendantOnOffenceValuesMap = new HashMap();

        Iterator iter = getDefenantOnOffenceValues(offenceId).iterator();

        while (iter.hasNext()) {
            DefendantOnOffenceValue defOnOffence = ((DefendantOnOffenceValue) iter.next());

            Integer defendantId = defOnOffence.getDefendantId();

            // Create defendant on offence to hold CRN
            XhbDefendantOnOffenceBasicValue defOnOffenceBasicVO = new XhbDefendantOnOffenceBasicValue();
            defOnOffenceBasicVO.setCrnId(defOnOffence.getCrn());

            defendantOnOffenceValuesMap.put(defendantId, defOnOffenceBasicVO);
        }

        return defendantOnOffenceValuesMap;
    }

    /**
     * Retruns id for defendants on the offence
     *
     * @param id
     * @return
     */
    public Vector getDefenantIds(Integer id) {
        Iterator iter = getDefenantOnOffenceValues(id).iterator();
        Vector defendantIds = new Vector();

        while (iter.hasNext()) {
            Integer defId = ((DefendantOnOffenceValue) iter.next()).getDefendantId();
            defendantIds.addElement(defId);
        }

        return defendantIds;
    }

    /**
     * Return The selected OffenceValues or DefendantOnOffenceValues depending
     * on which constructor was used to create the model.
     *
     * @return
     */
    public Collection getSelectedValues() {
        ArrayList values = new ArrayList();

        for (int i = 0; i < this.getRowCount(); i++) {
            // Get Row t
            DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(i);

            if (entry.isSelected().booleanValue())
                values.add(entry.getValue());
        }

        return values;
    }

    /**
     * Checks to see if any defendants or counts are selected
     *
     * @return true if empty
     */
    public boolean isSelectionEmpty() {
        boolean empty = true;

        for (int i = 0; i < this.getRowCount(); i++) {
            // Get Row t
            DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(i);

            if (entry.isSelected().booleanValue())
                return false;
        }

        return empty;
    }

    /**
     * Returns the number selected of defendants or offences selected
     *
     * @return
     */
    public int getNumberAutoCRNs() {
        int numberSelected = 0;

        for (int i = 0; i < this.getRowCount(); i++) {
            // Get Row t
            DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(i);

            // If selected and auto CRN selected
            if (entry.isSelected().booleanValue() && entry.isAutoGenerate().booleanValue())
                numberSelected++;
        }

        return numberSelected;
    }

    /**
     * Add all defendants to count or add all counts to defendants
     */
    public void selectAll() {
        for (int i = 0; i < this.getRowCount(); i++)
            setValueAt(new Boolean(true), i, ON_COUNT_COLUMN_NO);
    }

    /**
     * Sets the automatically generated CRNS in the Table.
     *
     * @param generatedCRNs
     */
    public void setCRNs(CrnValue[] generatedCRNs) {
        int crnIndex = 0;

        for (int row = 0; row < this.getRowCount(); row++) {
            // Get Row t
            DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(row);

            // If selected and auto CRN selected then set the CRN
            if (entry.isSelected().booleanValue() && entry.isAutoGenerate().booleanValue()) {
                String strCRN = generatedCRNs[crnIndex].getCrnStr();
                this.setValueAt(strCRN, row, DefendantsCountsTableModel.CRN_COLUMN_NO);
                crnIndex++;
            }
        }

    }

    /**
     * Validates the table model. Each entry that is selected must be set to
     * auto generate a CRN of have a CRN specified
     *
     * @return
     */
    public boolean isValid() {
        for (int row = 0; row < this.getRowCount(); row++) {
            // Get Row t
            DefendantsCountsTableEntry entry = (DefendantsCountsTableEntry) this.getDataAt(row);

            // If selected and auto CRN selected then set the CRN
            if (entry.isSelected().booleanValue() && !entry.isAutoGenerate().booleanValue()) {
                String strCRN = entry.getCommonRefNo();

                // Valid for CRN not to be entered
                if (strCRN == null)
                    continue;

                if (!CrnValue.isValid(strCRN))
                    return false;
            }
        }

        return true;
    }

    /**
     * Checks if the table model contains rows. Should not realy require this
     * method if the getRowCount returns zero then the getters on the table
     * model should not be called.
     *
     * @return true - empty
     */
    private boolean isTableEmpty() {
        if (this._data.length <= 0)
            return true;
        else
            return false;
    }
}