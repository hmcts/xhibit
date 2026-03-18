package uk.gov.courtservice.xhibit.business.entities.courtsatellite;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;

public interface CourtSatelliteHome extends javax.ejb.EJBLocalHome {

	public CourtSatellite create(String internetSatelliteName, CourtSite courtSite, String obsInd, String userDisplayName)
			throws CreateException;

	public CourtSatellite findByPrimaryKey(Integer courtSatelliteId) throws FinderException;
}