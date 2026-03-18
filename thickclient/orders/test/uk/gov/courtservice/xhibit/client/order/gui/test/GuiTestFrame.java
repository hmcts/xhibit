//package uk.gov.courtservice.xhibit.client.order.gui.test;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.Toolkit;
//import java.io.IOException;
//import java.net.URL;
//
//
//import javax.swing.JFrame;
//import javax.swing.text.BadLocationException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.client.order.exceptions.DataEntryReaderException;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderPreviewException;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
//import uk.gov.courtservice.xhibit.client.order.gui.general.OrderGUI;
//import uk.gov.courtservice.xhibit.client.order.test.Resource;
//
///**
// * <p>
// * Title: Prototype
// * </p>
// * <p>
// * Description: Xhibit Iteration 2 Prototype
// * </p>
// * <p>
// * Copyright: Copyright (c) 2002
// * </p>
// * <p>
// * Company: EDS
// * </p>
// * 
// * @author David Duncan, Desmon Johnston, Neil Ellis, Neil Entwistle
// * @version 1.0
// */
//
//public class GuiTestFrame extends JFrame {
//    private static final Logger log = CSServices.getLogger(GuiTestFrame.class);
//
//    // contains locations of all source xml files.
//
//    // private static ResourceBundle res =
//    // ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.misc.Resource");
//
//    // private static ResourceBundle res =
//    // ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.misc.Resource");
//
//    // primary Panel for the Orders section of the Xhibit gui.
//    private OrderGUI gui;
//
//    /**
//     * Construct a GuiTestFrame from three strings representing the input
//     * streams connected to all xml data required.
//     * 
//     * @param dataURL
//     *            url for the test xml file.
//     * @param dataEntryURL
//     *            url for the data template xml file.
//     * @param translationURL
//     *            url for the xslt transform file.
//     * @throws DataEntryReaderException
//     *             thrown when the DataEntryReader cannot load a DataEntryPanel.
//     * @throws OrderReaderException
//     *             thrown when attempting to read an order.
//     * @throws OrderPreviewException
//     *             thrown when dealing with the Order Preview functionality.
//     * @throws IOException
//     *             IOException
//     * @throws OrderTransformException
//     *             thrown when transforming an order to a specific display or
//     *             print format.
//     * @throws BadLocationException
//     *             BadLocationException
//     */
////    public GuiTestFrame(String dataURL, String dataEntryURL, String translationURL, String narrativeURL)
////            throws DataEntryReaderException, OrderReaderException, OrderPreviewException, IOException,
////            OrderTransformException, BadLocationException {
////        super(Resource.getOrdersClientBundle("test.orderscreen"));
////        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
////        this.getContentPane().setLayout(new BorderLayout());
////        Toolkit kit = getToolkit();
////        Dimension wndSize = kit.getScreenSize();
////        setBounds(0, 0, wndSize.width, wndSize.height - 30);
////        setVisible(true);
////
////        try {
////            gui = new OrderGUI(new URL(dataURL).openStream(), new URL(dataEntryURL).openStream(), translationURL,
////                    new URL(narrativeURL).openStream());
////        } catch (Exception e) {
////            e.printStackTrace();
////            // JDialog dialog = new JDialog(this, e.getMessage());
////            // this.getContentPane().add(dialog);
////            // dialog.toFront();
////            // dialog.setBounds(0, 0, wndSize.width, wndSize.height - 30);
////            // dialog.setVisible(true);
////
////        }
////        if (gui == null) {
////            log.debug("Gui is null***************");
////        }
////        this.getContentPane().add(gui, BorderLayout.CENTER);
////    }
//
//    /**
//     * Create GuiTestFrame with preferred screen size.
//     * 
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        try {
//
//            GuiTestFrame guiTestFrame = new GuiTestFrame(args[0], args[1], args[2], args[3]);
//
//            // fine tune screen size.
//            guiTestFrame.validate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
