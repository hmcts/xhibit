package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

public class CourtCalendarTableCellEditor extends AbstractCellEditor implements TableCellEditor, CellEditorListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7487805377782074386L;
	private CourtCalendarTableCellPanel component;
	private CourtCalendarPanel myParent;
	
	public CourtCalendarTableCellEditor(CourtCalendarPanel myParent) {
		this.component = new CourtCalendarTableCellPanel(myParent);
		this.myParent = myParent;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		RefCalendarValue entry = (RefCalendarValue) value;
		component.updateData(entry, isSelected, table);
		
		return component;
	}	

	@Override
	public void editingStopped(ChangeEvent e) {
		Object source = e.getSource();
		CourtCalendarTableCellEditor editor = (CourtCalendarTableCellEditor) source;
		CourtCalendarTableCellPanel cellPanel = (CourtCalendarTableCellPanel) editor.getComponent();
		boolean isAvail = cellPanel.getYesButton().isSelected();
		cellPanel.updateModel(cellPanel.getReasonText().getText(), (isAvail == true ? "Y" : "N"));		
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
	}

	public CourtCalendarTableCellPanel getComponent() {
		return component;
	}

	/**
	 * @return the myParent
	 */
	public CourtCalendarPanel getMyParent() {
		return myParent;
	}

	/**
	 * @param myParent the myParent to set
	 */
	public void setMyParent(CourtCalendarPanel myParent) {
		this.myParent = myParent;
	}
}
