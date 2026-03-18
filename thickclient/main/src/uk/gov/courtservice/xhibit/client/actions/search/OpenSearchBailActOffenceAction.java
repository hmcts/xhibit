package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.offence.SearchBailActOffenceCriteria;
import uk.gov.courtservice.xhibit.client.search.offence.SearchOffenceDetails;
import uk.gov.courtservice.xhibit.client.search.offence.SearchOffenceResults;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: XHIBIT 2 OpenSearchBailActOffenceAction
 * </p>
 * <p>
 * Description: This action instatiates the search for Bail Act offences specifications
 * and calls processOffence(this) on the caller after the search component was
 * closed.
 * </p>

 * <p>
 * Company: Logica
 * </p>
 *
 * @author luis Valenzuela
 * @version 1.0
 */

public class OpenSearchBailActOffenceAction extends AbstractSearchAction {

    private static final long serialVersionUID = 1L;

    public OpenSearchBailActOffenceAction() {
        super();
        this.populateFromBundle("XhibitActions.OpenSearchOffenceAction");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        XhibitApplicationController xac;

        xac = (XhibitApplicationController) getController();

        clearSearchSpecification();
        addSearchSpecification(new SearchBailActOffenceCriteria());
        addSearchSpecification(new SearchOffenceResults());
        addSearchSpecification(new SearchOffenceDetails());

        /* Perform search wihout user input to find all Bail Act offences */
        @SuppressWarnings("unused")
        XHIBITSearch xs = new XHIBITSearch(this, xac, true);

        //user did not select cancel during XHibit Search
        if (!this.isSearchCancelled()){
            if (this.getCaller() != null) {
                log.debug("OpenSearchOffenceAction: caller object present");
                if (this.getCaller() instanceof SearchProcessHandler) {
                    //Catches all instances where cancel is pressed and redirects user back to the opening Xhibit search dialog box
                    try{
                        ((SearchProcessHandler) this.getCaller()).processResults(this);
                    }  catch (UserCancelException ue){
                        log.error(e);
                        XHIBITConstant.handleError(ue);
                        clearSearchSpecification();
                        throw new CSRecoverableException();
                    }
                }
            }
        }
    }
}