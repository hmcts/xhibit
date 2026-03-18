package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Vector;

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

public class DefendantPrintValue extends CSAbstractValue {
	
	static final long serialVersionUID = 8219887730002700122L;
	
    private boolean defendantFailure;

    private String defendantFullName;

    private Vector defendantFailureReasons;

    public DefendantPrintValue() {
    }

    public boolean isDefendantFailure() {
        return defendantFailure;
    }

    public String getDefendantFullName() {
        return defendantFullName;
    }

    public Vector getDefendantFailureReasons() {
        return defendantFailureReasons;
    }

    public void setDefendantFailure(boolean param) {
        defendantFailure = param;
    }

    public void setDefendantFullName(String param) {
        defendantFullName = param;
    }

    public void setDefendantFailureReasons(Vector param) {
        defendantFailureReasons = param;
    }
}