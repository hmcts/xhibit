package uk.gov.courtservice.xhibit.business.services.defendant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import com.bea.common.security.jdkutils.WeaverUtil.Collections;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.originalcharges.OriginalChargesDatabaseManager;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReference;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hatesentencing.HateSentencing;
import uk.gov.courtservice.xhibit.business.entities.hatesentencing.HateSentencingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.orders.AggravatingReasons;
import uk.gov.courtservice.xhibit.business.entities.orders.AggravatingReasonsMaintainer;
import uk.gov.courtservice.xhibit.business.entities.orders.RefAggravatingReasons;
import uk.gov.courtservice.xhibit.business.entities.orders.RefAggravatingReasonsMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.RefCourtHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.AggravatingReasonsComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAggravatingReasonsBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.SchedHearingLocationValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;

/**
 * <p>
 * Title: DefendantOnCaseHelper
 * </p>
 * <p>
 * Description: This helper class is used to construct the three necessary value
 * object and aggregate into a client specific VO.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 *          <p/>
 *          <Change History/>
 *          <p/>
 *          <P>
 *          03/05/03 - MH - Changed the updateDefentOnCaseDetails since the
 *          update of driver and cro never worked. The update could not be done
 *          since the id was not passed in. Had to create a new basic value from
 *          the entity found and use the new basic value for the update.
 *          </P>
 */
public class DefendantOnCaseHelper {
	private static final String DEF_NOT_FOUND = "defendantcontroller.defnotfound";

	private DefendantOnCaseMaintainer defOnCaseMaintainer;

	private DefendantMaintainer defMaintainer;

	private DefendantReferenceMaintainer defRefMaintainer;

	private CourtSiteMaintainer courtSiteMaintainer;

	private HateSentencingMaintainer hateSentencingMaintainer;
	
	private AggravatingReasonsMaintainer aggravatingReasonsMaintainer;
	
	private RefAggravatingReasonsMaintainer refAggravatingReasonsMaintainer;

	private static final Logger log = CSServices.getLogger(DefendantOnCaseHelper.class);

	private CaseControllerLocal caseController;

	public DefendantOnCaseHelper() {
		defOnCaseMaintainer = new DefendantOnCaseMaintainer();
		defRefMaintainer = new DefendantReferenceMaintainer();
		defMaintainer = new DefendantMaintainer();
		courtSiteMaintainer = new CourtSiteMaintainer();
		hateSentencingMaintainer = new HateSentencingMaintainer();
		aggravatingReasonsMaintainer = new AggravatingReasonsMaintainer();
		refAggravatingReasonsMaintainer = new RefAggravatingReasonsMaintainer();
		caseController = (CaseControllerLocal) CSServices.getEJBServices()
				.createLocalSession(CaseControllerLocalHome.class);
	}

	/**
	 * This method allows the retrieval of defendant on case information. It was
	 * written for use specifically with defendantId.
	 * 
	 * @param defendantId
	 *            the current defendant ID
	 * @return Collection of defendants on case
	 */
	public Collection findByDefendantId(Integer defendantId) throws DefendantControllerException {
		log.debug("*** findByDefendantId(" + defendantId + ") entered ***");
		Collection<DefendantOnCaseBasicValue> defOnCaseBasicVals = new ArrayList<DefendantOnCaseBasicValue>();
		// retrieve the values from DefendantOnCase entity
		try {
			Collection defsOnCase = defOnCaseMaintainer.findByDefendantId(defendantId);
			log.debug("*** found DefendantOnCase entity, creating new value object ***");
			Iterator iter = defsOnCase.iterator();
			while (iter.hasNext()) {
				DefendantOnCase defOnCase = (DefendantOnCase) iter.next();
				DefendantOnCaseBasicValue val = getDefOnCaseBasicValue(defOnCase);
				defOnCaseBasicVals.add(val);
			}

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new javax.ejb.EJBException(ex);
		}

		return defOnCaseBasicVals;

	}


