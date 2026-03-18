package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.AggravatingReasonsBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AggravatingReasonsComplexValue;

/**
 * Maintainer for aggravating reasons entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version $Revision: 1.0 $
 */
public class AggravatingReasonsMaintainer extends AbstractEntityMaintainer {
	private AggravatingReasonsHome home = null;

	/**
	 * Default constructor.
	 */
	public AggravatingReasonsMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public AggravatingReasons findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Find the entities using the defendantOnCaseId and refAggravatingReasonsId
	 * 
	 * @param defendantOnCaseId
	 * @param refAggravatingReasonsId
	 * @return Collection of entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findByDefOnCaseIdAndRefAggId(Integer defendantOnCaseId, Integer refAggravatingReasonsId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByDefendantOnCaseIdAndRefAggravatingReasonsId");
			return this.getHome().findByDefOnCaseIdAndRefAggId(defendantOnCaseId, refAggravatingReasonsId);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Find the entities using the supplied defendantOnCaseId.
	 * 
	 * @param id
	 *            DefendantOnCaseId to use when performing the search
	 * @return Collection of the local entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findNonObsoleteByDefendantOnCaseId(Integer defendantOnCaseId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findNonObsoleteByDefendantOnCaseId");
			return this.getHome().findNonObsoleteByDefendantOnCaseId(defendantOnCaseId);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            AggravatingReasons
	 * @return AggravatingReasonsBasicValue
	 */
	public AggravatingReasonsBasicValue getBasicValue(AggravatingReasons local) {
		log.debug("Entered: getBasicValue");
		AggravatingReasonsBasicValue value = new AggravatingReasonsBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            AggravatingReasons
	 * @return AggravatingReasonsComplexValue
	 */
	public AggravatingReasonsComplexValue getComplexValue(AggravatingReasons local) {
		log.debug("Entered: getComplexValue");
		AggravatingReasonsComplexValue value = new AggravatingReasonsComplexValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}
	
	/**
	 * The AggravatingReasons Home.
	 * 
	 * @return AggravatingReasonsHome
	 */
	public AggravatingReasonsHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise AggravatingReasonsHome");
			this.home = (AggravatingReasonsHome) CSServices.getServiceLocator().getLocalHome(AggravatingReasonsHome.class);
		}
		return this.home;
	}

	protected void loadValue(AggravatingReasonsBasicValue value, AggravatingReasons local) {
		value.setAggravatingReasonsId(local.getAggravatingReasonsId());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setRefAggravatingReasonsId(local.getRefAggravatingReasonsId());
		value.setObsInd(local.getObsInd());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof AggravatingReasonsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			AggravatingReasonsBasicValue basicValue = (AggravatingReasonsBasicValue) value;
			AggravatingReasons result = getHome().create(basicValue.getAggravatingReasonsId(), basicValue.getDefendantOnCaseId(),
					basicValue.getRefAggravatingReasonsId(),
					basicValue.getObsInd(), userDisplayName);
			return result;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof AggravatingReasonsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			AggravatingReasons local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			AggravatingReasonsBasicValue basicValue = (AggravatingReasonsBasicValue) value;

			// Update the record
			local.setDefendantOnCaseId(basicValue.getDefendantOnCaseId());
			local.setRefAggravatingReasonsId(basicValue.getRefAggravatingReasonsId());
			local.setObsInd(basicValue.getObsInd());
			local.setUpdated(userDisplayName);

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	@Override
	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}
	
}