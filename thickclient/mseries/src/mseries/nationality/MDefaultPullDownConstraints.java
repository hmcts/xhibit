
package mseries.nationality;

import java.awt.Color;
import java.awt.Font;
import java.util.ResourceBundle;


/**
 * The default implementation of MNationalitySelectorConstraints.
 */
public class MDefaultPullDownConstraints implements MNationalitySelectorConstraints {
    public String bundleName = "mseries.nationality.NationalitySelectorRB";

    public ResourceBundle rb;

    public Color foreground = null;

    public Color background = null;

    public Font font = null;

    public boolean hasShadow = false;


    public MDefaultPullDownConstraints() {
        // empty
    }

    public ResourceBundle getResourceBundle() {
        if (rb == null) {
            rb = ResourceBundle.getBundle(bundleName);
        }
        return rb;
    }

    public Color getForeground() {
        return foreground;
    }

    public Color getBackground() {
        return background;
    }

    public Font getFont() {
        return font;
    }

    public boolean hasShadow() {
        return hasShadow;
    }
}
