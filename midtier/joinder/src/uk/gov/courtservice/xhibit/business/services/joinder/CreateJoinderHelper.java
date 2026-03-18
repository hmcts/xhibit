package uk.gov.courtservice.xhibit.business.services.joinder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder.XhbJoinder;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder.XhbJoinderBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder.XhbJoinderBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_charge.XhbJoinderChargeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_charge.XhbJoinderChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_defendant_on_case.XhbJoinderDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_defendant_on_case.XhbJoinderDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_defendant_on_case.XhbJoinderDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.address.AddressHelper;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerLocal;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeType;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceMoveValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderIndictmentValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogCRUDValue;

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
 * @version $Id: CreateJoinderHelper.java,v 1.21 2007/09/03 12:20:56 rzvddy Exp
 *          $
 */
public class CreateJoinderHelper {
	private static final Logger logger = CSServices.getLogger(CreateJoinderHelper.class);

	private ChargeControllerLocal chargeController;

	private static final String JUDGE_NOT_FOUND = "joinderindictmenthelper.judgenotfound";

	private static final String CHARGES_NOT_CREATED = "joinderindictmenthelper.chargenotcreated";

	/**
	 * List containing an array of old caseId , crestoffenceSeqNo,
	 * defendantOnCaseId, defendantOnOffenceId
	 */
	private List oldDefOnOffenceLookupList = new ArrayList();

	private ResourceBundle messages = CSServices.getConfigServices().getBundle("CourtLogFreeText");

	public CreateJoinderHelper() {
		String methodName = "JoinderIndictmentHelper - ";
		logger.debug(methodName + "called ");
		try {
			chargeController = ((ChargeControllerLocalHome) CSServices.getServiceLocator()
					.getLocalHome(ChargeControllerLocalHome.class)).create();
			logger.debug(methodName + "Exited OK");
		} catch (javax.ejb.CreateException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new EJBException(ex);
		}
	}

	public void createJoinderIndictment(JoinderIndictmentValue joinderIndictment, String userDisplayName) throws ChargeControllerException {
		String methodName = "createJoinderIndictment - ";
		if (logger.isDebugEnabled()) {
			logger.debug(methodName + "called ***-1-***");
			printJoinderIndictmentValue(joinderIndictment);
		}
		// get charge ids
		Integer[] chargeIds = joinderIndictment.getOriginalJoiningChargeIds();
		Integer[] caseIds = joinderIndictment.getOriginalJoiningCaseIds();

		// get new charge value
		ChargeValue charge = joinderIndictment.getNewChargeValue();
		// add court log entry
		if (charge.getCourtLogDate() == null) {
			throw new CSUnrecoverableException(
					"createJoinderIndictment() - " + "CourtLogDate has not been set on the JoinderIndictmentValue.");
		}
		// there must be a judge identifed if we are to create a joinder
		if (joinderIndictment.getRefJudgeId() != null && joinderIndictment.getRefJudgeId().intValue() != 0) {
			logEvent(caseIds, chargeIds, charge, joinderIndictment.getRefJudgeId(), charge.getCourtLogDate());
		} else {
			throw new ChargeControllerException(JUDGE_NOT_FOUND, "No refJudgeId provided, cannot contiune.");
		}
		Integer joinderId = createJoinder(joinderIndictment.getRefJudgeId());
		ChargeValue[] chargeValues = new ChargeValue[caseIds.length];
		for (int i = 0; i < caseIds.length; i++) {
			// create newCharge
			ChargeValue newIndictment = getNewIndictmentValue(charge, caseIds[i]);
			logger.debug(methodName + ": new Charge value = " + newIndictment);
			chargeValues[i] = newIndictment;
		}
		logger.debug(methodName + " call to integration facade ***-2-***");
		// calls the method on the int facade
		Integer[] newChargeIds = chargeController.addJoinderChargeToCase(chargeValues, userDisplayName);
		logger.debug(methodName + " return from integration facade ***-5-***");
		if (newChargeIds == null || newChargeIds.length != caseIds.length) {
			String errTxt = "Expected " + caseIds.length + " chargeIds from Mercator, recieved: "
					+ (newChargeIds == null ? 0 : newChargeIds.length);
			logger.debug(errTxt);
			throw new ChargeControllerException(CHARGES_NOT_CREATED, errTxt);
		}
		// for each case
		for (int i = 0; i < newChargeIds.length; i++) {
			// create joindercharge record
			createJoinderCharge(newChargeIds[i], joinderId);
		}
		// move results in batch as now moved in Mercator aswell
		moveResults(newChargeIds, chargeIds);
		// create JoinderDefendantOnCases for this case
		HashMap defOnCaseAliases = joinderIndictment.getDefendantOnCaseAliases();
		if (defOnCaseAliases != null && !defOnCaseAliases.isEmpty()) {
			createJoinderDefendantOnCases(defOnCaseAliases, joinderId);
		}
		stayOriginalIndictments(chargeIds, joinderIndictment.getNewChargeValue().getCourtLogDate().getTime(),
				joinderIndictment.getNewChargeValue().isInCourt());
		// clear lookup lists
		this.oldDefOnOffenceLookupList.clear();
		logger.debug(methodName + "Exited OK ***-6-***");
	}

