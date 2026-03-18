package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.offence.SearchObsoleteOffenceCriteria;
import uk.gov.courtservice.xhibit.client.search.offence.SearchOffenceDetails;
import uk.gov.courtservice.xhibit.client.search.offence.SearchOffenceResults;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 OpenSearchObseleteOffenceAction
 * </p>
 * <p>
 * Description: This action instatiates the search for offence specifications
 * and calls processOffence(this) on the caller after the search component was
 * closed.
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

public class OpenSearchObsoleteOffenceAction extends AbstractSearchAction {
    public OpenSearchObsoleteOffenceAction() {
        super();
        this.populateFromBundle("XhibitActions.OpenSearchObsoleteOffenceAction");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        XhibitApplicationController xac;

        xac = (XhibitApplicationController) getController();

        addSearchSpecification(new SearchObsoleteOffenceCriteria());
        addSearchSpecification(new SearchOffenceResults());
        addSearchSpecification(new SearchOffenceDetails());

        XHIBITSearch xs = new XHIBITSearch(this, xac);

        if (this.getCaller() != null) {
            log.debug("OpenSearchObsoleteOffenceAction: caller object present");
            Class callerClass = this.getCaller().getClass();
            if (this.getCaller() instanceof SearchProcessHandler) {
                ((SearchProcessHandler) this.getCaller()).processResults(this);
            }
        }
    }
}
