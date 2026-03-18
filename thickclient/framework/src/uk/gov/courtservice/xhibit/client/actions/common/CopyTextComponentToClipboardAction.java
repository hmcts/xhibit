package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CopyTextComponentToClipboardAction extends AbstractAction {

    private boolean selectAllIfNoSelection = false;

    private Object model = null;

    public CopyTextComponentToClipboardAction() {

    }

    public void setSelectAllIfNoSelection(boolean b) {
        selectAllIfNoSelection = b;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JTextComponent jt;
        if (getModel() != null) {
            try {
                jt = (JTextComponent) getModel();
                String s = jt.getSelectedText();
                if (s != null) {

                    jt.copy();

                    XHIBITConstant.debug("copied pre selected text into clipboard");
                } else {
                    if (selectAllIfNoSelection) {
                        jt.selectAll();
                        s = jt.getSelectedText();
                        if (s != null) {
                            jt.copy();
                            jt.requestFocus();
                            XHIBITConstant.debug("copied all selected text into clipboard");
                        } else {
                            XHIBITConstant.debug("failed to copy any text into clipboard");
                        }
                    }
                }
            } catch (Exception e) {
                XHIBITConstant.debug("CopyTextComponentToClipboardAction: exception");
                XHIBITConstant.error(e);
            }
        } else {
            XHIBITConstant.debug("CopyTextComponentToClipboardAction: model = null");
        }
    }

    public void setModel(Object o) {
        this.model = o;
    }

    public Object getModel() {
        return this.model;
    }

    public void setName(String name) {
        putValue(Action.NAME, name);
    }

    public String getName() {
        return getStringValue(Action.NAME);
    }

    private String getStringValue(String item) {
        String s;
        try {
            s = (String) getValue(item);
        } catch (ClassCastException ex) {
            return null;
        }
        return s;
    }

}