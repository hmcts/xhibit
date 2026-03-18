package uk.gov.courtservice.xhibit.business.entities.courtsatellite;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSatelliteBasicValue;

public class CourtSatelliteMaintainer extends ReferenceDataMaintainer {
	private static CourtSatelliteHome home = null;

	/**
	 * Given the Primary Key, find the Local instance of Court Site.
	 * 
	 * @param id
	 *            Integer
	 * @return Court
	 * @throws ObjectNotFoundException
	 */
	public CourtSatellite findByPrimaryKey(Integer id) throws ObjectNotFoundException {

		try {
			log.debug(ENTER_METHOD + "findByPrimaryKey(" + id + ")");
			return this.getHome().findByPrimaryKey(id);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Creates a [basic] value object from the local reference
	 * 
	 * @param Local
	 *            reference
	 * @return Value Object
	 */
	public CourtSatelliteBasicValue getCourtSatelliteBasicValue(CourtSatellite local) {

		log.debug(ENTER_METHOD + "getCourtSatelliteBasicValue");

		CourtSatelliteBasicValue value = new CourtSatelliteBasicValue(local.getCourtSatelliteId(), local.getVersion());
		loadValue(value, local);

		log.debug(EXIT_METHOD + "getCourtSatelliteBasicValue");

		return value;
	}

	/**
	 * Cache the home in our local static for all instances of our class to use.
	 * 
	 * @return CourtSite
	 */
	public CourtSatelliteHome getHome() {
		if (home == null) {
			log.debug(": Lazy initialise this.home.");
			home = (CourtSatelliteHome) CSServices.getServiceLocator().getLocalHome(CourtSatelliteHome.class);
		}
		return home;
	}

	private void loadValue(CourtSatelliteBasicValue value, CourtSatellite local) {
		value.setInternetSatelliteName(local.getInternetSatelliteName());
		value.setCourtSiteId(local.getCourtSiteId());
		value.setObsInd(local.getObsInd());
	}
	
	/**
	 * Deletes a CourtSatellite.
	 * 
	 * @param local
	 * @param userDisplayName
	 * @return
	 */
	public void deleteCourtSatellite(CourtSatellite local, String userDisplayName) {
		local.setObsInd("Y");
		local.setUpdated(userDisplayName);
	}
}
