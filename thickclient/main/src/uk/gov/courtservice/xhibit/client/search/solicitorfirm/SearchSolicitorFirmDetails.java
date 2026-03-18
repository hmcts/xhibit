package uk.gov.courtservice.xhibit.client.search.solicitorfirm;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;

/**
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 */
public class SearchSolicitorFirmDetails extends XHIBITSearchDetails {
    public CSValueObject getDetailsValueObject() {
        return new RefSolicitorFirmBasicValue();
    }

    public void setDetails() {
        addDetail("solicitorFirmName", "solicitorFirm.details.solicitorFirmName");
        addDetail("shortName", "solicitorFirm.details.shortName");
        addDetail("address1", "solicitorFirm.details.address1");
        addDetail("address2", "solicitorFirm.details.address2");
        addDetail("town", "solicitorFirm.details.town");
        addDetail("postcode", "solicitorFirm.details.postCode");
    }

    public String getStepTitleResourceKey() {
        return "solicitorFirm.details.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "solicitorFirm.details.xxdescription";
    }
}