/**
 * 
 */
package jndiutil;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import weblogic.security.auth.callback.URLCallback;

final class SimpleCallbackHandler implements CallbackHandler
{
    private final String authenticationUrl;
    private final String principal;
    private final String credential;

    public SimpleCallbackHandler(String authenticationUrl, String principal, String credential)
    {
        this.authenticationUrl = authenticationUrl;
        this.principal = principal;
        this.credential = credential;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        for (int i = 0; i < callbacks.length; i++)
        {
            if (callbacks[i] instanceof NameCallback)
            {
                ((NameCallback) callbacks[i]).setName(principal);
            }
            else if (callbacks[i] instanceof PasswordCallback)
            {
                ((PasswordCallback) callbacks[i]).setPassword(credential.toCharArray());
            }
            else if (callbacks[i] instanceof URLCallback)
            {
                ((URLCallback) callbacks[i]).setURL(authenticationUrl);
            }
            else
            {
                throw new UnsupportedCallbackException(callbacks[i]);
            }
        }
    }
}