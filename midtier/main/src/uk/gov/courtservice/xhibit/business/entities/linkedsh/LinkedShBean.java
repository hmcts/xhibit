package uk.gov.courtservice.xhibit.business.entities.linkedsh;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class LinkedShBean extends CSEntityBean {

    public Integer ejbCreate(String userDisplayName) throws CreateException {
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String userDisplayName) throws CreateException {

    }

    // ------------------CMP-------------------------------------------------------------
    public abstract void setLinkedShId(Integer linkedShId);

    public abstract Integer getLinkedShId();

}