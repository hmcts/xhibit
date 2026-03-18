package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;
//
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
//
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
//
///**
// * <p>
// * Title: DefaultDataComponent
// * </p>
// * <p>
// * Description: Use a label for the data
// * </p>
// * <p>
// * Copyright: Copyright (c) 2004
// * </p>
// * <p>
// * Company: Electronic Data Systems
// * </p>
// *
// * @author William Fardell, Xdevelopment (2004)
// * @version $Id: V1DataComponentTest.java,v 1.4 2006/07/13 12:58:05 xzfdtb Exp $
// */
public class V1DataComponentTest {
  /**
//     * Used to test the V1DataComponent
//     *
//     * @param args
//     *            the command line arguments, these are not used
     */
    public static void main(String[] args) throws Exception {
        // Set look and feel to system look and feel.
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Create the test frame
        JFrame frame = createTestFrame();

        // Pack. Center. Show!
        // frame.setSize(200, 100);
        frame.pack();
        Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
        frame.setLocation(bounds.x + ((bounds.width - frame.getWidth()) / 2), bounds.y
                + ((bounds.height - frame.getHeight()) / 2));
        frame.setVisible(true);
    }

    private static JFrame createTestFrame() {
        final JFrame frame = new JFrame("Test: " + V1DataComponent.class.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());
        // Create Number Input
        V3DataComponent v3 = new V3DataComponent();
        frame.getContentPane().add(
                v3,
                new GridBagConstraints(0, 0, 10, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(4, 4, 2, 4), 0, 0));
        // Create Unit Input
        V1DataComponent v1 = new V1DataComponent();
        v1.setPreviousDataComponent(v3);
        frame.getContentPane().add(
                v1,
                new GridBagConstraints(0, 1, 10, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 4, 2, 4), 0, 0));

        // Create Scale
        for (int i = 0; i < 10; i++) {
            frame.getContentPane().add(
                    new JLabel(String.valueOf(i)),
                    new GridBagConstraints(i, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(4, 2, 4, 2), 0, 0));
        }

        return frame;
    }
}
