package uk.gov.courtservice.xhibit.client.order.screens.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAppResultCriteria;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * An ArrayList for the offence to add to the D20 order
 * 
 * @author guthriec
 *
 */
public class OrderOffenceModel {

	private static final Logger log = CSServices.getLogger(OrderOffenceModel.class);

	private static final String TT99 = "TT99";
	
	private ArrayList<OffenceModel> offences;
	private Integer caseID;
	private Integer defendantID;
	private ArrayList<String> convictionDates;
	private boolean finished;
	private boolean okEnabled;
	private int clickCount;

	public OrderOffenceModel() {
		this.offences = new ArrayList<OffenceModel>();
		this.setOkEnabled(false);
		this.setClickCount(0);
	}

	/**
	 * @return returns true for when the ok button is pressed. false when it is
	 *         not
	 */
	public boolean getFinished() {
		return finished;
	}

	/**
	 * sets the finished state
	 * 
	 * @param finished
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Integer getCaseID() {
		return caseID;
	}

	public void setCaseID(Integer caseID) {
		this.caseID = caseID;
	}

	public Integer getDefendantID() {
		return defendantID;
	}

	public void setDefendantID(Integer defendantID) {
		this.defendantID = defendantID;
	}


	/**
	 * @return List of offences to for that can be added to the D20 order
	 */
	public ArrayList<OffenceModel> getOffences() {
		return offences;
	}

	/**
	 * Method sorts the content of the offence model by sequence number 
	 */
	public void sortOffenceModel()
	{
		if ( getOffences()!= null && ! getOffences().isEmpty()) {
			Collections.sort( getOffences(), sortByOffenceModel);
		}
	}
	
	/**
	 * 
	 * @return
	 */

