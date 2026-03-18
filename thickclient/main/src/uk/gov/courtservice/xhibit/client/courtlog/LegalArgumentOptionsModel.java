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
public class LegalArgumentOptionsModel extends FreeTextModel {

    protected String subSchema;

    protected String pajText;

    protected String dajText;

    protected String jrText;

    protected String caseType;

    protected boolean publicNotPermitted;

    public String getPAJText() {
        return pajText;
    }

    public void setPAJText(String s) {
        pajText = s;
    }

    public String getDAJText() {
        return dajText;
    }

    public void setDAJText(String s) {
        dajText = s;
    }

    public String getJRText() {
        return jrText;
    }

    public void setJRText(String s) {
        jrText = s;
    }

    public boolean getPublicNotPermitted() {
        return publicNotPermitted;
    }

    public void setPublicNotPermitted(boolean pnp) {
        publicNotPermitted = pnp;
    }

    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String s) {
        subSchema = s;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("LegalArgumentOptionsModel");
        XHIBITConstant.info("-------------------------");
        XHIBITConstant.info("In Edit Mode?           : " + isInEditMode());
        XHIBITConstant.info("DateTime                : " + getDateTime());
        XHIBITConstant.info("FreeText                : " + getFreeText());
        XHIBITConstant.info("EventId                 : " + getEventId());
        XHIBITConstant.info("EventType               : " + getEventType());
        XHIBITConstant.info("Schema                  : " + getSchema());
        XHIBITConstant.info("Sub Schema              : " + getSubSchema());
        XHIBITConstant.info("PanelText               : " + getPanelText());
        XHIBITConstant.info("XAC                     : " + getXac());
        XHIBITConstant.info("PAJ Text                : " + getPAJText());
        XHIBITConstant.info("DAJ Text                : " + getDAJText());
        XHIBITConstant.info("JR Text                 : " + getJRText());
        XHIBITConstant.info("PNP CheckBox            : " + getPublicNotPermitted());
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
        setPAJText(null);
        setDAJText(null);
        setJRText(null);
        setPublicNotPermitted(false);
    }
}
