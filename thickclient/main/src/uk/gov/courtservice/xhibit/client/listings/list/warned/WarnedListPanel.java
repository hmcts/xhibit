package uk.gov.courtservice.xhibit.client.listings.list.warned;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Panel for firm list dialog.
 * 
 * @author uphillj
 *
 */
public class WarnedListPanel extends AbstractListPanel<WarnedListModel,WarnedListingDiaryPanel,WarnedMainListingPanel, WarnedListingDiaryModel> {

	private static final long serialVersionUID = 1L;

	public WarnedListPanel(final XDialog parent, final WarnedListModel warnedListModel) throws CSRecoverableException {
		super(parent, warnedListModel, (WarnedListingDiaryModel) warnedListModel.getListingDiaryModel());
	}
	
	@Override
	protected TreeNodeFactory createTreeNodeFactory() {
		return new WarnedListTreeNodeFactory(parent, listModel, listOutline, listTreeModel);
	}
    
	@Override
	protected WarnedMainListingPanel createMainListingPanel() {
		return new WarnedMainListingPanel(parent, listModel, listOutline, listTreeModel);
	}

	@Override
	protected WarnedListingDiaryPanel createListingDiaryPanel() {
		return new WarnedListingDiaryPanel(listingDiaryModel);
	}
}