	private ChargeValue getNewIndictmentValue(ChargeValue newCharge, Integer caseId) {
		String methodName = "getNewIndictmentValue - ";
		logger.debug(methodName + "called with caseId " + caseId);
		// instantiate chargeValue
		ChargeValue newIndictment = new ChargeValue();
		// instantiate newOffenceValues
		Vector newOffenceValues = new Vector();
		// Copy basic values
		newIndictment.setCaseID(caseId);
		newIndictment.setChargeType(new ChargeType(newCharge.getChargeTypeDescription(), newCharge.getChargeType()));
		newIndictment.setCourtID(newCharge.getCourtID());
		newIndictment.setCrestChargeSeqNo(newCharge.getCrestChargeSeqNo());
		newIndictment.setDateIndRec(newCharge.getDateIndRec());
		newIndictment.setInCourt(newCharge.isInCourt());
		newIndictment.setIndResp(newCharge.getIndResp());
		newIndictment.setIndSignedDate(newCharge.getIndSignedDate());
		newIndictment.setProsPaperServedDate(newCharge.getProsPaperServedDate());
		newIndictment.setCourtLogDate(newCharge.getCourtLogDate());
		// add offences
		Iterator newOffences = newCharge.getOffenceValues().iterator();
		while (newOffences.hasNext()) {
			JoinderOffenceValue newOffence = (JoinderOffenceValue) newOffences.next();
			newOffenceValues.add(getNewCount(newOffence, caseId));
		}
		newIndictment.setOffenceValues(newOffenceValues);
		logger.debug(methodName + "Exited OK");
		return newIndictment;
	}

	private OffenceValue getNewCount(JoinderOffenceValue newOffence, Integer caseId) {
		String methodName = "getNewCount - ";
		logger.debug(methodName + "called with caseId " + caseId);
		OffenceValue newCount = new OffenceValue();
		// Copy basic values that are required for OffenceMVO
		newCount.setCaseID(caseId);
		newCount.setCourtID(newOffence.getCourtID());
		newCount.setCrestOffenceFreeText(newOffence.getCrestOffenceFreeText());
		newCount.setDeleteResults(newOffence.isDeleteResults());
		newCount.setInCourt(newOffence.isInCourt());
		newCount.setOffenceDescription(newOffence.getOffenceDescription());
		newCount.setCrestHOClass(newOffence.getCrestHOClass());
		newCount.setCrestHOSubclass(newOffence.getCrestHOSubclass());
		newCount.setPlea(newOffence.getPlea());
		newCount.setRefOffenceID(newOffence.getRefOffenceID());
		newCount.setRefSystemCodeID(newOffence.getRefSystemCodeID());
		newCount.setCrestOffenceSeqNo(newOffence.getCrestOffenceSeqNo());
		newCount.setMultiple(newOffence.getMultiple());
		newCount.setCourtLogDate(newOffence.getCourtLogDate());
		newCount.setActSection(newOffence.getActSection());
		newCount.setOffenceCode(newOffence.getOffenceCode());
		newCount.setStatute(newOffence.getStatute());

		newCount.setOffenceStartDateTime(newOffence.getOffenceStartDateTime());
		newCount.setOffenceEndDateTime(newOffence.getOffenceEndDateTime());
		newCount.setForceLocationCode(newOffence.getForceLocationCode());
		newCount.setAddressId(newOffence.getAddressId());
		newCount.setAddressValue(AddressHelper.getAddressValue(newOffence.getAddressId()));

		/** get defendants on original offence */
		Vector defendantIds = new Vector();
		Vector defendantValues = new Vector();
		HashMap defendantOnOffenceBasicValues = new HashMap();
		Iterator caseOffences = newOffence.getOriginalCaseOffences().iterator();
		logger.debug("looping caseOffences");
		while (caseOffences.hasNext()) {
			Integer[] caseOffence = (Integer[]) caseOffences.next();
			logger.debug("caseId = " + caseOffence[0] + " offenceId = " + caseOffence[1]);
			if (caseOffence[0].equals(caseId)) {
				// create array containing caseId , crestoffenceSeqNo,
				// defendantOnCaseId (not yet populated), defendantOnOffenceId
				// (not yet populated)
				Integer originalChargeId = getOriginalChargeId(caseOffence[1]);
				DefendantOnOffenceLookup defOnOffenceLookup = new DefendantOnOffenceLookup(caseOffence[0],
						originalChargeId, newOffence.getCrestOffenceSeqNo());

				Object[] defs = getOriginalDefendantsAndDefendantOnOffences(caseOffence[1], defOnOffenceLookup,
						originalChargeId);
				defendantIds.addAll((Collection) defs[0]);
				defendantValues.addAll((Collection) defs[1]);
				// set defendantOnOffenceBasic Values (for carrying crn over)
				defendantOnOffenceBasicValues.putAll((HashMap) defs[2]);
			}
		}
		// set defendantIds
		// if empty, it is a new offence so use defendants that are passed in
		// the new offence
		if (!defendantIds.isEmpty()) {
			newCount.setDefendantIDs(defendantIds);
		}
		// set defendantValues
		// if empty, it is a new offence so use defendants that are passed in
		// the new offence
		if (!defendantValues.isEmpty()) {
			newCount.setDefendantValues(defendantValues);
		}
		// set defendantOnOffenceBasicValues
		// if empty, it is a new offence so use defendants that are passed in
		// the new offence
		if (!defendantOnOffenceBasicValues.isEmpty()) {
			newCount.setDefOnOffenceBasicValues(defendantOnOffenceBasicValues);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(methodName + ": new Count value = " + newCount);
			logger.debug(methodName + "Exited OK");
		}
		return newCount;
	}

