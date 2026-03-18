package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface WLLRecipient extends CSEntityLocal {

    public Integer getWllRecipientId();

    public Integer getCrestSolicitorFirmId();

    public Integer getCourtId();

    public String getSolicitorFirmName();

    public String getSolictiorFirmAddress();

    public String getSolicitorFirmFax();

    public String getSolicitorFirmEmail();

    public void setCrestSolicitorFirmId(Integer crestSolicitorFirmId);

    public void setCourtId(Integer courtId);

    public void setSolicitorFirmName(String solicitorFirmName);

    public void setSolictiorFirmAddress(String solictiorFirmAddress);

    public void setSolicitorFirmFax(String solicitorFirmFax);

    public void setSolicitorFirmEmail(String solicitorFirmEmail);

    public void setDocumentDistribution(DocumentDistribution documentDistribution);

    public DocumentDistribution getDocumentDistribution();
}