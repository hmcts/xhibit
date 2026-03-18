package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.util.XTextField;

public class CourtRoomNumberCellEditor extends DefaultCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7889836782659668813L;
	
	public CourtRoomNumberCellEditor(XTextField textField) {
		super(textField);
		
	}
	
	@Override
	public boolean stopCellEditing() {
		XTextField textField = (XTextField) getComponent();
		if (getCellEditorValue() != null) {
			try {
				if (!getCellEditorValue().equals("")) {
					Integer editingValue = Integer.parseInt((String) getCellEditorValue());
					if (editingValue < 1 || editingValue > 99) {
						textField.setBorder(BorderFactory.createLineBorder(Color.RED));
						textField.selectAll();
						textField.requestFocusInWindow();
						return false;
					}
				}
			} catch (NumberFormatException ex) {
				textField.setBorder(BorderFactory.createLineBorder(Color.RED));
				return false;
			}
		}
		return super.stopCellEditing();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component comp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		((JTextField) comp).setBorder(null);
		return comp;
	}
}
