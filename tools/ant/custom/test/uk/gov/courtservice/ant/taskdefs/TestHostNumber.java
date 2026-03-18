package uk.gov.courtservice.ant.taskdefs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.tools.ant.Project;

/**
 * <p>
 * Title: Translation Bundle Test
 * </p>
 * <p>
 * Description: A translation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: TestHostNumber.java,v 1.3 2006/06/05 12:32:41 bzjrnl Exp $
 */
public class TestHostNumber extends TestCase {

    //
    // The Framework!
    //

    /**
     * Execution entry point. Allows the test to be run in stand alone mode.
     * 
     * @param args
     *            String array of command line arguments
     */
    public static void main(String args[]) {
        TestRunner.run(suite());
    }

    /**
     * Create a Test useing reflection to determine tests
     */
    public static Test suite() {
        return new TestSuite(TestHostNumber.class);
    }

    private HostNumber task;

    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
        task = new HostNumber() {
            protected byte[] getAddress(String host) {
                return TestHostNumber.getAddress(host);
            }
        };
        task.setProject(new Project());
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
        task = null;
    }

    //
    // The Tests!
    //

    public void testHostA() {
        task.setProperty("test.property");
        task.setHost("hostname-a");
        task.execute();
        assertEquals("2192638754", task.getProject().getProperty("test.property"));
    }

    public void testHostAOffset() {
        task.setProperty("test.property");
        task.setHost("hostname-a");
        task.setOffset(10);
        task.execute();
        assertEquals("2192638764", task.getProject().getProperty("test.property"));
    }

    public void testHostAMax() {
        task.setProperty("test.property");
        task.setHost("hostname-a");
        task.setMax(200);
        task.execute();
        assertEquals("154", task.getProject().getProperty("test.property"));
    }

    public void testHostAMaxMin() {
        task.setProperty("test.property");
        task.setHost("hostname-a");
        task.setMin(-100);
        task.setMax(100);
        task.execute();
        assertEquals("54", task.getProject().getProperty("test.property"));
    }

    public void testHostB() {
        task.setProperty("test.property");
        task.setHost("hostname-b");
        task.execute();
        assertEquals("2192638240", task.getProject().getProperty("test.property"));
    }

    public void testHostBOffset() {
        task.setProperty("test.property");
        task.setHost("hostname-b");
        task.setOffset(10);
        task.execute();
        assertEquals("2192638250", task.getProject().getProperty("test.property"));
    }

    public void testHostBMax() {
        task.setProperty("test.property");
        task.setHost("hostname-b");
        task.setMax(200);
        task.execute();
        assertEquals("40", task.getProject().getProperty("test.property"));
    }

    public void testHostBMaxMin() {
        task.setProperty("test.property");
        task.setHost("hostname-b");
        task.setMin(-100);
        task.setMax(100);
        task.execute();
        assertEquals("-60", task.getProject().getProperty("test.property"));
    }

    //
    // Utilities
    //

    private static byte[] getAddress(String host) {
        if ("hostname-b".equals(host)) {
            return getAddress(130, 177, 1, 32);
        }
        if ("localhost".equals(host)) {
            return getAddress(127, 0, 0, 1);
        }
        return getAddress(130, 177, 3, 34);
    }

    private static byte[] getAddress(int i1, int i2, int i3, int i4) {
        return new byte[] { (byte) i1, (byte) i2, (byte) i3, (byte) i4 };
    }

}
