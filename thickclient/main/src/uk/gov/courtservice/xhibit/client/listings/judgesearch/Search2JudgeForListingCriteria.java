package uk.gov.courtservice.xhibit.client.listings.judgesearch;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJudgeCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteriaValue;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */
public class Search2JudgeForListingCriteria extends XHIBITSearchCriteria {
	XHIBITSearchCriteriaValue firstnameValue;
	XHIBITSearchCriteriaValue surnameValue;
	
    public Search2JudgeForListingCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return RefJudgeCriteria.class;
    }

    public void setCriteria() {
    	
    	// Add the criteria to the screen
    	surnameValue = new XHIBITSearchCriteriaValue("SURNAME", "judge.criteria.surName",
                new JTextField(new LimitedTextValidatingDocumentDecorator(35), null, 0), true, "");
        addCriteria(surnameValue);
    	
    	firstnameValue = new XHIBITSearchCriteriaValue("FIRST_NAME",
                "judge.criteria.firstName", new JTextField(new LimitedTextValidatingDocumentDecorator(35), null, 0),
                true, "");
        addCriteria(firstnameValue);
        
        // hidden field
        XHIBITSearchCriteriaValue middlenameValue = new XHIBITSearchCriteriaValue("MIDDLE_NAME",
               null, null ,false, "");
        addCriteria(middlenameValue);
        
        // Court Id criteria hard coded to the current court
        XHIBITSearchCriteriaValue courtIdValue = new XHIBITSearchCriteriaValue("COURT_ID", "", null, false,
                XhibitSingleton.getInstance().getCourtId().toString());
        addCriteria(courtIdValue);
        
        // Ensure a complex return type
        getSearchCriteria().setDetailIndicator("YES");
    }

    public String getMethodName() {
        return "findJudges";
    }

    public String getStepTitleResourceKey() {
        return "judge.criteria.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "judge.criteria.xxdescription";
    }
}