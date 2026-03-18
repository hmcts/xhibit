package uk.gov.courtservice.xhibit.client.util.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 * X Tree cell rendererer inhances basic renderee by allowing nodes to provide
 * there own icons
 * 
 * @author William Fardell
 * @version $Revision: 1.3 $
 */

public class XTreeCellRenderer extends DefaultTreeCellRenderer {

    /**
     * These are only valid for the duration of the call to
     * getTreeCellRendererComponent
     */
    private volatile XTreeModel model;

    private volatile Object node;

    /**
     * Set the icon node info
     */
    protected void setInfo(TreeModel newModel, Object newNode) {
        model = newModel instanceof XTreeModel ? (XTreeModel) newModel : null;
        node = newNode;
    }

    /**
     * Set the icon node info
     */
    protected void clearInfo() {
        model = null;
        node = null;

    }

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded.
     */
    public Icon getOpenIcon() {
        if (model != null && node != null) {
            Icon openIcon = model.getOpenIcon(node);
            if (openIcon != null) {
                return openIcon;
            }
        }
        return super.getOpenIcon();
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are not expanded.
     */
    public Icon getClosedIcon() {
        if (model != null && node != null) {
            Icon closedIcon = model.getClosedIcon(node);
            if (closedIcon != null) {
                return closedIcon;
            }
        }
        return super.getClosedIcon();
    }

    /**
     * Returns the icon used to represent leaf nodes.
     */
    public Icon getLeafIcon() {
        if (model != null && node != null) {
            Icon leafIcon = model.getLeafIcon(node);
            if (leafIcon != null) {
                return leafIcon;
            }
        }
        return super.getLeafIcon();
    }

    /**
     * Configures the renderer based on the passed in components. The value is
     * set from messaging the tree with <code>convertValueToText</code>,
     * which ultimately invokes <code>toString</code> on <code>value</code>.
     * The foreground color is set based on the selection and the icon is set
     * based on on leaf and expanded.
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        // This aproach was taken as the super method has many side effects, if
        // the implementation changes
        // in a future implementation this is less likly to break.
        setInfo(tree.getModel(), value);
        setToolTipText(getToolTipText(tree.getModel(), value));
        Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        clearInfo();
        return component;
    }

    private static String getToolTipText(TreeModel model, Object node) {
        if (model instanceof XTreeModel && node != null) {
            String text = ((XTreeModel) model).getTooltipText(node);
            if (text != null) {
                return text;
            }
        }
        return null;
    }

}
