//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Frame;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.exception.CSRecoverableException;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
//import uk.gov.courtservice.xhibit.client.order.exceptions.NoDataEnteredException;
//import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderExistsDialog;
//import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderListDialog;
//import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSavedDialog;
//import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSignedDialog;
//import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersPanelFactory;
//import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
//import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
//import uk.gov.courtservice.xhibit.client.util.XDialog;
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
//public class OrdersDialogTest {
//    private static final Logger log = CSServices.getLogger(OrdersDialogTest.class);
//
//    private OrdersScreenFactory osf = null;
//
//    private OrdersPanelFactory opf = null;
//
//    /**
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        final OrdersDialogTest ordersDialog1 = new OrdersDialogTest();
//
//        final JFrame frame = new JFrame("Dialog Test");
//
//        JButton button1 = new JButton("Exists");
//        button1.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                XDialog dialog = null;
//                try {
//                    dialog = ordersDialog1.testOrderExists();
//                } catch (CSRecoverableException ex) {
//                    log.debug(ex.getMessage());
//                }
//                dialog.setSize(new Dimension(800, 600));
//                dialog.setVisible(true);
//                dialog = null;
//            }
//        });
//
//        JButton button2 = new JButton("Signed");
//        button2.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                XDialog dialog = null;
//                try {
//                    dialog = ordersDialog1.testOrderSigned();
//                } catch (CSRecoverableException ex) {
//                    log.debug(ex.getMessage());
//                }
//                dialog.setLocationRelativeTo(frame);
//                dialog.setSize(new Dimension(400, 300));
//                dialog.setVisible(true);
//                String signedBy = null;
//                String signedDate = null;
//                try {
//                    signedBy = ((OrderSignedDialog) dialog).getSignDetails().getSignedSurname();
//                    signedDate = ((OrderSignedDialog) dialog).getSignDetails().getSignedDate();
//                    log.debug("Return from dialog: " + signedBy + " " + signedDate);
//                } catch (CancelledByUserException cbue) {
//                    log.debug(cbue.getMessage());
//                }
//                dialog = null;
//            }
//        });
//
//        JButton button3 = new JButton("Saved");
//        button3.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                XDialog dialog = null;
//                try {
//                    dialog = ordersDialog1.testOrderSaved();
//                } catch (CSRecoverableException ex) {
//                    log.debug(ex.getMessage());
//                }
//                dialog.setSize(new Dimension(400, 300));
//                dialog.setLocationRelativeTo(frame);
//                dialog.setVisible(true);
//                try {
//                    String result = ((OrderSavedDialog) dialog).getSavedText();
//                    log.debug("Return from dialog: " + result);
//                } catch (CancelledByUserException cbue) {
//                    log.debug(cbue.getMessage());
//                } catch (NoDataEnteredException nde) {
//                    log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@" + nde.getMessage());
//                }
//                dialog = null;
//            }
//        });
//
//        JButton button4 = new JButton("List");
//        button4.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                XDialog dialog = null;
//                try {
//                    dialog = ordersDialog1.testOrderList();
//                } catch (CSRecoverableException ex) {
//                    log.debug(ex.getMessage());
//                }
//                dialog.setSize(new Dimension(600, 400));
//                dialog.setVisible(true);
//                dialog = null;
//            }
//        });
//
//        frame.getContentPane().setLayout(new FlowLayout());
//
//        frame.getContentPane().add(button1);
//        frame.getContentPane().add(button2);
//        frame.getContentPane().add(button3);
//        frame.getContentPane().add(button4);
//        frame.setSize(new Dimension(800, 400));
//        frame.setVisible(true);
//
//    }
//
//    /**
//     *
//     * @return
//     * @throws CSRecoverableException
//     */
//    private XDialog testOrderExists() throws CSRecoverableException {
//        return new OrderExistsDialog(new Frame(""), "OrderExistsDialog", new OrderInitialDataVO(), false);
//    }
//
//    /**
//     *
//     * @return
//     * @throws CSRecoverableException
//     */
//    private XDialog testOrderSigned() throws CSRecoverableException {
//        return new OrderSignedDialog(null, "Orders - Signed Dialog", true, null);
//    }
//
//    /**
//     *
//     * @return
//     * @throws CSRecoverableException
//     */
//    private XDialog testOrderSaved() throws CSRecoverableException {
//        return new OrderSavedDialog(new Frame(""), "Orders - Saved Dialog");
//    }
//
//    /**
//     *
//     * @return
//     * @throws CSRecoverableException
//     */
//    private XDialog testOrderList() throws CSRecoverableException {
//        return new OrderListDialog(new Frame(""), "OrderListDialog", new OrderInitialDataVO(), false);
//    }
//}