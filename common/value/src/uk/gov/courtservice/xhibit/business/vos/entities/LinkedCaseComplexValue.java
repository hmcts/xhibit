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

public class LinkedCaseComplexValue extends LinkedCaseBasicValue {
	
	private static final long serialVersionUID = 6512211367257631415L;

    private CaseBasicValue caseBasicValue;

    public LinkedCaseComplexValue() {
    }

    public LinkedCaseComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    public CaseBasicValue getCaseBasicValue() {
        return caseBasicValue;
    }

    public void setCaseBasicValue(CaseBasicValue caseBasicValue) {
        this.caseBasicValue = caseBasicValue;
    }
}