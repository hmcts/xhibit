/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JPanel;

import mseries.ui.IntegerEditor;
import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import mseries.ui.MIntegerSpinnerModel;
import mseries.ui.MListSpinnerModel;
import mseries.ui.MSpinner;

public class MSpinnerChanger extends JPanel implements MDateChanger {
    private Calendar maxC = Calendar.getInstance();

    private Calendar minC = Calendar.getInstance();

    private int minMonth, minYear;

    private int maxYear;

    private int day;

    private DateFormatSymbols dfs;

    private MSpinner month, year;

    private MIntegerSpinnerModel yearModel;

    private MListSpinnerModel monthModel;

    protected Vector listeners = new Vector();

    public MSpinnerChanger() {
        super();

        DateFormatSymbols dfs;
        String[] months, mths;

        minC.set(1900, 0, 1);
        minMonth = minC.get(Calendar.MONTH);
        minYear = minC.get(Calendar.YEAR);

        maxC.set(2037, 11, 31);
        maxYear = maxC.get(Calendar.YEAR);

        // Create the widgets
        month = new MSpinner(7);
        month.setBackground(getBackground());
        month.setMinimumSize(new Dimension(90, 22));
        dfs = new DateFormatSymbols();
        mths = dfs.getMonths();
        months = new String[12];
        System.arraycopy(mths, 0, months, 0, 12);

        monthModel = new MListSpinnerModel(months);
        month.setModel(monthModel);
        month.addMChangeListener(new MChangeListener() {
            public void valueChanged(MChangeEvent e) {
                notifyListeners(MChangeEvent.CHANGE);
            }
        });

        year = new MSpinner(4);
        year.setBackground(getBackground());
        year.setMinimumSize(new Dimension(64, 22));
        yearModel = new MIntegerSpinnerModel(minYear, maxYear, minYear, 1, false);
        yearModel.setMinimum(minYear);
        yearModel.setMaximum(maxYear);
        year.setModel(yearModel);
        year.setEditor(new IntegerEditor());
        year.addMChangeListener(new MChangeListener() {
            public void valueChanged(MChangeEvent e) {
                notifyListeners(MChangeEvent.CHANGE);
            }
        });

        // Draw the screen
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        setLayout(gridbag);
        c.insets = new Insets(2, 2, 2, 2);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        add(month, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        add(year, c);
    }

    public void setMinimum(Date min) {
        minC.setTime(min);
        minYear = minC.get(Calendar.YEAR);
        minMonth = minC.get(Calendar.MONTH);

        yearModel.setMinimum(minYear);
        setValue(min);
    }

    public void setMaximum(Date max) {
        maxC.setTime(max);
        maxYear = maxC.get(Calendar.YEAR);

        yearModel.setMaximum(maxYear);
        setValue(max);
    }

    public void setValue(Date newVal) {
        Calendar valC = Calendar.getInstance();
        valC.setTime(newVal);

        int y = valC.get(Calendar.YEAR);
        int m = valC.get(Calendar.MONTH);
        monthModel.setIndex(m);
        yearModel.setValue(y);
    }

    public int getValue() {
        int m = 0;
        try {
            MListSpinnerModel.ListObject o = (MListSpinnerModel.ListObject) monthModel.getValue();
            m = o.index;
        } catch (Exception e) {
        }
        int y = ((Integer) yearModel.getValue()).intValue();

        int newValue = (y - minYear) * 12 + m - minMonth;
        return newValue;
    }

    private void notifyListeners(int type) {
        Vector list = (Vector) listeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MChangeListener l = (MChangeListener) listeners.elementAt(i);
            l.valueChanged(new MChangeEvent(this, new Integer(getValue()), type));
        }
    }

    public void addMChangeListener(MChangeListener l) {
        listeners.addElement(l);
    }

    public void removeMChangeListener(MChangeListener l) {
        listeners.removeElement(l);
    }

    /**
     * Does anything within the component have the focus
     * 
     * @return true if any child component has the focus
     */
    public boolean hasFocus() {
        return month.display.hasFocus() || year.display.hasFocus();
    }

    /**
     * Adds the focus listener by delegating to each child component
     * addFocusListener method.
     */
    public void addFListener(FocusListener l) {
        month.display.addFocusListener(l);
        year.display.addFocusListener(l);
    }

    /**
     * Removes the focusListner from the child components
     */
    public void removeFListener(FocusListener l) {
        month.display.removeFocusListener(l);
        year.display.removeFocusListener(l);
    }

    /*
     * public static void main(String[] argv) { JFrame f = new JFrame("Test");
     * final MSpinnerChanger c = new MSpinnerChanger();
     * 
     * c.setBground(Color.red); c.setFground(Color.green);
     * c.addMChangeListener(new MChangeListener(){ public void
     * valueChanged(MChangeEvent e) { System.out.println(c.getValue()); } });
     * 
     * f.getContentPane().add(c);
     * 
     * f.pack(); f.show(); }
     */
}
