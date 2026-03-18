package uk.gov.courtservice.xhibit.client.print.helper;

import java.io.InputStream;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.exception.FOPCreateDocumentException;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: helper class to generate a FOP preview dialog
 * </p>
 * <p>
 * Description: Generates an instance of XhibitPreviewDialog - this extends
 * org.apache.fop.viewer.PreviewDialog and manipulates the toolbar and menu to
 * remove problematic items.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003</p
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class PrintDialogHelper extends AbstractPrintHelper implements FOPInterface {
    private static Logger log = CSServices.getLogger(PrintDialogHelper.class);

    /**
     * Creates a document from the supplied FO
     * 
     * @param fo
     *            FO documment as a string
     * @param type
     *            Document type to be returned
     * @return Document as byte[]
     * @throws FOPCreateDocumentException
     */
    public byte[] createDocument(String fo, int type) throws FOPCreateDocumentException {
        //
        return super.createDocument(fo, type);
    }

    /**
     * Prints the document generated from the supplied FO. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer
     * 
     * @param fo
     *            FO document as a string
     * @param prev
     *            Is preview required
     * @param title
     *            the title of any preview dialog
     */
    public void printDocument(String fo, boolean prev, String title) throws CSRecoverableException {
        // Ignore the title as no preview dialog
        printDocument(fo, prev);
    }

    /**
     * Prints the document generated from the supplied FO. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer If showDialog is
     * false, the user will not have an option to change any printer options.
     * 
     * @param fo
     *            FO document as a string
     * @param prev
     *            Is preview required
     */
    public void printDocument(String fo, boolean prev, boolean showDialog) throws CSRecoverableException {
        log.debug("PrintDialogHelper.printDocument(String " + fo + ", boolean " + prev + ", boolean " + showDialog);
        try {
            getTransformer();
            resetRenderer();
            loadTransform(fo);
        } catch (FOPException fe) {
            log.error("Rendering failed");
            log.error("PrintDialogHelper.loadDOMTransform - print error");
        	log.error(fe.getMessage());
        	fe.printStackTrace();
            throw new CSRecoverableException("printing.fop", "Error Printing Document", fe);
        }

        print(showDialog);
    }

    /**
     * Prints the document generated from the supplied FO. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer
     * 
     * @param fo
     *            FO document as a string
     * @param prev
     *            Is preview required
     */
    public void printDocument(String fo, boolean prev) throws CSRecoverableException {
        log.debug("PrintDialogHelper.printDocument(String " + fo + ", boolean " + prev);
        printDocument(fo, prev, true);
    }

    /**
     * Prints the document generated from the FO as a string. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer
     * 
     * @param fo
     *            FO document as a string
     * @param prev
     *            Is preview required
     */
    public void printDocument(Document fo, boolean prev) throws CSRecoverableException {
        log.debug("PrintDialogHelper.printDocument(Document fo, boolean " + prev);
        try {
            resetRenderer();
            loadDOMTransform(fo);
        } catch (FOPException fe) {
            log.error("Rendering failed");
            log.error("PrintDialogHelper.printDocument(Dcoument, boolean) - print error");
        	log.error(fe.getMessage());
        	fe.printStackTrace();
            throw new CSRecoverableException("printing.fop", "Error Printing Document", fe);
        }
        print();
    }

    /**
     * Constructor using a Document object and streamed XSL
     * 
     * @param xmlIs
     *            InputStream - streamed XSL file
     * @param xslIs
     *            InputStream - streamed XSL file
     * @param title
     *            the title of any preview dialog
     * @throws CSRecoverableException
     */
    public void printDocument(InputStream xmlIs, InputStream xslIs, String title) throws CSRecoverableException {
        log.debug("PrintDialogHelper.printDocument(InputStream xmlIs, InputStream xslIs, String " + title);
        // Ignore the title as no preview dialog
        printDocument(xmlIs, xslIs);
    }

    /**
     * Constructor using a Document object and streamed XSL
     * 
     * @param xmlIs
     *            InputStream - streamed XSL file
     * @param xslIs
     *            InputStream - streamed XSL file
     * @throws CSRecoverableException
     */
    public void printDocument(InputStream xmlIs, InputStream xslIs) throws CSRecoverableException {
        log.debug("PrintDialogHelper.printDocument(InputStream xmlIs, InputStream xslIs)");
        setupTransformer(xslIs);

        try {
            loadTransform(xmlIs);
        } catch (FOPException fe) {
        	log.error("PrintDialogHelper.printDocument(InputStream, InputStream) - print error");
        	log.error(fe.getMessage());
        	fe.printStackTrace();
            throw new CSRecoverableException("printing.fop", "FOP Exception while transforming document", fe);
        }
        print();
    }

    /**
     * Constructor using a Document object and streamed XSL
     * 
     * @param xmlIs
     *            InputStream - streamed XSL file
     * @param xslIs
     *            InputStream - streamed XSL file
     * @throws CSRecoverableException
     */
    public void printDocument(String xmlIs, InputStream xslIs) throws CSRecoverableException {
        log.debug("PrintDialogHelper.printDocument(InputStream xmlIs, InputStream xslIs)");
        setupTransformer(xslIs);

        try {
            loadTransform(xmlIs);
        } catch (FOPException fe) {
        	log.error("PrintDialogHelper.printDocument(String, InputStream) - print error");
        	log.error(fe.getMessage());
        	fe.printStackTrace();
            throw new CSRecoverableException("printing.fop", "FOP Exception while transforming document", fe);
        }
        print();
    }

    /**
     * Initialise the Transformer
     * 
     * @param xslIs
     *            the InputStream
     * @throws TransformerFactoryConfigurationError
     * @throws CSRecoverableException
     */
    private void setupTransformer(InputStream xslIs) throws TransformerFactoryConfigurationError,
            CSRecoverableException {
        try {
            transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslIs));
        } catch (TransformerException te) {
        	log.error("PrintDialogHelper.setupTransformer - print error");
        	log.error(te.getMessage());
        	te.printStackTrace();
            throw new CSRecoverableException("printing.fop", "Transformer Exception while transforming document", te);
        }
    }

    /**
     * Transforms the DOM
     * 
     * @param dom
     */
    public void loadDOMTransform(Document dom) throws FOPException {
        log.debug("PrintDialogHelper.loadDOMTransform(Document dom)");
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(dom), new SAXResult(getFop().getDefaultHandler()));
        } catch (TransformerException te) {
        	log.error("PrintDialogHelper.loadDOMTransform - print error");
        	log.error(te.getMessage());
        	te.printStackTrace();
            throw new FOPException("Transformer Exception", te);
        }
    }

	@Override
	public void printDocument(String xslFO, boolean preview, String title, XAction action)
			throws CSRecoverableException {
		
	}

}