	/**
	 * This method populates DefendantOnCaseBasicValue
	 * 
	 * @param defOnCase
	 *            the current defendant on case
	 * @return DefendantOnCaseValue a composite value object
	 */
	private DefendantOnCaseBasicValue getDefOnCaseBasicValue(DefendantOnCase defOnCase) {
		log.debug("Found" + defOnCase.getCaseId());
 
		// Populate the DefendantValue object
		 DefendantOnCaseBasicValue defOnCaseBasicValue = new DefendantOnCaseBasicValue(defOnCase.getCaseId(),
				defOnCase.getDefendantId(), defOnCase.getAsn(), defOnCase.getCurrentBcStatus(),
				defOnCase.getIsJuvenile(), defOnCase.getDrivingDisqSuspendedDate(), defOnCase.getPncId(),
				defOnCase.getPtiurn(), defOnCase.getIsMasked(), defOnCase.getMaskedName(), defOnCase.getHateIndicator(),
				defOnCase.getHateType(), defOnCase.getMagCourtFirstHearingDate(), defOnCase.getMagCourtFinalHearingDate(),
				defOnCase.getNationality());
		if (defOnCase.getDefendantOnCaseId() != null && defOnCaseBasicValue.getDefendantOnCaseId() == null) {
			defOnCaseBasicValue.setDefendantOnCaseId(defOnCase.getDefendantOnCaseId());
			defOnCaseBasicValue.setId(defOnCase.getDefendantOnCaseId());
		}
		if (defOnCase.getCurrentBcStatus() != null) {
			defOnCaseBasicValue.setCurrentBcStatus(defOnCase.getCurrentBcStatus());

		}
		defOnCaseBasicValue.setVersion(defOnCase.getVersion());
		defOnCaseBasicValue.setDifferenceReport(defOnCase.getDifferenceReport());
		defOnCaseBasicValue.setDefendantNumber(defOnCase.getDefendantNumber());
		if (defOnCase.getAmendedDateExported() != null) {
			Date date = new Date(defOnCase.getAmendedDateExported().getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			defOnCaseBasicValue.setAmendedDateExported(cal);
		}
		defOnCaseBasicValue.setAmendedReason(defOnCase.getAmendedReason());
		defOnCaseBasicValue.setBcStatusBwExecuted(defOnCase.getBcStatusBwExecuted());
		defOnCaseBasicValue.setBenchWarrantExecDate(defOnCase.getBenchWarrantExecDate());
		defOnCaseBasicValue.setCacdAppealResult(defOnCase.getCacdAppealResult());
		defOnCaseBasicValue.setCacdAppealResultDate(defOnCase.getCacdAppealResultDate());
		defOnCaseBasicValue.setCoaStatus(defOnCase.getCoaStatus());
		defOnCaseBasicValue.setCollectMagistrateCourtId(defOnCase.getCollectMagistrateCourtId());
		defOnCaseBasicValue.setCommBcStatus(defOnCase.getCommBcStatus());
		defOnCaseBasicValue.setCustodial(defOnCase.getCustodial());
		defOnCaseBasicValue.setCustodyTimeLimit(defOnCase.getCustodyTimeLimit());
		if (defOnCase.getDateExported() != null) {
			Date date = new Date(defOnCase.getDateExported().getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			defOnCaseBasicValue.setDateExported(cal);
		}
		if (defOnCase.getDateOfCommittal() != null) {
			Date date = new Date(defOnCase.getDateOfCommittal().getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			defOnCaseBasicValue.setDateOfCommittal(cal);
		}
		defOnCaseBasicValue.setDateReceiptNoticeAppeal(defOnCase.getDateReceiptNoticeAppeal());
		defOnCaseBasicValue.setFinalDrivingLicenceStatus(defOnCase.getFinalDrivingLicenceStatus());
		defOnCaseBasicValue.setFormNgSentDate(defOnCase.getFormNgSentDate());

		defOnCaseBasicValue.setSeriousDrugOffence(defOnCase.getSeriousDrugOffence());
		defOnCaseBasicValue.setPublicDisplayHide(defOnCase.getPublicDisplayHide());
		defOnCaseBasicValue.setHateIndicator(defOnCase.getHateIndicator());
		defOnCaseBasicValue.setSuspended(defOnCase.getSuspended());
		defOnCaseBasicValue.setResultsVerified(defOnCase.getResultsVerified());
		defOnCaseBasicValue.setNoOfTICs(defOnCase.getNoOfTics());
		defOnCaseBasicValue.setRecommendedDeportation(defOnCase.getRecommendedDeportation());
		defOnCaseBasicValue.setHateSentIndicator(defOnCase.getHateSentIndicator());
		defOnCaseBasicValue.setCtlApplies(defOnCase.getCtlApplies());
		return defOnCaseBasicValue;
	}
	 
	/**
	 * Return all defendants for a given case ID
	 * 
	 * @param caseId
	 *            the current case ID
	 * @return Collection of defendant
	 */
	public Collection findByCaseId(Integer caseId) {
		try {
			Collection defendantCollection = defOnCaseMaintainer.findByCaseId(caseId);
			ArrayList<DefendantOnCaseBasicValue> defendants = new ArrayList<DefendantOnCaseBasicValue>();
			if (defendants != null) {
				Iterator itr = defendantCollection.iterator();
				while (itr.hasNext()) {
					DefendantOnCase defendantOnCase = (DefendantOnCase) itr.next();
					defendants.add(defOnCaseMaintainer.getDefendantOnCaseBasicValue(defendantOnCase));
				}
			}
			return defendants;
		} catch (Exception ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new javax.ejb.EJBException(ex);
		}
	}

	/**
	 * This method allows the retrieval of defendant on case information. It was
	 * written for use specifically with noOfTics, finalDrivingLicienceStatus,
	 * collectMagistratesCourtId, driverNumber and CRONumber.
	 * 
	 * @param defendantId
	 *            the current defendant ID
	 * @param caseId
	 *            the current case ID
	 * @return DefendantOnCaseValue a composite value object
	 */
	public DefendantOnCaseValue getDefendantOnCaseDetails(Integer defendantId, Integer caseId)
			throws DefendantControllerException {

		log.debug("*** getDefendantOnCaseDetails(" + defendantId + ", " + caseId + ") entered ***");

		DefendantOnCaseValue defOnCaseVO = new DefendantOnCaseValue();
		DefendantOnCaseBasicValue defOnCaseBVO = null;
		DefendantReferenceBasicValue defRefDriverNumberBVO = null;
		DefendantReferenceBasicValue defRefCRONumberBVO = null;
		DefendantReferenceBasicValue defRefLicenceTypeBVO = null;
		DefendantReferenceBasicValue defRefIssueNumberBVO = null;
		
		// retrieve the values from DefendantOnCase entity
		try {
			if (!(defOnCaseMaintainer.findByDefendantAndCase(defendantId, caseId) == null)) {
				DefendantOnCase defOnCase = defOnCaseMaintainer.findByDefendantAndCase(defendantId, caseId);
				if (!(defOnCase == null)) {
					log.debug("*** found DefendantOnCase entity, creating new value object ***");
					// get a BVO from the maintainer, by passing ref to local
					// entity
					defOnCase = defOnCaseMaintainer.findByPrimaryKey(defOnCase.getDefendantOnCaseId());
					defOnCaseBVO = defOnCaseMaintainer.getDefendantOnCaseBasicValue(defOnCase);
				} else {
					return null;
				}
			} else
				return null;

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new javax.ejb.EJBException(ex);
		}

		// retrieve the values from DefendantReference for driver number

		try {
			log.debug("*** locating DefendantReference[DRIVER_NUBMER] entity using defID:" + defendantId + " ***");
			DefendantReference defRefDriverNumber = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId,
					DefendantReferenceProperties.DRIVER_NUMBER);
			log.debug("*** found DefendantReference[DRIVER_NUBMER] entity, creating new value object ***");

			// get a BVO from the maintainer, by passing ref to local entity
			defRefDriverNumberBVO = defRefMaintainer.getDefendantReferenceBasicValue(defRefDriverNumber);

		} catch (ObjectNotFoundException ex) {
			// this can be expected and we have already set up the default
			// object in the VO
			defRefDriverNumberBVO = null;
		}

		// retrieve the values from DefendantReference for cro number

		try {
			log.debug("*** locating DefendantReference[CRO_NUMBER] entity using defID:" + defendantId + " ***");
			DefendantReference defRefCRONumber = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId,
					DefendantReferenceProperties.CRO_NUMBER);
			log.debug("*** found DefendantReference[CRO_NUMBER] entity, creating new value object ***");

			// get a BVO from the maintainer, by passing ref to local entity
			defRefCRONumberBVO = defRefMaintainer.getDefendantReferenceBasicValue(defRefCRONumber);

		} catch (ObjectNotFoundException ex) {

			// this can be expected and we have already set up the default
			// object in the VO
			defRefCRONumberBVO = null;
		}

		//	Retrieve the value for Licence Type
		try{
			log.debug("*** locating DefendantReference[LICENCE_TYPE] entity using defID: " + defendantId + " ***");
			DefendantReference defRefLicenceType = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId,
					DefendantReferenceProperties.LICENCE_TYPE);
			log.debug("*** found DefendantReference[LICENCE_TYPE] entity, creating new value object ***");
			
			//	Get a BVO from the maintainer, by passing ref to local entity
			defRefLicenceTypeBVO = defRefMaintainer.getDefendantReferenceBasicValue(defRefLicenceType);
		} catch( ObjectNotFoundException ex){
			// this can be expected and we have already set up the default
			// object in the VO
			defRefLicenceTypeBVO = null;
		}
		
		//	Retrieve the value for the Licence issue Number
		try {
			log.debug("*** locationg DefendantReference(LICENCE_ISSUE_NUMBRE) entity using defID: " + defendantId + " ***");
			DefendantReference defRefIssueNumber = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId, 
					DefendantReferenceProperties.LICENCE_ISSUE_NUMBER);
			log.debug("*** Found DefendantReference(LICENCE_ISSUE_NUMBER) entity, creating new value object ***");
			
			//	Get a BVO from teh maintainer, by passing ref to local entity
			defRefIssueNumberBVO = defRefMaintainer.getDefendantReferenceBasicValue(defRefIssueNumber);			
		} catch (ObjectNotFoundException ex) {
			defRefIssueNumberBVO = null;
		}
		
