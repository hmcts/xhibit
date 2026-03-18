package uk.gov.courtservice.xhibit.web.framework.response;

import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Redirect Response
 * </p>
 * <p>
 * Description: redirects according to a url, won't pass parameters . . .
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.4 $ $Log:
 *         RedirectResponse.java,v $ Revision 1.2 2003/10/01 15:29:24 bzw8gp Jon
 *         Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.1 2003/06/17 06:42:25 cawleye *** empty log message ***
 * 
 * 
 */
public class RedirectResponse extends AbstractResponse {
    /**
     * The page parameter key
     */
    private static final String URL_KEY = "url";

    /**
     * Empty normal constructor
     */
    public RedirectResponse() {
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
        responseEnvironment.redirect(getParameter(URL_KEY));
    }
}
