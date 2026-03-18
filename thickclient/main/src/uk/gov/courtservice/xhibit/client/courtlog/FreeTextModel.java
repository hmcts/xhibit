package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author J Parthiban
 * @version 1.0
 */
public class FreeTextModel implements Cloneable {
    protected String eventType; // ie. the ID, eg 1010

    protected Calendar dateTime;

    protected String freeText;

    protected String schema; // This is the name that is written to the XML

    protected String panelText;

    protected Long eventId;

    protected boolean inEditMode;

    protected XhibitApplicationController xac;

    protected Integer defendantOnCaseId;

    protected Integer defendantOnOffenceId;

    protected Integer version;

    private Integer scheduledHearingId;

    // Note the following two properties will not be populated when the
    // event
    // is in edit mode. They will only be populated by the
    // CourtLogEventLevelPanel
    // when stepDeinitialise is called
    private String defendantName;

    private Integer defendantId;

    // Note the following property is populated by the
    // CourtLogEventLevelPanel
    // when stepDeinitialise is called but only for specific event codes
    // where the
    // user may select multiple defendants.
    // It is an Object array of DefendantBasicValue objects for the selected
    // defendants.
    private Object[] selectedDefendants;

    // The following property is used to indicate which type of defendant
    // dipslay
    // is required of the CourtLogEventLevelPanel - see
    // CourtLogEventLevelPanel for
    // valid values. Where this is not set, the default will be a JComboBox.
    private int defendantDisplayType;

    public FreeTextModel() {
        super();
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar c) {
        dateTime = c;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String s) {
        freeText = s;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String s) {
        eventType = s;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String s) {
        schema = s;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long id) {
        eventId = id;
    }

    public boolean isInEditMode() {
        return inEditMode;
    }

    public void setInEditMode(boolean param) {
        this.inEditMode = param;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public void setXac(XhibitApplicationController x) {
        xac = x;
    }

    public String getPanelText() {
        return panelText;
    }

    public void setPanelText(String s) {
        panelText = s;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("FreeTextModel");
        XHIBITConstant.info("--------------------");
        XHIBITConstant.info("In Edit Mode?           : " + isInEditMode());
        XHIBITConstant.info("DateTime                : " + getDateTime());
        XHIBITConstant.info("FreeText                : " + getFreeText());
        XHIBITConstant.info("EventId                 : " + getEventId());
        XHIBITConstant.info("version                 : " + getVersion());
        XHIBITConstant.info("EventType               : " + getEventType());
        XHIBITConstant.info("Schema                  : " + getSchema());
        XHIBITConstant.info("XAC                     : " + getXac());
        XHIBITConstant.info("Defendant On Case ID    : " + getDefendantOnCaseId());
        XHIBITConstant.info("Defendant On Offence ID : " + getDefendantOnOffenceId());
        XHIBITConstant.info("Defendant Display Type  : " + getDefendantDisplayType());
        XHIBITConstant.info("selectedDefendants:");
        XHIBITConstant.info("------------------");
        for (int x = 0; getSelectedDefendants() != null && x < getSelectedDefendants().length; x++) {
            DefendantBasicValue dbv = (DefendantBasicValue) getSelectedDefendants()[x];
            XHIBITConstant.info("    Defendant ID        : " + dbv.getId());
            XHIBITConstant.info("    Defendant Name      : " + PDHConstants.buildDefendantName(dbv));
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setDateTime(null);
        setInEditMode(false);
        setFreeText(null);
        setEventId(null);
        setEventType(null);
        setSchema(null);
        setXac(null);
    }

    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }

    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }

    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        this.defendantOnOffenceId = defendantOnOffenceId;
    }

    public Integer getDefendantOnOffenceId() {
        return defendantOnOffenceId;
    }

    public void setDefendantName(String defendantName) {
        this.defendantName = defendantName;
    }

    public Object[] getSelectedDefendants() {
        return selectedDefendants;
    }

    public void setSelectedDefendants(Object[] selectedDefendants) {
        this.selectedDefendants = selectedDefendants;
    }

    /**
     * This will only return a defendant name after stepDeinitialise is called
     * on the CourtLogEventPanel and if the event level is DEFENDANT or CRN
     * 
     * @return Defendant Name
     */
    public String getDefendantName() {
        return defendantName;
    }

    public void setDefendantId(Integer defendantId) {
        this.defendantId = defendantId;
    }

    /**
     * This will only return a defendant id after stepDeinitialise is called on
     * the CourtLogEventPanel and if the event level is DEFENDANT or CRN
     * 
     * @return
     */
    public Integer getDefendantId() {
        return defendantId;
    }

    public void setDefendantDisplayType(int type) {
        defendantDisplayType = type;
    }

    public int getDefendantDisplayType() {
        return defendantDisplayType;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }
}
