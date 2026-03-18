package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.ArrayList;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SittingValue
 * </p>
 * <p>
 * Description: The value of the sitting. A sitting can have several scheduled
 * hearings. The reason why this is a seperate value is that it contains values
 * that are specific for the sitting.
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

public class SittingValue extends CSAbstractValue {
    private Integer sittingSeqNo;

    private Date sittingAt;

    private Boolean floating;

    private String sittingNote;

    private Integer courtRoomId;

    private String courtRoomName;

    private Integer crestCourtRoomNo;

    private String courtRoomDisplayName;

    private JudiciaryValue judiciaryValue;
    
    private static final long serialVersionUID =7551279260382491160L;

    /**
     * list of all the cases listed.
     */
    private ArrayList scheduledValues;

    public SittingValue() {
    }

    public Boolean getFloating() {
        return floating;
    }

    public ArrayList getScheduledValues() {
        if (scheduledValues == null) {
            scheduledValues = new ArrayList();
        }
        return scheduledValues;
    }

    public Date getSittingAt() {
        return sittingAt;
    }

    public Integer getSittingId() {
        return getId();
    }

    public String getSittingNote() {
        return sittingNote;
    }

    public Integer getSittingSeqNo() {
        return sittingSeqNo;
    }

    public void setSittingSeqNo(Integer sittingSeqNo) {
        this.sittingSeqNo = sittingSeqNo;
    }

    public void setSittingNote(String sittingNote) {
        this.sittingNote = sittingNote;
    }

    public void setSittingId(Integer sittingId) {
        setId(sittingId);
    }

    public void setSittingAt(Date sittingAt) {
        this.sittingAt = sittingAt;
    }

    public void setScheduledValues(ArrayList scheduledValues) {
        this.scheduledValues = scheduledValues;
    }

    public void setFloating(Boolean floating) {
        this.floating = floating;
    }

    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public String getCourtRoomName() {
        return courtRoomName;
    }

    public Integer getCrestCourtRoomNo() {
        return crestCourtRoomNo;
    }

    public String getCourtRoomDisplayName() {
        return courtRoomDisplayName;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public void setCourtRoomName(String courtRoomName) {
        this.courtRoomName = courtRoomName;
    }

    public void setCrestCourtRoomNo(Integer crestCourtRoomNo) {
        this.crestCourtRoomNo = crestCourtRoomNo;
    }

    public void setCourtRoomDisplayName(String courtRoomDisplayName) {
        this.courtRoomDisplayName = courtRoomDisplayName;
    }

    public JudiciaryValue getJudiciaryValue() {
        return judiciaryValue;
    }

    public void setJudiciaryValue(JudiciaryValue judiciaryValue) {
        this.judiciaryValue = judiciaryValue;
    }

}