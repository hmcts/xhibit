package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: SHJudgeComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the SHJudge
 * enitity CMR fields.
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

public class SHJudgeComplexValue extends SHJudgeBasicValue {
	private static final long serialVersionUID = 4148080754873611946L;
	
    public SHJudgeComplexValue() {
    }

    public SHJudgeComplexValue(Integer version) {
        super(version);
    }

    public SHJudgeComplexValue(Integer shJudgeID, Integer version) {
        super(shJudgeID, version);
    }

}