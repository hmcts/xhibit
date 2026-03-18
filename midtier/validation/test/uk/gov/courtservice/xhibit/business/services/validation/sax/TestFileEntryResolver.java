package uk.gov.courtservice.xhibit.business.services.validation.sax;

import java.io.File;

import uk.gov.courtservice.xhibit.business.services.validation.sax.FileEntityResolver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * File Entity Resolver Test
 * 
 * @author William Fardell
 * @version $Id: TestFileEntryResolver.java,v 1.2 2010/04/19 08:09:00 dunnepi Exp $
 */
public class TestFileEntryResolver extends TestCase {

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
        return new TestSuite(TestFileEntryResolver.class);
    }

    
    /**
     * The resolver to test;
     */
    private FileEntityResolver resolver;
    
    /**
     * The base dir as system dependant
     */
    private File expectedBaseDir;
    
    /**
     * The base url as system dependant
     */
    private String expectedBaseUrl;
        
    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() throws Exception {
        expectedBaseDir = new File("/foo/bar").getAbsoluteFile();
        expectedBaseUrl = expectedBaseDir.toURL().toExternalForm() + "/";
        resolver = new FileEntityResolver(expectedBaseDir);
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
        resolver = null;
        expectedBaseUrl = null;
        expectedBaseDir = null;
    }

    //
    // The Tests!
    //	
    
    public void testConstructorNull() {
        try {
            new FileEntityResolver((File) null);
            fail("IllegalArgumentException not thrown.");
        } catch (final IllegalArgumentException iae) {
            // Success
        }
        try {
            new FileEntityResolver((String) null);
            fail("IllegalArgumentException not thrown.");
        } catch (final IllegalArgumentException iae) {
            // Success
        }
    }

    public void testResolveEntityUrl() {
       assertEquals(expectedBaseUrl + "TrialRecordSheet-v5-2.xsd", resolver
                .resolveEntityUrl("TrialRecordSheet-v5-2.xsd"));

        assertEquals(expectedBaseUrl + "TrialRecordSheet-v5-2.xsd", resolver
                .resolveEntityUrl("http://www.courtservice.gov.uk/schemas/courtservice/TrialRecordSheet-v5-2.xsd"));
        
        assertEquals(expectedBaseUrl + "TrialRecordSheet-v5-2.xsd", resolver
                .resolveEntityUrl("file:///C:\\schemas\\courtservice\\TrialRecordSheet-v5-2.xsd"));
        
        assertEquals(expectedBaseUrl + "TrialRecordSheet-v5-2.xsd", resolver
                .resolveEntityUrl("http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v5-2.xsd"));    
    }
    
    public void testResolveEntityUrlNull() {
        try {
            resolver.resolveEntityUrl(null);
            fail("IllegalArgumentException not thrown.");
        } catch (final IllegalArgumentException iae) {
            // Success
        }
    }
    
    public void testResolveEntityFile() {
        assertEquals(new File(expectedBaseDir, "TrialRecordSheet-v5-2.xsd"), resolver
                .resolveEntityFile("TrialRecordSheet-v5-2.xsd"));

        assertEquals(new File(expectedBaseDir, "TrialRecordSheet-v5-2.xsd"), resolver
                .resolveEntityFile("http://www.courtservice.gov.uk/schemas/courtservice/TrialRecordSheet-v5-2.xsd"));
        
        assertEquals(new File(expectedBaseDir, "TrialRecordSheet-v5-2.xsd"), resolver
                .resolveEntityFile("file:///C:\\schemas\\courtservice\\TrialRecordSheet-v5-2.xsd"));
        
        assertEquals(new File(expectedBaseDir, "TrialRecordSheet-v5-2.xsd"), resolver
                .resolveEntityFile("http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v5-2.xsd"));       
    }

    public void testResolveEntityFileNull() {
        try {
            resolver.resolveEntityFile(null);
            fail("IllegalArgumentException not thrown.");
        } catch (final IllegalArgumentException iae) {
            // Success
        }
    }

}
