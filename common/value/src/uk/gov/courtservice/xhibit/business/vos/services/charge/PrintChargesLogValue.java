package uk.gov.courtservice.xhibit.business.vos.services.charge;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: PrintChargesLogValue
 * </p>
 * <p>
 * Description: Encapsulate information for printing the audit information when
 * printing charges
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class PrintChargesLogValue extends CSAbstractValue {
	
	static final long serialVersionUID = -2431780683973363190L;
	
    private String formattedDate;

    private String formattedTime;

    private String headerLogText;

    private String logFreeText;

    public PrintChargesLogValue() {
    }

    public PrintChargesLogValue(String formattedDate, String formattedTime, String headerLogText, String logFreeText) {
        this.formattedDate = formattedDate;
        this.formattedTime = formattedTime;
        this.headerLogText = headerLogText;
        this.logFreeText = logFreeText;

    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public void setHeaderLogText(String headerLogText) {
        this.headerLogText = headerLogText;
    }

    public void setLogFreeText(String logFreeText) {
        this.logFreeText = logFreeText;
    }

    public String getFormattedDate() {
        return this.formattedDate;
    }

    public String getFormattedTime() {
        return this.formattedTime;
    }

    public String getHeaderLogText() {
        return this.headerLogText;
    }

    public String getLogFreeText() {
        return this.logFreeText;
    }

}