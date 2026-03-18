package uk.gov.courtservice.xhibit.client.crestformsbf.swing.icon;

import javax.swing.Icon;

/**
 * Icon Factory creates icons <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public abstract class IconFactory implements Icon {

    /**
     * An icon showing a pressed up arrow.
     */
    public static final int UP_ARROW_PRESSED = 0;

    /**
     * An icon showing an unpressed up arrow.
     */
    public static final int UP_ARROW_UNPRESSED = 1;

    /**
     * An icon showing a pressed down arrow.
     */
    public static final int DOWN_ARROW_PRESSED = 2;

    /**
     * An icon showing an unpressed down arrow.
     */
    public static final int DOWN_ARROW_UNPRESSED = 3;

    /**
     * Icons indexed by their id
     */
    private static final Icon[] ICON_CACHE = new Icon[] { new UpArrowIcon(true), new UpArrowIcon(false),
            new DownArrowIcon(true), new DownArrowIcon(false) };

    /**
     * Stops instances of this class from being created
     */

    public static Icon getIcon(int iconId) {
        try {
            return ICON_CACHE[iconId];
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new IllegalArgumentException("Icon " + iconId + " was not found.");
        }
    }

    /**
     * Test method
     * 
     * @param args
     *            unused command line arguments
     */
    public static void main(String[] args) throws Exception {
        final int COLUMN_COUNT = 5;
        final int ROW_COUNT = 5;

        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

        javax.swing.JFrame frame = new javax.swing.JFrame("Sortable Table Test");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new java.awt.GridBagLayout());

        javax.swing.JPanel contentPane = new javax.swing.JPanel(new java.awt.GridLayout(0, 2));

        contentPane.add(new javax.swing.JLabel("Up Pressed: ", getIcon(UP_ARROW_PRESSED),
                javax.swing.SwingConstants.CENTER));
        contentPane.add(new javax.swing.JLabel("Up Unpressed: ", getIcon(UP_ARROW_UNPRESSED),
                javax.swing.SwingConstants.CENTER));

        contentPane.add(new javax.swing.JLabel("Down Pressed: ", getIcon(DOWN_ARROW_PRESSED),
                javax.swing.SwingConstants.CENTER));
        contentPane.add(new javax.swing.JLabel("Down Unpressed: ", getIcon(DOWN_ARROW_UNPRESSED),
                javax.swing.SwingConstants.CENTER));

        frame.setContentPane(contentPane);
        frame.show();

    }

}
