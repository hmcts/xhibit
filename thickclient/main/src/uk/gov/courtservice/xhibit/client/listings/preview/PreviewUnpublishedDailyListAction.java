package uk.gov.courtservice.xhibit.client.listings.preview;

import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class PreviewUnpublishedDailyListAction extends AbstractPreviewList {

	private static final long serialVersionUID = 1L;
	private static final String DAILY_LIST_STYLESHEET = "config/xml/documentdistribution/dailyListFO.xsl";
	private static final String DAILY_PRISON_LIST_STYLESHEET = "config/xml/documentdistribution/dailyPrisonListFO.xsl";
	private static final String DAILY_COURTROOM_LIST_STYLESHEET = "config/xml/documentdistribution/dailyCourtRoomListFO.xsl";
	private Integer listId;
	private Boolean showCourtRoomList;
	private Boolean showPrisonList;
	
	/**
     * Construct a new PreviewDailyList action populating from the resource
     * bundle
     */
    public PreviewUnpublishedDailyListAction(Integer listId, Boolean showCourtRoomList, Boolean showPrisonList) {
        super(showCourtRoomList ? "PreviewCourtRoomList" :
				showPrisonList ? "PreviewPrisonList" :
								 "PreviewDailyList");
        this.listId = listId;
        this.showCourtRoomList = showCourtRoomList;
        this.showPrisonList = showPrisonList;
    }
	
	@Override
	public String getXml() throws Exception {
		if (this.showPrisonList) {
			return XhibitDelegateHelper.getListingsDelegate().getDailyPrisonList(this.listId);
		} else {
			return XhibitDelegateHelper.getViewScheduleDelegate().getUnpublishedDailyList(this.listId, this.showCourtRoomList);
		}
	}

	@Override
	protected String getTransformationStylesheetURL() {
		String transformationStylesheetURL = DAILY_LIST_STYLESHEET;
		if(showCourtRoomList){
			transformationStylesheetURL = DAILY_COURTROOM_LIST_STYLESHEET;
		}
		else if(showPrisonList){
			transformationStylesheetURL = DAILY_PRISON_LIST_STYLESHEET;
		}
		return transformationStylesheetURL;
	}
}
