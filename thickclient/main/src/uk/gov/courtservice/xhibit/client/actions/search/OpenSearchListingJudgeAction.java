package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats.AddJudgeUsagePanel;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailPanel;
import uk.gov.courtservice.xhibit.client.listings.judgesearch.Search2JudgeForListingCriteria;
import uk.gov.courtservice.xhibit.client.listings.judgesearch.Search2JudgeForListingDetails;
import uk.gov.courtservice.xhibit.client.listings.judgesearch.Search2JudgeForListingResults;
import uk.gov.courtservice.xhibit.client.listings.list.sitting.SittingPanel;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */
public class OpenSearchListingJudgeAction extends AbstractSearchAction {

	private static final long serialVersionUID = 1L;	
	
	public OpenSearchListingJudgeAction() {        
	populateFromBundle(XhibitActions.OpenSearchListingJudge);
    }

    public void xActionPerformed(ActionEvent e) {
        addSearchSpecification(new Search2JudgeForListingCriteria());
        addSearchSpecification(new Search2JudgeForListingResults());
        addSearchSpecification(new Search2JudgeForListingDetails());

        new XHIBITSearch(this);

        if (!this.isSearchCancelled()) {
            if (this.getCaller() != null) {
                log.debug("OpenSearchListingJudgeAction: caller object present");

                if (this.getCaller() instanceof CaseListingDetailPanel) {
                    log.debug("OpenSearchListingJudgeAction: caller is CaseListingDetailPanel");
                    ((CaseListingDetailPanel) this.getCaller()).processAddJudge(this);
                } else if (this.getCaller() instanceof SittingPanel) {
                    log.debug("OpenSearchListingJudgeAction: caller is SittingPanel");    
                    ((SittingPanel) this.getCaller()).processAddJudge(this);
                } else if (this.getCaller() instanceof AddJudgeUsagePanel) {
                    log.debug("OpenSearchListingJudgeAction: caller is Record Courtroom Statistics");    
                    ((AddJudgeUsagePanel) this.getCaller()).processAddJudge(this);
                } else {
                	log.debug("OpenSearchListingJudgeAction: unknown caller");
                }
            }
        } else {
            log.debug("OpenSearchListingJudgeAction was cancelled.");
        }
    }
}
