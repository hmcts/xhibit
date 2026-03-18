package uk.gov.courtservice.xhibit.client.listings.list.daily;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Panel for daily list dialog.
 * 
 * @author uphillj
 *
 */
public class DailyListPanel extends AbstractListPanel<DailyListModel,DailyListingDiaryPanel,DailyMainListingPanel, ListingDiaryModel> {

	private static final long serialVersionUID = 1L;

	public DailyListPanel(final XDialog parent, final DailyListModel dailyListModel) throws CSRecoverableException {
		super(parent, dailyListModel, dailyListModel.getListingDiaryModel());
		
		// Stats button is not applicable for Daily List
		statsButton.setVisible(false);
	}
	
	@Override
	protected TreeNodeFactory createTreeNodeFactory() {
		return new DailyListTreeNodeFactory(parent, listModel, listOutline, listTreeModel);
	}
    
	@Override
	protected DailyMainListingPanel createMainListingPanel() {
		return new DailyMainListingPanel(parent, listModel, listOutline, listTreeModel);
	}

	@Override
	protected DailyListingDiaryPanel createListingDiaryPanel() {
		return new DailyListingDiaryPanel(listingDiaryModel);
	}	
}