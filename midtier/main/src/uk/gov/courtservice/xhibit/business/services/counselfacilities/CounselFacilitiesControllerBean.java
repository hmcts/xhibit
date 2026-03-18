package uk.gov.courtservice.xhibit.business.services.counselfacilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.ejb.EJBException;

import uk.gov.courtservice.xhibit.business.entities.leoadvlink.LeoAdvLink;
import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.counsel.CounselFacilitiesQueries;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrder;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.AssignedRepresentativesCriteriaValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.SearchCounselFacilitiesCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.CourtList;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.LegalRepValue;
import uk.gov.courtservice.xhibit.business.entities.leoadvlink.LeoAdvLinkMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.LeoAdvLinkBasicValue;
import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocateMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocate;

/**
 * <p>
 * Title: CounselFacilitiesControllerBean
 * </p>
 * <p>
 * Description: Local interface to counsel facilities session facade.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @ejb.bean name="CounselFacilitiesController" description="Counsel Facilities
 *           Session Bean" type="Stateless" view-type="remote"
 *           jndi-name="CounselFacilitiesControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @author Ian Hannaford
 * @version $Revision: 1.23 $
 */
public class CounselFacilitiesControllerBean extends CSSessionBean implements SessionBean {

	// set up the helper class
	CounselFacilitiesHelper counselFacilitiesHelper = new CounselFacilitiesHelper();

	/**
	 * <p>
	 * This method will return a Collection of PartyOnCase value objects.
	 * </p>
	 * <p>
	 * They will differ depending on case type.
	 * </p>
	 * <ul>
	 * <li>If case is type of Appellent then there will be 1 row for Repsondent,
	 * 1 row for Objector and 1 row PER appellent.</li>
	 * <li>For all other cases there will be 1 row for prosecution and 1 row PER
	 * Defendant.</li>
	 * </ul>
	 * 
	 * @param courtId
	 *            ID of the courtHouse
	 * @param scheduleDate
	 *            date that you wish to view scheduled hearings
	 * @param courtRoomId
	 *            ID of the courtRoom
	 * @return Collection of PartyOnCase VO
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getAssignRepresentatives(Integer courtId, Date scheduleDate, Integer courtRoomId)
			throws CounselFacilitiesControllerException {
		String methodName = "getAssignRepresentatives(" + courtId + ", " + scheduleDate + ", " + courtRoomId + ") - ";
		log.debug(methodName + " : entered");

		try {
			return counselFacilitiesHelper.getAssignRepresentatives(courtId, scheduleDate, courtRoomId);
		} catch (CounselFacilitiesControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
	}

	/**
	 * @see CounselFacilitiesQueries.getCourtRoomList(Integer, Date, Integer)
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CourtList getCourtRoomList(Integer courtId, Date scheduleDate, Integer courtRoomId) {
		final CounselFacilitiesQueries cfq = new CounselFacilitiesQueries();
		return cfq.getCourtRoomList(courtId, scheduleDate, courtRoomId);
	}

	/**
	 * Errorhandling method that will be used for all catch blocks where the
	 * CounselFacilitiesControllerException is being caught. This is used since
	 * all public methods in this class are handled in the same way.
	 * 
	 * @param methodName
	 *            String
	 * @param e
	 *            Exception
	 */
	private void errorHandling(String methodName, Exception e) {
		ctx.setRollbackOnly();
		CSServices.getDefaultErrorHandler().handleError(e, getClass());
		log.debug(methodName + " : failed! Transaction Rollback");
	}

