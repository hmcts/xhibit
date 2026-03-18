//package uk.gov.courtservice.xhibit.client.xhibitapplication;
//
//import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
//
//public class TestRoamingTerminalSelectDialog
//{
//    public TestRoamingTerminalSelectDialog()
//            throws Exception
//    {
//        LoginHelperForTestCases.login();
//        RoamingTerminalSelectDialog dialog = new RoamingTerminalSelectDialog();
//        dialog.setVisible(true);
//        if (dialog.isCancelClicked())
//        {
//            System.exit(-1);
//        } else {
//            XhibitSingleton sing = XhibitSingleton.getInstance();
//            
//            
//            
//            
//            System.exit(0);
//        }
//    }
//
//    public static void main(String[] argv)
//    {
//        try {
//            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//            TestRoamingTerminalSelectDialog dialog = new TestRoamingTerminalSelectDialog();
//        }
//        catch (Exception ex) {
//            ex.printStackTrace(System.err);
//            System.exit(-99);
//        }
//    }
//}
//