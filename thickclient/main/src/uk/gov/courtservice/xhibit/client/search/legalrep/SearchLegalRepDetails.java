package uk.gov.courtservice.xhibit.client.search.legalrep;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
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
public class SearchLegalRepDetails extends XHIBITSearchDetails {

    public SearchLegalRepDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefLegalRepresentativeBasicValue();
    }

    public void setDetails() {
        addDetail("firstName", "legalRep.details.firstName");
        addDetail("middleName", "legalRep.details.middleName");
        addDetail("surname", "legalRep.details.surName");
        addDetail("initials", "legalRep.details.initials");
        addDetail("title", "legalRep.details.title");
        addDetail("legalRepType", "legalRep.details.legalRepType");
    }

    public String getStepTitleResourceKey() {
        /** @todo: add entry in search resourceBundle */
        return "legalRep.criteria.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        /** @todo: add entry in search resourceBundle */
        return "legalRep.criteria.xxdescription";
    }
}