package uk.gov.courtservice.xhibit.client.print.helper;

import java.io.InputStream;

import javax.swing.JFrame;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.awt.viewer.PreviewDialog;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.exception.FOPCreateDocumentException;
import uk.gov.courtservice.xhibit.client.print.viewer.XhibitPreviewDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.helpers.WindowBoundsHelper;

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

public class PrintPreviewHelper extends AbstractPrintHelper implements FOPInterface {
    private static final Logger log = CSServices.getLogger(PrintPreviewHelper.class);

    XhibitPreviewDialog frame;

    /**
     * Creates an instance of the preview dialog to control the printing process
     * The rendering is for AWT - this may be extended to PDF for furure
     * releases of Xhibit
     * 
     * @param renderer
     *            The AWTRenderer used to render the FO
     * @param res
     *            org.apache.fop.viewer.Translator (SecureResourceBundle)
     * @return An instance of XhibitPreviewDialog
     */
    protected PreviewDialog createPreviewDialog(AWTRenderer renderer) {
        XhibitPreviewDialog frame = new XhibitPreviewDialog(getFOUserAgent());
        frame.validate();

        // Set the window bounds to the last used value or center in default
        // display.
        // Store any changes to the window bounds on close
        WindowBoundsHelper.setBounds(frame, WindowBoundsHelper.PRINT_PREVIEW_FRAME_NAME, WindowBoundsHelper
                .getDefaultBounds(frame), true);

        frame.setVisible(true);
        return frame;
    }
    
    /**
     * Creates an instance of the preview dialog to control the printing process
     * The rendering is for AWT - this may be extended to PDF for furure
     * releases of Xhibit
     * 
     * @param renderer
     *            The AWTRenderer used to render the FO
     * @param res
     *            org.apache.fop.viewer.Translator (SecureResourceBundle)
     * @return An instance of XhibitPreviewDialog
     */
    protected PreviewDialog createPreviewDialog(AWTRenderer renderer, XAction action) {
        XhibitPreviewDialog frame = new XhibitPreviewDialog(getFOUserAgent(), action);
        frame.validate();

        // Set the window bounds to the last used value or center in default
        // display.
        // Store any changes to the window bounds on close
        WindowBoundsHelper.setBounds(frame, WindowBoundsHelper.PRINT_PREVIEW_FRAME_NAME, WindowBoundsHelper
                .getDefaultBounds(frame), true);

        frame.setVisible(true);
        return frame;
    }

    /**
     * Creates a document from the supplied FO
     * 
     * @param fo
     *            FO documment as a String
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
     * This method provides support for direct printing of an XSL-FO document.
     * 
     * @param xslFO
     *            the xsl-FO to be rendered.
     * @param preview
     *            Whether to display a print preview frame.
     * @param showDialog
     *            Ignored as this is actually handled by the preview pane
     * @throws CSRecoverableException
     */
    public void printDocument(String xslFO, boolean preview, boolean showDialog) throws CSRecoverableException {
        printDocument(xslFO, preview);
    }

    /**
     * Prints the document generated fro the supplied FO path. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer
     * 
     * @param fo
     *            FO document as a string
     * @param prev
     *            Is preview required
     * @throws CSRecoverableException
     */
    public void printDocument(String fo, boolean prev) throws CSRecoverableException {
        try {
            resetRenderer();

            frame = (XhibitPreviewDialog) createPreviewDialog(getRenderer());

            getRenderer().setStatusListener(frame); 

            // init parser
            frame.setStatus(getResourceBundle("fop.init") + " ...");

            loadTransform(fo);

            renderDocument();

        } catch (FOPException ex) {
            log.error("FOPException: transform problem", ex);
            throw new CSRecoverableException("printing.fop", "FOP Transform Exception", ex);
        } catch (IllegalArgumentException ex) {
            log.error("IllegalArgumentException: transform problem", ex);
            throw new CSRecoverableException("printing.other", "FOP Transform Exception", ex);
        }
        //
    }
    
