//package uk.gov.courtservice.xhibit.client.publicdisplayconfig.test;
//
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowEvent;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.UIManager;
//
//import uk.gov.courtservice.xhibit.client.publicdisplayconfig.PublicDisplayConfigController;
//import uk.gov.courtservice.framework.exception.CSRecoverableException;
//import uk.gov.courtservice.xhibit.client.util.XFrame;
//import uk.gov.courtservice.xhibit.client.util.XAction;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
//import javax.security.auth.login.*;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.LoginHelperForTestCases;
//
//
///**
// * <p>Title: XHIBIT 2 - Public Display</p>
// * <p>Description: Test harness for running configuration controller.
// * To run with login, pass a parameter main and make sure the server is running.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Rakesh Lakhani
// * @version $Id: TestPublicDisplayConfigController.java,v 1.4 2006/07/04 07:23:32 xzfdtb Exp $
// */
//
//public class TestPublicDisplayConfigController
//{
//
//    public TestPublicDisplayConfigController()
//            throws CSRecoverableException
//    {
//        TestFrame frame = new TestFrame();
//        frame.setVisible(true);
//    }
//
//    public static void main(String[] args)
//            throws Exception
//    {
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//
//        TestPublicDisplayConfigController testPublicDisplayConfigurationController1 = new TestPublicDisplayConfigController();
//    }
//
//    class TestFrame extends XFrame
//    {
//        public TestFrame() throws CSRecoverableException
//        {
//            this.getContentPane().add(new TestPanel(this));
//            this.setTitle("Public Display Configurator Test Launcher");
//            pack();
//        }
//
//        class TestPanel extends JPanel
//        {
//            final Insets defaultInsets = new Insets(4,4,4,4);
//
//            final JButton login = new JButton("Login");
//            final JButton launch = new JButton("Launch Configurator");
//
//            public TestPanel(final java.awt.Frame parent)
//                    throws CSRecoverableException
//            {
//                super(new GridBagLayout());
//                login.addActionListener(new XAction()
//                {
//                    public void xActionPerformed(ActionEvent e)
//                    {
//                        try
//                        {
//                            LoginHelperForTestCases.login();
//                            JOptionPane.showMessageDialog(parent,
//                                    "Login successful", "Login Successful",
//                                    JOptionPane.INFORMATION_MESSAGE);
//                        }
//                        catch (Exception ex)
//                        {
//                            JOptionPane.showMessageDialog(parent,
//                                    "Login failed\n" + ex.getMessage(), "Login failed",
//                                    JOptionPane.ERROR_MESSAGE);
//                        }
//                    }
//                });
//
//                launch.addActionListener(new XAction()
//                {
//                    public void xActionPerformed(ActionEvent e)
//                    {
//                        try
//                        {
//                            PublicDisplayConfigController.showConfigurationController(parent);
//                        }
//                        catch (CSRecoverableException ex)
//                        {
//                            JOptionPane.showMessageDialog(parent,
//                                   "Initialisation failed\n" + ex.getMessage(), "Error Initialising Configurator",
//                                    JOptionPane.ERROR_MESSAGE);
//                        }
//                    }
//                });
//                this.add(login,  new GridBagConstraints(0, 0, 1, 1, 0, 0,
//                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
//                        defaultInsets, 5, 5));
//                this.add(launch, new GridBagConstraints(1, 0, 1, 1, 0, 0,
//                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
//                        defaultInsets, 5, 5));
//            }
//        }
//
//        //Overridden so we can exit when window is closed
//        protected void processWindowEvent(WindowEvent e) {
//            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//                try
//                {
//                    XhibitSingleton.getInstance().getUserSession().logout();
//                }
//                catch (LoginException ex)
//                {
//                }
//                System.exit(0);
//            } else {
//                super.processWindowEvent(e);
//            }
//        }
//    }
//}