//package uk.gov.courtservice.xhibit.client.im.util;
//
//import java.util.Properties;
//
//import javax.jms.JMSException;
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// *
// * Tests for JMSContextNode class
// */
//public class TestJMSContextNode extends TestCase
//{
//
//    // logging
//    private static Logger log = Logger.getLogger(TestJMSContextNode.class.getName());
//
//    // class under test
//    private JMSContextNode node = null;
//
//    // constants
//    private static final String JNDI_NAME = "CSJMSConnectionFactory";
//
//
//    /**
//     * Create new test class
//     * @param testName  will be passed in by JUnit runner
//     */
//    public TestJMSContextNode(String testName)
//    {
//        super(testName);
//        configureLogging();
//        log.debug(">>>>  TestJMSContextNode  <<<<");
//    }
//
//    /**
//     * Have JUnit pick up all tests in this class
//     */
//    public static Test suite()
//    {
//        return new TestSuite(TestJMSContextNode.class);
//    }
//
//
//    /**
//     * Initialise variables common to each test. Re-run before each test.
//     */
//    protected void setUp()
//    {
//        try
//        {
//            node = new JMSContextNode(null, JNDI_NAME);
//            assertNotNull("node was null", node);
//        }
//        catch (NamingException e)
//        {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//        catch (JMSException e)
//        {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }
//
//
//    /**
//     * Cleanup variables used by tests. Re-run after each test.
//     */
//    protected void tearDown()
//    {
//    }
//
//
//    //
//    // tests
//    //
//
//    public void testGetJndiName()
//    {
//        log.debug("[testGetJndiName]");
//        String jndiName = node.getJndiName();
//        assertEquals("unexpected JNDI name", JNDI_NAME, jndiName);
//    }
//
//    public void testIsLeaf()
//    {
//        log.debug("[testIsLeaf]");
//        boolean isLeaf = node.isLeaf();
//        assertEquals(false, isLeaf);
//    }
//
//    public void testGetAllowsChildren()
//    {
//        log.debug("[testGetAllowsChildren]");
//        boolean allowsChildren = node.getAllowsChildren();
//        assertEquals(true, allowsChildren);
//    }
//
//    public void testGetChildCount()
//    {
//        log.debug("[testGetChildCount]");
//        int childCount = node.getChildCount();
//        log.debug("children="+childCount);
//        // since this is variable, we don't test for explicit number
//    }
//
//    public void testToString()
//    {
//        log.debug("[testToString]");
//        String desc = node.toString();
//        assertNotNull("toString returned a null value", desc);
//    }
//
//
//    private void configureLogging()
//    {
//        Properties props = new Properties();
//        props.put("log4j.rootCategory","DEBUG, Console");
//        props.put("log4j.appender.Console","org.apache.log4j.ConsoleAppender");
//        props.put("log4j.appender.Console.layout","org.apache.log4j.PatternLayout");
//        props.put("log4j.appender.Console.layout.ConversionPattern","%d %-5p %-25c{4} - %m%n");
//        PropertyConfigurator.configure(props);
//    }
//
//}
//