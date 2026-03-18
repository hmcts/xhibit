package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Wizard Dialog to add Breach charges.
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
 * @author Bal Bhamra
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 07-08-2003 AW Daley Generation of CRNs added to stepDeInitialise
 */
public class AddBreachWizardDialog extends XWizardDialog implements BreachController {
	private final static int DEFENDANT_PANEL = 0;

	private final static int BREACH_PANEL = 1;

	private final static int OFFENCE_PANEL = 2;

	private XhibitApplicationController xac;

	private BreachValue breachValue;

	BreachWizardModel model = new BreachWizardModel();

	private ChargesControllerModel ccm;

	private BreachPanel breachPanel;

	private SelectDefendantPanel selectDefendantPanel;

	private AddedOffencesToBreachPanel addedOffencesPanel;

	public AddBreachWizardDialog(ChargesControllerModel ccm) throws CSRecoverableException {
		super(ccm.getACM().getXhibitApplicationController(), "", true);
		this.ccm = ccm;
		this.xac = ccm.getACM().getXhibitApplicationController();
		// model.setOffenceRequired(true); //Required if forcing the addition on
		// offences to breach

		breachValue = new BreachValue();
		breachValue.setDatePut(Calendar.getInstance());
		breachValue.setOriginalSentenceDate(Calendar.getInstance());
		breachValue.setCaseID(ccm.getACM().getCaseId());
 
		// Check if user is in Court Room, rather than assuming that they are.
		// Charges can be added by Court Clerk, not in a court room
		breachValue.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
		model.setBreachValue(breachValue);
		model.setAddedOffences(new ArrayList<OffenceValue>());
		model.setCaseID(ccm.getACM().getCaseId());

		model.setCaseType(ccm.getACM().getCaseType());
		model.setChargeType(ChargeTypes.BREACH);
		model.setCourtId(XhibitSingleton.getInstance().getCourtId());

		// create panels to add to wizard
		selectDefendantPanel = new SelectDefendantPanel(ccm, (BreachController) this, model);
		breachPanel = new BreachPanel((BreachController) this, BreachPanel.ADD_MODE, breachValue);
		addedOffencesPanel = new AddedOffencesToBreachPanel((BreachController) this, model);

		// Panel Array
		ArrayList al = new ArrayList();
		al.add(selectDefendantPanel);
		al.add(breachPanel);
		al.add(addedOffencesPanel);
		addBodyPanels(al);

		setWizardPanelImage("xwizardimage.jpg");
		pack();
	}

	/**
	 * BreachController implementation called when the state of the screen data
	 * changes to enable/disable the wizard button as appropriate.
	 */
	public void stepUpdateViewState() {
		// Need to check what the current screen is
		switch (currentPanel) {
		case DEFENDANT_PANEL:
			setTitle(getString("addBreachWizardDialogTitle") + " - " + getString("selectDefendantTitle"));

			getButtonPanel().getBack().setEnabled(false);
			getButtonPanel().getFinish().setEnabled(false);

			// If defendant selected Enable Next
			if (selectDefendantPanel != null) {
				if (selectDefendantPanel.getDefendantCb().getSelectedItem() instanceof DefendantValue) {
					getButtonPanel().getNext().setEnabled(true);
				} else {
					getButtonPanel().getNext().setEnabled(false);
				}
			} else {
				getButtonPanel().getNext().setEnabled(false);
			}
			// Finish Disabled as not all required data has yet been
			// entered.

			break;
		case BREACH_PANEL:
			setTitle(getString("addBreachWizardDialogTitle") + " - " + getString("enterBreachDetailsTitle"));

			boolean enableNext = true;

			// If Text is entered in Original Sentance
			if (breachPanel != null) {
				enableNext = breachPanel.breachEnableOK();
			} else {
				enableNext = false;
			}

			getButtonPanel().getNext().setEnabled(enableNext);
			getButtonPanel().getFinish().setEnabled(false);

			break;
		case OFFENCE_PANEL:
			setTitle(getString("addBreachWizardDialogTitle") + " - " + getString("addOffencesTitle"));

			getButtonPanel().getBack().setEnabled(true);
			getButtonPanel().getNext().setEnabled(false);
			if (addedOffencesPanel != null) {
				getButtonPanel().getFinish().setEnabled(addedOffencesPanel.getOffenceCount() > 0);
			} else {
				getButtonPanel().getFinish().setEnabled(false);
			}
			break;
		}
	}

	public void stepDeinitialise() throws CSRecoverableException {
		ChargeValue chargeValue = new ChargeValue();
		chargeValue.setCaseID(ccm.getACM().getCaseId());
		chargeValue.setCourtID(new Integer(ccm.getCourtId()));
		chargeValue.setCourtLogDate(Calendar.getInstance());
		chargeValue.setChargeType(ChargeTypes.BREACH);
		// chargeValue.setInCourt(inCourt);

		// Set from BreachWizardModel
		chargeValue.setDefendantID(model.getDefendantID());
		chargeValue.setBreachValue(model.getBreachValue());

		// offenceValue collection
		if (model.getAddedOffences() != null && model.getAddedOffences().size() > 0) {
			// Need to set offences as a vector not arraylist
			java.util.List offenceList = model.getAddedOffences();
			Integer seqNo = null;
			for (int i = 0; i < offenceList.size(); i++) {
				seqNo = new Integer(i + 1);
				((OffenceValue) offenceList.get(i)).setCrestOffenceSeqNo(seqNo);
			}
			chargeValue.setOffenceValues(new Vector(model.getAddedOffences()));
		}

		// BG call to add charge to case
		ccm.getDelegate().addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
				XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
	}

	public void prev() throws CSRecoverableException {
		if (currentPanel == BREACH_PANEL) {
			// Set boolean so moveModelToScreen() not called on stepActivate
			// on breachPanel
			breachPanel.setBackPressed(true);
		}
		super.prev(); // carry on with super method
	}

	public XhibitApplicationController getXac() {
		return xac;
	}

	/**
	 * Get a resource string from the Breach resources
	 * 
	 * @param key
	 *            the key to lookup
	 * @return the resource fro the given key.
	 */
	private String getString(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.Breaches, key);
	}
}