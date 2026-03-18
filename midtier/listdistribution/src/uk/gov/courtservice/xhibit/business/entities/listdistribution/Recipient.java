package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface Recipient extends CSEntityLocal {
    public Integer getRecipientId();

    public void setRecipientName(String recipientName);

    public void setFaxNumber(String faxNumber);

    public void setEmailAddress(String emailAddress);

    public void setCourtId(Integer courtId);

    public void setPrefDistributionType(java.lang.String prefDistributionType);

    public void setPrefMimeType(java.lang.String prefMimeType);

    public String getRecipientName();

    public String getFaxNumber();

    public String getEmailAddress();

    public Integer getCourtId();

    public java.lang.String getPrefDistributionType();

    public java.lang.String getPrefMimeType();

    public void setDocumentDistribution(Collection documentDistribution);

    public Collection getDocumentDistribution();

}