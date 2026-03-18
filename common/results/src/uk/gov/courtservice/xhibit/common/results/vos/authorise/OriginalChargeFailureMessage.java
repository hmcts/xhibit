package uk.gov.courtservice.xhibit.common.results.vos.authorise;

/**
 * <p>
 * Title: Failure message for missining Original Charge when no Indictment present.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version $Id: OriginalChargeFailureMessage.java,v 1.0 2005/01/28
 *          08:27:57 qz4rwx Exp $
 */

public class OriginalChargeFailureMessage extends FailureMessage {
	
	static final long serialVersionUID = -7437039090975441773L;
	
    /**
     * Parameters uesd to insert into the resource string.
     */
    Object[] parameters;

    /**
     * Creates a failure message using the given key.
     *
     * @param key
     *            the resource key used to lookup the failure reason.
     * @param parameters
     *            uesd to insert into the resource string.
     */
    public OriginalChargeFailureMessage(final String key, final Object[] parameters) {
        super(key);
        this.parameters = parameters;
    }

    /**
     * Get the authorisation failure details
     *
     * @return Details of the authorisation failure.
     */
    public String getFailureDetailsKey() {
        return "results.authorise.printValue.originalCharge";
    }

    /**
     * Has the Failure Details got Parameters.
     *
     * @return true if the failure details has parameters.
     */
    public boolean hasFailureDetailsParameters() {
        return true;
    }

    /**
     * Get the authorisation failure details lookup key
     *
     * @return Details of the authorisation failure.
     */
    public Object[] getFailureDetailsParameters() {
        return parameters;
    }
}