package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: FindCounselDefendantModel
 * </p>
 * <p>
 * Description: The model for finding a counsel or defendant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */
public class FindCounselDefendantModel implements Cloneable {
    // log directly from method rather than through XHIBITConstants
    private static Logger log = Logger.getLogger(FindCounselDefendantModel.class.getName());

    private String searchTypeRadio;

    private XhibitApplicationController xac;

    private Collection searchResults;


    public FindCounselDefendantModel() {
        // empty
    }

    // Getters
    public String getSearchTypeRadio() {
        return CounselFacilitiesHelper.tidyUp(searchTypeRadio);
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public Collection getSearchResults() {
        return searchResults;
    }

    // Setters
    public void setSearchTypeRadio(String param) {
        searchTypeRadio = param;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    public void setSearchResults(Collection param) {
        searchResults = param;
    }

    // Utility methods
    public void printModel() {
        log.debug("FindCounselDefendantModel");
        log.debug("-------------------------");
        log.debug("SearchTypeRadio      : " + getSearchTypeRadio());
        log.debug("XAC                  : " + getXac());
        log.debug("SearchResults.size( ): " + (getSearchResults() == null ? 0 : getSearchResults().size()));
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setSearchTypeRadio(null);
        setXac(null);
        setSearchResults(null);
    }
}
