package uk.gov.courtservice.xhibit.common.results.vos.authorise;

/**
 * <p>
 * Title: Failure message for Disposal Appeal Results.
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
 * @author Simon Gilmore
 * @version $Id: DisposalAppealResultFailureMessage.java,v 1.1 2005/01/28
 *          08:27:57 rzvddy Exp $
 */

public class DisposalAppealResultFailureMessage extends FailureMessage {
	
	static final long serialVersionUID = -1789955053409511212L;
	
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
    public DisposalAppealResultFailureMessage(final String key, final Object[] parameters) {
        super(key);
        this.parameters = parameters;
    }

    /**
     * Get the authorisation failure details
     * 
     * @return Details of the authorisation failure.
     */
    public String getFailureDetailsKey() {
        return "results.authorise.printValue.magistrate";
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