package uk.gov.courtservice.xhibit.business.entities.linkedcase;

// jdk
import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface LinkedCase extends CSEntityLocal {
    public Integer getLinkedCaseId();

    public void setCreatedBy(String createdBy);

    public String getCreatedBy();

    public void setLastUpdatedBy(String lastUpdatedBy);

    public String getLastUpdatedBy();

    public abstract void setCases(Collection cases);

    public abstract Collection getCases();
}