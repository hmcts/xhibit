package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_menu.XhbRefDisposalMenuBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_menu.XhbRefDisposalMenuBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseHelper;
import uk.gov.courtservice.xhibit.business.services.charge.GetChargesHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseHelper;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationRequestValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.CaseAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkHelperValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationFailureValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationReturnValue;

/**
 * Class handles the validation of disposals on offences for D20 report
 * generation
 * 
 * @author Ross McArthur
 * 
 */
public class D20OffenceValidator {

	private static final String DISTOT = "DISTOT";
	private static final String DISINT = "DISINT";
	private static final String COURT_CRITERIA = "M||C";
	private static final String YES = "Y";
	private static final Logger log = CSServices.getLogger(D20OffenceValidator.class);
	private DefendantOnCaseHelper defendantOnCaseHelper = new DefendantOnCaseHelper();
	private CaseHelper caseHelper = new CaseHelper();
	private D20OffenceLinkHelperValue helper;
	private AuthorisationHelper authHelper;
	AuthorisationReturnValue authReturn = null;

	public D20OffenceValidator(D20OffenceLinkHelperValue helper, AuthorisationHelper authHelper) {
		this.helper = helper;
		this.authHelper = authHelper;
	}
	
	// Empty constructor
	public D20OffenceValidator() {}

	/**
	 * Retrieves
	 * 
	 * @return The authorisation results for the current case
	 * @throws ResultsControllerException
	 */
	private AuthorisationReturnValue getVerdictReturnValue() throws ResultsControllerException{

		if (authReturn == null) {
			AuthorisationRequestValue request = new AuthorisationRequestValue();
       
			Integer caseId = helper.getXhibitCaseId();
			request.setCaseId(caseId);
			request.setCourtLogDate(Calendar.getInstance(Locale.getDefault()));
       
			AuthorisationValue[] authorisation = AuthorisationWorkFlow.getAuthorisableByDefendantOnCaseId(helper.getDefendantOnCaseId());
			request.setDefendantsToAuthorise(authorisation);
			request.setCourtLogDate(Calendar.getInstance());
        
			Integer shid = helper.getHearingID();
        
			authReturn = AuthorisationWorkFlow.validateD20Order(request, shid);
		}
		
		return authReturn;
	}

