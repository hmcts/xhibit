package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Used to cut text onto
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

public class CutAction extends XAction {

    private static CutAction pa = null;

    private static final Logger log = CSServices.getLogger(CutAction.class);

    private CutAction() {
        populateFromBundle("Cut");
        setMnemonicKeyFromBundle("Cut");
        setIcon(XHIBITConstant.imageRoot + "cut.gif");
        setAccelaratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    }

    public static CutAction getInstance() {
        if (pa == null)
            pa = new CutAction();
        return pa;
    }

    public static AbstractAction getInstance(Object controller) {
        AbstractAction aa = getInstance();
        ((XAction) aa).setController(controller);
        return aa;
    }

    public void checkState() {
        JTextComponent jt;
        if (getModel() != null) {
            try {
                if (getModel() instanceof JTextComponent) {
                    jt = (JTextComponent) getModel();
                    if (jt.isEditable() && jt.isEnabled()) {
                        String s = jt.getSelectedText();
                        if (s != null) {
                            if (s.length() > 0) {
                                this.setEnabled(true);
                            } else {
                                this.setEnabled(false);
                            }
                        } else
                            this.setEnabled(false);
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
                // Not a text component. Other components not supported yet.
                // Send getModel().toString() with the message so offending
                // component can be identified.
                // This should be logged in back-end
                this.setEnabled(false);
            }
        } else {
            this.setEnabled(false);
        }
    }

    private void setClipboardContents(String newContents) {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (c != null) {
            StringSelection t = new StringSelection(newContents);
            c.setContents(t, t);
        }
    }

    public void xActionPerformed(ActionEvent e) {
        JTextComponent jt;
        if (getModel() != null) {
            try {
                if (getModel() instanceof JTextComponent) {
                    jt = (JTextComponent) getModel();
                    String s = jt.getSelectedText();
                    if (s != null) {
                        jt.cut();
                        jt.requestFocus();
                        this.checkState();
                        CopyAction.getInstance().checkState();
                    }
                } else if (getModel() instanceof JTable) {
                    JTable jTable = (JTable) getModel();
                    int tableRow = jTable.getSelectedRow();
                    int tableCol = jTable.getSelectedColumn();

                    if (tableRow >= 0 || tableCol >= 0) {
                        if (CCPHelper.isSelectionEdittable(jTable)) {
                            String toCut = CCPHelper.getCellContents(jTable);
                            setClipboardContents(toCut);
                            CCPHelper.setSelectionValue(new String(""), jTable);
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