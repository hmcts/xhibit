package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.security.ServerRepository;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import weblogic.security.auth.callback.URLCallback;

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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class CallBackHandlerForTesting implements CallbackHandler {

    public CallBackHandlerForTesting() {
    }

    public void handle(Callback[] callbacks) throws java.io.IOException,
            javax.security.auth.callback.UnsupportedCallbackException {
        String thisUserName = "xhibit_internal";
        String thisPassword = "password";

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof TextOutputCallback) {

                // display the message according to the specified type
                TextOutputCallback toc = (TextOutputCallback) callbacks[i];
                switch (toc.getMessageType()) {
                case TextOutputCallback.INFORMATION:
                    JOptionPane.showMessageDialog(null, toc.getMessage(), "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(toc.getMessage());
                    break;
                case TextOutputCallback.WARNING:
                    JOptionPane.showMessageDialog(null, toc.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("WARNING: " + toc.getMessage());
                    break;
                case TextOutputCallback.ERROR:
                    JOptionPane.showMessageDialog(null, toc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("ERROR: " + toc.getMessage());
                    break;
                default:
                    throw new IOException("Unsupported message type: " + toc.getMessageType());
                }

            } else if (callbacks[i] instanceof NameCallback) {

                // prompt the user for a username
                NameCallback nc = (NameCallback) callbacks[i];
                nc.setName(thisUserName);

            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callbacks[i];
                pc.setPassword(thisPassword.toCharArray());
            } else if (callbacks[i] instanceof URLCallback) {
                URLCallback uc = (URLCallback) callbacks[i];
                uc.setURL(ServerRepository.getAuthenticationURL(CSServices.getCSUserSession().getServer()));
                XHIBITConstant
                        .debug("ServerRepository.getAuthenticationURL(CSServices.getCSUserSession().getServer())="
                                + ServerRepository.getAuthenticationURL(CSServices.getCSUserSession().getServer()));
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
}