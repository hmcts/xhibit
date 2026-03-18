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
import uk.gov.courtservice.xhibit.business.vos.entities.RefAggravatingReasonsBasicValue;

/**
 * Maintainer for ref aggravating reasons entity.
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
public class RefAggravatingReasonsMaintainer extends AbstractEntityMaintainer {
	private RefAggravatingReasonsHome home = null;

	/**
	 * Default constructor.
	 */
	public RefAggravatingReasonsMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public RefAggravatingReasons findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Find all the entities not marked as obsolete.
	 * 
	 * @return All entities
	 * @throws ObjectNotFoundException
	 */
	public Collection<RefAggravatingReasons> findAllNonObsolete() throws ObjectNotFoundException {
		try {
			log.debug("Entered: findAllNonObsolete");
			return this.getHome().findAllNonObsolete();
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
	 *            RefAggravatingReasons
	 * @return RefAggravatingReasonsBasicValue
	 */
	public RefAggravatingReasonsBasicValue getBasicValue(RefAggravatingReasons local) {
		log.debug("Entered: getBasicValue");
		RefAggravatingReasonsBasicValue value = new RefAggravatingReasonsBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * The AggravatingReasons Home.
	 * 
	 * @return AggravatingReasonsHome
	 */
	public RefAggravatingReasonsHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise AggravatingReasonsHome");
			this.home = (RefAggravatingReasonsHome) CSServices.getServiceLocator().getLocalHome(RefAggravatingReasonsHome.class);
		}
		return this.home;
	}

	protected void loadValue(RefAggravatingReasonsBasicValue value, RefAggravatingReasons local) {
		value.setRefAggravatingReasonsId(local.getRefAggravatingReasonsId());
		value.setReasonDescription(local.getReasonDescription());
		value.setObsInd(local.getObsInd());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefAggravatingReasonsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			RefAggravatingReasonsBasicValue basicValue = (RefAggravatingReasonsBasicValue) value;
			RefAggravatingReasons result = getHome().create(basicValue.getRefAggravatingReasonsId(), 
					basicValue.getReasonDescription(),
					basicValue.getObsInd(), userDisplayName);
			return result;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof RefAggravatingReasonsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RefAggravatingReasons local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RefAggravatingReasonsBasicValue basicValue = (RefAggravatingReasonsBasicValue) value;

			// Update the record
			local.setReasonDescription(basicValue.getReasonDescription());
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