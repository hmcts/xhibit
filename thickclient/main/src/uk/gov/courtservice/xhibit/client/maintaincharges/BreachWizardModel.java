package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;

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
 * @author Bal Bhamra
 * @version 1.0
 * 
 * Change Log version 1.4 - Simon Gilmore - Changed to extend ChargeWizardModel.
 */

public class BreachWizardModel extends ChargeWizardModel {

    /**
     * @todo Needs to hold following info selected defendant (id? or
     *       DefendantValue) Collection of OffenceValue objects
     */

    private BreachValue breachValue;

    public BreachWizardModel() {
        super();
    }

    public BreachValue getBreachValue() {
        return breachValue;
    }

    public void setBreachValue(BreachValue breachValue) {
        this.breachValue = breachValue;
    }
}