package uk.gov.courtservice.xhibit.business.vos.translation;

import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import uk.gov.courtservice.xhibit.business.vos.translation.impl.LookupTranslationBundle;

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
 * @version $Id: TestTranslationBundle.java,v 1.3 2006/06/05 12:28:30 bzjrnl Exp $
 */
public class TestTranslationBundle extends TestCase {

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
        return new TestSuite(TestTranslationBundle.class);
    }

    private TranslationBundle translationBundle;

    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
        LookupTranslationBundle lookuptranslationBundle = new LookupTranslationBundle(new Locale("xx", "YY"));
        lookuptranslationBundle.addTranslation("key 01", "translation01", null, true);
        lookuptranslationBundle.addTranslation("key 01", "translation02", "context01", true);
        lookuptranslationBundle.addTranslation("key 01", "translation03", "context02", true);
        lookuptranslationBundle.addTranslation("key 02", "translation04", null, false);
        lookuptranslationBundle.addTranslation("key 02", "translation05", "context01", false);
        lookuptranslationBundle.addTranslation("key 02", "translation06", "context02", false);
        lookuptranslationBundle.addTranslation("key 03", "translation07", null, true);
        lookuptranslationBundle.addTranslation("key 03", "translation08", "context01", true);
        lookuptranslationBundle.addTranslation("key 04", "translation09", null, true);
        lookuptranslationBundle.addTranslation("key 05", "translation10", "context01", true);
        translationBundle = lookuptranslationBundle;
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
        translationBundle = null;
    }

    //
    // The Tests!
    //	

    public void testGetLocale() {
        assertEquals(translationBundle.getLocale(), new Locale("xx", "YY"));
    }

    public void testGetTranslationExactMatch() {
        assertNull(translationBundle.getTranslation("key 00"));

        assertEquals("translation01", translationBundle.getTranslation("key 01"));
        assertEquals("translation02", translationBundle.getTranslation("key 01", "context01"));
        assertEquals("translation03", translationBundle.getTranslation("key 01", "context02"));
        assertNull(translationBundle.getTranslation("key 01", "context03"));

        assertEquals("translation04", translationBundle.getTranslation("key 02"));
        assertEquals("translation05", translationBundle.getTranslation("key 02", "context01"));
        assertEquals("translation06", translationBundle.getTranslation("key 02", "context02"));
        assertNull(translationBundle.getTranslation("key 02", "context03"));

        assertEquals("translation07", translationBundle.getTranslation("key 03"));
        assertEquals("translation08", translationBundle.getTranslation("key 03", "context01"));
        assertNull(translationBundle.getTranslation("key 03", "context02"));

        assertEquals("translation09", translationBundle.getTranslation("key 04"));
        assertNull(translationBundle.getTranslation("key 04", "context01"));

        assertNull(translationBundle.getTranslation("key 05"));
        assertEquals("translation10", translationBundle.getTranslation("key 05", "context01"));
    }

    public void testGetTranslationRuleMatch() {

        assertNull("translation01", translationBundle.getTranslation("key, 01."));
        assertNull("translation02", translationBundle.getTranslation("key, 01.", "context01"));
        assertNull("translation03", translationBundle.getTranslation("key, 01.", "context02"));
        assertNull(translationBundle.getTranslation("key, 01.", "context03"));

        assertEquals("translation04", translationBundle.getTranslation("key, 02."));
        assertEquals("translation05", translationBundle.getTranslation("key, 02.", "context01"));
        assertEquals("translation06", translationBundle.getTranslation("key, 02.", "context02"));
        assertNull(translationBundle.getTranslation("key, 02.", "context03"));

        assertEquals("translation04", translationBundle.getTranslation("key 02"));

        assertEquals("translation04", translationBundle.getTranslation("key 02."));
        assertEquals("translation04", translationBundle.getTranslation("key. 02"));
        assertEquals("translation04", translationBundle.getTranslation(".key 02"));
        assertEquals("translation04", translationBundle.getTranslation("key. 02."));
        assertEquals("translation04", translationBundle.getTranslation(".key. 02"));
        assertEquals("translation04", translationBundle.getTranslation(".key. 02."));

        assertEquals("translation04", translationBundle.getTranslation("key 02 "));
        assertEquals("translation04", translationBundle.getTranslation("key  02"));
        assertEquals("translation04", translationBundle.getTranslation(" key 02"));
        assertEquals("translation04", translationBundle.getTranslation("key  02 "));
        assertEquals("translation04", translationBundle.getTranslation(" key  02"));
        assertEquals("translation04", translationBundle.getTranslation(" key  02 "));

        assertEquals("translation04", translationBundle.getTranslation("Key 02"));

        assertEquals("translation04", translationBundle.getTranslation(" . . . key ., ., ., 02 . . . "));
    }
}
