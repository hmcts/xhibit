package uk.gov.courtservice.xhibit.business.services.charge;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.chargeslog.ChargesLogMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.orders.AggravatingReasons;
import uk.gov.courtservice.xhibit.business.entities.orders.AggravatingReasonsMaintainer;
import uk.gov.courtservice.xhibit.business.entities.orders.RefAggravatingReasons;
import uk.gov.courtservice.xhibit.business.entities.orders.RefAggravatingReasonsMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreach;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_charges_log.XhbChargesLog;
import uk.gov.courtservice.xhibit.business.entities.xhb_charges_log.XhbChargesLogBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLine;
import uk.gov.courtservice.xhibit.business.entities.xhb_hate_sentencing.XhbHateSentencing;
import uk.gov.courtservice.xhibit.business.entities.xhb_hate_sentencing.XhbHateSentencingBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_hate_sentencing.XhbHateSentencingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCodeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.services.address.AddressHelper;
import uk.gov.courtservice.xhibit.business.services.darts.XhibitDartsControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBean;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnOffenceHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AggravatingReasonsBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ChargesLogBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAggravatingReasonsBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CrnValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * This class provides methods to read, add, update and delete Charges and
 * related information (e.g. Offences, Breaches). All add, update and delete
 * methods invoke method(s) on the Integration Facade to perform these actions.
 * These actions require court log entries to be created, court log entries are
 * created before the call to the Integration Facade is performed, this is so
 * that the entry creation can be rolled back if there is an error in the
 * Integration Facade. It is not possible to roll back a transaction on the
 * Integration Facade if there is an error in the court log, hence the order of
 * operation.
 * <p>
 * Title: ChargeControllerBean
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * 
 * @ejb.bean name="ChargeController" description="Charge Session Bean"
 *           type="Stateless" view-type="both" jndi-name="ChargeControllerHome"
 *           local-jndi-name="ChargeControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @todo In future CJSE event parameters will need to be added to the court log
 *       events created in this class. However these events are not being sent
 *       to the CJSE for the October 03 release so the events will not be
 *       populated here.
 */
