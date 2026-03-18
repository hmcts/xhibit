package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.maintaincharges.BreachPanel;
import uk.gov.courtservice.xhibit.client.results.appealresults.MiscellaneousAppealPanel;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtCriteria;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtDetails;
import uk.gov.courtservice.xhibit.client.search.court.SearchCourtResults;

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
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class OpenSearchCourtAction extends AbstractSearchAction {
    public OpenSearchCourtAction() {
        super();
        populateFromBundle(XhibitActions.OpenSearchCourt);
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        Class callerClass = null;
        if (this.getCaller() != null) {
            callerClass = this.getCaller().getClass();
            if (callerClass.isAssignableFrom(MiscellaneousAppealPanel.class)) {
                addSearchSpecification(new SearchCourtCriteria(SearchCourtCriteria.CROWN_ONLY));
            } else {
                addSearchSpecification(new SearchCourtCriteria());
            }
        }
        addSearchSpecification(new SearchCourtResults());
        addSearchSpecification(new SearchCourtDetails());

        XHIBITSearch xSearch = new XHIBITSearch(this);

        if (!this.isSearchCancelled()) {
            if (callerClass != null) {
                log.debug("OpenSearchCourtAction: caller object present");
                if (callerClass.isAssignableFrom(BreachPanel.class)) {
                    log
                            .debug("OpenSearchCourtAction: caller is BreachPanel. Performing a BreachPanel.processCourtSearch(this)");
                    ((BreachPanel) this.getCaller()).processCourtSearch(this);
                } else if (callerClass.isAssignableFrom(MiscellaneousAppealPanel.class)) {
                    log
                            .debug("OpenSearchCourtAction: caller is MiscellaneousAppealPanel. Performing a MiscellaneousAppealPanel.processCourtSearch(this)");
                    ((MiscellaneousAppealPanel) this.getCaller()).processCourtSearch(this);
                }
            }
        } else {
            log.debug("OpenSearchCourtAction was cancelled.");
        }
    }
}