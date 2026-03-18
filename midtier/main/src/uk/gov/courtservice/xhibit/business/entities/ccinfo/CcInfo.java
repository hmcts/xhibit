package uk.gov.courtservice.xhibit.business.entities.ccinfo;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CcInfo extends CSEntityLocal {
    public Integer getCcInfoId();

    public void setCcInfoText(String ccInfoText);

    public String getCcInfoText();
}