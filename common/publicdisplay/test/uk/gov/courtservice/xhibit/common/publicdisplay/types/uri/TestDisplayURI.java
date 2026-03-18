package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;

import java.util.Random;

import junit.framework.TestCase;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.9 $
 */
public class TestDisplayURI extends TestCase {
    protected static final String DISPLAY_URL = "pd://display/Isleworth/Site1/Entrance/Display1";

    protected String STORE_LOCATION = "d:\\projects\\PublicDisplayRewrite\\thinclient\\docroot\\store";

    protected String STORE_URL = "http://localhost:7001/docroot/store";

    /**
     * Creates a new TestDisplayURI object.
     * 
     * @param s
     *            TODO:
     */
    public TestDisplayURI(String s) throws Exception {
        super(s);
        System.setProperty("publicdisplay.web.store_base", STORE_LOCATION);
        System.setProperty("publicdisplay.web.base_url", STORE_URL);
    }

    /**
     * TODO:
     * 
     * @throws java.lang.Exception
     *             TODO:
     */
    public void testBadURIs() throws Exception {
        try {
            DisplayURI bad = new DisplayURI("pd://display/Isleworth/Site1/../Entrance/Display1");
            fail("An invalid URL was accepted: " + bad);
        } catch (Exception e) {
            // e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            DisplayURI bad = new DisplayURI("pd://display/Isleworth/Entrance/Display1");
            fail("An invalid URL was accepted: " + bad);
        } catch (Exception e) {
            // e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            DisplayURI bad = new DisplayURI("pd://display/Isleworth/Site1/Entrance/Display 1");
            fail("An invalid URL was accepted: " + bad);
        } catch (Exception e) {
            // e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            DisplayURI bad = new DisplayURI("publicrubbish://display/Isleworth/Entrance/Display1");
            fail("An invalid URL was accepted: " + bad);
        } catch (Exception e) {
            // e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }
    }

    /**
     * TODO:
     * 
     * @throws java.lang.Exception
     *             TODO:
     */
    public void testDisplayURI() throws Exception {
        DisplayURI uri1 = new DisplayURI(DISPLAY_URL);
        DisplayURI uri2 = new DisplayURI("Isleworth", "Site1", "Entrance", "Display1");
        assertEquals(uri1.toString(), uri2.toString());
        assertEquals(uri1, uri2);
    }

    //
    // Performance Test
    //
    private static int count; // = 100000;

    private static char[] uriCharacters = ("0123456789_" + "ABCDEFBHIJKLMNOPQRSTUVWXYZ_" + "abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz").toCharArray();

    private static Random random = new Random();

    public void testPerformance() {
        String[] uris = generateTestURIs(count);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < uris.length; i++) {
            new DisplayURI(uris[i]);
        }
        long stopTime = System.currentTimeMillis();
        long time = stopTime - startTime;

        System.out.println("count: " + count + " time: " + time + " avg: " + ((double) time / (double) count));
    }

    private static String[] generateTestURIs(int count) {
        String[] uris = new String[count];
        for (int i = 0; i < count; i++) {
            uris[i] = generateTestURI();
        }
        return uris;
    }

    private static String generateTestURI() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("pd://display/");

        // Court
        for (int i = 0, c = random.nextInt(5) + 5; i < c; i++) {
            buffer.append(uriCharacters[random.nextInt(uriCharacters.length)]);
        }
        buffer.append('/');

        // Court Site Code
        for (int i = 0, c = random.nextInt(1) + 1; i < c; i++) {
            buffer.append(uriCharacters[random.nextInt(uriCharacters.length)]);
        }
        buffer.append('/');

        // Location
        for (int i = 0, c = random.nextInt(8) + 4; i < c; i++) {
            buffer.append(uriCharacters[random.nextInt(uriCharacters.length)]);
        }
        buffer.append('/');

        // Display
        for (int i = 0, c = random.nextInt(8) + 4; i < c; i++) {
            buffer.append(uriCharacters[random.nextInt(uriCharacters.length)]);
        }

        return buffer.toString();
    }
}
