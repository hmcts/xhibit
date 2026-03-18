package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.GeneralTrial;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.offence.SearchOffenceCriteria;
import uk.gov.courtservice.xhibit.client.search.offence.SearchOffenceDetails;
import uk.gov.courtservice.xhibit.client.search.offence.SearchOffenceResults;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 OpenSearchOffenceAction
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
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class OpenSearchOffenceAction extends AbstractSearchAction {

    public OpenSearchOffenceAction() {
        super();
        this.populateFromBundle("XhibitActions.OpenSearchOffenceAction");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        XhibitApplicationController xac;

        xac = (XhibitApplicationController) getController();

        addSearchSpecification(new SearchOffenceCriteria());
        addSearchSpecification(new SearchOffenceResults());
        addSearchSpecification(new SearchOffenceDetails());

        XHIBITSearch xs = new XHIBITSearch(this, xac);

        //user did not select cancel during XHibit Search
        if (!this.isSearchCancelled()){
            if (this.getCaller() != null) {
                log.debug("OpenSearchOffenceAction: caller object present");
                if (this.getCaller() instanceof SearchProcessHandler) {
                    //Catches all instances where cancel is pressed and redirects user back to the opening Xhibit search dialog box
                    try{
                        ((SearchProcessHandler) this.getCaller()).processResults(this);
                    }catch (UserCancelException ue){
                        xActionPerformed(e);
                    }
    
                }
                else if (this.getCaller() instanceof GeneralTrial){
                    Collection col = this.getResults();
                    Iterator it = col.iterator();
                    if (it.hasNext()) {
                    	RefOffenceBasicValue refOffence = (RefOffenceBasicValue) it.next();
						String text = refOffence.getOffenceCode() +" : "+ refOffence.getOffenceDesc();
						if(refOffence.getOffenceDesc2()!=null) {
							text= text + " - "+refOffence.getOffenceDesc2();
						}
						((GeneralTrial)this.getCaller()).getTxtCharges().append(text+"\n");

                    } else {
                        throw new UserCancelException();
                    }

                }
        }

        }
    }
}