package uk.gov.courtservice.xhibit.client.listings.preview;

import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class PreviewUnpublishedAnnotatedWarnedListAction extends AbstractPreviewList {

	private static final long serialVersionUID = 1L;
	private Integer listId;
	private boolean includeStandardNotes;
	private boolean includePriorityNotes;
	private boolean includeRestrictedNotes;
	
	public PreviewUnpublishedAnnotatedWarnedListAction(Integer listID, boolean includeStandardNotes,
			boolean includePriorityNotes, boolean includeRestrictedNotes) {
		super("PreviewAnnotatedWarnedList");
		this.listId = listID;
        this.includeStandardNotes = includeStandardNotes;
        this.includePriorityNotes = includePriorityNotes;
        this.includeRestrictedNotes = includeRestrictedNotes;
	}

	@Override
	protected String getTransformationStylesheetURL() {
	    return "config/xml/documentdistribution/annotatedwarnedListFO.xsl";
	}

	@Override
	public String getXml() throws Exception {
		return XhibitDelegateHelper.getViewScheduleDelegate().getUnpublishedWarnedList(this.listId, this.includeStandardNotes, this.includePriorityNotes, includeRestrictedNotes, true);
	}

}
