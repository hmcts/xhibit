//package uk.gov.courtservice.xhibit.client.test;
//
////import javax.security.auth.callback.CallbackHandler;
//import javax.security.auth.callback.Callback;
//import javax.security.auth.callback.NameCallback;
//import javax.security.auth.callback.PasswordCallback;
//import javax.security.auth.callback.TextOutputCallback;
//
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitCallbackHandler;
///**
// * <p>Title:  XHIBIT 2</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author unascribed
// * @version 1.0
// */
//
//public class TestCallbacks {
//
//    public TestCallbacks() {
//        Callback[] c = new Callback[5];
//        c[0] = new TextOutputCallback(TextOutputCallback.INFORMATION, "Information Message");
//        c[1] = new TextOutputCallback(TextOutputCallback.WARNING, "Warning Message");
//        c[2] = new TextOutputCallback(TextOutputCallback.ERROR, "Error Message");
//        c[3] = new NameCallback("Username", "Rakesh");
//        c[4] = new PasswordCallback("Password", false);
////        c[5] = new URLCallback("Username", "Password");
//        XhibitCallbackHandler xh = new XhibitCallbackHandler();
//        try {
//            xh.handle(c);
//            
//            
//        } catch (java.io.IOException ie) {
//            System.err.println(ie.getMessage());
//        } catch (javax.security.auth.callback.UnsupportedCallbackException ex) {
//            System.err.println(ex.getMessage());
//        } finally {
//            System.exit(0);
//        }
//    }
//    public static void main(String[] args) {
//        TestCallbacks testCallbacks1 = new TestCallbacks();
//    }
//}