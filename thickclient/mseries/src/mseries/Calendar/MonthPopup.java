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
package mseries.Calendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import mseries.utils.SafeCalendarUtils;

public class MonthPopup extends JWindow implements MouseListener {
    JList months;

    MonthModel model;

    AutoChanger autoChanger;

    Calendar c1;

    int width, height;

    public MonthPopup() {
        model = new MonthModel();
        months = new JList();
        setValue(new Date());
        months.setModel(model);
        getContentPane().setLayout(new GridLayout(1, 0));
        getContentPane().add(months);
        addMouseListener(this);

        months.setCellRenderer(getCellRenderer());
        months.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        months.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void setLocationOnScreen(int x, int y) {
        this.setLocation(x, y);
    }

    public void mouseEntered(MouseEvent e) {
        // Point p;
        if (autoChanger != null) {
            autoChanger.stopThread();
            autoChanger = null;
        }
    }

    public void mouseExited(MouseEvent e) {
        // int mouseX=e.getX();
        int mouseY = e.getY();

        if (mouseY < 0) {
            autoChanger = new AutoChanger(model);
            autoChanger.setDirection(AutoChanger.DEC);
            autoChanger.start();
        } else if (mouseY >= height) {
            autoChanger = new AutoChanger(model);
            autoChanger.setDirection(AutoChanger.INC);
            autoChanger.start();
        }
    }

    public Object getValue() {
        // System.out.println("MonthPopup.getValue:"+
        // months.getSelectedValue());
        return months.getSelectedValue();
    }

    public void setValue(Date value) {
        model.setMonth(value);
        months.setSelectedIndex(3);
    }

    public void setSelectedPoint(Point p) {
        if (p.y < 0 || p.y > height) {
            months.clearSelection();
        } else {
            months.setSelectedIndex(months.locationToIndex(p));
        }
    }

    public ListCellRenderer getCellRenderer() {
        ListCellRenderer r;
        r = new DefaultListCellRenderer() {

            DateFormat df = new SimpleDateFormat("MMMMM yyyy");

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Date d = (Date) value;
                setText(df.format(d));
                setHorizontalAlignment(CENTER);

                setBackground(isSelected ? UIManager.getColor("ComboBox.selectionBackground") : UIManager
                        .getColor("ComboBox.background"));
                setForeground(isSelected ? UIManager.getColor("ComboBox.selectionForeground") : UIManager
                        .getColor("ComboBox.foreground"));
                return this;
            }
        };

        return r;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        width = getWidth();
        height = getHeight();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    class MonthModel extends AbstractListModel {
        Date firstDate = new Date();

        Calendar firstCal;

        public void setMonth(Date month) {
            firstCal = Calendar.getInstance();
            firstCal.setTime(month);
            // firstCal.add(Calendar.MONTH, -3);
            SafeCalendarUtils.doSafeAddition(firstCal, Calendar.MONTH, -3);
        }

        public Object getElementAt(int i) {
            c1 = (Calendar) firstCal.clone();
            // c1.add(Calendar.MONTH, i);
            SafeCalendarUtils.doSafeAddition(c1, Calendar.MONTH, i);
            return c1.getTime();
        }

        public int getSize() {
            return 7;
        }

        public void increment(int inc) {
            // firstCal.add(Calendar.MONTH, inc);
            SafeCalendarUtils.doSafeAddition(firstCal, Calendar.MONTH, inc);
            fireContentsChanged(this, 0, 6);
        }
    }

    class AutoChanger extends Thread {
        static final int INC = 1;

        static final int DEC = -1;

        int dir = INC;

        MonthModel model;

        boolean keepGoing;

        public AutoChanger(MonthModel model) {
            this.model = model;
            keepGoing = true;
        }

        public void setDirection(int dir) {
            this.dir = dir;
        }

        public void run() {
            keepGoing = true;
            while (keepGoing) {
                try {
                    model.increment(dir);
                    sleep(750);
                } catch (InterruptedException e) {
                    keepGoing = false;
                }
            }
        }

        public void stopThread() {
            keepGoing = false;
        }
    }

    public static void main(String[] argv) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        MonthPopup f = new MonthPopup();

        f.pack();
        f.setLocationOnScreen(100, 100);
        f.setVisible(true);
    }
}
// $Log: MonthPopup.java,v $
// Revision 1.3  2006/06/05 12:31:45  bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting tab fix
//
// Revision 1.2 2006/05/31 14:26:06 bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting
//
// Revision 1.1 2004/04/02 15:44:01 sz0t7n
// Adding mseries to the build so we can bug fix it from now on
//
// Revision 1.5 2003/01/15 22:26:43 martin
// *** empty log message ***
//
// Revision 1.4 2002/07/21 16:24:39 martin
// no message
//
// Revision 1.3 2002/07/18 21:43:45 martin
// no message
//
// Revision 1.2 2002/07/17 21:32:40 martin
// no message
//
