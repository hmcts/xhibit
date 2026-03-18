package uk.gov.courtservice.xhibit.business.entities.internethtml;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class InternetHtmlBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(String status, Integer courtId, Long htmlBlobId, String userDisplayName) throws CreateException {
        setStatus(status);
        setCourtId(courtId);
        setHtmlBlobId(htmlBlobId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String status, Integer courtId, Long htmlBlobId, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setInternetHtmlId(Integer internetHtmlId);

    public abstract void setStatus(String status);
    
    public abstract void setCourtId(Integer courtId);

    public abstract void setHtmlBlobId(Long htmlBlobId);

    public abstract Integer getInternetHtmlId();
    
    public abstract Integer getCourtId();

    public abstract String getStatus();

    public abstract Integer getHtmlBlobId();

}