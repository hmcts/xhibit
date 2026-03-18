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
import java.util.Calendar;
import java.util.Date;

public class TestSpecialDayModel extends DefaultSpecialDayModel {

    /**
     * By default no dates are special
     * 
     * @return false
     */
    public boolean isSpecialDay(Date date) {
        Calendar cx = Calendar.getInstance();
        cx.set(2002, 5, 12);

        if (MMonth.compDates(date, cx) == 0) {
            return true;
        }
        return false;
    }

    public Color getForeground(Date date) {
        return Color.white;
    }
}
/*
 * $Log: TestSpecialDayModel.java,v $
 * Revision 1.3  2006/06/05 12:31:45  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.2 2006/05/31 14:26:06 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.1 2004/04/02 15:44:02 sz0t7n Adding mseries to the build so we can bug fix
 * it from now on
 * 
 * Revision 1.1 2002/02/09 12:54:39 martin Partial support for 'Special Days'
 * 
 */
