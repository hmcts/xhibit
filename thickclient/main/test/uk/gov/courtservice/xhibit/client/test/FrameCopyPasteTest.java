//package uk.gov.courtservice.xhibit.client.test;
//
//import java.awt.BorderLayout;
//import java.awt.FlowLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.KeyEvent;
//import java.awt.event.WindowEvent;
//
//import javax.swing.Action;
//import javax.swing.JButton;
//import javax.swing.JDialog;
//import javax.swing.JFrame;
//import javax.swing.JMenu;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.JToolBar;
//import javax.swing.KeyStroke;
//
//import uk.gov.courtservice.xhibit.client.actions.common.CopyAction;
//import uk.gov.courtservice.xhibit.client.actions.common.CutAction;
//import uk.gov.courtservice.xhibit.client.actions.common.PasteAction;
//import uk.gov.courtservice.xhibit.client.listeners.XhibitListeners;
//import uk.gov.courtservice.xhibit.client.util.XAction;
//import uk.gov.courtservice.xhibit.client.util.XToolbarButton;
//
///**
// * <p>Title:  XHIBIT 2</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Rakesh Lakhani
// * @version 1.0
// */
//public class FrameCopyPasteTest extends JFrame
//{
//    private JPanel jPanel1 = new JPanel(new BorderLayout());
//
//    public FrameCopyPasteTest()
//    {
//        try
//        {
//            jbInit();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void addMenu()
//    {
//        JMenuBar jmb = new JMenuBar();
//        JMenu jmEdit = new JMenu("Edit");
//        jmEdit.setMnemonic(KeyEvent.VK_E);
//        jmb.add(jmEdit);
//        jmEdit.add(getMenuItem(CopyAction.getInstance()));
//        jmEdit.add(getMenuItem(CutAction.getInstance()));
//        jmEdit.add(getMenuItem(PasteAction.getInstance()));
//        setJMenuBar(jmb);
//    }
//
//    private void jbInit() throws Exception
//    {
//        addMenu();
//        //        this.getContentPane().add(getToolBar(), BorderLayout.NORTH);
//        JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        jp.add(getToolBar(XToolbarButton.xbIcon + XToolbarButton.xbText, JToolBar.HORIZONTAL));
//        //        jp.add(getToolBar(XToolbarButton.xbText));
//
//        this.getContentPane().add(jp, BorderLayout.NORTH);
//        this.getContentPane().add(
//            getToolBar(XToolbarButton.xbText, JToolBar.VERTICAL),
//            BorderLayout.WEST);
//        this.getContentPane().add(getMainPanel(), BorderLayout.CENTER);
//    }
//
//    public JToolBar getToolBar(int buttonFormat, int orientation)
//    {
//        JToolBar tb = new JToolBar("Main", orientation);
//        //        tb.setLayout(new FlowLayout(FlowLayout.LEFT));
//        tb.add(getToolButton(CopyAction.getInstance(), buttonFormat));
//        tb.add(getToolButton(CutAction.getInstance(), buttonFormat));
//        tb.add(getToolButton(PasteAction.getInstance(), buttonFormat));
//        return tb;
//    }
//
//    private XToolbarButton getToolButton(Action a, int buttonFormat)
//    {
//        XToolbarButton tb1 = new XToolbarButton(buttonFormat);
//        tb1.setAction(a);
//        //        tb1.setText("");
//        //        tb1.setToolTipText(CopyAction.getInstance().getShortDescription());
//        return tb1;
//    }
//
//    private JMenuItem getMenuItem(Action a)
//    {
//        JMenuItem mi = new JMenuItem();
//        mi.setAction(a);
//        if (a.getValue(Action.ACCELERATOR_KEY) != null)
//        {
//            mi.setAccelerator((KeyStroke)a.getValue(Action.ACCELERATOR_KEY));
//        }
//        return mi;
//    }
//
//    public JPanel getMainPanel()
//    {
//        JTextField jTextField1 = new JTextField(40);
//        JTextField jTextField2 = new JTextField(40);
//        JButton jb = new JButton();
//        JPanel jp = new JPanel(new FlowLayout());
//
//        jTextField1.setText("jTextField1");
//        jTextField2.setText("jTextField2");
//        jb.setAction(new XAction()
//        {
//            public void xActionPerformed(ActionEvent ae)
//            {
//                JDialog d = new TestDialog();
//                d.setVisible(true);
//            }
//        });
//        jb.setText("Pop");
//        CopyAction.getInstance().checkState();
//        CutAction.getInstance().checkState();
//        PasteAction.getInstance().checkState();
//        jp.add(jTextField1);
//        jp.add(jTextField2);
//        jp.add(jb);
//        XhibitListeners.setDefaultListeners(jp);
//        return jp;
//    }
//
//    //Overridden so we can exit when window is closed
//    protected void processWindowEvent(WindowEvent e)
//    {
//        super.processWindowEvent(e);
//        if (e.getID() == WindowEvent.WINDOW_CLOSING)
//        {
//            System.exit(0);
//        }
//    }
//}