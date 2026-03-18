package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;

/**
 * <p>
 * Title: Extension of authorisation value that includes failure reasons
 * </p>
 * <p>
 * Description: This class is returned after an authorisation request and will
 * include a list of authorisation failures for the defendant if any occured.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DefendantAuthorisationReturnValue.java,v 1.2 2004/05/26
 *          16:26:35 sz0t7n Exp $
 */

public class DefendantAuthorisationReturnValue extends AuthorisationValue {
	
	static final long serialVersionUID = -7817171047441362486L;
	
    private ArrayList failures = new ArrayList();

    public DefendantAuthorisationReturnValue(XhbDefendantBasicValue defendant, Integer defendantOnCaseId,
            String resultsAuthorised, String amendedReason) {
        super(defendant, defendantOnCaseId, resultsAuthorised, amendedReason);
    }

    public DefendantAuthorisationFailureValue[] getFailures() {
        return (DefendantAuthorisationFailureValue[]) failures.toArray(new DefendantAuthorisationFailureValue[failures
                .size()]);
    }

    public void addFailure(DefendantAuthorisationFailureValue fail) {
        failures.add(fail);
    }

    public boolean hasDefendantFailed() {
        return !failures.isEmpty();
    }
}