package uk.gov.courtservice.framework.security.providers.authorization;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Factory class to create a <code>Context</code> to connect to the default
 * server. The properties are loaded from the command line if present, or values
 * are defaulted to those located in the application.properties file.
 * 
 * @author tz0d5m
 * @version $Id: ContextFactory.java,v 1.3 2006/06/05 12:30:04 bzjrnl Exp $
 */
public final class ContextFactory {
    /** The log4j <code>Logger</code> instance */
    private static final Logger log = Logger.getLogger(ContextFactory.class);

    /** Key used for the default provider URL required to create context */
    private static final String PROVIDER_URL_KEY = "default.PROVIDER_URL";

    /**
     * Key used for the default initial context factory required to create
     * context
     */
    private static final String CONTEXT_FACTORY_KEY = "default.INITIAL_CONTEXT_FACTORY";

    /** The environment used to create a context */
    private static final Hashtable environment;

    static {
        log.debug("static() - start of static initialisation block");

        try {
            final Properties props = new Properties();
            props.load(ContextFactory.class.getClassLoader().getResourceAsStream("application.properties"));

            // acquire the default values from the properties file...
            final String providerUrlDefault = props.getProperty(PROVIDER_URL_KEY);
            final String ctxFactoryDefault = props.getProperty(CONTEXT_FACTORY_KEY);

            // acquire the values, if overridden on the command line use
            // that,
            // else use the default from the properties file...
            final String providerUrl = System.getProperty(PROVIDER_URL_KEY, providerUrlDefault);
            final String ctxFactory = System.getProperty(CONTEXT_FACTORY_KEY, ctxFactoryDefault);

            if (log.isDebugEnabled()) {
                log.debug("static() - providerUrl: " + "default=\"" + providerUrlDefault + "\"" + "; actual=\""
                        + providerUrl + "\"");
                log.debug("static() - ctxFactory: " + "default=\"" + ctxFactoryDefault + "\"" + "; actual=\""
                        + ctxFactory + "\"");
            }

            // construct the environment using the properties acquired...
            environment = new Hashtable();
            environment.put(Context.INITIAL_CONTEXT_FACTORY, ctxFactory);
            environment.put(Context.PROVIDER_URL, providerUrl);
        } catch (final IOException e) {
            throw new NestedException(e);
        }

        log.debug("static() - end of static initialisation block");
    }

    /**
     * Acquire the <code>Context</code> used to connect to the default server.
     * 
     * @return A new <code>Context</code>.
     */
    public static Context getContext() {
        try {
            return new InitialContext(environment);
        } catch (final NamingException e) {
            throw new NestedException(e);
        }
    }
}