    /**
     * Prints the document generated fro the supplied FO path. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer
     * 
     * @param fo
     *            FO document as a string
     * @param prev
     *            Is preview required
     * @param action           
     *            Action required
     * @throws CSRecoverableException
     */
    public void printDocument(String fo, boolean prev, XAction action) throws CSRecoverableException {
        try {
            resetRenderer();

            frame = (XhibitPreviewDialog) createPreviewDialog(getRenderer(), action);

            getRenderer().setStatusListener(frame);
            
            // init parser
            frame.setStatus(getResourceBundle("fop.init") + " ...");

            loadTransform(fo);

            renderDocument();

        } catch (FOPException ex) {
            log.error("FOPException: transform problem", ex);
            throw new CSRecoverableException("printing.fop", "FOP Transform Exception", ex);
        } catch (IllegalArgumentException ex) {
            log.error("IllegalArgumentException: transform problem", ex);
            throw new CSRecoverableException("printing.other", "FOP Transform Exception", ex);
        }
        //
    }
    
    /**
     * Prints the document generated fro the supplied FO path. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer
     * 
     * @param fo
     * @param prev
     * @param title
     * @throws CSRecoverableException
     */
    public void printDocument(String fo, boolean prev, String title) throws CSRecoverableException {
        printDocument(fo, prev);
        frame.setTitle(title);
    }
    
    /**
     * Prints the document generated from the supplied FO path. If preview is
     * specified, the AWT preview dialog will be displayed, otherwise the
     * document will be sent directly to the default printer
     * 
     * @param fo
     * @param prev
     * @param title
     * @param action
     * @throws CSRecoverableException
     */
    public void printDocument(String fo, boolean prev, String title, XAction action) throws CSRecoverableException {
        printDocument(fo, prev, action);
        frame.setTitle(title);
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
        resetRenderer();

        setupTransformer(xslIs);

        frame = (XhibitPreviewDialog) createPreviewDialog(getRenderer());

        getRenderer().setStatusListener(frame);

        transformAndRender(xmlIs);
    }

    /**
     * Transform the input and Render the document
     * 
     * @param xmlIs
     *            the InputStream
     * @throws CSRecoverableException
     */
    private void transformAndRender(InputStream xmlIs) throws CSRecoverableException {
        try {
            loadTransform(xmlIs);

            resetDriverAndRender();
        } catch (FOPException fe) {
            throw new CSRecoverableException("printing.fop", "FOP Exception while transforming document", fe);
        }
    }

    /**
     * Reset the Driver after Transform and Render the document
     * 
     * @throws FOPException
     */
    private void resetDriverAndRender() throws FOPException {
        
    	resetFop();

        renderDocument();
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
        resetRenderer();

        setupTransformer(xslIs);

        frame = (XhibitPreviewDialog) createPreviewDialog(getRenderer());

        getRenderer().setStatusListener(frame);

        transformAndRender(xmlIs);
    }

    /**
     * Transform the input and Render the document
     * 
     * @param xmlIs
     *            the XML
     * @throws CSRecoverableException
     */
    private void transformAndRender(String xmlIs) throws CSRecoverableException {
        try {
            loadTransform(xmlIs);

            resetDriverAndRender();
        } catch (FOPException fe) {
            throw new CSRecoverableException("printing.fop", "FOP Exception while transforming document", fe);
        }
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
            throw new CSRecoverableException("printing.fop", "Transformer Exception while transforming document", te);
        }
    }

    /**
     * Constructor using a Document object and streamed XSL
     * 
     * @param xmlIs
     *            InputStream - streamed XSL file
     * @param xslIs
     *            InputStream - streamed XSL file
     * @param title
     *            The title of the preview dialog
     * @throws CSRecoverableException
     */
    public void printDocument(InputStream xmlIs, InputStream xslIs, String title) throws CSRecoverableException {
        printDocument(xmlIs, xslIs);

        frame.setTitle(title);
    }

    /**
     * Renders the document to the page
     * 
     * @throws FOPException
     */
    private void renderDocument() throws FOPException {
        try {
            frame.setStatus(getResourceBundle("fop.show"));
            frame.goToFirstPage();

        } catch (Exception e) {
            frame.reportException(e);
            if (e instanceof FOPException) {
                throw (FOPException) e;
            }
            throw new FOPException(e);
        }
    }

    /**
     * Get the frame of the preview pane
     * 
     * @return the JFrame
     */
    public JFrame getFrame() {
        return frame;
    }
}