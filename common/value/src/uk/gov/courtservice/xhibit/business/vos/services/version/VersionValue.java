package uk.gov.courtservice.xhibit.business.vos.services.version;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: Version information for XHIBIT
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: VersionValue.java,v 1.6 2009/12/04 15:28:08 hewittm Exp $
 */

public class VersionValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private String schemaName;

    private String schemaVersion;

    private String displayName;

    private int displaySeq;

    private java.sql.Timestamp lastUpdateDate;

    private String updatedBy;

    public VersionValue() {
        // empty
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplaySeq(int displaySeq) {
        this.displaySeq = displaySeq;
    }

    public int getDisplaySeq() {
        return displaySeq;
    }

    public void setLastUpdateDate(java.sql.Timestamp lastUpdated) {
        this.lastUpdateDate = lastUpdated;
    }

    public java.sql.Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }
}