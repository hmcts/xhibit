package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: AssignRepresentativesTableRowModel
 * </p>
 * <p>
 * Description: Table row model for Assigning Representatives
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class AssignRepresentativesTableRowModel {
    private String courtRoom;

    private Integer caseNumber;

    private String caseType;

    private String timeListed;

    private String role;

    private String party;

    private Boolean selected;

    private String representatives;

    private Integer shvId;

    private Integer defendantId;

    private Integer scheduledHearingDefendantId;

    private String isFloating;

    private Integer courtRoomId;

    private Integer crestCourtRoomNumber;

    private String courtSiteCode;

    private Integer sittingSequenceNo;

    private Integer shSequenceNo;

    private String courtSiteShortName;

    // get methods
    public String getCourtRoom() {
        return (CounselFacilitiesHelper.tidyUp(courtRoom));
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getTimeListed() {
        return (CounselFacilitiesHelper.tidyUp(timeListed));
    }

    public String getRole() {
        return (CounselFacilitiesHelper.tidyUp(role));
    }

    public String getParty() {
        return (CounselFacilitiesHelper.tidyUp(party));
    }

    public Boolean isSelected() {
        return selected;
    }

    public String getRepresentatives() {
        return (CounselFacilitiesHelper.tidyUp(representatives));
    }

    public Integer getShvId() {
        return shvId;
    }

    public Integer getDefendantId() {
        return defendantId;
    }

    public Integer getScheduledHearingDefendantId() {
        return scheduledHearingDefendantId;
    }

    public String getIsFloating() {
        return isFloating;
    }

    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public Integer getCrestCourtRoomNumber() {
        return crestCourtRoomNumber;
    }

    public String getCourtSiteCode() {
        return courtSiteCode;
    }

    public Integer getSittingSequenceNo() {
        return sittingSequenceNo;
    }

    public Integer getShSequenceNo() {
        return shSequenceNo;
    }

    public String getCourtSiteShortName() {
        return courtSiteShortName;
    }

    // set methods
    public void setCourtRoom(String param) {
        courtRoom = param;
    }

    public void setCaseNumber(Integer param) {
        caseNumber = param;
    }

    public void setCaseType(String param) {
        caseType = param;
    }

    public void setTimeListed(String param) {
        timeListed = param;
    }

    public void setRole(String param) {
        role = param;
    }

    public void setParty(String param) {
        party = param;
    }

    public void setSelected(Boolean param) {
        selected = param;
    }

    public void setRepresentatives(String param) {
        representatives = param;
    }

    public void setShvId(Integer param) {
        shvId = param;
    }

    public void setDefendantId(Integer param) {
        defendantId = param;
    }

    public void setScheduledHearingDefendantId(Integer param) {
        scheduledHearingDefendantId = param;
    }

    public void setIsFloating(String param) {
        isFloating = param;
    }

    public void setCourtRoomId(Integer param) {
        courtRoomId = param;
    }

    public void setCrestCourtRoomNumber(Integer param) {
        crestCourtRoomNumber = param;
    }

    public void setCourtSiteCode(String param) {
        courtSiteCode = param;
    }

    public void setSittingSequenceNo(Integer param) {
        sittingSequenceNo = param;
    }

    public void setShSequenceNo(Integer param) {
        shSequenceNo = param;
    }

    public void setCourtSiteShortName(String courtSiteShortName) {
        this.courtSiteShortName = courtSiteShortName;
    }

    // utility
    public void printModel() {
        XHIBITConstant.info("AssignRepresentativesTableRowModel");
        XHIBITConstant.info("----------------------------------");
        XHIBITConstant.info("CourtRoomID                : " + getCourtRoomId());
        XHIBITConstant.info("CourtRoom                  : " + getCourtRoom());
        XHIBITConstant.info("CaseType                   : " + getCaseType());
        XHIBITConstant.info("CaseNumber                 : " + getCaseNumber().toString());
        XHIBITConstant.info("TimeListed                 : " + getTimeListed());
        XHIBITConstant.info("Role                       : " + getRole());
        XHIBITConstant.info("Party                      : " + getParty());
        XHIBITConstant.info("IsSelected                 : "
                + (isSelected() == null ? false : isSelected().booleanValue()));
        XHIBITConstant.info("Representatives            : " + getRepresentatives());
        XHIBITConstant.info("SHVID                      : " + getShvId());
        XHIBITConstant.info("DefendantID                : " + getDefendantId());
        XHIBITConstant.info("ScheduledHearingDefendantID: " + getScheduledHearingDefendantId());
        XHIBITConstant.info("IsFloating                 : " + getIsFloating());
        XHIBITConstant.info("CrestCourtRoomNumber       : " + getCrestCourtRoomNumber().toString());
        XHIBITConstant.info("CourtSiteCode              : " + getCourtSiteCode());
        XHIBITConstant.info("SittingSequenceNo          : " + getSittingSequenceNo().toString());
        XHIBITConstant.info("shSequenceNo               : " + getShSequenceNo().toString());
        XHIBITConstant.info("CourtSiteShortName         : " + getCourtSiteShortName());
    }

    public String getLegalRole() {
        return (XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "dat" + getRole()));
    }
}
