package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

/**
 * HearingRecord version of the entity Value.
 * 
 * @author Anthony Marin
 * @version 1.1
 */

public class HRExportAValue implements HRValueObject {

    private Integer exportAID;

    private Integer linkedHearingID;

    private String statusFlag;

    private String courtClerkName;

    private Integer hearingID;
    
    private static final long serialVersionUID = -1558147329600125301L;

    public HRExportAValue(Integer exportAID) {
        this.exportAID = exportAID;
    }

    public String getCourtClerkName() {
        return courtClerkName;
    }

    public Integer getHearingID() {
        return hearingID;
    }

    public Integer getLinkedHearingID() {
        return linkedHearingID;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setCourtClerkName(String courtClerkName) {
        this.courtClerkName = courtClerkName;
    }

    public Integer getExportAID() {
        return exportAID;
    }

    public void setHearingID(Integer hearingID) {
        this.hearingID = hearingID;
    }

    public void setLinkedHearingID(Integer linkedHearingID) {
        this.linkedHearingID = linkedHearingID;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

}