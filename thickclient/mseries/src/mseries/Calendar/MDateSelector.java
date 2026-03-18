/*
 *   Copyright (c) 2000 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;

/**
 * The controller for the DateSelector. This class is the programming interface
 * and should be the only class in the package of which knowledge is needed in
 * order to use the MDateSelector.
 */
public class MDateSelector implements ActionListener, MMonthListener {

    protected Color[] background;

    protected Color[] foreground;

    private MDateSelectorUI view;

    private MDateSelectorPanel panel;

    private Date today, minDate, maxDate;

    private Date returnDate;

    private boolean cancelled;

    private DateFormat ddf;

    private ResourceBundle rb = null;

    private String bundleName = "mseries.Calendar.DateSelectorRB";

    private int firstDay = 1;

    private String imageFile;

    private int style = MDateChanger.SCROLLBAR;

    MDateSelectorConstraints constraints = new MDefaultPullDownConstraints();

    public MDateSelector() {
        DateFormat df;
        today = new Date();

        /*
         * The following DateFormat instance is only used to pass in the default
         * dates so en_GB will be OK
         */
        df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("en", "GB"));
        try {
            minDate = df.parse("1/1/1900");
            maxDate = df.parse("31/12/2037");
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }

        background = new Color[7];
        foreground = new Color[7];
    }

    /**
     * Gets a panel which is a self contained component to represent the
     * calendar grid and scrollbar, defaults to current date
     * 
     * @return a MDateSelectorPanel
     */
    public MDateSelectorPanel getDisplay() {
        return getDisplay(today);
    }

    /**
     * @deprecated use setConstraints and a constraints object instead
     */
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * @deprecated use setConstraints and a constraints object instead
     */
    public void setForeground(int day, Color color) {
        foreground[day - 1] = color;
    }

    /**
     * @deprecated use setConstraints and a constraints object instead
     */
    public void setBackground(int day, Color color) {
        background[day - 1] = color;

    }

    /**
     * Sets the constraints object that contains the parameters used to
     * configure the pop up calendar
     * 
     * @param c
     *            the constraints object
     * @see #getConstraints
     */
    public void setConstraints(MDateSelectorConstraints c) {
        this.constraints = c;
    }

    /**
     * Gets the constraints object that contains the parameters used to
     * configure the pop up calendar
     * 
     * @see #setConstraints
     */
    public MDateSelectorConstraints getConstraints() {
        return this.constraints;
    }

    /**
     * Gets a panel which is a self contained component to represent the
     * calendar grid and scrollbar
     * 
     * @param date
     *            the date to display in the calendar
     * @return a MDateSelectorPanel
     */
    public MDateSelectorPanel getDisplay(Date date) {
        this.returnDate = date;
        Date inDate;
        if (date == null) {
            inDate = today;
        } else {
            inDate = new Date(date.getTime());
        }
        if (panel == null) {
            panel = new MDateSelectorPanel(true);
            panel.setPullDownConstraints(constraints);
            panel.setFocusCycleRoot(false);
            panel.setDate(inDate);
        }
        return panel;
    }

    /**
     * @deprecated use setConstraints and a constraints object instead
     */
    public void setChangerStyle(int style) {
        this.style = style;
    }

    /**
     * @deprecated use setConstraints and a constraints object instead
     */
    public int getChangerStyle() {
        return this.style;
    }

    /**
     * Makes the calendar visible
     * 
     * @param parent,
     *            the parent or container
     * @param pnt,
     *            the position of the popup in the container
     * @param date,
     *            the initial date to display
     */
    public void show(Component parent, Point pnt, Date date) {
        showSelector(parent, pnt, date);
    }

    private void showSelector(Component parent, Point pnt, Date date) {
        JFrame f;
        int x = 0;
        int y = 0;
        returnDate = date;
        // Find the frame that is the parent of the component so
        // that the dialog can be hooked up with it
        Object frame = (Object) parent;

        while (!(frame instanceof JFrame)) {
            x += ((Component) frame).getBounds().x;
            y += ((Component) frame).getBounds().y;
            frame = ((Component) frame).getParent();
        }

        f = (JFrame) frame;
        // Construct the view
        if (panel == null) {
            panel = getDisplay(date);
            panel.addMMonthListener(this);
        }
        if (view == null) {
            view = new MDateSelectorUI(f, panel, this, constraints.getResourceBundle(), ddf, constraints.getImageFile());
            view.pack();
        }

        // Get the default (size &) position, change it to the mouse
        // clicked position
        Rectangle r = view.getBounds();
        r.x = pnt.x + f.getBounds().x + x;
        r.y = pnt.y + f.getBounds().y + y;

        // Get the size of the window so we can check for popup going
        // out of bounds.

        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension screen = t.getScreenSize();

        if (r.x + r.width > screen.width)
            r.x = screen.width - r.width;

        if (r.y + r.height > screen.height)
            r.y = screen.height - r.height;

        view.setBounds(r);

        view.show();
    }

    /**
     * Sets the minimum date value allowed
     * 
     * @param min
     *            the earliest possible date
     */
    public void setMinimum(Date min) {
        minDate = min;
    }

    /**
     * Sets the maximum date value allowed
     * 
     * @param max
     *            the earliest possible date
     */
    public void setMaximum(Date max) {
        maxDate = max;
    }

    /**
     * The first column on the calendar grid shows the dates for the day passed
     * here.
     * 
     * @param firstDay
     *            a day constant from java.util.Calendar such as Calendar.SUNDAY
     * @deprecated use setConstraints and a constraints object instead
     */
    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }

    /**
     * The ResourceBundle passed should be able to localize the strings on the
     * button labels and calendar day headings, defaults are provided.
     * 
     * @param rb
     *            a ResourceBundle to do the localization
     * @deprecated use setConstraints and a constraints object instead
     */
    public void setTextLocalizer(ResourceBundle rb) {
        this.rb = rb;
    }

    /**
     * The passed date format object is used to format the date displayed at the
     * top of the calendar popup
     * 
     * @param df
     *            a DateFormat object configured to format as required
     */
    public void setDateFormatter(DateFormat userDf) {
        this.ddf = userDf;
    }

    /**
     * Reacts to all changes in the data model (MMonth) which is given by the
     * event type. from mseries.utils.MMonthListener interface
     */
    public void dataChanged(MMonthEvent e) {
        Point pnt;
        Calendar date = e.getNewDate();
        switch (e.getType()) {
        case MMonthEvent.SELECTED:
            if (view != null)
                view.setVisible(false);
            break;
        default:
            break;
        }
    }

    public void actionPerformed(ActionEvent event) {
        cancelled = false;
        String command = event.getActionCommand();
        if (command.equals("today")) {
            panel.setDMY(today);
        }
        if (command.equals("ok")) {
            // return with the selected date from the model
            view.setVisible(false);
        }
        if (command.equals("cancel")) {
            // return with the date as passed in
            cancelled = true;
            view.setVisible(false);
        }
    }

    /**
     * Returns the currently selected date value
     * 
     * @return the date selected
     */
    public Date getValue() {
        if (cancelled) {
            return returnDate;
        } else {
            return panel.getDate();
        }
    }
}

// $Log: MDateSelector.java,v $
// Revision 1.3  2006/06/05 12:31:44  bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting tab fix
//
// Revision 1.2 2006/05/31 14:26:05 bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting
//
// Revision 1.1 2004/04/02 15:43:59 sz0t7n
// Adding mseries to the build so we can bug fix it from now on
//
// Revision 1.6 2002/08/17 20:45:19 martin
// Reformatted with intellij
//
// Revision 1.5 2002/08/17 20:40:25 martin
// Added call to new cosntructor of MDateSelectorPanel with lazy attribute
//
// Revision 1.4 2002/03/03 10:07:31 martin
// Removed "Use MDateFormat throughout" changes
//
// Revision 1.3 2002/03/03 09:33:41 martin
// Use MDateFormat throughout
//
