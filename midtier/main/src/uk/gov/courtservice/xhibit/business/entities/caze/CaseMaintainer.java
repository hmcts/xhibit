package uk.gov.courtservice.xhibit.business.entities.caze;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 * 
 * <Change History/>
 * <P>
 * 17/02/03 - JB - Corrected findbyNumbeTypeAndCourtId to call the correct
 * method on the home
 * </P>
 */

// jdk
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_indictment_log.XhbIndictmentLog;
import uk.gov.courtservice.xhibit.business.entities.xhb_indictment_log.XhbIndictmentLogBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_indictment_log.XhbIndictmentLogBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.IndictmentLogValue;

public class CaseMaintainer extends AbstractEntityMaintainer {

	private CaseHome home = null;

	private static final String FOR_COURT=" for court ";
	private static final String UNEXPECTED_TYPE= "Unexpected type: ";
	private static final String OPTIMISTIC_LOCK = "Optimistic Lock Error";

	public CaseMaintainer() {
		if (home == null) {
			home = (CaseHome) CSServices.getServiceLocator().getLocalHome(CaseHome.class);
		}
	}

	public CaseBasicValue getCaseBasicValue(Case local) {
		CaseBasicValue value = new CaseBasicValue(local.getCaseId(), local.getVersion());
		setCaseBasicValue(value, local);
		return value;
	}

	public CaseComplexValue getCaseComplexValue(Case local) {
		CaseComplexValue value = new CaseComplexValue(local.getCaseId(), local.getVersion());
		setCaseBasicValue(value, local);
		return value;
	}

