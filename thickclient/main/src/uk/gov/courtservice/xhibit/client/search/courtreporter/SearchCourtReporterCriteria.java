package uk.gov.courtservice.xhibit.client.search.courtreporter;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteriaValue;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT2
 * </p>
 * <p>
 * Description: SearchCourtReporterCriteria is a class used by the search
 * framework dictating which criteria should be used to display the Court
 * Reporter Criteria screen.
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
/*
 * Ref Date Author Description
 * 
 * 83,52539 28-04-2003 AW Daley Search criteria DetailIndicator set to complex
 * 
 */
public class SearchCourtReporterCriteria extends XHIBITSearchCriteria {

    public SearchCourtReporterCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return RefCourtReporterCriteria.class;
    }

    public void setCriteria() {
        // addCriteriaField( CriteriaAttributeName, XHIBITSearchResourceKey,
        // optional widget)
        XHIBITSearchCriteriaValue initialValue = new XHIBITSearchCriteriaValue("INITIALS",
                "personvalue.results.initials", new JTextField(new LimitedTextValidatingDocumentDecorator(4), null, 0),
                true, "");
        addCriteria(initialValue);

        XHIBITSearchCriteriaValue surnameValue = new XHIBITSearchCriteriaValue("SURNAME",
                "personvalue.results.surName", new JTextField(new LimitedTextValidatingDocumentDecorator(35), null, 0),
                true, "");
        addCriteria(surnameValue);

        XHIBITSearchCriteriaValue firmnameValue = new XHIBITSearchCriteriaValue("FIRM_NAME",
                "courtreporter.DetailsCard.firmName", new JTextField(new LimitedTextValidatingDocumentDecorator(35),
                        null, 0), true, "");
        addCriteria(firmnameValue);

        // Hidden criteria
        XHIBITSearchCriteriaValue courtIdValue = new XHIBITSearchCriteriaValue("COURT_ID", "", null, false,
                XhibitSingleton.getInstance().getCourtId().toString());
        addCriteria(courtIdValue);

        this.getSearchCriteria().setDetailIndicator("Complex");

    }

    public String getMethodName() {
        return "findCourtReporters";
    }

    public String getStepTitleResourceKey() {
        return "courtreporter.CriteriaCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "courtreporter.CriteriaCard.description";
    }

}
