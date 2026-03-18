package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.legalrep.SearchLegalRepCriteria;
import uk.gov.courtservice.xhibit.client.search.legalrep.SearchLegalRepDetails;
import uk.gov.courtservice.xhibit.client.search.legalrep.SearchLegalRepResults;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateDefendantCaseData;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateProsecutionCaseData;

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

public class OpenSearchLegalRepAction extends AbstractSearchAction {

    public OpenSearchLegalRepAction() {
        super();
        /** @todo: populate the actions resource bundle */
        populateFromBundle(XhibitActions.OpenSearchLegalRep);

    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {

        addSearchSpecification(new SearchLegalRepCriteria());
        addSearchSpecification(new SearchLegalRepResults());
        addSearchSpecification(new SearchLegalRepDetails());

        XHIBITSearch xSearch = new XHIBITSearch(this);

        if (!this.isSearchCancelled()) {
            if (this.getCaller() != null) {
                log.debug("OpenSearchLegalRepAction: caller object present");
                Class callerClass = this.getCaller().getClass();
                /** @todo: link in with the calling panel(s?) */
                if (callerClass.isAssignableFrom(UpdateDefendantCaseData.class)) {
                    log.debug("OpenSearchJudgeAction: caller is UpdateGeneralCaseData");
                    // ((UpdateDefendantCaseData)this.getCaller()).processAddDefenceAdvocate(this);
                } else {
                    if (callerClass.isAssignableFrom(UpdateProsecutionCaseData.class)) {
                        log.debug("OpenSearchJudgeAction: caller is UpdateProsecutionCaseData");
                        // ((UpdateProsecutionCaseData)this.getCaller()).processAddProsecutionAdvocate(this);
                    }
                }
            }
        } else {
            log.debug("OpenSearchJudgeAction was cancelled.");
        }
    }
}