
package mseries.ui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import javax.swing.JTextField;
import mseries.nationality.MNationalitySelector;
import mseries.nationality.MNationalitySelectorPanel;


/*
 * MNationalityField for the display and editing of a nationality.
 * Modeled on the MDateField for the display and editing of a date.
 */
public class MNationalityField extends JTextField {
    
    private static final long serialVersionUID = 1L;

    private MouseListener mouseListener;
    
    private Collection<String> nationalities;

    private boolean hasPopup = false;

    public static final int NATIONALITY_CHARS = 3;
    
    
    public MNationalityField(Collection<String> nationalities) {
        this.nationalities = nationalities;
        this.setColumns(NATIONALITY_CHARS);
        MNationalitySelectorPanel.setNationalities(nationalities);
    }


    public boolean hasPopup() {
        return this.hasPopup;
    }

    /**
     * @param hasPopup
     */
    public void setPopup(boolean hasPopup) {
        pSetPopup(hasPopup);
    }

    private void pSetPopup(boolean hasPopup) {
        /* Already has a popup and we want one */
        if (hasPopup && this.hasPopup) {
            return;
        }
        /* Don't want a popup and haven't got one */
        if (!hasPopup && !this.hasPopup) {
            return;
        }
        /* Don't want a popup but it has one */
        if (!hasPopup && this.hasPopup) {
            this.removeMouseListener(mouseListener);
            this.hasPopup = false;
        }

        /* User can right click on the textfield to get a nationality popup */
        mouseListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    doPopup(e.getPoint());
                }
            }

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    doPopup(e.getPoint());
                }
            }

            private void doPopup(Point p) {
                MNationalitySelector popup = new MNationalitySelector();

                p.x += getBounds(null).x;
                p.y += getBounds(null).y;
                popup.show(getParent(), p, getValue());

                setValue(popup.getValue());
                popup = null;
                requestFocus();

            }
        };
        addMouseListener(mouseListener);
        this.hasPopup = hasPopup;
    }


    public String getValue() {
        return getText();
    }


    public void setValue(String text) {
        setText(text);
    }
}

 