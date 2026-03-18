package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

/**
 * Value object to store the case information required on a CREST form 'A'. This
 * is a read-only object.
 * 
 * From case table on Pre Hearing CMR
 */
public class HRCaseValue implements HRValueObject {

    private Integer caseID;

    private String caseType;

    private String caseSubType;

    private Integer caseNumber;

    private Integer estPDHTrialLength; // estimated trial time

    private String crestSeveredInd; // is indictment severed

    private Integer noProsWitness;

    private Integer noPageProsEvidence;

    private Integer lengthTape;
    
    private static final long serialVersionUID = 5243733284168994598L;

    public HRCaseValue(Integer caseID) {
        this.caseID = caseID;
    }

    public HRCaseValue() {
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer id) {
        this.caseID = id;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getCaseSubType() {
        return caseSubType;
    }

    public Integer getLengthTape() {
        return lengthTape;
    }

    public Integer getNoPageProsEvidence() {
        return noPageProsEvidence;
    }

    public void setNoPageProsEvidence(Integer noPageProsEvidence) {
        this.noPageProsEvidence = noPageProsEvidence;
    }

    public void setLengthTape(Integer lengthTape) {
        this.lengthTape = lengthTape;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Integer getEstPDHTrialLength() {
        return estPDHTrialLength;
    }

    public void setEstPDHTrialLength(Integer estPDHTrialLength) {
        this.estPDHTrialLength = estPDHTrialLength;
    }

    public String getCrestSeveredInd() {
        return crestSeveredInd;
    }

    public void setCrestSeveredInd(String crestSeveredInd) {
        this.crestSeveredInd = crestSeveredInd;
    }

    public Integer getNoProsWitness() {
        return noProsWitness;
    }

    public void setNoProsWitness(Integer noProsWitness) {
        this.noProsWitness = noProsWitness;
    }
}
