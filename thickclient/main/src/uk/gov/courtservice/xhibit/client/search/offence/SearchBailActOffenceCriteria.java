package uk.gov.courtservice.xhibit.client.search.offence;


import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteriaValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:SearchBailActOffenceCriteria
 * </p>
 * <p>
 * Description: Search Criteria to pull back all Bail act offences
 * </p>
 * <p>
 * Company: logica
 * </p>
 * 
 * @author luis Valenzuela
 * @version 1.0
 */




public class SearchBailActOffenceCriteria extends XHIBITSearchCriteria {
    
    private static String BAIL_ACT_SELECTION_FLAG = "Y";
    
    public SearchBailActOffenceCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return RefOffenceCriteria.class;
    }

    public void setCriteria() {
        addCriteria(new XHIBITSearchCriteriaValue("COURT_ID", "", null, false, XhibitSingleton.getInstance().getCourtId().toString()));
        addCriteria(new XHIBITSearchCriteriaValue("BAIL_ACT", "", null, false, BAIL_ACT_SELECTION_FLAG ));
    }

    public String getMethodName() {
        return "findOffences";
    }

    public String getStepTitleResourceKey() {
        return "offence.CriteriaCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "offence.CriteriaCard.description";
    }

}