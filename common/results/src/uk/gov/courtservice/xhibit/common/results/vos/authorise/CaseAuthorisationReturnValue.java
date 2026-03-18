package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0
 */

public class CaseAuthorisationReturnValue extends CSAbstractValue {
	
	static final long serialVersionUID = 154559453847543223L;
	
    private ArrayList failures = new ArrayList();

    public CaseAuthorisationReturnValue() {
    }

    public void addFailureKey(String key) {
        failures.add(key);
    }

    public String[] getCaseFailures() {
        return (String[]) failures.toArray(new String[failures.size()]);
    }

    public boolean hasCaseLevelFailed() {
        return !failures.isEmpty();
    }
}