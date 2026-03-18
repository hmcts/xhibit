package uk.gov.courtservice.xhibit.client.listings.preview;

import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class PreviewUnpublishedFirmListAction extends AbstractPreviewList {

	private static final long serialVersionUID = 1L;
	private Integer listId;

	public PreviewUnpublishedFirmListAction(Integer listId) {
		 super("PreviewFirmList");
	        this.listId = listId;
	}
	
	@Override
	protected String getTransformationStylesheetURL(){
		return "config/xml/documentdistribution/firmListFO.xsl";
	}

	@Override
	public String getXml() throws Exception {
		return XhibitDelegateHelper.getViewScheduleDelegate().getUnpublishedFirmList(listId);
	}

}
