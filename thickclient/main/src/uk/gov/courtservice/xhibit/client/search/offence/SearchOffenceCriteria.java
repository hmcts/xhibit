package uk.gov.courtservice.xhibit.client.search.offence;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
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
/*
 * Ref Date Author Description
 * 
 * 161 07-04 -2003 AW Daley Offence Type replaced with 52135 Offence
 * Description.
 */

public class SearchOffenceCriteria extends XHIBITSearchCriteria {
    public SearchOffenceCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return RefOffenceCriteria.class;
    }

    public void setCriteria() {
        addCriteria(new XHIBITSearchCriteriaValue("OFFENCE_CODE", "offence.CriteriaCard.offenceCode", new JTextField(
                new LimitedTextValidatingDocumentDecorator(8), null, 0), true, ""));

        addCriteria(new XHIBITSearchCriteriaValue("OFFENCE_DESC", "offence.CriteriaCard.offenceDescription",
                new JTextField(new LimitedTextValidatingDocumentDecorator(240), null, 0), true, ""));

        addCriteria(new XHIBITSearchCriteriaValue("STATUTE", "offence.CriteriaCard.statute", new JTextField(
                new LimitedTextValidatingDocumentDecorator(100), null, 0), true, ""));

        addCriteria(new XHIBITSearchCriteriaValue("COURT_ID", "", null, false, XhibitSingleton.getInstance()
                .getCourtId().toString()));
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