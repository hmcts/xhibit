package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.print.helper.PrintPreviewHelper;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 Print
 * </p>
 * <p>
 * Description: Prints the current active panel
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

public class PrintAction extends SynchXAction {
    private String[] xslFOStrings;

    public PrintAction() {
        populateFromBundle("Print");
    }

    /**
     * If extending the print action for dialogs etc, override the
     * preSynchActionPerformed method and call this method with the strings that
     * you wish to be printed.
     * 
     * @param xslFoStrings
     */
    protected void setXslFoStrings(String[] xslFoStrings) {
        this.xslFOStrings = xslFoStrings;
    }

    protected void print(boolean preview, boolean showDialog) throws CSRecoverableException {
        if (xslFOStrings == null || xslFOStrings.length == 0) {
            String errMsg = "An error occurred whilst formatting the data for print. "
                    + " xslFOStrings returned null or is zero length!";
            throw new CSRecoverableException("gui.printaction.format", errMsg);
        } else {
            for (int i = 0; i < xslFOStrings.length; i++) {
                String xslFOString = xslFOStrings[i];

                try {
                    if (xslFOString == null || xslFOString.trim().length() == 0) {
                        String errMsg = "An error occurred whilst formatting the data for print. "
                                + " xslFOString returned null or blank!";
                        throw new CSRecoverableException("gui.printaction.format", errMsg);
                    }

                    try {
                        FOPInterface fop = FOPFactory.getFOPRenderer(preview);
                        fop.printDocument(xslFOString, preview, showDialog);

                        if (fop instanceof PrintPreviewHelper) {
                            ((PrintPreviewHelper) fop).getFrame().addWindowListener(new WindowAdapter() {
                                public void windowClosed(WindowEvent e) {
                                    giveControllerFocus();
                                }
                            });
                        }
                    } catch (Exception e) {
                        Object[] params = null;
                        CSRecoverableException csre = new CSRecoverableException("gui.printaction.print", params,
                                "An error occurred whilst printing.", e);
                        throw csre;
                    }
                } catch (UserCancelException uce) {
                    // user has clicked cancel in, for example a print
                    // dialog.
                    return;
                } catch (ClassCastException cce) {
                    Object[] params = null;
                    CSRecoverableException csre = new CSRecoverableException("gui.printaction.format", params,
                            "An error occurred whilst formatting the data for print.", cce);
                    throw csre;
                }
            }
        }
        if (!preview)
            giveControllerFocus();
    }

    private void giveControllerFocus() {
        if (getController() != null && getController() instanceof XhibitApplicationController) {
            // ((XhibitApplicationController)getController()).requestFocus();
            SwingUtilities.invokeLater(new BringToFront((XhibitApplicationController) getController()));
        }
    }

    public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        setXslFoStrings(((PrintFunction) ((XhibitApplicationController) getController()).getBodyPanel()).print());
    }

    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        print(false, true);
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        // if (!preview) giveControllerFocus();
    }

    public class BringToFront implements Runnable {
        XhibitApplicationController _xac;

        public BringToFront(XhibitApplicationController xac) {
            _xac = xac;
        }

        public void run() {
            _xac.toFront();
            _xac.requestFocus();
        }
    }

    public void setEnabled(boolean newValue) {
        if (this.getClass().equals(PrintAction.class)) {
            getPrintToolbarAction().setEnabled(newValue);
        }
        super.setEnabled(newValue);
    }

    private XAction printToolbarAction = null;

    public XAction getPrintToolbarAction() {
        if (printToolbarAction == null) {
            printToolbarAction = XhibitActions.getAction((XhibitApplicationController) getController(),
                    XhibitActions.PrintToolbar);
        }
        return printToolbarAction;
    }

}