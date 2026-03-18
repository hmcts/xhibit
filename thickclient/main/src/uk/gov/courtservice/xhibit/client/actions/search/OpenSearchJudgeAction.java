package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search.JudgeSearch;
import uk.gov.courtservice.xhibit.client.courtlog.LongAdjournmentPanel;
import uk.gov.courtservice.xhibit.client.schedule.addhearing.AddHearingSelectDefendants;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.judge.Search2JudgeCriteria;
import uk.gov.courtservice.xhibit.client.search.judge.Search2JudgeDetails;
import uk.gov.courtservice.xhibit.client.search.judge.SearchJudgeResults;
import uk.gov.courtservice.xhibit.client.search.judge.SearchJudgeUpdate;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateGeneralCaseData;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author Frederik Vandendriessched
 * @version 1.0
 */
public class OpenSearchJudgeAction extends AbstractSearchAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4285255453710468082L;

	public OpenSearchJudgeAction() {
		super();
		populateFromBundle(XhibitActions.OpenSearchJudge);
	}

	public void xActionPerformed(ActionEvent e) {
		XhibitApplicationController xac = (XhibitApplicationController) getController();
        
		addSearchSpecification(new Search2JudgeCriteria());
		addSearchSpecification(new SearchJudgeResults());
		if (this.getCaller() != null) {
			log.debug("OpenSearchJudgeAction: caller object present");

			if (this.getCaller() instanceof UpdateGeneralCaseData ) {
				log.debug("OpenSearchJudgeAction: caller is UpdateGeneralCaseData");
				addSearchSpecification(new Search2JudgeDetails());
				new XHIBITSearch(this);
				if(!this.isSearchCancelled()){
					((UpdateGeneralCaseData) this.getCaller()).processAddJudge(this);
				} else {
					log.debug("OpenSearchJudgeAction was cancelled.");
				}
			} else if (this.getCaller() instanceof AddHearingSelectDefendants) {
				log.debug("OpenSearchJudgeAction: caller is AddHearingSelectDefendants");
				addSearchSpecification(new Search2JudgeDetails());
				new XHIBITSearch(this);
				if(!this.isSearchCancelled()) {
					((AddHearingSelectDefendants) this.getCaller()).processAddJudge(this);
				} else {
					log.debug("OpenSearchJudgeAction was cancelled.");
				}
			} else if (this.getCaller() instanceof LongAdjournmentPanel) {
				log.debug("OpenSearchJudgeAction: caller is LongAdjournmentPanel");
				addSearchSpecification(new Search2JudgeDetails());
				new XHIBITSearch(this);
				if(!this.isSearchCancelled()) {
					((LongAdjournmentPanel) this.getCaller()).processAddJudge(this);
				} else {
					log.debug("OpenSearchJudgeAction was cancelled.");
				}
			} else {
				log.debug("OpenSearchJudgeDetailsAction: caller is JudgeDetailsAction");
				addSearchSpecification(new SearchJudgeUpdate());
				new JudgeSearch(this, xac);
			}
		} else {
			if (this.isSearchCancelled()) {
				log.debug("OpenSearchJudgeAction was cancelled.");
			}
		}
	}
}
