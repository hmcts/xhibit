package uk.gov.courtservice.xhibit.client.listings.list.firm;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.listings.preview.PreviewUnpublishedFirmListAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Panel for firm list dialog.
 * 
 * @author uphillj
 *
 */
public class FirmListPanel extends AbstractListPanel<FirmListModel,FirmListingDiaryPanel,FirmMainListingPanel, ListingDiaryModel> {

	private static final long serialVersionUID = 1L;

	public FirmListPanel(final XDialog parent, final FirmListModel firmListModel) throws CSRecoverableException {
		super(parent, firmListModel, firmListModel.getListingDiaryModel());
		
		// Stats button is not applicable for Firm List
		statsButton.setVisible(false);
	}

	@Override
	protected TreeNodeFactory createTreeNodeFactory() {
		return new FirmListTreeNodeFactory(parent, listModel, listOutline, listTreeModel);
	}
    
	@Override
	protected FirmMainListingPanel createMainListingPanel() {
		return new FirmMainListingPanel(parent, listModel, listOutline, listTreeModel);
	}

	@Override
	protected FirmListingDiaryPanel createListingDiaryPanel() {
		return new FirmListingDiaryPanel(listingDiaryModel);
	}
	
	/**
     * Open the preview dialog which allows the user to preview the saved list.
     * 
     * @throws CSRecoverableException
     */
	@Override
    protected void previewList() throws CSRecoverableException {
    	PreviewUnpublishedFirmListAction previewDailyListAction = new PreviewUnpublishedFirmListAction(listModel.getList().getListId());
		previewDailyListAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));         
    }
}