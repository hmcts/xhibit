package uk.gov.courtservice.framework.services.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.xml.serializer.Method;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xpath.XPathAPI;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ClasspathEntityResolver;
import uk.gov.courtservice.framework.services.XMLServices;

/**
 * <p>
 * Title: XMLServices
 * </p>
 * <p>
 * Description: This class handles loading and creation of xml documents.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Faisal Shoukat
 * @version $Id: XMLServicesImpl.java,v 1.42 2009/06/17 10:05:12 hewittm Exp $
 *
 * <Change History/>
 * <P>
 * 01/11/02 - JB - generateXMLFromPropSet now handles embedded property sets.
 * </P>
 * <P>
 * 22/11/02 - KB - added public Document createDocFromValue( CSValueObject obj )
 * throws CSXMLServicesException
 * <P>
 * 17/02/03 - AWD - Fixed bug Test Obs Id 49, Test Case 51994
 * </p>
 * <P>
 * 17/02/03 - AWD - generateXMLFromPropSet modified to build up an XML DOM
 * document rather than a StringBuffer. Advantage is that substitution of
 * special markup characters into entities is handled.
 * </P>
 *
 * <P>
 * 03/03/03 - AWD - Method added to provide support for XML transformations
 * </P>
 * <P>
 * 05/03/03 - JB - Added addCollectionToXMLDoc to generate XML from collections
 * </P>
 * <P>
 * 03/04/03 - JB - The transformer now ouputs the XML header
 * </P>
 * <P>
 * 03/04/03 - JB - Backed out last change
 * </P>
 * <P>
 * 08/04/03 - JB - Another attempt to get the transformXML to output the header
 * without affecting getStringXML
 * </P>
 * <P>
 * 08/05/03 - JB - Various changes to deal with special characters in the XML.
 * Applying ISO-8859-1 encoding as this handles special chars better.
 * </P>
 * <P>
 * 19/11/03 - RL - Changing encoding to UTF-8 as this handles special chars even
 * better!<br>
 * Removing XMLTransform as XSLServices should be used
 * </P>
 *
 */

public class XMLServicesImpl implements XMLServices {

    private static final String SCHEMA_SOURCE_PROPERTY = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";

    private static final String SCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";

    private static ErrorHandler DEFAULT_ERROR_HANDLER = new ErrorChecker();

    private static EntityResolver DEFAULT_RESOLVER = new SystemIdEntityResolver();

    private static final String ENCODING = "UTF-8";
    private static final String XML_PREFIX = "<?xml version=\"1.0\" encoding=\"";
    private static final String XML_SUFFIX = "\"?>";

    private static final Logger log = CSServices.getLogger(XMLServicesImpl.class);

    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    private static XMLServicesImpl instance = new XMLServicesImpl();

    protected XMLServicesImpl() {
        super();
    }

    /**
     * Get singleton instance of XMLServicesImpl.
     *
     * @return
     */
    public static XMLServicesImpl getInstance() {
        return instance;
    }

