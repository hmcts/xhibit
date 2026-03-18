package uk.gov.courtservice.xhibit.business.vos.entities;

//JDK
import java.util.Collection;

/**
 * <p>
 * Title: SittingComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the Sitting
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

public class SittingComplexValue extends SittingBasicValue {
	private static final long serialVersionUID = -345855723027347844L;
    private Collection scheduledHearings;

    private HearingListBasicValue hearingList;

    public SittingComplexValue() {
        super();
    }

    public SittingComplexValue(Integer version) {
        super(version);
    }

    public SittingComplexValue(Integer sittingID, Integer version) {
        super(sittingID, version);
    }

    /*
     * public SittingComplexValue(Integer sittingID, Integer sittingSequenceNo,
     * Boolean isSittingJudge, Date sittingTime, String sittingNote, Integer
     * refJustice1ID, Integer refJustice2ID, Integer refJustice4ID, Integer
     * refJustice3ID, Boolean isFloating, HearingListBasicValue hearingList,
     * Integer refJudgeID, Integer courtRoomID, Integer courtSiteID, Date
     * lastUpdateDate, Date creationDate, String createdBy, String
     * lastUpdatedBy, Integer version, String justiceName1, String justiceName2,
     * String justiceName3, String justiceName4, Collection scheduledHearings) {
     * super(sittingID, sittingSequenceNo, isSittingJudge, sittingTime,
     * sittingNote, refJustice1ID, refJustice2ID, refJustice4ID, refJustice3ID,
     * isFloating, hearingList.getListID(), refJudgeID, courtRoomID,
     * courtSiteID, lastUpdateDate, creationDate, createdBy, lastUpdatedBy,
     * version, justiceName1, justiceName2, justiceName3, justiceName4);
     * 
     * this.scheduledHearings = scheduledHearings; this.hearingList =
     * hearingList; }
     */

    public Collection getScheduledHearings() {
        return scheduledHearings;
    }

    public void setScheduledHearings(Collection scheduledHearings) {
        this.scheduledHearings = scheduledHearings;
    }

    public void setHearingList(HearingListBasicValue hearingList) {
        this.hearingList = hearingList;
    }

    public HearingListBasicValue getHearingList() {
        return hearingList;
    }
}