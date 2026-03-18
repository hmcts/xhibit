package uk.gov.courtservice.xhibit.business.entities.defendantoncasehistory;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseHistoryBasicValue;

/**
 * Maintainer for the DefendantOnCaseHistory table.
 * @author waltersn
 *
 */
public class DefendantOnCaseHistoryMaintainer extends AbstractEntityMaintainer {
	private DefendantOnCaseHistoryHome home = null;

	/**
	 * Default constructor.
	 */
	public DefendantOnCaseHistoryMaintainer() {
	}
	
	/**
	 * The DefendantOnCaseHistory Home.
	 * 
	 * @return DefendantOnCaseHistoryHome
	 */
	public DefendantOnCaseHistoryHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise DefendantOnCaseHistory");
			this.home = (DefendantOnCaseHistoryHome) CSServices.getServiceLocator().getLocalHome(DefendantOnCaseHistoryHome.class);
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
	public DefendantOnCaseHistory findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	
	/**
	 * Used in QACAS , returns a collection of defoncase history objects based on the case history id.
	 * @param caseHistoryId Integer
	 * @return Collection<DefendantOnCaseHistory>
	 * @throws ObjectNotFoundException if 0 results are found.
	 */
	public Collection<DefendantOnCaseHistory> findByCaseHistoryId(Integer caseHistoryId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByCaseHistoryId");
			return this.getHome().findByCaseHistoryId(caseHistoryId);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Used in QACAS , returns a collection of defoncase history objects based on the case history id.
	 * @param caseHistoryId Integer
	 * @return Collection<DefendantOnCaseHistory>
	 * @throws ObjectNotFoundException if 0 results are found.
	 */
	public Collection<DefendantOnCaseHistory> findByDefendantHistoryId(Integer defHistoryId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByDefendantHistoryId");
			return this.getHome().findByDefendantHistoryId(defHistoryId);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Method used to convert a DefendantOnCaseHistory object to a basic value object.
	 * @param local DefendantOnCaseHistory
	 * @return Basic value object.
	 */
	public DefendantOnCaseHistoryBasicValue returnBasicValue(DefendantOnCaseHistory local) {
		DefendantOnCaseHistoryBasicValue basic = new DefendantOnCaseHistoryBasicValue(local.getDefendantOnCaseHistoryId(), local.getVersion());
		basic.setCaseHistoryId(local.getCaseHistoryId());
		basic.setDefendantHistoryId(local.getDefendantHistoryId());
		basic.setDefendantNumber(local.getDefendantNumber());
		basic.setDefendantOnCaseHistoryId(local.getDefendantOnCaseHistoryId());
		return basic;
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
