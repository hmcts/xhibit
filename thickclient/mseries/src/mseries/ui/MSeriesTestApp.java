package mseries.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * System.out.println's show that MDateEntryField does not recognize the
 * instanceof properly.
 * 
 * In addition, after using the MDateEntryField component, focus traversal does
 * not work. Try selecting a date and use the tab key.
 */
public class MSeriesTestApp extends JFrame {
    private BorderLayout borderLayout1 = new BorderLayout();

    private JPanel jPanel1 = new JPanel();

    private JLabel jLabel1 = new JLabel();

    private JLabel jLabel2 = new JLabel();

    private JTextField jTextField1 = new JTextField();

    private MDateEntryField mDateEntryField1 = new MDateEntryField();

    private JTextField jTextField2 = new JTextField();

    private JLabel jLabel3 = new JLabel();

    private MSeriesTestInputVerifier iv = new MSeriesTestInputVerifier();

    public MSeriesTestApp() {
        try {
            jbInit();
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Container contentPane = this.getContentPane();
            // contentPane.setFocusable(false);
            final Component order[] = new Component[] { jTextField1, mDateEntryField1, jTextField2 };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MSeriesTestApp mSeriesTestApp = new MSeriesTestApp();
        mSeriesTestApp.setVisible(true);
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        jPanel1.setLayout(null);
        mDateEntryField1.setBounds(new Rectangle(101, 57, 78, 21));
        jLabel1.setText("jLabel1");
        jLabel1.setBounds(new Rectangle(16, 34, 58, 15));
        jLabel2.setText("jLabel2");
        jLabel2.setBounds(new Rectangle(16, 63, 58, 15));
        jLabel3.setText("jLabel3");
        jLabel3.setBounds(new Rectangle(16, 94, 58, 15));

        jTextField1.setInputVerifier(iv);
        jTextField1.setName("jTextField1");
        jTextField1.putClientProperty("MNEMONIC", "jTextField1");
        jTextField1.setBounds(new Rectangle(101, 28, 78, 21));

        jTextField2.setInputVerifier(iv);
        jTextField2.setName("jTextField2");
        jTextField2.putClientProperty("MNEMONIC", "jTextField2");
        jTextField2.setBounds(new Rectangle(101, 88, 78, 21));

        mDateEntryField1.setInputVerifier(iv);
        mDateEntryField1.setName("mDateEntryField1");
        mDateEntryField1.putClientProperty("MNEMONIC", "mDateEntryField1");

        jPanel1.setMaximumSize(new Dimension(300, 300));
        jPanel1.setMinimumSize(new Dimension(300, 300));
        jPanel1.setPreferredSize(new Dimension(300, 300));
        this.setSize(new Dimension(300, 300));
        jPanel1.add(jLabel1, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(jLabel3, null);
        jPanel1.add(mDateEntryField1, null);
        jPanel1.add(jTextField2, null);
        jPanel1.add(jTextField1, null);
        this.getContentPane().add(jPanel1, BorderLayout.EAST);
    }

    public class MSeriesTestInputVerifier extends InputVerifier {
        public boolean verify(JComponent input) {
            boolean retval = false;
            System.out.println("input.getClass() = " + input.getClass());
            // the getName call returns null
            System.out.println("input.getName() = " + input.getName());
            // the getClientProperty call returns null
            System.out.println("input.getClientProperty(MNEMONIC) = " + input.getClientProperty("MNEMONIC"));
            if (input instanceof MDateEntryField) {
                System.out.println("MDateEntryField");
                retval = true;
            } else if (input instanceof JTextField) {
                System.out.println("JTextField");
                retval = true;
            }
            return retval;
        }

    }
}