package uk.gov.courtservice.xhibit.client.listings.preview;

import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class PreviewUnpublishedWarnedListAction extends AbstractPreviewList {

	private static final long serialVersionUID = 1L;
	private Integer listId;

	public PreviewUnpublishedWarnedListAction(Integer listID) {
		super("PreviewWarnedList");
		this.listId = listID;
    }

	@Override
	protected String getTransformationStylesheetURL() {
	    return "config/xml/documentdistribution/warnedListFO.xsl";
	}

	@Override
	public String getXml() throws Exception {
		return XhibitDelegateHelper.getViewScheduleDelegate().getUnpublishedWarnedList(this.listId, true, true, true, false);
	}

}
