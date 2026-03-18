package uk.gov.courtservice.framework.security.activedirectory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import netscape.ldap.LDAPv2;

import org.apache.log4j.Logger;

/**
 * The MBean uses a complex and unpredictable proxy mechanism, the purpose of
 * this class is to extract the configuration from the mbean. After this the
 * mbean is no longer required and can be descarded.
 * 
 * @author Will Fardell, This is a wrapper aroung
 */
public class PropertiesActiveDirectoryServiceConfig extends ActiveDirectoryServiceConfig {
    private static final Logger log = Logger.getLogger(PropertiesActiveDirectoryServiceConfig.class);

    /**
     * Construct a new config from the mbean. Note how the mbean is discarded
     * imediatly!
     * 
     * @param mbean
     */
    public PropertiesActiveDirectoryServiceConfig(Properties properties) {
        super(getStringProperty(properties, "host", "10.63.127.14"), getIntProperty(properties, "port", 389),
                getStringProperty(properties, "principal", "weblogic103"),
                getStringProperty(properties, "credential", "password"), 
                getIntProperty(properties, "connectionPoolSize", 6),
                getBooleanProperty(properties, "cacheEnabled", true),
                getIntProperty(properties, "cacheSize", 32),
                getIntProperty(properties, "cacheTTL", 60),
                getBooleanProperty(properties, "followReferrals", true),
                getBooleanProperty(properties, "bindAnonymouslyOnReferrals", false),
                getIntProperty(properties, "resultsTimeLimit", 0),
                getIntProperty(properties, "connectTimeout", 0),
                getIntProperty(properties, "parallelConnectDelay", 0),
                getStringProperty(properties, "userBaseDN", "DC=ops,DC=cs,DC=root"),
                getScopeProperty(properties, "userSearchScope", LDAPv2.SCOPE_SUB),
                getStringProperty(properties, "userFilterExtension", "(objectClass=user)(!(userAccountControl:1.2.840.113556.1.4.803:=2))"),
                getStringProperty(properties, "userPrincipalAttribute", "samAccountName"),
                getStringProperty(properties, "userDisplayAttribute", "displayName"),
                getStringProperty(properties, "userForenameAttribute", "givenName"),
                getStringProperty(properties, "userSurnameAttribute", "sn"),
                getStringProperty(properties, "groupBaseDN", "DC=ops,DC=cs,DC=root"),
                getScopeProperty(properties, "groupSearchScope", LDAPv2.SCOPE_SUB),
                getStringProperty(properties, "groupFilterExtension", "(objectClass=group)"),
                getStringProperty(properties, "groupPrincipalAttribute", "cn"),
                getStringProperty(properties, "groupMemberOfAttribute", "memberOf"),
                getStringProperty(properties, "groupRootPrincipal", "XHIBIT User"));
    }

    /**
     * Get the resource as a config
     * 
     * @param resource
     *            the resource to load
     * @return a config based on the properties in the resource
     * @throws IOException
     *             if an error occures
     */
    public static ActiveDirectoryServiceConfig getResourceAsConfig(String resource) throws IOException {
        return new PropertiesActiveDirectoryServiceConfig(getResourceAsProperties(resource));
    }

    private static Properties getResourceAsProperties(String resource) throws IOException {
        Properties properties = new Properties();
        InputStream in = PropertiesActiveDirectoryServiceConfig.class.getClassLoader().getResourceAsStream(resource);
        if (in != null) {
            try {
                properties.load(in);
            } finally {
                try {
                    in.close();
                } catch (IOException ioe) {
                    log.warn("An error occured closing the stream.", ioe);
                }
            }
        }
        return properties;
    }

    private static boolean getBooleanProperty(Properties properties, String name, boolean def) {
        String property = getStringProperty(properties, name, null);
        if (property != null) {
            return "true".equalsIgnoreCase(property);
        }
        return def;
    }

    private static int getIntProperty(Properties properties, String name, int def) {
        String property = getStringProperty(properties, name, null);
        if (property != null) {
            try {
                return Integer.parseInt(property);
            } catch (NumberFormatException nfe) {
                // return def
            }
        }
        return def;
    }

    private static int getScopeProperty(Properties properties, String name, int def) {
        String property = getStringProperty(properties, name, null);
        if (property != null) {
            return parseScope(property);
        }
        return def;
    }

    private static String getStringProperty(Properties properties, String name, String def) {
        String property = properties.getProperty(name);
        if (property != null) {
            String trimed = property.trim();
            if (trimed.length() > 0) {
                return trimed;
            }
        }
        return def;
    }

}
