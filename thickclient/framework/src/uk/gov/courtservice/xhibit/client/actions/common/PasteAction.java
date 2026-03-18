package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Used to paste text onto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class PasteAction extends XAction {

    private static PasteAction pa = null;

    private static final Logger log = CSServices.getLogger(PasteAction.class);

    private PasteAction() {
        populateFromBundle("Paste");
        setIcon(XHIBITConstant.imageRoot + "paste.gif");
        setAccelaratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        setMnemonicKeyFromBundle("Paste");
    }

    public static PasteAction getInstance() {
        if (pa == null)
            pa = new PasteAction();
        return pa;
    }

    public static PasteAction getInstance(Object controller) {
        PasteAction aa = getInstance();
        aa.setController(controller);
        return aa;
    }

    private String getClipboardContents() throws CSBusinessException {
        try {
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            if (c != null) {
                Transferable t = c.getContents(this);
                if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    String clipText = (String) t.getTransferData(DataFlavor.stringFlavor);
                    return clipText;
                } else {
                    return null;
                }
            } else
                return null;
        } catch (IOException ex) {
            // Clipboard can not be accessed
            log.fatal(ex);
            throw new CSUnrecoverableException(ex);
        } catch (UnsupportedFlavorException ex) {
            log.fatal(ex);
            // Clipboard contents are not a string
            throw new CSUnrecoverableException(ex);
        }
    }

    public void checkState() {
        JTextComponent jt;
        if (getModel() != null) {
            try {
                if (getModel() instanceof JTextComponent) {
                    jt = (JTextComponent) getModel();
                    if (jt.isEditable() && jt.isEnabled()) {
                        String s = getClipboardContents();
                        if ((s != null) && (s.length() > 0)) {
                            this.setEnabled(true);
                        } else {
                            this.setEnabled(false);
                        }
                    } else {
                        this.setEnabled(false);
                    }
                } else if (getModel() instanceof JTable) {
                    JTable jTable = (JTable) getModel();
                    this.setEnabled(CCPHelper.isSelectionEdittable((JTable) getModel()));
                } else {
                    this.setEnabled(false);
                }
            } catch (ClassCastException ex) {
                this.setEnabled(false);
                // Not a text component.
                // Other components not supported yet.
                // This should be logged in back-end
            } catch (CSBusinessException be) {
                // Clipboard contents not supported.
                // No need to report.
                this.setEnabled(false);
            }
        } else {
            this.setEnabled(false);
        }
    }

    public void xActionPerformed(ActionEvent e) {
        JTextComponent jt;

        if (getModel() != null) {
            try {

                if (getModel() instanceof JTextComponent) {
                    jt = (JTextComponent) getModel();
                    jt.paste();
                    jt.requestFocus();
                } else if (getModel() instanceof JTable) {
                    JTable jTable = (JTable) getModel();
                    int tableRow = jTable.getSelectedRow();
                    int tableCol = jTable.getSelectedColumn();

                    if (tableRow >= 0 || tableCol >= 0) {
                        if (CCPHelper.isSelectionEdittable(jTable)) {
                            try {
                                String toPaste = getClipboardContents();
                                CCPHelper.setSelectionValue(toPaste, jTable);
                            } catch (CSBusinessException ex) {
                                Toolkit.getDefaultToolkit().beep();
                            }
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }

            } catch (ClassCastException ex) {
                log.info("Not a text component, other components not supported yet, send getModel().toString() with"
                        + " the message so offending component can be identified, this should be logged in back-end",
                        ex);
            }
        }
    }
}