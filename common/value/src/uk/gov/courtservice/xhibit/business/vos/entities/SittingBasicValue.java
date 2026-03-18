package uk.gov.courtservice.xhibit.business.vos.entities;

//JDK
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SittingBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the Sitting
 * enitity CMP fields.
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
 * 
 * modified history
 * MGroen - 02/03/18 - ctx-1170 added timeMarkingId 
 * MGroen - 09/03/18 - ctx-1170 added listNotePredefinedID , listNoteText, listNotePredefinedIDClass
 * 						listNoteTextClass
 * 
 * MGroen - 21/03/18 - take out above amendments
 */
public class SittingBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = -8646051362766538062L;
	
	private Integer sittingSequenceNo;

    private Date sittingTime;

    private String sittingNote;

    private Integer refJustice1ID;

    private Integer refJustice2ID;

    private Integer refJustice4ID;

    private Integer refJustice3ID;

    private Integer listID;

    private Integer refJudgeID;

    private Integer courtRoomID;

    private Integer courtSiteID;

    private String justiceName1;

    private String justiceName2;

    private String justiceName3;

    private String justiceName4;

    private String isFloating;

    private String isSittingJudge;
    
    public SittingBasicValue() {
        super();
    }

    public SittingBasicValue(Integer version) {
        super(version);
    }

    public SittingBasicValue(Integer sittingID, Integer version) {
        super(sittingID, version);
    }

    public void setSittingSequenceNo(Integer sittingSequenceNo) {
        this.sittingSequenceNo = sittingSequenceNo;
    }

    public Integer getSittingSequenceNo() {
        return sittingSequenceNo;
    }

    public void setSittingTime(Date sittingTime) {
        this.sittingTime = sittingTime;
    }

    public Date getSittingTime() {
        return sittingTime;
    }

    public void setSittingNote(String sittingNote) {
        this.sittingNote = sittingNote;
    }

    public String getSittingNote() {
        return sittingNote;
    }

    public void setRefJustice1ID(Integer refJustice1ID) {
        this.refJustice1ID = refJustice1ID;
    }

    public Integer getRefJustice1ID() {
        return refJustice1ID;
    }

    public void setRefJustice2ID(Integer refJustice2ID) {
        this.refJustice2ID = refJustice2ID;
    }

    public Integer getRefJustice2ID() {
        return refJustice2ID;
    }

    public void setRefJustice4ID(Integer refJustice4ID) {
        this.refJustice4ID = refJustice4ID;
    }

    public Integer getRefJustice4ID() {
        return refJustice4ID;
    }

    public void setRefJustice3ID(Integer refJustice3ID) {
        this.refJustice3ID = refJustice3ID;
    }

    public Integer getRefJustice3ID() {
        return refJustice3ID;
    }

    public void setListID(Integer listID) {
        this.listID = listID;
    }

    public Integer getListID() {
        return listID;
    }

    public void setRefJudgeID(Integer refJudgeID) {
        this.refJudgeID = refJudgeID;
    }

    public Integer getRefJudgeID() {
        return refJudgeID;
    }

    public void setCourtRoomID(Integer courtRoomID) {
        this.courtRoomID = courtRoomID;
    }

    public Integer getCourtRoomID() {
        return courtRoomID;
    }

    public void setCourtSiteID(Integer courtSiteID) {
        this.courtSiteID = courtSiteID;
    }

    public Integer getCourtSiteID() {
        return courtSiteID;
    }

    public void setJusticeName1(String justiceName1) {
        this.justiceName1 = justiceName1;
    }

    public String getJusticeName1() {
        return justiceName1;
    }

    public void setJusticeName2(String justiceName2) {
        this.justiceName2 = justiceName2;
    }

    public String getJusticeName2() {
        return justiceName2;
    }

    public void setJusticeName3(String justiceName3) {
        this.justiceName3 = justiceName3;
    }

    public String getJusticeName3() {
        return justiceName3;
    }

    public void setJusticeName4(String justiceName4) {
        this.justiceName4 = justiceName4;
    }

    public String getJusticeName4() {
        return justiceName4;
    }

    public void setIsFloating(String isFloating) {
        this.isFloating = isFloating;
    }

    public String getIsFloating() {
        return isFloating;
    }

    public void setIsSittingJudge(String isSittingJudge) {
        this.isSittingJudge = isSittingJudge;
    }

    public String getIsSittingJudge() {
        return isSittingJudge;
    }

}