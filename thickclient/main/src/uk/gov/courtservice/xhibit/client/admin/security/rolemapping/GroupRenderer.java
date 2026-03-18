package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

import java.awt.Component;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

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

public class GroupRenderer extends DefaultTreeCellRenderer {
    ResourceBundle securityBundle;

    public GroupRenderer() {
        securityBundle = ResourceBundleHelper.getResourceBundle(XhibitBundles.SecurityRoles);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        try {
            String text = securityBundle.getString(value.toString().replace(' ', '_'));
            this.setText(text);
        } catch (MissingResourceException ex) {
            // If text not found, then just display whatever is already
            // there.
        }
        return this;
    }
}