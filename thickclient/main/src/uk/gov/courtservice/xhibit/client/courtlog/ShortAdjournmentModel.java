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

public class ShortAdjournmentModel extends FreeTextModel {

    private int subEventId;

    private String adjournmentHours;

    private String adjournmentMinutes;

    private String subEventCode;

    public ShortAdjournmentModel() {
        super();
    }

    // Getters
    public int getSubEventId() {
        return subEventId;
    }

    public String getAdjournmentHours() {
        return (adjournmentHours.length() < 2 ? "0" + adjournmentHours : adjournmentHours);
    }

    public String getAdjournmentMinutes() {
        return (adjournmentMinutes.length() < 2 ? "0" + adjournmentMinutes : adjournmentMinutes);
    }

    public String getSubEventCode() {
        return subEventCode;
    }

    // Setters
    public void setSubEventId(int param) {
        subEventId = param;
    }

    public void setAdjournmentHours(String param) {
        adjournmentHours = param;
    }

    public void setAdjournmentMinutes(String param) {
        adjournmentMinutes = param;
    }

    public void setSubEventCode(String param) {
        subEventCode = param;
    }

    // Utility methods
    public void printModel() {
        super.printModel();

        XHIBITConstant.info("ShortAdjourmentModel");
        XHIBITConstant.info("--------------------");
        XHIBITConstant.info("Sub Event ID    : " + getSubEventId());
        XHIBITConstant.info("Sub Event Code  : " + getSubEventCode());
        XHIBITConstant.info("Adjournment HH  : " + getAdjournmentHours());
        XHIBITConstant.info("Adjournment MM  : " + getAdjournmentMinutes());
    }

    public void clearmodel() {
        super.clearmodel();

        setSubEventId(0);
        setAdjournmentHours(null);
        setAdjournmentMinutes(null);
        setSubEventCode(null);
    }
}
