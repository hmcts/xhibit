package uk.gov.courtservice.xhibit.business.entities.listing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListSaveResult;

/**
 * <p>
 * Title: ListMaintainer
 * </p>
 * <p>
 * Description: List Entity Maintainer Class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class ListMaintainer extends AbstractEntityMaintainer {
	private ListHome home = null;

	/**
	 * Default constructor.
	 */
	public ListMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public List findByPrimaryKey(Integer key) throws ObjectNotFoundException {
		try {
			log.debug("findByPrimaryKey");
			return this.getHome().findByPrimaryKey(key);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Find the collection using the court id.
	 * 
	 * @param integer
	 *            Court id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCourtId(Integer listId) throws ObjectNotFoundException {
		try {
			log.debug("findByCourtId");
			return this.getHome().findByCourtId(listId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass());
			throw new EJBException(f);
		}
	}

	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            List
	 * @return ListBasicValue
	 */
	public ListBasicValue getBasicValue(List local) {
		log.debug("getBasicValue");
		ListBasicValue value = new ListBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            List
	 * @return ListComplexValue
	 */
	public ListComplexValue getComplexValue(List local) {
		log.debug("getComplexValue");
		ListComplexValue value = new ListComplexValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * The List Home.
	 * 
	 * @return ListHome
	 */
	public ListHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise ListHome");
			this.home = (ListHome) CSServices.getServiceLocator().getLocalHome(ListHome.class);
		}
		return this.home;
	}

	protected void loadValue(ListBasicValue value, List local) {
	   	value.setListId(local.getListId());	
	   	value.setListTypeId(local.getListTypeId());	
	   	value.setListParentId(local.getListParentId());	
	   	value.setCourtId(local.getCourtId());
	   	value.setDraftOrFinal(local.getDraftOrFinal());
	   	value.setListNumber(local.getListNumber());	
	   	value.setListStartDate(local.getListStartDate());
	   	value.setListEndDate(local.getListEndDate());
	   	value.setPublishDate(local.getPublishDate());
	   	value.setPublishStatus(local.getPublishStatus());
	   	value.setPublishErrorReason(local.getPublishErrorReason());	
	   	value.setObsInd(local.getObsInd()); 
	   	value.setCreatedBy(local.getCreatedBy());
	   	value.setCreationDate(local.getCreationDate());
	   	value.setLastUpdatedBy(local.getLastUpdatedBy());
	   	value.setLastUpdateDate(local.getLastUpdateDate());
	   	value.setVersion(local.getVersion());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof ListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			ListBasicValue basicValue = (ListBasicValue) value;
			List local = getHome().create(basicValue.getListTypeId(), basicValue.getListParentId(), basicValue.getCourtId(),
					basicValue.getDraftOrFinal(), basicValue.getListNumber(), basicValue.getListStartDate(), 
					basicValue.getListEndDate(), basicValue.getPublishDate(),
					basicValue.getPublishStatus(), basicValue.getPublishErrorReason(), basicValue.getObsInd(), userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}
	
	
	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {

		if (!(value instanceof ListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		update(value, userDisplayName);

	}

	public ListSaveResult update(ListBasicValue value, String userDisplayName) throws ObjectNotFoundException {
		final ListSaveResult result = new ListSaveResult();
		try {
			// Get the current DB values
			List local = getHome().findByPrimaryKey(value.getId());
			result.setListId(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				
				final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				final String errorMessage = "The user " + userDisplayName + " has attempted to save List Id " + value.getId() + " "
						+ "with version " + value.getVersion() + " but the list was saved as version "
						+ local.getVersion() + " by user " + local.getLastUpdatedBy() + " at "
						+ df.format(local.getLastUpdateDate());
				
				log.warn(errorMessage);
				
				
				result.setSuccess(false);
				result.setReturnMessage(errorMessage);
				return result;
				
			}
			ListBasicValue basicValue = (ListBasicValue) value;

			// Update the record
			local.setListParentId(basicValue.getListParentId());
			local.setCourtId(basicValue.getCourtId());
			local.setDraftOrFinal(basicValue.getDraftOrFinal()); 
			local.setListNumber(basicValue.getListNumber());
			local.setListStartDate(basicValue.getListStartDate()); 
			local.setListEndDate(basicValue.getListEndDate());
			local.setPublishDate(basicValue.getPublishDate());
			local.setPublishStatus(basicValue.getPublishStatus()); 
			local.setPublishErrorReason(basicValue.getPublishErrorReason()); 
			local.setObsInd(basicValue.getObsInd());
			local.setUpdated(userDisplayName);
			return result;
			
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		} 	
	}

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new java.lang.UnsupportedOperationException();	
	}
	
	public void delete(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof ListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current DB values
			List local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			ListBasicValue basicValue = (ListBasicValue) value;

			// Update the record
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
}
