package uk.gov.courtservice.xhibit.courtlog.exceptions;

/**
 * @author pznwc5
 */
public class NoCaseCalledOnEventException extends CourtLogBusinessException {
	
	static final long serialVersionUID = -8226317282214973792L;
	
    /** Error key */
    public static final String KEY = "Court_Log.No_Case_Called_On_Prior_To_Adjournment";

    /**
     * 
     * @param caseId
     *            Case Id
     * @param ex
     *            Cause
     */
    public NoCaseCalledOnEventException(Integer caseId) {
        super(KEY, new Object[] { caseId }, "No case called on event to match adjournment for caseId");
    }
}
