package uk.gov.courtservice.xhibit.client.search.caseSearch;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CaseCriteria;
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
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */

public class SearchCaseCriteria extends XHIBITSearchCriteria {
 
    public SearchCaseCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return CaseCriteria.class; 
   }

    public void setCriteria() {
    	XHIBITSearchCriteriaValue caseNumberNameValue = new XHIBITSearchCriteriaValue("CASE_NUMBER",
                "case.CriteriaCard.caseNumber",
                new JTextField(new LimitedTextValidatingDocumentDecorator(9), null, 0), true, "");
        addCriteria(caseNumberNameValue);
    
        XHIBITSearchCriteriaValue defendantNameValue = new XHIBITSearchCriteriaValue("DEFENDANT_NAME",
                "case.CriteriaCard.defendantName",
                new JTextField(new LimitedTextValidatingDocumentDecorator(250), null, 0), true, "");
        addCriteria(defendantNameValue);
    }

    public String getMethodName() {
        return "findCases";
    }

    public String getStepTitleResourceKey() {
        return "case.CriteriaCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "case.CriteriaCard.description";
    }
}