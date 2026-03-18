package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.security.SecurityHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.1 - FV reworked resource handling and error reporting
 * @Editors: Frederik Vandendriessche
 */

public abstract class XAction extends AbstractAction {
    private Object caller;

    private Object objModel;

    private Object objController;

    private ResourceBundle actionResource = null;

    public static boolean internalDebug;

    private static final Logger log = CSServices.getLogger(XAction.class);

    public abstract void xActionPerformed(ActionEvent e) throws Exception;

    /**
     * Construct a blank action
     */
    public XAction() {
    }

    /**
     * Construct an action populating it from the bundle
     */
    public XAction(String actionName) {
        populateFromBundle(actionName);
    }

    public void actionPerformed(ActionEvent actionEvent) {

        Window jw = null;
        try {
            if (actionEvent.getSource() != null) {
                if (actionEvent.getSource() instanceof Component) {
                    jw = XSwingUtilities.getWindowAncestor((Component) actionEvent.getSource());
                }
                if (jw != null) {
                    jw.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    jw.validate();
                }
            }
            this.xActionPerformed(actionEvent);
        } catch (UserCancelException uce) {
            // Do nothing.
            log.info(uce);
        } catch (Exception xActionPerformException) {
            XHIBITConstant.handleError(xActionPerformException, this, actionEvent);
            // the cursor should still be in wait
        } finally {
            try {
                if (jw != null) {
                    jw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            } catch (Exception exceptionWhilstResettingCursor) {
                XHIBITConstant
                        .debug("Exception whilst getting XhibitApplicationController ref to set a Default Cursor.");
            }
        }
    }

    public Object getCaller() {
        return this.caller;
    }

    public void setCaller(Object o) {
        this.caller = o;
    }

    /**
     * Gets the name, short description and long description from the resource
     * bundle and sets them for this action. Also get the mnemonic and icon if
     * they exist. If the icon is not found then use the default icon spacer.
     * 
     * @param actionName
     *            key to resource bundle.
     */
    public void populateFromBundle(String actionName) {
        setName(XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, actionName + "Name"));
        setShortDescription(XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, actionName + "ShortDesc"));
        setLongDescription(XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, actionName + "LongDesc"));
        String mnemonic = null;
        try {
            mnemonic = getBundle().getString(actionName + "Mnemonic");
            setMnemonicKey(mnemonic.charAt(0));
        } catch (MissingResourceException ex) { // No mnemonic to set
            log.warn(ex);
        }
        try {
            String icon = getBundle().getString(actionName + "Icon");
            setIcon(XHIBITConstant.imageRoot + icon);
        } catch (MissingResourceException ex) { // No Icon to set
            log.warn(ex);
        }
    }

    protected ResourceBundle getBundle() {
        if (actionResource == null) {
            actionResource = XHIBITConstant.getResourceBundle(XhibitBundles.XhibitActionResources);
        }
        return actionResource;
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

    /**
     * Sets the icon for the action.
     * 
     * @param icon
     *            path or url where the icon image can be found.
     */
    public void setIcon(String icon) {
        if (icon == null || icon.trim().length() == 0) {
            setSmallIcon(null);
        } else {
            URL url = getClass().getClassLoader().getResource(icon);
            ImageIcon i = null;

            if (url != null) {
                i = new ImageIcon(url);
            } else {
                i = new ImageIcon(icon);
            }
            setSmallIcon(i);
        }
    }

    public void setSmallIcon(ImageIcon icon) {
        putValue(Action.SMALL_ICON, icon);
    }

    public ImageIcon getIcon() {
        ImageIcon ico;
        try {
            ico = (ImageIcon) getValue(Action.SMALL_ICON);
        } catch (ClassCastException ex) {
            return null;
        }
        return ico;
    }

    public void setName(String name) {
        putValue(Action.NAME, name);
    }

    public String getName() {
        return getStringValue(Action.NAME);
    }

    public void setShortDescription(String shortDescription) {
        putValue(Action.SHORT_DESCRIPTION, shortDescription);
    }

    public String getShortDescription() {
        return getStringValue(Action.SHORT_DESCRIPTION);
    }

    public void setLongDescription(String longDescription) {
        putValue(Action.LONG_DESCRIPTION, longDescription);
    }

    public String getLongDescription() {
        return getStringValue(Action.LONG_DESCRIPTION);
    }

    public void setAccelaratorKey(KeyStroke accelaratorKey) {
        putValue(Action.ACCELERATOR_KEY, accelaratorKey);
    }

    public KeyStroke getAccelaratorKey() {
        KeyStroke accelaratorKey;
        try {
            accelaratorKey = (KeyStroke) getValue(Action.ACCELERATOR_KEY);
        } catch (ClassCastException ex) {
            return null;
        }
        return accelaratorKey;
    }

    public void setMnemonicKey(int mnemonicKey) {
        setMnemonicKey(new Integer(mnemonicKey));
    }

    public void setMnemonicKey(char mnemonicKey) {
        setMnemonicKey((int) mnemonicKey);
    }

    public void setMnemonicKey(Integer mnemonicKey) {
        putValue(Action.MNEMONIC_KEY, mnemonicKey);
    }

    public void setMnemonicKeyFromBundle(String mnemonicProperty) {
        setMnemonicKey(XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, mnemonicProperty + "Mnemonic")
                .charAt(0));
    }

    public Integer getMnemonicKey() {
        Integer mnemonicKey;
        try {
            mnemonicKey = (Integer) getValue(Action.MNEMONIC_KEY);
        } catch (ClassCastException ex) {
            return null;
        }
        return mnemonicKey;
    }

    public void setModel(Object model) {
        // if (model != null)
        // {
        // XHIBITConstant.debug("XAction.name = " + this.getName());
        // XHIBITConstant.debug("XAction.shortDescription = " +
        // this.getShortDescription());
        // XHIBITConstant.debug("XAction.setModel("+ model.getClass()+")");
        // }
        // else
        // {
        // XHIBITConstant.debug("XAction model being set to null?!");
        // }
        this.objModel = model;
    }

    public Object getModel() {
        return objModel;
    }

    public void setController(Object controller) {
        this.objController = controller;
    }

    public Object getController() {
        return objController;
    }

    // Security

    /**
     * Does the user have rights to view the page. This is used mainly by the
     * menus to decide whether to display the menu item or not.
     * 
     * @return
     */
    public boolean hasReadAccess() {
        return SecurityHelper.hasReadAccess(this.getClass());
    }

    /**
     * Does user have access to edit the page. This includes whether the user
     * can access the page. For example View Todays Schedule. This is used
     * mainly by the enable method to decide if the action is allowed to be
     * enabled.
     * 
     * @return
     */
    public boolean hasEditAccess() {
        return SecurityHelper.hasEditAccess(this.getClass());
    }

    /**
     * Overridden method that checks if the user has access to the action before
     * it is enabled.
     * 
     * @param newValue
     */
    public void setEnabled(boolean newValue) {
        if (hasEditAccess()) {
            super.setEnabled(newValue);
        } else {
            super.setEnabled(false);
        }
    }

}