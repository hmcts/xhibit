package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.ArrayList;
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
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;

/**
 * Maintainer for case listing entry entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version $Revision: 1.0 $
 */
public class CaseListingEntryMaintainer extends AbstractEntityMaintainer {
	private CaseListingEntryHome home = null;

	/**
	 * Default constructor.
	 */
	public CaseListingEntryMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public CaseListingEntry findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Find the entity using the case id.
	 * 
	 * @param Integer
	 *            Case id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public CaseListingEntry findByCaseId(Integer caseId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByCaseId");
			return this.getHome().findByCaseId(caseId);
		} catch (ObjectNotFoundException e) {
			throw e;
		} catch (FinderException f) {
			throw new EJBException(f);
		}
	}
	
	/**
	 * Find the collection matching the judge id.
	 * 
	 * @param Integer
	 *            Judge id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByJudgeId(Integer judgeId) throws ObjectNotFoundException {
		Collection<CaseListingEntryBasicValue> retList = new ArrayList<CaseListingEntryBasicValue>();
		try {
			log.debug("Entered: findByJudgeId");
			Collection<CaseListingEntry> results = this.getHome().findByJudgeId(judgeId);
			for (CaseListingEntry local : results) {
				retList.add(getBasicValue(local));
			}
			return retList;
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
	 *            CaseListingEntry
	 * @return CaseListingEntryBasicValue
	 */
	public CaseListingEntryBasicValue getBasicValue(CaseListingEntry local) {
		log.debug("Entered: getBasicValue");
		CaseListingEntryBasicValue value = new CaseListingEntryBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            CaseListingEntry
	 * @return CaseListingEntryComplexValue
	 */
	public CaseListingEntryComplexValue getComplexValue(CaseListingEntry local) {
		log.debug("Entered: getComplexValue");
		CaseListingEntryComplexValue value = new CaseListingEntryComplexValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * The CaseListingEntry Home.
	 * 
	 * @return CaseListingEntryHome
	 */
	public CaseListingEntryHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise CaseListingEntryHome");
			this.home = (CaseListingEntryHome) CSServices.getServiceLocator().getLocalHome(CaseListingEntryHome.class);
		}
		return this.home;
	}

	protected void loadValue(CaseListingEntryBasicValue value, CaseListingEntry local) {
		value.setCaseListingEntryId(local.getCaseListingEntryId());
		value.setCaseId(local.getCaseId());
		value.setJudgeId(local.getJudgeId());
		value.setRefJudgeTypeId(local.getRefJudgeTypeId());
		value.setCourtId(local.getCourtId());
		value.setCourtSiteId(local.getCourtSiteId());
		value.setObsInd(local.getObsInd());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof CaseListingEntryBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			CaseListingEntryBasicValue basicValue = (CaseListingEntryBasicValue) value;
			CaseListingEntry result = getHome().create(basicValue.getCaseId(), basicValue.getRefJudgeTypeId(),
					basicValue.getCourtId(), basicValue.getCourtSiteId(), basicValue.getJudgeId(),
					basicValue.getObsInd(), userDisplayName);
			return result;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseListingEntryBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			CaseListingEntry cle = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (cle.getVersion() == null || value.getVersion() == null
					|| !cle.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			CaseListingEntryBasicValue basicValue = (CaseListingEntryBasicValue) value;

			// Update the record
			cle.setCourtId(basicValue.getCourtId());
			cle.setCourtSiteId(basicValue.getCourtSiteId());
			cle.setJudgeId(basicValue.getJudgeId());
			cle.setRefJudgeTypeId(basicValue.getRefJudgeTypeId());
			cle.setUpdated(userDisplayName);

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
	
	public void updateForAmendCase(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseListingEntryBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			CaseListingEntry cle = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (cle.getVersion() == null || value.getVersion() == null
					|| !cle.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			CaseListingEntryBasicValue basicValue = (CaseListingEntryBasicValue) value;

			// Update the record
			cle.setRefJudgeTypeId(basicValue.getRefJudgeTypeId());
			cle.setUpdated(userDisplayName);

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

}