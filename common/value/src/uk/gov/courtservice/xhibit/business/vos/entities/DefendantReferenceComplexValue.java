package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class DefendantReferenceComplexValue extends DefendantReferenceBasicValue {

	private static final long serialVersionUID = 7400605319877576945L;
	
	private DefendantBasicValue defendant;

    public DefendantReferenceComplexValue() {
    }

    public DefendantReferenceComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    public DefendantBasicValue getDefendant() {
        return defendant;
    }

    public void setDefendant(DefendantBasicValue defendant) {
        this.defendant = defendant;
    }
}