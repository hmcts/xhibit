package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import uk.gov.courtservice.xhibit.client.util.tree.XTreeCellEditor;
import uk.gov.courtservice.xhibit.client.util.tree.XTreeCellRenderer;
import uk.gov.courtservice.xhibit.client.util.tree.XTreeModel;

/**
 * Base Tree implementation that is 'aware' of X enhancements to the basic tree
 * 
 * @author William Fardell
 * @version $Revision: 1.6 $
 */
public class XTree extends JTree implements MouseListener, KeyListener {

    /**
     * The standard expand menu item
     */
    private final JMenuItem expandMenuItem = createMenuItem("tree.menu.expand", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            expandPath();
        }
    });

    /**
     * The standard expand all menu item
     */
    private final JMenuItem expandAllMenuItem = createMenuItem("tree.menu.expandall", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            expandAllPath();
        }
    });

    /**
     * The standard collapse menu item
     */
    private final JMenuItem collapseMenuItem = createMenuItem("tree.menu.collapse", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            collapsePath();
        }
    });

    /**
     * The standard collapse all menu item
     */
    private final JMenuItem collapseAllMenuItem = createMenuItem("tree.menu.collapseall", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            collapseAllPath();
        }
    });

    /**
     * True if the popup has been pressed
     */
    private boolean mousePressed = false;

    /**
     * True if the popup should be shown
     */
    private boolean mouseArmed = false;

    /**
     * List of menu items used for the pop up
     */
    private List popupMenuItems = null;

    /**
     * Cache the default renderer if required!
     */
    private XTreeCellRenderer defaultCellRenderer;

    /**
     * Cache the default editor if required!
     */
    private XTreeCellEditor defaultCellEditor;

    /**
     * Construct an XTree around the given model
     */
    public XTree(TreeModel model) {
        super(model);
        init();
    }

    /**
     * Construct an XTree around the default example
     */
    public XTree() {
        super();
        init();
    }

    /**
     * Shared Init code
     */
    private void init() {
        addMouseListener(this);
        addKeyListener(this);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * Expand the selected path
     */
    public void expandPath() {
        TreePath path = getSelectionPath();
        if (path != null) {
            expandPath(path);
        }
    }

    /**
     * Expand All from the selected path
     */
    public void expandAllPath() {
        expandAllPath(getSelectionPath());
    }

    /**
     * Expand All from the specified path
     */
    public void expandAllPath(TreePath path) {
        if (path != null) {
            TreeModel model = getModel();
            if (model instanceof XTreeModel) {
                XTreeModel xmodel = (XTreeModel) model;
                Object node = path.getLastPathComponent();
                for (int i = 0, c = xmodel.getChildCount(node); i < c; i++) {
                    expandAllPath(xmodel.getTreePath(xmodel.getChild(node, i)));
                }
            }
            expandPath(path);
        }
    }

    /**
     * Collapse the selected path
     */
    public void collapsePath() {
        TreePath path = getSelectionPath();
        if (path != null) {
            collapsePath(path);
        }
    }

    /**
     * Collapse All from the selected node
     */
    public void collapseAllPath() {
        collapseAllPath(getSelectionPath());
    }

    /**
     * Collapse all from the specified path
     */
    public void collapseAllPath(TreePath path) {
        if (path != null) {
            TreeModel model = getModel();
            if (model instanceof XTreeModel) {
                XTreeModel xmodel = (XTreeModel) model;
                Object node = path.getLastPathComponent();
                for (int i = 0, c = xmodel.getChildCount(node); i < c; i++) {
                    collapseAllPath(xmodel.getTreePath(xmodel.getChild(node, i)));
                }
            }
            collapsePath(path);
        }
    }

    /**
     * Return true if the path has expanded children
     */
    public boolean isAnyExpanded(TreePath path) {
        if (path != null) {
            TreeModel model = getModel();
            if (model instanceof XTreeModel) {
                XTreeModel xmodel = (XTreeModel) model;

                Object node = path.getLastPathComponent();

                if (!xmodel.isLeaf(node) && isExpanded(path)) {
                    return true;
                }

                for (int i = 0, c = xmodel.getChildCount(node); i < c; i++) {
                    if (isAnyExpanded(xmodel.getTreePath(xmodel.getChild(node, i)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return true if the path has collapsed children
     */
    public boolean isAnyCollapsed(TreePath path) {
        if (path != null) {
            TreeModel model = getModel();
            if (model instanceof XTreeModel) {
                XTreeModel xmodel = (XTreeModel) model;

                Object node = path.getLastPathComponent();

                if (!xmodel.isLeaf(node) && isCollapsed(path)) {
                    return true;
                }

                for (int i = 0, c = xmodel.getChildCount(node); i < c; i++) {
                    if (isAnyCollapsed(xmodel.getTreePath(xmodel.getChild(node, i)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Set the selected path ensuring the selection is visible
     */
    public void setSelectionPath(TreePath path) {
        super.setSelectionPath(path);
        scrollPathToVisible(path);
    }

    /**
     * Get the selected node.
     */
    public Object getSelectedNode() {
        TreePath path = getSelectionPath();
        if (path != null) {
            return path.getLastPathComponent();
        }
        return null;
    }

    /**
     * Returns the current <code>TreeCellRenderer</code> that is rendering
     * each cell. Overridden to use get default
     * 
     * @return the <code>TreeCellRenderer</code> that is rendering each cell
     */
    public TreeCellRenderer getCellRenderer() {
        TreeCellRenderer renderer = super.getCellRenderer();
        if (renderer != null) {
            return renderer;
        } else {
            return getDefaultCellRenderer();
        }
    }

    /**
     * Returns the default <code>TreeCellRenderer</code>
     * 
     * @return the <code>TreeCellRenderer</code> that is rendering each cell
     */
    public TreeCellRenderer getDefaultCellRenderer() {
        if (defaultCellRenderer == null) {
            defaultCellRenderer = new XTreeCellRenderer();
        }
        return defaultCellRenderer;
    }

    /**
     * Returns the current <code>TreeCellEditor</code> that is rendering each
     * cell. Overridden to use get default
     * 
     * @return the <code>TreeCellEditor</code> that is rendering each cell
     */
    public TreeCellEditor getCellEditor() {
        TreeCellEditor editor = super.getCellEditor();
        if (editor != null) {
            return editor;
        } else {
            return getDefaultCellEditor();
        }
    }

    /**
     * Returns the default <code>TreeCellEditor</code>
     * 
     * @return the <code>TreeCellEditor</code> that is rendering each cell
     */
    public TreeCellEditor getDefaultCellEditor() {
        if (defaultCellEditor == null) {
            TreeCellRenderer renderer = getCellRenderer();
            if (renderer instanceof XTreeCellRenderer) {
                defaultCellEditor = new XTreeCellEditor(this, (XTreeCellRenderer) renderer);
            } else {
                defaultCellEditor = null;
            }
        }
        return defaultCellEditor;
    }

    /**
     * Add a popup menu item to tree this is shown when the popup is displayed.
     * 
     * @param item
     *            the menu item to add
     */
    public void addPopupMenuItem(JMenuItem item) {
        if (popupMenuItems == null) {
            popupMenuItems = new ArrayList();
        }
        popupMenuItems.add(item);
    }

    /**
     * Remove a popup menu item from the tree
     * 
     * @param item
     *            the menu item to remove
     */
    public void removePopupMenuItem(JMenuItem item) {
        if (popupMenuItems != null) {
            popupMenuItems.remove(item);
        }
    }

    /**
     * Get an iterator over the trees menu items
     * 
     * @param get
     *            an iterator over the tree's popup menu items
     */
    public Iterator popupMenuItems() {
        if (popupMenuItems != null) {
            popupMenuItems.iterator();
        }
        return null;
    }

    /**
     * Get an iterator over the node's menu items, these are added to the trees
     * menu items when the menu is built
     * 
     * @param get
     *            an iterator over the node's popup menu items
     */
    public Iterator nodePopupMenuItems() {
        TreeModel model = getModel();
        if (model != null && model instanceof XTreeModel) {
            TreePath selectedPath = getSelectionPath();
            if (selectedPath != null) {
                return ((XTreeModel) model).popupMenuItems(selectedPath.getLastPathComponent());
            }
        }
        return null;
    }

    /**
     * Get the standard popup menu items
     */
    public Iterator standardPopupMenuItems() {
        TreeModel model = getModel();
        if (model != null && model instanceof XTreeModel) {
            TreePath selectedPath = getSelectionPath();
            if (selectedPath != null) {
                expandMenuItem.setEnabled(!getModel().isLeaf(selectedPath.getLastPathComponent())
                        && isCollapsed(selectedPath));
                expandAllMenuItem.setEnabled(isAnyCollapsed(selectedPath));
                collapseMenuItem.setEnabled(!getModel().isLeaf(selectedPath.getLastPathComponent())
                        && isExpanded(selectedPath));
                collapseAllMenuItem.setEnabled(isAnyExpanded(selectedPath));

                return new Iterator() {
                    int index = 0;

                    public boolean hasNext() {
                        return index < 4;
                    }

                    public Object next() {
                        switch (index++) {
                        case 0:
                            return expandMenuItem;
                        case 1:
                            return expandAllMenuItem;
                        case 2:
                            return collapseMenuItem;
                        case 3:
                            return collapseAllMenuItem;
                        default:
                            throw new NoSuchElementException();
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        }
        return null;
    }

    /**
     * Get the nodes default menu item this is used for double left and apears
     * in bold at the top of the popup menu
     * 
     * @param get
     *            an iterator over the node's popup menu items
     */
    public JMenuItem getNodePopupMenuDefault() {
        TreeModel model = getModel();
        if (model != null && model instanceof XTreeModel) {
            TreePath selectedPath = getSelectionPath();
            if (selectedPath != null) {
                return ((XTreeModel) model).getPopupMenuDefault(selectedPath.getLastPathComponent());
            }
        }
        return null;
    }

    /**
     * Show popup menu (if has items) built out of the tree's menu items and the
     * nodes menu items these are seperated by a menu seperator
     * 
     * @return the popup menu to show
     */
    private void showPopupMenu(int x, int y) {
        JPopupMenu popupMenu = createPopupMenu();
        if (popupMenu != null) {
            popupMenu.show(this, x, y);
        }
    }

    /**
     * Build popup menu (if has items) built out of the tree's menu items and
     * the nodes menu items these are seperated by a menu seperator
     * 
     * @return the popup menu to show
     */
    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = null;

        JMenuItem nodeDefault = getNodePopupMenuDefault();
        if (nodeDefault != null) {
            popupMenu = new JPopupMenu("Popup");
            popupMenu.add(nodeDefault);
        }

        Iterator treeItems = popupMenuItems();
        if (treeItems != null && treeItems.hasNext()) {
            if (popupMenu == null) {
                popupMenu = new JPopupMenu("Popup");
            } else {
                popupMenu.addSeparator();
            }
            popupMenu.add((JMenuItem) treeItems.next());
            while (treeItems.hasNext()) {
                popupMenu.add((JMenuItem) treeItems.next());
            }
        }

        Iterator nodeItems = nodePopupMenuItems();
        if (nodeItems != null && nodeItems.hasNext()) {
            if (popupMenu == null) {
                popupMenu = new JPopupMenu("Popup");
            } else {
                popupMenu.addSeparator();
            }
            popupMenu.add((JMenuItem) nodeItems.next());
            while (nodeItems.hasNext()) {
                popupMenu.add((JMenuItem) nodeItems.next());
            }
        }

        Iterator standardItems = standardPopupMenuItems();
        if (standardItems != null && standardItems.hasNext()) {
            if (popupMenu == null) {
                popupMenu = new JPopupMenu("Popup");
            } else {
                popupMenu.addSeparator();
            }
            popupMenu.add((JMenuItem) standardItems.next());
            while (standardItems.hasNext()) {
                popupMenu.add((JMenuItem) standardItems.next());
            }
        }
        return popupMenu;
    }

    // ActionListener implementation
    public void keyTyped(KeyEvent e) {
    }

    // ActionListener implementation
    public void keyPressed(KeyEvent e) {
    }

    // ActionListener implementation
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            handleDefaultAction();
        }
    }

    /**
     * MouseListener Implementation: Invoked when the mouse button has been
     * clicked (pressed and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {
        handleDefaultAction(e);
    }

    /**
     * MouseListener Implementation: Invoked when a mouse button has been
     * pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        handleSelect(e);
        handlePopupMenu(e);
    }

    /**
     * MouseListener Implementation: Invoked when a mouse button has been
     * released on a component.
     */
    public void mouseReleased(MouseEvent e) {
        handleSelect(e);
        handlePopupMenu(e);
        mousePressed = false;
    }

    /**
     * MouseListener Implementation: Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {
        mouseArmed = true;
    }

    /**
     * MouseListener Implementation: Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {
        mouseArmed = false;
    }

    /**
     * Invoked when the mouse should cause a select this causes selects to
     * occure correcty on the right button (ie they use the same processing as
     * the left)
     */
    private void handleSelect(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            processMouseEvent(switchEventFromRightToLeft(e));
        }
    }

    /**
     * Invoked when the mouse should cause a popup
     */
    private void handlePopupMenu(MouseEvent e) {
        if (e.isPopupTrigger() && mousePressed && mouseArmed) {
            showPopupMenu(e.getX(), e.getY());
        }
    }

    /**
     * Invoked when the mouse should cause a default action to be run
     */
    private void handleDefaultAction(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1) {
            handleDefaultAction();
        }
    }

    /**
     * Invoked when an event occures that should fire the default action
     */
    private void handleDefaultAction() {
        JMenuItem item = getNodePopupMenuDefault();
        if (item != null) {
            item.doClick();
        }
    }

    /**
     * Switch the event from the left button to the right button
     * 
     * @param the
     *            event to switch (undmodified)
     * @return a new event the same as param but moved from right to left mouse
     *         button
     */
    private static MouseEvent switchEventFromRightToLeft(MouseEvent e) {
        return new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), switchModifiersFromRightToLeft(e
                .getModifiers()), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
    }

    /**
     * Switch the modifiers from the left button to the right button
     * 
     * @param modifiers
     *            the modifers to manipulate
     * @return the manipulated modifiers
     */
    private static int switchModifiersFromRightToLeft(int modifiers) {
        return modifiers ^ InputEvent.BUTTON3_MASK | InputEvent.BUTTON1_MASK; // Clear
        // right(3)
        // add
        // left(1)
    }

    /**
     * Create a new menu item
     */
    private static final JMenuItem createMenuItem(String key, ActionListener actionListener) {
        JMenuItem item = new JMenuItem(XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources).getString(key));
        item.addActionListener(actionListener);
        return item;
    }

}