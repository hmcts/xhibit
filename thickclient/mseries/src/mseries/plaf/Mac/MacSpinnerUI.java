/*
 *   Copyright (c) 2003 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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
 *
 *   Created on Jan 14, 2003 at 8:14:39 PM by martin
 *   Commited on $Date: 2006/06/05 12:31:45 $
 */
package mseries.plaf.Mac;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import mseries.plaf.basic.BasicSpinnerUI;

public class MacSpinnerUI extends BasicSpinnerUI {
    /**
     * This method is called by the UIManager to get an instance of this class
     * and must be overridden in subclasses.
     */
    public static ComponentUI createUI(JComponent x) {
        return new MacSpinnerUI();
    }

}
