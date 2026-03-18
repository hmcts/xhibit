/*
 *   Copyright (c) 2002 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 * 
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 *
 *   The author requests that he be notified of any application, applet, or other binary that 
 *   makes use of this code and that some acknowedgement is given. Comments, questions and 
 *   requests for change will be welcomed.
 */
package mseries.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import mseries.Calendar.MDateChanger;
import mseries.Calendar.MDefaultPullDownConstraints;

/**
 * A very simple demonstration class showing the MDateCellEditor which is not
 * much more than a simple wrapping of MDateEntryField to use it as an editor
 * and a renderer for a Date object within a JTable.
 * <p>
 * Please take a close look at the table model that is used in this sample, it
 * always returns the same values for each cell, the edits have no effect. This
 * is intentional, the point of the code is to demonstrate the use of
 * MDateCellEditor <strong>only</strong>.
 */
public class TableTest {

    public TableTest() {

        JFrame frame = new JFrame("Table Demo");

        JTable table = new JTable();
        table.setModel(new MyTableModel());

        MDateCellEditor editor = new MDateCellEditor("dd/MM/yyyy");
        MDefaultPullDownConstraints c = new MDefaultPullDownConstraints();
        c.firstDay = Calendar.MONDAY;
        c.changerStyle = MDateChanger.SPINNER;
        editor.setConstraints(c);

        table.setDefaultEditor(Date.class, editor);

        /*
         * No need to configure the renderer as it doesn't really do anything,
         * in fact all it does really do is provide the button in the display
         * field. A normal Date renderer would do just fine only the the button
         * would not be present and the poor use might not know how to change
         * the value ?!
         */
        table.setDefaultRenderer(Date.class, new MDateCellEditor("dd/MM/yyyy"));

        JScrollPane scrollPane = new JScrollPane(table);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, "Center");

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        TableTest testFrame = new TableTest();
    }
}

/* Very simple TableModel to provide some data, edits are not preserved */
class MyTableModel extends AbstractTableModel {
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        return 3;
    }

    public Class getColumnClass(int col) {
        switch (col) {
        case 0:
            return String.class;
        case 1:
            return Date.class;
        case 3:
        default:
            return String.class;
        }
    }

    public Object getValueAt(int row, int col) {
        Object o;
        switch (col) {
        case 0:
            o = new String("Hello World");
            break;
        case 1:
            o = new Date();
            break;
        case 3:
        default:
            o = new String("Java");
        }
        return o;
    }
}
/*
 * $Log: TableTest.java,v $
 * Revision 1.3  2006/06/05 12:31:54  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.2 2006/05/31 14:26:10 bzjrnl Change:
 * TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision 1.1
 * 2004/04/02 15:44:08 sz0t7n Adding mseries to the build so we can bug fix it
 * from now on
 * 
 * Revision 1.1 2002/02/06 14:25:33 martin New class
 * 
 */
