package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellEditor;

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
 * @version $Id: VerdictComboBoxEditor.java,v 1.13 2005/02/15 14:54:54 sz0t7n
 *          Exp $
 */

public class VerdictComboBoxEditor extends XDefaultComboBoxCellEditor {
    // Each row will get its own combo box model.
    private HashMap comboModelCache = new HashMap();

    private VerdictRestrictionHelper verdictRestrictionHelper;

    /**
     * Creates a VerdictComboBoxEditor
     * 
     * @param combo
     *            the combo box with choices
     * @param verdictRefData
     *            the verdict reference data.
     */
    public VerdictComboBoxEditor(JComboBox combo, Collection verdictRefData) {
        super(combo);
        verdictRestrictionHelper = VerdictRestrictionHelper.getInstance(verdictRefData);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        log.debug("[VerdictComboBoxEditor] getTableCellEditorComponent: ");

        final JComboBox combo = (JComboBox) getEditor();

        DefaultComboBoxModel cmbModel = getComboBoxModel(table, row);
        combo.setModel(cmbModel);
        combo.setSelectedItem(value);

        setCellBackground(table, isSelected);

        return getPanel();
    }

    private DefaultComboBoxModel getComboBoxModel(JTable table, int row) {
        Integer rowObject = new Integer(row);
        if (comboModelCache.containsKey(rowObject)) {
            return (DefaultComboBoxModel) comboModelCache.get(rowObject);
        } else {
            ResultsRowValue rrv = (ResultsRowValue) ((XHIBITTableModelInterface) table.getModel()).getDataAt(row);
            String pleaCode = rrv.getPleaValue().getRefPleaCode();
            DefaultComboBoxModel model = new DefaultComboBoxModel(verdictRestrictionHelper
                    .getRestrictedVerdictList(pleaCode));
            comboModelCache.put(rowObject, model);
            return model;
        }
    }
}