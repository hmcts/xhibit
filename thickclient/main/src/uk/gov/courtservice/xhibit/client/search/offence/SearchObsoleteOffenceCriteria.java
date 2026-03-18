package uk.gov.courtservice.xhibit.client.search.offence;

import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteriaValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class SearchObsoleteOffenceCriteria extends SearchOffenceCriteria {
    public SearchObsoleteOffenceCriteria() {
        super();
    }

    public void setCriteria() {
        super.setCriteria();
        /** @todo Change back to "Y" */
        // addCriteria(new XHIBITSearchCriteriaValue("COURT_ID", "", null,
        // false,
       // XhibitSingleton.getInstance().getCourtId().toString()));
        // Ian Simmons,  defect 6612, USD reference - 797642 
        addCriteria(new XHIBITSearchCriteriaValue("OBS_IND", "", null, false, "N"));
        // end
       }
}