package uk.gov.courtservice.framework.services.errorhandling;

import uk.gov.courtservice.framework.services.ErrorHandler;

/**
 * <p>
 * Title: AbstractErrorHandler
 * </p>
 * <p>
 * Description: Base class which specific error handlers should extend.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 */

abstract class AbstractErrorHandler implements ErrorHandler {

    public AbstractErrorHandler() {
    }

    public abstract String handleError(Throwable t, Class klass, String errMsg);

    public abstract String handleError(Throwable t, Class klass);

}