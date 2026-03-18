package uk.gov.courtservice.xhibit.web.framework.servlet;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;


/**
 * <p>
 * Title: XHIBIT 2
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
 * @author unascribed
 * @version 1.0
 */

public class XhibitThinClientCallbackHandler implements CallbackHandler {

    public XhibitThinClientCallbackHandler() {
        // Nothing to do here
    }

    public void handle(Callback[] callbacks) throws java.io.IOException,
            javax.security.auth.callback.UnsupportedCallbackException {
        String thisUserName = "";
        String thisPassword = "";
        Integer thisCourtId = null;
    }
    
    
}