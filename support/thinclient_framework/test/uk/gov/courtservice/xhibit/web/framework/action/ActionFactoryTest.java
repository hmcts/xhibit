//package uk.gov.courtservice.xhibit.web.framework.action;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//
//import uk.gov.courtservice.xhibit.web.framework.util.ExceptionUtil;
//
//public class ActionFactoryTest extends TestCase
//{
//
//    public ActionFactoryTest(String testName)
//    {
//        super(testName);
//    }
//
//
//    public static Test suite()
//    {
//        return new TestSuite(ActionFactoryTest.class);
//    }
//
//    public static void main(String args[])
//    {
//        TestRunner.run(suite());
//    }
//
//    public void setUp()
//    {
//    }
//
//    public void tearDown()
//    {
//    }
//
//    public void testGetInstance()
//    {
//        try
//        {
//            System.out.println(new ActionFactory("TestAction.properties"));
//        }
//        catch (Exception e)
//        {
//            fail(ExceptionUtil.getStackTraces(e));
//        }
//    }
//}
//
//