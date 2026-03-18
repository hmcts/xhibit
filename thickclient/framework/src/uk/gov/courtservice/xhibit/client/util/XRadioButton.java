package uk.gov.courtservice.xhibit.client.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

/**
 * Button which adds additional functionality to Radio Button
 * 
 * @author Kelvin Davies
 * @version $Revision: 1.1 $
*/
    public class XRadioButton extends JRadioButton {

        // The following constants are used to Paint Focus this Looks Good for
        // windows 2000/XP
        private final Color focusBorderColor = Color.black;

        private final Stroke focusBorderStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f,
                new float[] { 1.0f, 1.0f }, 0.0f);

        /**
         * Construct a new unselected RadioButton
         */
        public XRadioButton() {
            this(false);
        }

        /**
         * Construct a new checkbox selected if specified
         */
        public XRadioButton(boolean selected) {
            this(null, selected);
        }

        /**
         * Construct a new RadioButton with the given title, selected if specified
         */
        public XRadioButton(String title, boolean selected) {
            super(title, selected);
            if (title == null || title.length() == 0) {
                setMargin(new java.awt.Insets(0, 0, 0, 0));
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            }
        }

        /**
         * Overridden to paint focus if no text otherwise dont know if it has focus
         */
        public void paint(Graphics g) {
            super.paint(g);
            if (getText().length() == 0 && hasFocus() && isFocusPainted()) {
                Graphics2D g2 = (Graphics2D) g;
                Color oldColor = g2.getColor();
                Stroke oldStroke = g2.getStroke();
                g2.setColor(focusBorderColor);
                g2.setStroke(focusBorderStroke);

                int x1 = 0;
                int y1 = 0;
                int x2 = getWidth();
                int y2 = getHeight();

                // The dashing looks better if the lines are drawn through the
                // end points.

                // g2.drawRect(x1, y1, x2 - 1, y2 - 1);

                g2.drawLine(x1, y1, x2, y1); // top
                g2.drawLine(x1, y1, x1, y2); // left
                g2.drawLine(x2 - 1, y1, x2 - 1, y2); // right
                g2.drawLine(x1, y2 - 1, x2, y2 - 1); // bottom

                g2.setStroke(oldStroke);
                g2.setColor(oldColor);
            }
        }

    }

    
    

