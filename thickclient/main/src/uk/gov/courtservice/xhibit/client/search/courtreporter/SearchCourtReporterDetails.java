package uk.gov.courtservice.xhibit.client.search.courtreporter;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterComplexValue;
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
public class SearchCourtReporterDetails extends XHIBITSearchDetails {

    public SearchCourtReporterDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefCourtReporterComplexValue();
    }

    public void setDetails() {
        addDetail("initials", "personvalue.results.initials");
        addDetail("surname", "judge.details.surName");
        addDetail("refCourtReporterFirm.firmName", "courtreporter.DetailsCard.firmName");
    }

    public String getStepTitleResourceKey() {
        return "courtreporter.DetailsCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "courtreporter.DetailsCard.description";
    }
}