package uk.gov.courtservice.xhibit.client.search.court;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
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

public class SearchCourtCriteria extends XHIBITSearchCriteria {
    public static final int ALL_COURTS = 0;

    public static final int MAGISTRATES_ONLY = 1;

    public static final int CROWN_ONLY = 2;

    // by default display all courts
    private int searchCourts = ALL_COURTS;

    public SearchCourtCriteria() {
        super();
    }

    public SearchCourtCriteria(int searchCourts) {
        super();
        this.searchCourts = searchCourts;
    }

    public Class getCriteriaValueClass() {
        return RefCourtCriteria.class; // on the bisRef
        // return CourtCriteria.class; // on the sysRef
    }

    public void setCriteria() {
        // addCriteriaField( CriteriaAttributeName, XHIBITSearchResourceKey,
        // optional widget)
        XHIBITSearchCriteriaValue courtNameValue = new XHIBITSearchCriteriaValue("COURT_NAME",
                "court.CriteriaCard.courtName",
                new JTextField(new LimitedTextValidatingDocumentDecorator(255), null, 0), true, "");
        addCriteria(courtNameValue);

        // Hidden criteria
        XHIBITSearchCriteriaValue courtIdValue = new XHIBITSearchCriteriaValue("COURT_ID", "", null, false,
                XhibitSingleton.getInstance().getCourtId().toString());
        addCriteria(courtIdValue);

        // if only to return magistrates courts then restrict the search with
        // IS_PSD='Y'
        if (searchCourts == MAGISTRATES_ONLY) {
            // Hidden criteria
            XHIBITSearchCriteriaValue isPsdValue = new XHIBITSearchCriteriaValue("IS_PSD", "", null, false, "Y");
            addCriteria(isPsdValue);
        } else if (searchCourts == CROWN_ONLY) {
            // Hidden criteria
            XHIBITSearchCriteriaValue isPsdValue = new XHIBITSearchCriteriaValue("IS_PSD", "", null, false, "N");
            addCriteria(isPsdValue);
        }
    }

    public String getMethodName() {
        return "findCourts";
    }

    public String getStepTitleResourceKey() {
        return "court.CriteriaCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "court.CriteriaCard.description";
    }
}