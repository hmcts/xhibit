//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.awt.Dimension;
//import java.util.ArrayList;
//
//import javax.security.auth.login.LoginException;
//import javax.swing.JButton;
//import javax.swing.JPanel;
//import javax.swing.UIManager;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.client.CSUserSession;
//import uk.gov.courtservice.framework.exception.CSRecoverableException;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.client.order.actions.OrderCreateAction;
//import uk.gov.courtservice.xhibit.client.order.actions.OrderViewAction;
//import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
//import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
//import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitCallbackHandler;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
//
///**
// * <p>
// * Title:
// * </p>
// * <p>
// * Description:
// * </p>
// * <p>
// * Copyright: Copyright (c) 2002
// * </p>
// * <p>
// * Company:
// * </p>
// *
// * @author unascribed
// * @version 1.0
// */
//
//public class OrdersWizardTest extends XWizardDialog {
//
//    private static final Logger log = CSServices.getLogger(OrdersWizardTest.class);
//
//    /**
//     *
//     * @param frame
//     */
//    public OrdersWizardTest(java.awt.Frame frame) {
//        super(frame, "Orders Wizard", true);
//        displayButtons(this);
//    }
//
//    /**
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        try {
//            System.setProperty("java.security.auth.login.config", XHIBITConstant.getProperty(
//                    XhibitProperties.XhibitClientProject, "java.security.auth.login.config"));
//            CSUserSession csus = CSServices.getCSUserSession(new XhibitCallbackHandler());
//            csus.login();
//            XhibitSingleton.getInstance().setUserSession(csus);
//        } catch (LoginException ex) {
//            ex.printStackTrace(System.err);
//        } catch (CSRecoverableException ex) {
//            ex.printStackTrace(System.err);
//        }
//        OrdersWizardTest ordersWizard1 = new OrdersWizardTest(new java.awt.Frame("Test"));
//    }
//
//    /**
//     *
//     * @param ordersWizard1
//     */
//    public void displayButtons(OrdersWizardTest ordersWizard1) {
//        JPanel panel = new JPanel();
//        JButton button1 = new JButton("Create");
//        try {
//            button1.addActionListener(new OrderCreateAction());
//        } catch (CSRecoverableException ex) {
//            log.debug(ex.getMessage());
//        }
//        panel.add(button1);
//
//        JButton button2 = new JButton("View");
//        try {
//            button2.addActionListener(new OrderViewAction());
//        } catch (CSRecoverableException ex) {
//            log.debug(ex.getMessage());
//        }
//        panel.add(button2);
//
//        ArrayList al = new ArrayList();
//        al.add(panel);
//        ordersWizard1.addBodyPanels(al);
//
//        ordersWizard1.setSize(new Dimension(800, 600));
//        ordersWizard1.setVisible(true);
//    }
//
//    /**
//     * Sets the look and feel to match that of the main application
//     */
//    private void setLookAndFeel() {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ex) {
//            logLookAndFeelError(ex);
//        }
//    }
//
//    private void logLookAndFeelError(Exception ex) {
//        log.error("OrdersWizardTest: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
//                + ex.getMessage());
//
//    }
//
//}