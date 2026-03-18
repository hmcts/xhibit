//package uk.gov.courtservice.xhibit.client.schedule.movecase;
//
//import java.awt.Dimension;
//import java.awt.GridBagLayout;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//
//import uk.gov.courtservice.framework.exception.CSRecoverableException;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Bal Bhamra
// * @version 1.0
// */
//
//public class TestMoveCaseDialog extends JFrame
//{
//  private GridBagLayout gridBagLayout1 = new GridBagLayout();
//  private JButton launchBtn;
//
//  public TestMoveCaseDialog() throws CSRecoverableException
//  {
//    MoveCaseDialog x = new MoveCaseDialog(null,new Integer(1));
//    x.pack();
//    x.setSize(new Dimension(600,500));
//    x.setVisible(true);
//  }
//  public static void main(String[] args)
//  {
//    try {
//        TestMoveCaseDialog testMoveCaseDialog = new TestMoveCaseDialog();
//      }
//      catch (Exception ex) {
//        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.error("PROBLEM!!!");
//      }
//  }
//}