
package mseries.nationality;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.Collection;

import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;


/**
 * Display a scrollable list of nationalities.
 * Modelled on MDateSelectorPanel
 */
public class MNationalitySelectorPanel 
extends JPanel 
implements MNationalityListener, MChangeListener, MouseListener {

    private static final long serialVersionUID = 1L;

    private MNationality dataModel;

    private JTable table;
    
    private JScrollPane pane;

    private boolean focusCycleRoot = false;

    private FocusCheckerNationality fc;
    
    private static Collection<String> nationalities;


    /*
     * Called as a popup from a mouse click on the down arrow button.
     * This relies on the MNationalitySelectorPanel(boolean lazy, Collection<String> nationalities)
     * constructor having been called once already.  That is guaranteed since you wont have a
     * down arrow if the MNationalityEntryField has not already been constructed.
     */
    public MNationalitySelectorPanel() {
        super(new GridBagLayout());
        init(false);
    }
    
    /**
     * @param lazy
     *            set to true if it is known that the some constraints will be
     *            set before the panel is displayed, this avoids the default
     *            constraints from being used only to get overridden by a new
     *            set.
     */
    public MNationalitySelectorPanel(boolean lazy) {
        super(new GridBagLayout());
        init(lazy);
    }
    
    private void init(boolean lazy) {
        this.dataModel = new MNationality(nationalities);
        dataModel.addMNationalityListener(this);
        
        fc = new FocusCheckerNationality();
        fc.setAction(new MDSActionNationality() {
            public void doAction() {
                dataModel.lostFocus();
            }
        });
        
        table = getTable();
        pane = getScrollPane(table);
        
        registerListeners();
        setColours(pane);

        add(pane);
        table.addFocusListener(fc);
        fc.addComponent(table);
        pane.setOpaque(false);
        this.setFocusTraversalPolicyProvider(true);

        if (!lazy) {
            setPullDownConstraints(new MDefaultPullDownConstraints());
        }
    }
    
    
    private JScrollPane getScrollPane(JTable table) {
        if (pane != null) 
            return pane;
        
        pane = new JScrollPane(table);
        int width = 400;
        int height = 200;
        pane.setPreferredSize(new Dimension(width,height));
        
        return pane;
    }
    
    
    private JTable getTable() {
        if (table != null) 
            return table;
        
        table = new JTable(this.dataModel);
        TableColumn column = table.getColumnModel().getColumn(0);
        final int nationalityCodeColumnWidth = 70;
        column.setPreferredWidth(nationalityCodeColumnWidth);
        column.setMaxWidth(nationalityCodeColumnWidth);
        column.setMinWidth(nationalityCodeColumnWidth);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(this);
        
        table.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                final int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    // Choose the currently highlighed item and close the nationality selector
                    final int selectedRow = table.getSelectedRow();
                    if (selectedRow > -1) {
                        String nationality = dataModel.getModelNationalityAt(selectedRow);
                        dataModel.setNationality(nationality);
                        dataModel.exitEvent();
                    }
                } else if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z){
                    // Pressing a keyboard letter will skip to the entries starting
                    // with that letter.
                    String keyText = KeyEvent.getKeyText(keyCode);
                    setSelectionStartingWith(keyText);
                }
            }
            public void keyTyped(KeyEvent e) {
                // empty
            }
            public void keyReleased(KeyEvent e) {
                // empty
            }
        });
        
        return table;
    }
    

    /*
     * This requires initializing once before any instantions of this
     * class can be made.  The nationalities are static throughout 
     * the lifetime of the process though.
     */
    public static void setNationalities(Collection<String> nationalities) {
        MNationalitySelectorPanel.nationalities = nationalities;
    }

    public void requestFocus() {
        table.requestFocus();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        pane.setEnabled(enabled);
    }


    public void setNationality(String nationality) {
        dataModel.setNationality(nationality);
        setSelection();
    }
    
    private void setSelectionStartingWith(String prefix) {
        final int rowIndex = dataModel.getModelNationalityIndexStartingWith(prefix);
        if (rowIndex > -1) {
            // Scroll the table viewport so that it includes the new row
            Rectangle rec = table.getCellRect(rowIndex, 0, true);
            table.scrollRectToVisible(rec);

            // Select the row
            table.setRowSelectionInterval(rowIndex, rowIndex);
        }
    }

    private void setSelection() {
        final int rowIndex = dataModel.getModelNationalityIndex(dataModel.getNationality());
        if (rowIndex > -1) {
            // Scroll the table viewport so that it includes the new row
            Rectangle rec;
            if (rowIndex == 0) {
                // it is the top row
                rec = table.getCellRect(rowIndex, 0, true);
            } else if (rowIndex == 1) {
                // show one row above
                rec = table.getCellRect(rowIndex - 1, 0, true);
            } else {
                // show two rows above - nice to show a bit of context for the selection
                rec = table.getCellRect(rowIndex - 2, 0, true);
            }
            table.scrollRectToVisible(rec);
            
            // Select the row
            table.setRowSelectionInterval(rowIndex, rowIndex);
        }
    }
    
    public String getNationality() {
        return dataModel.getNationality();
    }


    /**
     * Registers objects that are to listen to events from the NationalitySelector
     * Event type of NEW_NATIONALITY indicate that the value has changed
     * 
     * @param l
     *            an MNationalityListener
     */
    public void addMNationalityListener(MNationalityListener l) {
        dataModel.addMNationalityListener(l);
    }

    /**
     * De-Registers objects that are to listen to events from the NationalitySelector
     * 
     * @param l
     *            an MNationalityListener
     */
    public void removeMNationalityListener(MNationalityListener l) {
        dataModel.removeMNationalityListener(l);
    }

    /**
     * Sets the display attributes for the pull down nationality panel
     * 
     * @param c
     *            an instance of MNationalitySelectorConstraints that contains the
     *            desired settings.
     */
    public void setPullDownConstraints(MNationalitySelectorConstraints c) {
        Color foreground = c.getForeground();
        if (foreground != null) {
            setForeground(foreground);
        }
        Color background = c.getBackground();
        if (background != null) {
            setBackground(background);
        }
        Font font = c.getFont();
        if (font != null) {
            setFont(font);
        }
    }
    
    /* ----------- Methods required for interfaces ----------- */

    /**
     * Reacts to all changes in the data model (MNationality) which is given by the
     * event type MNationalityListener interface
     */
    public void dataChanged(MNationalityEvent e) {
        switch (e.getType()) {
        case MNationalityEvent.NEW_NATIONALITY:
            // selection will be changed already
            //setSelection();
            break;
        default:
            break;
        }
    }

    /**
     * for MChangeListener
     */
    public void valueChanged(MChangeEvent event) {
        switch (event.getType()) {
        case MChangeEvent.EXIT:
            dataModel.exitEvent();
            break;
        case MChangeEvent.CHANGE:
            break;
        }
    }

    private void registerListeners() {
        table.addMouseListener(this);
        table.addFocusListener(fc);
        fc.addComponent(table);
    }

    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 1) {
            final int selectedRow = table.getSelectedRow();
            if (selectedRow > -1) {
                String nationality = dataModel.getModelNationalityAt(selectedRow);
                dataModel.setNationality(nationality);
                dataModel.exitEvent();
            }
        }
    }

    public void mouseEntered(MouseEvent event) {
        // empty
    }

    public void mouseExited(MouseEvent event) {
        // empty
    }

    public void mousePressed(MouseEvent event) {
        // empty
    }

    public void mouseReleased(MouseEvent event) {
        // empty
    }

    protected void setColours(Component c) {
        c.setBackground(getBackground());
        c.setForeground(getForeground());
    }

    /**
     * Set the foreground colour
     */
    public void setForeground(Color foreground) {
        super.setForeground(foreground);
        updateComponentColours(this);
    }

    /**
     * Set the background colour
     */
    public void setBackground(Color background) {
        super.setBackground(background);
        updateComponentColours(this);
    }

    /*
     * Recurse through the components on the panel setting the colours
     */
    private void updateComponentColours(Container c) {
        Component[] children = c.getComponents();

        for (int i = 0; i < children.length; i++) {
            setColours(children[i]);
        }
    }

    public void setFont(Font font) {
        super.setFont(font);
        Component[] children = getComponents();

        for (int i = 0; i < children.length; i++) {
            setComponentFont(children[i]);
        }
    }

    public void setComponentFont(Component c) {
        c.setFont(getFont());
        if (c instanceof java.awt.Container) {
            Component[] children = ((Container) c).getComponents();

            for (int i = 0; i < children.length; i++) {
                children[i].setFont(getFont());
                setComponentFont(children[i]);
            }
        }
    }

    public void setFocusCycleRoot(boolean fcr) {
        focusCycleRoot = fcr;
    }

    public boolean isFocusCycleRoot() {
        return focusCycleRoot;
    }

    public void close(String command) {
        if (command.equals("CLOSE")) {
            dataModel.exitEvent();
        } else {
            dataModel.lostFocus();
        }
    }
}

/**
 * Class to report when the components in its group have all lost the keyboard
 * focus. Any number of components can participate, the action to execute when
 * they all have lost the focus is encapsulated in the MDSActionNationality passed in the
 * setAction method
 */
class FocusCheckerNationality implements FocusListener {
    Vector<Object> c = new Vector<Object>();

    MDSActionNationality action = new MDSActionNationality() {
        public void doAction() {
        }
    };

    public void addComponent(Object c) {
        this.c.add(c);
    }

    /**
     * The action contains the behaviour that will be executed when none of the
     * components in the group have the keyboard focus
     */
    public void setAction(MDSActionNationality action) {
        this.action = action;
    }

    public void setComponents(Vector<Object> c) {
        this.c = c;
    }

    public void focusLost(FocusEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < c.size(); i++) {
                    JComponent comp = (JComponent) c.get(i);
                    if (comp.hasFocus()) {
                        return;
                    }
                }
                action.doAction();
            }
        });
    }

    public void focusGained(FocusEvent e) {
        // empty
    }
}

interface MDSActionNationality {
    public void doAction();
}
