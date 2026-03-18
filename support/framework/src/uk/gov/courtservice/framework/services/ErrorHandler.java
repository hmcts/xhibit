package uk.gov.courtservice.framework.services;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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
public interface ErrorHandler {
    public String handleError(Throwable t, Class klass, String errMsg);

    public String handleError(Throwable t, Class klass);
}