package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;

/**
 * <p>
 * Title: ScheduledValue
 * </p>
 * <p>
 * Description: ScheduledValue - the lowest level on the schedule. Contains data
 * about the case, time etc. Has a class variable of type ScheduledHearingValue
 * that contains this data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version $Id: ScheduledValue.java,v 1.8 2006/06/05 12:30:00 bzjrnl Exp $
 */
public class ScheduledValue extends CSAbstractValue {
    private XhbRefCourtBasicValue refCourtBasicValue;

    private ScheduledHearingValue scheduledHearingValue;
    private static final long serialVersionUID =5292634222138657273L;
    

    public ScheduledValue() {
    }

    /**
     * Constructor that takes the scheduledHearingId as argument.
     * 
     * @param scheduledHearingId -
     *            Integer
     */
    public ScheduledValue(Integer scheduledHearingId) {
        setId(scheduledHearingId);
    }

    public XhbRefCourtBasicValue getRefCourtBasicValue() {
        return refCourtBasicValue;
    }

    public void setRefCourtBasicValue(XhbRefCourtBasicValue refCourtBasicValue) {
        this.refCourtBasicValue = refCourtBasicValue;
    }

    public Integer getScheduledHearingId() {
        return getId();
    }

    public ScheduledHearingValue getScheduledHearingValue() {
        return scheduledHearingValue;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        setId(scheduledHearingId);
    }

    public void setScheduledHearingValue(ScheduledHearingValue scheduledHearingValue) {
        this.scheduledHearingValue = scheduledHearingValue;
    }

    public Integer getSequenceNo() {
        return scheduledHearingValue.getSequenceNo();
    }

    public HearingDetailsValue getHearingDetails() {
        return new HearingDetailsValue(scheduledHearingValue.getHearingType(), scheduledHearingValue
                .getHearingTypeDesc(), scheduledHearingValue.getScheduledHearingBasicValue().getOriginalTime());
    }

    /**
     * Return the time element of the NotBeforeTime
     * 
     * @return time element of date in the following format HH:mm
     */
    public String getNotBeforeTime() {
        return XDateFormat.format(scheduledHearingValue.getScheduledHearingBasicValue().getNotBeforeTime(),
                XDateFormat.TIMEFORMAT);
    }

    public String getCaseTypeAndNumber() {
        String type = scheduledHearingValue.getCaseType();
        Integer no = scheduledHearingValue.getCaseNumber();
        return type + no;
    }

    public String getCaseTitle() {
        return scheduledHearingValue.getCaseTitle();
    }

    public String getProsRef() {
        return scheduledHearingValue.getCaseBasicValue().getProsAgencyRef();
    }

    public String getListNote() {
        return scheduledHearingValue.getScheduledHearingBasicValue().getListingNote();
    }

    public DefendantDetailsValue[] getDefendants() {

        Collection defendantsOnCase = scheduledHearingValue.getDefendantsOnCase();
        Iterator defBasicValues = defendantsOnCase.iterator();

        DefendantDetailsValue[] defendantDetailsValues = new DefendantDetailsValue[defendantsOnCase.size()];

        for (int i = 0; i < defendantDetailsValues.length; i++) {
            DefendantBasicValue defendantBasicValue = (DefendantBasicValue) defBasicValues.next();
            Integer defId = defendantBasicValue.getId();
            DefendantOnCaseBasicValue defendantOnCaseBasicValue = scheduledHearingValue
                    .getDefendantOnCaseBasicValue(defId);

            defendantDetailsValues[i] = new DefendantDetailsValue(defendantBasicValue, defendantOnCaseBasicValue);
        }

        return defendantDetailsValues;
    }
}
