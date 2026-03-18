package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Model for AddedOffencesToBreachPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
public class AddedOffencesToBreachModel extends XHIBITDefaultTableModel {
    private final static Logger log = CSServices.getLogger(AddedOffencesToBreachModel.class);

    public static final int CODE_COLUMN = 0;

    public static final int DESCRIPTION_COLUMN = 1;

    private ResourceBundle resources = null;

    private String uncodedOffenceCodeMask;

    public AddedOffencesToBreachModel(Object[] data) {
        super();
        loadResources();
        setOffences((OffenceValue[]) data);
    }

    public void setOffences(OffenceValue[] offences) {
        // Transforms the data into a format that can be renderered by a table
        AddOffencesTableEntry[] offenceValueEntries = new AddOffencesTableEntry[offences.length];

        // Create Table entry objects for each defendant
        for (int i = 0; i < offences.length; i++) {
            AddOffencesTableEntry entry = new AddOffencesTableEntry(offences[i], true);

            offenceValueEntries[i] = entry;
        }

        this.setData(offenceValueEntries);
    }

    public Object getValueAt(int r, int c) {
        if (_data.length <= 0)
            return null;

        switch (c) {
        case CODE_COLUMN:
            final String offenceCode = ((AddOffencesTableEntry) _data[r]).getOffenceCode();
            return (offenceCode.equalsIgnoreCase(UncodedOffenceInterface.UNCODED_OFFENCE_REFERENCE_CODE) ? uncodedOffenceCodeMask
                    : offenceCode);

        case DESCRIPTION_COLUMN:
            return ((AddOffencesTableEntry) _data[r]).getOffenceDescription();

        default:
            break;
        }
        return null;
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int r, int c) {
        switch (c) {
        // Get Defendant Column
        case CODE_COLUMN:
            return false;

            // get is onCount Column
        case DESCRIPTION_COLUMN:
            return false;

        default:
            log.error("Column index invalid > than number of columns in  the data");
        }

        return false;

    }

    public void setCellEditable(int r, int c, boolean editable) {
        switch (c) {
        // Get Defendant Column
        case CODE_COLUMN:
            break;

        // get is onCount Column
        case DESCRIPTION_COLUMN:
            break;

        default:
            log.error("Column index invalid > than number of columns in  the data");
        }
    }

    public void setValueAt(Object aValue, int r, int c) {
        this.fireTableDataChanged();
    }

    /**
     * Reads table column labels and error messages from a resource bundle
     */
    private void loadResources() {
        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);

        String[] columnNames = { XHIBITConstant.getResource(resources, "columnOffenceCode"),
                XHIBITConstant.getResource(resources, "columnOffenceDesc") };
        super.setColumnNames(columnNames);

        uncodedOffenceCodeMask = XHIBITConstant.getResource(resources, "uncodedOffenceCodeMask");
    }

    /**
     * Returns Offences held in the table. Each Offence should contain a CRN
     * 
     * @return
     */
    public Vector getOffences() {
        Vector offenceValues = new Vector();

        for (int i = 0; i < this.getRowCount(); i++) {
            // Get Row t
            AddOffencesTableEntry entry = ((AddOffencesTableEntry) _data[i]);
            offenceValues.add(entry.getOffence());
        }

        return offenceValues;
    }
}