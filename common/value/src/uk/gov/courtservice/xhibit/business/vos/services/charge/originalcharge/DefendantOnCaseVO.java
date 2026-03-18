package uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge;

import java.io.Serializable;

/**
 * <p>Title: DefendantOnCaseVO</p>
 * <p>Description: A VO that contains selected DefendantOnCase information sufficient to populate the Original charges screen.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
public class DefendantOnCaseVO implements Serializable {
    private Integer courtId;
    private Integer caseId;
    private String caseType;
    private Integer caseNumber;
    private Integer defendantOnCaseId;
    private String asn;
    private Integer defendantId;
    private String firstName;
    private String middleName;
    private String surname;
    private Integer totalIndictments;
    private Integer totalOriginalCharges;
    private static final long serialVersionUID = 3805845362054275826L;
    
    public String getAsn() {
        return asn;
    }
    public void setAsn(String asn) {
        this.asn = asn;
    }
    public Integer getCaseId() {
        return caseId;
    }
    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }
    public Integer getCaseNumber() {
        return caseNumber;
    }
    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }
    public String getCaseType() {
        return caseType;
    }
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
    public Integer getDefendantId() {
        return defendantId;
    }
    public void setDefendantId(Integer defendantId) {
        this.defendantId = defendantId;
    }
    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }
    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public Integer getTotalIndictments() {
        return totalIndictments;
    }
    public void setTotalIndictments(Integer totalIndictments) {
        this.totalIndictments = totalIndictments;
    }
    public Integer getTotalOriginalCharges() {
        return totalOriginalCharges;
    }
    public void setTotalOriginalCharges(Integer totalOriginalCharges) {
        this.totalOriginalCharges = totalOriginalCharges;
    }
    public String getDisplayableName() {
        StringBuilder bld = new StringBuilder();

        bld.append(getSurname() == null ? "" : getSurname());

        if (getFirstName() != null && getFirstName().length() > 0) {
            if (bld.length() > 0) {
                bld.append(", ");
            }

            bld.append(getFirstName() == null ? "" : getFirstName());
        }

        return bld.toString();
    }
    public boolean hasIndictments() {
        return getTotalIndictments().intValue() > 0;
    }
    public boolean hasNoIndictments() {
        return !hasIndictments();
    }
    public Integer getCourtId() {
        return courtId;
    }
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }
}
