package uk.gov.courtservice.xhibit.business.entities.cpplist;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CppListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CppListComplexValue;

public class CppListMaintainer extends AbstractEntityMaintainer {

	private CppListHome home = null;

	private static Logger log = CSServices.getLogger(CppListHome.class);

	/**
	 * Default constructor.
	 */
	public CppListMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param cppListid
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public CppList findByPrimaryKey(Integer cppListId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByPrimaryKey");
			return this.getHome().findByPrimaryKey(cppListId);
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Find the entities using the supplied court and date.
	 * 
	 * @param crestCourtId
	 *            CREST 3 digit Court Code
	 * @param listType
	 *            List Type
	 * @param listStartDate
	 *            List start date
	 * @return The local interface of the returned entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCourtCodeAndListTypeAndListDate(final Integer courtCode, final String listType,
			final Date listStartDate) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByCourtCodeAndListTypeAndListDate");
			return this.getHome().findByCourtCodeAndListTypeAndListDate(courtCode, listType, listStartDate);
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Find the entities using the supplied court and date.
	 * 
	 * @param crestCourtId
	 *            CREST 3 digit Court Code
	 * @param listType
	 *            List Type
	 * @param listStartDate
	 *            List start date
	 * @param listEndDate
	 *            List end date
	 * @return The local interface of the returned entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCourtCodeAndListTypeAndListStartAndEndDate(final Integer courtCode, final String listType,
			final Date listStartDate, final Date listEndDate) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByCourtCodeAndListTypeAndListStartAndEndDate");
			return this.getHome().findByCourtCodeAndListTypeAndListStartAndEndDate(courtCode, listType, listStartDate, listEndDate);
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            CppList
	 * @return CppListBasicValue
	 */
	public CppListBasicValue getBasicValue(CppList local) {
		log.debug("Entered: getBasicValue");
		CppListBasicValue value = new CppListBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            CppList
	 * @return CppListComplexValue
	 */
	public CppListComplexValue getComplexValue(CppList local) {
		log.debug("Entered: getBasicValue");
		CppListComplexValue value = new CppListComplexValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}
	
	/**
	 * The CppList Home.
	 * 
	 * @return CppListHome
	 */
	public CppListHome getHome() {
		if (this.home == null) {
			this.home = (CppListHome) CSServices.getServiceLocator().getLocalHome(CppListHome.class);
		}
		return this.home;
	}

	protected void loadValue(CppListBasicValue value, CppList local) {
		value.setCppListId(local.getCppListId());
		value.setCourtCode(local.getCourtCode());
		value.setListType(local.getListType());
		value.setTimeLoaded(local.getTimeLoaded());
		value.setListStartDate(local.getListStartDate());
		value.setListEndDate(local.getListEndDate());
		value.setListClobId(local.getListClobId());
		value.setMergedClobId(local.getMergedClobId());
		value.setStatus(local.getStatus());
		value.setErrorMessage(local.getErrorMessage());		
		value.setObsInd(local.getObsInd());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof CppListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			CppListBasicValue basicValue = (CppListBasicValue) value;
			CppList result = getHome().create(basicValue.getCppListId(), basicValue.getCourtCode(),
					basicValue.getListType(), basicValue.getTimeLoaded(), 
					basicValue.getListStartDate(), basicValue.getListEndDate(), 
					basicValue.getListClobId(), basicValue.getMergedClobId(),
					basicValue.getStatus(),	basicValue.getErrorMessage(), userDisplayName);
			return result;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CppListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			CppList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			
			// Update the record
			CppListBasicValue basicValue = (CppListBasicValue) value;
			local.setCourtCode(basicValue.getCourtCode());
			local.setListType(basicValue.getListType());
			local.setTimeLoaded(basicValue.getTimeLoaded());
			local.setListStartDate(basicValue.getListStartDate()); 
			local.setListEndDate(basicValue.getListEndDate()); 
			local.setListClobId(basicValue.getListClobId());
			local.setMergedClobId(basicValue.getMergedClobId());
			local.setStatus(basicValue.getStatus());
			local.setErrorMessage(basicValue.getErrorMessage());
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
	
	public void delete(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			CppList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}

			// Update the record
			CppListBasicValue basicValue = (CppListBasicValue) value;
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