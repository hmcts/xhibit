package uk.gov.courtservice.xhibit.client.listings.judgesearch;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;

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
public class Search2JudgeForListingDetails extends XHIBITSearchDetails {

    public Search2JudgeForListingDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefJudgeComplexValue();
    }

    public void setDetails() {
        // addDetail( DetailAttributeName, XHIBITSearchResourceKey, optional
        // widget)
        addDetail("court.courtName", "judge.court.results.courtSite");
        addDetail("judgeType", "judge.court.results.judgeType");
        addDetail("title", "judge.court.results.title");
        addDetail("firstName", "judge.court.results.firstName");
        addDetail("middleName", "judge.court.results.middleName");
        addDetail("surname", "judge.court.results.surName");
        addDetail("honours", "judge.court.results.honours");
        addDetail("fullListTitle", "judge.court.results.listTitle");
        addDetail("allTicketTypes", "judge.court.results.ticketType");
        
    }

    public String getStepTitleResourceKey() {
        return "judge.details.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "judge.details.xxdescription";
    }
}