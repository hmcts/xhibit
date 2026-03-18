package uk.gov.courtservice.xhibit.client.search.legalrep;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefLegalRepresentativeCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteriaValue;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;

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

public class SearchLegalRepCriteria extends XHIBITSearchCriteria {

    public SearchLegalRepCriteria() {
        super();
    }

    public void setCriteria() {
        addCriteria(new XHIBITSearchCriteriaValue("FIRST_NAME", "legalRep.criteria.firstName", new JTextField(
                new LimitedTextValidatingDocumentDecorator(35), null, 0), true, ""));

        addCriteria(new XHIBITSearchCriteriaValue("SURNAME", "legalRep.criteria.surName", new JTextField(
                new LimitedTextValidatingDocumentDecorator(35), null, 0), true, ""));
    }

    public String getMethodName() {
        return "findLegalRepresentatives";
    }

    public Class getCriteriaValueClass() {
        return RefLegalRepresentativeCriteria.class;
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