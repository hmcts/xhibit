package uk.gov.courtservice.framework.security;

import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ConfigServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This class provides utility methods to get information on
 * </p>
 * <p>
 * servers to which a client can connect.
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

public class ServerRepository {

    /**
     * This is the config key that stores the server list
     */
    public static String SERVERS_KEY = "servers";

    /**
     * This is the config key for the default server
     */
    public static String DEFAULT_SERVER_KEY = "defaultServer";

    /**
     * The name of the default server
     */
    public static final String DFEUALT_SERVER;

    /**
     * A hashmap of servers
     */
    private static HashMap serverRepository;

    /**
     * Static initializer initializes the server repository
     */
    static {
        if (System.getProperty(DEFAULT_SERVER_KEY) != null)
            DFEUALT_SERVER = System.getProperty(DEFAULT_SERVER_KEY);
        else
            DFEUALT_SERVER = CSServices.getConfigServices().getProperty(DEFAULT_SERVER_KEY);

        serverRepository = new HashMap();

        // Get the list of servers
        ConfigServices config = CSServices.getConfigServices();
        String servers = config.getProperty(SERVERS_KEY);

        StringTokenizer tok = new StringTokenizer(servers);

        // Add the server information in the repository
        while (tok.hasMoreTokens()) {
            String serverName = tok.nextToken();
            serverRepository.put(serverName, new Server(serverName));
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ServerRepository() {
    }

    /**
     * This method returns the list of servers
     * 
     * @return
     */
    public static Set getServers() {
        return serverRepository.keySet();
    }

    /**
     * This method returns the authentication URL for the named server
     * 
     * @param serverName
     * @return
     */
    public static String getAuthenticationURL(String serverName) {

        Server server = (Server) serverRepository.get(serverName);
        if (server == null)
            throw new IllegalArgumentException(serverName + " server not found");

        return server.getAuthenticationURL();

    }

    /**
     * This method returns the provider URL for the named server
     * 
     * @param serverName
     * @return
     */
    public static String getProviderURL(String serverName) {

        Server server = (Server) serverRepository.get(serverName);
        if (server == null)
            throw new IllegalArgumentException(serverName + " server not found");

        return server.getProviderURL();

    }

    /**
     * This method returns the initial context factory for the named server
     * 
     * @param serverName
     * @return
     */
    public static String getInitialContextFactory(String serverName) {

        Server server = (Server) serverRepository.get(serverName);
        if (server == null)
            throw new IllegalArgumentException(serverName + " server not found");

        return server.getInitialContextFactory();

    }

    /**
     * The private inner class represents a server
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
    private static class Server {

        /**
         * The authentication URL for the server
         */
        private String authenticationURL;

        /**
         * The provider URL for the server
         */
        private String providerURL;

        /**
         * The initial context factory for the server
         */
        private String initialContextFactory;

        /**
         * Accepts the server name and initializes the authentication and
         * provider URLs and initial context factory
         * 
         * @param serverName
         */
        public Server(String serverName) {

            ConfigServices config = CSServices.getConfigServices();

            // Get the authentication URL
            if (System.getProperty(serverName + ".AUTHENTICATION_URL") != null)
                authenticationURL = System.getProperty(serverName + ".AUTHENTICATION_URL");
            else
                authenticationURL = config.getProperty(serverName + ".AUTHENTICATION_URL");

            // Get the provider URL
            if (System.getProperty(serverName + ".PROVIDER_URL") != null)
                providerURL = System.getProperty(serverName + ".PROVIDER_URL");
            else
                providerURL = config.getProperty(serverName + ".PROVIDER_URL");

            // Get the initial context factory
            if (System.getProperty(serverName + ".INITIAL_CONTEXT_FACTORY") != null)
                initialContextFactory = System.getProperty(serverName + ".INITIAL_CONTEXT_FACTORY");
            else
                initialContextFactory = config.getProperty(serverName + ".INITIAL_CONTEXT_FACTORY");

        }

        /**
         * Returns the authentication URL for the server
         * 
         * @return
         */
        public String getAuthenticationURL() {
            return authenticationURL;
        }

        /**
         * Returns the provider URL for the server
         * 
         * @return
         */
        public String getProviderURL() {
            return providerURL;
        }

        /**
         * Returns the initial context factory for the server
         * 
         * @return
         */
        public String getInitialContextFactory() {
            return initialContextFactory;
        }

    }

}