	/**
	 * Get the list of selected offences to add to the Order
	 * 
	 * @return An arrayList of the pair of OffenceCode & OffenceModel
	 */
	public ArrayList<OffencePair> getSelectedOffences() {
		log.debug("Entry:: getSelectedOffences");
		ArrayList<OffencePair> returnList = new ArrayList<OffencePair>();

		try {

			ChargeCompositeValue charge = XhibitDelegateHelper.getChargeDelegate().getCharges(this.getCaseID(), true);
			boolean isAppealCase = charge.getCaseBasicValue().getCaseType().equalsIgnoreCase("A");
			ArrayList<OffenceValue> offences = new ArrayList<OffenceValue>(
					charge.getOffenceValuesForDefendant(this.getDefendantID()));
			ResultsCompositeValue results = XhibitDelegateHelper.getResults2Delegate().getResults(caseID);
			ArrayList<OffenceModel> listOfOffences = getOffences();

			for (OffenceModel model : listOfOffences) {
				boolean isChecked = model.isChecked();

				if (isChecked) {
					for (int i = 0; i < offences.size(); i++) {
						OffenceValue offence = offences.get(i);

						Integer refOffenceId = getRefOffenceId(isAppealCase, results, offence);

						if (model.getOffenceID().equals(refOffenceId)) {
							DefendantOnOffenceComplexValue defendantOnOffence = offence.getDefendantOnOffence(this.getDefendantID());
							boolean useThisOffence = defendantOnOffence != null && model.getDvlaOffence().equals(TT99)
										&& offenceHasDISTOT(defendantOnOffence.getDefendantOnOffenceId(), this.defendantID, offence.getCaseID());

							if (!useThisOffence) {
								useThisOffence = defendantOnOffence != null && model.getSeqNo()
													.equals(defendantOnOffence.getSeqNo());
							}

							if (useThisOffence) {
								OffencePair offencePair = new OffencePair(offence, model);

								// Check that we haven't already added this to the list...
								if (!returnList.contains(offencePair)) {
									returnList.add(offencePair);
								}
							}
						}

					}
				}
			}
		} catch (Exception e) {
			log.error("Exception when getting selected offences: " + e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}

		log.debug("Exit:: getSelectedOffences");
		return returnList;

	}
	
	
	private boolean offenceHasDISTOT(Integer defOnOffenceId, Integer defendantId, Integer caseId) {
		boolean hasADISTOT = XhibitDelegateHelper.getResults2Delegate().checkForDISTOT(defOnOffenceId, defendantId, caseId);
		return hasADISTOT;
	}

	/**
	 * sets the list of D20 offences that can be used to populate the D20 order
	 * 
	 * @param offences
	 */
	public void setOffences(ArrayList<OffenceModel> offences) {
		this.offences = offences;
		
	}

	/**
	 * Checks if a guilty (not lesser offence) is present on plea / verdict
	 * 
	 * @param plea
	 * @param verdict
	 * @return
	 */
	private boolean defendantIsGuilty(PleaValue plea, VerdictValue verdict) {
		boolean isGuilty = false;
		if (plea != null && plea.getRefPleaCode().matches("G")) {
			isGuilty = true;
		} else {
			if (verdict != null && (verdict.isGuiltyVerdict() && !verdict.isGuiltyOfLesser())) {
				isGuilty = true;
			}
		}
		return isGuilty;
	}

	/**
	 * Checks if a lesser offence is present
	 * 
	 * @param plea
	 * @param verdict
	 * @return
	 */
	private boolean defendantIsGuiltyOfLesser(PleaValue plea, VerdictValue verdict) {
		boolean isGuilty = false;
		if (plea != null && plea.isGuiltyOfLesser()) {
			isGuilty = true;
		} else {
			if (verdict != null && verdict.isGuiltyOfLesser()) {
				isGuilty = true;
			}
		}
		return isGuilty;
	}

	/**
	 * Retrieves lesser offence ID from plea / verdict
	 * 
	 * @param plea
	 * @param verdict
	 * @return
	 */
	private Integer getLesserOffenceID(PleaValue plea, VerdictValue verdict) {
		Integer offCode = 0;
		if (plea != null && plea.isGuiltyOfLesser()) {
			offCode = plea.getAltRefOffenceId();
		} else {
			if (verdict != null && verdict.isGuiltyOfLesser()) {
				offCode = verdict.getAltRefOffenceId();
			}
		}
		return offCode;
	}
	
	
	/**
	 * 
	 * @param caseType
	 * @param val
	 * @param off
	 * @return
	 */
	private Integer getRefOffenceId(boolean isAppealCase, ResultsCompositeValue rcv, OffenceValue offVal) {
		if (isAppealCase) {
			return getAppealRefOffenceId(rcv, offVal);
		} else {
			return getNonAppealRefOffenceId(rcv, offVal);
		}
	}

	/**
	 * Retrieves a lesser offence code (if present) for trial or sentence data
	 * --
	 * 
	 * @param val
	 * @param off
	 * @return the offence code if present, otherwise 0
	 */
	private Integer getNonAppealRefOffenceId(ResultsCompositeValue val, OffenceValue off) {
		Integer refOffenceId = off.getRefOffenceID();
		
		DefendantOnOffenceComplexValue defOnOff = off.getDefendantOnOffence(this.defendantID);
		if (defOnOff != null) {
			Integer defendantOnOffenceId = defOnOff.getDefendantOnOffenceId();
			if (defendantOnOffenceId != null) {
				VerdictValue verdict = val.getVerdict(defendantOnOffenceId);
				PleaValue plea = val.getPlea(defendantOnOffenceId);
		
				if (defendantIsGuiltyOfLesser(plea, verdict)) {
					refOffenceId = getLesserOffenceID(plea, verdict);
				}
			}
		}

		return refOffenceId;
	}

	/**
	 * Retrieves the lesser offence code if the appeal is of a lesser offence of ACALO
	 * 
	 * @param results
	 * @param off
	 * @return the lesser offence ID if a lesser offence is present, otherwise
	 *         the original offence ID
	 */
	private Integer getAppealRefOffenceId(ResultsCompositeValue results, OffenceValue off) {
		
		Integer refOffenceId = off.getRefOffenceID();

		VerdictValue verdict = results
				.getVerdict(off.getDefendantOnOffence(this.defendantID).getDefendantOnOffenceId());
		RefAppResultCriteria criteria = new RefAppResultCriteria();
		criteria.setPrimaryKey(verdict.getOriginalRefVerdictId());
		ArrayList<RefAppResultBasicValue> appealRes;
		
		try {
			appealRes = new ArrayList<RefAppResultBasicValue>(XhibitDelegateHelper.getBizRefDelegate().findAppResults(criteria));
		
			// Should really only be one but just in case ...
			for(RefAppResultBasicValue appeal: appealRes ) {
				if (appeal.getCode().equals("ACALO")) {
					if (verdict.getAltRefOffenceId() != null) {
						return verdict.getAltRefOffenceId();
					}
				}
			}
		} catch (BisRefControllerException e) {
			e.printStackTrace();
		}

		return refOffenceId;
	}

	/**
	 * @return boolean on whether the ok button is enabled
	 * 
	 *         For the ok button to be enabled it has to follow these rules
	 * 
	 *         -one checkbox must be a least selected -there can't be more that
	 *         four checkboxes selected -for the offence checkbox selected they
	 *         must have same conviction date
	 */
	public boolean isOkEnabled() {
		log.debug("Entry:: isOkEnabled");

		int clickCount = getClickCount();
		boolean outcome = clickCount > 0 && clickCount < 5;

		if (outcome) {
			// Need to do further checks.
			// All of the checked items need the same conviction date.
			String date = null;

			for (int index = 0; index < getOffences().size() && outcome; index++) {
				if (getOffences().get(index).isChecked()) {
					String candiDate = getOffences().get(index).getConvictionDateString();
					if (!"".equals(candiDate)) {					
						if (date == null) {
							date = candiDate;
						} else {
							outcome = candiDate.equals(date);
							if (!outcome) {
								break;
							}
						}
					}
				}
			}
		}

		log.debug("Exit:: isOkEnabled");
		return outcome;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public void incrementClickCount() {
		clickCount++;
	}

	public void decrementClickCount() {
		clickCount--;
	}

	public void addOffence(OffenceModel offenceModel) {
		offences.add(offenceModel);
	}

	public ArrayList<String> getConvictionDateString() {
		return convictionDates;
	}

	public void setConvictionDateString(ArrayList<String> convictionDates) {
		this.convictionDates = convictionDates;
	}

	public void setOkEnabled(boolean okEnabled) {
		this.okEnabled = okEnabled;
	}

	/**
	 * Sets the INT_D20 and the FIN_D20 to N as in setting a new D20 Order
	 */
	public void newD20() {
		for (OffenceModel offenceModel : offences) {
			offenceModel.setFinD20(false);
			offenceModel.setIntD20(false);
		}
	}

	private static Comparator<OffenceModel> sortByOffenceModel = new Comparator<OffenceModel>() {
		@Override
		public int compare(OffenceModel o1, OffenceModel o2) {
			Integer o1IsTT99 = Integer.valueOf(TT99.equals(o1.getDvlaOffence()) ? 1: 0);
			Integer o2IsTT99 = Integer.valueOf(TT99.equals(o2.getDvlaOffence()) ? 1: 0);
			// Sort by seqNo ASC
			Integer diff = o1.getSeqNo().compareTo(o2.getSeqNo());		
			// ...then TT99 (TT99 appearing last)
			if (Integer.valueOf(0).equals(diff)) {
				diff = o1IsTT99.compareTo(o2IsTT99);
			}
			return diff; 
		}
	};
}