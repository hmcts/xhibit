//package uk.gov.courtservice.xhibit.web.framework.response;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//
//import uk.gov.courtservice.xhibit.web.framework.util.ExceptionUtil;
//
//public class ResponseFactoryTest extends TestCase
//{
//
//    public ResponseFactoryTest(String testName)
//    {
//        super(testName);
//    }
//
//    public static Test suite()
//    {
//        return new TestSuite(ResponseFactoryTest.class);
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
//            System.out.println(new ResponseFactory("TestResponse.properties"));
//        }
//        catch (Exception e)
//        {
//            fail(ExceptionUtil.getStackTraces(e));
//        }
//    }
//}
//
//