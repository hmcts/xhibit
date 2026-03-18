package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class MediumEventMDModel extends FreeTextModel {

    protected String subSchema;

    private Calendar dateBy;

    private boolean selectionRequired;

    public Calendar getDateBy() {
        return dateBy;
    }

    public void setDateBy(Calendar c) {
        dateBy = c;
    }

    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String s) {
        subSchema = s;
    }

    public void setSelectionRequired(boolean selectionRequired) {
        this.selectionRequired = selectionRequired;
    }

    public boolean isSelectionRequired() {
        return selectionRequired;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("MediumEventMDModel");
        XHIBITConstant.info("--------------------");
        XHIBITConstant.info("In Edit Mode?           : " + isInEditMode());
        XHIBITConstant.info("DateTime                : " + getDateTime());
        XHIBITConstant.info("FreeText                : " + getFreeText());
        XHIBITConstant.info("EventId                 : " + getEventId());
        XHIBITConstant.info("EventType               : " + getEventType());
        XHIBITConstant.info("Schema                  : " + getSchema());
        XHIBITConstant.info("Sub Schema              : " + getSubSchema());
        XHIBITConstant.info("PanelText               : " + getPanelText());
        XHIBITConstant.info("XAC                     : " + getXac());
        XHIBITConstant.info("DateBy                  : " + getDateBy());
    }

    public void clearmodel() {
        setDateTime(null);
        setInEditMode(false);
        setFreeText(null);
        setEventId(null);
        setEventType(null);
        setSchema(null);
        setSubSchema(null);
        setPanelText(null);
        setXac(null);
        setDateBy(null);
    }
}