
package mseries.nationality;

import java.awt.Color;
import java.awt.Font;
import java.util.ResourceBundle;

/**
 * Modelled on mseries.Calendar.MDefaultPullDownConstraints
 */

public interface MNationalitySelectorConstraints {
    /** @return the resource bundle for the localisation */
    public ResourceBundle getResourceBundle();

    /** @return the default foreground */
    public Color getForeground();

    /** @return the default background */
    public Color getBackground();

    /** @return the default font */
    public Font getFont();

    /**
     * @return true if the lightweight popup is to have a shadow. Heavyweights
     *         never have shadows. Heavyweights over lap their parents
     *         boundaries.
     */
    public boolean hasShadow();
}

