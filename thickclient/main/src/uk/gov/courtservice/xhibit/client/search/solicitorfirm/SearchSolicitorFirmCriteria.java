package uk.gov.courtservice.xhibit.client.search.solicitorfirm;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSolicitorFirmCriteria;
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
public class SearchSolicitorFirmCriteria extends XHIBITSearchCriteria {

    public SearchSolicitorFirmCriteria() {
        super();
    }

    public Class getCriteriaValueClass() {
        return RefSolicitorFirmCriteria.class;
    }

    public void setCriteria() {
        // addCriteriaField( CriteriaAttributeName, XHIBITSearchResourceKey,
        // optional widget)
        // addCriteria("SOLICITOR_FIRM_NAME",
        // "solicitorFirm.criteria.solicitorFirmName");
        XHIBITSearchCriteriaValue firmValue = new XHIBITSearchCriteriaValue("SOLICITOR_FIRM_NAME",
                "solicitorFirm.criteria.solicitorFirmName", new JTextField(new LimitedTextValidatingDocumentDecorator(
                        35), null, 0), true, "");
        addCriteria(firmValue);

        // Hidden criteria
        XHIBITSearchCriteriaValue courtIdValue = new XHIBITSearchCriteriaValue("COURT_ID", "", null, false,
                XhibitSingleton.getInstance().getCourtId().toString());
        addCriteria(courtIdValue);
    }

    public String getMethodName() {
        return "findSolicitorFirms";
    }

    public String getStepTitleResourceKey() {
        return "solicitorFirm.criteria.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "solicitorFirm.criteria.xxdescription";
    }
}