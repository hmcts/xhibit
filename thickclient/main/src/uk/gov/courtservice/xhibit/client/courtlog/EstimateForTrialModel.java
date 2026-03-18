package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class EstimateForTrialModel extends FreeTextModel {

    private int subEventId;

    private String estimate;

    private String subEventCode;

    public EstimateForTrialModel() {
        super();
    }

    // Getters
    public int getSubEventId() {
        return subEventId;
    }

    public String getEstimate() {
        return estimate;
    }

    public String getSubEventCode() {
        return subEventCode;
    }

    // Setters
    public void setSubEventId(int id) {
        subEventId = id;
    }

    public void setEstimate(String text) {
        estimate = text;
    }

    public void setSubEventCode(String code) {
        subEventCode = code;
    }

    public void printModel() {
        super.printModel();

        XHIBITConstant.info("EstimateForTrialModel");
        XHIBITConstant.info("---------------------");
        XHIBITConstant.info("Sub Event ID    : " + getSubEventId());
        XHIBITConstant.info("Sub Event Code  : " + getSubEventCode());
        XHIBITConstant.info("Estimate        : " + getEstimate());
    }

    public void clearmodel() {
        setSubEventId(0);
        setSubEventCode(null);
        setEstimate(null);
    }
}