	/**
	 * For a given offence, populates a list of defendantIds and
	 * DefendantOnOffenceValues. Also sets defendantOnCaseId and
	 * defendantOnOffenceId in defOnOffenceLookup array and adds it to
	 * oldDefOnOffenceLookupList
	 *
	 * @param offenceId
	 * @param defOnOffenceLookup
	 * @return
	 */
	private Object[] getOriginalDefendantsAndDefendantOnOffences(Integer offenceId,
			DefendantOnOffenceLookup defOnOffenceLookup, Integer originalChargeId) {
		String methodName = "getOriginalDefendantsAndDefendantOnOffences - ";
		logger.debug(methodName + "called with offenceId " + offenceId);
		Vector defendantValues = new Vector();
		Vector defendantIds = new Vector();
		HashMap defendantOnOffenceBasicValues = new HashMap();
		// find defendantOnOffences
		Iterator dofs = XhbDefendantOnOffenceBeanHelper2.findByOffenceId(offenceId).iterator();
		logger.debug("looping defendantOnOffence");
		while (dofs.hasNext()) {
			XhbDefendantOnOffence dof = (XhbDefendantOnOffence) dofs.next();
			// set oldDefendantOnOffenceBasicValues
			XhbDefendantOnOffenceBasicValue dofBasicValue = dof.getData();
			// set oldDefOnOffenceLookupList instance variable
			setOldDefOnOffenceLookupList(defOnOffenceLookup, dofBasicValue, originalChargeId);
			// get defendantOnCases
			XhbDefendantOnCase doc = dof.getXhbDefendantOnCase();
			// set defendantIds
			defendantIds.add(doc.getDefendantId());
			// set defendant values
			XhbDefendant d = doc.getXhbDefendant();
			defendantValues.add(populateDefendantValue(d));
			logger.debug("doc : defendantId = " + doc.getDefendantId());
			logger.debug("dofBasicValue : offenceId = " + dofBasicValue.getOffenceId());
			logger.debug("dofBasicValue : crn = " + dofBasicValue.getCrnId());
			// set defendantOnOffenceBasic Values (for carrying crn over)
			defendantOnOffenceBasicValues.put(doc.getDefendantId(), dofBasicValue);
		}
		logger.debug(methodName + "Exited OK");
		return new Object[] { defendantIds, defendantValues, defendantOnOffenceBasicValues };
	}

