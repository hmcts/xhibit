package uk.gov.courtservice.xhibit.business.entities.defendanthistory;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantHistoryBasicValue;

public class DefendantHistoryMaintainer extends AbstractEntityMaintainer{
	private DefendantHistoryHome home = null;

	/**
	 * Default constructor.
	 */
	public DefendantHistoryMaintainer() {
	}
	
	/**
	 * The DefendantHistory Home.
	 * 
	 * @return DefendantHistoryHome
	 */
	public DefendantHistoryHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise DefendantHistory");
			this.home = (DefendantHistoryHome) CSServices.getServiceLocator().getLocalHome(DefendantHistoryHome.class);
		}
		return this.home;
	}
	
	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public DefendantHistory findByPrimaryKey(Integer key) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByPrimaryKey");
			return this.getHome().findByPrimaryKey(key);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	public Collection<DefendantHistory> findBySurname(String surname, Integer courtId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findBySurname");
			return this.getHome().findBySurname(surname, courtId);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Method is used to return a BasicValue from a DefendantHistory object
	 * @param local DefendantHistory
	 * @return basic value object.
	 */
	public DefendantHistoryBasicValue returnBasicValue(DefendantHistory local) {
		DefendantHistoryBasicValue value = new DefendantHistoryBasicValue(local.getDefendantHistoryId(), local.getVersion());
		value.setCourtId(local.getCourtId());
		value.setCrestDefendantId(local.getCrestDefendantId());
		value.setDateArchived(local.getDateArchived());
		value.setDateOfBirth(local.getDateOfBirth());
		value.setDefendantHistoryId(local.getDefendantHistoryId());
		value.setDefendantId(local.getDefendantId());
		value.setFirstName(local.getFirstName());
		value.setGender(local.getGender());
		value.setMiddleName(local.getMiddleName());
		value.setReasonDeleted(local.getReasonDeleted());
		value.setSurname(local.getSurname());
		return value;
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();

	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		throw new java.lang.UnsupportedOperationException();		
	}

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new java.lang.UnsupportedOperationException();		
	}
}
