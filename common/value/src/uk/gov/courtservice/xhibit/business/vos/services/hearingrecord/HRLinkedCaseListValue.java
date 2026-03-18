package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

/**
 * Value object to store the linked case information required on a CREST form
 * 'A'. This is a read-only object.
 */
public class HRLinkedCaseListValue implements HRValueObject {

    private Integer caseID;

    private String caseType;

    private Integer caseNumber;
    private static final long serialVersionUID = 8369978628889956326L;
    
    public HRLinkedCaseListValue(Integer id) {
        this.caseID = id;
    }

    public HRLinkedCaseListValue() {
    }

    public Integer getCaseID() {
        return caseID;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseID(Integer id) {
        this.caseID = id;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }
}
