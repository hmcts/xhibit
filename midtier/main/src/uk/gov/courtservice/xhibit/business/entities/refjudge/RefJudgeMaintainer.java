package uk.gov.courtservice.xhibit.business.entities.refjudge;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;

/**
 * Maintainer for Judge reference data type.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.7 $
 */
public class RefJudgeMaintainer extends ReferenceDataMaintainer {
	private RefJudgeHome home = null;

	/**
	 * Default constructor.
	 */
	public RefJudgeMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 */
	public RefJudge findByPrimaryKey(Integer key) throws ObjectNotFoundException {
		log.debug(ENTER_METHOD + "findByPrimaryKey");
		try {
			return this.getHome().findByPrimaryKey(key);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Find the entity using the supplied crest judge id.
	 * 
	 * @param courtId
	 *            the supplied courtId
	 * @param crestJudgeId
	 *            Crest Judge Id to use when performing the search
	 * @return The local interface of the returned entity(s)
	 */
	public Collection findByCourtIdAndCrestJudgeId(Integer courtId, Integer crestJudgeId) throws ObjectNotFoundException {
		log.debug(ENTER_METHOD + "findByCourtIdAndCrestJudgeId");
		try {
			return this.getHome().findByCourtIdAndCrestJudgeId(courtId, crestJudgeId);
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Find Judges matching courtId and statsCode.
	 * 
	 * @param courtId
	 *            the supplied courtId
	 * @param statsCode
	 *            the supplied statsCode
	 * @return The local interface of the returned entity
	 */
	public Collection findByCourtIdAndStatsCode(Integer courtId, String statsCode) throws ObjectNotFoundException {
		log.debug(ENTER_METHOD + "findByCourtIdAndStatsCode");
		try {
			return this.getHome().findByCourtIdAndStatsCode(courtId, statsCode);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Updates a Judge.
	 * 
	 * @param refJudgeId
	 * @param refJudge
	 * @throws SysRefControllerException
	 */
	public void updateRefJudge(Integer refJudgeId, RefJudgeComplexValue refJudgeCV) throws SysRefControllerException {
		try {
			// --- Update RefJudge fields ---
			RefJudge refJudge = this.getHome().findByPrimaryKey(refJudgeId);

			// check version
			if (refJudge.getVersion() == null || refJudgeCV.getVersion() == null
					|| !refJudge.getVersion().equals(refJudgeCV.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			refJudge.setLastUpdatedBy(refJudgeCV.getLastUpdatedBy());
			refJudge.setSurname(refJudgeCV.getSurname());
			refJudge.setInitials(refJudgeCV.getInitials());
			refJudge.setTitle(refJudgeCV.getTitle());
			refJudge.setFirstName(refJudgeCV.getFirstName());
			refJudge.setMiddleName(refJudgeCV.getMiddleName());
			refJudge.setHonours(refJudgeCV.getHonours());
			refJudge.setJudgeType(refJudgeCV.getJudgeType());
			refJudge.setStatsCode(refJudgeCV.getStatsCode());
			refJudge.setFullListTitle1(refJudgeCV.getFullListTitle1());
			refJudge.setFullListTitle2(refJudgeCV.getFullListTitle2());
			refJudge.setFullListTitle3(refJudgeCV.getFullListTitle3());
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}
	
	public Integer createRefJudge(RefJudgeComplexValue refJudgeCV, String userDisplayName) throws CreateException {
		RefJudge refJudge = this.getHome().create(refJudgeCV.getJudgeType(), 0, refJudgeCV.getFirstName(),
				refJudgeCV.getMiddleName(), refJudgeCV.getSurname(), refJudgeCV.getFullListTitle1(),
				refJudgeCV.getFullListTitle2(), refJudgeCV.getFullListTitle3(), refJudgeCV.getStatsCode(),
				refJudgeCV.getInitials(), refJudgeCV.getHonours(), null, "N", null, refJudgeCV.getTitle(),
				refJudgeCV.getCourtId(), userDisplayName);
		Integer refJudgeId = refJudge.getRefJudgeId();
		refJudge.setCrestJudgeId(refJudgeId);
		log.debug("Created RefJudge with id - " + refJudgeId);
		return refJudgeId;
	}

	/**
	 * Deletes a RefJudge by marking it as Obsolete.
	 * 
	 * @param refJudgeId
	 * @param userDisplayName
	 */
	public void deleteRefJudge(RefJudgeComplexValue refJudgeCV, String userDisplayName) throws SysRefControllerException {
		log.debug(ENTER_METHOD + "deleteRefJudge");
		try {
			RefJudge refJudge = this.getHome().findByPrimaryKey(refJudgeCV.getId());

			// check version
			if (refJudge.getVersion() == null || refJudgeCV.getVersion() == null
					|| !refJudge.getVersion().equals(refJudgeCV.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				refJudge.setObsInd("Y");
				refJudge.setLastUpdatedBy(userDisplayName);
			}
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            RefJudge
	 * @return RefJudgeBasicValue
	 */
	public RefJudgeBasicValue getBasicValue(RefJudge local) {
		log.debug(ENTER_METHOD + "getBasicValue");
		RefJudgeBasicValue value = new RefJudgeBasicValue(local.getRefJudgeId(), local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            RefHearingType
	 * @return RefHearingTypeComplexValue
	 */
	public RefJudgeComplexValue getComplexValue(RefJudge local) {
		log.debug(ENTER_METHOD + "getComplexValue");
		RefJudgeComplexValue value = new RefJudgeComplexValue(local.getRefJudgeId(), local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Home help ;o) No casting required.
	 */
	public RefJudgeHome getHome() {
		if (this.home == null) {
			log.debug("getHome: lazy initialise RefJudgeHome");
			this.home = (RefJudgeHome) CSServices.getServiceLocator().getLocalHome(RefJudgeHome.class);
		}
		return this.home;
	}

	/**
	 * Load the given value object with the data from the local reference.
	 * 
	 * @param RefJudgeBasicValue
	 *            value
	 * @param RefJudge
	 *            local
	 */
	private void loadValue(RefJudgeBasicValue value, RefJudge local) {
		value.setCourtId(local.getCourtId());
		value.setCrestJudgeId(local.getCrestJudgeId());
		value.setFirstName(local.getFirstName());
		value.setFullListTitle1(local.getFullListTitle1());
		value.setFullListTitle2(local.getFullListTitle2());
		value.setFullListTitle3(local.getFullListTitle3());
		value.setHonours(local.getHonours());
		value.setInitials(local.getInitials());
		value.setJudgeType(local.getJudgeType());
		value.setJudVers(local.getJudVers());
		value.setMiddleName(local.getMiddleName());
		value.setObsInd(local.getObsInd());
		value.setSourceTable(local.getSourceTable());
		value.setStatsCode(local.getStatsCode());
		value.setSurname(local.getSurname());
		value.setTitle(local.getTitle());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
	}
}