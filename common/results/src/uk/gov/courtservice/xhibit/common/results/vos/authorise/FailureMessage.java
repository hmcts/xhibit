package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.io.Serializable;

/**
 * <p>
 * Title: Base class for failure messages.
 * </p>
 * <p>
 * Description: The base class for failure messages used to report failures when
 * attempting to authorise results.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Id: FailureMessage.java,v 1.3 2006/06/05 12:28:29 bzjrnl Exp $
 */

public abstract class FailureMessage implements Serializable {
	
	static final long serialVersionUID = 3939701551549346553L;
	
    /**
     * The resource key that will be used to lookup the failure reason.
     */
    private final String failureKey;

    /**
     * Creates a failure message using the given key.
     * 
     * @param key
     *            the resource key used to lookup the failure reason.
     */
    public FailureMessage(final String key) {
        this.failureKey = key;
    }

    /**
     * Get the resource key used to lookup the failure.
     * 
     * @return the key to lookup the failure
     */
    public String getFailureKey() {
        return failureKey;
    }

    /**
     * Get the authorisation failure details
     * 
     * @return Details of the authorisation failure.
     */
    public abstract String getFailureDetailsKey();

    /**
     * Has the Failure Details got Parameters.
     * 
     * @return true if the failure details has parameters.
     */
    public abstract boolean hasFailureDetailsParameters();

    /**
     * Get the authorisation failure details lookup key
     * 
     * @return Details of the authorisation failure.
     */
    public abstract Object[] getFailureDetailsParameters();

}