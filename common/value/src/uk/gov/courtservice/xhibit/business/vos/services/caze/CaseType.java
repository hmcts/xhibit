package uk.gov.courtservice.xhibit.business.vos.services.caze;

/**
 * <p>
 * Title: ChargeType
 * </p>
 * <p>
 * Description: A case type linking internal case type to case type description
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

public class CaseType {
    String typeDescription;

    String type;

    /**
     * Construct a ChargeType
     * 
     * @param typeDescription
     *            description of the Case type
     */
    public CaseType(String typeDescription, String typeVal) {
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
     * get the Case type value
     * 
     * @return type value.
     */
    public final String getCaseType() {
        return type;
    }

    /**
     * get the Case type description
     * 
     * @return type value.
     */
    public final String getTypeDescription() {
        return typeDescription;
    }
}