public class ChargeControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private static final Logger log = CSServices.getLogger(ChargeControllerBean.class);

	// error keys
	private static final String CHARGE_NOT_FOUND = "charge.chargenotfound";

	private static final String CANNOT_DELETE = "charge.cannotdelete";

	private static final String[] HO_PROC_CODES = new String[] { "31", "44", "53", "56", "58", "66", "67", "81", "82",
			"83", "84" };

	private static final String[] RECEIPT_TYPE_CODES = new String[] { "ST", "EW", "IO" };

	private static final String OBSOLETE = "Y";

	private static final String CASE_TYPE_SENTENCE = "S";

	private static final String CASE_TYPE_TRIAL = "T";

	private static final String COURT_TYPE_FAIL2APPEAR = "C";

	private static final String BREACH_TYPE_BRING_BACK = "B";

	private static final String BREACH_TYPE_FAIL2APPEAR = "F";

	private static final String BRING_BACK_FAIL2APPEAR = "F";

	private static final String RECEIPT_TYPE_BRING_BACK = "BB";

	private BreachHelper breachHelper = new BreachHelper();

	private ChargeEventHelper eventHelper = new ChargeEventHelper();

	private OriginalChargesHelper originalChargesHelper = new OriginalChargesHelper();

	private RefAggravatingReasonsMaintainer refAggravatingReasonsMaintainer;
	
	private AggravatingReasonsMaintainer aggravatingReasonsMaintainer;
	
	/**
	 * 
	 * @param chargeValues
	 * @param userDisplayName
	 * @return
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public Integer[] addJoinderChargeToCase(ChargeValue[] chargeValues, String userDisplayName) throws ChargeControllerException {
		Integer[] chargeIds = new Integer[chargeValues.length];
		try {
			for (int i = 0; i < chargeValues.length; i++) {
				setDirtyFlags(chargeValues[i]);
				chargeIds[i] = getChargesId(chargeValues[i], userDisplayName);
			}
		} catch (Exception e) {
			log.error("Error occurred in adding joinder charge to case " + e);
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
		}
		return chargeIds;
	}

	/**
	 * @param chargeValue
	 * @param hasHearing
	 * @param userDisplayName
	 * @return
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	@SuppressWarnings("unchecked")
	public Integer addChargeToCase(ChargeValue chargeValue, Boolean hasHearing, String userDisplayName) throws ChargeControllerException {
		log.debug("start method: addChargeToCase() entered");
		Integer chargeId = null;
		try {

			// ctx-242, ctx-325 get the case bean
			XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(chargeValue.getCaseID());

			// ctx-242 validate the data
			if (ChargeTypes.isBreachChargeType(chargeValue.getChargeType())) {
				validateOriginalSentenceDate(caseBean, chargeValue.getChargeType(),
						chargeValue.getBreachValue().getOriginalSentenceDate());
				//onlt if it has a sched hearing, we don't want this done via case create/amend
				if(hasHearing) {
				
				validateDatePut(caseBean, chargeValue.getChargeType(), chargeValue.getBreachValue().getDatePut());
				validateBreachType(caseBean, chargeValue.getBreachValue());
validatePlea(caseBean, chargeValue.getChargeType(), chargeValue.getBreachValue().getPlea(),
						chargeValue.getBreachValue().getHoCode());
				}				
			}
			// ctx-325 validate the data and update case
			else if (ChargeTypes.INDICTMENT.getChargeType().equals(chargeValue.getChargeType())) {
				// Business rules to validate data
				validateProsecutionPaperServedDate(caseBean, chargeValue.getProsPaperServedDate());
				validateDateIndictmentReceived(chargeValue.getDateIndRec());

				// Defend against date indictment received being null
				Date dateIndRec = null;
				if (chargeValue.getDateIndRec() != null) {
					dateIndRec = chargeValue.getDateIndRec().getTime();
				}
				caseBean.setDateIndRec(dateIndRec);
			}

			// ctx-242, ctx-282, ctx-325, ctx-351 create the charge
			XhbCharge chargeBean = createChargeEntity(chargeValue, userDisplayName);
			chargeId = chargeBean.getChargeId();

			// ctx-242, ctx-282, ctx-325, ctx-351 add offences
			Collection offenceValues = chargeValue.getOffenceValues();
			if (offenceValues != null && !offenceValues.isEmpty()) {
				saveOffenceEntities(chargeBean, offenceValues, userDisplayName);
			}

			// ctx-242 create the breach and defendant charge
			if (ChargeTypes.isBreachChargeType(chargeValue.getChargeType())) {
				XhbDefendantCharge defendantChargeBean = createDefendantChargeEntity(chargeBean, chargeValue, userDisplayName);
				saveBreachEntity(chargeBean, chargeValue.getBreachValue());
				savePleaEntity(defendantChargeBean.getDefendantChargeId(), chargeValue.getBreachValue());
			}

			// have to create the court log entry after creating the
			// new charge sequence number for the court log event
			chargeValue.setChargeID(chargeId);
			CourtLogCRUDValue logEntry = eventHelper.addChargeCourtLog(chargeValue);
			if (offenceValues != null && !offenceValues.isEmpty()) {
				if (chargeValue.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
					// special handling for counts as we need the Crest Offence
					// Sequence Number
					logAllCounts(chargeValue, chargeId, logEntry);
				} else {
					// only need the offence descriptions here, which we already
					// have so no
					// need to look up the offences, just use the offence values
					// we have
					Iterator offValIt = offenceValues.iterator();
					while (offValIt.hasNext()) {
						OffenceValue offenceVal = (OffenceValue) offValIt.next();
						offenceVal.setChargeID(chargeId);
						if (!chargeValue.getChargeTypeDescription().equals(ChargeTypes.BREACH.getTypeDescription())) {
							eventHelper.logRelatedOffence(chargeValue.getChargeType(), logEntry, offenceVal);
						}
					}
				}
			}
		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		} catch (BisRefControllerException ex) {
			handleWrapAndRethrowException(ex);
		}

		log.debug("start method: addChargeToCase() exited");
		return chargeId;
	}

	/**
	 * 
	 * @param chargeId
	 *            Integer
	 * @param refOffenceId
	 *            Integer           
	 * 
	 * @ejb.interface-method view-type="remote"
	 */	
	public List<Integer> findDefendantIdsByChargeIdAndRefOffenceId(Integer chargeId, Integer refOffenceId, Integer addressId) {
		DefendantOnOffenceHelper dofHelper = new DefendantOnOffenceHelper();
		return dofHelper.findDefendantIdsByChargeIdAndRefOffenceId(chargeId, refOffenceId, addressId);
	}
	
	/**
	 * This method adds counts to defendants\defendants to counts as specified
	 * by the count defendant pairs in the LinkCountDefValue. If one update
	 * fails all will fail.
	 * 
	 * @param linkVal
	 *            LinkCountDefValue
	 * @param userDisplayName
	 *            String
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void linkCountsAndDefendants(LinkCountDefValue linkVal, String userDisplayName) throws ChargeControllerException {
		log.debug("start method: linkCountsAndDefs()");

		Collection links = linkVal.getDefendantOnOffenceValues();
		Iterator linksIt = links.iterator();
		UpdateJoinderIndictmentHelper updateJHelper = new UpdateJoinderIndictmentHelper();

		// create the log entry for each pair
		while (linksIt.hasNext()) {
			DefendantOnOffenceValue doof = (DefendantOnOffenceValue) linksIt.next();
			// check if the offence is from a joinder, if so we need to make
			// sure
			// we're adding to the correct offence id
			updateJHelper.checkJoinderOffence(new Integer[] { doof.getOffenceId(), doof.getDefendantId() });

			try {
				eventHelper.linkCountDefCourtLog(linkVal, doof);
			} catch (CourtLogBusinessException ex) {
				handleWrapAndRethrowException(ex);
			}

			// Get the beans
			XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(doof.getOffenceId());

			// ctx-317 Copy existing offence for summary and committal offences
			if (!ChargeTypes.INDICTMENT.getChargeType().equals(linkVal.getChargeType())) {
				offenceBean = copyOffenceEntity(offenceBean, userDisplayName);
			}

			// ctx-314, ctx-317 Create the defendant on offence entity
			saveDefendantOnOffenceEntity(offenceBean, doof, userDisplayName);
		}

	}

	/**
	 * This method is used to add an offence to the current charge.
	 * 
	 * @param offenceValue
	 *            OffenceValue
	 * @param userDisplayName
	 *            String 
	 * @throws ChargeControllerException
	 * @roseuid 3DAEB9FC034D
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void addOffence(OffenceValue offenceValue, String userDisplayName) throws ChargeControllerException {
		if (log.isDebugEnabled()) {
			log.debug("start method: addOffence(offenceValue) with offenceValue = " + offenceValue);
		}

		try {
			// ctx-282, ctx-295 Removed the call to mercator api
			// and replaced with direct database update
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(offenceValue.getChargeID());
			XhbOffence offenceBean = saveOffenceEntity(chargeBean, offenceValue, userDisplayName);
			offenceValue.setOffenceId(offenceBean.getOffenceId());

			// add the court log event
			eventHelper.addOffenceCourtLog(offenceValue);

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	/**
	 * This method is used to add an offence to the joinder
	 * 
	 * @param offenceValue
	 *            OffenceValue
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void addJoinderOffence(OffenceValue offenceValue, String userDisplayName) throws ChargeControllerException {
		if (log.isDebugEnabled()) {
			log.debug("start method: addJoinderOffence(offenceValue) with offenceValue = " + offenceValue);
		}

		try {
			XhbChargeBasicValue[] charges = XhbChargeBeanHelper2.findJoinedChargesValue(offenceValue.getChargeID());

			offenceValue.setDirty(true);

			if (offenceValue.getAddressValue() != null) {
				offenceValue.getAddressValue().setDirty(true);
			}

			Collection defValues = offenceValue.getDefendantValues();
			if (defValues != null && !defValues.isEmpty()) {
				Iterator defValsIt = defValues.iterator();
				while (defValsIt.hasNext()) {
					uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue = (uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue) defValsIt
							.next();
					defValue.setDirty(false);
					AddressValue addressVal = defValue.getAddressValue();
					if (addressVal != null) {
						addressVal.setDirty(false);
					}
				}
			}
			for (XhbChargeBasicValue charge : charges) {
				// Save the offence entity
				saveOffence(charge, offenceValue, userDisplayName);

				// add the court log event
				eventHelper.addJoinderOffenceCourtLog(offenceValue);
			}

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		} catch (Exception e) {
			log.error("Error occurred in addJoinderOffence " + e);
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
		}
	}

	/**
	 * This method deletes the charge corresponding to the charge ID passed as
	 * parameter in the DelChargeVal. If there are results associated with this
	 * Charge a ResultsFoundException will be thrown. If the user confirms that
	 * the results can be deleted this method should be called again with the
	 * deleteResults flag in the DelChargeValue set to true. The Charge will
	 * then be deleted.
	 * 
	 * @param delChargeVal
	 *            DelChargeValue
	 * @throws ChargeControllerException
	 * @throws ResultsFoundException
	 * @roseuid 3DB812400279
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void deleteCharge(DelChargeValue delChargeVal) throws ChargeControllerException, ResultsFoundException {
		if (log.isDebugEnabled()) {
			log.debug("start method: deleteCharge(delChargeValue) with delChargeValue = " + delChargeVal);
		}
		String userDisplayName = "XHIBIT";

		try {
			// Check the charge type, we only delete indictments and
			// breaches at the charge level
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(delChargeVal.getChargeID());
			String chargeType = chargeBean.getChargeType();
			if (log.isDebugEnabled()) {
				log.debug("chargeType = " + chargeType);
				log.debug("ChargeTypes.INDICTMENT = " + ChargeTypes.INDICTMENT);
				log.debug("ChargeTypes.INDICTMENT.getChargeType() = " + ChargeTypes.INDICTMENT.getChargeType());
			}

			if (chargeType != null && chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
				// check for joinder charges to delete
				DeleteJoinderIndictmentHelper deleteJHelper = new DeleteJoinderIndictmentHelper();
				// if no joinder charges this array will contain just the
				// original value
				DelChargeValue[] charges = deleteJHelper.deleteCheckJoinderCharge(delChargeVal);
				int numCharges = charges.length;
				for (int i = 0; i < numCharges; i++) {
					DelChargeValue delCharge = charges[i];
					doDeleteCharge(delCharge, chargeType, userDisplayName);
				}
				if (charges.length > 1) // we have a joinder
				{
					// remove the joinder, joinderCharge and
					// joinderDefOnCase records
					// for this joinder
					JoinderIndictmentHelper jHelper = new JoinderIndictmentHelper();
					Integer joinderId = jHelper.getJoinderId(chargeBean.getChargeId());
					deleteJHelper.removeJoinderRecord(joinderId);
				}
			} else if (chargeType != null && ChargeTypes.isBreachChargeType(chargeType)) {
				doDeleteCharge(delChargeVal, chargeType, userDisplayName);
			} else {
				ctx.setRollbackOnly();
				log.debug("deleteCharge: Transaction ROLLBACK");
				throw new ChargeControllerException(CANNOT_DELETE, new Object[] { delChargeVal.getChargeID() },
						"Cannot delete a charge which is not an indictment or a breach: " + "chargeID = "
								+ delChargeVal.getChargeID());
			}

		} catch (XhbChargeBeanNotFoundException e) {
			ctx.setRollbackOnly();
			log.debug("deleteCharge: Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// the chargeID has come from the GUI so treat this as a
			// business exception
			throw new ChargeControllerException(CHARGE_NOT_FOUND, new Object[] { delChargeVal.getChargeID() },
					"Could not find the charge specified with charge id" + delChargeVal.getChargeID(), e);
		}
		if (log.isDebugEnabled())
			log.debug("END: deleteCharge() normally");
	}

	/**
	 * This method deletes the offence corresponding to the offence ID passed as
	 * parameter in the DelOffenceVal. If there are results associated with this
	 * Offence a ResultsFoundException will be thrown. If the user confirms that
	 * the results can be deleted this method should be called again with the
	 * deleteResults flag in the DelOffenceValue set to true. The Offence will
	 * then be deleted.
	 * 
	 * @param delOffenceVal
	 *            DelOffenceValue
	 * @throws ChargeControllerException
	 * @throws ResultsFoundException
	 * @roseuid 3DAFF28F0269
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void deleteOffence(DelOffenceValue delOffenceVal) throws ChargeControllerException, ResultsFoundException {
		this.deleteJoinderIndictments(delOffenceVal);
	}

	private void deleteOffenceInternal(DelOffenceValue delOffenceVal, String userDisplayName)
			throws ChargeControllerException, ResultsFoundException {
		if (log.isDebugEnabled()) {
			log.debug("start method: deleteOffence(delOffenceValue) with delOffenceValue = " + delOffenceVal);
		}

		// Check for results if deleteResults flag is false
		if (!delOffenceVal.isDeleteResults()) {
			ChargeHelper.checkResultsForOffence(delOffenceVal.getOffenceID());
		}

		try {
			eventHelper.deleteOffenceCourtLog(delOffenceVal);

			// Get the beans
			XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(delOffenceVal.getOffenceID());
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(delOffenceVal.getChargeID());

			// CTX-260 Call the updates that were called as part of mercator
			deleteOffenceEntity(chargeBean, offenceBean, userDisplayName);

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		} catch (BisRefControllerException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	/**
	 * This method will sign the indictment if the number of days between today
	 * and the prosecution papers signed date is less than SIGN_IND_DAYS days,
	 * otherwise an OutOfTimeException will be thrown. If this exception is
	 * thrown the user should be prompted to confirm that leave has been granted
	 * by the judge to sign the Indictment out of time. If this is the case this
	 * method should be called again with the signOutOfTime flag set to true in
	 * the SignIndValue, the indictment will then be signed.
	 * 
	 * @param signIndVal
	 *            SignIndValue
	 * @throws ChargeControllerException
	 * @throws OutOfTimeException
	 * @roseuid 3DB3F5F503CF
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void signIndictment(SignIndValue signIndVal)
			throws ChargeControllerException, OutOfTimeException, IndictmentSignDateInTheFuture {
		if (log.isDebugEnabled())
			log.debug("START: signIndictment(SignIndValue signIndVal) ATT: chargeID + " + signIndVal.getChargeID());
		int totalDays = 0;

		// check the number of days between today and prospapers served date
		try {
			XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(signIndVal.getChargeID());
			Date prosDate = charge.getProsPaperServedDate();
			Calendar prosCal = null;

			if (prosDate != null) {
				prosCal = Calendar.getInstance();
				prosCal.setTime(prosDate);
				totalDays = checkProsPapersServedDate(prosCal);
			} else {
				log.warn("prosecution paper served date was null for chargeID " + signIndVal.getChargeID());
			}

			Date currentDate = new Date();
			if (signIndVal.getIndSignedDate().getTime().after(currentDate)) {
				throw new IndictmentSignDateInTheFuture();
			}

			if (!signIndVal.isSignOutOfTime() && totalDays > SignIndValue.SIGN_IND_DAYS) {
				// need permission to sign out of time
				throw new OutOfTimeException();
			}
		} catch (XhbChargeBeanNotFoundException e) {
			ctx.setRollbackOnly();
			log.debug("signIndictment: Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(e, ChargeControllerBean.class);
			// the chargeID has come from the GUI so treat this as a
			// business exception
			throw new ChargeControllerException(CHARGE_NOT_FOUND, new Object[] { signIndVal.getChargeID() },
					"Could not find the charge specified with charge id " + signIndVal.getChargeID(), e);
		}

		// if we are here then we have permission to sign out of time
		// (signOutOfTime is true)
		// or total days is less than SIGN_IND_DAYS so we can sign the
		// indictment
		try {
			eventHelper.signIndictmentCourtLog(signIndVal, totalDays);

			// ctx-346 Directly update the db
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(signIndVal.getChargeID());
			chargeBean.setIndSignedDate(signIndVal.getIndSignedDate().getTime());

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	/**
	 * Stays the defendant on offence for each defendant on offence basic value
	 * passed
	 * 
	 * @param defOnOffenceBasicValues
	 *            the defendants on offence to stay
	 * @param logEntry
	 *            basics of the court log event for this stay - caseId,
	 *            entryDate, entryFreeText, eventType and inCourt parameters
	 *            should be set on this Object
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateDefendantOnCountStatus(XhbDefendantOnOffenceBasicValue[] defOnOffenceBasicValues,
			CourtLogCRUDValue logEntry) throws ChargeControllerException {
		XhbDefendantOnOffenceBasicValue dooBasicValue;
		int numStays = defOnOffenceBasicValues.length;
		for (int i = 0; i < numStays; i++) {
			dooBasicValue = defOnOffenceBasicValues[i];
			// check if the obsind is being updated then no need to set the
			// stayed status.
			dooBasicValue.setIsStayed("Y");
			XhbDefendantOnOffenceBeanHelper2.update(dooBasicValue);
		}
		// create the court log event
		if (numStays != 0) {
			try {
				eventHelper.logStayEvent(defOnOffenceBasicValues[0], logEntry);
			} catch (CourtLogBusinessException ex) {
				handleWrapAndRethrowException(ex);
			}
		}
	}

	/**
	 * This method updates the breach passed as parameter.
	 * 
	 * @param breachValue
	 *            BreachValue
	 * @param hasHearing
	 *            boolean 
	 * @throws ChargeControllerException
	 * @roseuid 3DAFF42500DC
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateBreach(BreachValue breachValue, boolean hasHearing) throws ChargeControllerException {
		boolean createCourtLogEntry = true;
		updateBreachInternal(breachValue, createCourtLogEntry, hasHearing);
	}

	private void updateBreachInternal(BreachValue breachValue, boolean createCourtLogEntry, boolean hasHearing)
			throws ChargeControllerException {
		if (log.isDebugEnabled()) {
			log.debug("start method: updateBreach()");
		}
		try {
			if (createCourtLogEntry) {
				eventHelper.updateBreachCourtLog(breachValue);
			}

			// ctx-270 get the required beans
			XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(breachValue.getCaseID());
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(breachValue.getChargeID());

			ChargeValue chargeValue = new ChargeValue(caseBean.getCaseId(), ChargeTypes.ORIGINAL_CHARGE);
			// ctx-270 validate the breach values
			validateOriginalSentenceDate(caseBean, chargeBean.getChargeType(), breachValue.getOriginalSentenceDate());
			if (hasHearing) {			
				validateDatePut(caseBean, chargeBean.getChargeType(), breachValue.getDatePut());
				validateBreachType(caseBean, breachValue);
				validatePlea(caseBean, chargeBean.getChargeType(), breachValue.getPlea(), breachValue.getHoCode());
			}

			// set dirty flag
			breachValue.setDirty(true);

			// ctx-270 Directly update the charge in db
			chargeBean.setXhbRefSystemCode(
					XhbRefSystemCodeBeanHelper2.findByPrimaryKey(breachValue.getRefSystemCodeID()));

			// ctx-270 Directly update the breach in db
			saveBreachEntity(chargeBean, breachValue);

			// ctx-1631 Directly update the plea in db.
			savePleaEntity(chargeBean, breachValue);

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	/**
	 * ctx-1643
	 * 
	 * Update the defendant deportation details. This will only be udpated in
	 * the xhibit database.
	 * 
	 * 
	 * @param defOnCaseValue
	 *            DefendantOnCaseValue
	 * @param userDisplayName 
	 *            String           
	 * 
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */

	public void updateDefOnCaseDeportation(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName)
			throws ChargeControllerException {

		XhbDefendantOnCaseBasicValue xhbDefCaseValue = new XhbDefendantOnCaseBasicValue();
		XhbDefendantOnCase defCaseBean = XhbDefendantOnCaseBeanHelper2
				.findByPrimaryKey(defOnCaseBasicValue.getDefendantOnCaseId());

		// mandatory values
		xhbDefCaseValue.setDefendantOnCaseId(defCaseBean.getDefendantOnCaseId());
		xhbDefCaseValue.setCaseId(defCaseBean.getCaseId());
		xhbDefCaseValue.setDefendantId(defCaseBean.getDefendantId());

		// Deportation values.
		xhbDefCaseValue.setCustodial(defOnCaseBasicValue.getCustodial());
		xhbDefCaseValue.setSuspended(defOnCaseBasicValue.getSuspended());
		xhbDefCaseValue.setSeriousDrugOffence(defOnCaseBasicValue.getSeriousDrugOffence());
		xhbDefCaseValue.setRecommendedDeportation(defOnCaseBasicValue.getRecommendedDeportation());

		// Get existing values from db and update it.

		xhbDefCaseValue.setCaseId(defCaseBean.getCaseId());
		xhbDefCaseValue.setCreatedBy(defCaseBean.getCreatedBy());
		xhbDefCaseValue.setCreationDate(defCaseBean.getCreationDate());
		xhbDefCaseValue.setVersion(defCaseBean.getVersion());
		xhbDefCaseValue.setLastUpdatedBy(defCaseBean.getLastUpdatedBy());

		xhbDefCaseValue.setNoOfTics(defCaseBean.getNoOfTics());
		xhbDefCaseValue.setFinalDrivingLicenceStatus(defCaseBean.getFinalDrivingLicenceStatus());
		xhbDefCaseValue.setPtiurn(defCaseBean.getPtiurn());
		xhbDefCaseValue.setIsJuvenile(defCaseBean.getIsJuvenile());

		xhbDefCaseValue.setIsMasked(defCaseBean.getIsMasked());
		xhbDefCaseValue.setMaskedName(defCaseBean.getMaskedName());
		xhbDefCaseValue.setCaseId(defCaseBean.getCaseId());
		xhbDefCaseValue.setDefendantId(defCaseBean.getDefendantId());

		xhbDefCaseValue.setObsInd(defCaseBean.getObsInd());
		xhbDefCaseValue.setResultsVerified(defCaseBean.getResultsVerified());
		xhbDefCaseValue.setDefendantNumber(defCaseBean.getDefendantNumber());
		xhbDefCaseValue.setDateOfCommittal(defCaseBean.getDateOfCommittal());

		xhbDefCaseValue.setPncId(defCaseBean.getPncId());
		xhbDefCaseValue.setCollectMagistrateCourtId(defCaseBean.getCollectMagistrateCourtId());
		xhbDefCaseValue.setCurrentBcStatus(defCaseBean.getCurrentBcStatus());
		xhbDefCaseValue.setAsn(defCaseBean.getAsn());

		xhbDefCaseValue.setBenchWarrantExecDate(defCaseBean.getBenchWarrantExecDate());
		xhbDefCaseValue.setCommBcStatus(defCaseBean.getCommBcStatus());
		xhbDefCaseValue.setBcStatusBwExecuted(defCaseBean.getBcStatusBwExecuted());
		xhbDefCaseValue.setSpecialCirFound(defCaseBean.getSpecialCirFound());

		if ( null != defCaseBean.getDateExported() && !"".equals(defCaseBean.getDateExported())) {
			log.debug("updateDefOnCaseDeportation Date Exported will be set to " + defCaseBean.getDateExported());
			xhbDefCaseValue.setDateExported(defCaseBean.getDateExported());
		}
		
		xhbDefCaseValue.setNationality(defCaseBean.getNationality());
		xhbDefCaseValue.setFirstFixedTrial(defCaseBean.getFirstFixedTrial());
		xhbDefCaseValue.setFirstHearingType(defCaseBean.getFirstHearingType());

		xhbDefCaseValue.setPublicDisplayHide(defCaseBean.getPublicDisplayHide());
		xhbDefCaseValue.setAmendedDateExported(defCaseBean.getAmendedDateExported());
		xhbDefCaseValue.setAmendedReason(defCaseBean.getAmendedReason());
		xhbDefCaseValue.setHateInd(defCaseBean.getHateInd());

		xhbDefCaseValue.setHateType(defOnCaseBasicValue.getHateType());
		xhbDefCaseValue.setHateSentInd(defOnCaseBasicValue.getHateSentIndicator());
		xhbDefCaseValue.setDrivingDisqSuspendedDate(defCaseBean.getDrivingDisqSuspendedDate());
		xhbDefCaseValue.setMagCourtFirstHearingDate(defCaseBean.getMagCourtFirstHearingDate());

		xhbDefCaseValue.setMagCourtFinalHearingDate(defCaseBean.getMagCourtFinalHearingDate());
		xhbDefCaseValue.setCustodyTimeLimit(defCaseBean.getCustodyTimeLimit());
		xhbDefCaseValue.setFormNgSentDate(defCaseBean.getFormNgSentDate());
		xhbDefCaseValue.setCacdAppealResultDate(defCaseBean.getCacdAppealResultDate());

		xhbDefCaseValue.setSection28Name1(defCaseBean.getSection28Name1());
		xhbDefCaseValue.setSection28Name2(defCaseBean.getSection28Name2());
		xhbDefCaseValue.setSection28Phone1(defCaseBean.getSection28Phone1());
		xhbDefCaseValue.setSection28Phone2(defCaseBean.getSection28Phone2());

		updateHateCrime(defOnCaseBasicValue, userDisplayName);
		
		updateAggravatingReasons(defOnCaseBasicValue, userDisplayName);

		if (defCaseBean.getDefendantOnCaseId() != null && defCaseBean.getDefendantOnCaseId() != 0) {
			defCaseBean = XhbDefendantOnCaseBeanHelper2.updateLocal(xhbDefCaseValue);
			defCaseBean.setLastUpdatedBy(userDisplayName);
		}

	}

	/**
	 * 
	 * update xhb_hate_sentence table.
	 *
	 * @param xhbHateSentenceBV
	 *            XhbHateSentencingBasicValue
	 * @param userDisplayName
	 *            String
	 * 
	 * @throws ChargeControllerException
	 * 
	 * 
	 */
	private void updateHateCrime(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName) throws ChargeControllerException {

		// Current values are:

		XhbHateSentencingBasicValue[] xhbsvListOfCurrent = XhbHateSentencingBeanHelper2
				.findNonObsoleteByDefOnCaseIdValue(defOnCaseBasicValue.getDefendantOnCaseId());
		XhbHateSentencingBasicValue xhbHateSentenceBV = new XhbHateSentencingBasicValue();
		xhbHateSentenceBV.setDefendantOnCaseId(defOnCaseBasicValue.getDefendantOnCaseId());

		// GeneralDisability - Ref = 1
		if (defOnCaseBasicValue.getGeneralDisability()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 1, xhbHateSentenceBV)) {
				createHateSentenceBV(xhbHateSentenceBV, 1, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 1, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 1, userDisplayName);
			}
		}

		// VictimDisability - Ref = 2
		if (defOnCaseBasicValue.getVictimDisability()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 2, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 2, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 2, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 2, userDisplayName);
			}
		}

		// GeneralSexual - Ref = 3
		if (defOnCaseBasicValue.getRacialAggravated()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 3, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 3, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 3, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 3, userDisplayName);
			}
		}

		// VictimSexual - Ref = 4
		if (defOnCaseBasicValue.getRaceAndReligionAggravated()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 4, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 4, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 4, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 4, userDisplayName);
			}
		}

		// RacialAggravated - Ref = 5
		if (defOnCaseBasicValue.getReligionAggravated()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 5, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 5, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 5, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 5, userDisplayName);
			}
		}

		// ReligionAggravated - Ref = 6
		if (defOnCaseBasicValue.getGeneralSexual()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 6, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 6, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 6, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 6, userDisplayName);
			}
		}

		// RaceAndReligionAggravated - Ref = 7
		if (defOnCaseBasicValue.getVictimSexual()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 7, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 7, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 7, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 7, userDisplayName);
			}
		}

		// GeneralTransgender - Ref = 8
		if (defOnCaseBasicValue.getGeneralTransgender()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 8, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 8, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 8, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 8, userDisplayName);
			}
		}

		// VictimTransgender - Ref = 9
		if (defOnCaseBasicValue.getVictimTransgender()) {
			if (!hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 9, xhbHateSentenceBV)) {
				// We know user has selected it so add it!!
				createHateSentenceBV(xhbHateSentenceBV, 9, userDisplayName);
			}
		} else { // Its now been de-selected, so check whether it needs to be
					// deleted
			if (hateCrimeAlreadyExistsInDB(xhbsvListOfCurrent, 9, xhbHateSentenceBV)) {
				updateHateSentenceBV(xhbHateSentenceBV, 9, userDisplayName);
			}
		}
	}

	private void updateHateSentenceBV(XhbHateSentencingBasicValue xhbHateSentenceBV, Integer refHateSentTypeId, String userDisplayName) {

		xhbHateSentenceBV.setObsInd("Y");
		xhbHateSentenceBV.setRefHateSentTypeId(refHateSentTypeId);
		XhbHateSentencing xhbHateSentenceBean = XhbHateSentencingBeanHelper2.updateLocal(xhbHateSentenceBV);
		xhbHateSentenceBean.setLastUpdatedBy(userDisplayName);
	}

	private void createHateSentenceBV(XhbHateSentencingBasicValue xhbHateSentenceBV, Integer refHateSentTypeId, String userDisplayName) {

		xhbHateSentenceBV.setRefHateSentTypeId(refHateSentTypeId);
		XhbHateSentencing xhbHateSentenceBean = XhbHateSentencingBeanHelper2.createLocal(xhbHateSentenceBV);
		xhbHateSentenceBean.setCreatedBy(userDisplayName);
		xhbHateSentenceBean.setLastUpdatedBy(userDisplayName);
	}

	/**
	 * Check to see whether a a refhatesentencing type exists in the list of
	 * rows returned
	 * 
	 * @param currentEntries
	 * @param refHateSentencingType
	 * @return
	 */
	private boolean hateCrimeAlreadyExistsInDB(XhbHateSentencingBasicValue[] currentEntries, int refHateSentencingType,
			XhbHateSentencingBasicValue xhbHateSentenceBV) {
		boolean alreadyExists = false;

		for (int i = 0; i < currentEntries.length; i++) {
			if ((currentEntries[i] != null) && (currentEntries[i].getRefHateSentTypeId() != null)
					&& (currentEntries[i].getRefHateSentTypeId().intValue() == refHateSentencingType)) {
				alreadyExists = true;
				xhbHateSentenceBV.setCreatedBy(currentEntries[i].getCreatedBy());
				xhbHateSentenceBV.setCreationDate(currentEntries[i].getCreationDate());
				xhbHateSentenceBV.setHateSentencingId(currentEntries[i].getHateSentencingId());
				xhbHateSentenceBV.setVersion(currentEntries[i].getVersion());
				break;
			} else {
				xhbHateSentenceBV.setCreatedBy(currentEntries[i].getCreatedBy());
				xhbHateSentenceBV.setCreationDate(currentEntries[i].getCreationDate());
				xhbHateSentenceBV.setHateSentencingId(currentEntries[i].getHateSentencingId());
				xhbHateSentenceBV.setVersion(currentEntries[i].getVersion());
				xhbHateSentenceBV.setObsInd(null);
			}
		}

		return alreadyExists;
	}

	private void updateAggravatingReasons(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName) throws ChargeControllerException {
		Integer defendantOnCaseId = defOnCaseBasicValue.getDefendantOnCaseId();
		if (defendantOnCaseId != null) {
			Collection<RefAggravatingReasons> refAggravatingReasons;
			try {
				refAggravatingReasons = getRefAggravatingReasonsMaintainer().findAllNonObsolete();
			} catch (FinderException ex) {
				refAggravatingReasons = null;
			}
			if (refAggravatingReasons != null) { 
				for (RefAggravatingReasons refAggravatingReason : refAggravatingReasons) {
					RefAggravatingReasonsBasicValue refAggravatingReasonBV = getRefAggravatingReasonsMaintainer().getBasicValue(refAggravatingReason);
					Integer refAggravatingReasonsId = refAggravatingReasonBV.getRefAggravatingReasonsId();
					
					boolean isSelected = false;
					if (refAggravatingReasonBV.isAssaultOnWorkers()) {
						isSelected = defOnCaseBasicValue.isAggravatingAssaultOnWorkers();
					} else if (refAggravatingReasonBV.isTerroristConnection()) {
						isSelected = defOnCaseBasicValue.isAggravatingTerroristConnection();
					} else if (refAggravatingReasonBV.isEmergencyWorkers()) {
						isSelected = defOnCaseBasicValue.isAggravatingEmergencyWorkers();
					} else if (refAggravatingReasonBV.isHostility()) {
						isSelected = defOnCaseBasicValue.isAggravatingHostility();
					} else if (refAggravatingReasonBV.isSexualOrientation()) {
						isSelected = defOnCaseBasicValue.isAggravatingSexualOrientation();
					} else if (refAggravatingReasonBV.isSexualOrientationOfVictim()) {
						isSelected = defOnCaseBasicValue.isAggravatingSexualOrientationOfVictim();
					} else if (refAggravatingReasonBV.isTransgender()) {
						isSelected = defOnCaseBasicValue.isAggravatingTransgender();
					} else if (refAggravatingReasonBV.isTransgenderOfVictim()) {
						isSelected = defOnCaseBasicValue.isAggravatingTransgenderOfVictim();
					} else {
						log.error("RefAggravantingReasonsId "+refAggravatingReasonsId+" unknown type");
						continue;
					}
					
					AggravatingReasonsBasicValue aggravatingReasonsBV = getAggravatingReasonBV(defendantOnCaseId, refAggravatingReasonsId);
					aggravatingReasonsBV.setObsInd(isSelected ? null : OBSOLETE);
					if (aggravatingReasonsBV.getAggravatingReasonsId() == null) {
						// Create
						try {
							if (!OBSOLETE.equals(aggravatingReasonsBV.getObsInd())) {
								getAggravatingReasonsMaintainer().create(aggravatingReasonsBV, userDisplayName);
							}
						} catch (Exception e) {
							log.error("Error occurred in aggravatingReasons.create " + e);
							ctx.setRollbackOnly();
							CSServices.getDefaultErrorHandler().handleError(e, getClass());
						}
					} else {
						// Update
						try {
							getAggravatingReasonsMaintainer().update(aggravatingReasonsBV, userDisplayName);
						} catch (ObjectNotFoundException e) {
							log.error("Error occurred in aggravatingReasons.update " + e);
							ctx.setRollbackOnly();
							CSServices.getDefaultErrorHandler().handleError(e, getClass());
						}
					}
				}
			}
		}
	}
	
	private AggravatingReasonsBasicValue getAggravatingReasonBV(Integer defendantOnCaseId, Integer refAggravatingReasonsId) {
		AggravatingReasonsBasicValue basicValue = null;
		try {
			Collection<AggravatingReasons> locals = getAggravatingReasonsMaintainer().findByDefOnCaseIdAndRefAggId(defendantOnCaseId, refAggravatingReasonsId);
			if (locals != null) {
				// If somethings gone wrong and we have multiple, use the original one
				for (AggravatingReasons local : locals) {
					basicValue = getAggravatingReasonsMaintainer().getBasicValue(local);
					break;
				}
			}
		} catch (FinderException ex) {
			basicValue = null;
		}
		if (basicValue == null) {
			basicValue = new AggravatingReasonsBasicValue();
			basicValue.setDefendantOnCaseId(defendantOnCaseId);
			basicValue.setRefAggravatingReasonsId(refAggravatingReasonsId);
		}
		return basicValue;
	}
	
	private RefAggravatingReasonsMaintainer getRefAggravatingReasonsMaintainer() { 
		if (refAggravatingReasonsMaintainer == null) {
			refAggravatingReasonsMaintainer = new RefAggravatingReasonsMaintainer();
		}
		return refAggravatingReasonsMaintainer;
	}
	
	private AggravatingReasonsMaintainer getAggravatingReasonsMaintainer() { 
		if (aggravatingReasonsMaintainer == null) {
			aggravatingReasonsMaintainer = new AggravatingReasonsMaintainer();
		}
		return aggravatingReasonsMaintainer;
	}
	
	
	/**
	 * save defendant Entity in db with new values.
	 * 
	 * @param defendant
	 *            DefendantValue
	 * @param caseStatusValue
	 *            CaseStatusValue
	 * @param userDisplayName
	 *            String
	 * @return defendantValue
	 * 
	 * @throws DefendantControllerException
	 */

	private XhbDefendant saveDefendantEntity(DefendantValue defendant, CaseStatusValue caseStatusValue, String userDisplayName)
			throws DefendantControllerException {

		DefendantHelper defHelper = new DefendantHelper();

		XhbDefendant defBean = null;
		XhbDefendantOnCase defCaseBean = null;
		XhbAddress addBean = null;
		try {

			// ctx-1641 Update the defendant table with direct database update

			defBean = XhbDefendantBeanHelper2.findByPrimaryKey(defendant.getDefendantID());
			XhbDefendantBasicValue xhbDefBasicValue = updateDefendantValues(defendant, defBean);

			if (defBean.getDefendantId() != null && defBean.getDefendantId() != 0) {
				defBean = XhbDefendantBeanHelper2.updateLocal(xhbDefBasicValue);
				defBean.setLastUpdatedBy(userDisplayName);
			} else {
				defBean = XhbDefendantBeanHelper2.createLocal(xhbDefBasicValue);
				defBean.setCreatedBy(userDisplayName);
				defBean.setLastUpdatedBy(userDisplayName);
			}

			// ctx-1640 Update the address table with direct database update

			if (defendant.getAddressValue() != null) {
				addBean = saveAddressEntity(defendant.getAddressValue(), userDisplayName);
				xhbDefBasicValue.setAddressId(addBean.getAddressId());
				defBean.setXhbAddress(addBean);
			}

			// ctx-1642 Update the defendantOnCase table with direct database
			// update

			defCaseBean = XhbDefendantOnCaseBeanHelper2
					.findByPrimaryKey(defendant.getDefOnCaseBasicValue().getDefendantOnCaseId());
			XhbDefendantOnCaseBasicValue xhbDefCaseValue = updateDefendantOnCase(defendant, defCaseBean);

			// ctx-1646 update difference report field in xhb_def_on_case table.

			xhbDefCaseValue.setDifferenceReport("Y");

			if (defCaseBean.getDefendantOnCaseId() != null && defCaseBean.getDefendantOnCaseId() != 0) {
				defCaseBean = XhbDefendantOnCaseBeanHelper2.updateLocal(xhbDefCaseValue);
				defCaseBean.setLastUpdatedBy(userDisplayName);
			} else {
				defCaseBean = XhbDefendantOnCaseBeanHelper2.createLocal(xhbDefCaseValue);
				defCaseBean.setCreatedBy(userDisplayName);
				defCaseBean.setLastUpdatedBy(userDisplayName);
			}

			defHelper.createCourtLogEntry(defendant, caseStatusValue);
			createAuditEvent(defendant);

			// Update the public display hide fields which are local
			// to Xhibit.
			defHelper.updatePublicDisplayHideSettingsAux(defendant, caseStatusValue);

			// Tell the Public Displays that the defendant has changed
			defHelper.notifyNewPublicDisplays(caseStatusValue, userDisplayName);

		} catch (CourtLogBusinessException e) {
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);

		}
		return defBean;

	}

	/**
	 * ctx-1643
	 * 
	 * Update the defendant details. This will only be udpated in the xhibit
	 * database.
	 * 
	 * 
	 * @param defendant
	 *            DefendantValue
	 * @param caseStatusValue
	 *            caseStatusValue
	 * @param userDisplayName
	 *            String
	 * @throws DefendantControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */

	public void updateDefendant(DefendantValue defendant, CaseStatusValue caseStatusValue, String userDisplayName)
			throws DefendantControllerException {

		saveDefendantEntity(defendant, caseStatusValue, userDisplayName);
	}

	private XhbDefendantBasicValue updateDefendantValues(DefendantValue defendant, XhbDefendant defBean) {

		XhbDefendantBasicValue xhbDefBasicValue = new XhbDefendantBasicValue();

		xhbDefBasicValue.setVersion(defendant.getVersion());

		xhbDefBasicValue.setDefendantId(defendant.getDefendantID());
		xhbDefBasicValue.setFirstName(defendant.getFirstName());
		xhbDefBasicValue.setSurname(defendant.getSurName());
		xhbDefBasicValue.setMiddleName(defendant.getMiddleName());
		xhbDefBasicValue.setCourtId(defendant.getCourtID());
		xhbDefBasicValue.setGender(defendant.getGender().byteValue());
		if (defendant.getLastConvictionDate() != null) {
			xhbDefBasicValue.setLastConvictionDate(defendant.getLastConvictionDate().getTime());
		}
		xhbDefBasicValue.setGender(defendant.getGender().byteValue());
		if (defendant.getDateOfBirth() != null) {
			xhbDefBasicValue.setDateOfBirth(defendant.getDateOfBirth().getTime());
		}
		if (defendant.getGender().byteValue() == 0) {
			xhbDefBasicValue.setIsCompany("Y");
		} else {
			xhbDefBasicValue.setIsCompany("N");
		}
		xhbDefBasicValue.setInitials(defendant.getInitials());

		xhbDefBasicValue.setCreationDate(defBean.getCreationDate());
		xhbDefBasicValue.setCreatedBy(defBean.getCreatedBy());
		xhbDefBasicValue.setCrestDefendantId(defBean.getCrestDefendantId());
		xhbDefBasicValue.setEthnicAppearanceCode(defBean.getEthnicAppearanceCode());
		xhbDefBasicValue.setEthnicitySelfDefined(defBean.getEthnicitySelfDefined());
		xhbDefBasicValue.setParentGuardianName(defBean.getParentGuardianName());
		xhbDefBasicValue.setCurrentPrisonStatus(defBean.getCurrentPrisonStatus());
		xhbDefBasicValue.setPrisonId(defBean.getPrisonId());
		xhbDefBasicValue.setPublicDisplayHide(defBean.getPublicDisplayHide());
		return xhbDefBasicValue;
	}

	/**
	 * update defendantBasicValue bean with updated values.
	 * 
	 * @param defendant
	 *            DefendantValue
	 * @param defCaseBean
	 *            defendantOnCaseBean
	 * @param defBean
	 *            DefendantBean
	 * @return defendantOnCaseBasicValue
	 */
	private XhbDefendantOnCaseBasicValue updateDefendantOnCase(DefendantValue defendant,
			XhbDefendantOnCase defCaseBean) {

		XhbDefendantOnCaseBasicValue xhbDefCaseValue = new XhbDefendantOnCaseBasicValue();

		xhbDefCaseValue.setCaseId(defendant.getDefOnCaseBasicValue().getCaseID());
		xhbDefCaseValue.setDefendantOnCaseId(defendant.getDefOnCaseBasicValue().getDefendantOnCaseId());
		xhbDefCaseValue.setDefendantId(defendant.getDefOnCaseBasicValue().getDefendantID());

		xhbDefCaseValue.setPtiurn(defendant.getDefOnCaseBasicValue().getPtiurn());
		xhbDefCaseValue.setAsn(defendant.getDefOnCaseBasicValue().getAsn());
		xhbDefCaseValue.setIsMasked(defendant.getDefOnCaseBasicValue().getIsMasked());
		xhbDefCaseValue.setMaskedName(defendant.getDefOnCaseBasicValue().getMaskedName());
		xhbDefCaseValue.setIsJuvenile(defendant.getDefOnCaseBasicValue().getIsJuvenile());
		xhbDefCaseValue.setNationality(defendant.getDefOnCaseBasicValue().getNationality());

		xhbDefCaseValue.setCustodial(defendant.getDefOnCaseBasicValue().getCustodial());
		xhbDefCaseValue.setSuspended(defendant.getDefOnCaseBasicValue().getSuspended());
		xhbDefCaseValue.setRecommendedDeportation(defendant.getDefOnCaseBasicValue().getRecommendedDeportation());
		xhbDefCaseValue.setSeriousDrugOffence(defendant.getDefOnCaseBasicValue().getSeriousDrugOffence());

		xhbDefCaseValue.setVersion(defendant.getDefOnCaseBasicValue().getVersion());
		xhbDefCaseValue.setCreationDate(defCaseBean.getCreationDate());
		xhbDefCaseValue.setCreatedBy(defCaseBean.getCreatedBy());

		xhbDefCaseValue.setHateInd(defendant.getDefOnCaseBasicValue().getHateIndicator());
		xhbDefCaseValue.setHateType(defendant.getDefOnCaseBasicValue().getHateType());
		xhbDefCaseValue.setHateSentInd(defendant.getDefOnCaseBasicValue().getHateSentIndicator());
		xhbDefCaseValue.setPncId(defCaseBean.getPncId());

		// ctx-1646 update difference report field in xhb_def_on_case table.

		xhbDefCaseValue.setDifferenceReport("Y");

		if (defendant.getDefOnCaseBasicValue().getAmendedDateExported() != null) {
			xhbDefCaseValue
					.setAmendedDateExported(defendant.getDefOnCaseBasicValue().getAmendedDateExported().getTime());
		}

		xhbDefCaseValue.setAmendedReason(defendant.getDefOnCaseBasicValue().getAmendedReason());
		xhbDefCaseValue.setBcStatusBwExecuted(defendant.getDefOnCaseBasicValue().getBcStatusBwExecuted());
		xhbDefCaseValue.setBenchWarrantExecDate(defendant.getDefOnCaseBasicValue().getBenchWarrantExecDate());
		xhbDefCaseValue.setCollectMagistrateCourtId(defendant.getDefOnCaseBasicValue().getCollectMagistrateCourtId());
		xhbDefCaseValue.setCustodyTimeLimit(defendant.getDefOnCaseBasicValue().getCustodyTimeLimit());

		if (defendant.getDefOnCaseBasicValue().getDateExported() != null) {
			xhbDefCaseValue.setDateExported(defendant.getDefOnCaseBasicValue().getDateExported().getTime());
		}

		if (defendant.getDefOnCaseBasicValue().getDateOfCommittal() != null) {
			xhbDefCaseValue.setDateOfCommittal(defendant.getDefOnCaseBasicValue().getDateOfCommittal().getTime());
		}
		xhbDefCaseValue.setCommBcStatus(defendant.getDefOnCaseBasicValue().getCommBcStatus());
		xhbDefCaseValue.setCurrentBcStatus(defendant.getDefOnCaseBasicValue().getCurrentBcStatus());
		xhbDefCaseValue.setDefendantNumber(defendant.getDefOnCaseBasicValue().getDefendantNumber());

		xhbDefCaseValue.setDrivingDisqSuspendedDate(defendant.getDefOnCaseBasicValue().getDrivingDisqSuspendedDate());

		if (defendant.getDefOnCaseBasicValue().getFinalDrivingLicenceStatus() != null) {
			xhbDefCaseValue.setFinalDrivingLicenceStatus(
					defendant.getDefOnCaseBasicValue().getFinalDrivingLicenceStatus().byteValue());
		}

		xhbDefCaseValue.setMagCourtFinalHearingDate(defendant.getDefOnCaseBasicValue().getMagCourtFinalHearingDate());
		xhbDefCaseValue.setMagCourtFirstHearingDate(defendant.getDefOnCaseBasicValue().getMagCourtFirstHearingDate());

		// ctx-2830 - Missing fields
		xhbDefCaseValue.setNoOfTics(defendant.getDefOnCaseBasicValue().getNoOfTICs() != null
				? defendant.getDefOnCaseBasicValue().getNoOfTICs().shortValue() : null);
		xhbDefCaseValue.setSection28Name1(defendant.getDefOnCaseBasicValue().getSection28Name1());
		xhbDefCaseValue.setSection28Name2(defendant.getDefOnCaseBasicValue().getSection28Name2());
		xhbDefCaseValue.setSection28Phone1(defendant.getDefOnCaseBasicValue().getSection28Phone1());
		xhbDefCaseValue.setSection28Phone2(defendant.getDefOnCaseBasicValue().getSection28Phone2());
		xhbDefCaseValue.setCoaStatus(defendant.getDefOnCaseBasicValue().getCoaStatus());
		xhbDefCaseValue.setCacdAppealResult(defendant.getDefOnCaseBasicValue().getCacdAppealResult());
		xhbDefCaseValue.setDateReceiptNoticeAppeal(defendant.getDefOnCaseBasicValue().getDateReceiptNoticeAppeal());
		xhbDefCaseValue.setDifferenceReport(defendant.getDefOnCaseBasicValue().getDifferenceReport());
		xhbDefCaseValue.setCacdAppealResultDate(defendant.getDefOnCaseBasicValue().getCacdAppealResultDate());
		xhbDefCaseValue.setFormNgSentDate(defendant.getDefOnCaseBasicValue().getFormNgSentDate());
		xhbDefCaseValue.setPublicDisplayHide(defendant.getDefOnCaseBasicValue().getPublicDisplayHide());
		xhbDefCaseValue.setResultsVerified(defendant.getDefOnCaseBasicValue().getResultsVerified());

		return xhbDefCaseValue;

	}

	/**
	 * Update the defendant on case details only. This will only be udpated in
	 * the xhibit database.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defOnCaseValue
	 *            DefendantOnCaseValue
	 * @throws DefendantControllerException
	 */
	public void updateDefendantOnCaseDetails(DefendantOnCaseValue defOnCaseValue, String userDisplayName)
			throws DefendantControllerException {
		final DefendantOnCaseHelper defendantOnCaseHelper = new DefendantOnCaseHelper();
		log.debug("*** updateDefendantOnCaseDetails(" + defOnCaseValue + ") called ***");
		try {
			defendantOnCaseHelper.updateDefendantOnCaseDetails(defOnCaseValue, userDisplayName);
		} catch (DefendantControllerException dex) {
			ctx.setRollbackOnly();
			log.debug("updateDefendantOnCaseDetails() Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(dex, DefendantControllerBean.class);
			throw dex;
		}
	}

	private void createAuditEvent(DefendantValue defendant) {
		Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
		hashtable.put(DefendantValue.class.getName(), defendant);
		AuditTrailService auditService = CSServices.getAuditTrailService();
		AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
		event.setSuccess(true);
		auditService.createAuditRecord(event);
	}

	/**
	 * This method updates the offence passed as parameter.
	 * 
	 * @param offenceValue
	 *            OffenceValue
	 * @param userDisplayName
	 *            userDisplayName
	 * @throws ChargeControllerException
	 * @throws ResultsFoundException
	 *             if the Offence has results and the deleteResults flag has not
	 *             been set in the OffenceValue
	 * @roseuid 3DAFF267031F
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateOffence(OffenceValue offenceValue, String userDisplayName) throws ChargeControllerException, ResultsFoundException {
		updateOffenceAux(offenceValue, userDisplayName);
	}

	private void updateOffenceAux(OffenceValue offenceValue, String userDisplayName) throws ChargeControllerException, ResultsFoundException {
		// Determine if Offence AddressValues need adding.
		if (offenceValue.getAddressValue() != null) {
			offenceValue.getAddressValue().setDirty(true);
		}

		this.updateJoinderIndictments(offenceValue, userDisplayName);
	}

	/**
	 * This method updates the offence and/or breach passed as parameter.
	 * 
	 * @throws ChargeControllerException
	 * @throws ResultsFoundException
	 *             if the Offence has results and the deleteResults flag has not
	 *             been set in the OffenceValue
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateBailActOffence(OffenceValue offenceValue, boolean offenceChanged, BreachValue breachValue,
			boolean breachChanged, String userDisplayName) throws ChargeControllerException, ResultsFoundException {

		if (offenceChanged) {
			updateOffenceAux(offenceValue, userDisplayName);
		}

		if (breachChanged) {
			// PR6054 dont create a log entry for the breach change
			boolean createCourtLogEntry = false;
			updateBreachInternal(breachValue, createCourtLogEntry, true);
		}

		if (!offenceChanged && breachChanged) {
			// If the offence is unchanged but the breach has changed
			// because the 'original order data' value has been edited
			// then create an 'Ammend Offence' court log entry because
			// the user will expect to see some court log entry when they
			// change the 'original order data'.
			try {
				offenceValue.setCourtLogDate(Calendar.getInstance());
				eventHelper.updateOffenceLogEntry(offenceValue);
			} catch (CourtLogBusinessException ex) {
				handleWrapAndRethrowException(ex);
			}
		}
	}

	/**
	 * This method is called to update an existing defendant on offence with new
	 * details.
	 * 
	 * @param defendantOnOffenceValue
	 * @param userDisplayName
	 * @ejb.interface-method view-type="remote"
	 */
	@SuppressWarnings("unchecked")
	public void updateDefendantOnOffence(LinkCountDefValue linkCountDefValue, String userDisplayName) throws ChargeControllerException {
		log.debug("updateDefendantOnOffence: START");
		if (linkCountDefValue != null) {
			log.debug("linkCountDefValue!= null");

			// ctx-322 remove call to mercator api and update DB directly
			saveDefendantOnOffenceEntities(linkCountDefValue.getDefendantOnOffenceValues(), userDisplayName);

			log.debug("updateDefendantOnOffence called");

		}
		log.debug("updateDefendantOnOffence: START");
	}

	/**
	 * @param offenceValue
	 * @param userDisplayName
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateOffenceAndDefOnOffence(OffenceValue offenceValue, Integer defendantId, String userDisplayName) throws ChargeControllerException {
		if (offenceValue != null) {
			offenceValue.setDirty(true);
			
 			try {
				updateOffenceInternal(offenceValue, userDisplayName);
				DefendantOnOffenceComplexValue defComplexVal = offenceValue.getDefendantOnOffence(defendantId);
 				XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(offenceValue.getOffenceID());
				DefendantOnOffenceValue defendantOnOffenceValue = new DefendantOnOffenceValue(
						offenceBean.getOffenceId(), defendantId, null);
				
				if (defComplexVal.getArrestDate() != null) {
					Calendar arrestDate = Calendar.getInstance();
					arrestDate.setTime(defComplexVal.getArrestDate());
					defendantOnOffenceValue.setDateOfArrest(arrestDate);
				}
				
				if (defComplexVal.getChargeDate() != null) {
					Calendar chargeDate = Calendar.getInstance();
					chargeDate.setTime(defComplexVal.getChargeDate());
					defendantOnOffenceValue.setDateOfCharge(chargeDate);
				}
				
				if (defComplexVal.getSeqNo() != null) {
					defendantOnOffenceValue.setSequenceNo(defComplexVal.getSeqNo());
				}
				if (defComplexVal.getIsCommittedOnBail() != null) {
					defendantOnOffenceValue.setIsCommittedOnBail(defComplexVal.getIsCommittedOnBail());
				}
				if (defComplexVal.getInterimD20() != null) {
					defendantOnOffenceValue.setInterimD20(defComplexVal.getInterimD20());
				}
		
				saveDefendantOnOffenceEntity(offenceBean, defendantOnOffenceValue, userDisplayName);
  			} catch (ResultsFoundException e) {
          handleWrapAndRethrowException(e);
			}			  
		}
	}

	private void updateOffenceInternal(OffenceValue offenceValue, String userDisplayName)
			throws ChargeControllerException, ResultsFoundException {
		if (log.isDebugEnabled()) {
			log.debug("start method: updateOffence()");
		}

		// Check for results if deleteResults flag is false
		if (!offenceValue.isDeleteResults() && !offenceValue.isUpdatingAdditionalInfo()) {
			ChargeHelper.checkResultsForOffence(offenceValue.getOffenceID());
		}

		try {
			if (!offenceValue.isUpdatingAdditionalInfo())
				eventHelper.updateOffenceLogEntry(offenceValue);

			// ctx-290, ctx-300, ctx-358, ctx-371 Update the offence with direct
			// database update
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(offenceValue.getChargeID());
			saveOffenceEntity(chargeBean, offenceValue, userDisplayName);

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	/**
	 * This method returns a list of charges including those marked as obsolete
	 * because they have been logically deleted.
	 * 
	 * @param caseID
	 * @return Collection
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getChargesList(Integer caseID) throws ChargeControllerException {
		return new GetChargesHelper().getChargesList(caseID);
	}

	/**
	 * @param caseID
	 *            Integer
	 * @param chargeLogRequired
	 *            true if you require the charge log events. Should only be for
	 *            the charges screen.
	 * @return uk.gov.courtservice.xhibit.business.vos.services.charge.
	 *         ChargeCompositeValue
	 * @throws ChargeControllerException
	 * @roseuid 3DBD3D69030B
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public ChargeCompositeValue getCharges(Integer caseID, boolean chargeLogRequired) throws ChargeControllerException {
		return new GetChargesHelper().getCharges(caseID, chargeLogRequired);
	}

	/**
	 * 
	 * @param breachID
	 * @return
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public BreachValue getBreachValue(Integer breachID) throws ChargeControllerException {
		// return new BreachHelper().getBreachValue(breachID);
		return breachHelper.getBreachValue(breachID);
	}

	/**
	 * Exports all the charge information associated with this case for
	 * distribution via the CJSE.
	 * 
	 * @param caseID
	 *            Integer
	 * @throws ChargeControllerException
	 * @throws ExportChargeInProgressException
	 *             if the charges are already being exported at the time the
	 *             mothod is called
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void exportCharges(Integer caseID, Integer scheduledHearingID)
			throws ChargeControllerException, ExportChargeInProgressException {
		if (log.isDebugEnabled()) {
			log.debug("start method: exportCharges(caseID) with caseID = " + caseID);
		}

		try {
			XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(caseID);

			if (!hasCharges(caseID, ChargeTypes.INDICTMENT.getChargeType())) {
				Integer caseNumber = caseBean.getCaseNumber();
				throw new NoChargesToExportException("chargecontroller.nochargestoexport", new Object[] { caseNumber },
						"Case number " + caseNumber + " has no charges to export");
			}
			// if export in progress throw exception
			if (CaseBasicValue.EXPORTING.equals(caseBean.getExportCharges()))
				throw new ExportChargeInProgressException();

			// export any joinder indictments to joinderXml table
			new JoinderIndictmentHelper().exportJoinderIndictment(caseID, new GetChargesHelper());

			// set export charges flag to ready and update
			XhbCaseBasicValue caseBV = caseBean.getData();
			caseBV.setExportCharges(CaseBasicValue.READY);
			XhbCaseBeanHelper2.update(caseBV);

			// CR46: court log for export indictments
			try {
				eventHelper.logExportIndictments(caseID, scheduledHearingID);
			} catch (CourtLogBusinessException ex) {
				log.error(ex.getMessage());
			}
		} catch (XhbCaseBeanNotFoundException e) {
			ctx.setRollbackOnly();
			log.debug("exportCharges: Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new ChargeControllerException("chargecontroller.casenotfound", new Object[] { caseID },
					"Error getting charges, case not found. CaseID = " + caseID, e);
		}
	}

	/**
	 * This method is used to updates offences on joinder indictments. It will
	 * update all offences accross joined charges that match the input offences
	 * crestSequencenumber.
	 * 
	 * @param joinderIndictment
	 *            the OffenceValue to update
	 * @param userDisplayName
	 *            userDisplayName
	 * @throws ChargeControllerException
	 * @throws ResultsFoundException
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public void updateJoinderIndictments(OffenceValue joinderIndictment, String userDisplayName)
			throws ChargeControllerException, ResultsFoundException {
		// call through to helper to retreive all joined offence is

		UpdateJoinderIndictmentHelper updateJoinderHelper = new UpdateJoinderIndictmentHelper();
		Collection<Integer> joinedOffenceIds = updateJoinderHelper.getJoinedOffenceIds(joinderIndictment);

		for (Integer offenceId : joinedOffenceIds) {
			// iterare though returned collection, create a new OffenceValue
			// and call updateOffence()

			joinderIndictment.setOffenceId(offenceId);

			updateOffenceInternal(joinderIndictment, userDisplayName);
		}
	}

	/**
	 * 
	 * @param joinderIndictment
	 * @throws ChargeControllerException
	 * @throws ResultsFoundException
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public void deleteJoinderIndictments(DelOffenceValue joinderIndictment)
			throws ChargeControllerException, ResultsFoundException {
		// call through to helper to retreive all joined offence is

		DeleteJoinderIndictmentHelper deleteJoinderHelper = new DeleteJoinderIndictmentHelper();
		Collection<Integer> joinedOffenceIds = deleteJoinderHelper.getJoinedOffenceIds(joinderIndictment);

		for (Integer offenceId : joinedOffenceIds) {
			// iterare though returned collection, create a new OffenceValue
			// and call updateOffence()

			joinderIndictment.setOffenceID(offenceId);

			deleteOffenceInternal(joinderIndictment, "XHIBIT");
		}
	}

	// TODO - Removed code once DB CRN sequence removed.
	/**
	 * 
	 * @param courtId
	 * @param batchSize
	 * @return
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CrnValue[] getNextCrnBatch(Integer courtId, int batchSize) throws ChargeControllerException {
		// String methodName = "getNextCrnBatch(Integer courtId, int
		// batchSize)";
		CrnHelper crnHelper = new CrnHelper();

		return crnHelper.getNextCrnBatch(courtId, batchSize);
	}

	// -------------------------- Private methods -------------------------

	/**
	 * This method returns the time difference between the prosecution papers
	 * served date and the current date (in number of days).
	 * 
	 * @param prosPapersServedDate
	 *            Date
	 * @return time difference in days
	 * @roseuid 3DB3F662008D
	 */
	private int checkProsPapersServedDate(Calendar prosPapersServedDate) {

		int totalDays = 0;

		// we're comparing days so zero all fields smaller than day
		Calendar todayCal = Calendar.getInstance();
		todayCal.set(Calendar.HOUR_OF_DAY, 0);
		todayCal.set(Calendar.MINUTE, 0);
		todayCal.set(Calendar.SECOND, 0);
		todayCal.set(Calendar.MILLISECOND, 0);

		prosPapersServedDate.set(Calendar.HOUR_OF_DAY, 0);
		prosPapersServedDate.set(Calendar.MINUTE, 0);
		prosPapersServedDate.set(Calendar.SECOND, 0);
		prosPapersServedDate.set(Calendar.MILLISECOND, 0);

		while (prosPapersServedDate.before(todayCal)) {
			prosPapersServedDate.add(Calendar.DATE, 1);
			totalDays++;
		}
		return totalDays;
	}

	private void doDeleteCharge(DelChargeValue delChargeVal, String chargeType, String userDisplayName)
			throws ChargeControllerException, ResultsFoundException {
		try {
			// Check for results if deleteResults flag is false
			if (!delChargeVal.isDeleteResults()) {
				ChargeHelper.checkResultsForCharge(delChargeVal.getChargeID());
			}

			eventHelper.deleteChargeCourtLog(delChargeVal, chargeType);

			// Get the beans
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(delChargeVal.getChargeID());

			// Delete the charge and child objects
			deleteChargeEntity(chargeBean);
			if (ChargeTypes.isBreachChargeType(chargeType)) {
				deleteBreachEntity(chargeBean.getXhbBreach());
			}
			deleteOffenceEntities(chargeBean, userDisplayName);

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		} catch (BisRefControllerException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	private void setDirtyFlags(ChargeValue chargeVal) {
		chargeVal.setDirty(true);
		Collection offenceValues = chargeVal.getOffenceValues();
		if (offenceValues != null && !offenceValues.isEmpty()) {
			Iterator offValIt = offenceValues.iterator();
			while (offValIt.hasNext()) {
				OffenceValue offenceVal = (OffenceValue) offValIt.next();
				offenceVal.setDirty(true);
			}
		}
	}

	private void logAllCounts(ChargeValue chargeValue, Integer chargeId, CourtLogCRUDValue logEntry)
			throws CourtLogBusinessException, ChargeControllerException {
		// need the Crest Offence Sequence Number for counts so
		// must look up the just created offences to find this
		try {
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(chargeId);
			Collection<XhbOffence> offences = chargeBean.getXhbOffences();
			Iterator<XhbOffence> offencesIt = offences.iterator();
			while (offencesIt.hasNext()) {
				XhbOffence offence = offencesIt.next();
				eventHelper.logRelatedCount(chargeValue.getChargeType(), logEntry, chargeId, offence.getOffenceId());
			}
		} catch (XhbChargeBeanNotFoundException ex) {
			ctx.setRollbackOnly();
			log.debug("addChargeToCase: Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			// the chargeID has come from the GUI so treat this as a
			// business exception
			throw new ChargeControllerException(CHARGE_NOT_FOUND, new Object[] { chargeId },
					"Could not find the charge specified with charge id" + chargeId, ex);
		}
	}

	/**
	 * Utility method to deal with handling Exceptions from other controllers,
	 * and wrapping and rethrowing them as ChargeControllerException
	 * 
	 * @param ex
	 *            The Exception to handle and rethrow, must be a
	 *            <code>CSRecoverableException</code> or child of
	 * @throws ChargeControllerException
	 *             the new Exception to throw
	 */
	private void handleWrapAndRethrowException(CSRecoverableException ex) throws ChargeControllerException {
		ctx.setRollbackOnly();
		CSServices.getDefaultErrorHandler().handleError(ex, getClass());
		if (ex.getUserMessageAsMessage().getParameters().length > 0) {
			throw new ChargeControllerException(ex.getUserMessageAsMessage().getKey(),
					ex.getUserMessageAsMessage().getParameters(), ex.getMessage(), ex);
		}

		throw new ChargeControllerException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
	}

	private boolean hasCharges(Integer caseId, String chargeType) {
		Collection charges = XhbChargeBeanHelper2.findNonObsoleteByCaseIdAndChargeType(caseId, chargeType);
		return (charges.size() > 0);
	}

	/**
	 * Create or get the existing row in the xhb_charge table with a charge type
	 * of 'G'.
	 * 
	 * @param caseId
	 * @param userDisplayName
	 * @return XhbCharge
	 */
	private XhbCharge getOriginalChargeEntity(Integer caseId, String userDisplayName) {
		// Get the case entity for this case
		XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(caseId);

		// Search for an existing original charge entity
		XhbCharge originalChargeBean = null;
		Iterator chargesIterator = caseBean.getXhbCharges().iterator();
		while (chargesIterator.hasNext()) {
			XhbCharge nextChargeBean = (XhbCharge) chargesIterator.next();
			if (!OBSOLETE.equals(nextChargeBean.getObsInd())
					&& ChargeTypes.ORIGINAL_CHARGE.getChargeType().equals(nextChargeBean.getChargeType())) {
				originalChargeBean = nextChargeBean;
				break;
			}
		}

		// Create an original charge entity if one does not exist
		if (originalChargeBean == null) {
			ChargeValue chargeValue = new ChargeValue(caseId, ChargeTypes.ORIGINAL_CHARGE);
			originalChargeBean = createChargeEntity(chargeValue, userDisplayName);
		}

		return originalChargeBean;
	}

	/**
	 * Create new row in xhb_charge table.
	 * 
	 * @param chargeValue
	 * @param userDisplayName
	 * @return XhbCharge
	 */
	private XhbCharge createChargeEntity(ChargeValue chargeValue, String userDisplayName) {
		XhbCharge chargeBean = null;

		log.debug("Create a new Charge with ChargeValue " + chargeValue);
		XhbChargeBasicValue chargeBasicValue = new XhbChargeBasicValue();

		// Common charge values
		chargeBasicValue.setCaseId(chargeValue.getCaseID());
		chargeBasicValue.setChargeType(chargeValue.getChargeType());

		// Optional charge values
		if (chargeValue.getProsPaperServedDate() != null) {
			chargeBasicValue.setProsPaperServedDate(chargeValue.getProsPaperServedDate().getTime());
		}
		if (chargeValue.getBreachValue() != null) {
			chargeBasicValue.setRefSystemCodeId(chargeValue.getBreachValue().getRefSystemCodeID());
		}

		// ctx-1834 code to update the refSystemCodeId.

		if (ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType().equals(chargeValue.getChargeType())) {

			Collection xhbRefSysCodeBV = XhbRefSystemCodeBeanHelper2.findHOProcCodeType("HO_PROC_SENT",
					chargeValue.getCourtID());

			Iterator refSystemIt = xhbRefSysCodeBV.iterator();
			while (refSystemIt.hasNext()) {
				XhbRefSystemCode refSystem = (XhbRefSystemCode) refSystemIt.next();
				chargeBasicValue.setRefSystemCodeId(refSystem.getRefSystemCodeId());
			}
		}
		// ctx-1835 code to update the refSystemCodeId.
		if (ChargeTypes.SECTION_41.getChargeType().equals(chargeValue.getChargeType())) {

			Collection xhbRefSysCodeBV = XhbRefSystemCodeBeanHelper2.findHOProcCodeType("HO_PROC_S41",
					chargeValue.getCourtID());

			Iterator refSystemIt = xhbRefSysCodeBV.iterator();
			while (refSystemIt.hasNext()) {
				XhbRefSystemCode refSystem = (XhbRefSystemCode) refSystemIt.next();
				chargeBasicValue.setRefSystemCodeId(refSystem.getRefSystemCodeId());
			}
		}
		if (ChargeTypes.INDICTMENT.getChargeType().equals(chargeValue.getChargeType())
				|| ChargeTypes.isBreachChargeType(chargeValue.getChargeType())) {
			chargeBasicValue.setCrestChargeSeqNo(
					getNextCrestChargeSeqNum(chargeValue.getCaseID(), chargeValue.getChargeType()));
		}

		// Create new row
		chargeBean = XhbChargeBeanHelper2.createLocal(chargeBasicValue);
		chargeBean.setCreatedBy(userDisplayName);
		chargeBean.setLastUpdatedBy(userDisplayName);

		// Optional post create bean processing
		if (ChargeTypes.INDICTMENT.getChargeType().equals(chargeValue.getChargeType())
				|| ChargeTypes.isBreachChargeType(chargeValue.getChargeType())) {
			chargeBean.setCrestChargeId(chargeBean.getChargeId());
		}

		return chargeBean;
	}

	/**
	 * Get the next sequence number for a particular charge type for a case,
	 * e.g. if there are already two records, the next sequence number is 3.
	 * 
	 * @param caseID
	 * @param chargeType
	 * @return next sequence number
	 */
	private Integer getNextCrestChargeSeqNum(Integer caseID, String chargeType) {
		int seqNum = 0;
		XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(caseID);
		Collection charges = caseBean.getXhbCharges();
		Iterator chargesIt = charges.iterator();
		while (chargesIt.hasNext()) {
			XhbCharge charge = (XhbCharge) chargesIt.next();
			if (!OBSOLETE.equalsIgnoreCase(charge.getObsInd()) && charge.getChargeType().equals(chargeType)
					&& charge.getCrestChargeSeqNo() != null && charge.getCrestChargeSeqNo().intValue() > seqNum) {
				seqNum = charge.getCrestChargeSeqNo().intValue();
			}
		}
		return new Integer(seqNum + 1);
	}

	/**
	 * Get the next sequence number for an offence for a particular charge, e.g.
	 * if there are already two records, the next sequence number is 3.
	 * 
	 * @param charge
	 * @return next sequence number
	 */
	private Integer getNextCrestOffenceSeqNum(XhbCharge charge) {
		int seqNum = 0;
		Collection offences = charge.getXhbOffences();
		Iterator offencesIt = offences.iterator();
		while (offencesIt.hasNext()) {
			XhbOffence offence = (XhbOffence) offencesIt.next();
			if (!OBSOLETE.equals(offence.getObsInd()) && offence.getCrestOffenceSeqNo() != null  
						&& offence.getCrestOffenceSeqNo().intValue() > seqNum)
				seqNum = offence.getCrestOffenceSeqNo().intValue();
		}
		return new Integer(seqNum + 1);
	}

	/**
	 * Update existing or create new row in xhb_address table.
	 * 
	 * @param addressValue
	 * @param userDisplayName
	 * @return XhbAddress
	 */
	private XhbAddress saveAddressEntity(AddressValue addressValue, String userDisplayName) {
		XhbAddressBasicValue addrBasicValue = null;
		XhbAddress addrBean = null;

		if (addressValue.getAddressID() != null && addressValue.getAddressID() != 0) {
			log.debug("Update an Address with ID: " + addressValue.getId() + " and AddressValue " + addressValue);
			addrBasicValue = XhbAddressBeanHelper2.findByPrimaryKeyValue(addressValue.getAddressID());
		} else {
			log.debug("Create a new Address with AddressValue " + addressValue);
			addrBasicValue = new XhbAddressBasicValue();
		}

		addrBasicValue.setAddress1(addressValue.getAddress1());
		addrBasicValue.setAddress2(addressValue.getAddress2());
		addrBasicValue.setAddress3(addressValue.getAddress3());
		addrBasicValue.setAddress4(addressValue.getAddress4());
		addrBasicValue.setCountry(addressValue.getCountry());
		addrBasicValue.setCounty(addressValue.getCounty());
		addrBasicValue.setPostcode(addressValue.getPostcode());
		addrBasicValue.setTown(addressValue.getTown());

		if (addressValue.getAddressID() != null && addressValue.getAddressID() != 0) {
			addrBean = XhbAddressBeanHelper2.updateLocal(addrBasicValue);
			addrBean.setLastUpdatedBy(userDisplayName);
		} else {
			addrBean = XhbAddressBeanHelper2.createLocal(addrBasicValue);
			addrBean.setCreatedBy(userDisplayName);
			addrBean.setLastUpdatedBy(userDisplayName);
		}

		return addrBean;
	}

	/**
	 * Update existing or create new rows in xhb_offence table.
	 * 
	 * @param chargeBean
	 * @param offenceValues
	 * @param userDisplayName
	 * @return XhbOffence(s)
	 */
	private Collection<XhbOffence> saveOffenceEntities(XhbCharge chargeBean, Collection<OffenceValue> offenceValues,  String userDisplayName) {
		List<XhbOffence> offenceBeans = new ArrayList<XhbOffence>();

		// Save each offence supplied in the collection
		Iterator<OffenceValue> offencesIt = offenceValues.iterator();
		while (offencesIt.hasNext()) {
			OffenceValue offenceValue = offencesIt.next();
			XhbOffence offenceBean = saveOffenceEntity(chargeBean, offenceValue, userDisplayName);
			offenceBeans.add(offenceBean);
		}

		return offenceBeans;
	}

	/**
	 * Update existing or create new row in xhb_offence table.
	 * 
	 * @param chargeBean
	 * @param offenceValue
	 * @param userDisplayName
	 * @return XhbOffence
	 */
	private XhbOffence saveOffenceEntity(XhbCharge chargeBean, OffenceValue offenceValue, String userDisplayName) {
		XhbOffenceBasicValue offenceBasicValue = null;
		XhbOffence offenceBean = null;

		if (offenceValue.getOffenceID() != null && offenceValue.getOffenceID() != 0) {
			log.debug(
					"Update an Offence with ID: " + offenceValue.getOffenceID() + " and OffenceValue " + offenceValue);
			offenceBasicValue = XhbOffenceBeanHelper2.findByPrimaryKeyValue(offenceValue.getOffenceID());
			offenceBasicValue.setCrestOffenceSeqNo(offenceValue.getCrestOffenceSeqNo());
		} else {
			log.debug("Create a new Offence with OffenceValue " + offenceValue);

			// Setup new offence value object for creating entity
			offenceBasicValue = new XhbOffenceBasicValue();
			offenceBasicValue.setChargeId(chargeBean.getChargeId());
			offenceBasicValue.setCrestOffenceSeqNo(getNextCrestOffenceSeqNum(chargeBean));
		}

		// Common fields to set whether creating or updating
		offenceBasicValue.setForceLocationCode(offenceValue.getForceLocationCode());

		// Defend against start date being null
		Date startDate = null;
		if (offenceValue.getOffenceStartDateTime() != null) {
			startDate = offenceValue.getOffenceStartDateTime().getTime();
		}
		offenceBasicValue.setStartDate(startDate);

		// Defend against end date being null
		Date endDate = null;
		if (offenceValue.getOffenceEndDateTime() != null) {
			endDate = offenceValue.getOffenceEndDateTime().getTime();
		}
		offenceBasicValue.setEndDate(endDate);

		// Save address entity if supplied
		if (offenceValue.getAddressValue() != null) {
			XhbAddress addrBean = saveAddressEntity(offenceValue.getAddressValue(), userDisplayName);
			offenceBasicValue.setLocationAddressId(addrBean.getAddressId());
		}

		offenceBasicValue.setRefOffenceId(offenceValue.getRefOffenceID());
		if (ChargeTypes.INDICTMENT.getChargeType().equals(chargeBean.getChargeType())) {
			offenceBasicValue.setRefSystemCodeId(offenceValue.getRefSystemCodeID());
		}

		offenceBasicValue.setCrestOffenceFreetext(offenceValue.getCrestOffenceFreeText());
		offenceBasicValue.setCrestHooClassFreetext(offenceValue.getCrestHOClass());
		offenceBasicValue.setCrestHooSubclassFreetext(offenceValue.getCrestHOSubclass());
		
		// Check if the Appeal Type has changed
		boolean updateDefOnOffenceAppealType = false;
		if ( offenceValue.getAppealType() != null && offenceBasicValue.getAppealType() != null 
				&& !offenceValue.getAppealType().equals(offenceBasicValue.getAppealType()) ) {
			updateDefOnOffenceAppealType = true;
		}
		offenceBasicValue.setAppealType(offenceValue.getAppealType());

		if (offenceValue.getOffenceID() != null && offenceValue.getOffenceID() != 0) {
			offenceBean = XhbOffenceBeanHelper2.updateLocal(offenceBasicValue);
			offenceBean.setLastUpdatedBy(userDisplayName);
			if (updateDefOnOffenceAppealType) {
				// Update the defendant on offence entities
				updateDefendantOnOffenceEntities(offenceBean, offenceValue, userDisplayName);
			}
			
		} else {
			offenceBean = XhbOffenceBeanHelper2.createLocal(offenceBasicValue);
			offenceBean.setCreatedBy(userDisplayName);
			offenceBean.setLastUpdatedBy(userDisplayName);

			// Set values post id allocation
			offenceBean.setCrestOffenceId(offenceBean.getOffenceId());

			// Create defendant on offence entities if supplied
			createDefendantOnOffenceEntities(offenceBean, offenceValue, userDisplayName);
		}

		return offenceBean;
	}

	/**
	 * Copy existing row in xhb_offence table.
	 * 
	 * @param originalOffenceBean
	 * @param userDisplayName
	 * @return XhbOffence
	 */
	private XhbOffence copyOffenceEntity(XhbOffence originalOffenceBean, String userDisplayName) {
		XhbCharge chargeBean = originalOffenceBean.getXhbCharge();

		// Setup new offence value object for creating entity from existing
		// offence
		XhbOffenceBasicValue offenceBasicValue = originalOffenceBean.getData();
		offenceBasicValue.setOffenceId(null);
		offenceBasicValue.setCrestOffenceId(null);
		offenceBasicValue.setCrestOffenceSeqNo(getNextCrestOffenceSeqNum(chargeBean));

		// Create new offence from offence data
		XhbOffence offenceBean = XhbOffenceBeanHelper2.createLocal(offenceBasicValue);
		offenceBean.setCreatedBy(userDisplayName);
		offenceBean.setLastUpdatedBy(userDisplayName);

		// Set values post id allocation
		offenceBean.setCrestOffenceId(offenceBean.getOffenceId());

		return offenceBean;
	}

	/**
	 * Update existing or create new row in xhb_breach table.
	 * 
	 * @param chargeBean
	 * @param breachValue
	 * @return XhbBreach
	 */
	private XhbBreach saveBreachEntity(XhbCharge chargeBean, BreachValue breachValue) {
		XhbBreachBasicValue breachBasicValue = null;
		XhbBreach breachBean = null;

		if (breachValue.getBreachID() != null && breachValue.getBreachID() != 0) {
			log.debug("Update a Breach with ID: " + breachValue.getBreachID() + " and BreachValue " + breachValue);
			breachBasicValue = XhbBreachBeanHelper2.findByPrimaryKeyValue(breachValue.getBreachID());
		} else {
			log.debug("Create a new Breach with breachValue " + breachValue);

			// Setup new breach value updaobject for creating entity
			breachBasicValue = new XhbBreachBasicValue();
			breachBasicValue.setChargeId(chargeBean.getChargeId());
		}

		// Common fields to set whether creating or updating
		breachBasicValue.setOriginalSentence(breachValue.getOriginalSentence());

		// Defend against sentence date being null
		Date originalSentenceDate = null;
		if (breachValue.getOriginalSentenceDate() != null) {
			originalSentenceDate = breachValue.getOriginalSentenceDate().getTime();
		}
		breachBasicValue.setOriginalSentenceDate(originalSentenceDate);

		// Defend against date put being null
		Date datePut = null;
		if (breachValue.getDatePut() != null) {
			datePut = breachValue.getDatePut().getTime();
		}
		breachBasicValue.setDatePut(datePut);

		// Logic for failure to appear offence
		if (ChargeTypes.FAIL2APPEAR.getChargeType().equals(chargeBean.getChargeType())) {
			breachBasicValue.setRefCourtId(null);
			breachBasicValue.setOriginalCourtType(COURT_TYPE_FAIL2APPEAR);
			breachBasicValue.setBreachType(BREACH_TYPE_FAIL2APPEAR);
			breachBasicValue.setBringBack(BRING_BACK_FAIL2APPEAR);
		} else {
			breachBasicValue.setRefCourtId(breachValue.getOriginalCourtID());
			breachBasicValue.setOriginalCourtType(breachValue.getOriginalCourtType());
			breachBasicValue.setBreachType(breachValue.getBreachType());
			breachBasicValue.setBringBack(breachValue.getBringBack());
		}

		if (breachValue.getBreachID() != null && breachValue.getBreachID() != 0) {
			breachBean = XhbBreachBeanHelper2.updateLocal(breachBasicValue);
			breachBean.setLastUpdatedBy(breachValue.getLastUpdatedBy());			
		} else {
			breachBean = XhbBreachBeanHelper2.createLocal(breachBasicValue);
			breachBean.setCreatedBy(breachValue.getLastUpdatedBy());
			breachBean.setLastUpdatedBy(breachValue.getLastUpdatedBy());
		}

		return breachBean;
	}

	/* ctx-1630 to update xhb_plea table */

	/**
	 * Create new row in xhb_plea table.
	 * 
	 * @param defendantchargeBean
	 * @param breachValue
	 * @return XhbBreach
	 */
	private XhbPlea savePleaEntity(Integer defendantChargeId, BreachValue breachValue) {

		XhbPlea pleaBean = null;
		XhbPleaBasicValue xhbPleaBasicVal = new XhbPleaBasicValue();

		xhbPleaBasicVal.setBreachAdmitted(breachValue.getPlea());
		xhbPleaBasicVal.setDefOnChargeOrOffence("C");
		xhbPleaBasicVal.setDefendantChargeId(defendantChargeId);
		xhbPleaBasicVal.setObsInd("N");

		XhbPleaBasicValue[] pleaBasicValues = XhbPleaBeanHelper2.findByDefendantChargeIdValue(defendantChargeId);

		// if no plea returned for given defendantChargeId, then create one
		// in db.
		if (pleaBasicValues.length == 0) {
			// Only create one if the breach has a plea value (otherwise its N/A)
			if (breachValue.getPlea() != null) {
				// Setup new plea object for creating entity
				xhbPleaBasicVal.setDefendantChargeId(defendantChargeId);
				xhbPleaBasicVal.setPleaId(xhbPleaBasicVal.getPrimaryKey());
				pleaBean = XhbPleaBeanHelper2.createLocal(xhbPleaBasicVal);
				if (breachValue.getLastUpdatedBy() != null) {
					pleaBean.setCreatedBy(breachValue.getLastUpdatedBy());
					pleaBean.setLastUpdatedBy(breachValue.getLastUpdatedBy());
				}
			}

		} else {

			// if plea returned for given defendantChargeId, then update the
			// returned ones in db.

			for (int j = 0; j < pleaBasicValues.length; j++) {
				xhbPleaBasicVal = pleaBasicValues[j];

				if (breachValue.getPlea() != null) {
					if (!breachValue.getPlea().equals(xhbPleaBasicVal.getBreachAdmitted())) {
						xhbPleaBasicVal.setObsInd("Y");
						pleaBean = XhbPleaBeanHelper2.updateLocal(xhbPleaBasicVal);
						if (breachValue.getLastUpdatedBy() != null) {
							pleaBean.setLastUpdatedBy(breachValue.getLastUpdatedBy());
						}
					}
					xhbPleaBasicVal.setObsInd("N");
					xhbPleaBasicVal.setBreachAdmitted(breachValue.getPlea());
					pleaBean = XhbPleaBeanHelper2.createLocal(xhbPleaBasicVal);
					if (breachValue.getLastUpdatedBy() != null) {
						pleaBean.setCreatedBy(breachValue.getLastUpdatedBy());
						pleaBean.setLastUpdatedBy(breachValue.getLastUpdatedBy());
					}
				} else {
					// Breach Plea is N/A, so obsolete any plea records
					xhbPleaBasicVal.setObsInd("Y");
					pleaBean = XhbPleaBeanHelper2.updateLocal(xhbPleaBasicVal);
					if (breachValue.getLastUpdatedBy() != null) {
						pleaBean.setLastUpdatedBy(breachValue.getLastUpdatedBy());
					}
				}
			}
		}
		return pleaBean;

	}

	/**
	 * Update existing or create new row in xhb_plea table.
	 * 
	 * @param chargeBean
	 * @param breachValue
	 * @return XhbPlea
	 */
	private XhbPlea savePleaEntity(XhbCharge chargeBean, BreachValue breachValue) {

		XhbDefendantChargeBasicValue[] defChargeBV = chargeBean.getXhbDefendantChargesData();
		XhbPlea pleaBean = null;

		for (int i = 0; i < defChargeBV.length; i++) {
			pleaBean = savePleaEntity(defChargeBV[i].getDefendantChargeId(), breachValue);
		}
		return pleaBean;
	}

	/**
	 * Create new row in xhb_defendant_charge table.
	 * 
	 * @param OffenceValue
	 * @param userDisplayName
	 * @return XhbDefendantCharge
	 */
	private XhbDefendantCharge createDefendantChargeEntity(XhbCharge chargeBean, ChargeValue chargeValue, String userDisplayName) {
		log.debug("Create a new DefendantCharge for CaseID " + chargeBean.getCaseId() + ", chargeID "
				+ chargeBean.getChargeId());

		// Get the defendantOnCase bean
		XhbDefendantOnCase defendantOnCaseBean = XhbDefendantOnCaseBeanHelper2
				.findByDefendantAndCase(chargeValue.getDefendantID(), chargeBean.getCaseId());

		// Create the defendantCharge
		XhbDefendantChargeBasicValue defendantChargeBasicValue = new XhbDefendantChargeBasicValue();
		defendantChargeBasicValue.setChargeId(chargeBean.getChargeId());
		defendantChargeBasicValue.setDefendantOnCaseId(defendantOnCaseBean.getDefendantOnCaseId());

		XhbDefendantCharge defendantChargeBean = XhbDefendantChargeBeanHelper2.createLocal(defendantChargeBasicValue);
		defendantChargeBean.setCreatedBy(userDisplayName);
		defendantChargeBean.setLastUpdatedBy(userDisplayName);
		return defendantChargeBean;
	}

	/**
	 * Update existing or create new rows in xhb_defendant_on_offence table.
	 * 
	 * @param chargeBean
	 * @param defendantOnOffenceValues
	 * @param userDisplayName
	 * @return XhbDefendantOnOffence(s)
	 */
	private Collection<XhbDefendantOnOffence> saveDefendantOnOffenceEntities(
			Collection<DefendantOnOffenceValue> defendantOnOffenceValues, String userDisplayName) {
		List<XhbDefendantOnOffence> defendantOnOffenceBeans = new ArrayList<XhbDefendantOnOffence>();

		// Save each defendant on offence supplied in the collection
		Iterator<DefendantOnOffenceValue> defendantOffencesIt = defendantOnOffenceValues.iterator();
		while (defendantOffencesIt.hasNext()) {
			DefendantOnOffenceValue defendantOnOffenceValue = defendantOffencesIt.next();
			XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(defendantOnOffenceValue.getOffenceId());
			XhbDefendantOnOffence defendantOnOffenceBean = saveDefendantOnOffenceEntity(offenceBean,
					defendantOnOffenceValue, userDisplayName);
			defendantOnOffenceBeans.add(defendantOnOffenceBean);
		}

		return defendantOnOffenceBeans;
	}

	/**
	 * Update existing or create new row in the xhb_defendant_on_offence table.
	 * 
	 * @param offenceBean
	 * @param defendantOnOffenceValue
	 * @return XhbDefendantOnOffence
	 */
	private XhbDefendantOnOffence saveDefendantOnOffenceEntity(XhbOffence offenceBean,
			DefendantOnOffenceValue defendantOnOffenceValue, String userDisplayName) {
		XhbDefendantOnOffenceBasicValue defendantOnOffenceBasicValue;
		XhbDefendantOnOffence defendantOnOffenceBean;
		boolean updateEntity;

		// Get the defendantOnCase bean
		XhbDefendantOnCase defendantOnCaseBean = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(
				defendantOnOffenceValue.getDefendantId(), offenceBean.getXhbCharge().getCaseId());

		// Only way to check if the entity already exists is to attempt to
		// retrieve the entity by
		// the defendant on case id and the offence id and catch the not found
		// exception because
		// the primary key value is not available on the DefendantOnOffenceValue
		// object
		try {
			defendantOnOffenceBasicValue = XhbDefendantOnOffenceBeanHelper2.findByDefOnCaseAndOffenceValue(
					defendantOnCaseBean.getDefendantOnCaseId(), offenceBean.getOffenceId());
			log.debug("Update a DefendantOnOffence with ID: " + defendantOnOffenceBasicValue.getDefendantOnOffenceId()
					+ " and DefendantOnOffenceValueValue " + defendantOnOffenceValue);
			updateEntity = true;

		} catch (XhbDefendantOnOffenceBeanNotFoundException ex) {
			log.debug("Create a new DefendantOnOffence with DefendantOnOffenceValue " + defendantOnOffenceValue);
			updateEntity = false;

			// Setup new defendant on offence value object for creating entity
			defendantOnOffenceBasicValue = new XhbDefendantOnOffenceBasicValue();
			defendantOnOffenceBasicValue.setOffenceId(offenceBean.getOffenceId());
			defendantOnOffenceBasicValue.setDefendantOnCaseId(defendantOnCaseBean.getDefendantOnCaseId());
		}

		// Common fields to set whether creating or updating
		defendantOnOffenceBasicValue.setSeqNo(defendantOnOffenceValue.getSequenceNo());
		defendantOnOffenceBasicValue.setAppealAgainstType(offenceBean.getAppealType());

		// Defend against arrest date being null
		Date arrestDate = null;
		if (defendantOnOffenceValue.getDateOfArrest() != null) {
			arrestDate = defendantOnOffenceValue.getDateOfArrest().getTime();
		}
		defendantOnOffenceBasicValue.setArrestDate(arrestDate);

		// Defend against arrest date being null
		Date chargeDate = null;
		if (defendantOnOffenceValue.getDateOfCharge() != null) {
			chargeDate = defendantOnOffenceValue.getDateOfCharge().getTime();
		}
		defendantOnOffenceBasicValue.setChargeDate(chargeDate);

		defendantOnOffenceBasicValue.setIsCommittedOnBail(defendantOnOffenceValue.getIsCommittedOnBail());
		defendantOnOffenceBasicValue.setInterimD20(defendantOnOffenceValue.getInterimD20());
		defendantOnOffenceBasicValue.setDarRetentionPolicyId(defendantOnOffenceValue.getDarRetentionPolicyId());
		defendantOnOffenceBasicValue.setObsInd(defendantOnOffenceValue.getObsInd());

		if (updateEntity) {
			defendantOnOffenceBean = XhbDefendantOnOffenceBeanHelper2.updateLocal(defendantOnOffenceBasicValue);
			defendantOnOffenceBean.setLastUpdatedBy(userDisplayName);
		} else {
			defendantOnOffenceBean = XhbDefendantOnOffenceBeanHelper2.createLocal(defendantOnOffenceBasicValue);
			defendantOnOffenceBean.setLastUpdatedBy(userDisplayName);
			defendantOnOffenceBean.setCreatedBy(userDisplayName);
		}

		return defendantOnOffenceBean;
	}

	/**
	 * Create new rows in the xhb_defendant_on_offence table.
	 * 
	 * @param offenceBean
	 * @param offenceValue
	 * @return XhbDefendantOnOffence entities
	 */
	private Collection<XhbDefendantOnOffence> createDefendantOnOffenceEntities(XhbOffence offenceBean,
			OffenceValue offenceValue, String userDisplayName) {
		// List of created beans and values used to create the beans
		List<XhbDefendantOnOffence> defendantOnOffenceBeans = new ArrayList<XhbDefendantOnOffence>();
		HashMap<Integer, DefendantOnOffenceComplexValue> defOnOffenceComplexValues = offenceValue
				.getDefOnOffenceBasicValues();

		// Iterate through all the defendant on offence values whose key is the
		// defendant id
		Iterator<Integer> defendantIdIterator = defOnOffenceComplexValues.keySet().iterator();
		while (defendantIdIterator.hasNext()) {
			// Get the next defendant id and its corresponding defendant on
			// offence value
			Integer defendantId = defendantIdIterator.next();
			XhbDefendantOnOffenceBasicValue defOnOffenceBasicValue = defOnOffenceComplexValues.get(defendantId);

			// Set the id's required for the new row as well as copying the appeal type from the offence
			defOnOffenceBasicValue.setOffenceId(offenceBean.getOffenceId());
			defOnOffenceBasicValue.setAppealAgainstType(offenceBean.getAppealType());
			defOnOffenceBasicValue.setDefendantOnCaseId(XhbDefendantOnCaseBeanHelper2
					.findByDefendantAndCase(defendantId, offenceValue.getCaseID()).getDefendantOnCaseId());

			// Create new row
			XhbDefendantOnOffence defendantOnOffenceBean = XhbDefendantOnOffenceBeanHelper2
					.createLocal(defOnOffenceBasicValue);
			defendantOnOffenceBean.setLastUpdatedBy(userDisplayName);
			defendantOnOffenceBean.setCreatedBy(userDisplayName);
			defendantOnOffenceBeans.add(defendantOnOffenceBean);
		}

		return defendantOnOffenceBeans;
	}
	
	private Collection<XhbDefendantOnOffence> updateDefendantOnOffenceEntities(XhbOffence offenceBean, OffenceValue offenceValue, String userDisplayName) {
		List<XhbDefendantOnOffence> defendantOnOffenceBeans = new ArrayList<XhbDefendantOnOffence>();
		HashMap<Integer, DefendantOnOffenceComplexValue> defOnOffenceComplexValues = offenceValue
				.getDefOnOffenceBasicValues();

		// Iterate through all the defendant on offence values whose key is the
		// defendant id
		Iterator<Integer> defendantIdIterator = defOnOffenceComplexValues.keySet().iterator();
		while (defendantIdIterator.hasNext()) {
			// Get the next defendant id and its corresponding defendant on
			// offence value
			Integer defendantId = defendantIdIterator.next();
			XhbDefendantOnOffenceBasicValue defOnOffenceBasicValue = defOnOffenceComplexValues.get(defendantId);

			// Ensure the appeal type on the defendant on offence matches the offence appeal type
			defOnOffenceBasicValue.setAppealAgainstType(offenceBean.getAppealType());;

			// Create new row
			XhbDefendantOnOffence defendantOnOffenceBean = XhbDefendantOnOffenceBeanHelper2
					.updateLocal(defOnOffenceBasicValue);
			defendantOnOffenceBean.setLastUpdatedBy(userDisplayName);
			defendantOnOffenceBean.setCreatedBy(userDisplayName);
			defendantOnOffenceBeans.add(defendantOnOffenceBean);
		}

		return defendantOnOffenceBeans;
	}

	/**
	 * Implement the following business rules: XHIBIT2UC12CT12BR-2.4.17
	 * XHIBIT2UC12CT12BR-2.3.5C XHIBIT2UCBRXT03SR-1.0.29
	 * 
	 * @param caseBean
	 * @param String
	 * @param originalSentanceDate
	 * @throws ChargeValidationException
	 */
	private void validateOriginalSentenceDate(XhbCase caseBean, String chargeType, Calendar originalSentenceDate)
			throws ChargeValidationException {
		// XHIBIT2UC12CT12BR-2.4.17
		if (originalSentenceDate == null) {
			throw new ChargeValidationException("charge.validation.originalsentencedate.null",
					"Original Sentence Date cannot be null");
		}
		// XHIBIT2UC12CT12BR-2.3.5C
		if (ChargeTypes.BREACH.getChargeType().equals(chargeType) && caseBean.getReceivedDate() != null
				&& originalSentenceDate.getTime().compareTo(caseBean.getReceivedDate()) >= 0) {
			throw new ChargeValidationException("charge.validation.originalsentencedate.datereceived",
					"Original Sentence Date must be less than Case Date Received");
		}
		// XHIBIT2UCBRXT03SR-1.0.29
		if (originalSentenceDate.getTime().compareTo(new Date()) > 0) {
			throw new ChargeValidationException("charge.validation.originalsentencedate.currentdate",
					"Original Sentence Date cannot be greater than current date");
		}
	}

	/**
	 * Implement the following business rules: XHIBIT2UC12CT12BR-2.3.6A
	 * 
	 * @param caseBean
	 * @param String
	 * @param datePut
	 * @throws ChargeValidationException
	 */
	private void validateDatePut(XhbCase caseBean, String chargeType, Calendar datePut)
			throws ChargeValidationException {
		// XHIBIT2UC12CT12BR-2.3.6A
		if (ChargeTypes.BREACH.getChargeType().equals(chargeType) && caseBean.getReceivedDate() != null
				&& datePut != null && datePut.getTime().compareTo(caseBean.getReceivedDate()) < 0) {
			throw new ChargeValidationException("charge.validation.dateput.datereceived",
					"Date Put must be greater or equal to Case Date Received");
		}
	}

	/**
	 * Implement the following business rules: XHIBIT2UC12CT12BR-2.5.4
	 * XHIBIT2UC12CT12BR-2.5.5
	 * 
	 * @param caseBean
	 * @param String
	 * @param plea
	 * 
	 * @throws ChargeValidationException
	 */
	private void validatePlea(XhbCase caseBean, String chargeType, String plea, String hoCode)
			throws ChargeValidationException {
		
		// XHIBIT2UC12CT12BR-2.5.4
		if (CASE_TYPE_SENTENCE.equals(caseBean.getCaseType())) {
				if (RECEIPT_TYPE_BRING_BACK.equals(caseBean.getReceiptType()) && plea != null) {
					throw new ChargeValidationException("charge.validation.admitted.nopleaallowed", "You cannot enter a plea");
				}
	 
		// XHIBIT2UC12CT12BR-2.5.5
		
				if (!RECEIPT_TYPE_BRING_BACK.equals(caseBean.getReceiptType())) {
						if (Arrays.asList(HO_PROC_CODES).contains(hoCode) && plea == null) {
							throw new ChargeValidationException("charge.validation.admitted.pleamandatory", "Plea is mandatory");
						} 
			
						if (!Arrays.asList(HO_PROC_CODES).contains(hoCode) && plea != null) {
							throw new ChargeValidationException("charge.validation.admitted.nopleaallowed", "You cannot enter a plea");
						}
				}
		}
	  
	}

	/**
	 * @param case
	 * @param breach
	 * 
	 * @throws ChargeValidationException
	 */
	private void validateBreachType(XhbCase caseBean, BreachValue breachValue)
			throws ChargeValidationException {
		
		if (CASE_TYPE_TRIAL.equals(caseBean.getCaseType())) {
			if (breachValue.getBreachType() != null && !BREACH_TYPE_BRING_BACK.equals(breachValue.getBreachType()) &&
					!breachValue.getBreachType().equals(BREACH_TYPE_FAIL2APPEAR)) {
					throw new ChargeValidationException("charge.validation.breach.notallowed", "Breach type cannot be bring back for a Trial case");
		     }
 		}
	}

	/**
	 * Implement the following business rules: XHIBIT2UC3CT3BR-2.3.5
	 * XHIBIT2UC3CT3BR-2.3.2a
	 * 
	 * @param caseBean
	 * @param prosecutionPaperServedDate
	 * @throws ChargeValidationException
	 */
	private void validateProsecutionPaperServedDate(XhbCase caseBean, Calendar prosecutionPaperServedDate)
			throws ChargeValidationException {
		// XHIBIT2UC3CT3BR-2.3.5
		if (!Arrays.asList(RECEIPT_TYPE_CODES).contains(caseBean.getReceiptType())
				&& prosecutionPaperServedDate != null) {
			throw new ChargeValidationException("charge.validation.prosecutionpaperserveddate.null",
					"Prosecution paper served date must be null if receipt type is not ST, EW or IO");
		}
		// XHIBIT2UC3CT3BR-2.3.2a
		if (prosecutionPaperServedDate != null && prosecutionPaperServedDate.getTime().compareTo(new Date()) > 0) {
			throw new ChargeValidationException("charge.validation.prosecutionpaperserveddate.currentdate",
					"Prosecution paper served date cannot be after current date");
		}
	}

	/**
	 * Implement the following business rules: XHIBIT2UC3CT3BR-2.3.3
	 * 
	 * @param dateIndictmentReceived
	 * @throws ChargeValidationException
	 */
	private void validateDateIndictmentReceived(Calendar dateIndictmentReceived) throws ChargeValidationException {
		// XHIBIT2UC3CT3BR-2.3.3
		if (dateIndictmentReceived != null && dateIndictmentReceived.getTime().compareTo(new Date()) > 0) {
			throw new ChargeValidationException("charge.validation.dateindictmentreceived.currentdate",
					"Date Indictment Received cannot be after current date");
		}
	}

	/**
	 * The offence value has the offence description as EDIT_UNCODED_OFFENCE in
	 * order to override the updateOffence map and trigger Mercator not to
	 * delete the related data(pleas, full disposal details and CRNs).
	 * 
	 * @param offenceValue
	 *            OffenceValue
	 * @param userDisplayName
	 *            String
	 * @throws ChargeControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void editOffence(OffenceValue offenceValue, String userDisplayName) throws ChargeControllerException {

		try {
			eventHelper.updateOffenceLogEntry(offenceValue);

			// set the dirty flags
			offenceValue.setDirty(true);
			offenceValue.setOffenceDescription("EDIT_UNCODED_OFFENCE");

			// ctx-266 Update the offence with direct database update
			XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(offenceValue.getChargeID());
			saveOffenceEntity(chargeBean, offenceValue, userDisplayName);

		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	/**
	 * Returns AddressValue based on addressId
	 * 
	 * @param addressId
	 * @return
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public AddressValue getAddress(Integer addressId) {
		return AddressHelper.getAddressValue(addressId);
	}

	/**
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param caseId:
	 *            Integer
	 * @param chargeType:
	 *            String
	 * @return DefendantChargesCompositeVO[]
	 * @throws ChargeControllerException
	 */
	public DefendantChargesCompositeVO[] getDefendantChargesByCaseId(Integer caseId, String chargeType)
			throws ChargeControllerException {
		log.info("*** getDefendantChargesByCaseId(" + caseId + ", " + chargeType + ") called ***");

		return originalChargesHelper.getDefendantChargesByCaseId(caseId, chargeType);
	}

	/**
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param originalCharges:
	 *            OriginalChargeVO[]
	 * @param userDisplayName
	 *            String
	 * @return OriginalChargeVO[]
	 * @throws ChargeControllerException
	 */
	public OriginalChargeVO[] maintainOriginalCharges(OriginalChargeVO[] originalCharges, String userDisplayName)
			throws ChargeControllerException {
		log.info("***** OriginalChargeVO[] maintainOriginalCharges(OriginalChargeVO[])");

		// ctx-390 Cycle through all original charges and create, update or
		// delete each one
		for (OriginalChargeVO originalCharge : originalCharges) {
			if (originalCharge.getTrxCode().equalsIgnoreCase("C")) {
				log.debug("***** Create Original Charge, Case ID: " + originalCharge.getCaseId());

				// Get the charge entity for original charges
				XhbCharge chargeBean = getOriginalChargeEntity(originalCharge.getCaseId(), userDisplayName);

				// Create the offence entity
				OffenceValue offenceValue = new OffenceValue();
				offenceValue.setRefOffenceID(originalCharge.getRefOffenceId());
				offenceValue.setCrestOffenceFreeText(originalCharge.getOriginalCharge());
				XhbOffence offenceBean = saveOffenceEntity(chargeBean, offenceValue, userDisplayName);

				// Create the defendant on offence entity
				DefendantOnOffenceValue defendantOnOffenceValue = new DefendantOnOffenceValue(
						offenceBean.getOffenceId(), originalCharge.getDefendantId(), null);
				defendantOnOffenceValue.setSequenceNo(originalCharge.getSeqNo());
				XhbDefendantOnOffence defendantOnOffenceBean = saveDefendantOnOffenceEntity(offenceBean,
						defendantOnOffenceValue, userDisplayName);

				log.debug("***** Successfully Created Original Charge, DOO ID : "
						+ defendantOnOffenceBean.getDefendantOnOffenceId());
			} else if (originalCharge.getTrxCode().equalsIgnoreCase("U")) {
				log.debug("***** Update Original Charge, DOO ID: " + originalCharge.getDefendantOnOffenceId());

				// Get the beans
				XhbDefendantOnOffence defendantOnOffenceBean = XhbDefendantOnOffenceBeanHelper2
						.findByPrimaryKey(originalCharge.getDefendantOnOffenceId());
				XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(originalCharge.getOffenceId());

				// Only update the sequence number and free text so cannot use
				// the save entity methods
				defendantOnOffenceBean.setSeqNo(originalCharge.getSeqNo());
				offenceBean.setCrestOffenceFreetext(originalCharge.getOriginalCharge());

				log.debug("***** Successfully Updated Original Charge, DOO ID : "
						+ originalCharge.getDefendantOnOffenceId());
			} else if (originalCharge.getTrxCode().equalsIgnoreCase("D")) {
				log.debug("***** Delete Original Charge, DOO ID : " + originalCharge.getDefendantOnOffenceId());

				// Get the beans
				XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(originalCharge.getOffenceId());
				XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(originalCharge.getChargeId());

				// Delete the offence entity and its linked rows
				deleteOffenceEntity(chargeBean, offenceBean, userDisplayName);

				log.debug("***** Successfully Deleted Original Charge, DOO ID : "
						+ originalCharge.getDefendantOnOffenceId());
			}
		}

		return originalCharges;
	}

	/**
	 * @ejb.interface-method view-type="remote"
	 * 
	 * @param chargeValue:
	 *            ChargeValue
	 * @param userDisplayName           
	 * @throws ChargeControllerException
	 * @throws CourtLogBusinessException
	 */
	public void renumberCounts(ChargeValue chargeValue, HashMap changedCounts, String userDisplayName)
			throws uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException,
			CourtLogBusinessException {
		java.util.Iterator iter = chargeValue.getOffenceValues().iterator();
		while (iter.hasNext()) {
			OffenceValue offenceValue = (OffenceValue) iter.next();
			Integer offenceId = offenceValue.getOffenceID();
			if (changedCounts.get(offenceId) != null) {
				
					if (offenceValue.getAddressId() != null) {
						/*
						 * Get Address back from the DB so that we don't splat
						 * details in CREST
						 */
						XhbAddress address = XhbAddressBeanHelper2.findByPrimaryKey(offenceValue.getAddressId());
						XhbAddressBasicValue addBasVal = address.getData();
						AddressValue addVal = new AddressValue(addBasVal);
						offenceValue.setAddressValue(addVal);
						XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(offenceValue.getChargeID());
						saveOffenceEntity(chargeBean, offenceValue, userDisplayName);
					}

			}
		}

		CaseMaintainer caseMaintainer = new CaseMaintainer();
		Case caseBean;
		try {
			caseBean = caseMaintainer.findByPrimaryKey(chargeValue.getCaseID());
			caseBean.setIndChangeStatus("E");
		} catch (ObjectNotFoundException e) {
			ctx.setRollbackOnly();
			log.debug("renumberCounts: Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// the caseID has come from the charge so should be valid
			throw new ChargeControllerException("chargecontroller.casenotfound", new Object[] { chargeValue.getCaseID() },
					"Error renumbering counts, case not found. CaseID = " + chargeValue.getCaseID(), e);
		}
		// log the court log event
		eventHelper.renumberCounts(chargeValue);
	}

	/**
	 * @ejb.interface-method view-type="remote"
	 * @param defOnOffenceBasicValues
	 *            XhbDefendantOnOffenceBasicValue[]
	 * @param logEntry
	 *            CourtLogCRUDValue
	 * @throws uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException
	 */

	public void addRemoveDefendantsFromCountLog(XhbDefendantOnOffenceBasicValue[] defOnOffenceBasicValues,
			CourtLogCRUDValue logEntry)
			throws uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException {
		try {
			for (XhbDefendantOnOffenceBasicValue defendant : defOnOffenceBasicValues) {
				eventHelper.removeDefendantsLog(defendant, logEntry);
			}
		} catch (CourtLogBusinessException ex) {
			handleWrapAndRethrowException(ex);
		}
	}

	/**
	 * @ejb.interface-method view-type="remote"
	 * @param linkVal
	 *            LinkCountDefValue
	 * @param logEntry
	 * @throws ChargeControllerException
	 */
	public void addUnRemoveDefendantsFromCountLog(LinkCountDefValue linkVal) throws ChargeControllerException {
		// log the court log event for each defendant removal
		Collection links = linkVal.getDefendantOnOffenceValues();
		Iterator linksIt = links.iterator();
		while (linksIt.hasNext()) {
			DefendantOnOffenceValue doof = (DefendantOnOffenceValue) linksIt.next();
			// check if the offence is from a joinder, if so we need to make
			try {
				eventHelper.linkCountDefCourtLog(linkVal, doof);
			} catch (CourtLogBusinessException ex) {
				handleWrapAndRethrowException(ex);
			}
		}
	}

	private void deleteChargeEntity(XhbCharge charge) {
		charge.setObsInd(OBSOLETE);

		deleteDefendantChargeEntities(charge.getXhbDefendantCharges());

		resequenceChargeSeq(charge.getXhbCase().getXhbCharges(), charge.getChargeType());
	}

	private void deleteBreachEntity(XhbBreach breach) {
		breach.setObsInd(OBSOLETE);
	}

	private void deleteOffenceEntities(XhbCharge chargeBean, String userDisplayName) {
		Collection offences = chargeBean.getXhbOffences();
		if (offences != null && !offences.isEmpty()) {
			Iterator iterator = offences.iterator();
			while (iterator.hasNext()) {
				XhbOffence offence = (XhbOffence) iterator.next();
				deleteOffenceEntity(chargeBean, offence, !iterator.hasNext(), userDisplayName);
			}
		}
	}

	private void deleteOffenceEntity(XhbCharge chargeBean, XhbOffence offence, String userDisplayName) {
		deleteOffenceEntity(chargeBean, offence, true, userDisplayName);
	}

	private void deleteOffenceEntity(XhbCharge chargeBean, XhbOffence offence, boolean resequence, String userDisplayName) {
		offence.setObsInd(OBSOLETE);

		deleteDefendantsOnOffenceEntities(chargeBean.getCaseId(), chargeBean.getChargeType(), offence.getXhbDefendantOnOffences(), userDisplayName);

		// CTX-260 Resequence the offences listed on the charge
		if (resequence) {
			boolean activeOffences = resequenceOffenceSeq(chargeBean.getXhbOffences());

			// CTX-304 If no active offences on the indictment charge then mark
			// it as obsolete
			if (ChargeTypes.INDICTMENT.getChargeType().equals(chargeBean.getChargeType()) && !activeOffences) {
				deleteChargeEntity(chargeBean);
			}
		}
	}

	private void deleteDefendantsOnOffenceEntities(Integer caseId, String chargeType, Collection defendantsOnOffence, String userDisplayName) {
		if (defendantsOnOffence != null && !defendantsOnOffence.isEmpty()) {
			Iterator iterator = defendantsOnOffence.iterator();
			while (iterator.hasNext()) {
				XhbDefendantOnOffence defendantOnOffence = (XhbDefendantOnOffence) iterator.next();
				defendantOnOffence.setObsInd(OBSOLETE);

				deleteDisposalEntites(defendantOnOffence.getXhbDisposal2s());
				deleteVerdictEntities(defendantOnOffence.getXhbVerdicts());
				deletePleaEntities(defendantOnOffence.getXhbPleas());
			}
			recalculateCaseRetentionPolicy(caseId, userDisplayName);
		}
	}

	private void recalculateCaseRetentionPolicy(Integer caseId, String userDisplayName) {
		log.debug("recalculateCaseRetentionPolicy("+caseId+")");
		try {
			XhibitDartsControllerBeanBusinessDelegate.DelegateFactory.getInstance().recalculateCaseRetentionPolicy(caseId, userDisplayName);
		} catch (FinderException e) {
			log.error("Failed to recalculate the retention policies for caseId "+caseId);
		}
	}
	
	private void deleteVerdictEntities(Collection verdicts) {
		if (verdicts != null && !verdicts.isEmpty()) {
			Iterator iterator = verdicts.iterator();
			while (iterator.hasNext()) {
				XhbVerdict verdict = (XhbVerdict) iterator.next();
				verdict.setObsInd(OBSOLETE);
			}
		}
	}

	private void deleteDisposalEntites(Collection disposals) {
		if (disposals != null && !disposals.isEmpty()) {
			Iterator iterator = disposals.iterator();
			while (iterator.hasNext()) {
				XhbDisposal2 disposal = (XhbDisposal2) iterator.next();
				disposal.setObsInd(OBSOLETE);

				deleteDisposalLineEntites(disposal.getXhbDisposalLines());
			}
		}
	}

	private void deleteDisposalLineEntites(Collection disposalLines) {
		if (disposalLines != null && !disposalLines.isEmpty()) {
			Iterator iterator = disposalLines.iterator();
			while (iterator.hasNext()) {
				XhbDisposalLine disposalLine = (XhbDisposalLine) iterator.next();
				disposalLine.setObsInd(OBSOLETE);
			}
		}
	}

	private void deleteDefendantChargeEntities(Collection defendantCharges) {
		if (defendantCharges != null && !defendantCharges.isEmpty()) {
			Iterator iterator = defendantCharges.iterator();
			while (iterator.hasNext()) {
				XhbDefendantCharge defendantCharge = (XhbDefendantCharge) iterator.next();
				defendantCharge.setObsInd(OBSOLETE);

				deleteVerdictEntities(defendantCharge.getXhbVerdicts());
				deletePleaEntities(defendantCharge.getXhbPleas());
			}
		}
	}

	private void deletePleaEntities(Collection pleas) {
		if (pleas != null && !pleas.isEmpty()) {
			Iterator iterator = pleas.iterator();
			while (iterator.hasNext()) {
				XhbPlea plea = (XhbPlea) iterator.next();
				plea.setObsInd(OBSOLETE);
			}
		}
	}

	private boolean resequenceOffenceSeq(Collection offences) {
		boolean success = false;
		if (offences != null && !offences.isEmpty()) {
			@SuppressWarnings("unchecked")
			ArrayList<XhbOffence> offencesList = new ArrayList<XhbOffence>(offences);
			Collections.sort(offencesList, OFFENCE_SEQ_ORDER);
			int i = 0;
			for (XhbOffence offence : offencesList) {
				if (!OBSOLETE.equalsIgnoreCase(offence.getObsInd())) {
					offence.setCrestOffenceSeqNo(new Integer(++i));
					success = true;
				}
			}
		}
		return success;
	}

	static final Comparator<XhbOffence> OFFENCE_SEQ_ORDER = new Comparator<XhbOffence>() {
		public int compare(XhbOffence o1, XhbOffence o2) {
			XhbOffence offence1 = o1;
			XhbOffence offence2 = o2;

			if (offence1 == null || offence2 == null || offence1.getCrestOffenceSeqNo() == null
					|| offence2.getCrestOffenceSeqNo() == null) {
				return 0;
			}

			// a negative integer, zero, or a positive integer as the first
			// argument is less than, equal to, or greater than the second
			if (offence1.getCrestOffenceSeqNo().intValue() < offence2.getCrestOffenceSeqNo().intValue()) {
				return -1;
			} else if (offence1.getCrestOffenceSeqNo().intValue() > offence2.getCrestOffenceSeqNo().intValue()) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	private void resequenceChargeSeq(Collection charges, String chargeType) {
		if (charges != null && !charges.isEmpty()) {
			@SuppressWarnings("unchecked")
			ArrayList<XhbCharge> chargesList = new ArrayList<XhbCharge>(charges);
			Collections.sort(chargesList, CHARGE_SEQ_ORDER);
			int i = 0;
			for (XhbCharge charge : chargesList) {
				if (!OBSOLETE.equalsIgnoreCase(charge.getObsInd())
						&& charge.getChargeType().equalsIgnoreCase(chargeType)) {
					charge.setCrestChargeSeqNo(new Integer(++i));
				}
			}
		}
	}

	static final Comparator<XhbCharge> CHARGE_SEQ_ORDER = new Comparator<XhbCharge>() {
		public int compare(XhbCharge o1, XhbCharge o2) {
			XhbCharge charge1 = o1;
			XhbCharge charge2 = o2;

			if (charge1 == null || charge2 == null || charge1.getCrestChargeSeqNo() == null
					|| charge2.getCrestChargeSeqNo() == null) {
				return 0;
			}

			// a negative integer, zero, or a positive integer as the first
			// argument is less than, equal to, or greater than the second
			if (charge1.getCrestChargeSeqNo().intValue() < charge2.getCrestChargeSeqNo().intValue()) {
				return -1;
			} else if (charge1.getCrestChargeSeqNo().intValue() > charge2.getCrestChargeSeqNo().intValue()) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	/**
	 * 
	 * @param ChargesLogBasicValue
	 * @throws EJBException
	 *             if an error occurs while saving to DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void addToChargeLog(ChargesLogBasicValue val, String userDisplayName) throws EJBException {
		ChargesLogMaintainer main = new ChargesLogMaintainer();
		main.create(val, userDisplayName);
	}

	/**
	 * 
	 * @param caseId
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getChargesLog(Integer caseId) {
		try {
			ArrayList<String> chargesLogsCollection = new ArrayList<String>();
			ArrayList<XhbChargesLog> chargesLogOr= (ArrayList<XhbChargesLog>)XhbChargesLogBeanHelper2.findNonObsoleteByCaseId(caseId);
			Comparator<XhbChargesLog> chargeSeq = new Comparator<XhbChargesLog>() {
				@Override
				public int compare(XhbChargesLog o1, XhbChargesLog o2) {
					return o1.getSequenceNo().compareTo(o2.getSequenceNo());
				}
			};
			Collections.sort(chargesLogOr,chargeSeq);
			for (XhbChargesLog chargesLog : chargesLogOr) {
				chargesLogsCollection.add(chargesLog.getChargesInfo());
			}
			return chargesLogsCollection;
		} catch (Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * 
	 * @param receipt
	 *            receiptDate
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateDefCourtAppeals(Timestamp receipt, String username) {
		try {
			DefendantOnCaseBasicValue defOnCaseBV = new DefendantOnCaseBasicValue();
			DefendantOnCaseMaintainer defOnCaseMaintainer = new DefendantOnCaseMaintainer();
			defOnCaseBV.setDateReceiptNoticeAppeal(receipt);
			defOnCaseMaintainer.update(defOnCaseBV, username);
		} catch (Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Generate the offence Id and save the entity for the joinder process
	 */
	private void saveOffence(XhbChargeBasicValue charge, OffenceValue offenceValue, String userDisplayName) {
		offenceValue.setOffenceID(null);
		offenceValue.setChargeID(charge.getChargeId());
		offenceValue.setCaseID(charge.getCaseId());
		// Step 1: insert new entry into xhb_address, called from
		// saveOffenceEntity
		// Step 2: insert new entry into xhb offence
		// Step 3: save defendants, this is called in the saveOffenceEntity
		// method
		XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(charge.getChargeId());

		// Adding in the appeal type if it has one (for A type cases)
		XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(chargeBean.getCaseId());
		if (caseBean != null && caseBean.getCaseType().equalsIgnoreCase("A") && caseBean.getCaseSubType() != null) {
			offenceValue.setAppealType(caseBean.getCaseSubType());
		}
		XhbOffence offenceBean = saveOffenceEntity(chargeBean, offenceValue, userDisplayName);

		offenceValue.setOffenceID(offenceBean.getOffenceId());
		offenceValue.setCrestOffenceSeqNo(offenceBean.getCrestOffenceSeqNo());		
	}

	/**
	 * Generate the charges Id for the joinder process
	 */
	private Integer getChargesId(ChargeValue chargeValue, String userDisplayName) {
		// Step 1: insert new entry into xhb_charge
		log.debug("Create a new Charge with ChargeValue " + chargeValue);
		XhbChargeBasicValue chargeBasicValue = new XhbChargeBasicValue();
		// Common charge values
		chargeBasicValue.setCaseId(chargeValue.getCaseID());
		chargeBasicValue.setChargeType("I");
		// Optional charge values
		if (chargeValue.getProsPaperServedDate() != null) {
			chargeBasicValue.setProsPaperServedDate(chargeValue.getProsPaperServedDate().getTime());
		}

		chargeBasicValue
				.setCrestChargeSeqNo(getNextCrestChargeSeqNum(chargeValue.getCaseID(), chargeValue.getChargeType()));
		// Create new row
		XhbCharge chargeBean = XhbChargeBeanHelper2.createLocal(chargeBasicValue);
		chargeBean.setCreatedBy(userDisplayName);

		// Step 2: insert into tables xhb_address, xhb_offence and
		// xhb_defendant_on_offence
		Iterator iter = chargeValue.getOffenceValues().iterator();
		while (iter.hasNext()) {
			OffenceValue offenceValue = (OffenceValue) iter.next();
			if (offenceValue.getAddressId() != null) {
				XhbAddress address = XhbAddressBeanHelper2.findByPrimaryKey(offenceValue.getAddressId());
				// setting the appeal type of the offence
				XhbCase caseVal = XhbCaseBeanHelper2.findByPrimaryKey(chargeValue.getCaseID());
				if (caseVal != null && caseVal.getCaseType().equalsIgnoreCase("A")
						&& caseVal.getCaseSubType() != null) {
					offenceValue.setAppealType(caseVal.getCaseSubType());
				}
				XhbAddressBasicValue addBasVal = address.getData();
				AddressValue addVal = new AddressValue(addBasVal);
				offenceValue.setAddressValue(addVal);
				offenceValue.setChargeID(chargeBean.getChargeId());
				saveOffenceEntity(chargeBean, offenceValue, userDisplayName);
			}
		}
		return chargeBean.getChargeId();
	}
}
