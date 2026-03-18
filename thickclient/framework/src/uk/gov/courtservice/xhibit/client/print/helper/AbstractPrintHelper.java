package uk.gov.courtservice.xhibit.client.print.helper;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.XMLReader;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.print.exception.FOPCreateDocumentException;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Xhibit2 AbstractPrintHelper
 * </p>
 * <p>
 * Description: Abstract class that implements common FOP rendering
 * functionality. Basic functionality is as supplied by FOP example code
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Neil Entwistle
 * @version 1.0
 */

public abstract class AbstractPrintHelper {
	
	private static final FopFactory fopFactory = FOPFactory.getFopFactory(new File(".").toURI());

    private static final Logger log = CSServices.getLogger(AbstractPrintHelper.class);

    /**
     * The transformer
     */
    protected Transformer transformer;

    /**
     * The AWT Renderer
     */
    private AWTRenderer renderer;

    /**
     * The FOP User Agent
     */
    private FOUserAgent foUserAgent;
    
    /**
     * FOP
     */
    private Fop fop;

    /**
     * The parser
     */
    protected XMLReader parser;

    /**
     * Constructor
     */
    public AbstractPrintHelper() {
        renderer = getRenderer();
    }

    /**
     * This method provides per instance of sub-class.
     *
     * @return The AWT renderer for the sub class.
     */
    protected synchronized AWTRenderer getRenderer() {
        if (renderer == null) {
            renderer = new AWTRenderer(getFOUserAgent());
        }
        return renderer;
    }
    
    /**
     * This method provides an instantiated FOP USer Agent
     * 
     * @return the FOP User agent
     */
    protected synchronized FOUserAgent getFOUserAgent() {
    	if (foUserAgent == null) {
    		foUserAgent = FOPFactory.getFoUserAgent(fopFactory);
    	}
    	return foUserAgent;
    }

    protected synchronized Fop getFop() throws FOPException {
    	if (fop == null) {
			fop = fopFactory.newFop(MimeConstants.MIME_PDF, getFOUserAgent());
    	}
    	
    	return fop;
    }
    
    protected synchronized void resetFop() throws FOPException {
    	fop = getFOUserAgent().newFop(MimeConstants.MIME_PDF); 
    }
    
    protected synchronized void resetFop(ByteArrayOutputStream outStream) throws FOPException {
    	fop = getFOUserAgent().newFop(MimeConstants.MIME_PDF, outStream);
    }

    /**
     * Creates a document from the supplied FO
     *
     * @param fo
     *            FO documement as a string
     * @param type
     *            Document type to be returned
     * @return Document as byte[]
     */
    public byte[] createDocument(String fo, int type) throws FOPCreateDocumentException {
        log.info("Creating document of type " + type + " from " + fo);
        
        // Initialise the FOP resources
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // get a Transformer object
        try {
        	resetFop(out);
        	
        	// Parse the requested FO file
            loadTransform(fo);
        } catch (FOPException fe) {
            throw new FOPCreateDocumentException(fe);
        }

        return out.toByteArray();
    }

    /**
     * Transforms an FO file passed as a string
     *
     * @param s
     * @throws FOPException
     */
    public void loadTransform(String s) throws FOPException {
        log.debug("start - loadTransform(String s)");
        loadTransform(new StreamSource(new StringReader(s)));
    }

    /**
     * Transforms an FO file passed as a string
     *
     * @param s
     * @throws FOPException
     */
    public void loadTransform(StreamSource s) throws FOPException {
        log.debug("start - loadTransform(String s)");
        try {
        	Fop fop = getFop();
        	setTransformer(TransformerFactory.newInstance().newTransformer());            
            getTransformer().transform(s, new SAXResult(fop.getDefaultHandler()));
        } catch (TransformerException te) {
            throw new FOPException("Transformer Exception", te);
        }
    }

    /**
     * Transforms a DOM
     *
     * @param dom
     * @throws FOPException
     */
    public void loadTransform(Document dom) throws FOPException {
        try {
            getTransformer().transform(new DOMSource(dom), new SAXResult(getFop().getDefaultHandler()));
        } catch (TransformerException te) {
            throw new FOPException("Transformer Exception", te);
        }
    }

    /**
     * Transforms a DOM
     *
     * @param xmlIs
     *            an xml input stream
     * @throws FOPException
     */
    public void loadTransform(InputStream xmlIs) throws FOPException {
        loadTransform(new StreamSource(xmlIs));
    }

    /**
     * Invoke a Print Dialog for the rendered document
     */
    protected void print() throws CSRecoverableException {
        print(true);
    }

    /**
     * Optionally invoke a Print Dialog for the rendered document
     */
    protected void print(boolean showDialog) throws CSRecoverableException {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPageable(renderer);

        if (showDialog) {
            if (pj.printDialog()) {
                shellPrint(pj);
            }
        } else {
            shellPrint(pj);
        }
    }

    /**
     * Activates the local OS print
     *
     * @param pj
     *            PrinterJob
     * @throws CSRecoverableException
     */
    private void shellPrint(PrinterJob pj) throws CSRecoverableException {
        try {
            pj.print();
        } catch (PrinterException pe) {
            log.error("Printing Exception while printing document", pe);
            throw new CSRecoverableException("printing.fop", "Printing Exception while printing document", pe);
        } finally {
            pj = null;
        }
    }

    protected Transformer getTransformer() throws FOPException {
        try {
            if (transformer == null) {
            	setTransformer(TransformerFactory.newInstance().newTransformer());
            }
        } catch (TransformerConfigurationException te) {
            throw new FOPException("Transformer Exception", te);
        } catch (Exception te) {
            throw new FOPException("Transformer Exception", te);
        } 
        return transformer;
    }
    
    protected void setTransformer(Transformer transformer) {
    	this.transformer = transformer;
    }

    protected void setRenderer(AWTRenderer rend) {
        renderer = rend;
    }

    /**
     * Remove any existing pages from the renderer. This will remove any
     * possibility of data bleed between Orders
     */
    protected void resetRenderer() {
    	getRenderer().clearViewportList();
    }
    
    protected String getResourceBundleName() {
    	return XhibitBundles.FopResources;
    }
    
	public String getResourceBundle(String resourceKey, Object[] objects) {
		return MessageFormat.format(getResourceBundle(resourceKey), objects);
	}
	
	public String getResourceBundle(String key) {
		return XHIBITConstant.getResource(getResourceBundleName(), key);
	}
}