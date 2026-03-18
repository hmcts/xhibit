package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RecipientBean extends CSEntityBean implements EntityBean {

    public java.lang.Integer ejbCreate(String recipientName, String faxNumber, String emailAddress, Integer courtId,
            String prefDistributionType, String prefMimeType, String userDisplayName) throws CreateException {
        setRecipientName(recipientName);
        setFaxNumber(faxNumber);
        setEmailAddress(emailAddress);
        setCourtId(courtId);
        setPrefDistributionType(prefDistributionType);
        setPrefMimeType(prefMimeType);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String recipientName, String faxNumber, String emailAddress, Integer courtId,
            String prefDistributionType, String prefMimeType, String userDisplayName) throws CreateException {
    }

    /**
     * CMP Fields
     */
    public abstract void setRecipientId(java.lang.Integer recipientId);

    public abstract void setRecipientName(java.lang.String recipientName);

    public abstract void setFaxNumber(java.lang.String faxNumber);

    public abstract void setEmailAddress(java.lang.String emailAddress);

    public abstract void setCourtId(java.lang.Integer courtId);

    public abstract void setPrefDistributionType(java.lang.String prefDistributionType);

    public abstract void setPrefMimeType(java.lang.String prefMimeType);

    public abstract java.lang.Integer getRecipientId();

    public abstract java.lang.String getRecipientName();

    public abstract java.lang.String getFaxNumber();

    public abstract java.lang.String getEmailAddress();

    public abstract java.lang.Integer getCourtId();

    public abstract java.lang.String getPrefDistributionType();

    public abstract java.lang.String getPrefMimeType();

    /**
     * CMR Fields
     */
    public abstract void setDocumentDistribution(java.util.Collection documentDistribution);

    public abstract java.util.Collection getDocumentDistribution();
}