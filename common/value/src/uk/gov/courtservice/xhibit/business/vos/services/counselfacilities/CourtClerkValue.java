package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

/**
 * <p>
 * Title: CourtClerkValue
 * </p>
 * <p>
 * Description: Value to hold a court clerk
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class CourtClerkValue extends StaffValue {
	private static final long serialVersionUID = 3271899726284017529L;
    public CourtClerkValue() {
    }

    public CourtClerkValue(String name, String role) {
        this.setName(name);
        this.setRole(role);
    }

}