package uk.gov.courtservice.xhibit.client.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Helper method for client side XSL-FO transforming
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
/*
 * Ref Date Author Description
 * 
 * 52829 10-6-2003 AW Daley transformAndPrint method added where the document
 * title is passed.
 */
public class XSLTransformHelper {

    private static Logger log = Logger.getLogger(XSLTransformHelper.class.getName());
    private HashMap map;

    Locale myLocale;

    /**
     * Construct a XSLTransformHelper for the default Locale
     */
    public XSLTransformHelper() {
        this(Locale.getDefault());
    }

    public XSLTransformHelper(Locale locale) {
        myLocale = locale;
    }

    /**
     * Takes a value object and converts it into xml, then transforms it using
     * then xsl passed in
     * 
     * @param vo
     * @param xslName
     * @return
     */
    public String transform(CSValueObject vo, String xslName) {
        XMLHelper x = new XMLHelper();
        return transform(x.getXmlStringFromValue(vo), xslName);
    }
    
    public String transform(CSValueObject vo, String xslName, Map map) {
        XMLHelper x = new XMLHelper();
        return transform(x.getXmlStringFromValue(vo), xslName, map);
    }
    
    /**
     * transforms the xml string using the xsl passed in
     * 
     * @param xml
     * @param xslName
     * @return
     */
    public String transform(String xml, String xslName) {
        if (xml != null && !xml.equals("")) {
            // modified to use XSLServices rather than XMLServices - i18n
            // conventions now include Local in filename rather than
            // directory structure
            String styleSheet = "config/xsl/" + xslName + ".xsl";
            log.debug("xsl=" + styleSheet);
            String transformedFO = CSServices.getXSLServices().transform(xml, styleSheet, Locale.getDefault(), null);
            return transformedFO;
        }
        log.error("one of xml string or xsl filename was null");
        return "";
    }
    
    public String transform(String xml, String xslName, Map map) {
        if (xml != null && !xml.equals("")) {
            // modified to use XSLServices rather than XMLServices - i18n
            // conventions now include Local in filename rather than
            // directory structure
            String styleSheet = "config/xsl/" + xslName + ".xsl";
            log.debug("xsl=" + styleSheet);
            String transformedFO = CSServices.getXSLServices().transform(xml, styleSheet, Locale.getDefault(), map);
            return transformedFO;
        }
        log.error("one of xml string or xsl filename was null");
        return "";
    }

    /**
     * Translates the value object passed in into XML, then transforms into
     * XSL-FO using the xsl passed in and display in a FOP window with the
     * document title supplied.
     * 
     * @param vo
     * @param xslName
     * @param preview
     * @param documentTitle
     */
    public void transformAndPrint(CSValueObject vo, String xslName, boolean preview, String documentTitle)
            throws CSRecoverableException {
        XMLHelper x = new XMLHelper();
        transformAndPrint(x.getXmlStringFromValue(vo), xslName, preview, documentTitle);
    }
    
    public void transformAndPrint(CSValueObject vo, String xslName, boolean preview, String documentTitle, Map map)
            throws CSRecoverableException {
        XMLHelper x = new XMLHelper();
        transformAndPrint(x.getXmlStringFromValue(vo), xslName, preview, documentTitle,map);
    }

    /**
     * Translates the value object passed in into XML, then transforms into
     * XSL-FO using the xsl passed in and display in a FOP window
     * 
     * @param vo
     * @param xslName
     * @param preview
     */
    public void transformAndPrint(CSValueObject vo, String xslName, boolean preview) throws CSRecoverableException {
        transformAndPrint(vo, xslName, preview, null);
    }

    /**
     * Takes an xml string and an xsl file to parse it with, then displays it in
     * a FOP window
     * 
     * @param xml
     * @param xslName
     * @param preview
     */
    public void transformAndPrint(String xml, String xslName, boolean preview) throws CSRecoverableException {
        transformAndPrint(xml, xslName, preview, null);
    }

    /**
     * Takes an xml string and an xsl file to parse it with, then displays it in
     * a FOP window headed with the title passed in.
     * 
     * @param xml
     * @param xslName
     * @param preview
     * @param documentTitle
     */
    public void transformAndPrint(String xml, String xslName, boolean preview, String documentTitle)
            throws CSRecoverableException {
        if (xml != null && !xml.equals("")) {
            String differenceFO = transform(xml, xslName);
            try {
                FOPInterface fop = FOPFactory.getFOPRenderer(preview);

                if (documentTitle == null)
                    fop.printDocument(differenceFO, preview);
                else
                    fop.printDocument(differenceFO, preview, documentTitle);
            } catch (Exception e) {
                Object[] params = null;
                CSRecoverableException csre = new CSRecoverableException("gui.printaction.print", params,
                        "An error occurred whilst printing.", e);
                throw csre;
                // XHIBITConstant.handleError(csre);
            }
        }
    }
    
    public void transformAndPrint(String xml, String xslName, boolean preview, String documentTitle, Map map)
            throws CSRecoverableException {
        if (xml != null && !xml.equals("")) {
            String differenceFO = transform(xml, xslName, map);
            try {
                FOPInterface fop = FOPFactory.getFOPRenderer(preview);

                if (documentTitle == null)
                    fop.printDocument(differenceFO, preview);
                else
                    fop.printDocument(differenceFO, preview, documentTitle);
            } catch (Exception e) {
                Object[] params = null;
                CSRecoverableException csre = new CSRecoverableException("gui.printaction.print", params,
                        "An error occurred whilst printing.", e);
                throw csre;
                // XHIBITConstant.handleError(csre);
            }
        }
    }

	public HashMap getMap() {
		return map;
	}

	public void setMap(HashMap map) {
		this.map = map;
	}
}