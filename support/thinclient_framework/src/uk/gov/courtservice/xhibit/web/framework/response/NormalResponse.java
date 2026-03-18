package uk.gov.courtservice.xhibit.web.framework.response;

import java.util.Iterator;

import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Normal Response
 * </p>
 * <p>
 * Description: The normal response for the application, adds the parameters to
 * the request then forwards to the page in the page parameter
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.3 $ $Log:
 *         NormalResponse.java,v $ Revision 1.1 2003/03/31 13:22:00 rz3jq5
 *         Restructuring, moving to framework.
 * 
 * Revision 1.1 2003/03/27 11:01:24 fz0n8j Added to cvs
 * 
 * 
 */
public class NormalResponse extends AbstractResponse {
    /**
     * The page parameter key
     */
    private static final String PAGE_KEY = "page";

    /**
     * Empty normal constructor
     */
    public NormalResponse() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested
     * response.
     * </p>
     * 
     * @param responseEnvironment
     *            the environment to evaluate the response in
     */
    public void internalPerformResponse(ResponseEnvironment responseEnvironment) throws FrameworkException {
        Iterator names = getParameterNames();
        while (names.hasNext()) {
            String name = (String) names.next();
            responseEnvironment.setRequestParameter(name, getParameter(name));
        }
        responseEnvironment.forward(getParameter(PAGE_KEY));
    }
}
