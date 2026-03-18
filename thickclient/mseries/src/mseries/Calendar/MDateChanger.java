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

import java.awt.event.FocusListener;
import java.util.Date;

import mseries.ui.MChangeListener;

public interface MDateChanger {

    public static final int SCROLLBAR = 2;

    public static final int SPINNER = 3;

    public static final int BUTTON = 4;

    public void setOpaque(boolean opaque);

    public void setEnabled(boolean enabled);

    public void setMinimum(Date min);

    public void setMaximum(Date min);

    public void setValue(Date min);

    public boolean hasFocus();

    /**
     * @return the number of months since the minimum
     */
    public int getValue();

    public void addMChangeListener(MChangeListener l);

    public void removeMChangeListener(MChangeListener l);

    /**
     * Adds a focus listener, using the method addFocusListener causes a
     * NullPointerException for some reason.
     * 
     * @param l
     *            a FocusListener
     */
    public void addFListener(FocusListener l);

    /**
     * Removes a focus listener, using the method removeFocusListener causes a
     * NullPointerException for some reason
     * 
     * @param l
     *            a FocusListener
     */
    public void removeFListener(FocusListener l);
}
// $Log: MDateChanger.java,v $
// Revision 1.3  2006/06/05 12:31:44  bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting tab fix
//
// Revision 1.2 2006/05/31 14:26:04 bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting
//
// Revision 1.1 2004/04/02 15:43:59 sz0t7n
// Adding mseries to the build so we can bug fix it from now on
//
// Revision 1.5 2003/01/10 18:07:50 martin
// *** empty log message ***
//

