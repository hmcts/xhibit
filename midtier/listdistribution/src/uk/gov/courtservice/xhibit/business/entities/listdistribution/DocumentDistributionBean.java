package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class DocumentDistributionBean extends CSEntityBean implements EntityBean {

    public java.lang.Integer ejbCreate(String distributionType, String documentType, String mimeType, Integer courtId,
            Integer recipientId, Integer wllRecipientId, String usePrefDistType, String userDisplayName) throws CreateException {

        setDistributionType(distributionType);
        setDocumentType(documentType);
        setMimeType(mimeType);
        setCourtId(courtId);
        setUsePrefDistType(usePrefDistType);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String distributionType, String documentType, String mimeType, Integer courtId,
            Integer recipientId, Integer wllRecipientId, String usePrefDistType, String userDisplayName) throws CreateException {
    }

    /**
     * CMP Fields
     */
    public abstract void setDocDistributionId(java.lang.Integer docDistributionId);

    public abstract void setDistributionType(java.lang.String distributionType);

    public abstract void setDocumentType(java.lang.String documentType);

    public abstract void setMimeType(java.lang.String mimeType);

    public abstract void setRecipientId(java.lang.Integer recipientId);

    public abstract void setWllRecipientId(java.lang.Integer wllRecipientId);

    public abstract void setCourtId(java.lang.Integer courtId);

    public abstract void setUsePrefDistType(java.lang.String usePrefDistType);

    public abstract java.lang.Integer getDocDistributionId();

    public abstract java.lang.String getDistributionType();

    public abstract java.lang.String getDocumentType();

    public abstract java.lang.String getMimeType();

    public abstract java.lang.Integer getRecipientId();

    public abstract java.lang.Integer getWllRecipientId();

    public abstract java.lang.Integer getCourtId();

    public abstract java.lang.String getUsePrefDistType();

    /**
     * CMR Fields
     */
    public abstract void setRecipient(uk.gov.courtservice.xhibit.business.entities.listdistribution.Recipient recipient);

    public abstract uk.gov.courtservice.xhibit.business.entities.listdistribution.Recipient getRecipient();

    public abstract void setWLLRecipient(
            uk.gov.courtservice.xhibit.business.entities.listdistribution.WLLRecipient wLLRecipient);

    public abstract uk.gov.courtservice.xhibit.business.entities.listdistribution.WLLRecipient getWLLRecipient();
}