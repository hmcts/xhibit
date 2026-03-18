package uk.gov.courtservice.xhibit.client.schedule.movecase;

import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;

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
 * @author Bal Bhamra
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 72,52569 24-04-2003 AW Daley currentCaseType attribute added
 * 
 */

public class MoveCaseModel {
    private Integer currentCase;

    private String currentCaseType;

    private String hearingType;

    private String timeListed;

    private String shortHandWriter;

    private String judge;

    private Calendar time;

    private String courtSiteId; // This may be replaced

    private Integer courtRoomId; // This may be replaced

    private ScheduledHearingValue shv;

    private boolean adjourned = false;

    private boolean moveJudge = false;

    private boolean moveShorthandWriter = false;

    private boolean moveCourtClerk = false;

    // private DefaultListModel judgeListModel;
    private DefaultListModel prosAdvocateListModel;

    private DefaultListModel defendantListModel;

    private DefaultListModel defAdvocateListModel;

    private DefaultComboBoxModel courtComboModel;

    private ResourceBundle myResource;

    /**
     * @todo need attribute to hold selected court (is this a value object or
     *       integer???
     */

    public MoveCaseModel() {
    }

    public ScheduledHearingValue getScheduledHearingValue() {
        return shv;
    }

    public void setScheduledHearingValue(ScheduledHearingValue shv) {
        this.shv = shv;
    }

    public Integer getCurrentCase() {
        return currentCase;
    }

    public void setCurrentCase(Integer currentCase) {
        this.currentCase = currentCase;
    }

    public String getHearingType() {
        return hearingType;
    }

    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }

    public String getTimeListed() {
        return timeListed;
    }

    public void setTimeListed(String timeListed) {
        this.timeListed = timeListed;
    }

    public String getShortHandWriter() {
        return shortHandWriter;
    }

    public void setShortHandWriter(String shortHandWriter) {
        this.shortHandWriter = shortHandWriter;
    }

    // This may be replaced
    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public String getCourtSiteId() {
        return courtSiteId;
    }

    public void setCourtSiteId(String courtSiteId) {
        this.courtSiteId = courtSiteId;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public boolean isAdjourned() {
        return adjourned;
    }

    public void setAdjourned(boolean adjourned) {
        this.adjourned = adjourned;
    }

    public boolean isMoveJudge() {
        return moveJudge;
    }

    public void setMoveJudge(boolean moveJudge) {
        this.moveJudge = moveJudge;
    }

    public boolean isMoveShorthandWriter() {
        return moveShorthandWriter;
    }

    public void setMoveShorthandWriter(boolean moveShorthandWriter) {
        this.moveShorthandWriter = moveShorthandWriter;
    }

    public boolean isMoveCourtClerk() {
        return moveCourtClerk;
    }

    public void setMoveCourtClerk(boolean moveCourtClerk) {
        this.moveCourtClerk = moveCourtClerk;
    }

    // public DefaultListModel getJudgeListModel()
    // {
    // return judgeListModel;
    // }
    // public void setJudgeListModel(DefaultListModel judgeListModel)
    // {
    // this.judgeListModel = judgeListModel;
    // }

    public DefaultListModel getProsAdvocateListModel() {
        return prosAdvocateListModel;
    }

    public void setProsAdvocateListModel(DefaultListModel prosAdvocateListModel) {
        this.prosAdvocateListModel = prosAdvocateListModel;
    }

    public DefaultListModel getDefendantListModel() {
        return defendantListModel;
    }

    public void setDefendantListModel(DefaultListModel defendantListModel) {
        this.defendantListModel = defendantListModel;
    }

    public DefaultListModel getDefAdvocateListModel() {
        return defAdvocateListModel;
    }

    public void setDefAdvocateListModel(DefaultListModel defAdvocateListModel) {
        this.defAdvocateListModel = defAdvocateListModel;
    }

    public DefaultComboBoxModel getCourtComboModel() {
        return courtComboModel;
    }

    public void setCourtComboModel(DefaultComboBoxModel courtComboModel) {
        this.courtComboModel = courtComboModel;
    }

    public ResourceBundle getMyResource() {
        return myResource;
    }

    public void setMyResource(ResourceBundle resource) {
        this.myResource = resource;
    }

    public String getCurrentCaseType() {
        return currentCaseType;
    }

    public void setCurrentCaseType(String currentCaseType) {
        this.currentCaseType = currentCaseType;
    }
}