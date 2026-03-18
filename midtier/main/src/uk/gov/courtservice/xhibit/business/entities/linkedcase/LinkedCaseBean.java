package uk.gov.courtservice.xhibit.business.entities.linkedcase;

// jdk
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class LinkedCaseBean extends CSEntityBean implements EntityBean {
    public Integer ejbCreate(String userDisplayName) throws CreateException {
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String userDisplayName) throws CreateException {

    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setLinkedCaseId(Integer linkedCaseId);

    public abstract void setCreatedBy(String createdBy);

    public abstract void setLastUpdatedBy(String lastUpdatedBy);

    public abstract Integer getLinkedCaseId();

    public abstract String getCreatedBy();

    public abstract String getLastUpdatedBy();

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setCases(Collection cases);

    public abstract Collection getCases();
}