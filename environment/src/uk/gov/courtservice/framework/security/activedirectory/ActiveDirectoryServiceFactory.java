package uk.gov.courtservice.framework.security.activedirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * The active directory service factory. Hashing config would be expensive and
 * normally their will only be one service. It therefore makes sense to use a
 * list and iterate over it.
 * 
 * @author Will Fardell, Xdevelopment 2005
 */
public class ActiveDirectoryServiceFactory {
    private static final Logger log = Logger.getLogger(ActiveDirectoryServiceImpl.class);

    private static final List serviceMappingList = new ArrayList();

    private ActiveDirectoryServiceFactory() {
        // Reduce Access, Stop Construction!
    }

    /**
     * Get an active directory service that matches the config.
     * 
     * @param config
     *            the config to find the service for
     */
    public static synchronized ActiveDirectoryService getActiveDirectoryService(ActiveDirectoryServiceConfig config) {
        // Check Arguments
        if (config == null) {
            throw new IllegalArgumentException("config: null");
        }

        // Look for an existing service for the config.
        for (int i = 0, s = serviceMappingList.size(); i < s; i++) {
            ActiveDirectoryServiceMapping mapping = (ActiveDirectoryServiceMapping) serviceMappingList.get(i);
            if (config.equals(mapping.config)) {
                return mapping.service;
            }
        }

        // Create a service and store service for future access.
        ActiveDirectoryService service = createActiveDirectoryService();
        service.initialize(config);
        serviceMappingList.add(new ActiveDirectoryServiceMapping(config, service));
        return service;
    }

    /*
     * Service locator
     */
    private static ActiveDirectoryService createActiveDirectoryService() {
        String className = getActiveDirectoryServiceClassName();

        if (className != null) {
            try {
                return (ActiveDirectoryService) Class.forName(className).newInstance();
            } catch (InstantiationException e) {
                log.warn("Could not create ActiveDirectoryService " + className + ".", e);
            } catch (IllegalAccessException e) {
                log.warn("Could not create ActiveDirectoryService " + className + ".", e);
            } catch (ClassNotFoundException e) {
                log.warn("Could not create ActiveDirectoryService " + className + ".", e);
            }
        }

        return new ActiveDirectoryServiceImpl();
    }

    private static String getActiveDirectoryServiceClassName() {
        String className = getActiveDirectoryServiceClassNameProperty();
        if (className != null) {
            return className;
        }
        className = getActiveDirectoryServiceClassNameIdentifier();
        if (className != null) {
            return className;
        }
        return null;
    }

    private static String getActiveDirectoryServiceClassNameProperty() {
        String property = System
                .getProperty("uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryService");
        if (property != null) {
            String trimed = property.trim();
            if (trimed.length() > 0) {
                return trimed;
            }
        }
        return null;
    }

    private static String getActiveDirectoryServiceClassNameIdentifier() {
        URL url = ActiveDirectoryServiceFactory.class.getClassLoader().getResource(
                "META-INF/services/uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryService");
        if (url != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                try {
                    String line = reader.readLine();
                    if (line != null) {
                        String trimed = line.trim();
                        if (trimed.length() > 0) {
                            return trimed;
                        }
                    }
                } finally {
                    closeSilently(reader);
                }
            } catch (IOException ioe) {
                log.warn("An error occured reading resource " + url + ".", ioe);
            }
        }
        return null;
    }

    private static void closeSilently(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioe) {
            log.warn("An error occured closing the character stream.", ioe);
        }
    }

    /*
     * Hold mappings from configs to services.
     */
    private static class ActiveDirectoryServiceMapping {
        public final ActiveDirectoryServiceConfig config;

        public final ActiveDirectoryService service;

        public ActiveDirectoryServiceMapping(ActiveDirectoryServiceConfig config, ActiveDirectoryService service) {
            this.config = config;
            this.service = service;
        }
    }

}
