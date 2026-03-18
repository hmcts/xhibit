package uk.gov.courtservice.xhibit.web.framework.response;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.framework.control.AbstractControl;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: Response
 * </p>
 * <p>
 * Description: This class provides common functionality that is suitable for
 * most Response implementations. <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.5 $ <p/>
 */
public abstract class AbstractResponse extends AbstractControl implements Response {

    private static final Logger log = CSServices.getLogger(AbstractResponse.class);

    /**
     * <p>
     * This method is invoked by the framework to perform the requested
     * response.
     * </p>
     * 
     * @param responseEnvironment
     *            the environment to evaluate the response in
     * @throws IllegalArgumentException
     *             if the environment is null
     * @throws FrameworkException
     *             if an error occurs
     */
    public final void performResponse(ResponseEnvironment responseEnvironment) throws IllegalArgumentException,
            FrameworkException {

        if (responseEnvironment == null) {
            throw new IllegalArgumentException("responseEnvironment");
        }

        try {
            String sbk = (String) getParameter("skeletonBodyKey");

            if (!sbk.equals("home")) {
                int tokenid = (int) (Math.random() * 1000000000);
                responseEnvironment.setSessionParameter("stoken", "" + tokenid);
            }
        } catch (ParameterNotFoundException e) {
            log.info(e);
        }

        internalPerformResponse(responseEnvironment);

    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested
     * response.
     * </p>
     * 
     * @param responseEnvironment
     *            the environment to evaluate the response in
     * @throws FrameworkException
     *             if an error occurs
     */
    protected abstract void internalPerformResponse(ResponseEnvironment responseEnvironment) throws FrameworkException;

}
