/*
 *   Copyright (c) 2000 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
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
 */
package mseries.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import mseries.Calendar.MDateChanger;
import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.Calendar.MFieldListener;

public class MDateEntryFieldDemo implements Runnable {

    JFrame frame;

    public static void main(String[] argv) {
        MDateEntryFieldDemo demo = new MDateEntryFieldDemo();
        demo.run();
    }

    public void run() {
        String build = "";
        String date = "";

        // Locale.setDefault(new Locale("ru", "RU"));
        /*
         * This block of code deals with placing the build number & date in the
         * window title.
         */
        try {
            ResourceBundle rb = ResourceBundle.getBundle("build");
            build = rb.getString("build.number");
            date = rb.getString("build.date");

        } catch (Exception e) {
            System.out.println(e);
        }
        frame = new JFrame("Test Window " + build + " " + date);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        frame.setJMenuBar(createMenus());

        /***********************************************************************
         * The Demo code starts here *
         **********************************************************************/

        /*---------------- MDateEntryField ------------------*/
        Calendar cx = Calendar.getInstance();
        cx.set(2001, 5, 12);
        final MDateEntryField entryField = new MDateEntryField(10);

        // entryField.setDateFormatter(new MSimpleDateFormat("dd/MM/yyyy"));
        // entryField.setValue(cx.getTime());

        MDefaultPullDownConstraints c = new MDefaultPullDownConstraints();
        c.firstDay = Calendar.MONDAY;
        c.changerStyle = MDateChanger.BUTTON;
        // c.changerStyle=MDateChanger.SPINNER;
        // c.changerStyle=MDateChanger.SCROLLBAR;
        c.hasShadow = true;
        // c.selectionEventsEnabled=false;
        // c.todayForeground=Color.red;
        // c.todayBackground=Color.green;
        // c.imageFile="d:\\temp\\fineview.jpg";

        // Set the background colours of the pull down
        // c.background=Color.red;
        // Set the background of the entry field
        // entryField.setBackground(Color.red);
        // entryField.setShowTodayButton(true, true);

        entryField.setConstraints(c);
        // entryField.setEditable(false);
        // entryField.setEnabled(false);

        entryField.addMFieldListener(new MFieldListener() {
            public void fieldEntered(FocusEvent e) {
                System.out.println("MDateEntryField:Entered");
            }

            public void fieldExited(FocusEvent e) {
                System.out.println("MDateEntryField:Exited");
                try {
                    System.out.println("EE " + entryField.getValue());
                } catch (Exception ee) {
                    System.out.println(ee);
                }

            }
        });

        entryField.addMChangeListener(new MChangeListener() {
            public void valueChanged(MChangeEvent e) {
                System.out.println(e.getValue());
            }
        });

        entryField.setToolTipText("Click the button");

        /*---------------- MSpinner  for Dates------------------*/
        MSpinner month = new MSpinner(7);
        month.setModel(new MDateSpinnerModel());
        month.setEditor(new DateEditor("dd/MM/yyyy"));

        month.addMFieldListener(new MFieldListener() {
            public void fieldEntered(FocusEvent e) {
                System.out.println("MSpinner (Date):Entered");
            }

            public void fieldExited(FocusEvent e) {
                System.out.println("MSpinner (Date):Exited");
            }
        });

        month.addMChangeListener(new MChangeListener() {
            public void valueChanged(MChangeEvent e) {
                System.out.println("MSpinner(Date):Changed " + e.getValue());
            }
        });

        month.setToolTipText("Select a date/time portion and click a button");

        /*---------------- MDateField ------------------*/
        MDateField endDate = new MDateField();
        MDefaultPullDownConstraints c2 = new MDefaultPullDownConstraints();
        c2.firstDay = Calendar.SUNDAY;
        // c2.background=Color.red;
        endDate.setConstraints(c2);
        endDate.setDateFormatter(new MSimpleDateFormat("dd/MM/yyyy"));
        endDate.setPopup(true);
        endDate.setToolTipText("Right click me");

        /*---------------- MSpinner for Integers ------------------*/
        MSpinner integerSpinner = new MSpinner(7);
        integerSpinner.setModel(new MIntegerSpinnerModel(0, 100, 0, 1, true));
        integerSpinner.setEditor(new IntegerEditor());
        integerSpinner.addMChangeListener(new MChangeListener() {
            public void valueChanged(MChangeEvent e) {
                System.out.println("MIntegerSpinner:Changed " + e.getValue());
            }
        });

        integerSpinner.setToolTipText("Click the field, then click the buttons");

        /*---------------- MSpinner for floats ------------------*/
        MSpinner floatSpinner = new MSpinner(4);
        MFloatSpinnerModel floatModel = new MFloatSpinnerModel(10, 100, 0, (float) 0.5, true);
        FloatEditor floatEd = new FloatEditor();
        floatSpinner.setModel(floatModel);
        floatSpinner.setEditor(floatEd);

        floatSpinner.setToolTipText("Click the field, then click the buttons");

        /*---------------- MSpinner for Lists ------------------*/
        final MSpinner listSpinner = new MSpinner(10);
        String[] names = { "England", "Scotland", "N. Ireland", "Wales" };
        SpinnerModel listModel = new MListSpinnerModel(names);
        listSpinner.setModel(listModel);
        listSpinner.addMChangeListener(new MChangeListener() {
            public void valueChanged(MChangeEvent e) {
                int index = ((MListSpinnerModel.ListObject) listSpinner.getValue()).index;
                String name = (String) ((MListSpinnerModel.ListObject) listSpinner.getValue()).object;
                System.out.println(index + ", " + name);
            }
        });
        listSpinner.setToolTipText("Click the field, then click the buttons");

        /*---------------- Layout the frame ----------------*/
        GridLayout l = new GridLayout(3, 4, 7, 7);
        JPanel displayPanel = new JPanel(l);

        displayPanel.add(new JLabel("MDateEntryField"));
        displayPanel.add(entryField);
        displayPanel.add(new JLabel("MDateSelector (Right Click)"));
        displayPanel.add(endDate);
        displayPanel.add(new JLabel("MSpinner (Date)"));
        displayPanel.add(month);
        displayPanel.add(new JLabel("MSpinner (Integer)"));
        displayPanel.add(integerSpinner);
        displayPanel.add(new JLabel("MSpinner (Float)"));
        displayPanel.add(floatSpinner);
        displayPanel.add(new JLabel("MSpinner (List)"));
        displayPanel.add(listSpinner);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(displayPanel);

        frame.pack();
        frame.show();
    }

