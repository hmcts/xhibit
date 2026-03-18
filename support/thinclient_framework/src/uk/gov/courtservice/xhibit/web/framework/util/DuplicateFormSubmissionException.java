package uk.gov.courtservice.xhibit.web.framework.util;

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
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class DuplicateFormSubmissionException extends FrameworkException {

    public DuplicateFormSubmissionException() {
        this("Duplicate form submission");
    }

    public DuplicateFormSubmissionException(String message) {
        super("web.framework.util.DuplicateFormSubmissionException", message);
    }
}