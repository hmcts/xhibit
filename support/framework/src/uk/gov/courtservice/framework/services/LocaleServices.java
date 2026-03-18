package uk.gov.courtservice.framework.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;

/**
 * <p>
 * Title: Locale Services
 * </p>
 * <p>
 * Description: Prvoides services for localising URL and resource streams
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell (Xdevelopment 2003)
 * @version 1.0
 */

public class LocaleServices {

    /**
     * logger
     */

    protected Logger log = Logger.getLogger(getClass());

    /**
     * Lookup error key
     */

    private static final String LOOKUP_RESOURCE_ERROR_KEY = "localeservices.resource.lookuperror";

    private static final String LOOKUP_URL_ERROR_KEY = "localeservices.url.lookuperror";

    /**
     * The file extension seperator
     */
    private static final String FILE_EXTENSION_SEPERATOR = ".";

    /**
     * The singleton instance
     */
    private static final LocaleServices instance = new LocaleServices();

    /**
     * Get the singleton
     * 
     * @return the ResourceServices singleton instance
     */
    public static LocaleServices getInstance() {
        return instance;
    }

    /**
     * Stop external construction of the singleton
     */
    private LocaleServices() {
    }

    /**
     * Get the url for the resource using the given locale for name resolution
     * see getCandidates, this is a localised version of
     * ClassLoader.getResource(String).
     * 
     * @param locale,
     *            the required locale
     * @param name
     *            the name to find
     * @throws CSUnrecoverableException
     *             if the URLStream can not be opened
     */
    public String getResource(Locale locale, String name) throws CSUnrecoverableException {
        // we only handle absolute resources so / (absolute to indicate root in
        // Class.getResource) prefix needs to be removed
        Iterator candidates = getCandidates(locale, name.startsWith("/") ? name.substring(1) : name);
        if (log.isDebugEnabled()) {
            log.debug("Getting resource for " + name + " in locale " + locale);
        }
        while (candidates.hasNext()) {
            URL url = LocaleServices.class.getClassLoader().getResource((String) candidates.next());
            if (url != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Resource url: " + url.toString());
                }
                return url.toString();
            }
        }
        throw new CSUnrecoverableException(new Message(LOOKUP_RESOURCE_ERROR_KEY, new Object[] { name, locale }),
                "Could not open resource \"" + name + "\" in locale \"" + locale + "\"");
    }

    /**
     * Opens the stream for the url in the given locale for name resolution see
     * getCandidates, this is a localised version of URL.openStream()
     * 
     * @param locale,
     *            the required locale
     * @param url
     *            the url to open
     * @throws CSUnrecoverableException
     *             if the URLStream can not be opened
     */
    public InputStream openStream(Locale locale, String url) throws CSUnrecoverableException {
        Iterator candidates = getCandidates(locale, url);
        String candidate = null;
        if (log.isDebugEnabled()) {
            log.debug("Opening stream for " + url + " in locale " + locale);
        }
        while (candidates.hasNext()) {
            try {

                candidate = (String) candidates.next();
                if (log.isDebugEnabled()) {
                    log.debug("Opening stream for candidate: " + candidate);
                }
                return new URL(candidate).openStream();
                // return new URL((String) candidates.next()).openStream();
            } catch (MalformedURLException mue) {
                continue; // Could not open stream try next
            } catch (IOException ioe) {
                continue; // Could not open stream try next
            }
        }
        throw new CSUnrecoverableException(new Message(LOOKUP_URL_ERROR_KEY, new Object[] { url, locale }),
                "Could not open url \"" + url + "\" in locale \"" + locale + "\"");
    }

    /**
     * Get the base name, strip any localise info from the name, this requires
     * the locale as otherwise not possible to determine reliably what
     * localisation was added.
     * 
     * @param locale
     *            the locale the name was localised with
     * @param name
     *            the name
     * @return the base name
     */
    public String getBaseName(Locale locale, String name) {
        int extensionIndex = name.lastIndexOf(FILE_EXTENSION_SEPERATOR);
        if (extensionIndex == -1) {
            return getBaseName(locale, name, "");
        } else {
            return getBaseName(locale, name.substring(0, extensionIndex), name.substring(extensionIndex));
        }
    }

    private String getBaseName(Locale locale, String base, String extension) {
        Iterator localisations = getLocalisations(locale);
        while (localisations.hasNext()) {
            String localisation = (String) localisations.next();
            if (base.endsWith(localisation)) {
                return base.substring(0, base.length() - localisation.length()) + extension;
            }
        }
        return base + extension;
    }

    /**
     * Get a list of possible names given the locale and name <p/>
     * <p>
     * The LAST file extension (if any) is seperated from the base, the
     * extension is the part after and including the file extension seperator
     * '.' <p/>
     * <p>
     * The resource bundle lookup searches for classes with various paterns on
     * the basis of (1) the desired locale and (2) the current default locale as
     * returned by Locale.getDefault(), and (3) the root resource bundle (base),
     * in the following order from lower-level (more specific) to parent-level
     * (less specific):
     * <ul>
     * <li> base + "_" + language1 + "_" + country1 + "_" + variant1 + extension
     * <li> base + "_" + language1 + "_" + country1 + extension
     * <li> base + "_" + language1 + extension
     * <li> base + "_" + language2 + "_" + country2 + "_" + variant2 + extension
     * <li> base + "_" + language2 + "_" + country2 + extension
     * <li> base + "_" + language2 + extension
     * <li> base + extension
     * </ul>
     * 
     * @param locale
     *            the locale to use (the desired locale)
     * @param name
     *            the name to get candidates for
     * @return the list of names
     */

    public Iterator getCandidates(Locale locale, String name) {
        int extensionIndex = name.lastIndexOf(FILE_EXTENSION_SEPERATOR);
        if (extensionIndex == -1) {
            return getCandidates(locale, name, "");
        } else {
            return getCandidates(locale, name.substring(0, extensionIndex), name.substring(extensionIndex));
        }
    }

    //
    // This has been written to return an Iterator so it can be optimised
    // later if required
    // the construction of the candidates should be delayed until the next()
    // call on the
    // iterator!
    //
    private Iterator getCandidates(Locale locale, String base, String extension) {
        List candidateList = new ArrayList();
        Iterator localisations = getLocalisations(locale);
        while (localisations.hasNext()) {
            candidateList.add(base + (String) localisations.next() + extension);
        }
        return candidateList.iterator();
    }

    //
    // This has been written to return an Iterator so it can be optimised
    // later if required
    // the construction of the localisations should be delayed until the
    // next() call
    // on the iterator!
    //
    private Iterator getLocalisations(Locale locale) {
        List candidateExtensionsList = new ArrayList();

        // Specific

        String language01 = locale.getLanguage();
        String country01 = locale.getCountry();
        String variant01 = locale.getVariant();

        if (!language01.equals("")) {
            if (country01 != "") {
                if (variant01 != "") {
                    candidateExtensionsList.add("_" + language01 + "_" + country01 + "_" + variant01);
                }
                candidateExtensionsList.add("_" + language01 + "_" + country01);
            }
            candidateExtensionsList.add("_" + language01);
        }

        // Default

        Locale def = Locale.getDefault();

        String language02 = def.getLanguage();
        String country02 = def.getCountry();
        String variant02 = def.getVariant();

        if (language02 != "") {
            if (country02 != "") {
                if (variant02 != "") {
                    candidateExtensionsList.add("_" + language02 + "_" + country02 + "_" + variant02);
                }
                candidateExtensionsList.add("_" + language02 + "_" + country02);
            }
            candidateExtensionsList.add("_" + language02);
        }

        // Base

        candidateExtensionsList.add("");

        return candidateExtensionsList.iterator();
    }

}
