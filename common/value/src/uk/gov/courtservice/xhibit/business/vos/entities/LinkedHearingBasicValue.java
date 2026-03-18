package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CCInfoBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * LinkedHearing enitity CMP fields.
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

public class LinkedHearingBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -8784823192560244743L;
	
	public LinkedHearingBasicValue() {
        super();
    }

    public LinkedHearingBasicValue(Integer version) {
        super(version);
    }

    public LinkedHearingBasicValue(Integer linkedHearingID, Integer version) {
        super(linkedHearingID, version);
    }
}