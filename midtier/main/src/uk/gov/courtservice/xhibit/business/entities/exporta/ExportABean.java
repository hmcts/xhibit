package uk.gov.courtservice.xhibit.business.entities.exporta;

//jdk
import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ExportABean extends CSEntityBean implements EntityBean {

    // EntityContext entityContext;

    public java.lang.Integer ejbCreate(String courtClerkExport, String statusFlag, Integer linkedHearingID,
            Integer hearingId, String userDisplayName) throws CreateException {
        setCourtClerkExport(courtClerkExport);
        setStatusFlag(statusFlag);
        setLinkedHearingId(linkedHearingID);
        setHearingId(hearingId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String courtClerkExport, String statusFlag, Integer linkedHearingID, Integer hearingId, String userDisplayName)
            throws CreateException {

    }

    // ------------------------------CMP Fields - set
    // ------------------------------------
    public abstract void setExportAId(java.lang.Integer exportAId);

    public abstract void setCourtClerkExport(java.lang.String courtClerkExport);

    public abstract void setStatusFlag(java.lang.String statusFlag);

    public abstract void setLinkedHearingId(java.lang.Integer linkedHearingId);

    public abstract void setHearingId(Integer hearingId);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);

    // public abstract void setVersion(java.lang.Integer version);
    // public abstract void setCreationDate(java.sql.Date creationDate);
    // public abstract void setLastUpdateDate(java.sql.Date lastUpdateDate);

    // ------------------------------CMP Fields - get
    // ------------------------------------
    public abstract java.lang.Integer getExportAId();

    public abstract java.lang.String getCourtClerkExport();

    public abstract java.lang.String getStatusFlag();

    public abstract java.lang.Integer getLinkedHearingId();

    public abstract Integer getHearingId();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();
    // public abstract java.lang.Integer getVersion();
    // public abstract java.sql.Date getCreationDate();
    // public abstract java.sql.Date getLastUpdateDate();

}