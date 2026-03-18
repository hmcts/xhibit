package uk.gov.courtservice.xhibit.business.entities.courtsite;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatellite;

public interface CourtSite extends CSEntityLocal {
    public Integer getCourtSiteId();

    public void setCourtSiteName(String courtSiteName);

    public String getCourtSiteName();

    public void setCourtSiteCode(String courtSiteCode);

    public String getCourtSiteCode();

    public void setAddressId(Integer addressId);

    public Integer getAddressId();

    public void setCourtId(Integer courtId);

    public Integer getCourtId();

    public void setDisplayName(String displayName);

    public String getDisplayName();

    public void setObsInd(String obsInd);

    public String getObsInd();

    public void setShortName(String shortName);

    public String getShortName();
    
    public void setCrestCourtId(String crestCourtId);
    
    public String getCrestCourtId();
    
    public void setFloaterText(String floaterText);
    
    public String getFloaterText();
    
    public void setListName(String listName);
    
    public String getListName();
    
    public void setSiteGroup(Integer siteGroup);
    
    public Integer getSiteGroup();
    
    public void setTier(String tier);
    
    public String getTier();

    public void setCourt(Court court);

    public Court getCourt();

    public void setCourtRooms(Collection courtRooms);

    public Collection getCourtRooms();
    
    public void setCourtSatellite(CourtSatellite courtSatellite);
    
    public CourtSatellite getCourtSatellite();
}