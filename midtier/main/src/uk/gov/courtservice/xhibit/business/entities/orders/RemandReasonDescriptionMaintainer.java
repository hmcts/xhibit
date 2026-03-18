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
import uk.gov.courtservice.xhibit.business.vos.entities.RemandReasonDescriptionBasicValue;

/**
 * Maintainer for remand reason description entity.
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
public class RemandReasonDescriptionMaintainer extends AbstractEntityMaintainer {
	private RemandReasonDescriptionHome home = null;

	/**
	 * Default constructor.
	 */
	public RemandReasonDescriptionMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public RemandReasonDescription findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Find all the entities.
	 * 
	 * @return The local interface of all the returned entities
	 */
	public Collection<RemandReasonDescription> findAll()throws FinderException {
		log.debug("Entered: findAll");
		try {
			return this.getHome().findAllReasonDescriptions();
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            RemandReasonDescription
	 * @return RemandReasonDescriptionBasicValue
	 */
	public RemandReasonDescriptionBasicValue getBasicValue(RemandReasonDescription local) {
		log.debug("Entered: getBasicValue");
		RemandReasonDescriptionBasicValue value = new RemandReasonDescriptionBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * The RemandReasonDescription Home.
	 * 
	 * @return RemandReasonDescriptionHome
	 */
	public RemandReasonDescriptionHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise RemandReasonDescriptionHome");
			this.home = (RemandReasonDescriptionHome) CSServices.getServiceLocator().getLocalHome(RemandReasonDescriptionHome.class);
		}
		return this.home;
	}

	protected void loadValue(RemandReasonDescriptionBasicValue value, RemandReasonDescription local) {
		value.setRemandReasonDescriptionId(local.getRemandReasonDescriptionId());
		value.setReasonCategory(local.getReasonCategory());
		value.setReasonDescription(local.getReasonDescription());
		value.setObsInd(local.getObsInd());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RemandReasonDescriptionBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			RemandReasonDescriptionBasicValue basicValue = (RemandReasonDescriptionBasicValue) value;
			RemandReasonDescription result = getHome().create(basicValue.getRemandReasonDescriptionId(), basicValue.getReasonCategory(),
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
		if (!(value instanceof RemandReasonDescriptionBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RemandReasonDescription local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RemandReasonDescriptionBasicValue basicValue = (RemandReasonDescriptionBasicValue) value;

			// Update the record
			local.setRemandReasonDescriptionId(basicValue.getRemandReasonDescriptionId());
			local.setReasonCategory(basicValue.getReasonCategory());
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