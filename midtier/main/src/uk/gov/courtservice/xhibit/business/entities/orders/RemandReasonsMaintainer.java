package uk.gov.courtservice.xhibit.business.entities.orders;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RemandReasonsBasicValue;

/**
 * Maintainer for remand reasons entity.
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
public class RemandReasonsMaintainer extends AbstractEntityMaintainer {
	private RemandReasonsHome home = null;

	/**
	 * Default constructor.
	 */
	public RemandReasonsMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public RemandReasons findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            RemandReasons
	 * @return RemandReasonsBasicValue
	 */
	public RemandReasonsBasicValue getBasicValue(RemandReasons local) {
		log.debug("Entered: getBasicValue");
		RemandReasonsBasicValue value = new RemandReasonsBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * The RemandReasons Home.
	 * 
	 * @return RemandReasonsHome
	 */
	public RemandReasonsHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise RemandReasonsHome");
			this.home = (RemandReasonsHome) CSServices.getServiceLocator().getLocalHome(RemandReasonsHome.class);
		}
		return this.home;
	}

	protected void loadValue(RemandReasonsBasicValue value, RemandReasons local) {
		value.setRemandReasonsId(local.getRemandReasonsId());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setOrderId(local.getOrderId());
		value.setRemandReasonDescriptionId(local.getRemandReasonDescriptionId());
		value.setAdditionalInformation(local.getAdditionalInformation());
		value.setObsInd(local.getObsInd());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RemandReasonsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			RemandReasonsBasicValue basicValue = (RemandReasonsBasicValue) value;
			RemandReasons result = getHome().create(basicValue.getRemandReasonsId(), basicValue.getDefendantOnCaseId(),
					basicValue.getOrderId(), basicValue.getRemandReasonDescriptionId(), basicValue.getAdditionalInformation(),
					basicValue.getObsInd(), userDisplayName);
			return result;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof RemandReasonsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RemandReasons local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RemandReasonsBasicValue basicValue = (RemandReasonsBasicValue) value;

			// Update the record
			local.setDefendantOnCaseId(basicValue.getDefendantOnCaseId());
			local.setOrderId(basicValue.getOrderId());
			local.setRemandReasonDescriptionId(basicValue.getRemandReasonDescriptionId());
			local.setAdditionalInformation(basicValue.getAdditionalInformation());
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