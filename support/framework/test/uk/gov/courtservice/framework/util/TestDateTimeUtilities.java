package uk.gov.courtservice.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import uk.gov.courtservice.framework.util.DateTimeUtilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;

/**
 * <p>
 * Title: JUnit Test for DateTimeUtilities concentrating on date formatting methods
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: TestDateTimeUtilities.java,v 1.2 2006/09/12 10:59:23 qz4rwx Exp $ Exp $
 */

public class TestDateTimeUtilities extends TestCase {

    DateTimeUtilities dateTimeUtilities = null;
    Calendar calendar = null;

    // Setup Log4j configuration
    static {
        BasicConfigurator.configure();
    }
    
    public TestDateTimeUtilities(String name)
    {
       super(name);
    }
    
    /**
     * Execution entry point. Allows the test to be run in stand alone mode.
     * 
     * @param args String array of command line arguments
     */
    public static void main(String args[]) {
        TestRunner.run(suite());
    }

    /**
     * Create a Test useing reflection to determine tests
     */
    public static Test suite() {
        return new TestSuite(TestDateTimeUtilities.class);
    }
    
    protected void setUp() {
        try {
            dateTimeUtilities = new DateTimeUtilities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void tearDown() {
        dateTimeUtilities = null;
    }

    public final void testProcessOracleDateParameter() {
        try
        {
            assertEquals(DateTimeUtilities.processOracleDateParameter("2001-07-04T12:08:56").getTime().toString(), "Wed Jul 04 12:08:56 BST 2001");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        try
        {
            assertEquals(DateTimeUtilities.processOracleDateParameter("2001-07-04T23:08:56").getTime().toString(), "Wed Jul 04 23:08:56 BST 2001");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }

    public final void testProcessOracleDateParameterForDate() {
        try
        {
            assertEquals(DateTimeUtilities.processOracleDateParameterForDate("2001-07-04T12:08:56").toString(), "Wed Jul 04 12:08:56 BST 2001");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        try
        {
            assertEquals(DateTimeUtilities.processOracleDateParameterForDate("2001-07-04T23:08:56").toString(), "Wed Jul 04 23:08:56 BST 2001");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
    
    public final void testProcessDateParameter() {
        try
        {
            assertEquals(DateTimeUtilities.processDateParameter("2001-07-04T12:08:56",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).getTime().toString(), "Wed Jul 04 12:08:56 BST 2001");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        try
        {
            assertEquals(DateTimeUtilities.processDateParameter("2001-07-04T23:08:56",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).getTime().toString(), "Wed Jul 04 23:08:56 BST 2001");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    } 

    public final void testConvertOracleDate() {
        try
        {
            Date date = DateTimeUtilities.processOracleDateParameterForDate("2001-07-04T12:08:56");
            
            assertEquals(DateTimeUtilities.convertOracleDate(date), "2001-07-04T12:08:56");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            Date date = DateTimeUtilities.processOracleDateParameterForDate("2001-07-04T23:08:56");
            
            assertEquals(DateTimeUtilities.convertOracleDate(date), "2001-07-04T23:08:56");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
    
    public final void testConvertDate() {
        try
        {
            Date date = DateTimeUtilities.processDateParameter("2001-07-04T12:08:56",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).getTime();
            
            assertEquals(DateTimeUtilities.convertDate(date,new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")), "2001-07-04T12:08:56");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            Date date = DateTimeUtilities.processDateParameter("2001-07-04T23:08:56",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).getTime();
            
            assertEquals(DateTimeUtilities.convertDate(date,new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")), "2001-07-04T23:08:56");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }       
    }
}
