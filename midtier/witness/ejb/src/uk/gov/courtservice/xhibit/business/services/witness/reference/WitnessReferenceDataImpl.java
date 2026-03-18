package uk.gov.courtservice.xhibit.business.services.witness.reference;

import uk.gov.courtservice.xhibit.business.services.witness.WitnessReferenceDataControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces.WitnessReferenceData;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 */
public class WitnessReferenceDataImpl implements WitnessReferenceData {
    private WitnessReferenceDataControllerBeanBusinessDelegate instance = WitnessReferenceDataControllerBeanBusinessDelegate.DelegateFactory
            .getInstance();

    /**
     * return the valid witness types. (Defendant and Prosecution)
     */
    public String[] getWitnessTypes() {

        return instance.getWitnessTypes();
    }

    /**
     * return the valid witness statuses. (Profesional, Jouvernile etc)
     */
    public String[] getWitnessStatuses() {

        return instance.getWitnessStatuses();
    }

    // return the valid pager networks
    public String[] getPagerNetworks() {

        return instance.getPagerNetworks();
    }

    public String[] getTrialSessionTypes() {
        return instance.getTrialSessionTypes();
    }
}