    private ButtonGroup lafMenuGroup = new ButtonGroup();

    public JMenuBar createMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu lafMenu;
        /* LAF Switching Menu */
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        if (lafs.length <= 0) {
            return menuBar;
        }
        lafMenu = menuBar.add(new JMenu("Look & Feel"));
        lafMenu.setMnemonic('L');

        for (int i = 0; i < lafs.length; i++) {
            UIManager.LookAndFeelInfo laf = lafs[i];

            String lafName = laf.getName();
            String lafClassName = laf.getClassName();
            createLafMenuItem(lafMenu, lafName, "", lafClassName);
        }
        return menuBar;
    }

    public JMenuItem createLafMenuItem(JMenu menu, String label, String mnemonic, String laf) {
        JMenuItem mi = menu.add(new JRadioButtonMenuItem(label));
        lafMenuGroup.add(mi);
        mi.addActionListener(new ChangeLookAndFeelAction(this, laf));

        return mi;
    }

    public void setLookAndFeel(String laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        } catch (Exception ex) {
            System.out.println("Failed loading L&F: " + laf);
            System.out.println(ex);
        }
    }

    class ChangeLookAndFeelAction implements ActionListener {
        MDateEntryFieldDemo m;

        String laf;

        public ChangeLookAndFeelAction(MDateEntryFieldDemo d, String laf) {
            m = d;
            this.laf = laf;
        }

        public void actionPerformed(ActionEvent e) {
            m.setLookAndFeel(laf);
        }
    }
}
