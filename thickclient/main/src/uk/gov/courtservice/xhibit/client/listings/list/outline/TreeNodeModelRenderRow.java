package uk.gov.courtservice.xhibit.client.listings.list.outline;

import org.apache.commons.lang.WordUtils;
import org.netbeans.swing.outline.RowModel;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingUtils;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Class for displaying data in the row of the table/tree component
 * 
 * @author uphillj
 *
 */
public class TreeNodeModelRenderRow implements RowModel {

	protected static final String YES = "Y";
	protected static final String NO = "N";
	protected static final int NUMBER_COLUMNS = 6;
	protected static final int CASE_TITLE_COLUMN = 0;
	protected static final int CASE_GROUP_COLUMN = 1;
	protected static final int TIME_ESTIMATE_COLUMN = 2;
	protected static final int HEARING_TYPE_COLUMN = 3;
	protected static final int TIME_MARKING_COLUMN = 4;
	protected static final int COURT_ROOM_LIST_COLUMN = 5;
	
	@Override
	public Class getColumnClass(int column) {
		// String is default column type and the CourtListingOutline overrides the renderer
		// and cell editor on a cell by cell basis by checking the type of the cell value
		// which is required to be able to only display a checkbox on certain rows
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return NUMBER_COLUMNS;
	}

	@Override
	public String getColumnName(int column) {
		String columnName = null;
		switch (column) {
			case CASE_TITLE_COLUMN:
				columnName = XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseTitle");
	            break;
			case CASE_GROUP_COLUMN:
				columnName = XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseGroup");
	            break;
			case TIME_ESTIMATE_COLUMN:
				columnName = XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableTimeEstimate");
	            break;
			case HEARING_TYPE_COLUMN:
				columnName = XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableHearingType");
	            break;
			case TIME_MARKING_COLUMN:
				columnName = XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableTimeMarking");
	            break;
			case COURT_ROOM_LIST_COLUMN:
				columnName = XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCourtRoomList");
	            break;
		}
		return columnName;
	}

	@Override
	public Object getValueFor(Object node, int column) {
		Object cellValue = null;
		
		// Case values are displayed in the columns after the tree nodes
		if (isCaseRow(node)) {
			CaseTreeNodeController controller = (CaseTreeNodeController)OutlineUtils.getTreeNodeController(node);
			CaseOnListComplexValue caseOnList = controller.getModel().getCaseOnList();
			switch (column) {
				case CASE_TITLE_COLUMN:
					cellValue = caseOnList.getCase().getCaseTitle();
		            break;
				case CASE_GROUP_COLUMN:
					cellValue = getGroupNumberText(caseOnList);
		            break;
				case TIME_ESTIMATE_COLUMN:
					cellValue = ListingUtils.getTimeEstimateShortText(caseOnList.getDirectionsForCase());
		            break;
				case HEARING_TYPE_COLUMN:
					cellValue = getHearingTypeText(caseOnList);
		            break;
				case TIME_MARKING_COLUMN:
					cellValue = getTimeMarkingText(caseOnList);
		            break;
				case COURT_ROOM_LIST_COLUMN:
					cellValue = YES.equals(caseOnList.getIsCourtRoomListEntry());
		            break;
			}
		}
		// Sitting time marking is displayed in the column after the tree nodes
		else if (isSittingRow(node)) {
			SittingTreeNodeController controller = (SittingTreeNodeController)OutlineUtils.getTreeNodeController(node);
			SittingOnListComplexValue sittingOnList = controller.getModel().getSittingOnList();
			switch (column) {
				case TIME_MARKING_COLUMN:
					cellValue = getTimeMarkingText(sittingOnList);
		            break;
			}
		}
		
		return cellValue;
	}

	@Override
	public boolean isCellEditable(Object node, int column) {
		boolean editable = false;

		// Only case values can be modified
		if (isCaseRow(node)) {
			switch (column) {
				case COURT_ROOM_LIST_COLUMN:
					editable = true;
		            break;
			}
		}
		
		return editable;
	}