		try {
			log.debug("*** locating HateSentencing entity using defOnCaseID:" + defOnCaseBVO.getId() + " ***");
			Collection hateSentencingCollection = hateSentencingMaintainer
					.findNonObsoleteByDefendantOnCaseId(defOnCaseBVO.getId());
			log.debug("*** found HateSentencing entity, creating new value object ***");

			// Now convert the Collection into entries taht are meaningful
			Iterator it = hateSentencingCollection.iterator();
			while (it.hasNext()) {
				HateSentencing thisHateSentencingRow = (HateSentencing) it.next();
				// if (!thisHateSentencingRow.getObsInd().equals("Y")) {
				log.debug("Will process hate sentencing row");
				int rhst = thisHateSentencingRow.getRefHateSentTypeId();
				switch (rhst) {
				case 1:
					defOnCaseVO.setGeneralDisability(true);
					break;
				case 2:
					defOnCaseVO.setVictimDisability(true);
					break;
				case 3:
					defOnCaseVO.setRacialAggravated(true);
					break;
				case 4:
					defOnCaseVO.setRaceAndReligionAggravated(true);
					break;
				case 5:
					defOnCaseVO.setReligionAggravated(true);
					break;
				case 6:
					defOnCaseVO.setGeneralSexual(true);
					break;
				case 7:
					defOnCaseVO.setVictimSexual(true);
					break;
				case 8:
					defOnCaseVO.setGeneralTransgender(true);
					break;
				case 9:
					defOnCaseVO.setVictimTransgender(true);
					break;
				default:
					log.debug(
							"RefHateSentencingType Entry is invalid: " + thisHateSentencingRow.getRefHateSentTypeId());
				}
				/*
				 * } else { log.debug("RefHateSentType Entry is obsolete: " +
				 * thisHateSentencingRow.getRefHateSentTypeId()); }
				 */
			}
		} catch (ObjectNotFoundException ex) {

			// this can be expected and we have already set up the default
			// object in the VO
		}

