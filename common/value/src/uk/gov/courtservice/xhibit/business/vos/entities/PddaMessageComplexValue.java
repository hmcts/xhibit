package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;

/**
 * PDDA Message Complex Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class PddaMessageComplexValue extends PddaMessageBasicValue {

	private static final long serialVersionUID = 1L;
	
	private RefPddaMessageTypeBasicValue refPddaMessageType;
	private XhbClobBasicValue clob;

    public PddaMessageComplexValue() {
    }

    public PddaMessageComplexValue(Integer id, Integer version) {
        super(id, version);
    }

	public XhbClobBasicValue getClob() {
		return clob;
	}

	public void setClob(XhbClobBasicValue clob) {
		this.clob = clob;
	}

	public RefPddaMessageTypeBasicValue getRefPddaMessageType() {
		return refPddaMessageType;
	}

	public void setRefPddaMessageType(RefPddaMessageTypeBasicValue refPddaMessageType) {
		this.refPddaMessageType = refPddaMessageType;
	}
}