package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: The charge, offence (if applic) and reason for failure
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DefendantAuthorisationFailureValue.java,v 1.5 2005/01/28
 *          08:28:29 rzvddy Exp $
 */

public class DefendantAuthorisationFailureValue extends CSAbstractValue {
	
	static final long serialVersionUID = -931866649621233300L;

    private ChargeValue charge;

    private OffenceValue offence;

    private String reasonCode;

    public DefendantAuthorisationFailureValue(ChargeValue charge, OffenceValue offence, String reasonCode) {
        this.charge = charge;
        this.offence = offence;
        this.reasonCode = reasonCode;
    }

    /**
     * @return The charge which failed authorisation or null if case level
     */
    public ChargeValue getCharge() {
        return charge;
    }

    /**
     * Set the charge that failed authorisaion Note this may be null if case
     * level authorisation
     * 
     * @param charge
     */
    public void setCharge(ChargeValue charge) {
        this.charge = charge;
    }

    /**
     * Set the offence that failed authorisation Note this may be null for case
     * level failures
     * 
     * @param offence
     */
    public void setOffence(OffenceValue offence) {
        this.offence = offence;
    }

    /**
     * @return The offence that failed authorisation or null if case level
     */
    public OffenceValue getOffence() {
        return offence;
    }

    /**
     * @param reasonCode
     *            language independant reason code
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return The internationalised reason for failure
     */
    public String getReasonCode() {
        return reasonCode;
    }

    // The following methods are to aid sorting

    /**
     * Returns the charge number. This is only useful for Indictments and
     * breaches
     * 
     * @return The Indictment/breach number or null if case level.
     */
    public Integer getChargeSequence() {
        return charge.getCrestChargeSeqNo();
    }

    /**
     * null implementation for Sorter.sort to work
     * 
     * @param na
     */
    public void setChargeSequence(Integer na) {
        // null implementation for Sorter.sort to work
    }

    /**
     * Return the offence number
     * 
     * @return the offence number or null if case level authorisation
     */
    public Integer getOffenceSequence() {
        return offence.getCrestOffenceSeqNo();
    }

    /**
     * null implementation for Sorter.sort to work
     * 
     * @param na
     */
    public void setOffenceSequence(Integer na) {
        // null implementation for Sorter.sort to work
    }

    public int getChargeTypeSort() {
        switch (charge.getChargeType().charAt(0)) {
        case 'I':
            return 1;
        case 'O':
            return 2;
        case 'S':
            return 3;
        case 'B':
            return 4;
        case 'G':
            return 5;
        default:
            return 6;
        }
    }

    /**
     * null implementation for Sorter.sort to work
     * 
     * @param na
     */
    public void setChargeTypeSort(int value) {
        // null implementation for Sorter.sort to work
    }
}
