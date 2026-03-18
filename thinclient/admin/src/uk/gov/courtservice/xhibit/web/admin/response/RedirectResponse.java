package uk.gov.courtservice.xhibit.web.admin.response;

import uk.gov.courtservice.xhibit.web.framework.response.AbstractResponse;
import uk.gov.courtservice.xhibit.web.framework.response.ResponseEnvironment;
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
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.1 $ $Log:
 *         RedirectResponse.java,v $ Revision 1.2 2003/08/11 08:25:07 bzw8gp Jon
 *         Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.1 2003/03/24 08:23:26 fz0n8j Added view daily list, and public
 * display
 * 
 * Revision 1.1 2003/03/19 12:28:04 fz0n8j Framework update.
 * 
 * 
 */
public class RedirectResponse extends AbstractResponse {
    /**
     * The page parameter key
     */
    private static final String PAGE_KEY = "page";

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
        responseEnvironment.redirect(getParameter(PAGE_KEY));
    }
}
