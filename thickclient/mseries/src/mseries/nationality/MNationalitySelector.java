
package mseries.nationality;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * Display a list of nationalities that the user can choose from.
 * Analogous on MDateSelector for the interactive selection of a date.
 */
public class MNationalitySelector implements ActionListener, MNationalityListener {

    private MNationalitySelectorPanel panel;
    
    private MNationalitySelectorUI view;

    private boolean cancelled;
    
    MNationalitySelectorConstraints constraints = new MDefaultPullDownConstraints();
    
    private String returnNationality;


    public MNationalitySelector() {
        // empty
    }

    /**
     * Sets the constraints object that contains the parameters used to
     * configure the pop up calendar
     * 
     * @param c
     *            the constraints object
     * @see #getConstraints
     */
    public void setConstraints(MNationalitySelectorConstraints c) {
        this.constraints = c;
    }

    /**
     * Gets the constraints object that contains the parameters used to
     * configure the pop up calendar
     * 
     * @see #setConstraints
     */
    public MNationalitySelectorConstraints getConstraints() {
        return this.constraints;
    }

    public void show(Component parent, Point pnt, String nationality) {
        showSelector(parent, pnt, nationality);
    }

    private void showSelector(Component parent, Point pnt, String nationality) {
        JFrame f;
        int x = 0;
        int y = 0;
        returnNationality = nationality;

        // Find the frame that is the parent of the component so
        // that the dialog can be hooked up with it
        Object frame = parent;

        while (!(frame instanceof JFrame)) {
            x += ((Component) frame).getBounds().x;
            y += ((Component) frame).getBounds().y;
            frame = ((Component) frame).getParent();
        }

        f = (JFrame) frame;
        // Construct the view
        if (panel == null) {
            panel = getDisplay(nationality);
            panel.addMNationalityListener(this);
        }
        if (view == null) {
            view = new MNationalitySelectorUI(
                    f, panel, this, constraints.getResourceBundle(), nationality);
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

        view.setVisible(true);
    }

    
    public MNationalitySelectorPanel getDisplay(String nationality) {
        this.returnNationality = nationality;
        if (panel == null) {
            panel = new MNationalitySelectorPanel(true);
            panel.setPullDownConstraints(constraints);
            panel.setFocusCycleRoot(false);
            panel.setNationality(nationality);
        }
        return panel;
    }
    

    /**
     * Reacts to all changes in the data model (MNationalityEvent) which is given by the
     * event type. from mseries.utils.MNationalityListener interface
     */
    public void dataChanged(MNationalityEvent e) {
        switch (e.getType()) {
        case MNationalityEvent.SELECTED:
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
     * Returns the currently selected nationality value
     * 
     * @return the date selected
     */
    public String getValue() {
        if (cancelled) {
            return returnNationality;
        } else {
            return panel.getNationality();
        }
    }
}
