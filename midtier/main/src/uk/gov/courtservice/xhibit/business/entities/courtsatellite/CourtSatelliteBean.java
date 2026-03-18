package uk.gov.courtservice.xhibit.business.entities.courtsatellite;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;

/**
 * CourtSatelliteBean EntityBean class.
 * 
 * @author grewalg
 *
 */
public abstract class CourtSatelliteBean extends CSEntityBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2678660191847135579L;

	public Integer ejbCreate(String internetSatelliteName, CourtSite courtSite, String obsInd, String userDisplayName)
			throws CreateException {
		setInternetSatelliteName(internetSatelliteName);
		setObsInd(obsInd);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);

		return null;
	}

	public void ejbPostCreate(String internetSatelliteName, CourtSite courtSite, String obsInd, String userDisplayName)
			throws CreateException {
		setCourtSite(courtSite);
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract void setCourtSatelliteId(Integer courtSatelliteId);
	
	public abstract void setCourtSiteId(Integer courtSiteId);

	public abstract void setInternetSatelliteName(String internetSatelliteName);

	public abstract void setObsInd(String obsInd);

	public abstract Integer getCourtSatelliteId();
	
	public abstract Integer getCourtSiteId();

	public abstract String getInternetSatelliteName();

	public abstract String getObsInd();
	
	// CMR Fields
	public abstract void setCourtSite(CourtSite courtSite);
	
	public abstract CourtSite getCourtSite();
}
