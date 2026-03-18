package uk.gov.courtservice.xhibit.client.util;

import org.w3c.dom.Document;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.services.xml.CSXMLServicesException;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;

/**
 * <p>
 * Title: XML Helper
 * </p>
 * <p>
 * Description: This class simplifies and centralises calls to XMLServiceImpl in
 * the framework.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class XMLHelper {
    XMLServicesImpl xmlService;

    public XMLHelper() {
        xmlService = XMLServicesImpl.getInstance();
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
    public Document getXMLDocumentFromValue(CSValueObject vo) {
        return xmlService.createDocFromValue(vo);
    }

    /**
     * Returns the XML string from an XML document
     * 
     * @param doc
     * @return the xml string for the document
     */
    public String getStringFromDocument(Document doc) {
        return xmlService.getStringXML(doc);
    }

    /**
     * Returns the xml string transformed from a value object
     * 
     * @param vo
     * @return
     */
    public String getXmlStringFromValue(CSValueObject vo) {
        return getStringFromDocument(getXMLDocumentFromValue(vo));
    }
}