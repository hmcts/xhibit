package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;

/**
 * <p>
 * Title: HearingRecordUpdateValue
 * </p>
 * <p>
 * Description:This value will hold all value objects that can be updated.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class HearingRecordUpdateValue extends CSAbstractValue {
	private DefHearingRecordValue defHearingRecordValue;

	private HearingBasicValue hearingBasicValue;

	private Collection hrSHLegRepValues;

	private HRSHJudgeValue hrSHJudgeValue;

	private DirectionsForCaseValue directionsForCaseValue;
    
    private static final long serialVersionUID = 1304154583918676262L;

	private String caseType;

	public HearingRecordUpdateValue() {
		// default constructor.. this is required for printing using castor;
	}

	public void setDirectionsForCaseValue(DirectionsForCaseValue directionsForCaseValue) {
		this.directionsForCaseValue = directionsForCaseValue;
	}

	public DirectionsForCaseValue getDirectionsForCaseValue() {
		return this.directionsForCaseValue;
	}

	public DefHearingRecordValue getDefHearingRecordValue() {
		return defHearingRecordValue;
	}

	public HRSHJudgeValue getHrSHJudgeValue() {
		return hrSHJudgeValue;
	}

	public Collection getHrSHLegRepValues() {
		return hrSHLegRepValues;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(final String caseType) {
		this.caseType = caseType;
	}

	public HearingBasicValue getHearingBasicValue() {
		return hearingBasicValue;
	}

	public void setDefHearingRecordValue(DefHearingRecordValue defHearingRecordValue) {
		this.defHearingRecordValue = defHearingRecordValue;
	}

	public void setHrSHJudgeValue(HRSHJudgeValue hrSHJudgeValue) {
		this.hrSHJudgeValue = hrSHJudgeValue;
	}

	public void setHrSHLegRepValues(Collection hrSHLegRepValues) {
		this.hrSHLegRepValues = hrSHLegRepValues;
	}

	public void setHearingBasicValue(HearingBasicValue hearingBasicValue) {
		this.hearingBasicValue = hearingBasicValue;
	}
}