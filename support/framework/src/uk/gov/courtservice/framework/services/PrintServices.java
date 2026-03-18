package uk.gov.courtservice.framework.services;

import java.util.Locale;

import org.w3c.dom.Document;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.services.printing.PrintServiceException;

/**
 * <p>
 * Title: PrintServices
 * </p>
 * <p>
 * Description: Provides functionality for printing a graph of objects (jb - xml -
 * fop) using castor and fop
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
 * @author Kevin buckthorpe
 * @version 1.0
 */

public interface PrintServices {
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
     * @param printData
     *            contains the data required to generate the document. To work
     *            effectively the CSValueObject requires a default public
     *            constructor and public get methods for all vos that are
     *            required in the resulting xml document.
     * 
     * @return String: transformed document of value object
     * @throws PrintServiceException
     *             if an error occures
     * @author Will Fardell (Xdevelopment 2003)
     */
    public String getFormattedDocument(CSValueObject printData, Locale locale) throws PrintServiceException;

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
    public String getFormattedDoc(Document xml, String xslName, Locale locale) throws PrintServiceException;

}
