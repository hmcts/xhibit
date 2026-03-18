package uk.gov.courtservice.xhibit.client.print.viewer;

import java.awt.Dialog.ModalExclusionType;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.awt.viewer.PreviewDialog;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XhibitPreviewDialog
 * </p>
 * <p>
 * Description: Subclass of org.apachefop.viewer.PreviewDialog. Reload option
 * removed from toolbar and menu and Help option removed from menu. Mnemonics
 * added for File(f) and View(V) options on menu
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
public class XhibitPreviewDialog extends PreviewDialog {

    protected Logger log = CSServices.getLogger(XhibitPreviewDialog.class);
    
    private final String icon = XHIBITConstant.imageRoot + "XhibitCornerLogo.gif";

    /**
     * Constructor - creates the print preview dilaog for Xhibit
     * 
     * @param aWTRenderer
     *            <code>AWTRenderer</code> for preview
     * @param translator
     *            <code>Translator</code> holding FOP resources
     */
    public XhibitPreviewDialog(FOUserAgent foUserAgent) {
        super(foUserAgent);
        setLookAndFeel();
        setUpXhibitDialog();
        
        super.setIconImage(getIcon().getImage());
    }
    
    /**
     * Constructor - creates the print preview dilaog for Xhibit
     * 
     * @param aWTRenderer
     *            <code>AWTRenderer</code> for preview
     * @param translator
     *            <code>Translator</code> holding FOP resources
     */
    public XhibitPreviewDialog(FOUserAgent foUserAgent, XAction action) {
        super(foUserAgent);
        setLookAndFeel();
        setUpXhibitDialog(action);
        
        super.setIconImage(getIcon().getImage());
    }

    /**
     * The default <code>Previewdialog</code> includes menu and toolbar
     * options that are not required for Xhibit. The <i>Reload</i> option does
     * not actually work! These unrequired options are removed.
     */
    private void setUpXhibitDialog(XAction action) {
        if (action != null) {
        	// Add the action to the menu (replaces reload)
        	this.getJMenuBar().getMenu(0).add(new JMenuItem(action),1);
        } else {
            // Remove reload option from menu
        	this.getJMenuBar().getMenu(0).remove(1);
        }
        // Remove help option from menu
        this.getJMenuBar().remove(2);
        // Set mnemonic for File option on menu to F
        this.getJMenuBar().getMenu(0).setMnemonic('F');
        // Set mnemonic for View option on menu to V
        this.getJMenuBar().getMenu(1).setMnemonic('V');
        // System.out.println(this.getJMenuBar().getMenu(0).getMenuComponent(1).toString());
        this.setTitle("Xhibit Print Preview");
    }
    
    /**
     * The default <code>Previewdialog</code> includes menu and toolbar
     * options that are not required for Xhibit. The <i>Reload</i> option does
     * not actually work! These unrequired options are removed.
     */
    private void setUpXhibitDialog() {
    	setUpXhibitDialog(null);
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
        	this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            logLookAndFeelError(ex);
        } catch (InstantiationException ex) {
            logLookAndFeelError(ex);
        } catch (IllegalAccessException ex) {
            logLookAndFeelError(ex);
        } catch (UnsupportedLookAndFeelException ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        log.error("XhibitPreviewDialog: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                + ex.getMessage());

    }

    public void startPrinterJob(boolean showDialog) {
    	super.startPrinterJob(showDialog);
    	print();
    }
    
    protected void print() {
        // Override method to allow for record updates upon print
    }

    private ImageIcon getIcon() {
        java.net.URL url = getClass().getClassLoader().getResource(icon);
        ImageIcon i = null;

        if (url != null)
            i = new ImageIcon(url);
        else
            i = new ImageIcon(icon);
        return i;
    }
}