package uk.gov.courtservice.xhibit.business.entities.linkedhearing;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class LinkedHearingBean extends CSEntityBean {

    public Integer ejbCreate(String userDisplayName) throws CreateException {
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String userDisplayName) throws CreateException {

    }

    public abstract void setLinkedHearingId(Integer linkedHearingId);

    public abstract Integer getLinkedHearingId();

}