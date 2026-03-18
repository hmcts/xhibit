//package uk.gov.courtservice.xhibit.web.framework.util;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//
//public class UrlUtilTest extends TestCase
//{
//
//    public UrlUtilTest(String testName)
//    {
//        super(testName);
//    }
//
//
//    public static Test suite()
//    {
//        return new TestSuite(UrlUtilTest.class);
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
//    public void testGetUrlResourceAsString()
//    {
//        try
//        {
//            String contents = UrlUtil.getUrlResourceAsString("http://www.eds.com/");
//
//            int startIndex = contents.indexOf("<title>");
//            int endIndex = contents.indexOf("</title>");
//
//            if(startIndex == -1 || endIndex == -1) {
//                System.out.println("contents: " + contents);
//                fail("Not eds home page.");
//            }
//            else
//            {
//                endIndex = endIndex + 8; // "</title>".length
//
//                String title = contents.substring(startIndex, endIndex);
//                System.out.println("title: " + title);
//                assertEquals("<title>Welcome to eds.com</title>", title);
//            }
//        }
//        catch (Exception e)
//        {
//            fail(ExceptionUtil.getStackTraces(e));
//        }
//    }
//}
//
//