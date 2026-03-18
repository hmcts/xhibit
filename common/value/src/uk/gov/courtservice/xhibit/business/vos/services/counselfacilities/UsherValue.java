package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

/**
 * <p>
 * Title: UsherValue
 * </p>
 * <p>
 * Description: Value to hold an usher
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

public class UsherValue extends StaffValue {
	
	static final long serialVersionUID = -8087510192165311585L;

    public UsherValue() {
    }

    public UsherValue(String name, String role) {
        this.setName(name);
        this.setRole(role);
    }
}