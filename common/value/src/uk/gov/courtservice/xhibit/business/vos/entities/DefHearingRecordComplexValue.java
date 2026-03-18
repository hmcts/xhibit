package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: DefHearingRecordComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * DefHearingRecord enitity CMR fields.
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

public class DefHearingRecordComplexValue extends DefHearingRecordBasicValue {

    private HearingBasicValue hearing;

    public DefHearingRecordComplexValue() {
        super();
    }

    public DefHearingRecordComplexValue(Integer version) {
        super(version);
    }

    public DefHearingRecordComplexValue(Integer defHearingRecordID, Integer version) {
        super(defHearingRecordID, version);
    }

    public HearingBasicValue getHearing() {
        return hearing;
    }

    public void setHearing(HearingBasicValue hearing) {
        this.hearing = hearing;
    }
}