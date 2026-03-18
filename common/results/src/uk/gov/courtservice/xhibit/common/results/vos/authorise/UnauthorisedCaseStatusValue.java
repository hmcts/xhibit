package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;

/**
 * <p>
 * Title: UnauthorisedCaseValue
 * </p>
 * <p>
 * Description:  This class is used to represent an unauthorised case which needs to be 
 * displayed in the Unauthorised Case Status screen. It holds all the values that are
 * required to be displayed.
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


public class UnauthorisedCaseStatusValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;
    
    private UnauthorisedCaseDeftValue[] defendants;
    private CourtRoomBasicValue courtRoom;
    private XhbCaseBasicValue caseValue;
    private Date conclusionDate;
    private Integer scheduledHearingId;
    private Date scheduledHearingDate;
    
    
    public UnauthorisedCaseStatusValue(UnauthorisedCaseDeftValue[] defendants,CourtRoomBasicValue courtRoom,
            XhbCaseBasicValue caseValue,Integer scheduledHearingId,Date scheduledHearingDate, Date conclusionDate){
        this.defendants = defendants;
        this.courtRoom = courtRoom;
        this.caseValue = caseValue;
        this.conclusionDate = conclusionDate;
        this.scheduledHearingId = scheduledHearingId;        
        this.setScheduledHearingDate(scheduledHearingDate);
    }

    public void setDefendants(UnauthorisedCaseDeftValue[] defendants) {
        this.defendants = defendants;
    }

    public UnauthorisedCaseDeftValue[] getDefendants() {
        return defendants;
    }

    public void setCourtRoom(CourtRoomBasicValue courtRoom) {
        this.courtRoom = courtRoom;
    }

    public CourtRoomBasicValue getCourtRoom() {
        return courtRoom;
    }

    public void setCaseValue(XhbCaseBasicValue caseValue) {
        this.caseValue = caseValue;
    }

    public XhbCaseBasicValue getCaseValue() {
        return caseValue;
    }

    public void setConclusionDate(Date conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public Date getConclusionDate() {
        return conclusionDate;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }

    public void setScheduledHearingDate(Date scheduledHearingDate) {
        this.scheduledHearingDate = scheduledHearingDate;
    }

    public Date getScheduledHearingDate() {
        return scheduledHearingDate;
    }
    
    
}
