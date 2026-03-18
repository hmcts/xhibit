package uk.gov.courtservice.xhibit.client.search.judge;

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
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public class Search2JudgeCriteria extends XHIBITSearchCriteria {
    public Search2JudgeCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return RefJudgeCriteria.class;
    }

    public void setCriteria() {
    	XHIBITSearchCriteriaValue surnameValue = new XHIBITSearchCriteriaValue("SURNAME", "judge.criteria.surName",
                new JTextField(new LimitedTextValidatingDocumentDecorator(35), null, 0), true, "");
        addCriteria(surnameValue);
    	
    	XHIBITSearchCriteriaValue firstnameValue = new XHIBITSearchCriteriaValue("FIRST_NAME",
                "judge.criteria.firstName", new JTextField(new LimitedTextValidatingDocumentDecorator(35), null, 0),
                true, "");
        addCriteria(firstnameValue);

        XHIBITSearchCriteriaValue middlenameValue = new XHIBITSearchCriteriaValue("MIDDLE_NAME",
                "judge.criteria.middleName", new JTextField(new LimitedTextValidatingDocumentDecorator(35), null, 0),
                true, "");
        addCriteria(middlenameValue);

        // Hidden criteria
        XHIBITSearchCriteriaValue courtIdValue = new XHIBITSearchCriteriaValue("COURT_ID", "", null, false,
                XhibitSingleton.getInstance().getCourtId().toString());
        addCriteria(courtIdValue);
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