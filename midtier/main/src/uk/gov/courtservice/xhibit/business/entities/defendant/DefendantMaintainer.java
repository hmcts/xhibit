package uk.gov.courtservice.xhibit.business.entities.defendant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
// jdk
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

public class DefendantMaintainer extends AbstractEntityMaintainer {

	private DefendantHome home = null;

	private static Logger log = CSServices.getLogger(DefendantHome.class);

	public DefendantMaintainer() {
		if (home == null) {
			home = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
		}
	}

	public DefendantBasicValue getDefendantBasicValue(Defendant local) {
		DefendantBasicValue value = new DefendantBasicValue(local.getDefendantId(), local.getVersion());
		setDefendantBasicValue(value, local);
		return value;
	}

	public DefendantComplexValue getDefendantComplexValue(Defendant local) {
		DefendantComplexValue value = new DefendantComplexValue(local.getDefendantId(), local.getVersion());
		setDefendantBasicValue(value, local);
		return value;
	}

	public Collection getDefendantBasicValues(Collection locals) {
		if (locals == null)
			return null;
		List<DefendantBasicValue> values = new ArrayList<DefendantBasicValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			values.add(getDefendantBasicValue((Defendant) it.next()));
		}
		return values;
	}

	public Collection getDefendantComplexValues(Collection locals) {
		if (locals == null)
			return null;
		List<DefendantComplexValue> values = new ArrayList<DefendantComplexValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			values.add(getDefendantComplexValue((Defendant) it.next()));
		}
		return values;
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();
	}

	public void update(CSAbstractValue value, String userDisplayName) {
	            throw new IllegalArgumentException("Unexpected type:" + value.getClass()); 
	}

	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * New Find by surname and court id method.
	 */

	public Collection findDefendantByCourtIdSurname(Integer courtId, String surname) {
		log.debug("*** entered into findBySurname ***");
		try {
			return home.findDefendantByCourtIdSurname(courtId, surname);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection findDefendantByCourtIdSurnameGenderFirstName(Integer courtId, String firstName, String surname,
			Integer gender) {
		log.debug("*** entered into findDefendantByCourtIdSurnameGenderFirstName ***");
		try {
			if(firstName.equals("%")) {
				return home.findDefendantByCourtIdSurnameGender(courtId, surname, gender);
			} else {
				return home.findDefendantByCourtIdSurnameGenderFirstName(courtId, firstName, surname, gender);
			}
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection findDefendantByCourtIdFirstNameSurname(Integer courtId, String firstName, String surname) {
		log.debug("*** entered into findDefendantByCourtIdSurnameFirstName ***");
		try {
			if(firstName.equals("%")) {
				return home.findDefendantByCourtIdSurname(courtId, surname);
			} else {
				return home.findDefendantByCourtIdFirstNameSurname(courtId, firstName, surname);
			}
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Defendant findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		log.debug("*** entered into findByPrimaryKey ***");
		try {
			return home.findByPrimaryKey(id);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Defendant findByKeyAndVersion(Integer id, Integer version) throws ObjectNotFoundException {
		try {
			return home.findByKeyAndVersion(id, version);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Defendant findMinCrestDefendantId() throws ObjectNotFoundException {
		try {
			return home.findMinCrestDefendantId();
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public void updateListing(DefendantValue defendantValue, String userDisplayName) throws ObjectNotFoundException {
		try {
			Integer key = defendantValue.getDefendantID();
			Integer version = defendantValue.getVersion();
			Defendant local = home.findByPrimaryKey(key);
			if (!local.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			
			local.setCurrentPrisonStatus(defendantValue.getCurrentPrisonStatus());
			local.setUpdated(userDisplayName);
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);	        
	    }		
	}

	private void setDefendantBasicValue(DefendantBasicValue value, Defendant local) {
		value.setCrestDefendantID(local.getCrestDefendantId());
		value.setFirstName(local.getFirstName());
		value.setMiddleName(local.getMiddleName());
		value.setSurname(local.getSurname());
		value.setInitials(local.getInitials());
		value.setDateOfBirth(convertToCalendar(local.getDateOfBirth()));
		value.setGender(local.getGender());
		value.setLastConvictionDate(convertToCalendar(local.getLastConvictionDate()));
		value.setAddressID(local.getAddressId());
		value.setCourtID(local.getCourtId());
		value.setIsCompany(local.getIsCompany());
		value.setPublicDisplayHide(local.getPublicDisplayHide());
		value.setCurrentPrisonStatus(local.getCurrentPrisonStatus());
	}
	
	/**
	 * Updates the DefendantOnCase entity with the values from Form A
	 * 
	 * @param defendantId
	 *            Integer
	 * @param currentPrisonStatus
	 *            String
	 * @param userDisplayName
	 *            String
	 */
	public void updateFormA(final Integer defendantId,  final String currentPrisonStatus, String userDisplayName) throws ObjectNotFoundException {
		try {
			Defendant defendant = findByPrimaryKey(defendantId);
			defendant.setUpdated(userDisplayName);
			defendant.setCurrentPrisonStatus(currentPrisonStatus);
		} catch (ObjectNotFoundException ex) {
			throw ex;
		}	
	}
}