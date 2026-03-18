package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.Component;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JTable;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellEditor;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;

/**
 * <p>
 * Title:
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
 * @author Simon Gilmore
 * @version 1.0
 */

public class PleaComboBoxEditor extends XDefaultComboBoxCellEditor {
    private Collection refData;

    /**
     * Creates a PleaComboBoxEditor
     * 
     * @param combo
     *            the JComboBox to use as an editor.
     * @param pleaRefData
     *            the Plea reference data.
     */
    public PleaComboBoxEditor(JComboBox combo, Collection pleaRefData) {
        super(combo);
        refData = pleaRefData;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        log.debug("[PleaComboBoxEditor] getTableCellEditorComponent: ");

        XHIBITDefaultTableModel tableModel = (XHIBITDefaultTableModel) table.getModel();
        ResultsRowValue rrv = (ResultsRowValue) tableModel.getDataAt(row);
        PleaValue pv = rrv.getPleaValue();

        JComboBox combo = (JComboBox) getEditor();
        if (pv == null) {
            combo.setSelectedIndex(0);
        } else {
            if (pv.getRefPleaId() == null) {
                combo.setSelectedIndex(0);
            } else {
                Integer i = pv.getRefPleaId();
                log.debug("[PleaComboBoxEditor] getTableCellEditorComponent: pleaId = " + i);

                RefSystemCodeBasicValue ref = PleaHelper.getRefSystemCodeBasicValue(i, refData);
                // combo.setSelectedItem(ref);
                log.debug("[PleaComboBoxEditor] getTableCellEditorComponent: " + "\nref.getId()     = " + ref.getId()
                        + "\nref.getCode()   = " + ref.getCode() + "\nref.getDecode() = " + ref.getDecode());
                if (ref.getId().equals(new Integer(0)) && ref.getCode().equals("") && ref.getDecode().equals("")) {
                    log.debug("All are zero or blank");
                }
                combo.setSelectedItem(ref.getDecode());
            }
        }

        setCellBackground(table, isSelected);
        return getPanel();
    }

}