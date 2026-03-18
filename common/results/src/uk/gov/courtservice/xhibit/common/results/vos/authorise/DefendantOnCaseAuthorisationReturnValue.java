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
 * @author Simon Gilmore
 * @version $Id: DefendantOnCaseAuthorisationReturnValue.java,v 1.1 2005/01/28
 *          08:31:12 rzvddy Exp $
 */

public class DefendantOnCaseAuthorisationReturnValue extends AuthorisationValue {
    
	static final long serialVersionUID = -8874440964510942138L;
	
	private final ArrayList failures = new ArrayList();

    public DefendantOnCaseAuthorisationReturnValue(XhbDefendantBasicValue defendant, Integer defendantOnCaseId,
            String resultsAuthorised, String amendedReason) {
        super(defendant, defendantOnCaseId, resultsAuthorised, amendedReason);
    }

    public void addFailure(FailureMessage failureMessage) {
        failures.add(failureMessage);
    }

    public FailureMessage[] getFailures() {
        return (FailureMessage[]) failures.toArray(new FailureMessage[failures.size()]);
    }

    public boolean hasDefendantFailed() {
        return !failures.isEmpty();
    }
}