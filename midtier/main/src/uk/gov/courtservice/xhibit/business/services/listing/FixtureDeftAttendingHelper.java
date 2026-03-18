package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listing.FixtureDeftAttending;
import uk.gov.courtservice.xhibit.business.entities.listing.FixtureDeftAttendingMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;

/**
 * <p>
 * Title: FixtureDeftAttendingHelper
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jas Boparai
 * @version 1.0
 */
public class FixtureDeftAttendingHelper extends AbstractHelper {
	private FixtureDeftAttendingMaintainer fixtureDeftAttendingMaintainer;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public FixtureDeftAttendingHelper() {
		fixtureDeftAttendingMaintainer = new FixtureDeftAttendingMaintainer();
	}

	/**
	 * Description: Find FixtureDeftAttending
	 * 
	 * @param fixtureDeftAttendingId
	 * @return FixtureDeftAttendingBasicValue
	 * @throws FinderException 
	 */
	public FixtureDeftAttendingBasicValue findByPrimaryKey(Integer fixtureDeftAttendingId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByPrimaryKey(fixtureDeftAttendingId="+fixtureDeftAttendingId+")");
       	}
		try {
			FixtureDeftAttending local = fixtureDeftAttendingMaintainer.findByPrimaryKey(fixtureDeftAttendingId);
			FixtureDeftAttendingBasicValue result = fixtureDeftAttendingMaintainer.getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}
	}
	
	/**
	 * Description: Find Collection of FixtureDeftAttending by caseDiaryFixtureId
	 * 
	 * @param caseDiaryFixtureId
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection findByCaseDiaryFixtureId(Integer caseDiaryFixtureId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByCaseDiaryFixtureId(caseDiaryFixtureId="+caseDiaryFixtureId+")");
       	}
		try {
			Collection result = newCollection();
			Collection locals = fixtureDeftAttendingMaintainer.findByCaseDiaryFixtureId(caseDiaryFixtureId);
			for (FixtureDeftAttending local : (Collection<FixtureDeftAttending>) locals) {
				FixtureDeftAttendingBasicValue value = fixtureDeftAttendingMaintainer.getBasicValue(local);
				result.add(value);
			}
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}
	}
}