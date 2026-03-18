package uk.gov.courtservice.xhibit.business.vos.entities;

// jdk
import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DirForDefBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * DirectiosnForDefendant enitity attributes. DirForDefBasicValue objects need a
 * version and so need to extend CSAbstractValue
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 */

public class DirForDefBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -7029641856784871182L;
    
	// PK and the version are obtained from extending CSAbstractValue.
    private String freetext;

    private Calendar dateTime;

    private Integer toBeFiledBy;

    private String filedFormB;

    private Integer certAttendance;

    private String newBailConditions;

    private String bailStatus;

    private String arraigned;

    private String isIdentified;

    private Integer defendantOnCaseId;

    /**
     * Constructor for use with a pk and version
     * 
     * @param isIdentified
     * @param arraigned
     * @param bailStatus,
     * @param newBailConditions
     * @param certAttendance
     * @param filedFormB,
     * @param toBeFiledBy
     * @param dateTime
     * @param freetext
     * @param defendantOnCaseId
     */
    public DirForDefBasicValue(String isIdentified, String arraigned, String bailStatus, String newBailConditions,
            Integer certAttendance, String filedFormB, Integer toBeFiledBy, Calendar dateTime, String freetext,
            Integer defendantOnCaseId) {
        setIsIdentified(isIdentified);
        setArraigned(arraigned);
        setBailStatus(bailStatus);
        setNewBailConditions(newBailConditions);
        setCertAttendance(certAttendance);
        setFiledFormB(filedFormB);
        setToBeFiledBy(toBeFiledBy);
        setDateTime(dateTime);
        setFreetext(freetext);
        setDefendantOnCaseID(defendantOnCaseId);
    }

    /**
     * Constructor for use with a pk and version
     * 
     * @param pleaId
     *            the entity primary key.
     * @param version
     *            the version number.
     * @param isIdentified
     * @param arraigned
     * @param bailStatus,
     * @param newBailConditions
     * @param certAttendance
     * @param filedFormB,
     * @param toBeFiledBy
     * @param dateTime
     * @param freetext
     * @param defendantOnCaseId
     */
    public DirForDefBasicValue(Integer DirForDefId, Integer version, String isIdentified, String arraigned,
            String bailStatus, String newBailConditions, Integer certAttendance, String filedFormB,
            Integer toBeFiledBy, Calendar dateTime, String freetext, Integer defendantOnCaseId) {
        super(DirForDefId, version);
        setIsIdentified(isIdentified);
        setArraigned(arraigned);
        setBailStatus(bailStatus);
        setNewBailConditions(newBailConditions);
        setCertAttendance(certAttendance);
        setFiledFormB(filedFormB);
        setToBeFiledBy(toBeFiledBy);
        setDateTime(dateTime);
        setFreetext(freetext);
        setDefendantOnCaseID(defendantOnCaseId);
    }

    /**
     * No args constructor, for use with setters
     */
    public DirForDefBasicValue() {
    }

    // setters
    public void setIsIdentified(String isIdentified) {
        this.isIdentified = isIdentified;
    }

    public void setArraigned(String arraigned) {
        this.arraigned = arraigned;
    }

    public void setBailStatus(String bailStatus) {
        this.bailStatus = bailStatus;
    }

    public void setNewBailConditions(String newBailConditions) {
        this.newBailConditions = newBailConditions;
    }

    public void setCertAttendance(Integer certAttendance) {
        this.certAttendance = certAttendance;
    }

    public void setFiledFormB(String filedFormB) {
        this.filedFormB = filedFormB;
    }

    public void setToBeFiledBy(Integer toBeFiledBy) {
        this.toBeFiledBy = toBeFiledBy;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public void setFreetext(String freetext) {
        this.freetext = freetext;
    }

    public void setDefendantOnCaseID(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }

    // getters
    public String getIsIdentified() {
        return this.isIdentified;
    }

    public String getArraigned() {
        return this.arraigned;
    }

    public String getBailStatus() {
        return this.bailStatus;
    }

    public String getNewBailConditions() {
        return this.newBailConditions;
    }

    public Integer getCertAttendance() {
        return this.certAttendance;
    }

    public String getFiledFormB() {
        return this.filedFormB;
    }

    public Integer getToBeFiledBy() {
        return this.toBeFiledBy;
    }

    public Calendar getDateTime() {
        return this.dateTime;
    }

    public String getFreetext() {
        return this.freetext;
    }

    public Integer getDefendantOnCaseID() {
        return this.defendantOnCaseId;
    }

}
