package uk.gov.courtservice.framework.services.printing;

import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.PrintServices;
import uk.gov.courtservice.framework.services.xml.CSXMLServicesException;

/**
 * <p>
 * Title: PrintServicesImpl
 * </p>
 * <p>
 * Description: Implementation of the print service
 * </p>
 * <p>
 * This is a replacement for the PrintController
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell (Xdevelopment 2003)
 * @version 1.0
 */

public class PrintServicesImpl implements PrintServices {
    /**
     * Print Service properties used to map class names to xsl transforms
     */
    private static final String PRINT_SERVICE_PROPERTIES = "printxslschemas";

    /**
     * Log4j logger
     */
    private static final Logger log = CSServices.getLogger(PrintServicesImpl.class);

    /**
     * Singleton Instance
     */
    private static final PrintServicesImpl instance = new PrintServicesImpl();

    /**
     * Get the singleton
     * 
     * @return the singleton instance
     */
    public static PrintServicesImpl getInstance() {
        return instance;
    }

    /**
     * Guard against external creation of this class
     */
    private PrintServicesImpl() {

    }

    /**
     * Converts an object implementing the <code>CSValueObject</code>
     * interface (subclasses <code>CSAbstractValue</code>) into an xml
     * document (saved to D:\temp.xml if debug is enabled ) and transforms the
     * resulting xml according to the associated xsl file. The corresponding xsl
     * should be specified in the \config\components\printxslschemas.properties
     * file. The key in printxslschemas.properties should be the fully qualified
     * class name of the value object. The value in printxslschemas.properties
     * is the filename of the xsl file (without the .xsl extention).
     * 
     * @param data
     *            contains the data required to generate the document. To work
     *            effectively the CSValueObject requires a default public
     *            constructor and public get methods for all vos that are
     *            required in the resulting xml document.
     * 
     * @return String: transformed document of value object
     * @throws PrintServiceException
     *             if an error occures
     */
    public String getFormattedDocument(CSValueObject data, Locale locale) throws PrintServiceException {
        return formatDocument(getXMLDocument(data), getXSLTransformName(data), locale);
    }

    /**
     * Performs a similar function to 'getFormattedDocument'. Instead of looking
     * up the corresponding xsl file for a Document, it is specified by the
     * caller.
     * 
     * @param xml
     *            the document to be transformed
     * @param xslName
     *            XSL to use for transform
     * @return transformed document, as a string
     * @throws PrintServiceException
     */
    public String getFormattedDoc(Document xml, String xslName, Locale locale) throws PrintServiceException {
        return formatDocument(xml, xslName, locale);
    }

    /**
     * Format the Document
     * 
     * @param document
     *            the document to format
     * @param xslName
     *            the name of the transform to use
     * @param return
     *            the formated docuemnt
     */
    private static String formatDocument(Document document, String xslName, Locale locale) throws PrintServiceException {
        try {
            return CSServices.getXSLServices().transform(document, xslName, locale, null);
        } catch (CSUnrecoverableException e) {
            CSServices.getDefaultErrorHandler().handleError(e, PrintServicesImpl.class);
            throw new PrintServiceException(e);
        }
    }

    /**
     * Create an XML document from the data
     * 
     * @param data
     *            the data to generate the document from
     * @throws PrintServiceException
     *             if an error occures
     */
    private static Document getXMLDocument(CSValueObject data) throws PrintServiceException {
        try {
            return CSServices.getXMLServices().createDocFromValue(data);
        } catch (CSXMLServicesException e) {
            CSServices.getDefaultErrorHandler().handleError(e, PrintServicesImpl.class);
            throw new PrintServiceException(e);
        }
    }

    /**
     * Get the name of the xsl transform to use for the data
     * 
     * @param data
     *            the object to look up the transform form
     * @return the (resource) name of the transform
     * @throws CSConfigurationException
     *             if an error occures
     */
    private static String getXSLTransformName(CSValueObject data) throws CSConfigurationException {
        return getXSLTransformName(data.getClass().getName().toLowerCase());
    }

    /**
     * Get the name of the xsl transform to use for the data
     * 
     * @param printData
     *            the object to look up the transform form
     * @return the (resource) name of the transform
     * @throws CSConfigurationException
     *             if an error occures
     */
    private static String getXSLTransformName(String dataClassName) throws CSConfigurationException {
        String xslNameProperty = getProperties().getProperty(dataClassName);
        if (xslNameProperty != null) {
            String xslName = xslNameProperty.trim() + ".xsl"; // Add .xsl
            // extension to
            // returned name
            if (log.isDebugEnabled())
                log.debug("Resolved " + dataClassName + " to transform " + xslName);
            return xslName;
        } else {
            throw new CSConfigurationException("Could not find xsl transform for " + dataClassName + " in "
                    + PRINT_SERVICE_PROPERTIES);
        }
    }

    /**
     * Get the PrintServiceProperties (class xsl map)
     * 
     * @return the properties collection
     * @throws CSConfigurationException
     *             if an error occures
     */
    private static Properties getProperties() throws CSConfigurationException {
        return CSServices.getConfigServices().getProperties(PRINT_SERVICE_PROPERTIES);
    }

}
