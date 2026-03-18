package uk.gov.courtservice.xhibit.client.crestformsbf.swing;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import uk.gov.courtservice.xhibit.client.crestformsbf.swing.table.SortTableHeader;
import uk.gov.courtservice.xhibit.client.crestformsbf.swing.table.SortTableModel;
import uk.gov.courtservice.xhibit.client.crestformsbf.swing.table.TableKeySelectionManager;

/**
 * Base Table implementation that is 'aware' of the SortTableModel and provides
 * a special header for it
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */
public class XTable extends JTable implements KeyListener, TableKeySelectionManager {

    //
    // Data & Construction
    //

    /**
     * The key selection manager to use
     */
    protected TableKeySelectionManager keySelectionManager = this;

    /**
     * Construct an XTable arround the given model
     */
    public XTable(TableModel model) {
        super(model);
        addKeyListener(this);
    }

    //
    // Utility Methods
    // 

    /**
     * Useful utility method for scrolling the view to ensure a particular cell
     * is visible
     * 
     * @param row
     *            the row to select
     * @param column
     *            the column to select
     */
    public void ensureCellIsVisible(int row, int column) {
        Rectangle cellBounds = getCellRect(row, column, false);
        if (cellBounds != null) {
            scrollRectToVisible(cellBounds);
        }
    }

    //
    // Table Model Work
    //

    /**
     * Set the table model update the header if necisary
     */
    public void setModel(TableModel newModel) {
        setTableHeader(newModel);
        super.setModel(newModel);
    }

    /**
     * Set the table header bassed on the model you are using
     */
    public void setTableHeader(TableModel model) {
        JTableHeader header = getTableHeader();
        if (SortTableModel.isSortTableModel(model)) {
            if (SortTableHeader.isSortTableHeader(header)) {
                // do nothing
            } else {
                setTableHeader(new SortTableHeader(getColumnModel()));
            }
        } else {
            if (SortTableHeader.isSortTableHeader(header)) {
                setTableHeader(super.createDefaultTableHeader());
            }
        }
    }

    /**
     * Create a SortTableHeader if we have a SortTableModel else use default
     * 
     * @return the table header to use
     */
    public JTableHeader createDefaultTableHeader() {
        if (SortTableModel.isSortTableModel(getModel())) {
            return new SortTableHeader(getColumnModel());
        } else {
            return super.createDefaultTableHeader();
        }
    }

    //
    // KeyListener implementation
    //

    /**
     * KeyListener implementation
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     *      KeyListener
     */
    public void keyTyped(KeyEvent e) {
        if (isEnabled() && !isNavigationKey(e) && isTypeAheadKey(e)) {
            int column = getSelectedColumn();
            int row = getSelectedRow();
            if (!isCellEditable(row, column)) {
                if (selectWithKeyChar(e.getKeyChar(), column == -1 ? 0 : column)) {
                    e.consume();
                }
            }
        }
    }

    /**
     * KeyListener implementation
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     *      KeyListener
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * KeyListener implementation
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     *      KeyListener
     */
    public void keyReleased(KeyEvent e) {
    }

    private boolean isTypeAheadKey(KeyEvent e) {
        return !e.isAltDown() && !e.isControlDown() && !e.isMetaDown();
    }

