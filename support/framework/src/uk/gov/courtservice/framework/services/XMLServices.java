package uk.gov.courtservice.framework.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.xml.CSXMLServicesException;

/**
 * <p>
 * Title: XML Services
 * </p>
 * <p>
 * Description: Insulates the application components from knowedge of XML
 * Services.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Faisal Shoukat
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 03-03-2003 AW Daley Interface changed to provide support for XML
 * transformations
 */
public interface XMLServices {

    /**
     * This method determines where the XML files are to be read from and
     * returns a java object
     * 
     * @return Document - the document object to be parsed
     * @param File -
     *            Directory of XML file to be read
     * @throws The
     *             exception description.
     */
    public Document createDocFromFile(String docName) throws CSXMLServicesException, CSUnrecoverableException;

    /**
     * Creates a document from a value object
     * 
     * @param the
     *            name of mapping.xml file to be used
     * @returns the created document
     * @throws CSXMLServicesException,
     *             CSUnrecoverableException if xml file cannot be read.
     */
    public Document createDocFromValue(CSValueObject obj, String docName) throws CSXMLServicesException,
            CSUnrecoverableException;

    /**
     * 
     * @param xmlContent
     * @return
     * @throws CSXMLServicesException
     */
    public Document createDocFromString(String xmlContent) throws CSXMLServicesException;

    public String getStringXML(Document doc) throws CSXMLServicesException;

    /**
     * Loads the document specified
     * 
     * @param the
     *            name of the document to be loaded
     * @returns the document as an input stream
     * @throws CSUnrecoverableException
     *             if the document cannot be read.
     */
    public InputStream loadDocument(String docName) throws CSUnrecoverableException;

    /**
     * generates an xml document from a properties set
     * 
     * @param xmlToConvert
     *            the properties object which contains the key value pairs
     * @param tag
     *            The root element of the xml document
     * @return String the xml document
     */
    public String generateXMLFromPropSet(Map xmlToConvert, String tag);

    // RL: Method has been replaced by XSLServices
    /**
     * transforms xml using the specified style sheet
     * 
     * @param xmlToTransform
     *            xmlToTransform held in a String
     * @param styleSheetName
     *            Style Sheet to apply (full path). Can be null
     * @param resolver
     *            An object called by the processor to turn a URI used in
     *            document(), xsl:import, or xsl:include into a Source object.
     *            can be null
     * @return String the result of the transformation
     * @throws CSXMLServicesException
     *             and CSUnrecoverableException if the style sheet cannot be
     *             read
     */
    /*
     * public String transformXML(String xmlToTransform, String styleSheetName,
     * URIResolver resolver) throws CSXMLServicesException,
     * CSUnrecoverableException;
     */
    /**
     * Retrieves the value of an xpath from an xml String
     * 
     * @param xmlString
     *            The xml to search
     * @param xpath
     *            The xpath to look for
     * @return The value of the xpath
     * @throws CSUnrecoverableException
     *             If
     */
    public String getXpathValueFromXmlString(String xmlString, String xpath) throws CSUnrecoverableException;

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
    public String[] getXpathValuesFromXmlString(String xmlString, String xpath);

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
    public Document createDocFromValue(CSValueObject obj) throws CSXMLServicesException;

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
    public Document createDocFromValue(CSValueObject obj, boolean suppressXsiType) throws CSXMLServicesException;

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
    public void addElementByTagName(Document document, String tagName, String value, String beforeTag);

    /**
     * Validates the XML specified in
     * <code>xmlString<code> against the schema specified
     * by the argument <code>schemaBase</code>
     * 
     * @param xmlString XML to be validated
     * @param schemaLocation Schema to use for validation
     */
    public void validateXML(String xmlString, String schemaLocation);

    /**
     * Encodes the XML specified in
     * <code>xmlString<code>. 
     *      *
     * @param xmlString XML to be validated
     * @return String
     */
    public String encodeXML(String xmlString);
    
    /**
     * Decodes the XML specified in
     * <code>xmlString<code>. 
     *      *
     * @param xmlString XML to be validated
     * @return String
     */
    
    /**
     * Decodes the XML specified in
     * <code>xmlString<code> to replace &lt; and &gt; strings.
     *      
     * @param xmlString XML to be modified
     * @return String
     */
    public String decodeXML(String xmlString); 
    
    /**
     * Puts 'xml version and encoding header' at 
     * the beginning of the <code>xmlString<code>
     *      
     * @param xmlString XML to be modified
     * @return String
     */
    public String addXMLHeader(String xmlString);
    
    /**
     * Create a serializer to write to the output stream
     * 
     * @param out
     *            the stream to create the serializer from
     * @return the new serializer
     * @throws IOException
     *             if an error occures creating the content handler
     */
    public ContentHandler createXmlSerializer(OutputStream out) throws IOException;

    /**
     * Create a serializer to write to the character stream
     * 
     * @param writer
     *            the character stream to create the serializer from
     * @return the new serializer
     * @throws IOException
     *             if an error occures creating the content handler
     */
    public ContentHandler createXmlSerializer(Writer writer) throws IOException;

    /**
     * Create a serializer to write to the output stream
     * 
     * @param out
     *            the stream to create the serializer from
     * @return the new serializer
     * @throws IOException
     *             if an error occures creating the content handler
     */
    public ContentHandler createHtmlSerializer(OutputStream out) throws IOException;

    /**
     * Create a serializer to write to the character stream
     * 
     * @param writer
     *            the character stream to create the serializer from
     * @return the new serializer
     * @throws IOException
     *             if an error occures creating the content handler
     */
    public ContentHandler createHtmlSerializer(Writer writer) throws IOException;
}
