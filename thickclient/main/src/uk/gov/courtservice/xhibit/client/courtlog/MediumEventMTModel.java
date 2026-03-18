package uk.gov.courtservice.xhibit.client.courtlog;

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
public class MediumEventMTModel extends FreeTextModel {

    protected String subSchema;

    protected String enteredText;

    private boolean selectionRequired;

    public MediumEventMTModel() {
        super();
    }

    public String getEnteredText() {
        return enteredText;
    }

    public void setEnteredText(String s) {
        enteredText = s;
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
        XHIBITConstant.info("MediumEventMTModel");
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
        XHIBITConstant.info("EnteredText             : " + getEnteredText());
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
        setEnteredText(null);
    }
}
