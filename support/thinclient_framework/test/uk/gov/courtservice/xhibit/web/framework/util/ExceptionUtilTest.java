//package uk.gov.courtservice.xhibit.web.framework.util;
//
//import java.rmi.RemoteException;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.servlet.ServletException;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//import uk.gov.courtservice.framework.exception.CSRecoverableException;
//import uk.gov.courtservice.framework.exception.CSException;
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.framework.exception.Message;
//import uk.gov.courtservice.framework.services.validation.CSValidationException;
//
//public class ExceptionUtilTest extends TestCase
//{
//
//    public ExceptionUtilTest(String testName)
//    {
//        super(testName);
//    }
//
//
//    public static Test suite()
//    {
//        return new TestSuite(ExceptionUtilTest.class);
//    }
//
//    public static void main(String args[])
//    {
//        TestRunner.run(suite());
//    }
//
//    private Throwable leaf = null;
//    private Throwable middle = null;
//    private Throwable root = null;
//
//    public void setUp()
//    {
//        leaf = new Throwable("leaf");
//        middle = new RemoteException("middle", leaf);
//        root = new ServletException("root", middle);
//    }
//
//    public void tearDown()
//    {
//        leaf = null;
//        middle = null;
//        root = null;
//    }
//
//    public void testGetExceptions()
//    {
//        Throwable[] exceptions = ExceptionUtil.getExceptions(root);
//
//        assertEquals(3, exceptions.length);
//        assertEquals(leaf, exceptions[0]);
//        System.out.println("exceptions[0]: " + exceptions[0]);
//        assertEquals(middle, exceptions[1]);
//        System.out.println("exceptions[1]: " + exceptions[1]);
//        assertEquals(root, exceptions[2]);
//        System.out.println("exceptions[2]: " + exceptions[2]);
//    }
//
//    public void testGetExceptionList()
//    {
//        List exceptions = ExceptionUtil.getExceptionList(root);
//
//        assertEquals(3, exceptions.size());
//        assertEquals(leaf, exceptions.get(0));
//        assertEquals(middle, exceptions.get(1));
//        assertEquals(root, exceptions.get(2));
//    }
//
//    public void testGetExceptionIterator()
//    {
//        Iterator exceptions = ExceptionUtil.getExceptionIterator(root);
//
//        assertEquals(true, exceptions.hasNext());
//        assertEquals(leaf, exceptions.next());
//        assertEquals(true, exceptions.hasNext());
//        assertEquals(middle, exceptions.next());
//        assertEquals(true, exceptions.hasNext());
//        assertEquals(root, exceptions.next());
//        assertEquals(false, exceptions.hasNext());
//    }
//
//    public void testGetRootException()
//    {
//        assertEquals(leaf, ExceptionUtil.getRootException(root));
//    }
//
//    public void testCSRecoverableException()
//    {
//        CSRecoverableException cse = new CSRecoverableException("test", new Object[]{}, "test");
//        RemoteException re = new RemoteException("midtierwrapper", cse);
//        Throwable t = ExceptionUtil.getRootException(re);
//        if (t instanceof CSException)
//        {
//            assertEquals("test message", ExceptionUtil.buildMessageString((CSException)t));
//        } else {
//            fail("Root exception did not return CSException");
//        }
//    }
//
//    public void testCSUnrecoverableException()
//    {
//        CSUnrecoverableException cse = new CSUnrecoverableException(new Message("test", new Object[]{}), "test");
//        RemoteException re = new RemoteException("midtierwrapper", cse);
//        Throwable t = ExceptionUtil.getRootException(re);
//        if (t instanceof CSException)
//        {
//            assertEquals("test message", ExceptionUtil.buildMessageString((CSException)t));
//        } else {
//            fail("Root exception did not return CSException");
//        }
//    }
//
//    public void testCSValidationException()
//    {
//        CSValidationException cse = new CSValidationException("validation.general", new Object[]{}, "test");
//        RemoteException re = new RemoteException("midtierwrapper", cse);
//        Throwable t = ExceptionUtil.getRootException(re);
//        if (t instanceof CSException)
//        {
//            assertEquals("Validation of input values failed.", ExceptionUtil.buildMessageString((CSException)t));
//        } else {
//            fail("Root exception did not return CSException");
//        }
//    }
//}
//
//