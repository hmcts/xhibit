package uk.gov.courtservice.xhibit.client.importexportnotification;

import java.awt.SystemColor;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportCaseCourtDetailsValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportStatusVO;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

/**
 * <p>
 * Title: Import/Export Notification Helper
 * </p>
 * <p>
 * Description: This class is the general purpose helper for this package
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully, Marie Holmberg
 * @version 1.0
 */

public class ImportExportNotificationHelper {

    /**
     * static int for the court tab.
     */
    public static final int COURT_TAB = 0;

    /**
     * static int for the case tab.
     */
    public static final int CASE_TAB = 1;

    /**
     * Sorts a collection of objects in descending startDate/transaction order.
     * The object must implement compareTo for the comparison to work
     * 
     * @param unsortedList -
     *            the data collection to be sorted
     */
    // public void sortByStartDate( Collection unsortedList ) {
    // Sorter.sort( (List)unsortedList,
    // new String[] { "startDate", "transaction" },
    // Sorter.DESCENDING );
    // }
    /**
     * Sorts a collection of objects in ascending transaction/startDate order.
     * The object must implement compareTo for the comparison to work
     * 
     * @param unsortedList -
     *            the data collection to be sorted
     */
    public void sortByTransaction(Collection unsortedList) {
        Sorter.sort((List) unsortedList, new String[] { "transaction", "startDate" }, Sorter.ASCENDING);
    }

    /**
     * Reloads the data in the table
     * 
     * @param table -
     *            the table to be reloaded
     * @param data -
     *            the data it should now contain
     */
    public void redisplayTable(XTable table, Collection data) {
        XHIBITTableModelInterface model = (XHIBITTableModelInterface) table.getModel();
        model.setData(data);
        table.tableChanged(new TableModelEvent(model));
    }

    /**
     * Enables/disables a textfield setting the background colour to indicate
     * whether or not the field is editable
     * 
     * @param textField -
     *            the text field to be acted upon
     * @param state -
     *            the edit/enable state of the field
     */
    public void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? SystemColor.white : SystemColor.text));
    }

    /**
     * Method to make sure nulls are not displayed.
     * 
     * @param param
     * @return String
     */
    public String tidyUp(String param) {
        return (param == null ? "" : param);
    }

    /**
     * Method to build the defendant name. The defendant name will be in format:
     * surname, firsname initials
     * 
     * @param surname
     *            String
     * @param firstname
     *            String
     * @param initials
     *            String
     * @return String the entire name.
     */
    public String buildName(String surname, String firstname, String initials) {
        StringBuffer name = new StringBuffer();

        // add the surname
        if (surname != null) {
            name.append(surname);
        }
        // add firstname and any formatting
        if (firstname != null) {
            if (name.length() > 0) {
                name.append(", ");
            }
            name.append(firstname);
        }
        // add intitials and any formatting
        if (initials != null) {
            if (name.length() > 0) {
                name.append(" ");
            }
            name.append(initials);
        }
        return name.toString();
    }

    /**
     * Returns a Collection of ImportExportNotificationTableRowModel objects
     * using a Collection of ImportExportCaseCourtDetailsValue as raw data
     * 
     * @param rawData
     * @return Collection
     */
    public Collection convertToTableModel(Collection rawData) {
        Vector ienTableRowModels = new Vector();

        Iterator iter = rawData.iterator();

        while (iter.hasNext()) {
            ImportExportNotificationTableRowModel row = new ImportExportNotificationTableRowModel();
            ImportExportCaseCourtDetailsValue value = (ImportExportCaseCourtDetailsValue) iter.next();
            ImportExportStatusVO impExpStaVO = value.getImportExportStatusValueObject();

            row.setTransaction(impExpStaVO.getTypeCode());
            row.setStatus(impExpStaVO.getStatusCode());
            row.setStartDate(impExpStaVO.getCreationDate());
            row.setEndDate(impExpStaVO.getLastUpdateDate());
            row.setDescription(tidyUp(impExpStaVO.getMessage()));

            // set the defendant name after it has been built in the right
            // format.
            row
                    .setDefendantName(this.buildName(value.getDefSurname(), value.getDefFirstname(), value
                            .getDefInitials()));

            ienTableRowModels.add(row);
        }

        return ienTableRowModels;
    }
}