	/**
	 * Moves pleas, verdicts and disposals by updating the defendantOnOffenceId
	 *
	 * @param chargeId
	 */
	private void moveResults(Integer[] chargeIds, Integer[] originalChargeIds) throws ChargeControllerException {
		String methodName = "moveResults - ";

		DefendantOnOffenceMoveValue[] dofMoves = getOldToNewDefOnOffenceMapping(chargeIds, originalChargeIds);

		// move results by deleting old results for all old defendant on
		// offences and add new results for all
		// new defendant on offences

		// for (int i = 0; i < dofMoves.length; i++) {
		// logger.debug("dofMoves[i]=" +
		// dofMoves[i].getNewDefendantOnOffenceId() + "," +
		// dofMoves[i].getOldDefendantOnOffenceId());
		// }

		try {
			Results2WorkFlow.moveResults(dofMoves);
		} catch (ResultsControllerException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new ChargeControllerException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
		logger.debug(methodName + "Exited OK");
	}

	private DefendantOnOffenceMoveValue[] getOldToNewDefOnOffenceMapping(Integer[] chargeIds,
			Integer[] originalChargeIds) {
		Set dofMoveValues = new HashSet();
		DefendantOnOffenceLookup[] newDofLookup = getNewDefOnOffenceLookupList(chargeIds, originalChargeIds);
		DefendantOnOffenceLookup[] oldDofLookup = (DefendantOnOffenceLookup[]) this.oldDefOnOffenceLookupList
				.toArray(new DefendantOnOffenceLookup[oldDefOnOffenceLookupList.size()]);

		for (int i = 0, oldLen = oldDofLookup.length; i < oldLen; i++) {
			for (int j = 0, newLen = newDofLookup.length; j < newLen; j++) {
				if (oldDofLookup[i].equals(newDofLookup[j])) {
					dofMoveValues.add(new DefendantOnOffenceMoveValue(oldDofLookup[i].defendantOnOffenceId,
							newDofLookup[j].defendantOnOffenceId));
					break;
				}
			}
		}
		return (DefendantOnOffenceMoveValue[]) dofMoveValues
				.toArray(new DefendantOnOffenceMoveValue[dofMoveValues.size()]);
	}

	private void setOldDefOnOffenceLookupList(DefendantOnOffenceLookup defOnOffenceLookup,
			XhbDefendantOnOffenceBasicValue dofBasicValue, Integer originalChargeId) {
		String methodName = "setOldDefOnOffenceLookupList - ";
		logger.debug(methodName + "called ");
		// set defendantOnCaseId and defendantOnOffenceId in DefOnOffenceLookup
		// array
		defOnOffenceLookup.defendantOnCaseId = dofBasicValue.getDefendantOnCaseId();
		logger.debug(methodName + "defendantOnOffenceId = " + dofBasicValue.getDefendantOnOffenceId());
		defOnOffenceLookup.defendantOnOffenceId = dofBasicValue.getDefendantOnOffenceId();
		// set oldDefOnOffenceLookupList
		oldDefOnOffenceLookupList.add(defOnOffenceLookup);
		logger.debug(methodName + "Exited OK");
	}

	private DefendantOnOffenceLookup[] getNewDefOnOffenceLookupList(Integer chargeIds[], Integer originalChargeIds[]) {
		String methodName = "setNewDefOnOffenceLookupList - ";
		ArrayList newDefOnOffenceLookupList = new ArrayList();
		for (int i = 0, len = chargeIds.length; i < len; i++) {
			logger.debug(methodName + "called with chargeId " + chargeIds[i]);
			// get charge
			XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeIds[i]);
			// get offences
			Iterator offences = charge.getXhbOffences().iterator();
			while (offences.hasNext()) {
				XhbOffence offence = (XhbOffence) offences.next();
				Integer crestOffenceSeqNo = offence.getCrestOffenceSeqNo();
				/** @todo check for nulls */
				// get defendantOnOffences
				Iterator dofs = offence.getXhbDefendantOnOffences().iterator();
				while (dofs.hasNext()) {
					XhbDefendantOnOffence dof = (XhbDefendantOnOffence) dofs.next();
					Integer defendantOnCaseId = dof.getDefendantOnCaseId();
					// set defOnOffenceLookup
					DefendantOnOffenceLookup defOnOffenceLookup = new DefendantOnOffenceLookup(charge.getCaseId(),
							originalChargeIds[i], crestOffenceSeqNo, defendantOnCaseId, dof.getDefendantOnOffenceId());

					logger.debug(
							methodName + "dof.getDefendantOnOffenceId()" + defOnOffenceLookup.defendantOnOffenceId);
					// set newDefOnOffenceLookupList instance variable
					newDefOnOffenceLookupList.add(defOnOffenceLookup);
					logger.debug(methodName + "Exited OK");
				}
			}
		}
		return (DefendantOnOffenceLookup[]) newDefOnOffenceLookupList
				.toArray(new DefendantOnOffenceLookup[newDefOnOffenceLookupList.size()]);
	}

