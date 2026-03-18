package uk.gov.courtservice.xhibit.business.entities.courtsite;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.courtsatellite.CourtSatellite;

abstract public class CourtSiteBean extends CSEntityBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = -480719942475342595L;

	public Integer ejbCreate(String courtSiteName, String courtSiteCode, String displayName, Integer addressId, Court court,
            String obsInd, String shortName, String userDisplayName, String crestCourtId, String floaterText,
            String listName, Integer siteGroup, String tier) throws CreateException {
        setCourtSiteName(courtSiteName);
        setCourtSiteCode(courtSiteCode);
        setDisplayName(displayName);
        setAddressId(addressId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        setShortName(shortName);
        setCrestCourtId(crestCourtId);
        setFloaterText(floaterText);
        setListName(listName);
        setSiteGroup(siteGroup);
        setTier(tier);
        return null;
    }

    public void ejbPostCreate(String courtSiteName, String courtSiteCode, String displayName, Integer addressId, Court court,
            String obsInd, String shortName, String userDisplayName, String crestCourtId, String floaterText,
            String listName, Integer siteGroup, String tier) throws CreateException {
    	setCourt(court);
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setCourtSiteId(Integer courtSiteId);

    public abstract void setCourtSiteName(String courtSiteName);

    public abstract void setCourtSiteCode(String courtSiteCode);

    public abstract void setAddressId(Integer addressId);

    public abstract void setCourtId(Integer courtId);

    public abstract void setDisplayName(String displayName);

    public abstract void setObsInd(String obsInd);

    public abstract void setShortName(String shortName);
    
    public abstract void setCrestCourtId(String crestCourtId);
    
    public abstract void setFloaterText(String floaterText);
    
    public abstract void setListName(String listName);
    
    public abstract void setSiteGroup(Integer siteGroup);
    
    public abstract void setTier(String tier);

    public abstract Integer getCourtSiteId();

    public abstract String getCourtSiteName();

    public abstract String getCourtSiteCode();

    public abstract Integer getAddressId();

    public abstract Integer getCourtId();

    public abstract String getDisplayName();

    public abstract String getObsInd();

    public abstract String getShortName();
    
    public abstract String getCrestCourtId();

    public abstract String getFloaterText();

    public abstract String getListName();

    public abstract Integer getSiteGroup();

    public abstract String getTier();

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setCourt(uk.gov.courtservice.xhibit.business.entities.court.Court court);

    public abstract void setCourtRooms(java.util.Collection courtRooms);
    
    public abstract void setCourtSatellite(CourtSatellite courtSatellite);

    public abstract uk.gov.courtservice.xhibit.business.entities.court.Court getCourt();

    public abstract java.util.Collection getCourtRooms();
    
    public abstract CourtSatellite getCourtSatellite();
}