	@Override
	public void setValueFor(Object node, int column, Object value) {
		// Only case values can be modified
		if (isCaseRow(node)) {
			CaseTreeNodeController controller = (CaseTreeNodeController)OutlineUtils.getTreeNodeController(node);
			CaseOnListComplexValue caseOnList = controller.getModel().getCaseOnList();
			switch (column) {
				case COURT_ROOM_LIST_COLUMN:
					caseOnList.setIsCourtRoomListEntry(Boolean.TRUE.equals(value) ? YES : NO);
					caseOnList.setDirty(true);
					
					// Update the court room list value for the defendants attending
					for (DefOnCaseOnListBasicValue doc : caseOnList.getDefOnCaseOnLists()) {
						if ( null == doc.getObsInd() || NO.equals(doc.getObsInd()) )
						doc.setIsCourtRoomListEntry(Boolean.TRUE.equals(value) ? YES : NO);
						doc.setDirty(true);
					}
					
					controller.save("CourtRoomListCheckbox");
		            break;
			}
		}
	}

	/**
	 * Rows in the control that have a sitting display data in the columns.
	 * 
	 * @param node
	 * @return
	 */
	protected boolean isSittingRow(Object node) {
		TreeNodeController controller = OutlineUtils.getTreeNodeController(node);
		return (controller != null && controller.getModel() instanceof SittingTreeNodeModel);
	}

	/**
	 * Rows in the control that have a case display data in the columns.
	 * 
	 * @param node
	 * @return
	 */
	protected boolean isCaseRow(Object node) {
		TreeNodeController controller = OutlineUtils.getTreeNodeController(node);
		return (controller != null && controller.getModel() instanceof CaseTreeNodeModel);
	}
	
	/**
	 * Return the text for the group number which may not be set.
	 * 
	 * @param caseOnList
	 * @return
	 */
	protected String getGroupNumberText(CaseOnListComplexValue caseOnList) {
		String text = "";
		if (caseOnList.getCase().getCaseGroupNumber() != null) {
			text = caseOnList.getCase().getCaseGroupNumber().toString();
		}
		return text;
	}
	
	/**
	 * Return the text for the time marking which may not be set.
	 * 
	 * @param sittingOnList
	 * @return
	 */
	protected String getTimeMarkingText(SittingOnListComplexValue sittingOnList) {
		String text = "";
		if (sittingOnList.getTimeMarking() != null) {
			text = getTimeMarkingText(sittingOnList.getTimeMarking().getDecode(), sittingOnList.getTimeListedHour(), sittingOnList.getTimeListedMinute());
		}
		return text;
	}
	
	/**
	 * Return the text for the time marking which may not be set.
	 * 
	 * @param caseOnList
	 * @return
	 */
	protected String getTimeMarkingText(CaseOnListComplexValue caseOnList) {
		String text = "";
		if (caseOnList.getTimeMarking() != null) {
			text = getTimeMarkingText(caseOnList.getTimeMarking().getDecode(), caseOnList.getTimeListedHour(), caseOnList.getTimeListedMinute());
		}
		return text;
	}
	
	/**
	 * Return the text for the time marking and hour and minute.
	 * 
	 * @param timeMarking
	 * @param hour
	 * @param minute
	 * @return
	 */
	protected String getTimeMarkingText(String timeMarking, int hour, int minute) {
		StringBuilder builder = new StringBuilder(WordUtils.capitalizeFully(timeMarking));
		builder.append(" ").append(String.format("%02d", hour));
		builder.append(":").append(String.format("%02d", minute));
		return builder.toString();
	}
	
	/**
	 * Return the text for the hearing type which should not be null but this
	 * method prevents display errors if it ever does happen.
	 * 
	 * @param caseOnList
	 * @return
	 */
	protected String getHearingTypeText(CaseOnListComplexValue caseOnList) {
		String text = "";
		if (caseOnList.getHearingType() != null) {
			text = caseOnList.getHearingType().getHearingTypeCode();
		}
		return text;
	}
}