    private boolean isNavigationKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        return keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT
                || keyCode == KeyEvent.VK_RIGHT;
    }

    //
    // Key selection
    //

    /**
     * Get the current key selection manager
     * 
     * @return the current key selection manager
     */
    public TableKeySelectionManager getKeySelectionManager() {
        return keySelectionManager;
    }

    /**
     * Set the current key selection manager
     * 
     * @param keySelectionManager
     *            the new keySelectionManager;
     */
    public void setKeySelectionManager(TableKeySelectionManager keySelectionManager) {
        if (keySelectionManager == null) {
            throw new IllegalArgumentException("keySelectionManager");
        }
        this.keySelectionManager = keySelectionManager;
    }

    /**
     * Selects the row corresponds to the specified keyboard character pressed
     * in the specified column. Returns true if there is a corresponding entry
     * to select, otherwise returns false.
     * 
     * @param keyChar
     *            a char, typically this is a keyboard key typed by the user
     */
    public boolean selectWithKeyChar(char keyChar, int column) {
        int index = keySelectionManager.selectionForKey(keyChar, column, getModel());
        if (index != -1) {
            setRowSelectionInterval(index, index);
            ensureCellIsVisible(index, column);
            return true;
        } else {
            return false;
        }
    }

    //
    // TableKeySelectionManager Implementation
    //

    private static final long RESET_PAUSE = 500;

    private StringBuffer patternBuffer = new StringBuffer();

    private long lastTime = 0;

    private int lastColumn = 0;

    /**
     * TableKeySelectionManager Implementation
     * 
     * Match a pattern
     * 
     * @see uk.gov.courtservice.xhibit.client.crestformsbf.swing.table.TableKeySelectionManager#selectionForKey(char,
     *      int, javax.swing.table.TableModel) TableKeySelectionManager
     */
    public int selectionForKey(char key, int column, TableModel model) {
        if (lastColumn == column) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > RESET_PAUSE) {
                patternBuffer.setLength(0);
            }
            lastTime = currentTime;
        } else {
            patternBuffer.setLength(0);
            lastTime = System.currentTimeMillis();
            lastColumn = column;
        }

        patternBuffer.append(Character.toLowerCase(key));

        String pattern = patternBuffer.toString();
        for (int i = 0, c = model.getRowCount(); i < c; i++) {
            if (String.valueOf(model.getValueAt(i, column)).toLowerCase().startsWith(pattern)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Test method
     * 
     * @param args
     *            unused command line arguments
     */
    public static void main(String[] args) throws Exception {
        final int COLUMN_COUNT = 5;
        final int ROW_COUNT = 5;

        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

        javax.swing.JFrame frame = new javax.swing.JFrame("X Table Test");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new java.awt.GridBagLayout());

        java.awt.GridBagConstraints constraints = new java.awt.GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;

        frame.getContentPane().add(new javax.swing.JLabel("Selected: "), constraints);

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;

        final javax.swing.JLabel selected = new javax.swing.JLabel("None");

        frame.getContentPane().add(selected, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 3;
        constraints.fill = java.awt.GridBagConstraints.BOTH;
        constraints.insets = new java.awt.Insets(4, 4, 4, 4);

        final javax.swing.table.TableModel numberModel = new javax.swing.table.AbstractTableModel() {

            Integer[][] data = new Integer[COLUMN_COUNT][ROW_COUNT];
            {
                java.util.Random random = new java.util.Random();
                for (int i = 0; i < COLUMN_COUNT; i++) {
                    for (int j = 0; j < ROW_COUNT; j++) {
                        data[i][j] = new Integer(Math.abs(random.nextInt(1000)));
                    }
                }
            }

            public String getColumnName(int column) {
                return "Column " + column;
            }

            public int getColumnCount() {
                return COLUMN_COUNT + 1;
            }

            public int getRowCount() {
                return ROW_COUNT;
            }

            public Class getColumnClass(int columnIndex) {
                if (columnIndex == COLUMN_COUNT) {
                    return Boolean.class;
                } else {
                    return Integer.class;
                }
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == COLUMN_COUNT) {
                    return rowIndex % 2 == 1 ? Boolean.TRUE : Boolean.FALSE;
                } else {
                    return data[columnIndex][rowIndex];
                }
            }
        };
        final SortTableModel sortedModel = new SortTableModel(numberModel);

        sortedModel.sort(2);

        final XTable table = new XTable(sortedModel);

        table.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                int[] indicies = sortedModel.mapRowIndicies(table.getSelectedRows());
                if (0 < indicies.length) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(getRowData(indicies[0]));
                    for (int i = 1; i < indicies.length; i++) {
                        buffer.append(", ");
                        buffer.append(getRowData(indicies[i]));
                    }
                    selected.setText(buffer.toString());
                } else {
                    selected.setText("No Row Data");
                }
            }

            public String getRowData(int row) {
                int c = numberModel.getColumnCount();
                if (0 < c) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("[");
                    buffer.append(numberModel.getValueAt(row, 0));
                    for (int i = 1; i < c; i++) {
                        buffer.append(",");
                        buffer.append(numberModel.getValueAt(row, i));
                    }
                    buffer.append("]");
                    return buffer.toString();
                } else {
                    return "[ No Column Data ]";
                }
            }
        });

        frame.getContentPane().add(new javax.swing.JScrollPane(table), constraints);

        // Sort

        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = java.awt.GridBagConstraints.WEST;
        constraints.fill = java.awt.GridBagConstraints.NONE;

        frame.getContentPane().add(new javax.swing.JLabel("Sort: "), constraints);

        constraints.gridx = 1;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(0, COLUMN_COUNT, 4, 4));

        final javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();

        final javax.swing.JCheckBox sortAscending = new javax.swing.JCheckBox("Ascending", sortedModel
                .isSortAscending());

        class IndexRadioButton extends javax.swing.JRadioButton implements java.awt.event.ActionListener {
            private final int index;

            public IndexRadioButton(int index) {
                super(String.valueOf(index), index == sortedModel.getSortedColumn());
                this.index = index;
                addActionListener(this);
                group.add(this);
            }

            public int getIndex() {
                return index;
            }

            public void actionPerformed(java.awt.event.ActionEvent e) {
                sortedModel.sort(index, sortAscending.isSelected());
            }
        }

        for (int i = 0; i < COLUMN_COUNT; i++) {
            panel.add(new IndexRadioButton(i));
        }
        frame.getContentPane().add(panel, constraints);

        constraints.gridx = 2;
        constraints.fill = java.awt.GridBagConstraints.NONE;

        sortAscending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int index = 0;
                java.util.Enumeration buttons = group.getElements();
                while (buttons.hasMoreElements()) {
                    if (((IndexRadioButton) buttons.nextElement()).isSelected()) {
                        sortedModel.sort(index, sortAscending.isSelected());
                        break;
                    }
                    index++;
                }
            }
        });
        frame.getContentPane().add(sortAscending, constraints);

        // Show

        frame.show();

    }
}
