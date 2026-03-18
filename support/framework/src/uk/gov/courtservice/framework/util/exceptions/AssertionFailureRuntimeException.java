package uk.gov.courtservice.framework.util.exceptions;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class AssertionFailureRuntimeException extends CSUnrecoverableException {
    public AssertionFailureRuntimeException(String s) {
        super(s);
    }
}
