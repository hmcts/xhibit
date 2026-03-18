package uk.gov.courtservice.xhibit.client.util;

import javax.swing.Icon;

import uk.gov.courtservice.xhibit.client.util.icon.BlankIcon;
import uk.gov.courtservice.xhibit.client.util.icon.TickIcon;

/**
 * Factory for creating icons.
 * 
 * @author Will Fardell, Xdevelopment 2004
 * @version $Revision: 1.4 $
 */
public class IconFactory {
    private static Icon TICK_ICON = new TickIcon();

    private static Icon BLANK_ICON = new BlankIcon();

    /**
     * Stop creation of this utility class
     */
    private IconFactory() {
    }

    /**
     * Create a tick icon
     */
    public static Icon createTickIcon() {
        return TICK_ICON;
    }

    /**
     * Create a tick icon
     */
    public static Icon createBlankIcon() {
        return BLANK_ICON;
    }

}
