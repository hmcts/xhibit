package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CourtRoomNumberRenderer implements TableCellRenderer {

	CourtRoomTableModel model;
	TableCellRenderer renderer;
	
	public CourtRoomNumberRenderer(TableCellRenderer renderer, CourtRoomTableModel model) {
		this.renderer = renderer;
		this.model = model;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel component = (JLabel) renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (column == 0) {
			if (value != null) {
				Integer roomNo = (Integer) value;
				if (roomNo > 99 || roomNo == 0 || model.isRoomNumberExist(roomNo, table.convertRowIndexToModel(row))) {
					component.setBorder(BorderFactory.createLineBorder(Color.RED));
				} else {
					component.setBorder(null);
				}
			} else {
				component.setBorder(BorderFactory.createLineBorder(Color.RED));
			}
		}
		return component;
	}
}
