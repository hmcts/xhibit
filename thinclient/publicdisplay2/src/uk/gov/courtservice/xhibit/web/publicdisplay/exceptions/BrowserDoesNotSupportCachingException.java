package uk.gov.courtservice.xhibit.web.publicdisplay.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.Warning;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class BrowserDoesNotSupportCachingException extends PublicDisplayRuntimeException implements Warning {
    private static final Logger log = CSServices.getLogger(BrowserDoesNotSupportCachingException.class);

    public BrowserDoesNotSupportCachingException(HttpServletRequest request) {
        super("The browser in use is not generating an 'If-Modified-Since' header. This would cause"
                + "the system to perform excessive re-rendering. You must change the settings of the browser"
                + "such that it does produce the 'If-Modified-Since' header. See W3C RFC2616 at"
                + "http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html (section 14.25) for further clarification"
                + "regarding this header and it's usage.");
        log.warn("Non-caching browser at IP address: " + request.getRemoteHost());
    }
}
