package uk.gov.courtservice.xhibit.client.listings.list.common;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * Court Listing outline class which adds support for only displaying checkboxes
 * for cells that actually contain a boolean value, i.e. only on case rows.
 * 
 * @author uphillj
 *
 */
public class CourtListingOutline extends Outline {

	private static final long serialVersionUID = 1L;
	private AbstractMainListingPanel parentPanel;

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if (isCellValueBoolean(row, column)) {
			return super.getDefaultRenderer(Boolean.class);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		if (isCellValueBoolean(row, column)) {
			return super.getDefaultEditor(Boolean.class);
		} else {
			return super.getCellEditor(row, column);
		}
	}

	/**
	 * Returns true if the cell value for the column is Boolean.
	 * 
	 * @param row
	 * @param column
	 * @return true if value is Boolean
	 */
	protected boolean isCellValueBoolean(int row, int column) {
		Object cellValue = super.getValueAt(row, column);
		return (cellValue instanceof Boolean);
	}

	public XAction getRefreshAction() {
		return getParentPanel().getRefreshAction();
	}

	public XAction getRefreshFixturesAction() {
		return getParentPanel().getRefreshFixturesAction();
	}

	public XAction getSaveAction() {
		return getParentPanel().getSaveAction();
	}

	public AbstractMainListingPanel getParentPanel() {
		return parentPanel;
	}

	public void setParentPanel(AbstractMainListingPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
}
