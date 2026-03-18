package uk.gov.courtservice.xhibit.client.search.justice;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeBasicValue;
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
public class SearchJusticeDetails extends XHIBITSearchDetails {

    public SearchJusticeDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefJusticeBasicValue();
    }

    public void setDetails() {
        addDetail("title", "justice.details.title");
        addDetail("justiceName", "justice.details.name");
        addDetail("initials", "justice.details.initials");
    }

    public String getStepTitleResourceKey() {
        return "justice.details.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "justice.details.xxdescription";
    }
}