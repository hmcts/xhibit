package uk.gov.courtservice.xhibit.business.vos.entities;

//FRAMEWORK
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SHStaffBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the SHStaff
 * enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class SHStaffBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 4484791876897711272L;
	private String staffRole;

    private String staffName;

    public SHStaffBasicValue() {
        super();
    }

    public SHStaffBasicValue(Integer version) {
        super(version);
    }

    public SHStaffBasicValue(Integer shStaffID, Integer version) {
        super(shStaffID, version);
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffName() {
        return staffName;
    }
}