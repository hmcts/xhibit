package uk.gov.courtservice.xhibit.business.entities.courtroom;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomComplexValue;

/**
 * <p>
 * Summary.
 * </p>
 * <p>
 * Full description.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.15 $
 */
public class CourtRoomMaintainer extends ReferenceDataMaintainer {

	private static CourtRoomHome home = null;

	/**
	 * Given the Primary Key, find the Local instance of a Court Room.
	 * 
	 * @param id
	 *            Integer
	 * @return Court
	 */
	public CourtRoom findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
	 * Creates a [basic] value object given a local reference.
	 * 
	 * @param CourtRoom
	 *            local
	 * @return CourtRoomBasicValue
	 */
	public CourtRoomBasicValue getBasicValue(CourtRoom local) {
		log.debug(ENTER_METHOD + "getCourtRoomBasicValue");
		CourtRoomBasicValue value = new CourtRoomBasicValue(local.getCourtRoomId(), local.getVersion());
		this.loadValue(value, local);
		log.debug(EXIT_METHOD + "getCourtRoomBasicValue");
		return value;
	}

	/**
	 * Creates a [complex] value object given a local reference.
	 * 
	 * @param CourtRoom
	 *            local
	 * @return CourtRoomComplexValue
	 */
	public CourtRoomComplexValue getComplexValue(CourtRoom local) {
		log.debug(ENTER_METHOD + "getCourtRoomComplexValue");
		CourtRoomComplexValue value = new CourtRoomComplexValue(local.getCourtRoomId(), local.getVersion());
		this.loadValue(value, local);
		log.debug(EXIT_METHOD + "getCourtRoomComplexValue");
		return value;
	}

	/**
	 * Cache the home in our local static for all instances of our class to use.
	 * 
	 * @return CourtRoom
	 */
	public CourtRoomHome getHome() {
		if (home == null) {
			log.debug("::getHome: Lazy initialise this.home");
			home = (CourtRoomHome) CSServices.getServiceLocator().getLocalHome(CourtRoomHome.class);
		}
		return home;
	}

	/**
	 * Load the given value object with the data from the local reference.
	 * 
	 * @param CourtRoomBasicValue
	 *            value
	 * @param CourtRoom
	 *            local
	 */
	private void loadValue(CourtRoomBasicValue value, CourtRoom local) {
		value.setCourtRoomName(local.getCourtRoomName());
		value.setCrestCourtRoomNo(local.getCrestCourtRoomNo());
		value.setDescription(local.getDescription());
		value.setCourtSiteId(local.getCourtSite().getCourtSiteId());
		value.setDisplayName(local.getDisplayName());
		value.setObsInd(local.getObsInd());
		value.setSecurityInd(local.getSecurityInd());
		value.setVideoInd(local.getVideoInd());
	}
}