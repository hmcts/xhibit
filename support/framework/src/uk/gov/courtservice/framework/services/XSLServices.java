package uk.gov.courtservice.framework.services;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.xml.serializer.DOMSerializer;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.w3c.dom.Document;
import org.xml.sax.XMLFilter;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.xsl.LocaleURIResolver;
import uk.gov.courtservice.framework.xml.transform.ParameterTemplatesAdapter;
import uk.gov.courtservice.framework.xml.transform.URIResolverTemplatesAdapter;

/**
 * <p>
 * Title: XSL Services
 * </p>
 * <p>
 * Description: Insulates the application components from knowedge of XSL.
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
public class XSLServices {
    /**
     * Logger
     */
    private static final Logger log = CSServices.getLogger(XSLServices.class);

    /**
     * The singleton instance
     */
    private static final XSLServices instance = new XSLServices();

    /**
     * Templates cache this map contains maps indexed by systemId, the indexed
     * maps contain the Templates indexed by resolvers.
     */
    private final Map templatesCache = new HashMap();

    /**
     * TransformerFactory cache this map contains TransformerFactory's indexed
     * by resolvers
     */
    private final Map transformerFactoryCache = new HashMap();

    /**
     * Get the singleton
     * 
     * @return the ResourceServices singleton instance
     */
    public static final XSLServices getInstance() {
        return instance;
    }

    /**
     * Transform the xml using the xsl, if specified use the locale and or
     * parameter map.
     * 
     * @param xml
     *            the xml transform
     * @param xslName
     *            the name of a resource containing the xsl style sheet
     * @param locale
     *            optional, locale to resolve the xsl
     * @param parameterMap
     *            optional, map of transformer parameterMap
     * @return the transformed xml
     * @throws IllegalArgumentException
     *             if either parameter is null
     * @throws CSUnrecoverableException
     *             if an error occures with the transform
     */
    public String transform(String xml, String xslName, Locale locale, Map parameterMap)
            throws IllegalArgumentException, CSUnrecoverableException {
        if (xml == null) {
            throw new IllegalArgumentException("xml: null");
        }
        if (xslName == null) {
            throw new IllegalArgumentException("xslName: null");
        }

        if (log.isDebugEnabled()) {
            log.debug("Source: " + xml);
        }
        return transform(new StreamSource(new StringReader(xml)), getSystemId(xslName, locale), locale, parameterMap);
    }

    /**
     * Transform the xml using the xsl, if specified use the locale and or
     * parameter map.
     * 
     * @param document
     *            the xml transform
     * @param xslName
     *            the name of a resource containing the xsl style sheet
     * @param locale
     *            optional, the locale to resolve the xsl
     * @param parameterMap
     *            optional map of transformer parameterMap
     * @return the transformed xml
     * @throws CSUnrecoverableException
     *             if an error occures
     */
    public String transform(Document document, String xslName, Locale locale, Map parameterMap)
            throws CSUnrecoverableException {
        if (document == null) {
            throw new IllegalArgumentException("document: null");
        }
        if (xslName == null) {
            throw new IllegalArgumentException("xslName: null");
        }

        if (log.isDebugEnabled()) {
            log.debug("Source: " + documentToString(document));
        }
        return transform(new DOMSource(document), getSystemId(xslName, locale), locale, parameterMap);
    }

    /**
     * Transform the xml using the xsl for the specified locale
     * 
     * @param source
     *            the xml transform
     * @param systemId
     *            the system Id of a resource containing the xsl style sheet
     * @param locale
     *            optional, the locale to resolve the xsl
     * @param parameterMap
     *            optional map of transformer parameterMap
     * @return the transformed xml
     * @throws CSUnrecoverableException
     *             if an error occures
     * @throws ClassCastException
     *             if parameter map not keyed by strings
     */
    protected String transform(Source source, String systemId, Locale locale, Map parameterMap)
            throws ClassCastException, CSUnrecoverableException {
        if (log.isDebugEnabled()) {
            log.debug("Transform: systemId: " + systemId + " locale: " + locale + " parameters: "
                    + parameterMapToString(parameterMap));
            String xml = _transform(source, systemId, locale, parameterMap);
            log.debug("Result: " + xml);
            return xml;
        }
        return _transform(source, systemId, locale, parameterMap);
    }

    private String _transform(Source source, String systemId, Locale locale, Map parameterMap)
            throws ClassCastException, CSUnrecoverableException {
        URIResolver resolver = getResolver(locale);
        Transformer transformer = getTransformer(systemId, resolver, parameterMap);

        Writer writerResult = new StringWriter();
        try {
            transformer.transform(source, new StreamResult(writerResult));
        } catch (TransformerException te) {
            Message userMessage = new Message("xslservices.transformationerror", new Object[] { source, systemId,
                    resolver });
            String logMessage = "Could not transform XML \"" + source + "\" using transform \"" + systemId
                    + "\" resolved by \"" + resolver + "\"";
            throw new CSUnrecoverableException(userMessage, te, logMessage);
        }
        String transformedXML = writerResult.toString();

        return transformedXML;
    }

    //
    // Pipeline Methods
    //		

    /**
     * Create a transformer for rendering pipelines
     * 
     * @param locale
     *            the locale to use, if null use default
     * @param parameterMap
     *            any parameters that need to be added to the transformer
     * @return the new transformer
     * @throws CSUnrecoverableException
     */
    public Transformer getTransformer(Locale locale, Map parameterMap) throws CSUnrecoverableException {
        try {
            Transformer transformer = getTransformerFactory(getResolver(locale)).newTransformer();
            if (parameterMap != null) {
                Iterator keys = parameterMap.keySet().iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Object value = parameterMap.get(key);
                    if (log.isDebugEnabled()) {
                        log.debug("Changeing parameter " + key + " from " + transformer.getParameter(key) + " to "
                                + value + " for transformer " + transformer + ".");
                    }
                    transformer.setParameter(key, value);
                }
            }
            return transformer;
        } catch (TransformerException te) {
            throw new CSUnrecoverableException(new Message("xslservices.pipelineerror"), te,
                    "Could not create a pipeline transformer.");
        }
    }

    /**
     * Gets the transformer for the xsl name and resolver, private because xsl
     * classes should not be used outside this class.
     * 
     * @param systemId
     *            the name of the xsl
     * @param locale
     *            optional, the locale to use
     * @param parameterMap
     *            optional, additional parameters to use
     * @return the transformer
     */
    public Transformer getTransformer(String xslName, Locale locale, Map paramaterMap) throws CSUnrecoverableException {
        if (xslName == null) {
            throw new IllegalArgumentException("xslName");
        }
        return getTransformer(getSystemId(xslName, locale), getResolver(locale), paramaterMap);
    }

    /**
     * Gets the transformer for the xsl name and resolver, private because xsl
     * classes should not be used outside this class.
     * 
     * @param systemId
     *            the name of the xsl
     * @param resolver
     *            the resolver used to create the transform
     * @return the transformer
     */
    protected Transformer getTransformer(String systemId, URIResolver resolver, Map paramaterMap)
            throws CSUnrecoverableException {
        try {
            return getTemplates(systemId, resolver, paramaterMap).newTransformer();
        } catch (TransformerException te) {
            Message userMessage = new Message("xslservices.transformererror", new Object[] { systemId, resolver });
            String logMessage = "Could not create transformer from template created from xsl \"" + systemId
                    + "\" resolved by \"" + resolver + "\"";

            throw new CSUnrecoverableException(userMessage, te, logMessage);
        }
    }

    /**
     * Get XMLFilter array pipeline for templates array passed in
     * 
     * @param templates
     * @return
     * @throws TransformerException
     */
    public XMLFilter[] getFilters(Templates[] templates) throws TransformerException {
        if (templates == null) {
            throw new IllegalArgumentException("templates: null");
        }

        XMLFilter[] filters = new XMLFilter[templates.length];
        SAXTransformerFactory stf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        for (int i = 0; i < filters.length; i++) {
            filters[i] = stf.newXMLFilter(templates[i]);

        }
        return filters;
    }

    //
    // Templates Cache
    //		

    /**
     * Get an Templates array for String array of xsl names and locale passed in
     * 
     * @param xslNames
     * @param locale
     * @return
     */
    public Templates[] getTemplatesArray(String[] xslNames, Locale locale, Map parameterMap) {
        if (xslNames == null) {
            throw new IllegalArgumentException("xslNames: null");
        }
        URIResolver resolver = getResolver(locale);

        Templates[] templates = new Templates[xslNames.length];
        for (int i = 0; i < templates.length; i++) {
            templates[i] = getTemplates(getSystemId(xslNames[i], locale), resolver, parameterMap);
        }

        return templates;
    }

    /**
     * Gets the templates for the xsl name and resolver, private because xsl
     * classes should not be used outside this class.
     * 
     * @param systemId
     *            the name of the xsl
     * @param resolver
     *            the resolver used to create the transform
     * @param parameterMap
     * 
     * @return the templates
     */
    protected Templates getTemplates(String systemId, URIResolver resolver, Map parameterMap)
            throws CSUnrecoverableException {
        // Cached in a map of maps the first indexed by systemId the second by
        // resolver
        Templates templates;
        synchronized (templatesCache) {
            Map resolverCache = (Map) templatesCache.get(systemId);
            if (resolverCache != null) {
                templates = (Templates) resolverCache.get(resolver);
                if (templates == null) {
                    templates = createTemplates(systemId, resolver);
                    resolverCache.put(resolver, templates);
                }
            } else {
                resolverCache = new HashMap();
                templatesCache.put(systemId, resolverCache);
                templates = createTemplates(systemId, resolver);
                resolverCache.put(resolver, templates);
            }
        }

        // If parameter map not equal to null decorate templates
        if (parameterMap != null) {
            return new ParameterTemplatesAdapter(templates, parameterMap);
        } else {
            return templates;
        }
    }

    /**
     * Create the templates for the xsl name and resolver, private because xsl
     * classes should not be used outside this class.
     * 
     * @param systemId
     *            the name of the xsl
     * @param resolver
     *            the resolver used to create the transform
     * @return the templates for the xsl using the given resolver
     * @throws CSUnrecoverableException
     *             if an error occures creating the transform
     */
    protected Templates createTemplates(String systemId, final URIResolver resolver) throws CSUnrecoverableException {
        try {
            return new URIResolverTemplatesAdapter(getTransformerFactory(resolver).newTemplates(
                    resolver.resolve(systemId, null)), resolver);
        } catch (TransformerException te) {
            Message userMessage = new Message("xslservices.templateserror", new Object[] { systemId, resolver });
            String logMessage = "Could not create templates from xsl \"" + systemId + "\" resolved by \"" + resolver
                    + "\"";
            throw new CSUnrecoverableException(userMessage, te, logMessage);
        }
    }

    //
    // TransformerFactory Cache
    //		

    /**
     * Get the transformer factory for the resolver, private because xsl classes
     * should not be used outside this class.
     * 
     * @param resolver
     *            the resolver used to by the transform factory to create
     *            templates
     * @return the transformer factory for the resolver
     * @throws CSUnrecoverableException
     *             if an error occures creating the transform
     */
    protected TransformerFactory getTransformerFactory(URIResolver resolver) throws CSUnrecoverableException {
        TransformerFactory factory = (TransformerFactory) transformerFactoryCache.get(resolver);
        if (factory == null) {
            factory = createTransformerFactory(resolver);
            transformerFactoryCache.put(resolver, factory);
        }
        return factory;
    }

    //
    // Utilities
    //		

    /**
     * This is more complex than desirable, we need to take the resource name
     * and resolve it in a localised way to get the actual URL of the resource
     * inside the jar, we then need to take this url and strip out any
     * localisation that was added so when it gets passed into the
     * LocalisedURIResolver it behaves properly, the reason we cant just create
     * a stream from the resource we find is we need the URI for the system id
     * to work out any relative uri's
     * 
     * @param locale
     * @return the uri of the resource name
     */
    private static String getSystemId(String xslName, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return CSServices.getLocaleServices().getBaseName(locale,
                LocaleServices.getInstance().getResource(locale, xslName));
    }

    /**
     * Get the URI resolver for the locale
     * 
     * @param locale,
     *            if null use default
     * @return the resolver
     */
    protected static final URIResolver getResolver(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return LocaleURIResolver.getResolver(locale);
    }

    /**
     * Get the transformer factory for the resolver, private because xsl classes
     * should not be used outside this class.
     * 
     * @param resolver
     *            the resolver used by the transform factory
     * @return the transformer factory for the resolver
     * @throws CSUnrecoverableException
     *             if an error occures creating the transform
     */
    private static TransformerFactory createTransformerFactory(URIResolver resolver) throws CSUnrecoverableException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(resolver);
        return transformerFactory;
    }

    /**
     * Format parameter map for debug
     * 
     * @param parameterMap
     * @return a string containing formatted parameter map
     */
    protected static final String parameterMapToString(Map parameterMap) {
        if (parameterMap == null) {
            return "null";
        } else {
            Iterator parameterNames = parameterMap.keySet().iterator();
            if (parameterNames.hasNext()) {
                StringBuffer buffer = new StringBuffer();
                String parameterName = (String) parameterNames.next();
                buffer.append(parameterName);
                buffer.append(": ");
                buffer.append(parameterMap.get(parameterName));
                while (parameterNames.hasNext()) {
                    buffer.append(", ");
                    parameterName = (String) parameterNames.next();
                    buffer.append(parameterName);
                    buffer.append(": ");
                    buffer.append(parameterMap.get(parameterName));
                }
                return buffer.toString();
            } else {
                return "none";
            }
        }
    }

    /**
     * Convert a document to a String (this is used for debug output)
     * 
     * @param document
     *            the docuemnt to convert
     * @return a String containing the xml
     */
    private static final String documentToString(Document document) {
        try {
            StringWriter buffer = new StringWriter();

            Serializer serializer = SerializerFactory.getSerializer(OutputPropertiesFactory
                    .getDefaultMethodProperties("XML"));
            serializer.setWriter(buffer);
            DOMSerializer domSerializer = serializer.asDOMSerializer();
            domSerializer.serialize(document);

            return buffer.toString();
        } catch (IOException ioe) {
            return "Could not serialize Document: " + ioe;
        }
    }
}
