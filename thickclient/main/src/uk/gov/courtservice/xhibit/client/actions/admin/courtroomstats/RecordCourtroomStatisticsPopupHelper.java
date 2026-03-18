package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Class that enables the user to right click and delete an item
 * from Record Courtroom Statistics.
 * 
 * @author westalll
 *
 */
public class RecordCourtroomStatisticsPopupHelper {
	
	private XDialog parent;

	public RecordCourtroomStatisticsPopupHelper(final XDialog parent) {
		super();
		this.parent = parent;
	}

	/**
	 * Method to add the Context Menu Popup to a table.
	 * 
	 * @param table
	 *            The table you wish to add the popup to.
	 * @param model
	 *            The model for the table.
	 */
	public void addEntryAndSummaryPopupMenu(final JTable table, final XHIBITDefaultTableModel model) {
		final JPopupMenu casePopupMenu = new JPopupMenu();
		casePopupMenu.add(new DeleteEntryAction(table, model));
		table.addMouseListener(new PopupMenuListener(table, model, casePopupMenu));
	}

	private class PopupMenuListener extends MouseAdapter {

		private JTable table;
		private XHIBITDefaultTableModel model;
		private JPopupMenu popupMenu;

		private PopupMenuListener(final JTable table, XHIBITDefaultTableModel model, final JPopupMenu casePopupMenu) {
			this.table = table;
			this.model = model;
			this.popupMenu = casePopupMenu;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			showPopupMenu(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			showPopupMenu(e);
		}

		private void showPopupMenu(MouseEvent e) {
			if (e.isPopupTrigger()) {
				if (isSupportedForPopUp(getListCaseTableRow(table, model))) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				} else {
					//Don't show menu.
				}
			}
		}

		private boolean isSupportedForPopUp(final Object object) {
			if (object == null) {
				return false;
			}	
			return true;
		}
	};

	protected Object getListCaseTableRow(final JTable table, final XHIBITDefaultTableModel tableModel) {
		final int selectedRow = getSelectedRowFromTableSorter(table);
		return selectedRow >= 0 ? tableModel.getDataAt(selectedRow) : null;
	}

	private int getSelectedRowFromTableSorter(final JTable table) {

		return table.getRowSorter() == null ? table.getSelectedRow()
				: table.convertRowIndexToModel(table.getSelectedRow());
	}

	private class DeleteEntryAction extends XAction {

		private static final long serialVersionUID = 1L;
		private XHIBITDefaultTableModel model;
		private JTable table;
		
		private static final String DELETE_JUDGE_USAGE = "RCSDeleteJudgeUsage";
		private static final String DELETE_COURTROOM_USAGE = "RCSDeleteCourtroomUsage";

		public DeleteEntryAction(final JTable table, final XHIBITDefaultTableModel model) {
			this.table = table;
			this.model = model;
			if (model instanceof JudgeUsageTableModel) {
				populateFromBundle(DELETE_JUDGE_USAGE);
			} else if (model instanceof CourtRoomUsageTableModel) {
				populateFromBundle(DELETE_COURTROOM_USAGE);
			} 
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			final int selectedRow = getSelectedRowFromTableSorter(table);
			final Object row = model.getDataAt(selectedRow);
			
			if (row instanceof JudgeUsageRow && showDeleteConfirmationMsg()) {
				JudgeUsageRow judgeUsageRow = (JudgeUsageRow) row;
				XhibitDelegateHelper.getBizRefDelegate().deleteJudgeUsage(judgeUsageRow.getJudgeUsageId());
				
				((JudgeUsageTableModel) model).removeCase(judgeUsageRow);
			}
			else if(row instanceof CourtRoomUsageRow && showDeleteConfirmationMsg()) {
				CourtRoomUsageRow courtRoomUsageRow = (CourtRoomUsageRow) row;
				XhibitDelegateHelper.getBizRefDelegate().deleteCourtRoomUsage(courtRoomUsageRow.getCourtRoomUsageId());
				
				((CourtRoomUsageTableModel) model).removeCase(courtRoomUsageRow);
			}
		}

		
		private boolean showDeleteConfirmationMsg() throws CSRecoverableException {
			boolean messageBoxReply = XMessageBox.alert(parent,
					XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
							"RecordCourtroomStatisticsDeleteConfirmationTitle"),
					true, XMessageBox.ICONQUESTION,
					XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
							"RecordCourtroomStatisticsDeleteConfirmationMessage"),
					XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);

			
			return messageBoxReply;
		}
	};
}

