package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLink;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResult;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResultBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.GetChargesHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkHelperValue;

public class OffenceLinkValidationHelper {

	private static final String FAIL_TO_APPEAR_CASES = "BA76001|BA76002";

	private DefendantHelper defendantHelper = new DefendantHelper();
	private static final Logger log = CSServices.getLogger(OffenceLinkValidationHelper.class);
	private List<XhbD20OffenceLinkBasicValue> valuesToBeDisplayed = new ArrayList<XhbD20OffenceLinkBasicValue>();
	private D20OffenceLinkHelperValue helper;
	private AuthorisationHelper authHelper;
	private D20OffenceValidator validator;
	
	private ResultsCompositeValue results;
	private ChargeCompositeValue charges;
	
	private static final String YES ="Y";
	private String TT99_DVLA_CODE = "TT99";
	private static final String ORIGINAL_OFFENCE_CODE = "ZZ99998";
	private static final String NE98_DVLA_CODE = "NE98";
	private Integer defendantOnCaseID;
	private Integer caseID;
	private Integer defendantID;
	private Integer SHID;

	/**
	 * 
	 * @param helper
	 * @param authHelper
	 */
	public void validateResults(D20OffenceLinkHelperValue helper, AuthorisationHelper authHelper) {

		log.debug("validateResults - BEGIN");
		
		validator = new D20OffenceValidator(helper, authHelper);
		defendantOnCaseID = helper.getDefendantOnCaseId();
		this.caseID = helper.getXhibitCaseId();
		this.defendantID = helper.getDefendantId();
		this.authHelper = authHelper;
		this.helper = helper;
		this.SHID = helper.getHearingID();
		try {
			results = Results2WorkFlow.getResults(caseID, SHID);
			charges = new GetChargesHelper().getCharges(caseID, false);
		} catch (ChargeControllerException e) {
			log.error("Error in validateResults: ",e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (ResultsControllerException e) {
			log.error("Error in validateResults: ",e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}

		validateAllOffenceLinks();

		// If there are no values that have been derived for display on the
		// offence link popup then set a warning message to that effect
		if (valuesToBeDisplayed.isEmpty()) {
			log.debug("There are no values that have been retrieved for display in the offence link pop-up for caseId="+caseID+", defendantOncaseid="+defendantOnCaseID+" and defendantId="+defendantID);
			authHelper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.noValidOffences");
		}
		log.debug("validateResults - END");
	}

	/**
	 * Deal with all the data in XHB_D20_OFFENCE_LINK Check existing data and
	 * obsolete/remove the link if the equivalent offence has been
	 * obsoleted/removed For all other offences process them one by one
	 */
	@SuppressWarnings("unchecked")
	private void validateAllOffenceLinks() {
		log.debug("validateAllOffenceLinks - BEGIN");
		
		removeOrphanObsoleteOffenceLinks(defendantOnCaseID);

		try {
			// Get all current offences for the defendant and loop through to
			// check each one
			ArrayList<OffenceValue> offences = new ArrayList<OffenceValue>(
					charges.getCurrentOffencesForDefendant(defendantID));

			removeOriginalCharges(offences);

			for (OffenceValue offence : offences) {

				// Ignore FAILURE TO APPEAR offences
				if (offence.getOffenceCode().matches(FAIL_TO_APPEAR_CASES)) {
					continue;
				}

				DefendantOnOffenceComplexValue defOnOff = offence.getDefendantOnOffence(defendantID);

				if (defOnOff != null) {

					XhbRefOffenceBasicValue offenceRef = XhbRefOffenceBeanHelper2
							.findByPrimaryKeyValue(offence.getRefOffenceID());

					// If the defendant on offence or the offence is obsolete
					// then obsolete data in XHB_D20_OFFENCE_LINK
					// Otherwise process the offence to see if it (a) already
					// exists and (b) if it needs to be added to eligible
					// offences for a D20
					if (YES.equals(defOnOff.getObsInd())) {
						log.debug("validateAllOffenceLinks: Obsolete defendant on offence found");
						log.debug("defendantOnOffenceID = " + defOnOff.getDefendantOnCaseId());
						try {
							// If it exists, make the equivalent D20Offence link
							// obsolete too, as the defendant on offence is
							// obsolete
							XhbD20OffenceLinkBasicValue offenceLinkBV = getSingleOffLinkObs(defOnOff, offence, null);
							if (offenceLinkBV != null) {
								setSingleOffLinktoObs(offenceLinkBV);
							}

						} catch (Exception e) {
							log.error("validateAllOffenceLinks: Offence Link retrieval, or updating has failed", e);
							e.printStackTrace();
						}
					} else {
						processOffence(offence, offenceRef, defOnOff);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error in validateAllOffenceLinks: ", e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}

		// Validate missing DVLA records
		validator.checkForValidOffence(caseID, charges, defendantID);
		
		log.debug("validateAllOffenceLinks - END");
	}

	/**
	 * Removes original charges from the offence selection list if present 
	 * -- Occurs when an offence value of offence code ZZ99998  is present
	 * -- Will remove any of the ID's found in the original offences listed 
	 * -- as an original offence value.
	 * @param offences the offence list to filter out original offences
	 */
	private void removeOriginalCharges(ArrayList<OffenceValue> offences) {
		ArrayList<Integer> itemsToRemove = new ArrayList<Integer>();
		
		for (int i=0; i < offences.size(); i++) {
			OffenceValue offence = offences.get(i);
			if(offence.getOffenceCode().equals(ORIGINAL_OFFENCE_CODE)) {
				itemsToRemove.add(i);
			}
		}
		
		for(int i=0; i < offences.size(); i++){
			OffenceValue offence = offences.get(i);
			if(itemsToRemove.contains(offence.getOffenceID())) {
				offences.remove(i);
			}
		}
	}

	
	/**
	 * For a given record in XHB_D20_OFFENCE_LINK set it to obsolete
	 * 
	 * @param offenceLinkBV
	 */
	private void setSingleOffLinktoObs(XhbD20OffenceLinkBasicValue offenceLinkBV) {
		log.debug("setSingleOffLinktoObs: Current offence link set to obsolete ");
		offenceLinkBV.setObsInd(YES);
		createUpdateOffenceLinkForOffence(offenceLinkBV);
	}
	
	/**
	 * For a given record in XHB_D20_OFFENCE_LINK set it to live and make sure the conviction date is correct
	 * 
	 * @param offenceLinkBV
	 */
	private XhbD20OffenceLinkBasicValue setSingleOffLinktoLive(Integer defOnOffId, Integer chargeId, XhbD20OffenceLinkBasicValue offenceLinkBV) {
		log.debug("setSingleOffLinktoLive: Current offence link set to live ");
		offenceLinkBV.setObsInd(null);
		offenceLinkBV = processCaseTypeData(defOnOffId, offenceLinkBV, chargeId);
		return createUpdateOffenceLinkForOffence(offenceLinkBV);
	}

	/**
	 * High level method that redirects the offence to its appropriate method
	 * for validation / offence link population: Either its a driving offence or
	 * its not
	 * 
	 * - If its a driving offence then we need to check for driving disposals
	 * --- If it has driving disposals choose the 1st one (for Final Sentence
	 * D20) --- If it is an Interim D20 then it must have a DISINT disposal ---
	 * If it has no driving disposals then this will result in a warning to the
	 * user but might not be terminal
	 * 
	 * - If its not a driving offence but it has a driving disposal then as
	 * above --- For a Final Sentence D20 choose the 1st one we find --- For an
	 * Interim D20 then it must have a DISINT disposal
	 * 
	 * - If its not a driving offence and it has no driving disposals then skip,
	 * these are to be ignored
	 * 
	 * @param offence
	 *            the Offence to check
	 * @param offenceRef
	 *            the reference data for the aforementioned offence
	 */
	private void processOffence(OffenceValue offence, XhbRefOffenceBasicValue offenceRef, DefendantOnOffenceComplexValue defOnOff) {

		log.debug("processOffence - BEGIN");
		if (!validator.checkValidVerdict()) {
			String dvlaCode = offenceRef.getDvlcCode();
			
			if (dvlaCode != null && !dvlaCode.equals(NE98_DVLA_CODE)) {
				processDrivingOffences(offence, offenceRef, dvlaCode, defOnOff);
			} else {
				processNonDrivingOffences(offence, offenceRef, defOnOff);
			}
		}
		log.debug("processOffence - END");
	}

	/**
	 * Method handles high level processing of driving offences for validation
	 * 
	 * @param offence
	 *            The driving offence to begin validating
	 * @param offenceRef
	 *            The offence reference for the aforementioned offence
	 * @param dvlaCode
	 *            the DVLA code for the offence link
	 */
	private void processDrivingOffences(OffenceValue offence, XhbRefOffenceBasicValue offenceRef, String dvlaCode, DefendantOnOffenceComplexValue defOnOff) {

		log.debug("processDrivingOffences - BEGIN");

		try {
			XhbD20OffenceLinkBasicValue offenceLinkBV = getSingleOffLinkObs(defOnOff, offence, dvlaCode);

			if (offenceLinkBV.getD20OffenceLinkId() == null) {
				offenceLinkBV = createNewOffenceLink(offence, defOnOff.getSeqNo(), dvlaCode);
			}

			checkExistingOffenceLink(offence, offenceRef, offenceLinkBV, defOnOff);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in processDrivingOffences: ",e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Method handles high level processing of non-driving offences for
	 * validation
	 * 
	 * @param offence
	 *            The non-driving offence to begin validating
	 * @param offenceRef
	 *            The offence reference for the aforementioned offence
	 */
	private void processNonDrivingOffences(OffenceValue offence, XhbRefOffenceBasicValue offenceRef, DefendantOnOffenceComplexValue defOnOff) {
		log.debug("processNonDrivingOffences - BEGIN");
		try {

			XhbD20OffenceLinkBasicValue offenceLinkBV = getSingleOffLinkObs(defOnOff, offence, NE98_DVLA_CODE);

			if (offenceLinkBV.getD20OffenceLinkId() == null) {				
				offenceLinkBV = createNewOffenceLink(offence, defOnOff.getSeqNo(), NE98_DVLA_CODE);
			}
			checkExistingOffenceLink(offence, offenceRef, offenceLinkBV, defOnOff);

		} catch (Exception e) {
			try {
				XhbD20OffenceLinkBasicValue offenceLinkBV = getSingleOffLinkObs(defOnOff, offence, NE98_DVLA_CODE);
				offenceLinkBV = createNewOffenceLink(offence, defOnOff.getSeqNo(), NE98_DVLA_CODE);
				createUpdateOffenceLinkForOffence(offenceLinkBV);
				checkExistingOffenceLink(offence, offenceRef, offenceLinkBV, defOnOff);
			} catch (Exception ex) {
				e.printStackTrace();
				log.error("processNonDrivingOffences: failed to retrieve / create offence link",e);
			}
		}
		log.debug("processNonDrivingOffences - END");
	}

	/**
	 * Method handles the redirection to the appropriate method handling Case
	 * type specific data
	 * 
	 * @param defOnOffID
	 * @param d20olbv
	 * @param chargeId
	 * @return
	 */
	private XhbD20OffenceLinkBasicValue processCaseTypeData(Integer defOnOffID, XhbD20OffenceLinkBasicValue d20olbv, Integer chargeId) {
		log.debug("processCaseTypeData(defOnOffID="+defOnOffID+", chargeId="+chargeId+")");
		if (helper.isTrialCase()) {
			d20olbv = processTrialData(defOnOffID, d20olbv, chargeId);
		} else if (helper.isSentenceCase()) {
			d20olbv = processSentenceData(defOnOffID, d20olbv);
		} else if (helper.isaCase()) {
			d20olbv = getAppealOffenceData(defOnOffID, d20olbv);
		}

		return d20olbv;
	}

	/**
	 * 
	 * @param plea
	 * @param verdict
	 * @param breach
	 * @return
	 */
	private boolean defendantIsGuilty(PleaValue plea, VerdictValue verdict, BreachValue breachValue) {
		boolean isGuilty = false;
		if (plea != null && plea.isGuilty()) {
			log.debug("defendantIsGuilty(PLEA) = Guilty");
			isGuilty = true;
		} else if (verdict != null && verdict.isGuiltyVerdict()) {
			log.debug("defendantIsGuilty(VERDIT) = Guilty");
			isGuilty = true;
		} else if (breachValue != null && YES.equals(breachValue.getPlea())) {
			log.debug("defendantIsGuilty(BREACH) = Guilty");
			isGuilty = true;
		}  else {
			log.debug("defendantIsGuilty() = Not Guilty");
		}
		return isGuilty;
	}
	
	/**
	 * Method handles all information processed for verdicts / pleas when the
	 * current case is a trial
	 * 
	 * @param defOnOffID
	 * @param d20olbv
	 * @return the amended of the offence link
	 */
	private XhbD20OffenceLinkBasicValue processTrialData(Integer defOnOffID, XhbD20OffenceLinkBasicValue d20olbv, Integer chargeId) {

		try {

			VerdictValue verdict = results.getVerdict(defOnOffID);
			PleaValue plea = results.getPlea(defOnOffID);
			BreachValue breachValue = null;
			
			// If this is a breach then get the plea
			if (plea == null) {
				ChargeValue chargeValue = getChargeValue(chargeId);
				if (chargeValue != null) {
					breachValue = chargeValue.getBreachValue();
				}
			}

			if (defendantIsGuilty(plea, verdict, breachValue)) {
				if (plea != null && plea.isGuilty()) {
					// Use the plea data in this case
					if (plea.getPleaBasicValue().getArraignmentDate() != null) {
						d20olbv.setConvictionDate(plea.getPleaBasicValue().getArraignmentDate());
					} else {
						d20olbv.setConvictionDate(AuthorisationWorkFlow.DUMMY_DATE);
					}
				} else if (verdict != null && verdict.getRefVerdictCode() != null) {
					d20olbv.setConvictionDate(verdict.getVerdictDate());
				} else if (breachValue != null && breachValue.getDatePut() != null) {
					d20olbv.setConvictionDate(breachValue.getDatePut().getTime());
				}

				if (plea != null && plea.isGuiltyOfLesser()) {
					d20olbv.setRefOffenceId(plea.getAltRefOffenceId());
				} else if (verdict != null && verdict.isGuiltyOfLesser()) {
					d20olbv.setRefOffenceId(verdict.getAltRefOffenceId());
				}
			} else {
				this.setSingleOffLinktoObs(d20olbv);
			}

		} catch (Exception e) {
			log.error("Error in processTrialData: ", e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}

		return d20olbv;
	}
	
	private ChargeValue getChargeValue(Integer chargeId) {
		ChargeValue chargeValue;
		Collection charges = this.charges.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            // Is this the correct Charge
            if (chargeId.equals(chargeValue.getChargeID())) {
                return chargeValue;
            }
        }
        return null;
	}

	/**
	 * 
	 * @param defOnOffID
	 * @param d20olbv
	 * @return
	 */
	private XhbD20OffenceLinkBasicValue processSentenceData(Integer defOnOffID, XhbD20OffenceLinkBasicValue d20olbv) {
		try {

			if (authHelper.getXhbCase().getMagConvictionDate() != null) {
				d20olbv.setConvictionDate(authHelper.getXhbCase().getMagConvictionDate());
			} else {
				d20olbv.setConvictionDate(AuthorisationWorkFlow.DUMMY_DATE);
			}

			// Unlike T cases there is no need to check verdict for S(entence)
			// cases - as by their nature the defendant has been found Guilty

		} catch (Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}

		return d20olbv;
	}

	/**
	 * If the user is attempting to create an Interim D20 order then the offence
	 * must have a DISINT disposal If the user is attempting to create a Final
	 * Sentence D20 then the offence must have a non-DISINT driving disposal
	 * 
	 * @param defOnOffID
	 * @param defOnOffence
	 * @param isDisq
	 * @return
	 */
	private boolean doValidationByD20OrderType(Integer defOnOffID, DefendantValue defOnOffence) {

		if (helper.isD20Interim()) {
			if (validator.checkForDISINTInOff(defOnOffID, defOnOffence.getDefendantID(), caseID)) {
				return true;
			} else {
				authHelper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.noDisIntFound");
				return false;
			}

		} else {
			return validator.checkForDrivingDisposalsOnOffence(defOnOffID, defOnOffence.getDefendantID(), caseID);
		}
	}

	/**
	 * Method checks the offence associated with the offence link meets the D20
	 * Offence criteria i.e has at least one driving disposal ( or
	 * disqualification if non-driving offence)
	 * 
	 * @param offence
	 *            the offence record to check
	 * @param offenceRef
	 *            the offence ref to check for DVLA code
	 * @param seq
	 *            the sequence number found for the defendant on offence
	 * @param offenceLinkBV
	 *            the offence link to query
	 */
	private void checkExistingOffenceLink(OffenceValue offence, XhbRefOffenceBasicValue offenceRef, 
			XhbD20OffenceLinkBasicValue offenceLinkBV, DefendantOnOffenceComplexValue defOnOff) {

		Integer defOnOffID = defOnOff.getDefendantOnOffenceId();
		
		// Apply the rules for the case type
		offenceLinkBV = processCaseTypeData(defOnOffID, offenceLinkBV, offence.getChargeID());
		if (!offenceRef.getRefOffenceId().equals(offenceLinkBV.getRefOffenceId())) {
			log.debug("Lesser offence to be used on offence link");

			offenceRef = XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(offenceLinkBV.getRefOffenceId());	
		}

		// Added catch to stop any offence links which are obsolete
		if (YES.equals(offenceRef.getObsInd()))
			return;

		String dvlaCode = offenceRef.getDvlcCode() == null ? NE98_DVLA_CODE : offenceRef.getDvlcCode();
		DefendantValue defOnOffence = null;

		try {

			defOnOffence = defendantHelper.findByDefId(defendantID);

			if (doValidationByD20OrderType(defOnOffID, defOnOffence)) {

				boolean needsChange = offenceLinkBV.getObsInd() == null;

				// TT99 is not possible for an Interim case; TT99 requires that
				// the offence has a DISTOT applied
				boolean isThereADISTOT = !helper.isD20Interim() && validator.checkForDISTOT(defOnOffID, defOnOffence.getDefendantID(), caseID);
				if (isThereADISTOT) {
					checkForTT99(offence, offenceRef, offenceLinkBV, defOnOff, dvlaCode, defOnOffence);
				} else {
					if (!YES.equals(offenceLinkBV.getObsInd())) {
						needsChange = false;
					}

					if (!dvlaCode.equals(offenceLinkBV.getDvlaOffenceCode()) && !needsChange) {
						// Means we have a change in the offence code here, so
						// make the retrieved offence link obsolete
						makeSingleOffObs(defendantOnCaseID, defOnOff.getSeqNo());
						addOffenceLink(offence, dvlaCode, defOnOff);
					} else {
						XhbD20OffenceLinkBasicValue refOffenceLinkBV = getSingleOffLinkObs(defOnOff, offence, null);
						if (!offenceLinkBV.equals(refOffenceLinkBV)) {
							offenceLinkBV = createUpdateOffenceLinkForOffence(offenceLinkBV);
						}

						addValueToBeDisplayed(offenceLinkBV);
					}
				}
			} else {
				makeSingleOffObs(defendantOnCaseID, defOnOff.getSeqNo());
				authHelper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.noDOWithDisposals");
				if (offenceLinkBV.getDvlaOffenceCode() != null && !offenceLinkBV.getDvlaOffenceCode().startsWith("NE")) {
					authHelper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.disposalsMissing");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in checkExistingOffenceLink: ", e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);	
		}
	}

	/**
	 * 
	 * @param defOnOffID
	 * @param d20olbv
	 * @return
	 */
	private XhbD20OffenceLinkBasicValue getAppealOffenceData(Integer defOnOffID, XhbD20OffenceLinkBasicValue d20olbv) {

		try {

			VerdictValue verdict = results.getVerdict(defOnOffID);
			if (verdict != null && verdict.getRefAppResultId() != null) {
				if (authHelper.getXhbCase().getMagConvictionDate() != null) {
					d20olbv.setConvictionDate(authHelper.getXhbCase().getMagConvictionDate());
				} else {
					d20olbv.setConvictionDate(AuthorisationWorkFlow.DUMMY_DATE);
				}
				XhbRefAppResult refAppResult = XhbRefAppResultBeanHelper2.findByPrimaryKey(verdict.getRefAppResultId());
				// Appeal Result
				if ("ACALO".equalsIgnoreCase(refAppResult.getAppResultCode())) {
					try {
						XhbRefOffenceBasicValue refOffence = XhbRefOffenceBeanHelper2
								.findByPrimaryKeyValue(verdict.getAltRefOffenceId());
						if (refOffence != null) {
							d20olbv.setDvlaOffenceCode(refOffence.getDvlcCode());
							d20olbv.setRefOffenceId(refOffence.getRefOffenceId());
						}
					} catch (XhbRefOffenceBeanNotFoundException ex) {
						log.debug(ex.getMessage());
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return d20olbv;
	}

	/**
	 * Method carries out check for an offence with the DVLA code "TT99"
	 * 
	 * @param offence
	 * @param offenceRef
	 * @param offenceLinkBV
	 * @param defOnOff
	 * @param dvlaCode
	 * @param defOnOffence
	 */
	private void checkForTT99(OffenceValue offence, XhbRefOffenceBasicValue offenceRef,
			XhbD20OffenceLinkBasicValue offenceLinkBV, DefendantOnOffenceComplexValue defOnOff, String dvlaCode, DefendantValue defOnOffence) {
		log.debug("checkForTT99 - BEGIN");
		if (offenceLinkBV.getDvlaOffenceCode() != null) {
			boolean isOtherDrivingDisposal = validator.checkForDISTOTOnlyOffences(defOnOff.getDefendantOnOffenceId(), defendantID, caseID);
			addTT99Offence(offence, defOnOff, offenceLinkBV, true, isOtherDrivingDisposal);
		} else {
			processCaseTypeData(defOnOff.getDefendantOnOffenceId(), offenceLinkBV, offence.getChargeID());
			addTT99Offence(offence, defOnOff, offenceLinkBV, false, false);
		}
		log.debug("checkForTT99 - END");
	}

	/**
	 * Method handles the creation / update of details to be in database for
	 * TT99 offences
	 * 
	 * @param offence
	 * @param defOnOff
	 * @param offenceRef
	 * @param existed
	 *            indicates that the record existed in a prior session of xhibit
	 * @param isOtherDrivingDisposal
	 * 		If the driving disposals on the offence are only a DISTOT and optionally a DISINT then we only add a TT99 and not the explicit DVLA offence code
	 */
	private void addTT99Offence(OffenceValue offence, DefendantOnOffenceComplexValue defOnOff, XhbD20OffenceLinkBasicValue offenceRef, boolean existed, boolean isOtherDrivingDisposal) {
		try {
			if(existed) {
				makeSingleOffLinkObs(offenceRef.getDefendantOnCaseId(), defOnOff.getSeqNo(), TT99_DVLA_CODE);
			}

			// Create an initial record for the regular DVLA code but only if its not an offence where the only disposals are
			// driving disposals that are not either a DISINT or DISTOT
			// If the only disposal is a DISTOT then no regular offence is added and its only a TT99
			if (offenceRef.getD20OffenceLinkId() == null) {
				offenceRef = createUpdateOffenceLinkForOffence(offenceRef);
			}
			
			// Add the original offence to the popup
			if (isOtherDrivingDisposal) {
				addValueToBeDisplayed(offenceRef);
			}

			// Process the TT99 record
			XhbD20OffenceLinkBasicValue offenceRefTT99 = getSingleOffLinkObs(defOnOff, offence, TT99_DVLA_CODE);
			if (offenceRefTT99.getD20OffenceLinkId() == null) {	
				// If we haven't already created a TT99 record then do it now
				offenceRefTT99 = new XhbD20OffenceLinkBasicValue(offenceRef);
				offenceRefTT99.setD20OffenceLinkId(null);
				offenceRefTT99.setDvlaOffenceCode(TT99_DVLA_CODE);
				offenceRefTT99 = createUpdateOffenceLinkForOffence(offenceRefTT99);
				addValueToBeDisplayed(offenceRefTT99);
			} else {
				// This is a TT99 record, so display it
				offenceRefTT99 = setSingleOffLinktoLive(defOnOff.getDefendantOnOffenceId(), offence.getChargeID(), offenceRefTT99);
				addValueToBeDisplayed(offenceRefTT99);
			}
		} catch (Exception e) {
			log.error("Error in addTT99Offence: ",e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Makes a single record obsolete
	 * 
	 * @param defOnCase
	 *            Defendant on Case ID
	 * @param seq
	 *            sequence number
	 */
	private void makeSingleOffObs(Integer defOnCase, Integer seq) {

		makeSingleOffLinkObs(defOnCase, seq, ""); // Pass empty dvlaCode across from this method
		log.debug("Offence link with following credentials made obsolete");
		log.debug("defendantOnCase - " + defOnCase);
		log.debug("sequenceNumber -" + seq);
	}

	/**
	 * Method sends completed Offence Link Model to database
	 * 
	 * @param offence
	 * @param dvlaCode
	 * @param defOnOff
	 */
	private void addOffenceLink(OffenceValue offence, String dvlaCode, DefendantOnOffenceComplexValue defOnOff) {
		XhbD20OffenceLinkBasicValue offenceLinkBV = getSingleOffLinkObs(defOnOff, offence, null);
		if (offenceLinkBV.getD20OffenceLinkId() == null) {
			offenceLinkBV = createNewOffenceLink(offence, defOnOff.getSeqNo(), dvlaCode);
		}

		processCaseTypeData(getDefOnOff(offence.getOffenceID(), this.defendantID).getDefendantOnOffenceId(),
				offenceLinkBV, offence.getChargeID());
		offenceLinkBV = createUpdateOffenceLinkForOffence(offenceLinkBV);

		addValueToBeDisplayed(offenceLinkBV);
	}

	/**
	 * Creates new OffenceLink Object with necessary data
	 * 
	 * @param offence
	 * @param seq
	 * @param dvlaCode
	 */
	private XhbD20OffenceLinkBasicValue createNewOffenceLink(OffenceValue offence, Integer seq, String dvlaCode) {
		log.debug("createNewOffenceLink(SeqNo="+seq+",dvlaCode="+dvlaCode+")");
		XhbD20OffenceLinkBasicValue offenceLinkBV;
		offenceLinkBV = new XhbD20OffenceLinkBasicValue();
		offenceLinkBV.setDvlaOffenceCode(dvlaCode);
		offenceLinkBV.setSeqNo(seq);
		offenceLinkBV.setCreatedBy(helper.getUserName());
		offenceLinkBV.setLastUpdatedBy(helper.getUserName());
		offenceLinkBV.setCreationDate(new Date(System.currentTimeMillis()));
		offenceLinkBV.setDefendantOnCaseId(defendantOnCaseID);
		offenceLinkBV.setLastUpdateDate(new Date(System.currentTimeMillis()));
		offenceLinkBV.setRefOffenceId(offence.getRefOffenceID());
		offenceLinkBV.setVersion(1);

		return offenceLinkBV;
	}

	/**
	 * Method retrieves defendant on offence value based on input credentials
	 * 
	 * @param offenceID
	 *            the offence ID to search
	 * @param defendantID
	 *            the defendant to search for
	 * @return the Defendant On Offence record if found, otherwise null
	 */
	private DefendantOnOffenceComplexValue getDefOnOff(Integer offenceID, Integer defendantID) {

		try {
			ArrayList<OffenceValue> offences = new ArrayList<OffenceValue>(charges.getOffenceValuesForDefendant(defendantID));
			for (OffenceValue offence : offences) {
				if (offence.getOffenceID().equals(offenceID)) {
					return offence.getDefendantOnOffence(defendantID);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getDefOnOff: Unable to retrive a matching offence with the following credentials: /n offenceID= "
					+ offenceID + " /n defendantID= " + defendantID);
		}

		log.warn("We havent found a defendant on offence for offenceID=" + offenceID + " and defendantID=" + defendantID);
		return null;
	}	
	
	/**
	 * Removes all obsolete offences links based defCaseID
	 */
	@SuppressWarnings("unchecked")
	private void removeOrphanObsoleteOffenceLinks(Integer defOnCaseId) {
		log.debug("removeOrphanObsoleteOffenceLinks - BEGIN");
		try {
			List<XhbD20OffenceLink> offenceLinks = getOffenceLinks(defOnCaseId, false);
			List<XhbDefendantOnOffence> defOnOffs = new ArrayList<XhbDefendantOnOffence>(
					XhbDefendantOnOffenceBeanHelper2.findByDefOnCaseIdAndObsInd(defOnCaseId));

			// Loop through the obsolete defendant on offences
			for (XhbDefendantOnOffence defOnOff : defOnOffs) {
				if (YES.equals(defOnOff.getObsInd())) { 
					// Loop through related D20 offence links
					for (XhbD20OffenceLink offLink : offenceLinks) {
						// If this offence and seq match then update the ObsInd to Y
						if (offLink.getRefOffenceId().equals(defOnOff.getXhbOffenceData().getRefOffenceId())
								&& offLink.getSeqNo().equals(defOnOff.getSeqNo())) {
							log.debug("removeOrphanObsoleteOffenceLinks(SeqNo="+offLink.getSeqNo()+") - Set ObsInd=Y");
							// Set link to obsolete
							offLink.setObsInd(YES);
							XhbD20OffenceLinkBeanHelper2.update(offLink.getData());
						}
					}
				} else {
					// Loop through making sure the refOffence is correct on the seq
					for (XhbD20OffenceLink offLink : offenceLinks) {
						// If this isn't the offence but seq match then update the ObsInd to Y
						if (!offLink.getRefOffenceId().equals(defOnOff.getXhbOffenceData().getRefOffenceId())
								&& offLink.getSeqNo().equals(defOnOff.getSeqNo())) {
							log.debug("removeOrphanObsoleteOffenceLinks(SeqNo="+offLink.getSeqNo()+") - Set ObsInd=Y");
							// Set link to obsolete
							offLink.setObsInd(YES);
							XhbD20OffenceLinkBeanHelper2.update(offLink.getData());
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error in removeOrphanObsoleteOffenceLinks: ", e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		log.debug("removeOrphanObsoleteOffenceLinks - END");
	}
	

	/**
	 * retrieves all offences links based defCaseID
	 * 
	 * @param defOnCase
	 *            defendantOnCaseID
	 * 
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	@SuppressWarnings("unchecked")
	private List<XhbD20OffenceLink> getOffenceLinks(Integer defOnCaseId, boolean includeObsolete) {
		try {
			if (includeObsolete) {
				return new ArrayList<XhbD20OffenceLink>(XhbD20OffenceLinkBeanHelper2.findByDefendantOnCaseIdIncObsolete(defOnCaseId));
			} else {
				return new ArrayList<XhbD20OffenceLink>(XhbD20OffenceLinkBeanHelper2.findByDefendantOnCaseId(defOnCaseId));
			}

		} catch (Exception e) {
			log.error("Error in getOffenceLinks: ", e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Sets an existing offence link to obsolete
	 */
	private void makeSingleOffLinkObs(Integer defOnCaseId, Integer seq, String dvlaCode) {
		ArrayList<XhbD20OffenceLink> offences = new ArrayList<XhbD20OffenceLink>(getOffenceLinks(defOnCaseId, true));
		for (XhbD20OffenceLink offence : offences) {
			if (offence.getSeqNo().equals(seq)) {
				if ((dvlaCode.length() > 0) && (dvlaCode.equals(offence.getDvlaOffenceCode()))) {  // Update all records for this defoncase / seqno / dvlacode combo
					log.debug("makeSingleOffLinkObs(seq="+seq+",dvlaCode="+dvlaCode+")");
					offence.setObsInd(YES);
					offence.setDvlaOffenceCode(dvlaCode);
					createUpdateOffenceLinkForOffence(offence.getData());
				} else if (dvlaCode.length() == 0) { // Default update all records for this defoncase / seqno combo, regardless of dvlacode
					log.debug("makeSingleOffLinkObs(seq="+seq+")");
					offence.setObsInd(YES);
					createUpdateOffenceLinkForOffence(offence.getData());
				}
			}
		}
	}

	/**
	 * Sets an existing offence link to obsolete
	 * 
	 * @param defOnOff
	 * @param offence
	 * @return
	 */
	private XhbD20OffenceLinkBasicValue getSingleOffLinkObs(DefendantOnOffenceComplexValue defOnOff, OffenceValue offence, String dvlaCode) {

		final List<XhbD20OffenceLink> offences = getOffenceLinks(defendantOnCaseID, true);
		XhbD20OffenceLinkBasicValue offenceLinkBV = new XhbD20OffenceLinkBasicValue();
		
		for (XhbD20OffenceLink thisOffence : offences) {
			if (thisOffence.getSeqNo().equals(defOnOff.getSeqNo()) &&
					(dvlaCode == null || dvlaCode.equals(thisOffence.getDvlaOffenceCode()))) {
				if(!YES.equalsIgnoreCase(thisOffence.getObsInd())) {
                    // Get the live one
					offenceLinkBV = ((XhbD20OffenceLinkBasicValue) thisOffence.getData());
					return offenceLinkBV;
				} else if (offenceLinkBV.getD20OffenceLinkId() == null) {
					// Get the obsolete one for now incase there isn't a live one
					offenceLinkBV = ((XhbD20OffenceLinkBasicValue) thisOffence.getData());
				}
			}
		}
		
		// Found one but its obsolete, so make it live
		if (offenceLinkBV.getD20OffenceLinkId() != null &&
				YES.equalsIgnoreCase(offenceLinkBV.getObsInd())) {
			log.debug("getSingleOffLinkObs(Id="+offenceLinkBV.getD20OffenceLinkId()+",SeqNo="+offenceLinkBV.getSeqNo()+") - Found. Setting obsInd to Live");
			offenceLinkBV = setSingleOffLinktoLive(defOnOff.getDefendantOnOffenceId(), offence.getChargeID(), offenceLinkBV);
		} else if (offenceLinkBV.getD20OffenceLinkId() != null) {
			log.debug("getSingleOffLinkObs(Id="+offenceLinkBV.getD20OffenceLinkId()+",SeqNo="+offenceLinkBV.getSeqNo()+") - Found");
		}
		
		return offenceLinkBV;
	}
	
	/**
	 * 
	 * @param offenceLink
	 * @return
	 */
	private boolean offenceLinkExists(XhbD20OffenceLinkBasicValue offenceLink) {
		XhbD20OffenceLink testCase = XhbD20OffenceLinkBeanHelper2.findByPrimaryKey(offenceLink.getPrimaryKey());

		return (testCase != null);
	}

	/**
	 * Creates or updates an offence link
	 * 
	 * @param offenceLink
	 *            the offence link to create / update
	 * @return true if successful update/creation, otherwise false
	 * @throws EJBException
	 *             if an error occurs while retrieving from DB
	 * 
	 */
	private XhbD20OffenceLinkBasicValue createUpdateOffenceLinkForOffence(XhbD20OffenceLinkBasicValue offenceLink) {
		try {
			if (offenceLink.getD20OffenceLinkId() != null) {
				if (!offenceLinkExists(offenceLink)) {
					offenceLink = XhbD20OffenceLinkBeanHelper2.create(offenceLink);
					log.debug("createUpdateOffenceLinkForOffence(Id="+offenceLink.getD20OffenceLinkId()+",SeqNo="+offenceLink.getSeqNo()+") - Created");
				} else {
					XhbD20OffenceLinkBeanHelper2.update(offenceLink);
					log.debug("createUpdateOffenceLinkForOffence(Id="+offenceLink.getD20OffenceLinkId()+",SeqNo="+offenceLink.getSeqNo()+") - Updated");
				}

			} else {
				// Only create a link that is not obsolete
				if (!YES.equals(offenceLink.getObsInd())) {
					offenceLink = XhbD20OffenceLinkBeanHelper2.create(offenceLink);
					log.debug("createUpdateOffenceLinkForOffence(Id="+offenceLink.getD20OffenceLinkId()+",SeqNo="+offenceLink.getSeqNo()+") - Created(2)");
				}
			}
			return offenceLink;
		} catch (Exception e) {
			log.error("Error in createUpdateOffenceLinkForOffence: ", e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public D20OffenceValidator getValidator() {
		return validator;
	}

	/**
	 * Get the D20 Offence Links to be displayed.
	 * @return List<XhbD20OffenceLinkBasicValue>
	 */
	public List<XhbD20OffenceLinkBasicValue> getD20OffenceLinks() {
		return valuesToBeDisplayed;
	}
	
	private void addValueToBeDisplayed(XhbD20OffenceLinkBasicValue offenceLinkBV) {
		if (!YES.equals(offenceLinkBV.getObsInd())) {
			log.debug("valuesToBeDisplayed.add(Id=" + offenceLinkBV.getD20OffenceLinkId()+")");
			valuesToBeDisplayed.add(offenceLinkBV);
		}
	}

}
