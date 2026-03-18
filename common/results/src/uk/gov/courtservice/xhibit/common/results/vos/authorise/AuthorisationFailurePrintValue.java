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

public class AuthorisationFailurePrintValue extends CSAbstractValue {
	
	static final long serialVersionUID = 586655281446682637L;

    public AuthorisationFailurePrintValue() {
    }

    private boolean caseFailure;

    private boolean defOnCaseFailure;

    private String caseTypeAndNumber;

    private Vector caseFailureReasons;

    private Vector defendantPrintValues;

    private Vector defendantOnCasePrintValues;

    public boolean isCaseFailure() {
        return caseFailure;
    }

    public boolean isDefOnCaseFailure() {
        return defOnCaseFailure;
    }

    public String getCaseTypeAndNumber() {
        return caseTypeAndNumber;
    }

    public Vector getCaseFailureReasons() {
        return caseFailureReasons;
    }

    public Vector getDefendantPrintValues() {
        return defendantPrintValues;
    }

    public Vector getDefendantOnCasePrintValues() {
        return defendantOnCasePrintValues;
    }

    public void setCaseFailure(boolean param) {
        caseFailure = param;
    }

    public void setDefOnCaseFailure(boolean param) {
        defOnCaseFailure = param;
    }

    public void setCaseTypeAndNumber(String param) {
        caseTypeAndNumber = param;
    }

    public void setCaseFailureReasons(Vector param) {
        caseFailureReasons = param;
    }

    public void setDefendantPrintValue(Vector param) {
        defendantPrintValues = param;
    }

    public void setDefendantOnCasePrintValue(Vector param) {
        defendantOnCasePrintValues = param;
    }
}