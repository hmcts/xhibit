package uk.gov.courtservice.xhibit.business.entities.exporta;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface ExportA extends CSEntityLocal {

    // -------------------------- getter methods ------------------
    public Integer getExportAId();

    public String getCourtClerkExport();

    public String getStatusFlag();

    public Integer getLinkedHearingId();

    public Integer getHearingId();

    // public Integer getVersion();
    // public String getLastUpdatedBy();
    // public String getCreatedBy();
    // public Date getCreationDate();
    // public Date getLastUpdateDate();

    // ------------------------- setter methods -----------------
    public void setCourtClerkExport(String courtClerkExport);

    public void setStatusFlag(String statusFlag);

    public void setLinkedHearingId(Integer linkedHearingId);

    public void setHearingId(Integer hearingId);
    // public void setVersion(Integer version);
    // public void setLastUpdatedBy(String lastUpdatedBy);
    // public void setCreatedBy(String createdBy);
    // public void setCreationDate(Date creationDate);
    // public void setLastUpdateDate(Date lastUpdateDate);

}