	public Collection getCaseBasicValues(Collection locals) {
		if (locals == null)
			return null;
		List<CaseBasicValue> caseValues = new ArrayList<CaseBasicValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			caseValues.add(getCaseBasicValue((Case) it.next()));
		}
		return caseValues;
	}

	public Collection getCaseComplexValues(Collection locals) {
		if (locals == null)
			return null;
		List<CaseComplexValue> caseValues = new ArrayList<CaseComplexValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			caseValues.add(getCaseComplexValue((Case) it.next()));
		}
		return caseValues;
	}

	public Case findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		try {
			return home.findByPrimaryKey(id);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Case findByKeyAndVersion(Integer id, Integer version) throws ObjectNotFoundException {
		try {
			return home.findByKeyAndVersion(id, version);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	// ObjectNotFoundException will not be thrown as this finder returns a
	// Collection,
	// an empty Collection will be returned if no records are found
	public Collection findByLinkedCaseId(Integer linkedCaseId) {
		try {
			return home.findByLinkedCaseId(linkedCaseId);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Case findByNumberTypeAndCourt(Integer caseNumber, String caseType, Integer courtId)
			throws ObjectNotFoundException {
		try {
			return home.findByNumberTypeAndCourt(caseNumber, caseType, courtId);
		} catch (ObjectNotFoundException e) {
			log.warn("Unable to find "+caseType+""+caseNumber+FOR_COURT+courtId);			
			throw e;
		} catch (FinderException e) {
			log.warn("Unable to find "+caseType+""+caseNumber+FOR_COURT+courtId);
			throw new EJBException(e);
		}
	}

	public Collection findAllCasesByDefendantId(Integer defendantId, Integer courtId) {
		try {
			return home.findAllCasesByDefendantId(defendantId, courtId);
		} catch (FinderException e) {
			log.warn("Unable to find cases by defId"+defendantId+FOR_COURT+courtId);			
			throw new EJBException(e);
		}
	}

	public Collection findByDefendantNamesAndCaseNumber(String defendantFirstName, String defendantSurname, String caseType, String caseNumber,
			Integer courtId) {
		try {
			return home.findByDefendantNamesAndCaseNumber(defendantFirstName, defendantSurname, caseType, caseNumber,
					courtId);
		} catch (FinderException e) {
			log.warn("Unable to find case "+caseType+""+caseNumber +" for "+defendantFirstName+" "+defendantSurname);
			throw new EJBException(e);
		}
	}

	public Collection findByDefendantSurnameAndCaseNumber(String defendantName, String caseType, String caseNumber,
			Integer courtId) {
		try {
			return home.findByDefendantSurnameAndCaseNumber(defendantName, caseType, caseNumber,
					courtId);
		} catch (FinderException e) {
			log.warn("Unable to find case "+caseType+""+caseNumber +" for "+defendantName);
			throw new EJBException(e);
		}
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();
	}

	public Case amendCase(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseBasicValue))
			throw new IllegalArgumentException(UNEXPECTED_TYPE + value.getClass());
		try {
			CaseBasicValue caseBasicValue = (CaseBasicValue) value;
			Case cs = home.findByPrimaryKey(value.getId());

			// check version
			if (cs.getVersion() == null || value.getVersion() == null || !cs.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException(OPTIMISTIC_LOCK);
			}
			cs.setCaseNumber(caseBasicValue.getCaseNumber());
			cs.setCaseType(caseBasicValue.getCaseType());
			cs.setCaseTitle(caseBasicValue.getCaseTitle());
			cs.setCaseSubType(caseBasicValue.getCaseSubType());
			
			// Only update the class code for non-trial cases or for trial cases providing the new value is not blank/null
			if ( !"T".equals(caseBasicValue.getCaseType()) ||
				("T".equals(caseBasicValue.getCaseType()) && null != caseBasicValue.getClassCode())) {
				log.debug("amendCase - Set Class Code to: " + caseBasicValue.getClassCode());
				cs.setClassCode(caseBasicValue.getClassCode());
			}
			
			cs.setCourtId(caseBasicValue.getCourtID());
			cs.setMonitoringCategoryId(caseBasicValue.getMonitoringCategoryId());
			cs.setAppealLodgedDate(caseBasicValue.getAppealLodgedDate());
			cs.setReceivedDate(caseBasicValue.getReceivedDate());
			cs.setSentForTrialDate(caseBasicValue.getSentForTrialDate());
			cs.setTicketRequired(caseBasicValue.getTicketRequired());
			cs.setTicketTypeCode(caseBasicValue.getTicketTypeCode());
			cs.setCourtIdReceivingSite(caseBasicValue.getCourtIdReceivingSite());
			cs.setCommittalDate(caseBasicValue.getCommittalDate());
			cs.setReceiptType(caseBasicValue.getReceiptType());
			cs.setEitherWayType(caseBasicValue.getEitherWayType());
			cs.setNoDefendantsForCase(caseBasicValue.getNoDefendantsForCase());
			cs.setTransferredCase(caseBasicValue.getTransferredCase());
			cs.setTransferDeferredSentence(caseBasicValue.getTransferDeferredSentence());
			cs.setDateTransFrom(caseBasicValue.getDateTransFrom());
			cs.setDateTransTo(caseBasicValue.getDateTransTo());
			cs.setOriginalCaseNumber(caseBasicValue.getOriginalCaseNumber());
			cs.setCccTransFromRefCourtId(caseBasicValue.getCccTransFromRefCourtId());
			cs.setCccTransToRefCourtId(caseBasicValue.getCccTransToRefCourtId());
			cs.setRetrial(caseBasicValue.getRetrial());
			cs.setSecureCourt(caseBasicValue.getSecureCourt());
			cs.setPreliminaryDateOfHearing(caseBasicValue.getPreliminaryDateOfHearing());
			cs.setRefCourtId(caseBasicValue.getRefCourtID());
			cs.setMagistratesCaseRef(caseBasicValue.getMagistratesCaseRef());
			cs.setMagCourtHearingTypeRefId(caseBasicValue.getMagCourtHearingTypeRefId());
			cs.setOriginalJps1(caseBasicValue.getOriginalJps1());
			cs.setOriginalJps2(caseBasicValue.getOriginalJps2());
			cs.setOriginalJps3(caseBasicValue.getOriginalJps3());
			cs.setOriginalJps4(caseBasicValue.getOriginalJps4());
			cs.setCaseDescription(caseBasicValue.getCaseDescription());
			cs.setPoliceForceCode(caseBasicValue.getPoliceForceCode());
			cs.setCaseListed(caseBasicValue.getCaseListed());
			cs.setCaseGroupNumber(caseBasicValue.getCaseGroupNumber());
			cs.setNoPageProsEvidence(caseBasicValue.getNoPageProsEvidence());
			cs.setOrigBodyDecisionDate(caseBasicValue.getOrigBodyDecisionDate());
			cs.setChargeImportIndicator(caseBasicValue.getChargeImportIndicator());
			cs.setMagConvictionDate(convertToTimestamp(caseBasicValue.getMagConvictionDate()));
			cs.setLcSentDate(caseBasicValue.getLcSentDate());
			cs.setEstPDHTrialLength(caseBasicValue.getEstPDHTrialLength());
			cs.setUpdated(userDisplayName);
			
			cs.setDateTransRecordedTo(caseBasicValue.getDateTransRecordedTo());
			
			cs.setCivilUnrest(caseBasicValue.getCivilUnrest());
			
			cs.setTelevisedAppGranted(caseBasicValue.getTelevisedAppGranted());
			cs.setTelevisedAppMadeDate(caseBasicValue.getTelevisedAppMadeDate());
			cs.setTelevisedApplicationMade(caseBasicValue.getTelevisedApplicationMade());
			cs.setTelevisedAppRefusedFreetext(caseBasicValue.getTelevisedAppRefusedFreetext());
			cs.setTelevisedRemarksFilmed(caseBasicValue.getTelevisedRemarksFilmed());
			
			return cs;
			
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	/**
	 * The following attributes can be updated by Xhibit on this entity:
	 * crestSeveredInd estPDHTrialLength noProsWitness noPageProsEvidence
	 * lengthTape judgeReasonForAppeal indChangeStatus - theses fields will NOT
	 * be updated in CREST indictmentInformation1 indictmentInformation2
	 * indictmentInformation3 indictmentInformation4 indictmentInformation5
	 * indictmentInformation6 RESULTS_VERIFIED - these fields will be updated in
	 * CREST when the indChangeStatus field is set to ??? All other attribute
	 * updates must go via Mercator and the IntegrationFacade.
	 * 
	 * @param value
	 */
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseBasicValue))
			throw new IllegalArgumentException(UNEXPECTED_TYPE + value.getClass());
		try {
			CaseBasicValue caseBasicValue = (CaseBasicValue) value;

			Case cs = home.findByPrimaryKey(value.getId());

			// check version
			if (cs.getVersion() == null || value.getVersion() == null || !cs.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException(OPTIMISTIC_LOCK);
			}
			cs.setCrestSeveredInd(caseBasicValue.getCrestSeveredInd());
			cs.setEstPDHTrialLength(caseBasicValue.getEstPDHTrialLength());
			cs.setNoProsWitness(caseBasicValue.getNoProsWitness());
			cs.setNoPageProsEvidence(caseBasicValue.getNoPageProsEvidence());
			cs.setLengthTape(caseBasicValue.getLengthTape());
			cs.setJudgeReasonForAppeal(caseBasicValue.getJudgeReasonForAppeal());
			cs.setIndictmentInfo1(caseBasicValue.getIndictmentInfo1());
			cs.setIndictmentInfo2(caseBasicValue.getIndictmentInfo2());
			cs.setIndictmentInfo3(caseBasicValue.getIndictmentInfo3());
			cs.setIndictmentInfo4(caseBasicValue.getIndictmentInfo4());
			cs.setIndictmentInfo5(caseBasicValue.getIndictmentInfo5());
			cs.setIndictmentInfo6(caseBasicValue.getIndictmentInfo6());
			cs.setIndChangeStatus(caseBasicValue.getIndChangeStatus());
			cs.setExportCharges(caseBasicValue.getExportCharges());
			
			// Only update the class code for non-trial cases or for trial cases providing the new value is not blank/null
			if ( !"T".equals(caseBasicValue.getCaseType()) ||
				("T".equals(caseBasicValue.getCaseType()) && null != caseBasicValue.getClassCode())) {
				log.debug("update - Set Class Code to: " + caseBasicValue.getClassCode());
				cs.setClassCode(caseBasicValue.getClassCode());
			}

			cs.setOffenceGroup(caseBasicValue.getOffenceGroupCode());
			cs.setResultsVerified(caseBasicValue.getResultsVerified());
			// CCN1236- KD
			cs.setVulnerableVictimIndicator(caseBasicValue.getVulnerableVictimIndicator());
			cs.setPublicDisplayHide(caseBasicValue.getPublicDisplayHide());
			
			cs.setS28Eligible(caseBasicValue.getS28Eligible());
			cs.setS28OrderMade(caseBasicValue.getS28OrderMade());
			
			cs.setCivilUnrest(caseBasicValue.getCivilUnrest());
			
			cs.setTelevisedAppGranted(caseBasicValue.getTelevisedAppGranted());
			cs.setTelevisedApplicationMade(caseBasicValue.getTelevisedApplicationMade());
			cs.setTelevisedAppMadeDate(caseBasicValue.getTelevisedAppMadeDate());
			cs.setTelevisedAppRefusedFreetext(caseBasicValue.getTelevisedAppRefusedFreetext());
			cs.setTelevisedRemarksFilmed(caseBasicValue.getTelevisedRemarksFilmed());
			
			cs.setUpdated(userDisplayName);
			if(caseBasicValue.getCaseListed()!=null && !caseBasicValue.getCaseListed().equals("")) {
        cs.setCaseListed(caseBasicValue.getCaseListed());
      }

			// CCN2867 - New Indictment Log table
			Collection indictmentLog = XhbIndictmentLogBeanHelper2.findNonObsoleteByCaseId(cs.getCaseId());
			int noOfExistingRows = indictmentLog.size();
			Collection<IndictmentLogValue> updatedIndictmentLog = caseBasicValue.getIndictmentLog();
			int noOfNewRows = updatedIndictmentLog.size();

			Iterator iter = indictmentLog.iterator();
			Iterator<IndictmentLogValue> iterNew = updatedIndictmentLog.iterator();
			if (noOfExistingRows == noOfNewRows) {
				// Update each existing row
				for (int i = 1; i <= noOfNewRows; i++) {
					XhbIndictmentLog iL = (XhbIndictmentLog) iter.next();
					IndictmentLogValue iLV = iterNew.next();
					iL.setIndictmentInfo(iLV.getIndictmentInfo());
					iL.setObsInd("N");
				}
			} else if (noOfExistingRows > noOfNewRows) {
				// update exisitng rows where data still exists
				for (int i = 1; i <= noOfNewRows; i++) {
					XhbIndictmentLog iL = (XhbIndictmentLog) iter.next();
					IndictmentLogValue iLV = iterNew.next();
					iL.setIndictmentInfo(iLV.getIndictmentInfo());
					iL.setObsInd("N");
				}
				// now delete no longer used rows
				while (iter.hasNext()) {
					XhbIndictmentLog iL = (XhbIndictmentLog) iter.next();
					iL.setObsInd("Y");
				}
			} else if (noOfExistingRows < noOfNewRows) {
				// update existing rows
				for (int i = 1; i <= noOfExistingRows; i++) {
					XhbIndictmentLog iL = (XhbIndictmentLog) iter.next();
					IndictmentLogValue iLV = iterNew.next();
					iL.setIndictmentInfo(iLV.getIndictmentInfo());
					iL.setObsInd("N");
				}
				// add new rows
				for (int i = noOfExistingRows; i < noOfNewRows; i++) {
					IndictmentLogValue iLV = iterNew.next();
					XhbIndictmentLogBasicValue logBasicValue = new XhbIndictmentLogBasicValue();
					logBasicValue.setCaseId(iLV.getCaseID());
					logBasicValue.setSequenceNo(iLV.getSequenceNo());
					logBasicValue.setIndictmentInfo(iLV.getIndictmentInfo());
					XhbIndictmentLogBeanHelper2.create(logBasicValue);
				}
			}
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}
	 

	public void updateListing(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseBasicValue))
			throw new IllegalArgumentException(UNEXPECTED_TYPE + value.getClass());
		try {
			CaseBasicValue caseBasicValue = (CaseBasicValue) value;

			Case cs = home.findByPrimaryKey(value.getId());

			// check version
			if (cs.getVersion() == null || value.getVersion() == null || !cs.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException(OPTIMISTIC_LOCK);
			}

			cs.setSecureCourt(caseBasicValue.getSecureCourt());
			cs.setVideoLinkRequired(caseBasicValue.getVideoLinkRequired());
			cs.setDefaultHearingType(caseBasicValue.getDefaultHearingType());
			cs.setSection28Name1(caseBasicValue.getSection28Name1());
			cs.setSection28Name2(caseBasicValue.getSection28Name2());
			cs.setSection28Phone1(caseBasicValue.getSection28Phone1());
			cs.setSection28Phone2(caseBasicValue.getSection28Phone2());
			cs.setS28Eligible(caseBasicValue.getS28Eligible());
			cs.setS28OrderMade(caseBasicValue.getS28OrderMade());
			cs.setUpdated(userDisplayName);
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}

	private void setCaseBasicValue(CaseBasicValue value, Case local) {
		value.setCaseNumber(local.getCaseNumber());
		value.setCaseId(local.getCaseId());
		value.setCaseType(local.getCaseType());
		value.setMagConvictionDate(convertToCalendar(local.getMagConvictionDate()));
		value.setCaseSubType(local.getCaseSubType());
		value.setCaseTitle(local.getCaseTitle());
		value.setCaseDescription(local.getCaseDescription());
		value.setLinkedCaseID(local.getLinkedCaseId());
		value.setRefCourtID(local.getRefCourtId());
		value.setBailMagCode(local.getBailMagCode());
		value.setChargeImportIndicator(local.getChargeImportIndicator());
		value.setDateIndRec(convertToCalendar(local.getDateIndRec()));
		value.setCrestIndictResp(local.getCrestIndictResp());
		value.setCrestSeveredInd(local.getCrestSeveredInd());
		value.setCaseClass(local.getCaseClass());
		value.setEstPDHTrialLength(local.getEstPDHTrialLength());
		value.setNoProsWitness(local.getNoProsWitness());
		value.setNoPageProsEvidence(local.getNoPageProsEvidence());
		value.setLengthTape(local.getLengthTape());
		value.setJudgeReasonForAppeal(local.getJudgeReasonForAppeal());
		value.setResultsVerified(local.getResultsVerified());
		value.setCourtID(local.getCourtId());
		value.setProsAgencyRef(local.getProsAgencyRef());
		value.setIndictmentInfo1(local.getIndictmentInfo1());
		value.setIndictmentInfo2(local.getIndictmentInfo2());
		value.setIndictmentInfo3(local.getIndictmentInfo3());
		value.setIndictmentInfo4(local.getIndictmentInfo4());
		value.setIndictmentInfo5(local.getIndictmentInfo5());
		value.setIndictmentInfo6(local.getIndictmentInfo6());
		value.setPoliceOfficerAttending(local.getPoliceOfficerAttending());
		value.setCpsCaseWorker(local.getCpsCaseWorker());
		value.setExportCharges(local.getExportCharges());
		value.setIndChangeStatus(local.getIndChangeStatus());
		value.setClassCode(local.getClassCode());
		value.setOffenceGroupCode(local.getOffenceGroup());
		value.setReceiptType(local.getReceiptType());
		value.setVulnerableVictimIndicator(local.getVulnerableVictimIndicator());
		value.setPublicDisplayHide(local.getPublicDisplayHide());
		// Added extra fields for View Case Details
		value.setCommittalDate(local.getCommittalDate());
		value.setAppealLodgedDate(local.getAppealLodgedDate());
		value.setSentForTrialDate(local.getSentForTrialDate());
		value.setCaseStatus(local.getCaseStatus());
		value.setCaseListed(local.getCaseListed());
		value.setPubRunningListId(local.getPubRunningListId());
		// RFC2867 - New XHB_Indictment_Log table
		Integer caseID = local.getCaseId();
		XhbIndictmentLogBasicValue[] indictmentLog = XhbIndictmentLogBeanHelper2.findNonObsoleteByCaseIdValue(caseID);
		int noOfLogs = indictmentLog.length;
		Collection<IndictmentLogValue> iLogValues = new ArrayList<IndictmentLogValue>();

		for (int i = 0; i < noOfLogs; i++) {
			IndictmentLogValue iLV = new IndictmentLogValue();
			iLV.setCaseID(indictmentLog[i].getCaseId());
			iLV.setSequenceNo(indictmentLog[i].getSequenceNo());
			iLV.setIndictmentInfo(indictmentLog[i].getIndictmentInfo());
			iLogValues.add(iLV);
		}
		value.setIndictmentLog(iLogValues);

		// new fields after case create/maintenance
		if (local.getAppealLodgedDate() != null) {
			value.setAppealLodgedDate(local.getAppealLodgedDate());
		}
		value.setMonitoringCategoryId(local.getMonitoringCategoryId());
		if (local.getReceivedDate() != null) {
			value.setReceivedDate(local.getReceivedDate());
		}
		if (local.getSentForTrialDate() != null) {
			value.setSentForTrialDate(local.getSentForTrialDate());
		}
		value.setTicketRequired(local.getTicketRequired());
		value.setTicketTypeCode(local.getTicketTypeCode());
		if (local.getCommittalDate() != null) {
			value.setCommittalDate(local.getCommittalDate());
		}
		value.setEitherWayType(local.getEitherWayType());
		value.setNoDefendantsForCase(local.getNoDefendantsForCase());
		value.setTransferredCase(local.getTransferredCase());
		value.setTransferDeferredSentence(local.getTransferDeferredSentence());
		if (local.getDateTransFrom() != null) {
			value.setDateTransFrom(local.getDateTransFrom());
		}
		if (local.getDateTransTo() != null) {
			value.setDateTransTo(local.getDateTransTo());
		}
		value.setOriginalCaseNumber(local.getOriginalCaseNumber());
		value.setRetrial(local.getRetrial());
		value.setSecureCourt(local.getSecureCourt());
		value.setVideoLinkRequired(local.getVideoLinkRequired());
		if (local.getPreliminaryDateOfHearing() != null) {
			value.setPreliminaryDateOfHearing(local.getPreliminaryDateOfHearing());
		}
		value.setMagistratesCaseRef(local.getMagistratesCaseRef());
		value.setMagCourtHearingTypeRefId(local.getMagCourtHearingTypeRefId());
		value.setOriginalJps1(local.getOriginalJps1());
		value.setOriginalJps2(local.getOriginalJps2());
		value.setOriginalJps3(local.getOriginalJps3());
		value.setOriginalJps4(local.getOriginalJps4());
		value.setPoliceForceCode(local.getPoliceForceCode());
		value.setCccTransFromRefCourtId(local.getCccTransFromRefCourtId());
		value.setCccTransToRefCourtId(local.getCccTransToRefCourtId());
		value.setCourtIdReceivingSite(local.getCourtIdReceivingSite());
		if (local.getOrigBodyDecisionDate() != null) {
			value.setOrigBodyDecisionDate(local.getOrigBodyDecisionDate());
		}
		if (local.getLcSentDate() != null) {
			value.setLcSentDate(local.getLcSentDate());
		}
		value.setCrackedIneffectiveId(local.getCrackedIneffectiveId());
		value.setDefaultHearingType(local.getDefaultHearingType());
		value.setSection28Name1(local.getSection28Name1());
		value.setSection28Name2(local.getSection28Name2());
		value.setSection28Phone1(local.getSection28Phone1());
		value.setSection28Phone2(local.getSection28Phone2());
		value.setS28Eligible(local.getS28Eligible());
		value.setS28OrderMade(local.getS28OrderMade());
		value.setCaseGroupNumber(local.getCaseGroupNumber());
		value.setDateTransRecordedTo(local.getDateTransRecordedTo());
		value.setCivilUnrest(local.getCivilUnrest());
		value.setTelevisedAppGranted(local.getTelevisedAppGranted());
		value.setTelevisedAppMadeDate(local.getTelevisedAppMadeDate());
		value.setTelevisedApplicationMade(local.getTelevisedApplicationMade());
		value.setTelevisedAppRefusedFreetext(local.getTelevisedAppRefusedFreetext());
		value.setTelevisedRemarksFilmed(local.getTelevisedRemarksFilmed());

		// DARTS
		value.setDarRetentionPolicyId(local.getDarRetentionPolicyId());
		value.setCrpLastUpdateDate(local.getCrpLastUpdateDate());
		value.setCreationDate(local.getCreationDate());
	}

	/**
	 * Case creation process
	 * 
	 * @param value
	 *            return case id because this is needed for ref prosecutor
	 *            agency
	 */
	public Case createCase(CSAbstractValue value, String username) {
		CaseBasicValue val = (CaseBasicValue) value;
		log.debug("about to create case for " + val.getCaseType() + "" + val.getCaseNumber());
		try {
			Case caseCreated = home.create(val.getCaseNumber(), val.getCaseType(), val.getCaseTitle(),
					val.getCaseSubType(), val.getClassCode(), val.getCourtID(), val.getMonitoringCategoryId(),
					val.getAppealLodgedDate(), val.getReceivedDate(), val.getSentForTrialDate(),
					val.getTicketRequired(), val.getTicketTypeCode(), val.getCourtIdReceivingSite(),
					val.getCommittalDate(), val.getReceiptType(), val.getEitherWayType(), val.getNoDefendantsForCase(),
					val.getTransferredCase(), val.getTransferDeferredSentence(), val.getDateTransFrom(),
					val.getDateTransTo(), val.getOriginalCaseNumber(), val.getCccTransFromRefCourtId(),
					val.getRetrial(), val.getSecureCourt(), val.getPreliminaryDateOfHearing(), val.getRefCourtID(),
					val.getMagistratesCaseRef(), val.getMagCourtHearingTypeRefId(), val.getOriginalJps1(),
					val.getOriginalJps2(), val.getOriginalJps3(), val.getOriginalJps4(), val.getCaseDescription(),
					val.getCaseListed(), val.getPoliceForceCode(), val.getNoPageProsEvidence(),
					val.getOrigBodyDecisionDate(), val.getChargeImportIndicator(),
					convertToTimestamp(val.getMagConvictionDate()), val.getLcSentDate(), val.getVideoLinkRequired(),
					val.getDefaultHearingType(), val.getCrackedIneffectiveId(), val.getSection28Name1(),
					val.getSection28Name2(), val.getSection28Phone1(), val.getSection28Phone2(), val.getS28Eligible(), 
					val.getS28OrderMade(), username, val.getCaseGroupNumber(), val.getCivilUnrest(), val.getTelevisedAppGranted(), 
					val.getTelevisedApplicationMade(), val.getTelevisedAppMadeDate(), val.getTelevisedAppRefusedFreetext(), 
					val.getTelevisedRemarksFilmed(),
					val.getDarRetentionPolicyId(), val.getCrpLastUpdateDate());
			log.debug("Created case for " + val.getCaseType() + "" + val.getCaseNumber() + " successfully");
			return caseCreated;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);

		}

	}
	
	/**
	 * Updating last updated by which in turn will update last update date,
	 * Needed to fix issue discovered in unauth case report
	 * @param caseId Integer
	 * @param updatedBy User who opened the case
	 * @throws ObjectNotFoundException
	 */
	public void updateLastUpdate(Integer caseId, String lastUpdatedBy) throws ObjectNotFoundException {
		try {
			Case cs = home.findByPrimaryKey(caseId);
			cs.setUpdated(lastUpdatedBy);
			cs.setLastUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}
	
	/**
	 * Update Case Retention Policy
	 * @param caseId Integer
	 * @param updatedBy User who opened the case
	 * @throws ObjectNotFoundException
	 */
	public void updateCRP(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseBasicValue))
			throw new IllegalArgumentException(UNEXPECTED_TYPE + value.getClass());
		try {
			CaseBasicValue caseBasicValue = (CaseBasicValue) value;

			Case local = home.findByPrimaryKey(value.getId());

			// Check version
			if (local.getVersion() == null || value.getVersion() == null || 
					!local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException(OPTIMISTIC_LOCK);
			}

			if (userDisplayName != null) {
				local.setUpdated(userDisplayName);
			}
			local.setDarRetentionPolicyId(caseBasicValue.getDarRetentionPolicyId());
			local.setCrpLastUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}
}
