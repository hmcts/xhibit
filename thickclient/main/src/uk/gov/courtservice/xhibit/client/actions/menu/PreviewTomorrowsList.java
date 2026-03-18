package uk.gov.courtservice.xhibit.client.actions.menu;

import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: PreviewTomorrowsList
 * </p>
 * <p>
 * Description: Action for preiviewing the tomorrows list
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment (2003)
 * @version 1.0
 */
public class PreviewTomorrowsList extends AbstractPreviewList {

    /**
     * Construct a new PreviewTomorrowsList action populating from the resource
     * bundle
     */
    public PreviewTomorrowsList() {
        super("PreviewTomorrowsList");
    }

    /**
     * Return the tomorrows list xml
     * 
     * @return String the xml
     * @throws Exception
     */
    public String getXml() throws Exception {
        return XhibitDelegateHelper.getViewScheduleDelegate().getTomorrowsList(getCurrentCourtId(),
                getTodaysTimeStamp());
    }
    
    @Override
	protected String getTransformationStylesheetURL() {
	    	return "config/xsl/viewschedule/xhbDailyListFO.xsl";
	}
}
