package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.awt.Component;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JTable;

import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellEditor;

/**
 * <p>
 * Title: AppealResultsComboBoxEditor
 * </p>
 * <p>
 * Description: Combo editor for criminal appeal result types
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 * @version 1.0
 */

public class AppealResultsComboBoxEditor extends XDefaultComboBoxCellEditor {
    private Collection appealOffenceRefData;

    /**
     * Creates an Appeal Results ComboBox Editor
     * 
     * @param combo
     *            the combo to create the component with
     */
    public AppealResultsComboBoxEditor(JComboBox combo, Collection appealOffenceRefData) {
        super(combo);
        this.appealOffenceRefData = appealOffenceRefData;
    }

    /**
     * gets the editor component
     * 
     * @param table
     *            the table the component is on
     * @param value
     *            the value in the cell
     * @param isSelected
     *            whether the cell is selected
     * @param row
     *            the row we are drawing for
     * @param column
     *            the column we are drawing for
     * @return the component
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) table.getModel();
        ResultsRowValue rrv = (ResultsRowValue) tableModel.getDataAt(row);

        RefAppResultBasicValue rscbv = AppealResultsHelper.getRefAppResultBasicValue(rrv, appealOffenceRefData);

        if (rscbv != null) {
            ((JComboBox) getComponent()).setSelectedItem(rscbv);
        } else {
            ((JComboBox) getComponent()).setSelectedIndex(0);
        }

        setCellBackground(table, isSelected);

        return getPanel();
    }
}