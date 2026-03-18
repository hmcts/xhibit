package uk.gov.courtservice.xhibit.business.vos.entities;

//JDK
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: LinkedSHBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the LinkedSH
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

public class LinkedSHBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1556353751432682464L;
	public LinkedSHBasicValue() {
        super();
    }

    public LinkedSHBasicValue(Integer version) {
        super(version);
    }

    public LinkedSHBasicValue(Integer linkedSHID, Integer version) {
        super(linkedSHID, version);
    }
}