package uk.gov.courtservice.xhibit.common.results.vos.authorise;


import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;

/**
 * <p>
 * Title: Object containing defendants and their results authorisation state
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AuthorisationValue.java,v 1.9 2011/07/11 10:28:00 hingstb Exp $
 */

public class AuthorisationValue extends CSAbstractValue {
	
	static final long serialVersionUID = 5569598233660141520L;
    
    public static final String AUTHORISED_READY = "R";

    public static final String AUTHORISED_EXPORTING = "U";

    public static final String AUTHORISED_EXPORT_SUCCESS = "E";

    public static final String AUTHORISED_EXPORT_FAILURE = "F";
    
    public static final String AUTHORISED_INITIAL_STATUS = "N";

    private XhbDefendantBasicValue defendant;

    private Integer defendantOnCaseId;

    private String resultsAuthorised;
    
    private String amendedReason;

    /**
     * Constructor
     * 
     * @param defendant
     * @param defendantOnCaseId
     * @param resultsVerified
     */
    public AuthorisationValue(XhbDefendantBasicValue defendant, Integer defendantOnCaseId, String resultsAuthorised, String amendedReason) {
        this.defendant = defendant;
        this.defendantOnCaseId = defendantOnCaseId;
        this.resultsAuthorised = resultsAuthorised;
        this.amendedReason = amendedReason;
    }

    public XhbDefendantBasicValue getDefendant() {
        return defendant;
    }

    public void setDefendant(XhbDefendantBasicValue defendant) {
        this.defendant = defendant;
    }

    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }

    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }

    public void setResultsAuthorised(String resultsAuthorised) {
        this.resultsAuthorised = resultsAuthorised;
    }

    public String getResultsAuthorised() {
        return resultsAuthorised;
    }
    
    public void setAmendedReason(String amendedReason){
        this.amendedReason = amendedReason;
    }
    
    public String getAmendedReason(){
        return amendedReason;
    }
    
    public String toString() {
        StringBuffer str = new StringBuffer("{");
        
        str.append("AuthorisationValue:: defendantOnCaseId="+defendantOnCaseId.intValue() + " " + "defendant="+defendant.toString() + " " + "resultsAuthorised="+resultsAuthorised + " " + "amendedReason="+amendedReason);
        str.append('}');

        return(str.toString());
    }
}