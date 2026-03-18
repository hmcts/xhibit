package uk.gov.courtservice.framework.services.xsl;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.xml.utils.URI;
import org.apache.xml.utils.URI.MalformedURIException;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Locale URI Resover
 * </p>
 * <p>
 * Description: Resolves names for a given locale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell (Xdevelopment 2003)
 * @version 1.0
 */
public class LocaleURIResolver implements URIResolver {
    private static Logger log = CSServices.getLogger(LocaleURIResolver.class);

    /**
     * Resolver cache
     */
    private static final Map cache = new HashMap();

    /**
     * Get a resolver for a given locale
     */
    public static synchronized LocaleURIResolver getResolver(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("locale: null");
        }

        LocaleURIResolver resolver = (LocaleURIResolver) cache.get(locale);
        if (resolver == null) {
            resolver = new LocaleURIResolver(locale);
            cache.put(locale, resolver);
        }
        return resolver;
    }

    /**
     * The locale
     */
    private Locale locale;

    /**
     * Construct a URI resolver for the given Locale
     * 
     * @throws IllegalArgumentException
     *             if the locale is null
     */
    private LocaleURIResolver(Locale locale) throws IllegalArgumentException {
        if (locale == null) {
            throw new IllegalArgumentException("locale: " + locale);
        }
        this.locale = locale;
    }

    /**
     * Returns the locale
     * 
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * URIResolver Implementation, resolve the name to the source
     * 
     * @param href
     *            An href attribute, which may be relative or absolute.
     * @param base
     *            The base URI in effect when the href attribute was
     *            encountered.
     * @return A Source object, or null if the href cannot be resolved, and the
     *         processor should try to resolve the URI itself.
     * @throws TransformerException -
     *             if an error occurs when trying to resolve the URI.
     */
    public Source resolve(String href, String base) throws TransformerException {
        try {
            String uri = resolveName(href, base);
            StreamSource source = new StreamSource(CSServices.getLocaleServices().openStream(locale, uri));
            // System id needed for subsequent name resolutions
            source.setSystemId(uri);
            return source;
        } catch (CSUnrecoverableException csue) {
            log.error("An error occured resolving href \"" + href + "\" from base \"" + base + "\".", csue);
            return null;
        }
    }

    /**
     * Resolve the name if it is relative against the given base, this is an
     * interface into some code stripped out of XALAN (did not want the
     * dependency)
     * 
     * @return the resolved base
     */
    public static String resolveName(String urlString, String base) throws TransformerException {
        String resolvedUrl = base == null ? urlString : getAbsoluteURI(urlString, base);
        log.debug("Resolved url \"" + urlString + "\"" + " to \"" + resolvedUrl + "\" using base \"" + base + "\".");
        return resolvedUrl;
    }

    /**
     * Returns a <code>String</code> representation of this object
     * 
     * @return a <code>String</code> representation of this object
     */
    public String toString() {
        return locale.toString();
    }

    //
    // START OF BLOCK COPIED FROM XALAN
    // org.apache.xml.utils.SystemIDResolver
    //

    /**
     * Get absolute URI from a given relative URI. <p/>
     * <p>
     * The URI is resolved relative to the system property "user.dir" if it is
     * available; if not (i.e. in an Applet perhaps which throws
     * SecurityException) then it is currently resolved relative to "" or a
     * blank string. Also replaces all backslashes with forward slashes.
     * </p>
     * 
     * @param uri
     *            Relative URI to resolve
     * @return Resolved absolute URI or the input relative URI if it could not
     *         be resolved.
     */
    public static String getAbsoluteURIFromRelative(String uri) {

        String curdir = "";
        try {
            curdir = System.getProperty("user.dir");
        } catch (SecurityException se) {
            log.fatal(se);
        }// user.dir not accessible from applet

        if (null != curdir) {
            String base;
            if (curdir.startsWith(File.separator))
                base = "file://" + curdir;
            else
                base = "file:///" + curdir;

            if (uri != null)
                // Note: this should arguably stick in a '/' forward
                // slash character instead of the file separator,
                // since we're effectively assuming it's a hierarchical
                // URI and adding in the abs_path separator -sc
                uri = base + System.getProperty("file.separator") + uri;
            else
                uri = base + System.getProperty("file.separator");
        }

        if (null != uri && (uri.indexOf('\\') > -1))
            uri = uri.replace('\\', '/');

        return uri;
    }

    //
    // CUT - public static String getAbsoluteURI(String url) As not required
    //

    /**
     * Take a SystemID string and try and turn it into a good absolute URL.
     * 
     * @param urlString
     *            SystemID string
     * @param base
     *            Base URI to use to resolve the given systemID
     * @return The resolved absolute URI
     * @throws TransformerException
     *             thrown if the string can't be turned into a URL.
     */
    public static String getAbsoluteURI(String urlString, String base) throws TransformerException {
        boolean isAbsouteUrl = false;
        boolean needToResolve = false;

        // Start of change to resolve absolute urlString against a jar url.
        // An example illistrates this well
        // urlString: "/config/xml/PrintCourtLogEvents.xsl"
        // base:
        // "jar:file:/D:/projects/XHIBIT/thickclient/dist/lib/common/Config.jar!/config/xml/courtlogprint.xsl"
        // resolved:
        // "jar:file:/D:/projects/XHIBIT/thickclient/dist/lib/common/Config.jar!/config/xml/PrintCourtLogEvents.xsl"
        if (urlString.startsWith("/") && (base.startsWith("jar") || base.startsWith("zip"))) {
            urlString = base.substring(0, base.indexOf("!") + 1) + urlString;
            isAbsouteUrl = true;
        }
        // end of change

        // Bugzilla#5701: the below if is incorrect, if you read
        // section 5.2 of RFC 2396. If the url did start with file:,
        // it implies we should assume it's absolute and be done
        // with resolving. Note that I'm not even sure why we put
        // in the second check for '/' anyways -sc
        // if(urlString.startsWith("file:") && urlString.charAt(5) != '/')
        // {
        // needToResolve = true;
        // }
        // else if (urlString.indexOf(':') > 0)
        // Bugzilla#5701 comment out code end
        if (urlString.indexOf(':') > 0) {
            // If there is a colon to separate the scheme from the rest,
            // it should be an absolute URL
            isAbsouteUrl = true;
        } else if (urlString.startsWith(File.separator)) {
            // If the url starts with a path separator, we assume it's
            // a reference to a file: scheme (why do we do this? -sc)
            urlString = "file://" + urlString;
            isAbsouteUrl = true;
        }

        if ((!isAbsouteUrl) && ((null == base) || (base.indexOf(':') < 0))) {
            if (base != null && base.startsWith(File.separator))
                base = "file://" + base;
            else
                base = getAbsoluteURIFromRelative(base);
        }

        // bit of a hack here. Need to talk to URI person to see if this can
        // be fixed.
        if ((null != base) && needToResolve) {
            if (base.equals(urlString)) {
                base = "";
            } else {
                urlString = urlString.substring(5);
                isAbsouteUrl = false;
            }
        }

        // This is probably a bad idea, we should at least check for
        // quotes...
        if (null != base && (base.indexOf('\\') > -1))
            base = base.replace('\\', '/');

        if (null != urlString && (urlString.indexOf('\\') > -1))
            urlString = urlString.replace('\\', '/');

        URI uri;

        try {
            if ((null == base) || (base.length() == 0) || (isAbsouteUrl)) {
                uri = new URI(urlString);
            } else {
                URI baseURI = new URI(base);

                uri = new URI(baseURI, urlString);
            }
        } catch (MalformedURIException mue) {
            throw new TransformerException(mue);
        }

        String uriStr = uri.toString();

        // Not so sure if this is good. But, for now, I'll try it. We really
        // must
        // make sure the return from this function is a URL!
        if ((Character.isLetter(uriStr.charAt(0)) && (uriStr.charAt(1) == ':') && (uriStr.charAt(2) == '/') && (uriStr
                .length() == 3 || uriStr.charAt(3) != '/'))
                || ((uriStr.charAt(0) == '/') && (uriStr.length() == 1 || uriStr.charAt(1) != '/'))) {
            uriStr = "file:///" + uriStr;
        }
        return uriStr;
    }

    //
    // END OF BLOCK COPIED FROM XALAN org.apache.xml.utils.SystemIDResolver
    //

}