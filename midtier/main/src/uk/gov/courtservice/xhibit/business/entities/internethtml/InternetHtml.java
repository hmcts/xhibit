package uk.gov.courtservice.xhibit.business.entities.internethtml;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface InternetHtml extends CSEntityLocal {
    public Integer getInternetHtmlId();

    public void setInternetHtmlId(String internetHtmlId);

    public String getStatus();

    public void setStatus(String status);

    public Integer getCourtId();

    public void setCourtId(Integer courtId);
    
    public Long getHtmlBlobId();

    public void setHtmlBlobId(Long htmlBlobId);

}