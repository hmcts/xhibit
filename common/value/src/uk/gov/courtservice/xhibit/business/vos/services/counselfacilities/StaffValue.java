package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: StaffValue
 * </p>
 * <p>
 * Description: Super class for staffs
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

public class StaffValue extends CSAbstractValue {
	
	static final long serialVersionUID = 7532963129432109411L;
	
    private String name;

    private String role;

    public StaffValue() {
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }
}