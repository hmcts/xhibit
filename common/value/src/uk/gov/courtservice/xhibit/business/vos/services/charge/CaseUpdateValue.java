package uk.gov.courtservice.xhibit.business.vos.services.charge;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseUpdateValue
 * </p>
 * <p>
 * Description: CaseUpdateValue is intended to represent case entities as stored in
 *              the case table. for use with mercator map Update Case
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Kelvin Davies
 * @version 1.0

 */

public class CaseUpdateValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer caseNumber;

    private String caseType;

    private Integer courtID;

    private String vulnerableVictimIndicator;

    public CaseUpdateValue(){
       super();
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

    public Integer getCourtID() {
        return courtID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public String getVulnerableVictimIndicator() {
        return vulnerableVictimIndicator;
    }

    public void setVulnerableVictimIndicator(String vulnerableVictimIndicator) {
        this.vulnerableVictimIndicator = vulnerableVictimIndicator;
    }

}