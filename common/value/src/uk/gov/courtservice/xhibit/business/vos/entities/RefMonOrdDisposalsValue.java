package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefMonOrdDisposalsValue
 * </p>
 * <p>
 * Description: A Value Object where the attributes map one to one with the
 * RefMonOrdDisposalsValue entity CMP fields.
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

public class RefMonOrdDisposalsValue extends CSAbstractValue {
	
	static final long serialVersionUID = 6726211718625406409L;

    private String disposalCode;

    private String moType;
        
    
    public String getMOType() {
		return moType;
	}

	public void setMOType(String moType) {
		this.moType = moType;
	}

	public void setDisposalCode(String disposalCode) {
		this.disposalCode = disposalCode;
	}

	public RefMonOrdDisposalsValue() {
        super();
    }

    public RefMonOrdDisposalsValue(Integer version) {
        super(version);
    }

    public RefMonOrdDisposalsValue(Integer id, Integer version) {
        super(id, version);
    }

    public RefMonOrdDisposalsValue(Integer moDisId, String disposalCode, Integer version) {
        this(moDisId, version);
        this.disposalCode = disposalCode;        
    }


    public String getDisposalCode() {
        return disposalCode;
    }

    
}
