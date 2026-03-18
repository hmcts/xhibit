package uk.gov.courtservice.xhibit.business.entities.refjustice;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefJustice extends CSEntityLocal {

    public Integer getRefJusticeId(); // PK

    public Integer getCourtId(); // This will become a CMR

    public Integer getCrestJusticeId();

    public String getInitials();

    public String getJusticeName();

    public String getObsInd();

    public String getPsdCourtCode();

    public String getTitle();

    public void setCourtId(Integer courtId);

    public void setCrestJusticeId(Integer crestJusticeId);

    public void setInitials(String initials);

    public void setJusticeName(String justiceName);

    public void setObsInd(String obsInd);

    public void setPsdCourtCode(String psdCourtCode);

    public void setTitle(String title);
    /*
     * public Court getCourt(); public void getCourt(Court newValue);
     */
}