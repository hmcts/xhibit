package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DocumentDistribution extends CSEntityLocal {

    public Integer getDocDistributionId();

    public String getDistributionType();

    public String getDocumentType();

    public String getMimeType();

    public Integer getRecipientId();

    public Integer getWllRecipientId();

    public Integer getCourtId();

    public String getUsePrefDistType();

    public void setDistributionType(String distributionType);

    public void setDocumentType(String documentType);

    public void setMimeType(String mimeType);

    public void setRecipientId(Integer recipientId);

    public void setWllRecipientId(Integer wllRecipientId);

    public void setCourtId(Integer courtId);

    public void setUsePrefDistType(String usePrefDistType);

    /**
     * CMR Fields
     */
    public void setRecipient(Recipient recipient);

    public Recipient getRecipient();

    public void setWLLRecipient(WLLRecipient wllRecipient);

    public WLLRecipient getWLLRecipient();
}