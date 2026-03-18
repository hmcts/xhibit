package uk.gov.courtservice.xhibit.business.entities.ccinfo;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CcInfoBean extends CSEntityBean {

    public Integer ejbCreate(java.lang.String ccInfoText, String userDisplayName) throws CreateException {
        setCcInfoText(ccInfoText);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(java.lang.String ccInfoText, String userDisplayName) throws CreateException {
    }

    public abstract void setCcInfoId(Integer ccInfoId);

    public abstract void setCcInfoText(java.lang.String ccInfoText);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);

    // ----------------------------------------------------------------------------------------
    public abstract Integer getCcInfoId();

    public abstract java.lang.String getCcInfoText();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();

}