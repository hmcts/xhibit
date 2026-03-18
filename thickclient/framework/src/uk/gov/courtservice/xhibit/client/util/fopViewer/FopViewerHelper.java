package uk.gov.courtservice.xhibit.client.util.fopViewer;

import org.apache.fop.apps.FOPException;
import org.apache.fop.render.awt.AWTRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.print.helper.AbstractPrintHelper;

/**
 * <p>
 * Title: FOP Viewer Helper
 * </p>
 * <p>
 * Description: Generates a panel that can be used to display FOP documents.
 * Includes a Print button for creating hard copies.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: FopViewerHelper.java,v 1.6 2009/03/18 10:06:59 powellja Exp $
 */
public class FopViewerHelper extends AbstractPrintHelper {
    public static final String DISPLAY_MODE = "display";

    private static FopPanel _frame;

    /**
     * Constructor using an OrderData object and streamed XSL
     */
    public FopViewerHelper() {
        super();
        _frame = createPreviewPanel();
    }

    protected void print() throws CSRecoverableException {
        super.print();
    }

    /**
     * Creates an OrderPreviewPanel containing the AWT rendering of the FOP
     * document
     * 
     * @return OrderPreviewPanel
     */
    protected FopPanel createPreviewPanel() {
        FopPanel frame = new FopPanel(this);
        frame.validate();

        return frame;
    }

    public void showFop(String fop) throws FOPException {
        resetRenderer();
        resetFop();
        loadTransform(fop);
        _frame.showPage();
    }

    /**
     * Returns the OrderPreviewPanel as a JPanel
     * 
     * @return JPanel
     */
    public FopPanel getDisplayPanel() {
        return _frame;
    }

    /**
     * Resets the superclass renderer
     * 
     * @param rend
     */
    public void refreshRenderer(AWTRenderer rend) {
        setRenderer(rend);
    }
    
    /**
     * Selects the first page
     */
    public void setFirstPage(){
        _frame.setFirstPage();
    }

    protected AWTRenderer getRenderer() {
        return super.getRenderer();
    }
}
