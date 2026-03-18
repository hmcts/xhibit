package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseDeftValue;

/**
 * <p>
 * Title: UnauthorisedCaseStatusTableRowModel
 * </p>
 * <p>
 * Description: This Class is used to represent a row in the 'Unauthorised Case
 * Status' grid
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class UnauthorisedCaseStatusTableRowModel {
    private String courtRoom;
    private XhbCaseBasicValue caseValue;
    private UnauthorisedCaseDeftValue[] defendants;
    private Date caseConclusionDate;
    private Integer scheduledHearingId;
    private Date scheduledHearingDate;    
    private Integer court_site_id;
    
    
    public void setCourtRoom(String courtRoom) {
        this.courtRoom = courtRoom;
    }
    public String getCourtRoom() {
        return courtRoom;
    }
    public void setCaseValue(XhbCaseBasicValue caseValue) {
        this.caseValue = caseValue;
    }
    public XhbCaseBasicValue getCaseValue() {
        return caseValue;
    }
    public void setDefendants(UnauthorisedCaseDeftValue[] defendants) {
        this.defendants = defendants;
    }
    public UnauthorisedCaseDeftValue[] getDefendants() {
        return defendants;
    }
    public void setCaseConclusionDate(Date caseConclusionDate) {
        this.caseConclusionDate = caseConclusionDate;
    }
    public Date getCaseConclusionDate() {
        return caseConclusionDate;
    }
    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }
    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }
    public void setCourt_site_id(Integer court_site_id) {
        this.court_site_id = court_site_id;
    }
    public Integer getCourt_site_id() {
        return court_site_id;
    }
    public void setScheduledHearingDate(Date scheduledHearingDate) {
        this.scheduledHearingDate = scheduledHearingDate;
    }
    public Date getScheduledHearingDate() {
        return scheduledHearingDate;
    }
}
