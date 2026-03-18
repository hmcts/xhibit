package uk.gov.courtservice.xhibit.client.order.gui.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationRequestValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
//import uk.gov.courtservice.xhibit.common.results.vos.authorise.CaseAuthorisationReturnValue;
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
	private static final Logger log = CSServices.getLogger(OrderInitialDataHelper.class);
	private boolean NO_DVLA = false;

	private OrderInitialDataVO model = null;

	public D20OffenceValidator(OrderInitialDataVO model) {
		this.model = model;
	}

	/**
	 * Retrieves
	 * 
	 * @return The authorisation results for the current case
	 * @throws ResultsControllerException
	 */
	private AuthorisationReturnValue getVerdictReturnValue() throws ResultsControllerException{
		AuthorisationReturnValue returnValue =null;
   	 	AuthorisationRequestValue request = new AuthorisationRequestValue();
       
        Integer caseId = model.getXhibitCaseId();
        request.setCaseId(caseId);
        request.setCourtLogDate(Calendar.getInstance(Locale.getDefault()));
       

        AuthorisationValue[] authorisation =  XhibitDelegateHelper.getResults2Delegate().getAuthorisable(caseId);
        request.setDefendantsToAuthorise(authorisation);

        request.setCourtLogDate(Calendar.getInstance());
        
        Integer shid =  model.getHearingID();
        
        returnValue = XhibitDelegateHelper.getResults2Delegate().validateD20Order(request,
                shid);
        
        return returnValue;
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
			//CaseAuthorisationReturnValue item1 = returnValue.getCaseAuthorisationReturnValue();
			//String[] fail = item1.getCaseFailures();

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
	 * Method handles check for any driving orders on offence where the parent
	 * folder is "DO"
	 * 
	 * @param defOnOffence
	 *            ID of defendant on offence
	 * @param thisDefendant
	 *            Defendants information
	 * @param caseId
	 *            Xhbibit case ID
	 * @param isDisqu
	 *            indicator of whether or not to search the whole "DO" folder or
	 *            just those in the "DIS" subfolder (i.e disqualification only)
	 * @return
	 */
	public boolean checkForDOInOff(Integer defOnOffence, DefendantValue thisDefendant, Integer caseId,
			boolean isDisqu) {
		String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ ": ";
		try {

			List<DisposalValue> disposals = Arrays
					.asList(XhibitDelegateHelper.getDefendantDelegate().getDisposalsForDefendantOnOffence(defOnOffence,
							getDefendantOnCaseId(thisDefendant.getDefendantID(), caseId), caseId));
			List<DisposalValue> matchingDisposals = new ArrayList<DisposalValue>();
			for (int i = 0; i < disposals.size(); i++) {
				DisposalValue disp = disposals.get(i);
				if (disposals.get(i).getDefendantOnOffenceId().equals(defOnOffence))
					matchingDisposals.add(disp);
			}

			DisposalValue[] disp = new DisposalValue[matchingDisposals.size()];
			matchingDisposals.toArray(disp);
			return checkForDODisposal(disp, isDisqu);
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
	 * @param thisDefendant
	 *            Defendants information
	 * @param caseId
	 *            Xhbibit case ID
	 * @return true if there is not a non-obsolete DISTOT disposal, otherwise
	 *         false
	 */
	public boolean checkForOBSDISTOT(Integer defOnOffence, DefendantValue thisDefendant, Integer caseId) {

		try {

			List<DisposalValue> disposals = Arrays
					.asList(XhibitDelegateHelper.getDefendantDelegate().getDisposalsForDefendantOnOffence(defOnOffence,
							getDefendantOnCaseId(thisDefendant.getDefendantID(), caseId), caseId));
			List<DisposalValue> matchingDisposals = new ArrayList<DisposalValue>();
			for (int i = 0; i < disposals.size(); i++) {
				DisposalValue disp = disposals.get(i);
				if (disposals.get(i).getDefendantOnOffenceId().equals(defOnOffence))
					matchingDisposals.add(disp);
			}

			DisposalValue[] disp = new DisposalValue[matchingDisposals.size()];
			matchingDisposals.toArray(disp);

			for (DisposalValue disposal : matchingDisposals) {
				if (disposal.getRefDisposalType().getDisposalCode().equals("DISTOT")) {
					if (disposal.getObsInd() != null) {
						if (disposal.getObsInd().equals("Y")) {
							return true;
						}
					}
					if (disposal.getCourtType().matches("M||C"))
						return false;
				}
			}
		} catch (Exception e) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": ";
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
	 * @param thisDefendant
	 *            Defendants information
	 * @param caseId
	 *            Xhbibit case ID
	 * @return true if there is not a non-obsolete "DISINT" disposal, otherwise
	 *         false
	 */
	public boolean checkForDISINTInOff(Integer defOnOffence, DefendantValue thisDefendant, Integer caseId) {

		try {
			List<DisposalValue> disposals = Arrays
					.asList(XhibitDelegateHelper.getDefendantDelegate().getDisposalsForDefendantOnOffence(defOnOffence,
							getDefendantOnCaseId(thisDefendant.getDefendantID(), caseId), caseId));
			List<DisposalValue> matchingDisposals = new ArrayList<DisposalValue>();
			for (int i = 0; i < disposals.size(); i++) {
				DisposalValue disp = disposals.get(i);
				if (disposals.get(i).getDefendantOnOffenceId().equals(defOnOffence))
					matchingDisposals.add(disp);
			}

			DisposalValue[] disp = new DisposalValue[matchingDisposals.size()];
			matchingDisposals.toArray(disp);
			for (DisposalValue value : matchingDisposals) {
				if (value.getRefDisposalType().getDisposalCode().equals("DISINT")) {

					return true;
				}
			}
		} catch (Exception e) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": ";
			e.printStackTrace();
			log.error(logPath + "unable to retrieve disposals");
		}
		return false;
	}

	/**
	 * Gets the current selected defendants ID
	 * 
	 * @return the defendant's ID if it can be retrieved
	 */
	public Integer getDefendantId() {
		if (this.model.isBCase()) {
			log.debug("OrderInitialDataHelper.getDefendantId: Getting defendantid for a b case");
			// Need to get the defendantId
			try {
				Collection allDefdts = XhibitDelegateHelper.getCaseDelegate()
						.getDefendants(this.model.getXhibitCaseId());
				if (allDefdts != null) {
					for (Object currDef : allDefdts) {
						DefendantValue thisDef = (DefendantValue) currDef;
						if ((thisDef != null) && (thisDef.getSurName().equals(this.model.getDefendantName()))) {
							log.debug("OrderInitialDataHelper.getDefendantId: defendantid returned is: "
									+ thisDef.getDefendantID());
							return thisDef.getDefendantID();
						}
					}
				}
			} catch (DefendantControllerException dce) {
				log.error("Error getting defendant for B case " + this.model.getCaseID());
				dce.printStackTrace();
			} catch (CaseControllerException cce) {
				log.error("Error getting defendant for B case " + this.model.getCaseID());
				cce.printStackTrace();
			}
		}

		log.debug("OrderInitialDataHelper.getDefendantId: defendantid returned is: " + this.model.getDefendantID());
		return this.model.getDefendantID();
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
		if (this.model.isBCase() || this.model.getDefendantOnCaseID() == null) {
			if (defendantId != null) {
				try {
					// Now need to get the defendant on case using defid an
					// caseid
					log.debug("OrderInitialDataHelper.getDefendantOnCaseId: Finding defoncase with defid=" + defendantId
							+ " and caseid=" + caseId);
					DefendantOnCaseValue docv = XhibitDelegateHelper.getDefendantDelegate()
							.getDefendantOnCaseDetails(defendantId, caseId);
					return docv.getDefendantOnCaseBVO().getId();
				} catch (DefendantControllerException dce) {
					log.error("Error getting defendant for B case " + this.model.getCaseID());
					dce.printStackTrace();
				}
			} else {
				log.debug("No defendant found for the B caseid: " + this.model.getCaseID());
			}
		} else {
			return this.model.getDefendantOnCaseID();
		}

		return null;
	}

	/**
	 * Method handles the redirection of the type of check to do for disposals
	 * 
	 * @param disposals
	 *            the disposals to check
	 * @param disqualificationOnly
	 *            flag indicates when true that we only want to search for
	 *            disqualification disposals
	 * @return true if the disposals found for the offence are a form of driving
	 *         disposal, otherwise false
	 */
	public boolean checkForDODisposal(DisposalValue[] disposals, boolean disqualificationOnly) {
		try {
			boolean passed = false;

			// Designated Parent off all listed driving offences

			for (int i = 0; i < disposals.length; i++) {

				DisposalValue dispose = disposals[i];

				if (!dispose.getRefDisposalType().getDisposalCode().equals("DISINT")) {
					try {
						int number = XhibitDelegateHelper.getDefendantDelegate()
								.getParentRefDisposalId(dispose.getRefDisposalTypeId(), model.getXhibitCourtId());
						if (disqualificationOnly) {
							passed = !passed ? isMatchingDrivingDisqual(number) : passed;
						} else {
							passed = !passed ? isMatchingDrivingOrder(number) : passed;
						}

					} catch (Exception e) {
						e.printStackTrace();
						String logPath = this.getClass().getName() + "."
								+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
						e.printStackTrace();
						log.error(logPath + "unable to retrieve parent ID for disposals");
					}
				}

				if (passed)
					return true;
			}

		} catch (Exception e) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": ";
			e.printStackTrace();
			log.error(logPath + "unable to retrieve disposals");
		}
		return false;
	}

	/**
	 * Method checks the disposals code matches any Driving disposal
	 * 
	 * @param number
	 *            the parent number of the disposal
	 * @return true if the disposal has a parent number matching Driving
	 *         disposal parents, otherwise false
	 */
	private boolean isMatchingDrivingOrder(int number) {
		switch (number) {
		case 500:
		case 501:
		case 505:
			return true;
		}
		return false;
	}

	/**
	 * Method checks the disposals code matches any disqualification disposal
	 * 
	 * @param number
	 *            the parent number of the disposal
	 * @return true if the disposal has a parent number matching
	 *         disqualification disposal parents, otherwise false
	 */
	private boolean isMatchingDrivingDisqual(int number) {
		switch (number) {

		case 501:
		case 505:
			return true;
		}
		return false;
	}

	/**
	 * High level method checks all offences held on a case
	 * 
	 * @param caseId
	 *            The case ID to check
	 * @return true if the offence is valid, otherwise false
	 */
	public boolean checkForValidOffence(Integer caseId) {
		boolean passed = false;
		ChargeCompositeValue ccv;
		try {
			ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseId, true);
			Collection ref_D20Offence = XhibitDelegateHelper.getBizRefDelegate().getOffenceCodes();
			Collection<DefendantValue> allDefendants = ccv.getAllDefendants();
			if ((allDefendants != null) && (allDefendants.size() > 0)) {

				DefendantValue thisDefendant = null;
				Iterator<DefendantValue> dv = allDefendants.iterator();
				while (dv.hasNext() && !passed) {
					thisDefendant = dv.next();
					//DefendantBasicValue dbv = convertToDBV(thisDefendant);

					// For each defendant on this case get all the disposals.
					Collection items = ccv.getCharges();
					Collection<OffenceValue> offences = new ArrayList<OffenceValue>();
					for (int i = 0; i < items.size(); i++) {
						ChargeValue charge = (ChargeValue) items.toArray()[i];
						offences.addAll(ccv.getOffenceValues(charge.getChargeID()));
					}

					if (!offences.isEmpty()) {
						passed = checkforD20offence(caseId, ref_D20Offence, thisDefendant, offences);
					} else {
						NO_DVLA = true;
						return true;
					}

				}
			}
		} catch (Exception e) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": ";
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
	 *            disqualificaitions only
	 * @return true if a driving disposal can be found, otherwise false
	 */
	public boolean checkForDrivingDisposals(Integer caseId, boolean isDisqual) {
		boolean passed = false;
		ChargeCompositeValue ccv;
		try {
			ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseId, true);
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
	public boolean checkForDODisposalCase(Integer caseId, DefendantValue thisDefendant, boolean isDisqal) {
		try {

			DisposalValue[] disposals = XhibitDelegateHelper.getDefendantDelegate().getDisposalsForDefendantOnCase(
					getDefendantOnCaseId(thisDefendant.getDefendantID(), caseId), caseId);
			return checkForDODisposal(disposals, isDisqal);
		}

		catch (Exception e) {
			String logPath = this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ ": ";
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
	public boolean checkforD20offence(Integer caseId, Collection ref_D20Offence, DefendantValue thisDefendant,
			Collection<OffenceValue> offences) {

		boolean passed = false;
		boolean noDVLA = true;
		for (int i = 0; i < offences.size(); i++) {
			try {
				OffenceValue oof = (OffenceValue) offences.toArray()[i];
				XhbRefOffenceBasicValue offence = XhibitDelegateHelper.getDefendantDelegate()
						.getOffenceRefFromOffenceCode(oof.getRefOffenceID());
				DefendantOnOffenceComplexValue defOnOffence = oof.getDefendantOnOffence(thisDefendant.getDefendantID());

				if (defOnOffence != null) { // defOnOffence can be returned as a
											// null...
					Integer value = defOnOffence.getDefendantOnOffenceId();
					if (offence.getDvlcCode() != null) {
						noDVLA = false;
						if (ref_D20Offence.contains(offence.getDvlcCode())) {

							passed = checkForDOInOff(value, thisDefendant, caseId, false);

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				String logPath = this.getClass().getName() + "."
						+ Thread.currentThread().getStackTrace()[1].getMethodName() + ": ";
				e.printStackTrace();
				log.error(logPath + "unable to retrieve defendant on offence or offence on case with ID = " + caseId);
			}
		}
		if (noDVLA) {
			NO_DVLA = true;
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
		Collection ref_D20Offence = XhibitDelegateHelper.getBizRefDelegate().getOffenceCodes();
		XhbRefOffenceBasicValue offence = XhibitDelegateHelper.getDefendantDelegate().getOffenceRefFromOffenceCode(value.getRefOffenceID());
		if(offence!=null)
		{
			if(offence.getDvlcCode()== null)
				return true;
			if(ref_D20Offence.contains(offence.getDvlcCode()) &&  !offence.getDvlcCode().startsWith("NE"))
			{
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

	/**
	 * Getter
	 * 
	 * @return
	 */
	public boolean hasNoDVLA() {
		return NO_DVLA;
	}
}