    /**
     * This method determines where the XML files are to be read from and
     * returns a java object
     *
     * @return Document - the document object to be parsed
     * @param File -
     *            Directory of XML file to be read
     * @trhows uk.gov.courtservices.exhibit.framework.MappingException The
     *         exception description.
     */
    public Document createDocFromFile(String docName) throws CSXMLServicesException, CSUnrecoverableException {
        log.debug("[createDocFromFile]");
        Document document = null;
        try {
            // Read in XML file
            URL importFile = loadDocumentAsURL(docName);
            // InputStream stream = loadDocument(docName);
            if (importFile == null) {
                throw new IOException("import file not found");
            }
            log.debug("created the input stream");
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(importFile.toString());
        } catch (ParserConfigurationException pce) {
            // log.error("Parse exception thrown when parsing file");
            CSXMLServicesException ex = new CSXMLServicesException(pce);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (SAXException sae) {
            // log.error("SAX exception caught when parsing");
            CSXMLServicesException ex = new CSXMLServicesException(sae);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (IOException ioe) {
            log.fatal("caught IOexception" + ioe);
            CSUnrecoverableException ex = new CSUnrecoverableException(ioe);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        }

        return document;
    }

    /**
     * This method loads the file specified and returns the file as a URL
     *
     * @param docName
     *            the name of the file to be loaded
     * @return loaded document as an InputStream object
     * @throws CSUnrecoverableException
     *             if no resource with this name is found
     */
    // ammendments will be made to this method when the location of the
    // files has been determined
    private URL loadDocumentAsURL(String docName) throws CSUnrecoverableException {

        log.debug(XMLServicesImpl.class.getResource(docName));
        URL importFile = XMLServicesImpl.class.getResource(docName);

        if (importFile == null) {
            CSUnrecoverableException rue = new CSUnrecoverableException("No resource with this name is found : "
                    + docName);
            CSServices.getDefaultErrorHandler().handleError(rue, XMLServicesImpl.class);
            throw rue;
        }

        return importFile;
    }

    /**
     * This method loads the file specified and returns the file as an
     * inputstream
     *
     * @param docName
     *            the name of the file to be loaded
     * @return loaded document as an InputStream object
     * @throws CSUnrecoverableException
     *             if no resource with this name is found
     */
    // ammendments will be made to this method when the location of the
    // files has been determined
    public InputStream loadDocument(String docName) throws CSUnrecoverableException {

        log.debug(XMLServicesImpl.class.getResource(docName));
        InputStream importFile = XMLServicesImpl.class.getResourceAsStream(docName);

        if (importFile == null) {
            String msg = "resource '" + docName + "' not found";
            CSUnrecoverableException rue = new CSUnrecoverableException(msg);
            // system test commented on the number of stack traces being
            // thrown -
            // here, the handleError method prints the stack even though not
            // finding
            // a resource can be an expected condition. changed to simply
            // print a
            // warning message to log (JonP)
            log.warn(msg);
            // CSServices.getDefaultErrorHandler().handleError(rue,
            // XMLServicesImpl.class );
            throw rue;
        }

        return importFile;
    }

    /**
     * Convert a object implementing the CSValueObject interface to an xml
     * document
     *
     * @param obj:
     *            To work effectively the CSValueObject requires a default
     *            public constructor and public get methods for all vos that are
     *            required in the resulting xml document
     * @return Document xml representation of the CSValueObject
     * @throws CSXMLServicesException
     */
    public Document createDocFromValue(CSValueObject obj) throws CSXMLServicesException {
        if (log.isDebugEnabled()) {
            log.debug("START: createDocFromValue(CSValueObject) ATTRIBUTE = " + obj);
        }

        return createDocFromValue(obj, false);
    }

    /**
     * Convert a object implementing the CSValueObject interface to an xml
     * document
     *
     * @param obj:
     *            To work effectively the CSValueObject requires a default
     *            public constructor and public get methods for all vos that are
     *            required in the resulting xml document
     * @return Document xml representation of the CSValueObject
     * @throws CSXMLServicesException
     */
    public Document createDocFromValue(CSValueObject obj, boolean suppressXsiType) throws CSXMLServicesException {
        if (log.isDebugEnabled()) {
            log.debug("START: createDocFromValue(CSValueObject) ATTRIBUTE = " + obj);
        }

        Document doc = null;

        try {
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            doc = builder.newDocument();
            // Mapping mapping = new
            // Mapping(XMLServicesImpl.class.getClassLoader());
            Marshaller marshaller = new Marshaller(doc);
            marshaller.setSuppressXSIType(suppressXsiType);
            marshaller.marshal(obj);
        } catch (ParserConfigurationException pce) {
            if (log.isDebugEnabled())
                log.debug("caught ParserConfigurationException " + pce);
            CSXMLServicesException ex = new CSXMLServicesException(pce);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (MarshalException mse) {
            if (log.isDebugEnabled())
                log.debug("caught Marshalexception" + mse);
            CSXMLServicesException ex = new CSXMLServicesException(mse);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (ValidationException ve) {
            if (log.isDebugEnabled())
                log.debug("caught Validationexception" + ve);
            CSXMLServicesException ex = new CSXMLServicesException(ve);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        }

        return doc;
    }

    /**
     * creates a document object from a value object using CAStor to marshall
     * the object
     *
     * @param docName
     *            the name of the file to be loaded
     * @param object
     *            value object to be marshalled
     * @param Whether
     *            to suppress XSI type
     * @return document the xml document
     * @throws CSUnrecoverableException
     *             if no resource with this name is found
     * @throws CSXMLServicesException
     *             error when marshalling xml document
     */
    public Document createDocFromValue(CSValueObject obj, String docName) throws CSXMLServicesException,
            CSUnrecoverableException {
        Document doc;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            doc = documentBuilder.newDocument();

            // this method will be implemented when it is decided where
            // files will be loaded from
            InputStream importFile = loadDocument(docName);
            InputSource input = new InputSource(importFile);

            Mapping mapping = new Mapping(XMLServicesImpl.class.getClassLoader());

            ClasspathEntityResolver resolve = ClasspathEntityResolver.getInstance();
            resolve.registerReference("-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN", "config/xml/mapping.dtd");
            mapping.setEntityResolver(resolve);

            // The following lines of code are temporary until location of
            // files is determined
            log.debug("loading" + docName);
            mapping.loadMapping(input);

            log.debug("loaded file");

            Marshaller marshaller = new Marshaller(doc);
            marshaller.setMapping(mapping);
            marshaller.marshal(obj);

            log.debug("marshalled object");

        } catch (ParserConfigurationException pce) {
            log.debug("caught ParserConfigurationException " + pce);
            CSXMLServicesException ex = new CSXMLServicesException(pce);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (MappingException me) {
            log.debug("caught Mappingexception" + me);
            CSXMLServicesException ex = new CSXMLServicesException(me);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (MarshalException mse) {
            log.debug("caught Marshalexception" + mse);
            CSXMLServicesException ex = new CSXMLServicesException(mse);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (ValidationException ve) {
            log.debug("caught Validationexception" + ve);
            CSXMLServicesException ex = new CSXMLServicesException(ve);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (IOException ioe) {
            log.debug("caught IOexception" + ioe);
            CSUnrecoverableException ex = new CSUnrecoverableException(ioe);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        }

        return doc;
    }

    /**
     * generates an xml document from a properties set
     *
     * @param xmlToConvert
     *            the properties object which contains the key value pairs
     * @param tag
     *            The root element of the xml document
     * @return String the xml document
     */
    public String generateXMLFromPropSet(Map xmlToConvert, String tag) {
        String methodName = "generateXMLFromPropSet() - ";
        log.debug(methodName + "called :: tag: " + tag + " Map: " + xmlToConvert);

        Document doc;

        try {
            // Get DocumentBuilder
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            log.debug("ParserConfigurationException " + e);
            CSXMLServicesException ex = new CSXMLServicesException(e);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        }

        Element root = addPropertyMapToXMLDoc(doc, xmlToConvert, tag);
        doc.appendChild(root);

        return getStringXML(doc);
    }

    /**
     * Add a collection to the XML document being created
     *
     * @param doc
     * @param xmlToConvert
     * @param tag
     * @return Collection of DOM Elements
     */
    private Collection addCollectionToXMLDoc(Document doc, Collection xmlToConvert, String tag) {
        String methodName = "addCollectionToXMLDoc() - ";
        if (log.isDebugEnabled())
            log.debug(methodName + "called :: collection: " + xmlToConvert + " tag: " + tag);
        Vector<Element> v = new Vector<Element>();

        Iterator it = xmlToConvert.iterator();
        while (it.hasNext()) {
            Map currentMap = (Map) it.next();
            Element collectionElement = addPropertyMapToXMLDoc(doc, currentMap, tag);
            v.add(collectionElement);
        }

        if (log.isDebugEnabled())
            log.debug(methodName + "Exited OK :: " + v.toString());
        return v;
    }

    /**
     * Recursive method to handle embedded property sets.
     *
     * @param document
     *            that the property map will be added to
     * @param xmlToConvert
     *            the properties object which contains the key value pairs
     * @param tag
     *            The root element of the xml document
     */
    private Element addPropertyMapToXMLDoc(Document doc, Map xmlToConvert, String tag) {
        String methodName = "addPropertyMapToXMLDoc() - ";

        log.debug(methodName + "called :: tag: " + tag);
        log.debug(methodName + "No. properties to add : " + xmlToConvert.size());
        Element element = null;

        // Create Document
        Element node = doc.createElement(tag);

        Set keySet = xmlToConvert.keySet();
        if (log.isDebugEnabled())
            log.debug(methodName + "keyset: " + keySet);

        Iterator itr = keySet.iterator();
        if (log.isDebugEnabled())
            log.debug(methodName + "iterator: " + itr);

        // X53490 - Changed from "do while" to "while" loop to stop
        // NoSuchElementException being thrown on
        // an empty collection.
        while (itr.hasNext()) {
            // Get Object and its key from the map
            String key = (String) itr.next();
            if (log.isDebugEnabled())
                log.debug(methodName + "Key: " + key);

            Object obj = xmlToConvert.get(key);

            if (obj instanceof Map) {
                if (log.isDebugEnabled())
                    log.debug(methodName + "Adding Map: " + key);
                element = addPropertyMapToXMLDoc(doc, (Map) obj, key);
                node.appendChild(element);
            } else if (obj instanceof Collection) {
                if (log.isDebugEnabled())
                    log.debug(methodName + "Adding Collection: " + obj.getClass().getName());
                Collection coll = this.addCollectionToXMLDoc(doc, (Collection) obj, key);
                Iterator it = coll.iterator();
                while (it.hasNext()) {
                    node.appendChild((Element) it.next());
                }
            }

            else {
                // Create Element for map entry
                // String value = (String)xmlToConvert.get(key);
                if (log.isDebugEnabled())
                    log.debug(methodName + "Adding element: " + key + "value: " + obj);
                element = doc.createElement(key);
                if (obj != null) {
                    String value = obj.toString();
                    log.debug("value does not equal null has no length");
                    element.appendChild(doc.createTextNode(new String(value)));
                    if (log.isDebugEnabled())
                        log.debug(methodName + "Tag added, name: " + key + ", value: " + value);
                }
                node.appendChild(element);

            }
        }
        // X53490 - End

        return node;
    }

    /**
     *
     * @param xmlContent
     * @return
     * @throws CSXMLServicesException
     */
    public Document createDocFromString(String xmlContent) throws CSXMLServicesException {
        Document xml = null;

        try {
            InputSource is = new InputSource(new StringReader(xmlContent));
            // ByteArrayInputStream bAIS = new
            // ByteArrayInputStream(xmlContent.getBytes());
            // xmlSource = new StreamSource(bAIS);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xml = documentBuilder.parse(is);
        } catch (ParserConfigurationException e) {
            log.debug("ParserConfigurationException " + e);
            CSXMLServicesException xmlE = new CSXMLServicesException(e);
            CSServices.getDefaultErrorHandler().handleError(xmlE, XMLServicesImpl.class);
            throw xmlE;
        } catch (FactoryConfigurationError e) {
            log.debug("FactoryConfigurationError " + e);
            CSXMLServicesException xmlE = new CSXMLServicesException(e);
            CSServices.getDefaultErrorHandler().handleError(xmlE, XMLServicesImpl.class);
            throw xmlE;
        } catch (IOException e) {
            log.debug("FactoryConfigurationError " + e);
            CSXMLServicesException xmlE = new CSXMLServicesException(e);
            CSServices.getDefaultErrorHandler().handleError(xmlE, XMLServicesImpl.class);
            throw xmlE;
        } catch (SAXException e) {
            log.debug("FactoryConfigurationError " + e);
            CSXMLServicesException xmlE = new CSXMLServicesException(e);
            CSServices.getDefaultErrorHandler().handleError(xmlE, XMLServicesImpl.class);
            throw xmlE;
        }

        return xml;

    }

    // RL: Method only used by transform XML so has been removed
    /*
     * public String getStringXML(ByteArrayOutputStream os) { String methodName =
     * "getStringXML() - "; if ( log.isDebugEnabled()) log.debug(methodName +
     * "called");
     *
     * String xmlout = new String( os.toByteArray() );
     *
     * if ( log.isDebugEnabled()) log.debug(methodName + "exited :: xmlout: " +
     * xmlout);
     *
     * return xmlout; }
     */
    public String getStringXML(Document doc) throws CSXMLServicesException {
        String methodName = "getStringXML() - ";
        if (log.isDebugEnabled()) {
            log.debug(methodName + "called");
        }

        DOMSource source = new DOMSource(doc);

        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // StreamResult result = new StreamResult(baos);
        StringWriter write = new StringWriter();
        StreamResult result = new StreamResult(write);

        this.transform(source, result, null, null, false);

        // ByteArrayOutputStream os =
        // (ByteArrayOutputStream)(result.getOutputStream());
        // String xmlout = new String(os.toByteArray());

        String xmlout = write.toString();
        if (log.isDebugEnabled()) {
            log.debug(methodName + "exited :: xmlout: " + xmlout);
        }
        return xmlout;
    }

    // RL: Method has been replaced by XSLServices
    /**
     * transforms xml using the specified style sheet
     *
     * @param xmlToTransform
     *            xmlToTransform held in a String
     * @param styleSheetName
     *            Style Sheet to apply (full path)
     * @param resolver
     *            An object called by the processor to turn a URI used in
     *            document(), xsl:import, or xsl:include into a Source object.
     *            can be null
     * @return String the result of the transformation
     * @throws CSXMLServicesException
     *             and CSUnrecoverableException if the style sheet cannot be
     *             read
     * @deprecated Please use XSLServices.
     */
    /*
     * public String transformXML(String xmlToTransform, String styleSheetName,
     * URIResolver resolver) throws CSXMLServicesException,
     * CSUnrecoverableException { String methodName = "transformXML() - ";
     * log.debug( methodName + "entry" );
     *
     * log.debug( methodName + "xml to transform = " + xmlToTransform );
     *
     * //Creates Input Source from XML to transform ByteArrayInputStream bAIS =
     * new ByteArrayInputStream( xmlToTransform.getBytes() ); StreamSource
     * xmlSource = new StreamSource(bAIS);
     *
     * //Creates Input Source from Style Sheet log.debug( methodName + "xsl = " +
     * styleSheetName ); StreamSource xslSource = new StreamSource(
     * loadDocument( styleSheetName ) );
     *
     * //Creates Result Stream ByteArrayOutputStream baos = new
     * ByteArrayOutputStream(); StreamResult result = new StreamResult( baos );
     *
     * //Performs Transformation this.transform( xmlSource, result, xslSource,
     * resolver, false);
     *
     * //log.debug( methodName + "transformedXml = " +
     * transformedXml.getBuffer().toString() ); log.debug( methodName +
     * "transformedXml = " + baos.toString() ); //return baos.toString();
     *
     * ByteArrayOutputStream os =
     * (ByteArrayOutputStream)(result.getOutputStream()); return
     * getStringXML(os); //return transformedXml.getBuffer().toString(); }
     */
    /**
     * Returns the transformer for the specified Style Sheet and resolver
     *
     * @param xslSource
     *            The style sheet Source.
     * @param resolver
     *            An object called by the processor to turn a URI used in
     *            document(), xsl:import, or xsl:include into a Source object.
     *            can be null
     * @return transformer
     * @throws TransformerConfigurationException
     */
    private Transformer getTransformer(Source xslSource, URIResolver resolver) throws TransformerConfigurationException {

        String methodName = "getTransformer() - ";
        log.debug(methodName + "entry");

        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer;

        if (resolver != null) {
            transFactory.setURIResolver(resolver);
        }

        if (xslSource == null) {
            transformer = transFactory.newTransformer();
        } else {
            transformer = transFactory.newTransformer(xslSource);
        }

        return transformer;
    }

    /**
     * Performs the transformation
     *
     * @param xmlToTransform
     *            xmlToTransform held in a String
     * @param Result
     *            the result of the transformation
     * @param styleSheetName
     *            Style Sheet to apply (full path) can be null
     * @param resolver
     *            An object called by the processor to turn a URI used in
     *            document(), xsl:import, or xsl:include into a Source object.
     *            can be null
     * @throws CSXMLServicesException
     */
    private void transform(Source source, Result result, Source xslSource, URIResolver resolver, boolean omitHeader)
            throws CSXMLServicesException {
        try {

            Transformer transformer = getTransformer(xslSource, resolver);
            if (omitHeader == true)
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, XMLServicesImpl.ENCODING);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            log.debug("TransformerConfigurationException " + e);
            CSXMLServicesException ex = new CSXMLServicesException(e);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        } catch (TransformerException e) {
            log.debug("TransformerException " + e);
            CSXMLServicesException ex = new CSXMLServicesException(e);
            CSServices.getDefaultErrorHandler().handleError(ex, XMLServicesImpl.class);
            throw ex;
        }
    }

    /**
     * Retrieves the value of an xpath from an xml string.
     *
     * @param xmlString
     *            The xml to search
     * @param xpath
     *            The xpath to search for
     * @return The value of the xpath
     * @throws CSUnrecoverableException
     *             If the xpath is not found in the xml
     */
    public String getXpathValueFromXmlString(String xmlString, String xpath) throws CSUnrecoverableException {
        log.debug("getXpathValueFromXpathString() start with " + "xmlString = " + xmlString + " and xpath = " + xpath);
        // check parameters we're going to use
        if (xmlString == null || xpath == null) {
            throw new IllegalArgumentException("Both xmlString and xpath parameters must both be provided "
                    + "with not null values to the getXpathValueFromXpathString() method");
        }

        try {
            // First turn the string XML into a DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            // ByteArrayInputStream in = new
            // ByteArrayInputStream(xmlString.getBytes());
            Document eventDocument = builder.parse(is);

            // Then using the XPathAPI to get the value of the node
            Node valueNode = XPathAPI.selectSingleNode(eventDocument, xpath);

            if (valueNode != null) // Is there a node for the XPath?
            {
                log.debug("getXpathFromLogEntry() : Returning xpath value " + valueNode.getNodeValue());
                return valueNode.getNodeValue();
            } else
                throw new CSUnrecoverableException("Could not get a node for the XPath: " + xpath + " for the xml: "
                        + xmlString);
        } catch (ParserConfigurationException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Problem in parser configuration.", e);
        } catch (FactoryConfigurationError e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Problem in factory configuration.", e);
        } catch (IOException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Error reading input XML: " + xmlString, e);
        } catch (SAXException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Error parsing input XML: " + xmlString, e);
        } catch (TransformerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Problem in reading XPath: " + xpath + " in XML: " + xmlString, e);
        }
    }

    /**
     * Retrieves an array of values of an xpath from an xml string. I.e. the
     * value for each ocurrance of the xpath in the xml.
     *
     * @param xmlString
     *            The xpath to search for
     * @param xpath
     *            The value of the xpath
     * @return A String[] containing the values for each occrance of the xpath
     *         in the xml, or an empty array if no occurrances found.
     */
    public String[] getXpathValuesFromXmlString(String xmlString, String xpath) {
        log.debug("getXpathValueFromXpathString() start with " + "xmlString = " + xmlString + " and xpath = " + xpath);
        // check parameters we're going to use
        if (xmlString == null || xpath == null) {
            throw new IllegalArgumentException("Both xmlString and xpath parameters must both be provided "
                    + "with not null values to the getXpathValueFromXpathString() method");
        }

        try {
            // First turn the string XML into a DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            // ByteArrayInputStream in = new
            // ByteArrayInputStream(xmlString.getBytes());
            Document eventDocument = builder.parse(is);

            // Then use the XPathAPI to get the values of the nodes
            NodeIterator valueNodeIterator = XPathAPI.selectNodeIterator(eventDocument, xpath);

            Node valueNode = valueNodeIterator.nextNode();
            ArrayList<String> values = new ArrayList<String>();

            // Loop through each node returned and get the value
            while (valueNode != null) {
                log.debug("getXpathFromLogEntry() : Adding xpath value " + valueNode.getNodeValue());
                values.add(valueNode.getNodeValue());
                valueNode = valueNodeIterator.nextNode();
            }

            return values.toArray(new String[] {});
        } catch (ParserConfigurationException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Problem in parser configuration.", e);
        } catch (FactoryConfigurationError e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Problem in factory configuration.", e);
        } catch (IOException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Error reading input XML: " + xmlString, e);
        } catch (SAXException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Error parsing input XML: " + xmlString, e);
        } catch (TransformerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Problem in reading XPath: " + xpath + " in XML: " + xmlString, e);
        }
    }

    /**
     * Adds the specified element before the <coed>beforeTag</code> in the
     * passed document
     *
     * @param document
     *            Document to which the element is added
     * @param tagName
     *            Name of the element that is added
     * @param value
     *            Node value of the element
     * @param beforeTag
     *            Tag before whish the new element is added
     */
    public void addElementByTagName(Document document, String tagName, String value, String beforeTag) {
        if (document == null) {
            log.warn(getClass().getName() + "addElementByTagName() :: No XML document loaded");
            return;
        }

        NodeList beforeNodeList = document.getElementsByTagName(beforeTag);

        if (beforeNodeList.getLength() > 0) {
            Element beforeElement = (Element) beforeNodeList.item(0);
            Element newElement = document.createElement(tagName);
            newElement.appendChild(document.createTextNode(value));

            beforeElement.insertBefore(newElement, beforeElement.getFirstChild());
        }
    }

    /**
     * Validates the XML specified in
     * <code>xmlString<code> against the schema specified
     * by the argument <code>schemaBase</code>
     *
     * @param xmlString XML to be validated
     * @param schemaLocation Schema to use for validation
     */
    public void validateXML(String xmlString, String schemaLocation) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setFeature(SCHEMA_VALIDATION_FEATURE, true);

            URL schema = getClass().getResource(schemaLocation);
            if (schema == null) {
                throw new CSUnrecoverableException(
                        "Unable to get schema from schema location: " + schemaLocation);
            }
            reader.setProperty(SCHEMA_SOURCE_PROPERTY, schema.toString());

            reader.setEntityResolver(DEFAULT_RESOLVER);
            reader.setErrorHandler(DEFAULT_ERROR_HANDLER);
            reader.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException(e.getMessage(), e);
        }

    }

    /**
     * Encodes the XML specified in
     * <code>xmlString<code> to replace < and  > characters.
     *
     * @param xmlString XML to be validated
     * @return String
     */
    public String encodeXML(String xmlString) {
        final StringBuffer result = new StringBuffer();
        final StringCharacterIterator iterator = new StringCharacterIterator(xmlString);
        char character = iterator.current();
        while (character != CharacterIterator.DONE) {
            if (character == '<') {
                result.append("&lt;");
            } else if (character == '>') {
                result.append("&gt;");
            }
            // Document Option 4b Walkthrough(section 2) does not require
            // apostophes and double quotes to be encoded.
            /* else if (character == '\"') { result.append("&quot;"); }
             * else if (character == '\'') { result.append("&apos;"); }
             */
            else {
                // the char is not a special one
                // add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }

    /**
     * Decodes the XML specified in
     * <code>xmlString<code> to replace &lt; and &gt; strings.
     *
     * @param xmlString XML to be modified
     * @return String
     */
    public String decodeXML(String xmlString) {

        String returnStr = xmlString.replaceAll("&lt;|&LT;", "<");
        returnStr = returnStr.replaceAll("&gt;|&GT;", ">");
        // Document Option 4b Walkthrough(section 2) does not require
        // apostophes and double quotes to be encoded.
        returnStr = returnStr.replaceAll("&quot;|&QUOT;", "\"");
        returnStr = returnStr.replaceAll("&apos;|&APOS;", "\'");

        return returnStr;
    }

    /**
     * Puts 'xml version and encoding header' at
     * the beginning of the <code>xmlString<code>
     *
     * @param xmlString XML to be modified
     * @return String
     */
    public String addXMLHeader(String xmlString) {

        // build up string like this: <?xml version="1.0" encoding="UTF-8"?>
        // and put it in front of xmlString
        StringBuilder returnStr = new StringBuilder(XML_PREFIX);
        returnStr.append(ENCODING);
        returnStr.append(XML_SUFFIX);
        returnStr.append(xmlString);

        return returnStr.toString();
    }

    /**
     * Create a serializer to write to the output stream
     *
     * @param out
     *            the stream to create the serializer from
     * @return the new serializer
     */
    public ContentHandler createXmlSerializer(OutputStream out) throws IOException {
        Serializer serializer = createSerializer(Method.XML);
        serializer.setOutputStream(out);
        return serializer.asContentHandler();
    }

    /**
     * Create a serializer to write to the character stream
     *
     * @param writer
     *            the character stream to create the serializer from
     * @return the new serializer
     */
    public ContentHandler createXmlSerializer(Writer writer) throws IOException {
        Serializer serializer = createSerializer(Method.XML);
        serializer.setWriter(writer);
        return serializer.asContentHandler();
    }

    /**
     * Create a serializer to write to the output stream
     *
     * @param out
     *            the stream to create the serializer from
     * @return the new serializer
     */
    public ContentHandler createHtmlSerializer(OutputStream out) throws IOException {
        Serializer serializer = createSerializer(Method.HTML);
        serializer.setOutputStream(out);
        return serializer.asContentHandler();
    }

    /**
     * Create a serializer to write to the character stream
     *
     * @param writer
     *            the character stream to create the serializer from
     * @return the new serializer
     */
    public ContentHandler createHtmlSerializer(Writer writer) throws IOException {
        Serializer serializer = createSerializer(Method.HTML);
        serializer.setWriter(writer);
        return serializer.asContentHandler();
    }

    /**
     * Creates a Serializer for the give mime type.
     *
     * @param mimeType
     *            The mime type e.g. HTML, XML etc.
     * @return the Serializer for the given mimeType
     */
    private Serializer createSerializer(String mimeType) {
        return SerializerFactory.getSerializer(OutputPropertiesFactory.getDefaultMethodProperties(mimeType));
    }

}