package uk.gov.courtservice.xhibit.client.actions.search;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchStep;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: XHIBIT 2 AbstractSearchAction
 * </p>
 * <p>
 * Description: This class is the base for all search actions in the
 * action.search package.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * edited: Luis Valenzuela added clearSearchSpecification 
 * @version $Revision: 1.4 $
 * 
 */
public abstract class AbstractSearchAction extends XAction {
    protected final Logger log = CSServices.getLogger(getClass());

    private Vector mySearchSpecification = null;

    private Collection theResults = null;

    private boolean searchCanceled = false;

    public AbstractSearchAction() {
        this.mySearchSpecification = new Vector();
        this.theResults = new Vector();
    }

    public void setResults(Collection results) {
        this.theResults = results;
    }

    public Collection getResults() {
        return this.theResults;
    }

    public void addSearchSpecification(XHIBITSearchStep xss) {
        this.mySearchSpecification.add(xss);
    }

    public void clearSearchSpecification() {
        this.mySearchSpecification.clear();
    }


    public Collection getSearchSpecification() {
        return this.mySearchSpecification;
    }

    public void setSearchCancelled() {
        this.setSearchCancelled(true);
    }

    public void setSearchCancelled(boolean flag) {
        this.searchCanceled = flag;
    }

    public boolean isSearchCancelled() {
        return this.searchCanceled;
    }
}
