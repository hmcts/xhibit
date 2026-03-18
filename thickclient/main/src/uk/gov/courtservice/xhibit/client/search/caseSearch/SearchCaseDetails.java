package uk.gov.courtservice.xhibit.client.search.caseSearch;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;

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
public class SearchCaseDetails extends XHIBITSearchDetails {

    public SearchCaseDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefCourtBasicValue();
    }

    public void setDetails() {
    	//TODO
        // addDetail("courtFullName", "case.DetailsCard.courtFullName");
    }

    public String getStepTitleResourceKey() {
        return "case.DetailsCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "case.DetailsCard.description";
    }
}