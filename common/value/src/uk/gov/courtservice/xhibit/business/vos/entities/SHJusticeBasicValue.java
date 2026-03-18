package uk.gov.courtservice.xhibit.business.vos.entities;

//FRAMEWORK
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SHJusticeBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * SHJustice enitity CMP fields.
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

public class SHJusticeBasicValue extends CSAbstractValue {
	
	static final long serialVersionUID = 1939710959340128912L;
	
    private String justiceName;

    private Integer hearingID;

    public SHJusticeBasicValue() {
        super();
    }

    public SHJusticeBasicValue(Integer version) {
        super(version);
    }

    public SHJusticeBasicValue(Integer shJusticeID, Integer version) {
        super(shJusticeID, version);
    }

    public void setJusticeName(String justiceName) {
        this.justiceName = justiceName;
    }

    public String getJusticeName() {
        return justiceName;
    }

    public void setHearingID(Integer hearingID) {
        this.hearingID = hearingID;
    }

    public Integer getHearingID() {
        return hearingID;
    }
}