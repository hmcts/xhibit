package datamigration1745.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>Title: DefendantByCourtVO </p>
 * <p>Description: A VO that describes Defendant By Court details.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 *
 * @author GJS
 * @version 1.0
 */
public class DefendantByCourtVO extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer courtId;
    private Integer caseId;
    private String caseType;
    private String caseSubType;
    private Integer caseNumber;
    private String ptiurn;
    private String firstName;
    private String surname;
    private Integer defendantId;
    private Integer crestDefendantId;

    public Integer getCrestDefendantId() {
        return crestDefendantId;
    }

    public void setCrestDefendantId(Integer crestDefendantId) {
        this.crestDefendantId = crestDefendantId;
    }

    public Integer getDefendantId() {
        return defendantId;
    }

    public void setDefendantId(Integer defendantId) {
        this.defendantId = defendantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Empty constructor
     */
    public DefendantByCourtVO() { }

    public DefendantByCourtVO(Integer courtId,
                              Integer caseId,
                              String caseType,
                              String caseSubType,
                              Integer caseNumber,
                              Integer id,
                              String ptiurn,
                              String firstName,
                              String surname,
                              Integer defendantId,
                              Integer crestDefendantId) {
        setCourtId(courtId);
        setCaseId(caseId);
        setCaseType(caseType);
        setCaseNumber(caseNumber);
        setId(id);
        setPtiurn(ptiurn);
        setFirstName(firstName);
        setSurname(surname);
        setDefendantId(defendantId);
        setCaseSubType(caseSubType);
        setCrestDefendantId(crestDefendantId);
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType=caseType;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getPtiurn() {
        return ptiurn;
    }

    public void setPtiurn(String ptiurn) {
        this.ptiurn=ptiurn;
    }

    /**
     * Return a debug object containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Court Id=");
        builder.append(courtId);
        builder.append(",Case Id=");
        builder.append(caseId);
        builder.append(",Case Type=");
        builder.append(caseType);
        builder.append(",Case Number=");
        builder.append(caseNumber);
        builder.append(",DOC Id=");
        builder.append(getId());
        builder.append(",PTIURN=");
        builder.append(getPtiurn());
        builder.append(",Defendant Id=");
        builder.append(getDefendantId());
        builder.append(",Crest Defendant Id=");
        builder.append(getCrestDefendantId());
        builder.append("]");
        return builder.toString();
    }

    public String getCaseSubType() {
        return caseSubType;
    }

    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }
}
