package uk.gov.courtservice.framework.services.config;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: ENCManager
 * </p>
 * <p/> Description: This class can be used for locally caching a component's
 * ENV (Environment Naming Context). Currenly this class only supports objects
 * bound to the root context (java:comp/env). However, this can be easily
 * extended to support sub contexts like jdbc, ejb, url, mail, jms etc
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class EncHelper {

    // Name of the ENC root context
    private static final String COMP_ENV = "java:comp/env";

    // Map used for locally caching the entries
    private Map envMap = new HashMap();

    // Name used to list the bindings in the root of a given context
    private String ROOT = "";

    // Name by which the default filter class is stored in config
    private String ENC_FILTER_CLASS_KEY = "encFilter.class";

    // Default filter class when not found in config
    private String DEF_ENC_FILTER_CLASS = "uk.gov.courtservice.framework.services.config.DefaultNameFilter";

    /**
     * Constructor initialises the cache
     * 
     * @param Filter
     *            for cached names
     */
    public EncHelper(NameFilter filter) {

        // If filter is null, get the default filter
        if (filter == null)
            filter = getDefaultFilter();

        Context initialContext = null;

        try {

            // Create the initial context
            initialContext = new InitialContext();
            // Lookup the ENC root context
            Context envCtx = (Context) initialContext.lookup(COMP_ENV);

            // Get the bindings in the initial context
            NamingEnumeration enumeration = envCtx.listBindings(ROOT);

            // Cache the bindings
            while (enumeration.hasMore()) {
                Binding binding = (Binding) enumeration.next();
                if (!filter.accept(binding.getName()))
                    continue;
                envMap.put(binding.getName(), binding.getObject());
            }

        } catch (NamingException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (initialContext != null)
                    initialContext.close();
            } catch (NamingException ignore) {
                ignore.printStackTrace();
            }
        }

    }

    /**
     * Constructor initialises the cache and uses default filter
     */
    public EncHelper() {
        this(null);
    }

    /**
     * Gets an object from the ENC root
     * 
     * @param Name
     *            by which the object is bound
     * @return
     */
    public Object getEnvObject(String name) {

        if (name == null)
            throw new IllegalArgumentException(name);

        // Check if the name exists in the cache
        if (!envMap.containsKey(name))
            throw new CSConfigurationException(name + "not found");

        return envMap.get(name);

    }

    /**
     * Gets the default filter to use
     * 
     * @return Filter
     */
    private NameFilter getDefaultFilter() {

        // Get the filter class from configuration
        String encFilterClass = CSServices.getConfigServices().getProperty(ENC_FILTER_CLASS_KEY);

        // If not defined, use the default filter class
        if (encFilterClass == null)
            encFilterClass = DEF_ENC_FILTER_CLASS;

        try {
            return (NameFilter) Class.forName(encFilterClass).newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

}