	private Integer createJoinder(Integer refJudgeId) {
		String methodName = "createJoinder - ";
		logger.debug(methodName + "called with refJudgeId = " + refJudgeId);
		// create joinder record
		XhbJoinderBasicValue jBV = new XhbJoinderBasicValue();
		// set refJudgeId
		jBV.setRefJudgeId(refJudgeId);
		XhbJoinder joinder = XhbJoinderBeanHelper2.createLocal(jBV);
		logger.debug(methodName + "Exited OK");
		return joinder.getJoinderId();
	}

	private void createJoinderCharge(Integer chargeId, Integer joinderId) {
		String methodName = "createJoinderCharge - ";
		logger.debug(methodName + "called with chargeId = " + chargeId + ", joinderId = " + joinderId);
		// create joindercharge record
		XhbJoinderChargeBasicValue jcBV = new XhbJoinderChargeBasicValue();
		jcBV.setChargeId(chargeId);
		jcBV.setJoinderId(joinderId);
		XhbJoinderChargeBeanHelper2.create(jcBV);
		logger.debug(methodName + "Exited OK");
	}

	private void createJoinderDefendantOnCases(HashMap defendantOnCaseAliases, Integer joinderId) {
		String methodName = "createJoinderDefendantOnCases - ";
		logger.debug(methodName + "called with  joinderId = " + joinderId);
		// assumes defendantOnCaseAliases is a HashMap keyed on defOnCaseId2
		// (unique for a given joinderId)
		// the value part is defOnCaseId1
		for (Iterator it = defendantOnCaseAliases.keySet().iterator(); it.hasNext();) {
			Integer defOnCaseId2 = (Integer) it.next();
			Integer defOnCaseId1 = (Integer) defendantOnCaseAliases.get(defOnCaseId2);
			createJoinderDefendantOnCase(defOnCaseId1, defOnCaseId2, joinderId);
		}
		logger.debug(methodName + "Exited OK");
	}

	private void createJoinderDefendantOnCase(Integer defOnCaseId1, Integer defOnCaseId2, Integer joinderId) {
		String methodName = "createJoinderDefendantOnCase - ";
		logger.debug(methodName + "called with  joinderId = " + joinderId + "defOnCase1 = " + defOnCaseId1
				+ " defOnCase2 = " + defOnCaseId2);
		// check the ids are not the same - this may happen if we are joining
		// indictments from the same case and in this instance the record is not
		// required
		if (defOnCaseId1.equals(defOnCaseId2)) {
			logger.debug("Ids are the same, no need to create alias.");
			return;
		}
		// create joinderDefendantOnCase vo
		XhbJoinderDefendantOnCaseBasicValue jdocBV = new XhbJoinderDefendantOnCaseBasicValue();
		jdocBV.setDefendantOnCaseId1(defOnCaseId1);
		jdocBV.setDefendantOnCaseId2(defOnCaseId2);
		jdocBV.setJoinderId(joinderId);

		// create joinderDefendantOnCase record
		XhbJoinderDefendantOnCaseBeanHelper2.create(jdocBV);

		logger.debug(methodName + "Exited OK");
	}

