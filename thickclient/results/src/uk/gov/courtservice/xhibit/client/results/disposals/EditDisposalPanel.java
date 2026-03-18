package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;

/**
 * <p>
 * Title: EditDisposalPanel
 * </p>
 * <p>
 * Description: Creating new disposals and editing previously created disposals.
 * This is the entry point into the disposal client subsystem.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @version $Id: EditDisposalPanel.java,v 1.17 2014/08/04 23:49:13 atwells Exp $
 */
public class EditDisposalPanel extends DisposalPanel {

	// Constraints
	private static final GridBagConstraints createRendererConstraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(4, 4, 4, 4);
		return constraints;
	}

	// Disposal Listeners (cache as we swap in and out renders)
	private final List listeners = new ArrayList();

	// Components
	private DisposalRenderer renderer;

	/**
	 * Construct a new instance to create a disposal
	 */
	public EditDisposalPanel() throws CSRecoverableException {
		// Throw the default constructors exception
	}

	public void init(DisposalReferenceValue reference, DisposalValue value) throws CSRecoverableException {
		if (reference == null) {
			throw new IllegalArgumentException("reference: null");
		}
		if (value == null) {
			throw new IllegalArgumentException("value: null");
		}

		// Copy the reference so we do not corrupt the reference data cache
		renderer = DisposalRendererFactory.create(reference.copy());
		renderer.setDisposal(value);
		for (int i = 0, s = listeners.size(); i < s; i++) {
			renderer.addDisposalListener((DisposalListener) listeners.get(i));
		}

		removeAll();
		add(renderer.getDisposalComponent().getComponent(), createRendererConstraints());
		doLayout();
	}

	/**
	 * XPanel Lifecycle Method: called when the ok button (on containing dialog)
	 * is pressed
	 */
	public void stepValidate() throws CSValidationException {
		if (renderer != null && !renderer.isComplete()) {
			throw new CSValidationException("EditDisposalPanel.Incomplete", "Disposal is incomplete");
		}
	}

	/**
	 * Return true if the disposal is complete
	 */
	public boolean isComplete() {
		if (renderer == null) {
			throw new IllegalStateException("renderer: null");
		}
		return renderer.isComplete();
	}

	/**
	 * Get the disposal data
	 */
	public DisposalValue getValue() {
		if (renderer == null) {
			throw new IllegalStateException("renderer: null");
		}
		return renderer.getDisposal();
	}

	/**
	 * Set the disposal data
	 */
	public void setValue(DisposalValue value) {
		if (renderer == null) {
			throw new IllegalStateException("renderer: null");
		}
		renderer.setDisposal(value);
	}

	/**
	 * Get the reference disposal data
	 */
	public DisposalReferenceValue getReference() {
		if (renderer == null) {
			throw new IllegalStateException("renderer: null");
		}

		DisposalReferenceValue tempDisposalValue = renderer.getReference();
		DeportationModel deportationReasons = renderer.getDisposalComponent().getDeportationReason();

		tempDisposalValue.setCustodial(deportationReasons.getCustodial());
		tempDisposalValue.setSuspended(deportationReasons.getSuspended());
		tempDisposalValue.setSeriousDrugOffence(deportationReasons.getSeriousDrugOffence());
		tempDisposalValue.setRecommendedDeportation(deportationReasons.getRecommendedDeportation());
 		

		HateCrimeModel hateCrimeReasons = renderer.getDisposalComponent().getHateCrimeReasons();
		tempDisposalValue.setHateCrimeFlag(hateCrimeReasons.getHateCrimeFlag());
		tempDisposalValue.setGeneralDisability(hateCrimeReasons.getGeneralDisability());
		tempDisposalValue.setGeneralSexual(hateCrimeReasons.getGeneralSexual());
		tempDisposalValue.setGeneralTransgender(hateCrimeReasons.getGeneralTransgender());
		tempDisposalValue.setRaceAndReligionAggravated(hateCrimeReasons.getRaceAndReligionAggravated());
		tempDisposalValue.setRacialAggravated(hateCrimeReasons.getRacialAggravated());
		tempDisposalValue.setReligionAggravated(hateCrimeReasons.getReligionAggravated());
		tempDisposalValue.setVictimDisability(hateCrimeReasons.getVictimDisability());
		tempDisposalValue.setVictimSexual(hateCrimeReasons.getVictimSexual());
		tempDisposalValue.setVictimTransgender(hateCrimeReasons.getVictimTransgender());

		AggravatingReasonsModel aggravatingReasons = renderer.getDisposalComponent().getAggravatingReasons();
		tempDisposalValue.setAggravatingAssaultOnWorkers(aggravatingReasons.isAssaultOnWorkers());
		tempDisposalValue.setAggravatingTerroristConnection(aggravatingReasons.isTerroristConnection());
        tempDisposalValue.setAggravatingEmergencyWorkers(aggravatingReasons.isEmergencyWorkers());
        tempDisposalValue.setAggravatingHostility(aggravatingReasons.isHostility());
        tempDisposalValue.setAggravatingSexualOrientation(aggravatingReasons.isSexualOrientation());
        tempDisposalValue.setAggravatingSexualOrientationOfVictim(aggravatingReasons.isSexualOrientationOfVictim());
        tempDisposalValue.setAggravatingTransgender(aggravatingReasons.isTransgender());
        tempDisposalValue.setAggravatingTransgenderOfVictim(aggravatingReasons.isTransgenderOfVictim());
 
		return tempDisposalValue;
	}

	/**
	 * Set the reference disposal data
	 */
	public void setReference(DisposalReferenceValue reference) {
		// Copy the reference so we do not corrupt the reference data cache
		renderer = DisposalRendererFactory.create(reference.copy());

		for (int i = 0, s = listeners.size(); i < s; i++) {
			renderer.addDisposalListener((DisposalListener) listeners.get(i));
		}
		removeAll();
		add(renderer.getDisposalComponent().getComponent(), createRendererConstraints());
		doLayout();

	}

	/**
	 * Add the listener
	 */
	public void addDisposalListener(DisposalListener listener) {
		if (renderer != null) {
			renderer.addDisposalListener(listener);
		}
		listeners.add(listener);
	}

	/**
	 * Remove the listener
	 */
	public void removeDisposalListener(DisposalListener listener) {
		if (renderer != null) {
			renderer.removeDisposalListener(listener);
		}
		listeners.remove(listener);
	}

}