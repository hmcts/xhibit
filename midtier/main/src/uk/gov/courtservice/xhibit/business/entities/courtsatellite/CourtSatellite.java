package uk.gov.courtservice.xhibit.business.entities.courtsatellite;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;

/**
 * CourtSatellite Entity.
 * 
 * @author grewalg
 *
 */
public interface CourtSatellite extends CSEntityLocal {
	public Integer getCourtSatelliteId();
	
    public Integer getCourtSiteId();
    
    public String getInternetSatelliteName();
    
    public String getObsInd();
    
    public CourtSite getCourtSite();
    
    public void setCourtSatelliteId(Integer courtSatelliteId);
    
    public void setCourtSiteId(Integer courtSiteId);

    public void setInternetSatelliteName(String internetSatelliteName);
    
    public void setObsInd(String obsInd);
    
    public void setCourtSite(CourtSite courtSite);
}