	private void logEvent(Integer[] caseIds, Integer[] chargeIds, ChargeValue charge, Integer refJudgeId,
			Calendar courtLogDate) throws ChargeControllerException {
		String methodName = "logEvent - ";
		logger.debug(methodName + "called with  charge = " + charge.getChargeID());
		try {
			// log to each case from which indictments have been joined, but
			// only
			// once for each case so remove duplicates from the caseIds
			// Array
			Integer[] caseIdsNoDupes = removeDuplicates(caseIds);
			HashMap caseNums = findCaseNums(chargeIds);
			// message to log - build using the message text from the bundle
			StringBuffer freeText = new StringBuffer();
			freeText.append(MessageFormat.format(messages.getString("joinIndictmentsStart"),
					new Object[] { findIndNum(chargeIds[0]), (String) caseNums.get(chargeIds[0]) }));
			for (int i = 1; i < chargeIds.length; i++) {
				freeText.append(MessageFormat.format(messages.getString("joinIndictmentsMiddle"),
						new Object[] { findIndNum(chargeIds[i]), (String) caseNums.get(chargeIds[i]) }));
			}
			freeText.append(MessageFormat.format(messages.getString("joinIndictmentsEnd"), new Object[] {}));

			logger.debug(methodName + " adding log event with free text = " + freeText.toString());
			MultiCaseCourtLogCRUDValue logEntry = new MultiCaseCourtLogCRUDValue();
			// set the value which notifies if the case is active in court
			logEntry.setInCourt(charge.isInCourt());
			logEntry.setCaseIds(caseIdsNoDupes);
			logEntry.setEntryDate(courtLogDate.getTime());
			logEntry.setEntryFreeText(freeText.toString());
			logEntry.setEventType(new Integer(40203));
			// this is a JOINDER \ CASE level event so don't need any extra
			// CJSE parameters
			CourtLogWorkFlow.newEntry(logEntry);
			logger.debug(methodName + "Exited OK");
		} catch (CourtLogBusinessException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new ChargeControllerException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
	}

	private Integer[] removeDuplicates(Integer[] caseIds) {
		HashSet set = new HashSet();
		for (int i = 0; i < caseIds.length; i++) {
			// The set removes all the duplicates
			set.add(caseIds[i]);
		}
		Integer[] caseIdsNoDupes = new Integer[set.size()];
		int j = 0;
		Iterator setIter = set.iterator();
		while (setIter.hasNext()) {
			caseIdsNoDupes[j] = (Integer) setIter.next();
			j++;
		}
		return caseIdsNoDupes;
	}

	private Integer findIndNum(Integer chargeId) {
		XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeId);
		return charge.getCrestChargeSeqNo();
	}

