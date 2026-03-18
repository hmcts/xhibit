package uk.gov.courtservice.xhibit.client.search.justice;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJusticeCriteria;
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
public class SearchJusticeCriteria extends XHIBITSearchCriteria {

    public SearchJusticeCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return RefJusticeCriteria.class;
    }

    public void setCriteria() {
        XHIBITSearchCriteriaValue justiceValue = new XHIBITSearchCriteriaValue("JUSTICE_NAME", "justice.criteria.name",
                new JTextField(new LimitedTextValidatingDocumentDecorator(70), null, 0), true, "");
        addCriteria(justiceValue);

        // Hidden criteria
        XHIBITSearchCriteriaValue courtIdValue = new XHIBITSearchCriteriaValue("COURT_ID", "", null, false,
                XhibitSingleton.getInstance().getCourtId().toString());
        addCriteria(courtIdValue);
    }

    public String getMethodName() {
        return "findJustices";
    }

    public String getStepTitleResourceKey() {
        return "justice.criteria.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "justice.criteria.xxdescription";
    }
}