		// Fetch the aggravating reasons and set the defOnCaseVO values
		Collection<AggravatingReasonsComplexValue> aggravatingReasons = getAggravatingReasons(defOnCaseBVO.getId());
		if (aggravatingReasons != null) {
			for (AggravatingReasonsComplexValue aggravatingReason : aggravatingReasons) {
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isAssaultOnWorkers()) {
					defOnCaseVO.setAggravatingAssaultOnWorkers(aggravatingReason.getRefAggravatingReasonsBasicValue().isAssaultOnWorkers());
				}
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isTerroristConnection()) {
					defOnCaseVO.setAggravatingTerroristConnection(aggravatingReason.getRefAggravatingReasonsBasicValue().isTerroristConnection());
				}
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isEmergencyWorkers()) {
					defOnCaseVO.setAggravatingEmergencyWorkers(aggravatingReason.getRefAggravatingReasonsBasicValue().isEmergencyWorkers());
				}
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isHostility()) {
					defOnCaseVO.setAggravatingHostility(aggravatingReason.getRefAggravatingReasonsBasicValue().isHostility());
				}
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isSexualOrientation()) {
					defOnCaseVO.setAggravatingSexualOrientation(aggravatingReason.getRefAggravatingReasonsBasicValue().isSexualOrientation());
				}
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isSexualOrientationOfVictim()) {
					defOnCaseVO.setAggravatingSexualOrientationOfVictim(aggravatingReason.getRefAggravatingReasonsBasicValue().isSexualOrientationOfVictim());
				}
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isTransgender()) {
					defOnCaseVO.setAggravatingTransgender(aggravatingReason.getRefAggravatingReasonsBasicValue().isTransgender());
				}
				if (aggravatingReason.getRefAggravatingReasonsBasicValue().isTransgenderOfVictim()) {
					defOnCaseVO.setAggravatingTransgenderOfVictim(aggravatingReason.getRefAggravatingReasonsBasicValue().isTransgenderOfVictim());
				}
			}
		}
		
		log.debug("*** We now have all the information we need to populate the client specific VO ***");

		defOnCaseVO.setDefendantOnCaseBVO(defOnCaseBVO);
		if (defRefDriverNumberBVO != null) {
			defOnCaseVO.setDriverNumberBVO(defRefDriverNumberBVO);
		}
		if (defRefCRONumberBVO != null) {
			defOnCaseVO.setCRONumberBVO(defRefCRONumberBVO);
		}
		if ( defRefLicenceTypeBVO != null) {
			defOnCaseVO.setLicenceTypeBVO(defRefLicenceTypeBVO);
		}
		if ( defRefIssueNumberBVO != null) {
			defOnCaseVO.setIssueNumberBVO(defRefIssueNumberBVO);
		}
		
		// set the col mag court name
		if (defOnCaseBVO.getCollectMagistrateCourtId() != null) {
			defOnCaseVO.setColMagCourtName(getCollectMagistrateCourtName(defOnCaseBVO.getCollectMagistrateCourtId()));
		}

		log.debug("*** returning from getDefendantOnCaseDetails passing back VO [" + defOnCaseVO + "] ***");

		return defOnCaseVO;

	}
	
	
	
	/**
	 * This method allows the retrieval of defendant on case information. 
	 * 
	 * @param defendantOnCaseId
	 *            the current defendant on case id
	 * @return Collection of defendants on case
	 */
	public DefendantOnCaseBasicValue findByDefendantOnCaseId(Integer defendantOnCaseId) throws DefendantControllerException {
		log.debug("*** findByDefendantId(" + defendantOnCaseId + ") entered ***");
		// retrieve the values from DefendantOnCase entity
		try {
			DefendantOnCase defsOnCase = defOnCaseMaintainer.findByPrimaryKey(defendantOnCaseId);
			DefendantOnCaseBasicValue val = getDefOnCaseBasicValue(defsOnCase);
			return val;

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new javax.ejb.EJBException(ex);
		}

	}
	
	/**
	 * 
	 * @param defendantOnCaseId
	 * @param offenceID
	 * @return
	 */
	public Integer getDefendantOnOffenceID(java.lang.Integer defendantOnCaseId, Integer offenceID)
    {
		XhbDefendantOnOffenceBasicValue[] allDoO = XhbDefendantOnOffenceBeanHelper2
				.findByDefOnCaseIdValue(defendantOnCaseId);
		
		for(int i=0; i < allDoO.length; i++)
		{
			XhbDefendantOnOffenceBasicValue item  =allDoO[i];
			if(item.getOffenceId().equals(offenceID)) 
			{
				if(item.getDefendantOnOffenceId()!=null)
					return item.getDefendantOnOffenceId();
			}
		}
		
		return -1;
    }


	/**
	 * This method allows the updating of defendant on case fields such as
	 * noOfTics, finalDrivingLicienceStatus, collectMagistratesCourtId,
	 * driverNumber and CRONumber.
	 * 
	 * @param defOnCaseVO
	 *            DefendantOnCaseValue containing updated fields
	 */
	public void updateDefendantOnCaseDetails(DefendantOnCaseValue defOnCaseVO, String userDisplayName)
			throws DefendantControllerException {

		log.debug("*** updateDefendantOnCaseDetails(" + defOnCaseVO + ") entered ***");

		// When this value is passed back, we need to check if there are existing records, if there are update
		// else create.

		DefendantOnCaseBasicValue defOnCaseBVO = defOnCaseVO.getDefendantOnCaseBVO();
		DefendantReferenceBasicValue driverNumberDefRefBVO = defOnCaseVO.getDriverNumberBVO();
		DefendantReferenceBasicValue croNumberDefRefBVO = defOnCaseVO.getCRONumberBVO();
		DefendantReferenceBasicValue licenceTypeDefBVO = defOnCaseVO.getLicenceTypeBVO();
		DefendantReferenceBasicValue licenceIssueNumberDefBVO = defOnCaseVO.getIssueNumberBVO();
		
		// find the defendant
		log.debug("*** Finding defedant by defendantId = " + defOnCaseBVO.getDefendantID() + " ***");
		try {
			Defendant tmpDef = defMaintainer.findByPrimaryKey(defOnCaseBVO.getDefendantID());
			log.debug("tmpDef = " + tmpDef);

			//ctx-2098 isMercatorUsedForDOCUpdate() check is now removed.
			// defendantOnCase details
			/*if (isMercatorUsedForDOCUpdate()) {
				log.debug("*** updating DefendantOnCase entity via Mercator ***");
				IntegrationFacade intFacade = IntegrationFacadeFactory.getInstance().getIntegrationFacade();
				intFacade.updateDefendantOnCase(defOnCaseVO);
			} else {*/
				log.debug("*** updating DefendantOnCase entity via Java ***");
				defOnCaseMaintainer.update(defOnCaseBVO, userDisplayName);
			//}

			// driverNumber
			log.debug("*** Checking to see if there is already a record for driver number ***");
			try {

				// try to find the defendant reference with defendantid and the static driver number
				DefendantReference defRefDriverNumber = defRefMaintainer.findByDefendantIdAndReferenceName(
						driverNumberDefRefBVO.getDefendantID(), DefendantReferenceProperties.DRIVER_NUMBER);

				log.debug("*** There is already a driver number so update entity with id : "
						+ defRefDriverNumber.getDefRefId());

				// get the basic value
				DefendantReferenceBasicValue defRefBasicValueDriverForUpdate = defRefMaintainer
						.getDefendantReferenceBasicValue(defRefDriverNumber);

				defRefBasicValueDriverForUpdate.setReferenceValue(driverNumberDefRefBVO.getReferenceValue());

				// update
				defRefMaintainer.update(defRefBasicValueDriverForUpdate, userDisplayName);

			} catch (ObjectNotFoundException ex) {

				log.debug("*** There is no driverNumber record so create one ***");
				defRefMaintainer.create(driverNumberDefRefBVO, tmpDef, userDisplayName);
			}

			// croNumber
			log.debug("*** Checking to see if there is already a record for cro number ***");
			try {
				// try to find the defendant reference with defendantid and the static cro number
				DefendantReference defRefCRONumber = defRefMaintainer.findByDefendantIdAndReferenceName(
						croNumberDefRefBVO.getDefendantID(), DefendantReferenceProperties.CRO_NUMBER);

				// there is a reference so get the basic value
				DefendantReferenceBasicValue defRefBasicValueCroForUpdate = defRefMaintainer
						.getDefendantReferenceBasicValue(defRefCRONumber);

				defRefBasicValueCroForUpdate.setReferenceValue(croNumberDefRefBVO.getReferenceValue());

				log.debug("*** There is already a cro number so update entity ***");
				defRefMaintainer.update(defRefBasicValueCroForUpdate, userDisplayName);

			} catch (ObjectNotFoundException ex) {

				log.debug("*** There is no croNumber record so create one ***");
				defRefMaintainer.create(croNumberDefRefBVO, tmpDef, userDisplayName);
			}

			//	Licence Type:
			try{
				// try to find the defendant reference with defendantid and the static Licence Type
				DefendantReference defRefLicenceType = defRefMaintainer.findByDefendantIdAndReferenceName(
						licenceTypeDefBVO.getDefendantID(), DefendantReferenceProperties.LICENCE_TYPE);

				// there is a reference so get the basic value
				DefendantReferenceBasicValue defRefBasicValueLicenceTypeForUpdate = defRefMaintainer
						.getDefendantReferenceBasicValue(defRefLicenceType);
				

				defRefBasicValueLicenceTypeForUpdate.setReferenceValue(licenceTypeDefBVO.getReferenceValue());

				log.debug("*** There is already a Licence Type so update entity ***");
				defRefMaintainer.update(defRefBasicValueLicenceTypeForUpdate, userDisplayName);				
			} catch (ObjectNotFoundException ex){
				log.debug("*** There is no licence type record, so create one ***");
				defRefMaintainer.create(licenceTypeDefBVO, tmpDef, userDisplayName);
			}
			
			// Licence Issue number:
			try{
				//	Try to find the defendant reference with defendant ID and the static Licece Issue Number
				DefendantReference defRefIssueNumber = defRefMaintainer.findByDefendantIdAndReferenceName(
						licenceIssueNumberDefBVO.getDefendantID(), DefendantReferenceProperties.LICENCE_ISSUE_NUMBER);
				
				//	There is a reference, so get the basic value:
				DefendantReferenceBasicValue defRefBasicvalueLicendeIssueNumberForUpdate = defRefMaintainer.
						getDefendantReferenceBasicValue(defRefIssueNumber);
				
				defRefBasicvalueLicendeIssueNumberForUpdate.setReferenceValue(licenceIssueNumberDefBVO.getReferenceValue());
				
				log.debug("*** There is already a Licence Issue Number, so update entity ***");
				defRefMaintainer.update(defRefBasicvalueLicendeIssueNumberForUpdate, userDisplayName);
			} catch (ObjectNotFoundException ex){
				log.debug("*** There is no Licence Issue Number, so create one ***");
				defRefMaintainer.create(licenceIssueNumberDefBVO, tmpDef, userDisplayName);
			}
			// tell the Public Displays that defendant details have been changed
			// Should this be here anymore - Meeraj?
			// notifyPublicDisplays(defOnCaseVO.getDefendantOnCaseBVO().getCaseID());
			notifyNewPublicDisplays(defOnCaseVO.getDefendantOnCaseBVO().getCaseID(), userDisplayName);

		} catch (ObjectNotFoundException ex) {
			// the defendant passed in could not be found
			CSServices.getDefaultErrorHandler().handleError(ex, DefendantControllerBean.class);
			throw new DefendantControllerException(DEF_NOT_FOUND,
					"Defendant " + defOnCaseVO.getDefendantOnCaseBVO().getDefendantID() + " not found", ex);
		}  
	}

	public DefendantOnCaseVO[] getDefendantsOnCaseByCaseId(Integer caseId) {
		return getDefendantsOnCaseByCaseId(new OriginalChargesDatabaseManager(), caseId);
	}

	public DefendantOnCaseVO[] getDefendantsOnCaseByCaseId(OriginalChargesDatabaseManager originalChargesDBM,
			Integer caseId) {
		return originalChargesDBM.getDefendantsOnCase(caseId);
	}

	private void notifyNewPublicDisplays(Integer caseId, String userDisplayName) throws DefendantControllerException {
		try {
			// See if there are any scheduled hearings for today...
			ScheduledHearingValue[] scheduledHearings = caseController.getScheduledHearingsForCaseOnDay(caseId,
					Calendar.getInstance());
			if (scheduledHearings.length > 0) {
				// Notify...
				SchedHearingLocationValue locationVal = caseController.getTodaysSchedHearingLocation(caseId);
				Integer courtId = courtSiteMaintainer.findByPrimaryKey(locationVal.getCourtSiteID()).getCourtId();
				String courtName = getCourtName(courtId);
				Integer courtRoomNo = getCourtRoomNumber(locationVal);
				DisplayablePublicNoticeValue[] publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(locationVal.getCourtRoomID());
				CourtRoomIdentifier cri = new CourtRoomIdentifier(courtId, locationVal.getCourtRoomID(), courtName, courtRoomNo, publicNotices);
				CaseChangeInformation cci = new CaseChangeInformation(isCaseActive(scheduledHearings));
				UpdateCaseEvent uce = new UpdateCaseEvent(cri, cci);
				PddaHelper notifier = new PddaHelper();
				notifier.sendMessage(uce, userDisplayName);
			}
		} catch (CaseControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
		} catch (ObjectNotFoundException ex) {
			// the defendant passed in could not be found
			CSServices.getDefaultErrorHandler().handleError(ex, DefendantControllerBean.class);
			throw new CSUnrecoverableException("Court site not found.", ex);
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
	}

	private boolean isCaseActive(ScheduledHearingValue[] scheduledHearings) {
		boolean returnValue = false;
		for (int i = 0; i < scheduledHearings.length && !returnValue; i++) {
			returnValue = scheduledHearings[i].getCaseActive().equals("Y");
		}
		return returnValue;
	}

	/**
	 * This will try to find a ref court via the BisRef
	 * 
	 * @param collectMagistrateCourtID
	 *            Integer
	 * @return String the description name of the ref court.
	 * @throws HearingRecordException
	 */
	private String getCollectMagistrateCourtName(Integer collectMagistrateCourtId) throws DefendantControllerException {
		log.debug("DefendantOnCaseHelper.getCollectMagistrateCourtName("
				+ "Integer collectMagistrateCourtId called with id : " + collectMagistrateCourtId);
		try {
			RefCourtCriteria criteria = new RefCourtCriteria();
			criteria.setPrimaryKey(collectMagistrateCourtId);
			RefCourtHelper refCourtHelper = new RefCourtHelper();
			Collection courts = refCourtHelper.findCourts(criteria);
			// test collection has only one element
			if (courts.size() != 1) {
				// unexpected exception
				throw new CSUnrecoverableException("could not find the ref court");
			}
			java.util.Iterator iterator = courts.iterator();
			RefCourtBasicValue refCourtBasicValue = (RefCourtBasicValue) iterator.next();
			String courtName = refCourtBasicValue.getCourtFullName();

			log.debug("DefendantOnCaseHelper.getCollectMagistrateCourtName("
					+ "Integer collectMagistrateCourtId finished");

			return courtName;
		} catch (BisRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new DefendantControllerException(e.getUserMessage(), "Failed to find name for CollectMagistrateCourt",
					e);
		}
	}

	/**
	 * Update the Form A related values
	 * 
	 * @param defendantOnCaseId
	 * @param bcStatus
	 */
	public void updateFormA(Integer defendantOnCaseId, DefendantOnCaseBasicValue defOnCase, String bcStatus, String userDisplayName) 
			throws ObjectNotFoundException {
		defOnCaseMaintainer.updateFormA(defendantOnCaseId, defOnCase, bcStatus, userDisplayName);
	}
	
	


	/**
	 * Get all disposals associated with the defendant on case
	 * 
	 * @param defendantOnCaseId
	 * @param caseId
	 * @return DisposalValue[]
	 */
	public DisposalValue[] getDisposalsForDefendantOnCase(Integer defendantOnCaseId, Integer caseId)
			throws DisposalControllerException {

		ArrayList<DisposalValue> disposalsFound = new ArrayList<DisposalValue>();

		log.debug("*** locating disposals for doc=" + defendantOnCaseId + " and caseId=" + caseId + " ***");
		// Create the disposal values

		// Amended to get all disposals as disposals for defendant on case is
		// not enough - also require those for charge/defendant on offence
		XhbDisposal2BasicValue[] returnedDisposalsCollDoC = XhbDisposal2BeanHelper2.findRSByDefendantOnCaseIdValue(defendantOnCaseId);

		ArrayList listOfDisposals = getDefendantOffenceDisposals(defendantOnCaseId);

		// Now concatenate all the arrays into one
		XhbDisposal2BasicValue[] tempDisposalsList = new XhbDisposal2BasicValue[listOfDisposals.size()];
		tempDisposalsList = (XhbDisposal2BasicValue[]) listOfDisposals.toArray(tempDisposalsList);


		XhbDisposal2BasicValue[] returnedDisposalsColl = concat(returnedDisposalsCollDoC, tempDisposalsList);
		// Carry out duplicate checks only when there is more than one item in
		// each array
		if (returnedDisposalsCollDoC.length > 0 && tempDisposalsList.length > 0)
			returnedDisposalsColl = removeDuplicateDisposals(returnedDisposalsColl);
		for (int i = 0; i < returnedDisposalsColl.length; i++) {
			// Get the disposal
			XhbDisposal2BasicValue thisDisposalRow = returnedDisposalsColl[i];

			// Get the ref disposal
			XhbRefDisposalTypeBasicValue thisRefDisposalType = XhbRefDisposalTypeBeanHelper2
					.findByPrimaryKeyValue(thisDisposalRow.getRefDisposalTypeId());

			// Get the disposal lines
			XhbDisposalLineBasicValue[] disposalLines = XhbDisposalLineBeanHelper2
					.findByDisposal2IdValue(thisDisposalRow.getDisposal2Id());

			// Get the ref disposal lines
			XhbRefDisposalLineBasicValue[] refDisposalLines = new XhbRefDisposalLineBasicValue[disposalLines.length];
			for (int j = 0; j < disposalLines.length; j++) {
				XhbRefDisposalLineBasicValue thisRefDisposalLine = XhbRefDisposalLineBeanHelper2
						.findByPrimaryKeyValue(disposalLines[j].getRefDisposalLineId());
				refDisposalLines[j] = thisRefDisposalLine;
			}

			log.debug("getDisposalsForDefendantOnCase: found a disposal; caseId=" + caseId + "; defOnCaseId="
					+ defendantOnCaseId);
			ArrayList<Integer> crestOffenceSeqNos = new ArrayList<Integer>();
			Integer crestOffenceSequenceNo = new Integer(0); // Default to 0
			// Get the count number for this disposal (crest_offence_seq_no
			// taken from xhb_offence)
			if (thisDisposalRow.getDisposal2Id() != null) {
				log.debug("getDisposalsForDefendantOnCase: found a disposal row; about to get the count no");

				// Get the defendant on offence
				Integer defOnOffId = thisDisposalRow.getDefendantOnOffenceId();
				if (defOnOffId != null) {
					// Get def on offence row to get the offence
					log.debug("getDisposalsForDefendantOnCase: defendant on offence id=" + defOnOffId);
					XhbDefendantOnOffence xdoo = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defOnOffId);
					// Find crest_offence_seq_no for the offence
					if (xdoo != null) {
						log.debug("getDisposalsForDefendantOnCase: got the defendant on offence object");
						XhbOffence xo = XhbOffenceBeanHelper2.findByPrimaryKey(xdoo.getOffenceId());
						if (xo != null) {
							log.debug(
									"getDisposalsForDefendantOnCase: got the offence object - about to check for a crestOffenceSeqNo");
							crestOffenceSequenceNo = xo.getCrestOffenceSeqNo();
							log.debug(
									"getDisposalsForDefendantOnCase: crestOffenceSequenceNo=" + crestOffenceSequenceNo);
							crestOffenceSeqNos.add(crestOffenceSequenceNo);
						}
					}
				}

				// Do the same for defendant on case and union results - ideally
				// there will only be one result!
				Integer defOnCaseId = thisDisposalRow.getDefendantOnCaseId();
				if (defOnCaseId != null) {
					log.debug(
							"getDisposalsForDefendantOnCase: (def on case route) defendant on case id=" + defOnCaseId);
					// Get def on case row to get the case
					XhbDefendantOnCase xdoc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defOnCaseId);
					if (xdoc != null) {
						crestOffenceSequenceNo = getDefOnCaseRow(crestOffenceSeqNos, crestOffenceSequenceNo, xdoc);
					}
				}
			}
			// Loop round the resultset - hopefully only one row returned
			// though! Taking the first result regardless and will warn about
			// the existence of others
			if (crestOffenceSeqNos != null && crestOffenceSeqNos.size() > 0) {
				crestOffenceSequenceNo = getDisposalCrestResults(thisDisposalRow, crestOffenceSeqNos,
						crestOffenceSequenceNo);
			}

			disposalsFound.add(new DisposalValue(thisDisposalRow, thisRefDisposalType, disposalLines, refDisposalLines,
					crestOffenceSequenceNo));
		}
		log.debug("*** found disposals and creating new value object ***");

		return disposalsFound.toArray(new DisposalValue[disposalsFound.size()]);
	}

	/**
	 * @param returnedDisposalsCollDoC
	 * @param tempDisposalsList
	 * @param returnedDisposalsColl
	 * @return
	 */
	private XhbDisposal2BasicValue[] removeDuplicateDisposals(XhbDisposal2BasicValue[] returnedDisposalsColl) {
		
			
			//Remove possibility of duplicates from merging two arrays from different sources together
			ArrayList<XhbDisposal2BasicValue> items = new ArrayList<XhbDisposal2BasicValue>();
			ArrayList<Integer> installedIDs = new ArrayList<Integer>();
			Collections.addAll(items, returnedDisposalsColl);
			
			for(int i=0; i < returnedDisposalsColl.length; i++)
			{
				
				XhbDisposal2BasicValue disposal =returnedDisposalsColl[i];
				if(installedIDs.isEmpty())
				{
					installedIDs.add(disposal.getDisId());
				}
				else if(!installedIDs.contains(disposal.getDisId()))
				{
					installedIDs.add(disposal.getDisId());
				}
				else
				{
					items.remove(i);
				}
			}
			XhbDisposal2BasicValue[] returnedValues = new XhbDisposal2BasicValue[items.size()];
			returnedValues = (XhbDisposal2BasicValue[]) items.toArray(returnedValues);

		
		return returnedValues;
	}
	
	
	/**
	 * Get all disposals associated with the defendant on offence
	 * 
	 * @param defendantOnCaseId
	 * @param caseId
	 * @param deffendantOnOffId 
	 * @return DisposalValue[]
	 */
	public DisposalValue[] getDisposalsForDefendantOnOffence(Integer defendantOnOffId, Integer defendantOnCaseId, Integer caseId)
			throws DisposalControllerException {

		ArrayList<DisposalValue> disposalsFound = new ArrayList<DisposalValue>();

		log.debug("*** locating disposals for doo=" + defendantOnOffId + " and caseId=" + caseId + " ***");
		// Create the disposal values

		// Amended to get all disposals as disposals for defendant on case is
		// not enough - also require those for charge/defendant on offence
		XhbDisposal2BasicValue[] returnedDisposalsCollDoC = XhbDisposal2BeanHelper2.findRSByDefendantOnOffenceIdValue(defendantOnOffId);

		
		ArrayList listOfDisposals = getDefendantOffenceDisposals(defendantOnCaseId);

		// Now concatenate all the arrays into one
		XhbDisposal2BasicValue[] tempDisposalsList = new XhbDisposal2BasicValue[listOfDisposals.size()];
		tempDisposalsList = (XhbDisposal2BasicValue[]) listOfDisposals.toArray(tempDisposalsList);
		
		XhbDisposal2BasicValue[] returnedDisposalsColl = concat(returnedDisposalsCollDoC, tempDisposalsList);
		for (int i = 0; i < returnedDisposalsColl.length; i++) {
			processSingleDisposal(defendantOnCaseId, caseId, disposalsFound, returnedDisposalsColl, i);
		}
		log.debug("*** found disposals and creating new value object ***");

		return disposalsFound.toArray(new DisposalValue[disposalsFound.size()]);
	}

	/**
	 * @param defendantOnCaseId
	 * @param caseId
	 * @param disposalsFound
	 * @param returnedDisposalsColl
	 * @param i
	 */
	private void processSingleDisposal(Integer defendantOnCaseId, Integer caseId,
			ArrayList<DisposalValue> disposalsFound, XhbDisposal2BasicValue[] returnedDisposalsColl, int i) {
		// Get the disposal
		XhbDisposal2BasicValue thisDisposalRow = returnedDisposalsColl[i];

		// Get the ref disposal
		XhbRefDisposalTypeBasicValue thisRefDisposalType = XhbRefDisposalTypeBeanHelper2
				.findByPrimaryKeyValue(thisDisposalRow.getRefDisposalTypeId());

		// Get the disposal lines
		XhbDisposalLineBasicValue[] disposalLines = XhbDisposalLineBeanHelper2
				.findByDisposal2IdValue(thisDisposalRow.getDisposal2Id());

		// Get the ref disposal lines
		XhbRefDisposalLineBasicValue[] refDisposalLines = new XhbRefDisposalLineBasicValue[disposalLines.length];
		for (int j = 0; j < disposalLines.length; j++) {
			XhbRefDisposalLineBasicValue thisRefDisposalLine = XhbRefDisposalLineBeanHelper2
					.findByPrimaryKeyValue(disposalLines[j].getRefDisposalLineId());
			refDisposalLines[j] = thisRefDisposalLine;
		}

		log.debug("getDisposalsForDefendantOnCase: found a disposal; caseId=" + caseId + "; defOnCaseId="
				+ defendantOnCaseId);
		ArrayList<Integer> crestOffenceSeqNos = new ArrayList<Integer>();
		Integer crestOffenceSequenceNo = new Integer(0); // Default to 0
		// Get the count number for this disposal (crest_offence_seq_no
		// taken from xhb_offence)
		if (thisDisposalRow.getDisposal2Id() != null) {
			log.debug("getDisposalsForDefendantOnOffence: found a disposal row; about to get the count no");

			crestOffenceSequenceNo = getDisposalResults(thisDisposalRow, crestOffenceSeqNos,
					crestOffenceSequenceNo);
		}
		// Loop round the resultset - hopefully only one row returned
		// though! Taking the first result regardless and will warn about
		// the existence of others
		if (crestOffenceSeqNos != null && crestOffenceSeqNos.size() > 0) {
			crestOffenceSequenceNo = getDisposalCrestResults(thisDisposalRow, crestOffenceSeqNos,
					crestOffenceSequenceNo);
		}

		disposalsFound.add(new DisposalValue(thisDisposalRow, thisRefDisposalType, disposalLines, refDisposalLines,
				crestOffenceSequenceNo));
	}

	/**
	 * @param thisDisposalRow
	 * @param crestOffenceSeqNos
	 * @param crestOffenceSequenceNo
	 * @return
	 */
	private Integer getDisposalResults(XhbDisposal2BasicValue thisDisposalRow, ArrayList<Integer> crestOffenceSeqNos,
			Integer crestOffenceSequenceNo) {
		// Get the defendant on offence
		Integer defOnOffId = thisDisposalRow.getDefendantOnOffenceId();
		if (defOnOffId != null) {
			crestOffenceSequenceNo = getDefOnOffenceRow(crestOffenceSeqNos, crestOffenceSequenceNo, defOnOffId);
		}

		// Do the same for defendant on case and union results - ideally
		// there will only be one result!
		Integer defOnCaseId = thisDisposalRow.getDefendantOnCaseId();
		if (defOnCaseId != null) {
			log.debug(
					"getDisposalsForDefendantOnOffence: () defendant on case id=" + defOnCaseId);
			// Get def on case row to get the case
			XhbDefendantOnCase xdoc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defOnCaseId);
			if (xdoc != null) {
				crestOffenceSequenceNo = getDefOnCaseRow(crestOffenceSeqNos, crestOffenceSequenceNo, xdoc);
			}
		}
		return crestOffenceSequenceNo;
	}

	/**
	 * @param thisDisposalRow
	 * @param crestOffenceSeqNos
	 * @param crestOffenceSequenceNo
	 * @return
	 */
	private Integer getDisposalCrestResults(XhbDisposal2BasicValue thisDisposalRow,
			ArrayList<Integer> crestOffenceSeqNos, Integer crestOffenceSequenceNo) {
		if (crestOffenceSeqNos.size() > 1) {
			log.warn("More than one result found when finding the offence for a disposal2Id = "
					+ thisDisposalRow.getDisposal2Id());
			// Use the first result anyway
			if (crestOffenceSeqNos.get(0) == null) {
				crestOffenceSequenceNo = 0;
			} else {
				crestOffenceSequenceNo = crestOffenceSeqNos.get(0);
			}
		} else if (crestOffenceSeqNos.size() == 0) {
			log.warn("No results found when finding the offence for a disposal2Id = "
					+ thisDisposalRow.getDisposal2Id());
		} else {
			if (crestOffenceSeqNos.get(0) == null) {
				log.debug(
						"getDisposalsForDefendantOnCase: one and only one crestoffenceseqno found; but value is null");
				crestOffenceSequenceNo = 0;
			} else {
				log.debug("getDisposalsForDefendantOnCase: one and only one crestoffenceseqno found; value is "
						+ crestOffenceSeqNos.get(0));
				crestOffenceSequenceNo = crestOffenceSeqNos.get(0);
			}
		}
		return crestOffenceSequenceNo;
	}

	/**
	 * @param crestOffenceSeqNos
	 * @param crestOffenceSequenceNo
	 * @param xdoc
	 * @return
	 */
	private Integer getDefOnCaseRow(ArrayList<Integer> crestOffenceSeqNos, Integer crestOffenceSequenceNo,
			XhbDefendantOnCase xdoc) {
		log.debug(
				"getDisposalsForDefendantOnCase: (def on case route) got the defendant on case object");
		// Get the case for this def on case
		XhbCase xc = XhbCaseBeanHelper2.findByPrimaryKey(xdoc.getCaseId());
		if (xc != null) {
			log.debug(
					"getDisposalsForDefendantOnCase: (def on case route) got the case object for this defendant on case; caseid="
							+ xdoc.getCaseId());
			// Get the charge
			XhbCharge xch = null;
			try {
				xch = XhbChargeBeanHelper2.findByPrimaryKey(xc.getCaseId());
			} catch (XhbChargeBeanNotFoundException ex) {
				// This is ok!
				log.debug("No charge found for case id: " + xc.getCaseId());
			}
			if (xch != null) {
				log.debug(
						"getDisposalsForDefendantOnCase: (def on case route) got the charge object for the case; chargeid="
								+ xch.getChargeId());
				// Find crest_offence_seq_no for the offence
				XhbOffence xo = null;
				try {
					xo = XhbOffenceBeanHelper2.findByPrimaryKey(xch.getChargeId());
				} catch (XhbOffenceBeanNotFoundException ex) {
					// This is ok!
					log.debug("No offence found for charge id: " + xch.getChargeId());
				}
				if (xo != null) {
					log.debug(
							"getDisposalsForDefendantOnCase: got the offence object (def on case route) - about to check for a crestOffenceSeqNo");
					crestOffenceSequenceNo = xo.getCrestOffenceSeqNo();
					log.debug(
							"getDisposalsForDefendantOnCase: (def on case route) crestOffenceSequenceNo="
									+ crestOffenceSequenceNo);
					crestOffenceSeqNos.add(crestOffenceSequenceNo);
				}
			}
		}
		return crestOffenceSequenceNo;
	}

	/**
	 * @param crestOffenceSeqNos
	 * @param crestOffenceSequenceNo
	 * @param defOnOffId
	 * @return
	 */
	private Integer getDefOnOffenceRow(ArrayList<Integer> crestOffenceSeqNos, Integer crestOffenceSequenceNo,
			Integer defOnOffId) {
		// Get def on offence row to get the offence
		log.debug("getDisposalsForDefendantOnOffence: defendant on offence id=" + defOnOffId);
		XhbDefendantOnOffence xdoo = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defOnOffId);
		// Find crest_offence_seq_no for the offence
		if (xdoo != null) {
			log.debug("getDisposalsForDefendantOnCase: got the defendant on offence object");
			XhbOffence xo = XhbOffenceBeanHelper2.findByPrimaryKey(xdoo.getOffenceId());
			if (xo != null) {
				log.debug(
						"getDisposalsForDefendantOnCase: got the offence object - about to check for a crestOffenceSeqNo");
				crestOffenceSequenceNo = xo.getCrestOffenceSeqNo();
				log.debug(
						"getDisposalsForDefendantOnCase: crestOffenceSequenceNo=" + crestOffenceSequenceNo);
				crestOffenceSeqNos.add(crestOffenceSequenceNo);
			}
		}
		return crestOffenceSequenceNo;
	}

	/**
	 * @param defendantOnCaseId
	 * @return
	 */
	private ArrayList getDefendantOffenceDisposals(Integer defendantOnCaseId) {
		// Also add all disposals for defendant on offence too
		// Get all the defendant on offences and loop round them
		XhbDefendantOnOffenceBasicValue[] allDoO = XhbDefendantOnOffenceBeanHelper2
				.findByDefOnCaseIdValue(defendantOnCaseId);
		ArrayList listOfDisposals = new ArrayList();
		for (int i = 0; i < allDoO.length; i++) {
			if (allDoO[i] != null) {
				Integer thisDoOId = allDoO[i].getDefendantOnOffenceId();
				// for each Disposal2 returned conver to a Disposal2BasicValue
				// object
				ArrayList disposals = (ArrayList) XhbDisposal2BeanHelper2.findRSByDefendantOnOffenceId(thisDoOId);
				for (int j = 0; j < disposals.size(); j++) {
					XhbDisposal2BasicValue thisDisposal = ((XhbDisposal2) disposals.get(j)).getData();
					if (thisDisposal != null) {
						listOfDisposals.add(thisDisposal);
					}
				}
			}
		}
		return listOfDisposals;
	}
	
	

	/**
	 * Concatenate 2 arrays.
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private XhbDisposal2BasicValue[] concat(XhbDisposal2BasicValue[] arr1, XhbDisposal2BasicValue[] arr2) {
		int arr1Len = arr1.length;
		int arr2Len = arr2.length;
		XhbDisposal2BasicValue[] returnArr = new XhbDisposal2BasicValue[arr1Len + arr2Len];
		System.arraycopy(arr1, 0, returnArr, 0, arr1Len);
		System.arraycopy(arr2, 0, returnArr, arr1Len, arr2Len);

		return returnArr;
	}

	private List<AggravatingReasonsComplexValue> getAggravatingReasons(Integer defendantOnCaseId) {
		log.debug("getAggravatingReasons("+defendantOnCaseId+")");
		List<AggravatingReasonsComplexValue> result = new ArrayList<AggravatingReasonsComplexValue>();
		try {
			Collection<AggravatingReasons> aggravatingReasons = aggravatingReasonsMaintainer.findNonObsoleteByDefendantOnCaseId(defendantOnCaseId);
			if (aggravatingReasons != null) {
				for (AggravatingReasons aggravatingReason : aggravatingReasons) {
					AggravatingReasonsComplexValue complexValue = aggravatingReasonsMaintainer.getComplexValue(aggravatingReason);
					RefAggravatingReasonsBasicValue refAggravatingReasonsBasicValue = getRefAggravatingReason(complexValue.getRefAggravatingReasonsId());
					complexValue.setRefAggravatingReasonsBasicValue(refAggravatingReasonsBasicValue);
					result.add(complexValue);
				}
			}
		} catch (FinderException e) {
			log.debug("No aggravatingReasons found");
		}
		return result;
	}
	
	private RefAggravatingReasonsBasicValue getRefAggravatingReason(Integer refAggravatingReasonsId) throws FinderException {
		RefAggravatingReasonsBasicValue basicValue = null;
		if (refAggravatingReasonsId != null) {
			RefAggravatingReasons local = refAggravatingReasonsMaintainer.findByPrimaryKey(refAggravatingReasonsId);
			basicValue = refAggravatingReasonsMaintainer.getBasicValue(local);
		}
		return basicValue;
	}
	
	public String getCourtName(Integer courtId) {
		String courtName = "Unknown";
		try {
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
		} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court site name.");
			e.printStackTrace();
		}
		return courtName;
	}
	
	
	public Integer getCourtRoomNumber(SchedHearingLocationValue shlv) {
		Integer courtRoomNo = 0;
		if ((shlv != null) && (shlv.getCourtSiteID() != null)) {
			CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
			try {
				courtRoomNo = courtRoomMaintainer.findByPrimaryKey(shlv.getCourtRoomID()).getCrestCourtRoomNo();
			} catch (ObjectNotFoundException e) {
				log.error("Cannot find the court room number.");
				e.printStackTrace();
			}
		}
		return courtRoomNo;
	}
	
}