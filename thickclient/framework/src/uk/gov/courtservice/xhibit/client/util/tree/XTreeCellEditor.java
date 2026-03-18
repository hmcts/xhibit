package uk.gov.courtservice.xhibit.client.util.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

/**
 * X Tree cell rendererer inhances basic editor by allowing nodes to provide
 * there own icons
 * 
 * @author William Fardell
 * @version $Revision: 1.4 $
 */

public class XTreeCellEditor extends DefaultTreeCellEditor {

    /**
     * Constructs a <code>XTreeCellEditor</code> object for a JTree using the
     * specified renderer and a default editor. (Use this constructor for normal
     * editing.)
     * 
     * @param tree
     *            a <code>JTree</code> object
     * @param renderer
     *            a <code>DefaultTreeCellRenderer</code> object
     */
    public XTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    /**
     * Constructs a <code>XTreeCellEditor</code> object for a
     * <code>JTree</code> using the specified renderer and the specified
     * editor. (Use this constructor for specialized editing.)
     * 
     * @param tree
     *            a <code>JTree</code> object
     * @param renderer
     *            a <code>DefaultTreeCellRenderer</code> object
     * @param editor
     *            a <code>TreeCellEditor</code> object
     */
    public XTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer, TreeCellEditor editor) {
        super(tree, renderer, editor);
    }

    /**
     * Yes really this is where the icon is set
     */
    protected void determineOffset(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        if (renderer instanceof XTreeCellRenderer) {
            // This aproach was taken as the super method has many side
            // effects, if the implementation changes
            // in a future implementation this is less likly to break.
            ((XTreeCellRenderer) renderer).setInfo(tree.getModel(), value);
            super.determineOffset(tree, value, isSelected, expanded, leaf, row);
            ((XTreeCellRenderer) renderer).clearInfo();
        } else {
            super.determineOffset(tree, value, isSelected, expanded, leaf, row);
        }
    }
}
