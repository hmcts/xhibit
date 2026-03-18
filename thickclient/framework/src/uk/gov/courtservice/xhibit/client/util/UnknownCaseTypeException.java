package uk.gov.courtservice.xhibit.client.util;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class UnknownCaseTypeException extends CSRecoverableException {

    public UnknownCaseTypeException() {
        super();
    }

    public UnknownCaseTypeException(String string, String string1, Throwable throwable) {
        super(string, string1, throwable);
    }

    public UnknownCaseTypeException(String string, String string1) {
        super(string, string1);
    }

    public UnknownCaseTypeException(String string, Object[] objectArray, String string2) {
        super(string, objectArray, string2);
    }
}