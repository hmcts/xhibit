package uk.gov.courtservice.xhibit.client.listeners;

import java.awt.Component;
import java.awt.Container;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.xhibit.client.listeners.common.CutCopyCaretListener;
import uk.gov.courtservice.xhibit.client.listeners.common.CutCopyListSelectionListener;
import uk.gov.courtservice.xhibit.client.listeners.common.CutCopyPasteFocusListener;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.6 $
 */
public class XhibitListeners {
    private static final CutCopyCaretListener ccCaretListener = new CutCopyCaretListener();

    private static final CutCopyListSelectionListener ccListListener = new CutCopyListSelectionListener();

    private static final CutCopyPasteFocusListener ccFocusListener = new CutCopyPasteFocusListener();

    /**
     * Private constructor to prevent instantiation of this helper class
     */
    private XhibitListeners() {
        // no implementation required...
    }

    /**
     * This adds the default listeners to the panel. Recursively calls set on
     * components that contain components. Currently this is only used to
     * support Cut/copy/paste for sub classes of JTextComponent.
     * 
     * @param container
     *            The container to set the default listeners on.
     */
    public static void setDefaultListeners(Container container) {
        Component[] allComponents = container.getComponents();
        for (int i = 0; i < allComponents.length; i++) {
            allComponents[i].addFocusListener(ccFocusListener);

            if (allComponents[i] instanceof Container) {
                setDefaultListeners((Container) allComponents[i]);
            }

            if (allComponents[i] instanceof JTextComponent) {
                ((JTextComponent) allComponents[i]).addCaretListener(ccCaretListener);
            } else if (allComponents[i] instanceof JTable) {
                JTable jTable = (JTable) allComponents[i];
                ListSelectionModel colSM = jTable.getColumnModel().getSelectionModel();
                ListSelectionModel rowSM = jTable.getSelectionModel();
                colSM.addListSelectionListener(ccListListener);
                rowSM.addListSelectionListener(ccListListener);
            }
        }
    }

    /**
     * Helper method to remove all listeners from the passed in container and
     * all contained components (recursive). This will also reset the action to
     * <i>null</i> to any <code>JMenuItem</code>'s contained to remove a
     * memory leak in the java Swing code (see Sun bug: 4839069).
     * 
     * @param container
     *            The container to remove all listeners on
     */
    // used to fix Performance/scalability PR 56516
    public static void removeAllListeners(Container container) {
        if (container != null) {
            final Component[] components = container.getComponents();

            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof Container) {
                    removeAllListeners((Container) components[i]);
                }

                removeListeners(components[i]);
            }
        }
    }

    /**
     * Remove some listeners from the component, Note the component will not
     * work after this is run.
     * 
     * @param component
     *            the component to remove listeners from.
     */
    private static void removeListeners(Component component) {
        component.removeFocusListener(ccFocusListener);

        if (component instanceof JMenu) {
            final JMenu menu = (JMenu) component;
            // the popup menus seem to be the main cause of the memory
            // leaks...
            removeAllListeners(menu.getPopupMenu());
            menu.setAction(null);
        } else if (component instanceof AbstractButton) {
            // prevent actions sticking maintaining object references...
            ((AbstractButton) component).setAction(null);
        } else if (component instanceof JTextComponent) {
            ((JTextComponent) component).removeCaretListener(ccCaretListener);
        } else if (component instanceof JTable) {
            // remove the default table model listeners...
            final JTable jTable = (JTable) component;
            jTable.getColumnModel().getSelectionModel().removeListSelectionListener(ccListListener);
            jTable.getSelectionModel().removeListSelectionListener(ccListListener);
        }
    }

    /*
     * // Although the code below will remove all of the listeners from all //
     * components, the performance is unacceptable, taking between 700ms and
     * 2000ms // to remove all listeners. Therefore, only the default listeners
     * created by // this class will be removed (< 10ms), and the JMenuItems
     * will have their // actions removed, this is enough to fix the memory leak
     * problem...
     * 
     * private static void removeListeners(Component component) { Method[]
     * methods = component.getClass().getMethods(); for (int i = 0; i <
     * methods.length; i++) { String name = methods[i].getName(); if
     * (name.startsWith("remove") && name.endsWith("Listener")) { Class[]
     * parameters = methods[i].getParameterTypes(); // check to exclude
     * removePropertyChangeListener(String, PropertyChangeListener) if
     * (parameters.length == 1) { EventListener[] listeners =
     * component.getListeners(parameters[0]); for (int j = 0; j <
     * listeners.length; j++) { try { methods[i].invoke(component, new Object[] {
     * listeners[j]}); } catch (Throwable t) { log.error("Error invoking: " +
     * methods[i], t); } } } } } }
     */
}
