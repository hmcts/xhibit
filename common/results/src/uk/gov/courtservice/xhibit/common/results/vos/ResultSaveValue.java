package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.Parameterizeable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version $Revision: 1.19 $
 * @todo Add Data Range Checks
 */
public abstract class ResultSaveValue extends ResultValue implements Parameterizeable, Serializable {
    
	static final long serialVersionUID = -7103457965275506488L;
	
	// Used to format
    private static final SimpleDateFormat CREST_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    // OPERATIONS
    public static final String ADD = "I";

    public static final String UPDATE = "U";

    public static final String DELETE = "D";

    // Input Info
    private String operation;

    private Integer courtLogCaseId;

    private Integer courtLogCaseNumber;

    private String courtLogCaseType;

    private Integer defendantOnCaseId; // required for resetting verified flag

    // on defendantOnCase

    // Return Info
    private Integer returnCode;

    private Integer oraCode;

    private String dataType; // datatype from ResultsSequenceMVO

    // should this result be court logged, defaults to true
    private boolean courtLogged = true;

    /**
     * default is CourtLogCaseId = EntityCaseId only different when part of a
     * joinder indictment
     */
    private Integer entityCaseId;

    private Integer entityCaseNumber;

    private String entityCaseType;

    // Constructor
    public ResultSaveValue(String operation, Integer caseId, Integer caseNumber, String caseType,
            Integer defendantOnCaseId) {
        setOperation(operation);
        setCourtLogCaseId(caseId);
        setCourtLogCaseNumber(caseNumber);
        setCourtLogCaseType(caseType);
        setDefendantOnCaseId(defendantOnCaseId);

        /**
         * default is CourtLogCaseId = EntityCaseId only different when part of
         * a joinder indictment
         */
        setEntityCaseId(caseId);
        setEntityCaseNumber(caseNumber);
        setEntityCaseType(caseType);

    }

    // Input Accesors
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getCourtLogCaseId() {
        return courtLogCaseId;
    }

    public void setCourtLogCaseId(Integer courtLogCaseId) {
        this.courtLogCaseId = courtLogCaseId;
    }

    public Integer getCourtLogCaseNumber() {
        return courtLogCaseNumber;
    }

    public void setCourtLogCaseNumber(Integer courtLogCaseNumber) {
        this.courtLogCaseNumber = courtLogCaseNumber;
    }

    public String getCourtLogCaseType() {
        return courtLogCaseType;
    }

    public void setCourtLogCaseType(String courtLogCaseType) {
        this.courtLogCaseType = courtLogCaseType;
    }

    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }

    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }

    // Return Accessors
    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public Integer getOraCode() {
        return oraCode;
    }

    public void setOraCode(Integer oraCode) {
        this.oraCode = oraCode;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    // Debug
    public void appendDebugParameters(StringBuffer buffer) {
        buffer.append("operation=");
        buffer.append(operation);
        buffer.append(", courtLogCaseId=");
        buffer.append(courtLogCaseId);
        buffer.append(", courtLogCaseNumber=");
        buffer.append(courtLogCaseNumber);
        buffer.append(", courtLogCaseType=");
        buffer.append(courtLogCaseType);
        buffer.append(", defendantOnCaseId=");
        buffer.append(defendantOnCaseId);
        buffer.append(", returnCode=");
        buffer.append(returnCode);
        buffer.append(", oraCode=");
        buffer.append(oraCode);
        buffer.append(", dataType=");
        buffer.append(dataType);
    }

    // Crest Date Format
    protected static String formatCrestDate(Calendar calendar) {
        if (calendar == null) {
            return null;
        } else {
            return formatCrestDate(calendar.getTime());
        }
    }

    protected static String formatCrestDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return CREST_DATE_FORMATTER.format(date).toUpperCase();
        }
    }

    // Business methods
    public String getMessageKey() {
        return getDataType() + "." + getReturnCode();
    }

    // Parameterizeable Implementation
    public Object[] getMessageParameters() {
        return new Object[] { getOraCode(), getCourtLogCaseType(), getCourtLogCaseNumber() };
    }

    /**
     * @param courtLogged
     *            The courtLogged to set.
     */
    public void setCourtLogged(boolean courtLogged) {
        this.courtLogged = courtLogged;
    }

    /**
     * @return Returns the courtLogged.
     */
    public boolean isCourtLogged() {
        return courtLogged;
    }

    /**
     * Helper method used to determine if this save value represents a delete
     * operation.
     * 
     * @return <i>true</i> if a delete operation, <i>false</i> otherwise.
     */
    public boolean isDeleteOperation() {
        return DELETE.equals(getOperation());
    }

    /**
     * Helper method used to determine if this save value represents an update
     * operation.
     * 
     * @return <i>true</i> if an update operation, <i>false</i> otherwise.
     */
    public boolean isUpdateOperation() {
        return UPDATE.equals(getOperation());
    }

    /**
     * Helper method used to determine if this save value represents an add
     * operation.
     * 
     * @return <i>true</i> if an add operation, <i>false</i> otherwise.
     */
    public boolean isAddOperation() {
        return ADD.equals(getOperation());
    }

    public void setEntityCaseId(Integer entityCaseId) {
        this.entityCaseId = entityCaseId;
    }

    public Integer getEntityCaseId() {
        return entityCaseId;
    }

    public void setEntityCaseNumber(Integer entityCaseNumber) {
        this.entityCaseNumber = entityCaseNumber;
    }

    public Integer getEntityCaseNumber() {
        return entityCaseNumber;
    }

    public void setEntityCaseType(String entityCaseType) {
        this.entityCaseType = entityCaseType;
    }

    public String getEntityCaseType() {
        return entityCaseType;
    }
}