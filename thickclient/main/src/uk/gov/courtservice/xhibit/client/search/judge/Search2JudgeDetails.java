package uk.gov.courtservice.xhibit.client.search.judge;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;

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
public class Search2JudgeDetails extends XHIBITSearchDetails {

    public Search2JudgeDetails() {
        super();
    }

    public CSValueObject getDetailsValueObject() {
        return new RefJudgeBasicValue();
    }

    public void setDetails() {
        // addDetail( DetailAttributeName, XHIBITSearchResourceKey, optional
        // widget)
        addDetail("title", "judge.details.title");
        addDetail("firstName", "judge.details.firstName");
        addDetail("middleName", "judge.details.middleName");
        addDetail("surname", "judge.details.surName");
        addDetail("fullListTitle1", "judge.details.fullListTitle1");
        addDetail("fullListTitle2", "judge.details.fullListTitle2");
        addDetail("fullListTitle3", "judge.details.fullListTitle3");
    }

    public String getStepTitleResourceKey() {
        return "judge.details.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "judge.details.xxdescription";
    }
}