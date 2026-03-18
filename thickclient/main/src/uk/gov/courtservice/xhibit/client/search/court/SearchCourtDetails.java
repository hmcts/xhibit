package uk.gov.courtservice.xhibit.client.search.court;

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
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public class SearchCourtDetails extends XHIBITSearchDetails {

    public SearchCourtDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefCourtBasicValue();
    }

    public void setDetails() {
        // addDetail( DetailAttributeName, XHIBITSearchResourceKey, optional
        // widget)
        addDetail("courtFullName", "court.DetailsCard.courtFullName");
        addDetail("courtShortName", "court.DetailsCard.courtShortName");
        // addDetail("courtType", "court.DetailsCard.courtType");
    }

    public String getStepTitleResourceKey() {
        return "court.DetailsCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "court.DetailsCard.description";
    }
}