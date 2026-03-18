package uk.gov.courtservice.xhibit.business.vos.services.charge;

/**
 * <p>
 * Title: ChargeType
 * </p>
 * <p>
 * Description: A charge type linking internal charge type to charge type
 * description
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class ChargeType {
    String typeDescription;

    String type;

    /**
     * Construct a ChargeType
     * 
     * @param typeDescription
     *            description of the charge type
     */
    public ChargeType(String typeDescription, String typeVal) {
        this.typeDescription = typeDescription;
        this.type = typeVal;
    }

    /**
     * Get the description
     * 
     * @return description of the event
     */
    public final String toString() {
        return typeDescription;
    }

    /**
     * get the charge type value
     * 
     * @return type value.
     */
    public final String getChargeType() {
        return type;
    }

    /**
     * get the charge type description
     * 
     * @return type value.
     */
    public final String getTypeDescription() {
        return typeDescription;
    }
}