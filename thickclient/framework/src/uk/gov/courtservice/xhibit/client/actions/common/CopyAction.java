package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Used to copy text onto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @author Sarah Tong
 * @version $Id: CopyAction.java,v 1.3 2006/06/05 12:30:28 bzjrnl Exp $
 */

public class CopyAction extends XAction {

    private static CopyAction ca = null;

    private Clipboard system;

    private StringSelection stsel;

    private CopyAction() {
        populateFromBundle("Copy");
        setMnemonicKeyFromBundle("Copy");
        setIcon(XHIBITConstant.imageRoot + "copy.gif");
        setAccelaratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    }

    public static CopyAction getInstance() {
        if (ca == null)
            ca = new CopyAction();
        return ca;
    }

    public static CopyAction getInstance(Object controller) {
        CopyAction aa = getInstance();
        aa.setController(controller);
        return aa;
    }

    public void checkState() {
        JTextComponent jt;
        JTable jTable;

        if (getModel() != null) {
            if (getModel() instanceof JTextComponent) {
                jt = (JTextComponent) getModel();
                if (jt.isEnabled()) {
                    String s = jt.getSelectedText();
                    if (s != null) {
                        if (s.length() > 0) {
                            this.setEnabled(true);
                        } else {
                            this.setEnabled(false);
                        }
                    } else
                        this.setEnabled(false);
                } else
                    this.setEnabled(false);
            } else if (getModel() instanceof JTable) {
                jTable = (JTable) getModel();

                // check we have selected only a contiguous block of cells
                int numcols = jTable.getSelectedColumnCount();
                int numrows = jTable.getSelectedRowCount();
                int[] rowsselected = jTable.getSelectedRows();
                int[] colsselected = jTable.getSelectedColumns();
                if (rowsselected.length > 0
                        && colsselected.length > 0
                        && !((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0] && numrows == rowsselected.length) && (numcols - 1 == colsselected[colsselected.length - 1]
                                - colsselected[0] && numcols == colsselected.length)))
                    this.setEnabled(false);
                else {
                    if (CCPHelper.getCellContents(jTable).length() > 0)
                        this.setEnabled(true);
                    else
                        this.setEnabled(false);
                }
            } else {
                // Not a text or JTable component. Other components not
                // supported yet.
                this.setEnabled(false);
            }
        } else {
            this.setEnabled(false);
        }
    }

    public void xActionPerformed(ActionEvent e) {
        JTextComponent jt;
        JTable jTable;
        if (getModel() != null) {
            if (getModel() instanceof JTextComponent) {
                jt = (JTextComponent) getModel();
                String s = jt.getSelectedText();
                if (s != null) {
                    jt.copy();
                    jt.requestFocus();
                }
            } else if (getModel() instanceof JTable) {
                jTable = (JTable) getModel();

                stsel = new StringSelection(CCPHelper.getCellContents(jTable));
                system = Toolkit.getDefaultToolkit().getSystemClipboard();
                system.setContents(stsel, stsel);
            } else {
                // Not a text or JTable component. Other components not
                // supported yet.
                // Send getModel().toString() with the message so offending
                // component can be identified.
                // This should be logged in back-end
            }
        }
    }

}