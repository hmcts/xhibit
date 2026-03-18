/*
 * Created on 13-Jan-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.framework.services.threadpool;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * 
 * @author pznwc5 The exception is thrown when the thread pool is inactive
 */
public class WorkerUnavailableException extends CSUnrecoverableException {
    /**
     * @param ex
     *            Interrupted exception
     */
    public WorkerUnavailableException(InterruptedException ex) {
        super(ex.getMessage(), ex);
    }
}
