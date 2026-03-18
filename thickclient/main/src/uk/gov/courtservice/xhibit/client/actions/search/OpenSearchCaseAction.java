package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.courtlog.LongAdjournmentPanel;
import uk.gov.courtservice.xhibit.client.maintaincharges.BreachPanel;
import uk.gov.courtservice.xhibit.client.results.appealresults.MiscellaneousAppealPanel;
import uk.gov.courtservice.xhibit.client.schedule.addhearing.AddHearingSelectDefendants;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.caseSearch.SearchCaseCriteria;
import uk.gov.courtservice.xhibit.client.search.caseSearch.SearchCaseDetails;
import uk.gov.courtservice.xhibit.client.search.caseSearch.SearchCaseResults;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtCriteria;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtDetails;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtResults;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateDefendantPanel;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateGeneralCaseData;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */

public class OpenSearchCaseAction extends AbstractSearchAction {
    public OpenSearchCaseAction() {
        super();
        populateFromBundle(XhibitActions.OpenSearchCase);
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        addSearchSpecification(new SearchCaseCriteria());
        addSearchSpecification(new SearchCaseResults());
        addSearchSpecification(new SearchCaseDetails());

        XHIBITSearch xSearch = new XHIBITSearch(this);

        if (!this.isSearchCancelled()) {
            if (this.getCaller() != null) {
                log.debug("OpenSearchCaseAction: caller object present");

                /* TODO
                if (this.getCaller() != null) {
                    ((SearchCaseDialog) this.getCaller()).processCaseSearch(this);
                }*/
            }

        } else {
            log.debug("OpenSearchCaseAction was cancelled.");
        }
    }
}