	private HashMap findCaseNums(Integer[] chargeIds) {
		HashMap caseNumsMap = new HashMap();
		for (int i = 0; i < chargeIds.length; i++) {
			XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeIds[i]);
			XhbCase caze = charge.getXhbCase();
			String caseNumber = caze.getCaseType() + caze.getCaseNumber();
			caseNumsMap.put(chargeIds[i], caseNumber);
		}
		return caseNumsMap;
	}

	private DefendantValue populateDefendantValue(XhbDefendant defendantBean) {
		String methodName = "populateDefendantValue - ";
		logger.debug(methodName + "called with  defendantId = " + defendantBean.getDefendantId());
		// convert Timestamps to Calendars
		Calendar dob = Calendar.getInstance();
		if (defendantBean.getDateOfBirth() != null) {
			dob.setTime(defendantBean.getDateOfBirth());
		} else {
			dob = null;
		}
		Calendar lastConvDate = Calendar.getInstance();
		if (defendantBean.getLastConvictionDate() != null) {
			lastConvDate.setTime(defendantBean.getLastConvictionDate());
		} else {
			lastConvDate = null;
		}

		Byte genderByte = defendantBean.getGender();
		Integer gender = genderByte == null ? null : genderByte.intValue();

		DefendantValue defendantValue = new DefendantValue(defendantBean.getDefendantId(),
				defendantBean.getCrestDefendantId(), defendantBean.getFirstName(), defendantBean.getMiddleName(),
				defendantBean.getSurname(), defendantBean.getInitials(), dob, gender, lastConvDate,
				defendantBean.getCourtId(), defendantBean.getCurrentPrisonStatus(), defendantBean.getPrisonId());
		defendantValue.setUpdateCount(defendantBean.getVersion().intValue());
		logger.debug(methodName + "Exited OK");
		return defendantValue;
	}

	public DefendantValue getDefendantAlias(XhbDefendantOnCase doc, Integer joinderId)
			throws ChargeControllerException {
		String methodName = "getDefendantAlias - ";
		logger.debug(methodName + "called with  defendantOnCaseId = " + doc.getDefendantOnCaseId());
		XhbDefendant defBean;
		DefendantHelper defHelper = new DefendantHelper();
		// JoinderDefOnCase jdoc =
		// jdocM.findByPrimaryKey(doc.getDefendantOnCaseId());
		// Collection jdocs =
		// jdocM.findByDefOnCaseId2AndJId(doc.getDefendantOnCaseId(),
		// joinderId);
		Collection jdocs = XhbJoinderDefendantOnCaseBeanHelper2.findByDefOnCaseId2AndJId(doc.getDefendantOnCaseId(),
				joinderId);
		try {
			switch (jdocs.size()) {
			case 0:
				logger.debug(methodName + "Exited OK");
				defBean = doc.getXhbDefendant();
				return defHelper.getDefendantDetails(defBean.getDefendantId(), doc.getCaseId());
			case 1:
				XhbJoinderDefendantOnCase jdoc = (XhbJoinderDefendantOnCase) jdocs.iterator().next();
				logger.debug(methodName + "Exited OK");
				defBean = jdoc.getXhbDefendantOnCaseByDefendantOnCaseId1().getXhbDefendant();
				return defHelper.getDefendantDetails(defBean.getDefendantId(),
						jdoc.getXhbDefendantOnCaseByDefendantOnCaseId1().getCaseId());
			default:
				logger.error(
						"findByDefOnCaseId2AndJId returned more than one row, there should be only one DefendantOnCase row per defendantOnCaseId2,joinderId");
				throw new EJBException(
						"findByDefOnCaseId2AndJId returned more than one row, there should be only one DefendantOnCase row per defendantOnCaseId2,joinderId");
			}
		} catch (DefendantControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			Object[] params = e.getUserMessageAsMessage().getParameters();
			if (params.length > 0) {
				throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), params, e.getMessage(), e);
			} else {
				throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
			}
		}
	}

	/**
	 * Utility method for printing the value to debug
	 *
	 * @param value
	 *            The JoinderIndictmentValue to print to degug
	 */
	private void printJoinderIndictmentValue(JoinderIndictmentValue value) {
		ChargeValue charge = null;
		logger.debug("");
		logger.debug("");
		logger.debug("PRINT JOINDER INDICTMENT VALUE");
		logger.debug("");
		logger.debug("");
		logger.debug("refJudgeId = " + value.getRefJudgeId());
		charge = value.getNewChargeValue();
		if (charge != null) {
			logger.debug("new charge = " + charge);
		} else {
			logger.debug("charge is null");
			return;
		}
		Collection offences = charge.getOffenceValues();
		if (offences != null) {
			logger.debug(offences.size() + " offences on new charge");
			Iterator it = offences.iterator();
			while (it.hasNext()) {
				JoinderOffenceValue offence = (JoinderOffenceValue) it.next();
				logger.debug("offence = " + offence);
				Collection defendantIds = offence.getDefendantIDs();
				if (defendantIds != null) {
					logger.debug("Defendant Ids:");
					Iterator defIt = defendantIds.iterator();
					while (defIt.hasNext()) {
						logger.debug("def id = " + defIt.next());
					}
				} else {
					logger.debug("No defendant Ids");
				}
				HashMap dooBasicVals = offence.getDefOnOffenceBasicValues();
				if (dooBasicVals != null) {
					Iterator keysIt = dooBasicVals.keySet().iterator();
					while (keysIt.hasNext()) {
						logger.debug("Defendant on offence bv's:");
						Integer key = (Integer) keysIt.next();
						logger.debug("defendant id: " + key + " had def on offence bv " + dooBasicVals.get(key));
					}
				} else {
					logger.debug("No defendant on offence bv's");
				}
				Vector originalCaseOffences = offence.getOriginalCaseOffences();
				if (originalCaseOffences != null) {
					logger.debug("Examining caseOffences, found " + originalCaseOffences.size() + " CountOffence(s)");
					Iterator originalCaseOffencesIt = originalCaseOffences.iterator();
					while (originalCaseOffencesIt.hasNext()) {
						Integer[] caseOffence = (Integer[]) originalCaseOffencesIt.next();
						logger.debug("caseId = " + caseOffence[0] + " offenceId = " + caseOffence[1]);
					}
				} else {
					logger.debug("Examining caseOffences, found 0 CountOffences");
				}
			}
		}
		HashMap aliases = value.getDefendantOnCaseAliases();
		Integer[] caseIds = value.getOriginalJoiningCaseIds();
		Integer[] chargeIds = value.getOriginalJoiningChargeIds();
		if (aliases != null && !aliases.isEmpty()) {
			logger.debug("Defendant aliases: ");
			Iterator keysIt = aliases.keySet().iterator();
			// should be only one - the case id
			while (keysIt.hasNext()) {
				Integer key = (Integer) keysIt.next();
				Integer defOnCaseId1 = (Integer) aliases.get(key);
				logger.debug("defOnCaseId1 =  " + defOnCaseId1);
				logger.debug("defOnCaseId2 =  " + key);
			}
		} else {
			logger.debug("No defendant aliases");
		}
		if (caseIds != null) {
			logger.debug("Original case ids:");
			int size = caseIds.length;
			for (int i = 0; i < size; i++) {
				logger.debug("case Id: " + caseIds[i]);
			}
		} else {
			logger.debug("No original case ids");
		}
		if (chargeIds != null) {
			logger.debug("Original charge ids:");
			int size = chargeIds.length;
			for (int j = 0; j < size; j++) {
				logger.debug("charge Id: " + chargeIds[j]);
			}
		} else {
			logger.debug("No original charge ids");
		}
	}

	private void stayOriginalIndictments(Integer[] chargeIds, Date courtLogDate, boolean isInCourt)
			throws ChargeControllerException {
		String methodName = "stayOriginalIndictments - ";
		logger.debug(methodName + "called.");
		// loop through each indictment
		for (int i = 0; i < chargeIds.length; i++) {
			logger.debug(" chargeId " + i + " = " + chargeIds[i]);
			// find all defendantOnOffences for this indictment
			// Collection dofsCol = dofMaintainer.findByChargeId(chargeIds[i]);
			Collection dofsCol = XhbDefendantOnOffenceBeanHelper2.findByChargeId(chargeIds[i]);
			XhbDefendantOnOffenceBasicValue[] dofbvs = new XhbDefendantOnOffenceBasicValue[dofsCol.size()];
			Iterator dofs = dofsCol.iterator();
			// convert local bean interface to basic value object (required
			// by updateDefendantOnCountStatus method)
			for (int j = 0; dofs.hasNext(); j++) {
				dofbvs[j] = ((XhbDefendantOnOffence) dofs.next()).getData();
				logger.debug("defendantOnOffenceId " + j + " = " + dofbvs[j].getDefendantOnOffenceId());
			}
			// create CourtLogCRUDValue
			XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeIds[i]);
			CourtLogCRUDValue cv = new CourtLogCRUDValue();
			cv.setCaseId(charge.getCaseId());
			cv.setEntryDate(courtLogDate);
			cv.setEntryFreeText("");
			cv.setEventType(new Integer(40206));
			cv.setInCourt(isInCourt);
			// call method to stay all defendantOnOffences for this indictment
			chargeController.updateDefendantOnCountStatus(dofbvs, cv);
			logger.debug(methodName + "exited.");
		}
	}

	private Integer getOriginalChargeId(Integer offenceId) {
		return XhbOffenceBeanHelper2.findByPrimaryKey(offenceId).getXhbCharge().getChargeId();
	}

	class DefendantOnOffenceLookup {
		Integer caseId;

		Integer chargeId;

		Integer crestOffenceSeqNo;

		Integer defendantOnCaseId;

		Integer defendantOnOffenceId;

		public DefendantOnOffenceLookup(Integer caseId, Integer chargeId, Integer crestOffenceSeqNo) {
			this.caseId = caseId;
			this.chargeId = chargeId;
			this.crestOffenceSeqNo = crestOffenceSeqNo;
		}

		public DefendantOnOffenceLookup(Integer caseId, Integer chargeId, Integer crestOffenceSeqNo,
				Integer defendantOnCaseId, Integer defendantOnOffenceId) {
			this.caseId = caseId;
			this.chargeId = chargeId;
			this.crestOffenceSeqNo = crestOffenceSeqNo;
			this.defendantOnCaseId = defendantOnCaseId;
			this.defendantOnOffenceId = defendantOnOffenceId;
		}

		public boolean equals(Object o) {
			if (o instanceof DefendantOnOffenceLookup) {
				return equals((DefendantOnOffenceLookup) o);
			} else {
				return false;
			}
		}

		boolean equals(DefendantOnOffenceLookup o) {
			// Mercator guarantees to keep the offence seq no the
			// same, the defendantOnCaseId is the same
			// so if the caseId, crestOffenceSeqNo and
			// defendantOnCaseId are the same then the
			// defendantOnOffenceIds
			// refer to the same defendantOnOffence record
			return caseId != null && caseId.equals(o.caseId) && chargeId != null && chargeId.equals(o.chargeId)
					&& crestOffenceSeqNo != null && crestOffenceSeqNo.equals(o.crestOffenceSeqNo)
					&& defendantOnCaseId != null && defendantOnCaseId.equals(o.defendantOnCaseId);
		}

		public int hashCode() {
			int result = 37;
			result = 37 * result + ((this.caseId != null) ? this.caseId.hashCode() : 0);
			result = 37 * result + ((this.chargeId != null) ? this.chargeId.hashCode() : 0);
			result = 37 * result + ((this.crestOffenceSeqNo != null) ? this.crestOffenceSeqNo.hashCode() : 0);
			result = 37 * result + ((this.defendantOnCaseId != null) ? this.defendantOnCaseId.hashCode() : 0);

			return result;
		}
	}
}
