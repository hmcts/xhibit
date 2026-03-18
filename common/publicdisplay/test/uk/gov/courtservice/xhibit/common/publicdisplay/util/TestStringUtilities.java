package uk.gov.courtservice.xhibit.common.publicdisplay.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * <p>
 * Title: String Utilities Test
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
 * @version $Id: TestStringUtilities.java,v 1.3 2006/06/05 12:28:27 bzjrnl Exp $
 */
public class TestStringUtilities extends TestCase {

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
        return new TestSuite(TestStringUtilities.class);
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {

    }

    //
    // The Tests!
    //
    public void testToSentenceCase() {
        assertEquals("Kingston  -Upon-Thames", StringUtilities.toSentenceCase("Kingston  -upon-thames"));
        assertEquals("Kingston. -Upon-Thames", StringUtilities.toSentenceCase("Kingston. -upon-thames"));
        assertEquals("Kingston -Upon-Thames", StringUtilities.toSentenceCase("Kingston -upon-thames"));
        assertEquals("Kingston-Upon-Thames", StringUtilities.toSentenceCase("Kingston-upon-thames"));
        assertEquals("-Kingston-Upon-Thames", StringUtilities.toSentenceCase("-Kingston-upon-thames"));
        assertEquals("Kingston-Upon-Thames-", StringUtilities.toSentenceCase("Kingston-upon-thames-"));
        assertEquals("Swansea (Guildhall)", StringUtilities.toSentenceCase("Swansea (Guildhall)"));
        assertEquals("Newcastle_Upon_Tyne", StringUtilities.toSentenceCase("newcastle_upon_tyne"));
        assertEquals("Cvs/Tooting", StringUtilities.toSentenceCase("CVS/tooting"));
        assertEquals("Tomcat,Fir", StringUtilities.toSentenceCase("tomcat,fir"));
        assertEquals("", StringUtilities.toSentenceCase(""));
        assertEquals(null, StringUtilities.toSentenceCase(null));
    }
}
