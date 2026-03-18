//package uk.gov.courtservice.xhibit.client.test;
//
//import java.awt.Dimension;
//import java.awt.Frame;
//import java.awt.Toolkit;
//
//import javax.swing.UIManager;
//
///**
// * <p>Title:  XHIBIT 2</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Rakesh Lakhani
// * @version 1.0
// */
//
//public class TestApplication {
//    private boolean packFrame = false;
//
//    public TestApplication() {
//        Frame frame = new FrameCopyPasteTest();
//        frame.setSize(new Dimension(550,300));
//        //Validate frames that have preset sizes
//        //Pack frames that have useful preferred size info, e.g. from their layout
//        if (packFrame) {
//            frame.pack();
//        }
//        else {
//            frame.validate();
//        }
//        //Center the window
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension frameSize = frame.getSize();
//        if (frameSize.height > screenSize.height) {
//            frameSize.height = screenSize.height;
//        }
//        if (frameSize.width > screenSize.width) {
//            frameSize.width = screenSize.width;
//        }
//        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
//        frame.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        TestApplication testApplication1 = new TestApplication();
//    }
//}