package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.Collection;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: A dialog box that give the user a drop down list to select from.
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

public class SingleItemSelectionDialog extends XDialog {
    SingleItemSelectionPanel xsp;

    /**
     * Show a dialog which prompts the user to pick and item from a list.
     * 
     * @param itemList
     * @param strTitle
     * @param helpText
     */
    public SingleItemSelectionDialog(Frame parent, Collection itemList, String strTitle, String helpText) {
        this(parent, itemList, null, strTitle, helpText);
    }

    public SingleItemSelectionDialog(Dialog parent, Collection itemList, String strTitle, String helpText) {
        this(parent, itemList, null, strTitle, helpText);
    }

    /**
     * Show a dialog which prompts the user to pick and item from a list, with a
     * default pre-selected.
     * 
     * @param itemList
     * @param defaultValue
     * @param strTitle
     * @param helpText
     */
    public SingleItemSelectionDialog(Frame parent, Collection itemList, Object defaultValue, String strTitle,
            String helpText) {
        super(parent, strTitle, true);
        init(itemList, defaultValue, helpText);
    }

    public SingleItemSelectionDialog(Dialog parent, Collection itemList, Object defaultValue, String strTitle,
            String helpText) {
        super(parent, strTitle, true);
        init(itemList, defaultValue, helpText);
    }

    void init(Collection itemList, Object defaultValue, String helpText) {
        xsp = new SingleItemSelectionPanel(this, itemList, defaultValue, helpText);
        addBodyPanel(xsp);
        pack();
    }

    /**
     * Get the item selected in the combo box
     * 
     * @return an object if anything is selected, otherwise null
     */
    public Object getSelectedItem() {
        return xsp.getSelectedItem();
    }

    /**
     * 
     * @param newValue
     */
    public void setRequired(boolean newValue) {
        xsp.setRequired(newValue);
    }
}