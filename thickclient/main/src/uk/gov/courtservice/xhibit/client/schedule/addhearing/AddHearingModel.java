package uk.gov.courtservice.xhibit.client.schedule.addhearing;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author David Crossland
 * @version 1.0
 */
public class AddHearingModel {
    // log from this class rather than XHIBITConstants
    private final Logger log = Logger.getLogger(AddHearingModel.class);

    private Integer caseNumber;

    private String caseType;

    private Integer courtId;

    private Integer courtSiteId;

    private String selectedCourtRoomItem;

    private Integer selectedCourtRoomId;

    private Date time;

    private String judgeName;

    private Collection defendants;

    private Collection selectedBWHDefendants;

    private XhibitApplicationController xac;

    private Integer refJudgeId;

    private XhbCourtRoomBasicValue courtRoomBasicValue;

    private String caseTitle;

    private String caseTypeAndNumber;

    private RefHearingTypeBasicValue refHearingTypeBasicValue;

    private String caseLifeCycleCommand;

    public AddHearingModel() {
    }

    // Getters
    public Integer getCaseNumber() {
        return caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public Integer getCourtSiteId() {
        return courtSiteId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public String getSelectedCourtRoomItem() {
        return selectedCourtRoomItem;
    }

    public Integer getSelectedCourtRoomId() {
        return selectedCourtRoomId;
    }

    public Date getTime() {
        return time;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public Collection getDefendants() {
        return defendants;
    }

    public Collection getSelectedBWHDefendants() {
        return selectedBWHDefendants;
    }

    public XhibitApplicationController getXAC() {
        return xac;
    }

    public Integer getRefJudgeId() {
        return refJudgeId;
    }

    public XhbCourtRoomBasicValue getCourtRoomBasicValue() {
        return courtRoomBasicValue;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public String getCaseTypeAndNumber() {
        return (getCaseType() == null || getCaseNumber() == null ? "" : getCaseType() + getCaseNumber().toString());
    }

    public RefHearingTypeBasicValue getRefHearingTypeBasicValue() {
        return refHearingTypeBasicValue;
    }

    public String getCaseLifeCycleCommand() {
        return caseLifeCycleCommand;
    }

    public boolean isExistingCase() {
        return (getCaseLifeCycleCommand() != null && getCaseLifeCycleCommand().equalsIgnoreCase(
                getBundleEntry("AddHearingExistingCaseAction")));
    }

    public boolean isNewUCase() {
        return (getCaseLifeCycleCommand() != null && getCaseLifeCycleCommand().equalsIgnoreCase(
                getBundleEntry("AddHearingNewUCaseAction")));
    }

    // Setters
    public void setCaseNumber(Integer caseno) {
        caseNumber = caseno;
    }

    public void setCaseType(String ct) {
        caseType = ct;
    }

    public void setCourtSiteId(Integer ctsiteId) {
        courtSiteId = ctsiteId;
    }

    public void setCourtId(Integer ctId) {
        courtId = ctId;
    }

    public void setSelectedCourtRoomItem(String s) {
        selectedCourtRoomItem = s;
    }

    public void setSelectedCourtRoomId(Integer i) {
        selectedCourtRoomId = i;
    }

    public void setTime(Date t) {
        time = t;
    }

    public void setJudgeName(String name) {
        judgeName = name;
    }

    public void setDefendants(Collection d) {
        defendants = d;
    }

    public void setSelectedBWHDefendants(Collection sd) {
        selectedBWHDefendants = sd;
    }

    public void setXAC(XhibitApplicationController x) {
        xac = x;
    }

    public void setRefJudgeId(Integer judgeId) {
        refJudgeId = judgeId;
    }

    public void setCourtRoomBasicValue(XhbCourtRoomBasicValue crbv) {
        courtRoomBasicValue = crbv;
    }

    public void setCaseTitle(String title) {
        caseTitle = title;
    }

    public void setRefHearingTypeBasicValue(RefHearingTypeBasicValue param) {
        refHearingTypeBasicValue = param;
    }

    public void setCaseLifeCycleCommand(String param) {
        caseLifeCycleCommand = param;
    }

    public void printModel() {
        // only do all of this if debug is enabled!
        if (log.isDebugEnabled()) {
            log.debug("AddHearingModel");
            log.debug("---------------");
            log.debug("Case Title             : " + getCaseTitle());
            log.debug("Case Number            : " + getCaseNumber());
            log.debug("Case Type              : " + getCaseType());
            log.debug("Court Site Id          : " + getCourtSiteId());
            log.debug("Court Id               : " + getCourtId());
            log.debug("Selected Court Room    : " + getSelectedCourtRoomItem());
            log.debug("Selected Court Room Id : " + getSelectedCourtRoomId());
            log.debug("Time                   : " + XDateFormat.format(getTime(), XDateFormat.TIMEFORMAT));
            log.debug("Judge                  : " + getJudgeName());
            log.debug("Judge ID               : " + getRefJudgeId());
            log.debug("Court Room Basic Value : " + getCourtRoomBasicValue());
            log.debug("Case Life Cycle Command: " + getCaseLifeCycleCommand());
            log.debug("Defendants List        : ");
            printListOfDefendants(getDefendants());
            log.debug("------------------------- ");
            log.debug("Selected BWH Defendants List : ");
            printListOfDefendants(getSelectedBWHDefendants());
            log.debug("------------------------- ");
            log.debug("XAC                 : " + getXAC().toString());
            log.debug("------------------------- ");
            log.debug("RefHearingTypeBasicValue: ");
            printHearingType();
        }
    }

    private void printListOfDefendants(Collection defendantsCollection) {
        if (defendantsCollection == null) {
            log.debug(" No list of defendants ");
        } else {
            final Iterator it = defendantsCollection.iterator();
            for (int i = 1; it.hasNext(); i++) {
                final DefendantValue dv = (DefendantValue) it.next();
                log.debug(" Defendant " + i + ":   " + dv.getFirstName() + " " + dv.getSurName());
            }
        }
    }

    private void printHearingType() {
        if (getRefHearingTypeBasicValue() == null) {
            log.debug(" No hearing type ");
        } else {
            log.debug("id  :" + getRefHearingTypeBasicValue().getId());
            log.debug("code:" + getRefHearingTypeBasicValue().getHearingTypeCode());
            log.debug("desc:" + getRefHearingTypeBasicValue().getHearingTypeDesc());
        }
    }

    private String getBundleEntry(String param) {
        return ResourceBundleHelper.getResource(XhibitBundles.TodaysSchedule, param);
    }
}
