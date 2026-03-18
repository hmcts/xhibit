package uk.gov.courtservice.framework.services.locator;

import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This is a privileged class for creating initial contextx. I wish Java had
 * method pointers.
 * <p>
 * Title:
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
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class ContextCreator implements PrivilegedExceptionAction {

    /**
     * JNDI environment
     */
    private Hashtable env;

    /**
     * Initializes the JNDI environment
     * 
     * @param env
     */
    public ContextCreator(Hashtable env) {
        this.env = env;
    }

    /**
     * PrivilegedExceptionAction implementation
     * 
     * @return
     * @throws NamingException
     */
    public Object run() throws NamingException {
        return getContext();
    }

    /**
     * Gets the context
     * 
     * @return
     * @throws NamingException
     */
    private InitialContext getContext() throws NamingException {
        return new InitialContext(env);
    }

}
