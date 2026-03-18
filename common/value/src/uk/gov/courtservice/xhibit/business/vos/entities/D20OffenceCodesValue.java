package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: D20OffenceCodeValue
 * </p>
 * <p>
 * Description: A Value Object where the attributes map one to one with the
 * D20OffenceCode entity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 * 
 */

public class D20OffenceCodesValue extends CSAbstractValue {

	private static final long serialVersionUID = -3554004061857303154L;

	private String offenceCode;

    private String reasonType;
    
    private String reason;
    
    private String penaltyPoints;
    
    
    public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPenaltyPoints() {
		return penaltyPoints;
	}

	public void setPenaltyPoints(String penaltyPoints) {
		this.penaltyPoints = penaltyPoints;
	}

	public void setOffenceCode(String offenceCode) {
		this.offenceCode = offenceCode;
	}

	public D20OffenceCodesValue() {
        super();
    }

    public D20OffenceCodesValue(Integer version) {
        super(version);
    }

    public D20OffenceCodesValue(Integer id, Integer version) {
        super(id, version);
    }

    public D20OffenceCodesValue(Integer offenceCodeId, String offenceCode, Integer version) {
        this(offenceCodeId, version);
        this.offenceCode = offenceCode;        
    }


    public String getOffenceCode() {
        return offenceCode;
    }

    
}
