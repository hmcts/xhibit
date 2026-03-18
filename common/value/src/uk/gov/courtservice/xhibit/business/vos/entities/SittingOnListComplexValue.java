package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: SittingOnListComplexValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class SittingOnListComplexValue extends SittingOnListBasicValue {

	private static final long serialVersionUID = 1L;

	private RefJudgeComplexValue refJudge;

	private RefSystemCodeBasicValue timeMarkingBasicValue;
	
	public SittingOnListComplexValue() {
        super();
    }

    public SittingOnListComplexValue(Integer id, Integer version) {
        super(id, version);
    }

	public RefJudgeComplexValue getRefJudge() {
		return refJudge;
	}

	public void setRefJudge(RefJudgeComplexValue refJudge) {
		this.refJudge = refJudge;
	}

	public RefSystemCodeBasicValue getTimeMarking() {
		return timeMarkingBasicValue;
	}

	public void setTimeMarking(RefSystemCodeBasicValue timeMarkingBasicValue) {
		this.timeMarkingBasicValue = timeMarkingBasicValue;
	}
}