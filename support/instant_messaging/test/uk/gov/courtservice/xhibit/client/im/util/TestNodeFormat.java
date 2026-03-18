package uk.gov.courtservice.xhibit.client.im.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author  Jon Powell (Electronic Data Systems)
 *
 * Tests for NodeFormat class
 */
public class TestNodeFormat extends TestCase
{

    // logging
    private static Logger log = Logger.getLogger(TestNodeFormat.class.getName());

    // constants
    private static final String NULL_NODE = null;
    private static final String EMPTY_NODE = "";
    private static final String EMPTY_STRING = "";
    private static final String SINGLE_LEVEL_NODE = "jms";
    private static final String SINGLE_LEVEL_NODE_RESULT = "Jms";
    private static final String MULTI_LEVEL_NODE = "jms.im.snaresbrook_crown_court.court_site_a";
    private static final String MULTI_LEVEL_NODE_RESULT = "Court Site A";


	/**
	 * Create new test class
	 * @param testName  will be passed in by JUnit runner
	 */
    public TestNodeFormat(String testName)
	{
		super(testName);
        configureTestLogging();
	}

	/**
	 * Have JUnit pick up all tests in this class
	 */
	public static Test suite()
	{
        return new TestSuite(TestNodeFormat.class);
	}


	/**
	 * Initialise variables common to each test. Re-run before each test.
	 */
	protected void setUp()
	{
	}


	/**
	 * Cleanup variables used by tests. Re-run after each test.
	 */
	protected void tearDown()
	{
	}


	//
    // tests
    //

    public void testDisplayNullNodeName()
    {
        log.debug("[testDisplayNullNodeName]");
        String formattedNode = NodeFormat.displayNodeName(NULL_NODE);
        assertEquals("null node name not formatted correctly", EMPTY_STRING, formattedNode);  
    }

    public void testDisplayEmptyNodeName()
    {
        log.debug("[testDisplayEmptyNodeName]");
        String formattedNode = NodeFormat.displayNodeName(EMPTY_NODE);
        assertEquals("zero-length node name not formatted correctly", EMPTY_STRING, formattedNode);      
    }
    
    public void testDisplayNodeName()
    {
        log.debug("[testDisplayNodeName]");
        String formattedNode = NodeFormat.displayNodeName(SINGLE_LEVEL_NODE);
        assertEquals("node name with no underscores not formatted correctly", SINGLE_LEVEL_NODE_RESULT, formattedNode);        
        formattedNode = NodeFormat.displayNodeName(MULTI_LEVEL_NODE);
        assertEquals("node name with underscores not formatted correctly", MULTI_LEVEL_NODE_RESULT, formattedNode);
    }


    private void configureTestLogging()
    {
        Properties props = new Properties();
        props.put("log4j.rootCategory","DEBUG, Console");
        props.put("log4j.appender.Console","org.apache.log4j.ConsoleAppender");
        props.put("log4j.appender.Console.layout","org.apache.log4j.PatternLayout");
        props.put("log4j.appender.Console.layout.ConversionPattern","%d %-5p %-25c{4} - %m%n");
        PropertyConfigurator.configure(props);
    }

}
