package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

/**
 * <p>
 * Title: AuthoriseWarning
 * </p>
 * <p>
 * Description: A Value Object which contains the results of the
 * pre-authorisation check.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class AuthoriseWarning extends CSAbstractValue {
    private static final long serialVersionUID = 1L;
    
    private Integer defendantId;
    private String firstName;
    private String surname;
    private String disposalCode;
    private Date disposalResultDate;
    private Date hearingEndDate;

    public AuthoriseWarning(){
        super();
    }

    public String toString() {
        return "id=" + defendantId
            + " firstName=" + firstName
            + " surname=" + surname
            + " disposalCode=" + disposalCode
            + " disposalResultDate=" + disposalResultDate
            + " hearingEndDate=" + hearingEndDate;
    }
    
    public void setDefendantId(Integer value) {
        this.defendantId = value;
    }

    public Integer getDefendantId() {
        return this.defendantId;
    }
    
    public void setFirstName(String value) {
        this.firstName = value;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setSurname(String value) {
        this.surname = value;
    }
    
    public String getSurname() {
        return this.surname;
    }
    
    public void setDisposalCode(String value) {
        this.disposalCode = value;
    }
    
    public String getDisposalCode() {
        return this.disposalCode;
    }
    
    public void setDisposalResultDate(Date value) {
        this.disposalResultDate = value;
    }
    
    public Date getDisposalResultDate() {
        return this.disposalResultDate;
    }
    
    public void setHearingEndDate(Date value) {
        this.hearingEndDate = value;
    }
    
    public Date getHearingEndDate() {
        return this.hearingEndDate;
    }
    
}
