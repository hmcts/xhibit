package uk.gov.courtservice.xhibit.business.vos.services.systemadmin;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: ImportExportStatusVO
 * </p>
 * <p>
 * Description: A VO that describes an import/export status.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */
public class ImportExportStatusVO extends CSAbstractValue {
    private Integer importExportStatusId;

    private String typeCode;

    private String statusCode;

    private String message;

    private Integer caseId;

    private Integer courtId;

    private Integer defendantOnCaseId;

    private Date lastUpdateDate;

    private Date creationDate;

    private String createdBy;

    private String lastUpdatedBy;

    private Integer version;
    
    private static final long serialVersionUID =8142634139371714222L;

    /**
     * Empty constructor
     */
    public ImportExportStatusVO() {
    }

    public Integer getImportExportStatusId() {
        return this.importExportStatusId;
    }

    public String getTypeCode() {
        return this.typeCode;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCaseId() {
        return this.caseId;
    }

    public Integer getCourtId() {
        return this.courtId;
    }

    public Integer getDefendantOnCaseId() {
        return this.defendantOnCaseId;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setImportExportStatusId(Integer param) {
        this.importExportStatusId = param;
    }

    public void setTypeCode(String param) {
        this.typeCode = param;
    }

    public void setStatusCode(String param) {
        this.statusCode = param;
    }

    public void setMessage(String param) {
        this.message = param;
    }

    public void setCaseId(Integer param) {
        this.caseId = param;
    }

    public void setCourtId(Integer param) {
        this.courtId = param;
    }

    public void setDefendantOnCaseId(Integer param) {
        this.defendantOnCaseId = param;
    }

    public void setLastUpdateDate(Date param) {
        this.lastUpdateDate = param;
    }

    public void setCreationDate(Date param) {
        this.creationDate = param;
    }

    public void setCreatedBy(String param) {
        this.createdBy = param;
    }

    public void setLastUpdatedBy(String param) {
        this.lastUpdatedBy = param;
    }

    public void setVersion(Integer param) {
        this.version = param;
    }
}
