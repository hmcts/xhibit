package uk.gov.courtservice.xhibit.business.entities.courtroom;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;

public interface CourtRoom extends CSEntityLocal {

    public Integer getCourtRoomId();

    public Integer getCrestCourtRoomNo();

    public String getCourtRoomName();

    public String getDescription();

    public String getDisplayName();

    public String getObsInd();
    
    public String getSecurityInd();
    
    public String getVideoInd();

    public void setCourtRoomName(String courtRoomName);

    public void setCrestCourtRoomNo(Integer crestCourtRoomNo);

    public void setDescription(String description);

    public void setDisplayName(String displayName);

    public void setObsInd(String obsInd);
    
    public void setSecurityInd(String securityInd);

    public void setVideoInd(String videoInd);

    public CourtSite getCourtSite();

    public void setCourtSite(CourtSite courtSite);

    // public Collection getTerminals();
    // public void setTerminals(Collection terminals);

    public String getUrn();
}