	/**
	 * <p>
	 * This method will return a Collection of PartyOnCase value objects.
	 * </p>
	 * <p>
	 * They will differ depending on case type.
	 * </p>
	 * <ul>
	 * <li>If case is type of Appellent then there will be 1 row for Repsondent,
	 * 1 row for Objector and 1 row PER appellent.</li>
	 * <li>For all other cases there will be 1 row for prosecution and 1 row PER
	 * Defendant.</li>
	 * </ul>
	 * 
	 * @param courtId
	 *            ID of the courtHouse
	 * @param scheduleDate
	 *            date that you wish to view scheduled hearings
	 * @return Collection of PartyOnCase VO
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getAssignRepresentatives(Integer courtId, Date scheduleDate)
			throws CounselFacilitiesControllerException {
		String methodName = "getAssignRepresentatives(" + courtId + ", " + scheduleDate + ") - ";
		log.debug(methodName + " : entered");

		try {
			return counselFacilitiesHelper.getAssignRepresentatives(courtId, scheduleDate, null);
		} catch (CounselFacilitiesControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
	}

	/**
	 * This methos uses the above method <code>getAssignRepresentatives</code>
	 * but takes a AssignedRepresentativesCriteriaValue VO rather than a
	 * scheduled hearing ID and date.
	 * 
	 * @param assignRepCriteria
	 *            AssignedRepresentativesCriteriaValue Value Obejct
	 * @return Collection of PartyOnCase VO
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getAssignRepresentatives(AssignedRepresentativesCriteriaValue assignRepCriteria)
			throws CounselFacilitiesControllerException {
		String methodName = "getAssignRepresentatives(" + assignRepCriteria + ") - ";
		log.debug(methodName + " : entered");

		try {
			return getAssignRepresentatives(assignRepCriteria.getCourtId(), assignRepCriteria.getDate(),
					assignRepCriteria.getCourtRoomId());
		} catch (CounselFacilitiesControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
	}

	/**
	 * This method is used to assign legal representatives to a case and role
	 * type, for a specific hearing.
	 * 
	 * @param shLegRepBasicValues
	 *            Collection of ShLegReps
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void setAssignRepresentativesUsingBasicVO(Collection shLegRepBasicValues, String userDisplayName)
			throws CounselFacilitiesControllerException {
		String methodName = "setAssignRepresentatives(" + shLegRepBasicValues + ") - ";
		log.debug(methodName + " : entered");
		counselFacilitiesHelper.setAssignRepresentatives(shLegRepBasicValues, userDisplayName);
	}

	/**
	 * This method uses the method in this class
	 * <code>setAssignRepresentatives</code> but takes a Complete VO rather than
	 * a basic VO.
	 * 
	 * @param legalRepValues
	 *            Collection of LegalRepValue
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void setAssignRepresentatives(Collection legalRepValues, String userDisplayName)
			throws CounselFacilitiesControllerException {
		String methodName = "setAssignRepresentatives(" + legalRepValues + ") - ";
		log.debug(methodName + " : entered");
		ArrayList shLegRepBVO = new ArrayList();

		try {
			Iterator legRepIter = legalRepValues.iterator();
			while (legRepIter.hasNext()) {
				// try to cast the object to a LegalRepValue
				LegalRepValue legRepVal = (LegalRepValue) legRepIter.next();
				shLegRepBVO.add(legRepVal.getSHLegRep());
			}
			setAssignRepresentativesUsingBasicVO(shLegRepBVO, userDisplayName);
		} catch (ClassCastException cce) {
			this.errorHandling(methodName, cce);
			throw new CounselFacilitiesControllerException("", cce.getMessage(), cce);
		} catch (CounselFacilitiesControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
	}

	/**
	 * This method checks if a defenant on a case has a legal aid order.
	 * 
	 * @parma defendantId java.lang.Integer
	 * @parma caseId java.lang.Integer
	 * @return boolean
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public boolean legalAidOrderGranted(java.lang.Integer defendantId, java.lang.Integer caseId)
			throws CounselFacilitiesControllerException {
		log.debug("legalAidOrderGranted(" + defendantId + "," + caseId + ") : entered");
		boolean legalAidOrderGranted;

		try {
			XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantId, caseId);

			XhbLegalAidOrder lao = XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseId(doc.getDefendantOnCaseId());

			log.debug("CREST legal aid order = " + lao.getCrestLeoId());
			legalAidOrderGranted = true;
		} catch (XhbLegalAidOrderBeanNotFoundException ex) {
			log.debug("Legal aid order not found");
			legalAidOrderGranted = false;
		} catch (XhbDefendantOnCaseBeanNotFoundException ex) {
			log.debug("Defendant on case not found for legal aid order");
			legalAidOrderGranted = false;
		}

		return legalAidOrderGranted;
	}
	
	/**
	 * Add a new instructed advocate.
	 * 
	 * @parma defendantId java.lang.Integer
	 * @parma caseId java.lang.Integer
	 * @param advocateId
	 *            java.lang.Integer
	 * @param defenceCategory
	 *            String
	 * @param available
	 *            String
	 * @parma crestPostNumber Integer
	 * 
	 * @return void
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void addInstructedAdvocate(java.lang.Integer defendantId, java.lang.Integer caseId,
			java.lang.Integer advocateId, String defenceCategory, String available, Integer crestPostNumber,
			String userDisplayName) throws CounselFacilitiesControllerException {
		String methodName = "addInstructedAdvocate(" + defendantId + "," + caseId + "," + advocateId + ","
				+ defenceCategory + "," + available + "," + crestPostNumber + ") - ";
		log.debug(methodName + " : entered");

		try {
			XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantId, caseId);

			XhbLegalAidOrder lao = XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseId(doc.getDefendantOnCaseId());

			RefAdvocateMaintainer ram = new RefAdvocateMaintainer();
			RefAdvocate ra = ram.findByRefLegalRepresentativeId(advocateId);

			LeoAdvLinkMaintainer instance = LeoAdvLinkMaintainer.getInstance();

			try {
				LeoAdvLink bean = instance.findByDefendantOnCaseRefAdvocateIdLegalAidOrderPostNumber(
						doc.getDefendantOnCaseId(), ra.getRefAdvocateId(), lao.getLegalAidOrderId(), crestPostNumber);

				log.debug("leo_adv_link already exists - update");
				bean.setAvailable(available);
				bean.setCrestAdvCategory(defenceCategory);
				bean.setNewRowFlag("N");
				bean.setUpdated(userDisplayName);

			} catch (ObjectNotFoundException notFound) {
				LeoAdvLinkBasicValue value = new LeoAdvLinkBasicValue();
				value.setLegalAidOrderId(lao.getLegalAidOrderId());
				value.setDefendantOnCaseId(doc.getDefendantOnCaseId());
				value.setCrestAdvCategory(defenceCategory);
				value.setRefAdvocateId(ra.getRefAdvocateId());
				value.setAvailable(available);
				value.setCrestPostNumber(crestPostNumber);
				value.setNewRowFlag("N");
				instance.create(value, userDisplayName);
			}

		} catch (EJBException ex) {
			this.errorHandling(methodName, ex);
			throw new CounselFacilitiesControllerException("", ex.getMessage(), ex);
		}
	}

	/**
	 * Update an instructed advocate.
	 * 
	 * @parma defendantId java.lang.Integer
	 * @parma caseId java.lang.Integer
	 * @param advocateId
	 *            java.lang.Integer
	 * @param defenceCategory
	 *            String
	 * @param available
	 *            String
	 * @parma crestPostNumber Integer
	 * 
	 * @return void
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateInstructedAdvocate(java.lang.Integer defendantId, java.lang.Integer caseId,
			java.lang.Integer advocateId, String defenceCategory, String available, Integer crestPostNumber,
			String userDisplayName)
			throws CounselFacilitiesControllerException {
		String methodName = "updateInstructedAdvocate(" + defendantId + "," + caseId + "," + advocateId + ","
				+ defenceCategory + "," + available + "," + crestPostNumber + ") - ";
		log.debug(methodName + " : entered");

		try {
			XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantId, caseId);

			XhbLegalAidOrder lao = XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseId(doc.getDefendantOnCaseId());

			RefAdvocateMaintainer ram = new RefAdvocateMaintainer();
			RefAdvocate ra = ram.findByRefLegalRepresentativeId(advocateId);

			LeoAdvLinkMaintainer instance = LeoAdvLinkMaintainer.getInstance();
			LeoAdvLink bean = instance.findByDefendantOnCaseRefAdvocateIdLegalAidOrderPostNumber(
					doc.getDefendantOnCaseId(), ra.getRefAdvocateId(), lao.getLegalAidOrderId(), crestPostNumber);

			bean.setAvailable(available);
			bean.setCrestAdvCategory(defenceCategory);
			bean.setNewRowFlag("N");
			bean.setUpdated(userDisplayName);

		} catch (EJBException ex) {
			this.errorHandling(methodName, ex);
			throw new CounselFacilitiesControllerException("", ex.getMessage(), ex);
		} catch (ObjectNotFoundException ex) {
			this.errorHandling(methodName, ex);
			throw new CounselFacilitiesControllerException("", ex.getMessage(), ex);
		}
	}

	/**
	 * Delete an instructed advocate.
	 * 
	 * @parma defendantId java.lang.Integer
	 * @parma caseId java.lang.Integer
	 * @param advocateId
	 *            java.lang.Integer
	 * @param userDisplayName
	 * @return void
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void deleteInstructedAdvocate(Integer defendantId, Integer caseId, Integer advocateId,
			Integer crestPostNumber, String userDisplayName) throws CounselFacilitiesControllerException {
		String methodName = "deleteInstructedAdvocate(" + defendantId + "," + caseId + "," + advocateId + ","
				+ crestPostNumber + ") - ";
		log.debug(methodName + " : entered");

		try {
			XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantId, caseId);

			XhbLegalAidOrder lao = XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseId(doc.getDefendantOnCaseId());

			RefAdvocateMaintainer ram = new RefAdvocateMaintainer();
			RefAdvocate ra = ram.findByRefLegalRepresentativeId(advocateId);

			LeoAdvLinkMaintainer instance = LeoAdvLinkMaintainer.getInstance();
			LeoAdvLink bean = instance.findByDefendantOnCaseRefAdvocateIdLegalAidOrderPostNumber(
					doc.getDefendantOnCaseId(), ra.getRefAdvocateId(), lao.getLegalAidOrderId(), crestPostNumber);

			bean.setNewRowFlag("N");
			bean.setObsInd("Y");
			bean.setUpdated(userDisplayName);

		} catch (EJBException ex) {
			this.errorHandling(methodName, ex);
			throw new CounselFacilitiesControllerException("", ex.getMessage(), ex);
		} catch (ObjectNotFoundException ex) {
			this.errorHandling(methodName, ex);
			throw new CounselFacilitiesControllerException("", ex.getMessage(), ex);
		}
	}

	/**
	 * This method searhes for counsels that are signed in and returns a
	 * Collection of PartyOnCaseValue objects
	 * 
	 * @param criteria
	 *            SearchCounselFacilitiesCriteria
	 * @return Collection PartyOnCaseValue
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection searchForCounsel(SearchCounselFacilitiesCriteria criteria)
			throws CounselFacilitiesControllerException {
		String methodName = "searchForCounsel(" + criteria.getCourtId() + ", " + criteria.getScheduleDate() + ") - ";
		log.debug(methodName + " : entered");
		try {
			return counselFacilitiesHelper.searchForCounsel(criteria);
		} catch (CounselFacilitiesControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
	}

	/**
	 * This method searches for defendants and returns a Collection of
	 * PartyOnCaseValue objects which contain what counsel is signed in to the
	 * defendant.
	 * 
	 * @param criteria
	 *            SearchCounselFacilitiesCriteria
	 * @return Collection PartyOnCaseValue
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection searchForDefendant(SearchCounselFacilitiesCriteria criteria)
			throws CounselFacilitiesControllerException {
		String methodName = "searchForCounsel(" + criteria.getCourtId() + ", " + criteria.getScheduleDate() + ") - ";
		log.debug(methodName + " : entered");
		try {
			return counselFacilitiesHelper.searchForDefendant(criteria);
		} catch (CounselFacilitiesControllerException cfce) {
			this.errorHandling(methodName, cfce);
			throw cfce;
		}
	}

	/**
	 * @param legalRepSignInValues
	 *            collection of legalRepSignInValue
	 * @throws CounselFacilitiesControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void removeSignedInLegalReps(Collection legalRepSignInValues) throws CounselFacilitiesControllerException {
		counselFacilitiesHelper.removeSignedInLegalReps(legalRepSignInValues);
	}

	/**
	 * Return a collection of Court Rooms Ids' for a given court site
	 * 
	 * @param site
	 *            court_site_id
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getCourtRoomIds(Integer site) {
		return counselFacilitiesHelper.getCourtRoomIds(site);
	}

	/**
	 * Return a collection of Court Site Details for a given Court Id
	 * 
	 * @param courtId
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getSiteInfo(Integer courtId) {
		return counselFacilitiesHelper.getSiteInfo(courtId);
	}
}