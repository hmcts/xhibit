package uk.gov.courtservice.xhibit.business.vos.entities;

//JDK
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CCInfoBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the CCInfo
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

public class CCInfoBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -7623906771976952629L;
	private String ccInfoText;

    public CCInfoBasicValue() {
        super();
    }

    public CCInfoBasicValue(Integer version) {
        super(version);
    }

    public CCInfoBasicValue(Integer ccInfoID, Integer version) {
        super(ccInfoID, version);
    }

    public void setCcInfoText(String ccInfoText) {
        this.ccInfoText = ccInfoText;
    }

    public String getCcInfoText() {
        return ccInfoText;
    }
}