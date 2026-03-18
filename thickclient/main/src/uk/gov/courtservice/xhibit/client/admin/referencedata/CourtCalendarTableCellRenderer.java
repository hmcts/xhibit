package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CourtCalendarTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3983170383921454090L;
	private CourtCalendarTableCellPanel cellPanel;
	private CourtCalendarPanel myParent;

	public CourtCalendarTableCellRenderer(CourtCalendarPanel myParent) {
		this.cellPanel = new CourtCalendarTableCellPanel(myParent);
		this.myParent = myParent;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		RefCalendarValue refCalendar = null;

		if (value != null && value instanceof RefCalendarValue) {
			refCalendar = (RefCalendarValue) value;
		}

		cellPanel.updateData(refCalendar, isSelected, table);
		return cellPanel;
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
