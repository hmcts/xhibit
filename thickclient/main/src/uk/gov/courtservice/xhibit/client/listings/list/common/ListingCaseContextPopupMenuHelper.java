package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailDialog;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Support Class to help with the creation and execution of
 * Case Context Menu Popup.
 * @author westalll
 *
 */
public class ListingCaseContextPopupMenuHelper {

	private final List<String> SUPPORTED_CONTEXT_MENU_CASE_TYPES = Arrays.asList("T", "S", "A");
	private JTable table;
	private JPopupMenu casePopupMenu;
	private XHIBITDefaultTableModel model;
	private ListModel listModel;

	public ListingCaseContextPopupMenuHelper(final ListModel listModel, final JTable table, final XHIBITDefaultTableModel model) {
		super();
		this.listModel = listModel;
		this.table = table;
		this.model = model;
		this.casePopupMenu = new JPopupMenu();
		this.table.addMouseListener(new ListingPopupMenuListener());
	}
	
	public void addCaseListingEntryPopupMenu() {
		addCaseListingEntryPopupMenu(null);
	}

	public void addCaseListingEntryPopupMenu(XAction postPopupAction) {
		casePopupMenu.add(new CaseEntryListAction(postPopupAction));
	}

	public void addCaseSummaryPopupMenu() {
		addCaseSummaryPopupMenu(null);
	}

	public void addCaseSummaryPopupMenu(XAction postPopupAction) {
		casePopupMenu.add(new CaseSummaryListAction(postPopupAction));
	}

	private class ListingPopupMenuListener extends MouseAdapter {

		private ListingPopupMenuListener() {
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
				if (isSupportedForPopUp(getListCaseTableRow())) {
					casePopupMenu.show(e.getComponent(), e.getX(), e.getY());
				} else {
					//Don't show menu.
				}
			}
		}

		private boolean isSupportedForPopUp(final ListCaseTableRow row) {
			if (row == null || row.getCaseNumber() == null) {
				return false;
			}
			for (final String supported : SUPPORTED_CONTEXT_MENU_CASE_TYPES) {
				if (row.getCaseNumber().startsWith(supported)) {
					return true;
				}
			}
			return false;

		}
	};

	private int getSelectedRowFromTableSorter(final JTable table) {

		return table.getRowSorter() == null || table.getSelectedRow() == -1 ? table.getSelectedRow()
				: table.convertRowIndexToModel(table.getSelectedRow());
	}

	private XhibitApplicationController getXac() {
		return listModel.getXac();
	}

	private int getSelectedRow() {
		return getSelectedRowFromTableSorter(table);
	}

	private ListCaseTableRow getListCaseTableRow() {
		return getSelectedRow() != -1 ? (ListCaseTableRow) model.getDataAt(getSelectedRow()) : null;
	}

	private class CaseSummaryListAction extends PopupAction {

		private static final long serialVersionUID = 1L;

		public CaseSummaryListAction(XAction postPopupAction) {
			super("ListingCaseSummary", postPopupAction);
		}

		public void firePopup(ActionEvent ae) throws Exception {
			if (getListCaseTableRow() != null) {
				final CaseSummaryModel summaryModel = new CaseSummaryModel(getListCaseTableRow().getCaseId());
				summaryModel.setFromListScreen(true);
				CaseSummaryDialog caseSummaryDialog = new CaseSummaryDialog(getXac(), summaryModel);
				caseSummaryDialog.setVisible(true);
			}
		}
	};

	private class CaseEntryListAction extends PopupAction {

		private static final long serialVersionUID = 1L;

		public CaseEntryListAction(XAction postPopupAction) {
			super("ListingCaseListingEntry", postPopupAction);
		}

		public void firePopup(ActionEvent ae) throws Exception {
			if (getListCaseTableRow() != null) {
				final CaseListingDetailModel model = new CaseListingDetailModel(getXac(),
						getListCaseTableRow().getCaseId(), XhibitSingleton.getInstance().getCourtId());
				final CaseListingDetailDialog dialog = new CaseListingDetailDialog(getXac(), model, true);
				dialog.setVisible(true);
			}
		}
	};

	abstract class PopupAction extends XAction {

		private static final long serialVersionUID = 1L;
		private XAction postPopupAction;

		public PopupAction(final String actionName, final XAction postPopupAction) {
			populateFromBundle(actionName);
			this.postPopupAction = postPopupAction;
		}
		
		protected abstract void firePopup(ActionEvent ae) throws Exception;

		protected void firePostPopupAction(ActionEvent ae) {
			if (postPopupAction != null) {
				postPopupAction.actionPerformed(new ActionEvent(getXac(), 0, "Call postPopupAction"));
			}
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			firePopup(ae);
			firePostPopupAction(ae);
		}
	}
}
