
package mseries.plaf.Mac;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

public class MacNationalityEntryUI extends mseries.plaf.basic.BasicNationalityEntryUI {
    public MacNationalityEntryUI() {
        super();
    }

    /**
     * This method is called by the UIManager to get an instance of this class
     * and must be overridden in subclasses.
     */
    public static ComponentUI createUI(JComponent x) {
        return new MacNationalityEntryUI();
    }

    public void configureDisplay(JComponent display) {
        display.setBorder(UIManager.getBorder("TextField.border"));
        display.setPreferredSize(new Dimension(75, 21));
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        nationalityEntry.setBorder(null);
    }

    protected void configureBorder(JComponent c) {
        // empty
    }

    protected JButton createArrowButton() {
        JButton x = new MacArrowButton(SwingConstants.SOUTH);
        return x;
    }
}
