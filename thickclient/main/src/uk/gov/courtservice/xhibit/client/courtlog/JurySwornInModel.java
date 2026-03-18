package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class JurySwornInModel extends FreeTextModel {

    protected String subSchema;

    protected String selectedItem;

    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String s) {
        subSchema = s;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("JurySwornInModel");
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
        XHIBITConstant.info("--------------------------------");
        XHIBITConstant.info("Juror list              : ");
        XHIBITConstant.info("--------------------------------");
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
    }
}
