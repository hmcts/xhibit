package uk.gov.courtservice.xhibit.business.vos.entities;

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
 * @author unascribed
 * @version 1.0
 */

public class ComboHelperVO {

    private String xml;

    private String db;

    private String displayText;

    public ComboHelperVO(String xmlValue, String dbValue, String displayText) {
        setXmlValue(xmlValue);
        setDbValue(dbValue);
        setDisplayText(displayText);
    }

    public String getXmlValue() {
        return xml;
    }

    public void setXmlValue(String xml) {
        this.xml = xml;
    }

    public void setDbValue(String db) {
        this.db = db;
    }

    public String getDbValue() {
        return db;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String toString() {
        return displayText;
    }
}