	/**
	 * Method checks for a valid verdict being recorded against all offences
	 * 
	 * @return true if any offences do not have verdicts otherwise false
	 */
	public boolean checkValidVerdict() {
		boolean failed = false;
		
		try {
			AuthorisationReturnValue returnValue = getVerdictReturnValue();

			DefendantAuthorisationReturnValue[] defCase = returnValue.getDefendantAuthorisationReturnValue();
			CaseAuthorisationReturnValue item1 = returnValue.getCaseAuthorisationReturnValue();

			for (int i = 0; i < defCase.length; i++) {
				DefendantAuthorisationReturnValue item = defCase[i];

				DefendantAuthorisationFailureValue[] failures = item.getFailures();
				for (int j = 0; j < failures.length; j++) {
					if (failures[j] != null) {
						if (failures[j].getReasonCode().equals("authorise.appealresult.notrecorded")) {
							failed = true;
						}
					}
				}
			}
		} catch (ResultsControllerException exception) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": ";
			exception.printStackTrace();
			log.error(logPath + "Unable to retrive results of authorisation attempt");
		}
		return failed;

	}

	/**
	 * Method handles check for any driving disposals on offence where the parent
	 * folder is "DO"
	 * 
	 * @param defOnOffence
	 *            ID of defendant on offence
	 * @param defendantId
	 *            Defendant ID
	 * @param caseId
	 *            XHIBIT case ID
	 * @return
	 */
	public boolean checkForDrivingDisposalsOnOffence(Integer defOnOffence, Integer defendantId, Integer caseId) {
		String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
		
		try {
			List<DisposalValue> disposals = Arrays.asList(defendantOnCaseHelper.getDisposalsForDefendantOnOffence(
					defOnOffence, getDefendantOnCaseId(defendantId, caseId), caseId));
			List<DisposalValue> matchingDisposals = new ArrayList<DisposalValue>();
			for (int i = 0; i < disposals.size(); i++) {
				DisposalValue disp = disposals.get(i);
				if (disposals.get(i).getDefendantOnOffenceId().equals(defOnOffence))
					matchingDisposals.add(disp);
			}

			DisposalValue[] disp = new DisposalValue[matchingDisposals.size()];
			matchingDisposals.toArray(disp);
			return hasADrivingDisposal(disp);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(logPath + "unable to retrieve disposals / ");
		}

		return false;
	}

	/**
	 * Method handles check for any driving disposals on offence where the
	 * disposal is "DISTOT" only
	 * 
	 * @param defOnOffence
	 *            ID of defendant on offence
	 * @param defendantId
	 *            Defendant ID
	 * @param caseId
	 *            XHIBIT case ID
	 * @return true if there is not a non-obsolete DISTOT disposal, otherwise
	 *         false
	 */
	public boolean checkForDISTOT(Integer defOnOffenceId, Integer defendantId, Integer caseId) {
		return checkForDisposalType(defOnOffenceId, defendantId, caseId, DISTOT);
	}
		
	private boolean checkForDisposalType(Integer defOnOffenceId, Integer defendantId, Integer caseId, String disposalType) {	
		try {
			List<DisposalValue> disposals = Arrays.asList(defendantOnCaseHelper.getDisposalsForDefendantOnOffence(
					defOnOffenceId, getDefendantOnCaseId(defendantId, caseId), caseId));
			for (DisposalValue disposal : disposals) {
				// This disposal is for the passed in defendantOnOffenceId and M or C court
				if (disposal.getDefendantOnOffenceId().equals(defOnOffenceId) && 
						disposal.getCourtType().matches(COURT_CRITERIA)) {
					//... and the disposal type code is <disposalType>
					if (disposalType.equals(disposal.getRefDisposalType().getDisposalCode())) {
						// ...and not obsolete then return TRUE
						if (!YES.equalsIgnoreCase(disposal.getObsInd())) {
							return true;
						}						
					}
				}
			}
		} catch (Exception e) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
			e.printStackTrace();
			log.error(logPath + "unable to retrieve disposals");
		}

		return false;
	}
	
	
	/**
	 * A variation on the above method
	 * Method handles check for any driving disposals on offence where the
	 * disposal is "DISTOT" only AND that there are no other driving disposals
	 * except optionally a "DISINT" 
	 * 
	 * @param defOnOffence
	 *            ID of defendant on offence
	 * @param defendantId
	 *            Defendant ID
	 * @param caseId
	 *            XHIBIT case ID
	 * @return true if there is not a non-obsolete DISTOT disposal, otherwise false
	 */
	public boolean checkForDISTOTOnlyOffences(Integer defOnOffence, Integer defendantId, Integer caseId) {

		try {
			List<DisposalValue> disposals = Arrays.asList(defendantOnCaseHelper.getDisposalsForDefendantOnOffence(
					defOnOffence, getDefendantOnCaseId(defendantId, caseId), caseId));
			boolean foundDISTOT = false, foundOtherDrivingDisposalType = false;
			for (DisposalValue disposal : disposals) {
				// This disposal is for the passed in defendantOnOffenceId and M or C court
				if (disposal.getDefendantOnOffenceId().equals(defOnOffence) &&
						disposal.getCourtType().matches(COURT_CRITERIA)) {
					//... and the disposal type code is DISTOT
					if (DISTOT.equals(disposal.getRefDisposalType().getDisposalCode())) {
						if (!YES.equalsIgnoreCase(disposal.getObsInd())) {
							foundDISTOT = true;
						}
					} 
					//...else not a DISINT disposal type code
					else if (!DISINT.equals(disposal.getRefDisposalType().getDisposalCode())) {
						if (isDrivingDisposal(disposal.getRefDisposalTypeId(), helper.getXhibitCourtId())) {
							foundOtherDrivingDisposalType = true;
						}
					}
				}
				if (foundDISTOT && foundOtherDrivingDisposalType) {
					return true;
				}
			}
		} catch (Exception e) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
			e.printStackTrace();
			log.error(logPath + "unable to retrieve disposals");
		}

		return false;
	}
	

	/**
	 * Method handles check for any driving disposals on offence where the
	 * disposal is "DISINT" only
	 * 
	 * @param defOnOffence
	 *            ID of defendant on offence
	 * @param defendantId
	 *            Defendant ID
	 * @param caseId
	 *            XHIBIT case ID
	 * @return true if there is not a non-obsolete "DISINT" disposal, otherwise
	 *         false
	 */
	public boolean checkForDISINTInOff(Integer defOnOffence, Integer defendantId, Integer caseId) {
		return checkForDisposalType(defOnOffence, defendantId, caseId, DISINT);
	}
	
	/**
	 * Gets the current selected defendants ID
	 * 
	 * @return the defendant's ID if it can be retrieved
	 */
	public Integer getDefendantId() {
		if (this.helper.isBreachCase()) {
			log.debug("OrderInitialDataHelper.getDefendantId: Getting defendantid for a b case");
			// Need to get the defendantId
			try {
				Collection allDefdts = caseHelper.getDefendants(this.helper.getXhibitCaseId());
				if (allDefdts != null) {
					for (Object currDef : allDefdts) {
						DefendantValue thisDef = (DefendantValue) currDef;
						if ((thisDef != null) && (thisDef.getSurName().equals(this.helper.getDefendantName()))) {
							log.debug("OrderInitialDataHelper.getDefendantId: defendantid returned is: "
									+ thisDef.getDefendantID());
							return thisDef.getDefendantID();
						}
					}
				}
			} catch (DefendantControllerException dce) {
				log.error("Error getting defendant for B case " + this.helper.getXhibitCaseId());
				dce.printStackTrace();
			} catch (CaseControllerException cce) {
				log.error("Error getting defendant for B case " + this.helper.getXhibitCaseId());
				cce.printStackTrace();
			}
		}

		log.debug("OrderInitialDataHelper.getDefendantId: defendantid returned is: " + this.helper.getDefendantId());
		return this.helper.getDefendantId();
	}

	private String getActiveMethod() {
		return D20OffenceValidator.class.getName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + ": ";
	}

	/**
	 * Retrieves defendant on case ID for current defendant and case
	 * 
	 * @param defendantId
	 *            -1 means we dont yet know the defendantid
	 * @param caseId
	 *            the case to search
	 * @return the defendant on offence ID if it is found
	 */
	public Integer getDefendantOnCaseId(Integer defendantId, Integer caseId) {

		if (defendantId != null) {
			if (defendantId.intValue() == -1) {
				defendantId = getDefendantId();
			}
		}
		if (this.helper.isBreachCase() || this.helper.getDefendantOnCaseId() == null) {
			if (defendantId != null) {
				try {
					// Now need to get the defendant on case using defid an
					// caseid
					log.debug(getActiveMethod() + " Finding defoncase with defid=" + defendantId + " and caseid="
							+ caseId);
					DefendantOnCaseValue docv = defendantOnCaseHelper.getDefendantOnCaseDetails(defendantId, caseId);
					return docv.getDefendantOnCaseBVO().getId();
				} catch (DefendantControllerException dce) {
					log.error("Error getting defendant for B case " + this.helper.getXhibitCaseId());
					dce.printStackTrace();
				}
			} else {
				log.debug("No defendant found for the B caseid: " + this.helper.getXhibitCaseId());
			}
		} else {
			return this.helper.getDefendantOnCaseId();
		}

		return null;
	}

	/**
	 * Method handles the redirection of the type of check to do for disposals
	 * 
	 * @param disposals
	 *            the disposals to check
	 * @return true if the disposals found for the offence are a form of driving
	 *         disposal, otherwise false
	 */
	public boolean hasADrivingDisposal(DisposalValue[] disposals) {
		String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
		boolean passed = false;
		
		try {
			// Designated Parent off all listed driving offences

			for (int i = 0; i < disposals.length; i++) {
				DisposalValue dispose = disposals[i];

				if (!DISINT.equals(dispose.getRefDisposalType().getDisposalCode())) {
					try {
						passed = isDrivingDisposal(dispose.getRefDisposalTypeId(), helper.getXhibitCourtId());

					} catch (Exception e) {
						e.printStackTrace();
						log.error(logPath + "unable to retrieve parent ID for disposals");
					}
				}
				if (passed) {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(logPath + "unable to retrieve disposals");
		}

		return passed;
	}

	public Integer getParentRefDisposalId(Integer refDisposalId, Integer courtId) {
		Integer parentId = null;

		XhbRefDisposalTypeBasicValue xrdtbv = XhbRefDisposalTypeBeanHelper2.findByPrimaryKeyValue(refDisposalId);
		if ((xrdtbv != null) && (xrdtbv.getDisposalCode() != null)) {
			String disposalCode = xrdtbv.getDisposalCode();
			XhbRefDisposalMenuBasicValue[] parentRefDisposalMenuColl = XhbRefDisposalMenuBeanHelper2
					.findRSByDisposalCodeAndCourtIdValue(disposalCode, courtId);
			// Some disposal codes may have more than one parent but that's ok
			// as any of them is fine - in that case just return the first one
			if (parentRefDisposalMenuColl.length >= 1) {
				XhbRefDisposalMenuBasicValue xrdmbv = parentRefDisposalMenuColl[0];
				parentId = xrdmbv.getParent();
			}
		}

		return parentId;
	}

	/**
	 * High level method checks all offences held on a case
	 * 
	 * @param caseId
	 *            The case ID to check
	 * @param ccv
	 *            The ChargeCompositeValue to check
	 * @return true if the offence is valid, otherwise false
	 */
	public boolean checkForValidOffence(Integer caseId, ChargeCompositeValue ccv, Integer defendantId) {
		String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
		
		boolean passed = false;
		
		try {
			Collection ref_D20Offence = helper.getRefD20Offences();
			Collection<DefendantValue> allDefendants = ccv.getAllDefendants();
			if ((allDefendants != null) && (allDefendants.size() > 0)) {

				DefendantValue thisDefendant = null;
				Iterator<DefendantValue> dv = allDefendants.iterator();
				while (dv.hasNext() || !passed) {
					thisDefendant = dv.next();
					if(defendantId.equals(thisDefendant.getDefendantID())){
						// For each defendant on this case get all the disposals.
						Collection items = ccv.getCharges();
						Collection<OffenceValue> offences = new ArrayList<OffenceValue>();
						for (int i = 0; i < items.size(); i++) {
							ChargeValue charge = (ChargeValue) items.toArray()[i];
							offences.addAll(ccv.getOffenceValues(charge.getChargeID()));
						}

						if (!offences.isEmpty()) {
							passed = isADrivingOffenceWithADrivingDisposal(caseId, ref_D20Offence, thisDefendant.getDefendantID(), offences);
						} else {
							authHelper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.noDVLA");
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(logPath + "unable to process cases for case with ID " + caseId);
		}

		return passed;
	}

	/**
	 * Method retrieves all defendants starts the process of finding if those
	 * offences have a driving disposal
	 * 
	 * @param caseId
	 *            the case ID to query
	 * @param isDisqual
	 *            flag indicates that the search will be limited to
	 *            disqualifications only
	 * @return true if a driving disposal can be found, otherwise false
	 */
	public boolean checkForDrivingDisposals(Integer caseId, boolean isDisqual) {
		boolean passed = false;
		ChargeCompositeValue ccv;
		try {
			ccv = new GetChargesHelper().getCharges(caseId, true);
			Collection<DefendantValue> allDefendants = ccv.getAllDefendants();
			if ((allDefendants != null) && (allDefendants.size() > 0)) {
				DefendantValue thisDefendant = null;
				Iterator<DefendantValue> dv = allDefendants.iterator();
				while (dv.hasNext()) {
					thisDefendant = dv.next();
					passed = checkForDODisposalCase(caseId, thisDefendant, isDisqual);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": ";
			log.error(logPath + "unable to retrieve charges");
		}

		return passed;
	}

	/**
	 * Method retrieves all disposals for defendants and retrieves if the case
	 * has any driving disposals
	 * 
	 * @param caseId
	 *            the case ID to check
	 * @param thisDefendant
	 *            the defendants record
	 * @param isDisqal
	 *            flag to indicate if the search is limited to disqualifications
	 *            only
	 * @return
	 */
	public boolean checkForDODisposalCase(Integer caseId, DefendantValue thisDefendant, boolean isDisqual) {
		String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
		
		try {
			DisposalValue[] disposals = defendantOnCaseHelper.getDisposalsForDefendantOnCase(
					getDefendantOnCaseId(thisDefendant.getDefendantID(), caseId), caseId);
			return hasADrivingDisposal(disposals);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(logPath + "unable to retrieve disposals");
		}

		return false;
	}

	/**
	 * 
	 * Checks for a D20Offence and if present, see if it has a driving disposal
	 * 
	 * @param caseId
	 * @param passed
	 * @param ref_D20Offence
	 * @param thisDefendant
	 * @param offences
	 * @return true if one is present and has a DO disposal
	 */
	public boolean isADrivingOffenceWithADrivingDisposal(Integer caseId, Collection ref_D20Offence, Integer defendantId, Collection<OffenceValue> offences) {
		
		String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";

		boolean passed = false;
		boolean noDVLA = true;
		for (int i = 0; i < offences.size(); i++) {
			try {
				OffenceValue offValue = (OffenceValue) offences.toArray()[i];
				XhbRefOffenceBasicValue offence = XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(offValue.getRefOffenceID());
				DefendantOnOffenceComplexValue defOnOffence = offValue.getDefendantOnOffence(defendantId);

				if (defOnOffence != null) { // defOnOffence can be returned as a null...if so then skip as we are not interested
					// Is a driving offence...
					if (offence.getDvlcCode() != null) {
						noDVLA = false;
						
						// If the DVLC code attached to the offence is not NE (i.e. not a non-driving offence) and it exists then its a driving offence
						if (ref_D20Offence.contains(offence.getDvlcCode()) && !offence.getDvlcCode().startsWith("NE")) {
							passed = checkForDrivingDisposalsOnOffence(defOnOffence.getDefendantOnOffenceId(), defendantId, caseId);
						}
					} 
					// Else a non-driving offence but with a driving disposal - set the noDVLA flag if we haven't already
					else if (noDVLA && checkForDrivingDisposalsOnOffence(defOnOffence.getDefendantOnOffenceId(), defendantId, caseId)) {
						noDVLA = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(logPath + "unable to retrieve defendant on offence or offence on case with ID = " + caseId);
			}
		}
		if (noDVLA) {
			authHelper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.noDVLA");
			return true;
		}
		
		return passed;
	}

	/**
	 * Method checks for driving offences using an Offence Value to query
	 * 
	 * @param value
	 *            the offence to check
	 * @return true if the inputted offence is a driving offence (Not starting
	 *         with "NE")) otherwise false
	 */
	public boolean checkForDrivingOffence(OffenceValue value) {
		Collection ref_D20Offence = helper.getRefD20Offences();
		XhbRefOffenceBasicValue offence = XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(value.getRefOffenceID());
		if (offence != null) {
			if(offence.getDvlcCode()== null)
				return false;                   //  No (or Null) DVLC code is a non-driving offence, so return false
			if (ref_D20Offence.contains(offence.getDvlcCode()) && !offence.getDvlcCode().startsWith("NE")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Converts defendant value to defendant basic value
	 * 
	 * @param dv
	 *            the defendant value to convert
	 * @return the converted Defendant value as Defendant Basic Value
	 */
	public DefendantBasicValue convertToDBV(DefendantValue dv) {
		DefendantBasicValue dbv = new DefendantBasicValue();
		dbv.setId(dv.getDefendantID());
		dbv.setCourtID(dv.getCourtID());
		dbv.setFirstName(dv.getFirstName());
		dbv.setMiddleName(dv.getMiddleName());
		dbv.setSurname(dv.getSurName());
		dbv.setAddressID(dv.getAddressId());
		dbv.setGender(dv.getGender());
		dbv.setDateOfBirth(dv.getDateOfBirth());

		return dbv;
	}

	private boolean isDrivingDisposal(Integer refDisposalTypeId, Integer courtId) {
		int number = getParentRefDisposalId(refDisposalTypeId, courtId);
		return AuthorisationWorkFlow.isDrivingDisposal(number);
	}
	
}
