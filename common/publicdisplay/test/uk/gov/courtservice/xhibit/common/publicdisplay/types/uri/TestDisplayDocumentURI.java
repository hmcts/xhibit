package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;

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
 * @version $Revision: 1.8 $
 */
public class TestDisplayDocumentURI extends TestCase {
    /**
     * Creates a new TestDisplayDocumentURI object.
     * 
     * @param s
     *            TODO:
     */
    public TestDisplayDocumentURI(String s) throws Exception {
        super(s);
    }

    /**
     * TODO:
     * 
     * @throws java.lang.Exception
     *             TODO:
     */
    public void xtestDisplayDocumentURI_negative() throws Exception {
        DisplayDocumentURI uri;

        try {
            uri = new DisplayDocumentURI("pd://wibble:ww/1/CourtDetail:1,2,3,100");
            fail("The type was incorrect and the uri should not have been accepted.");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            uri = new DisplayDocumentURI(Locale.ENGLISH, 1, DisplayDocumentType.getDisplayDocumentType("fred/.."),
                    new int[] { 1, 2, 3 });
            fail("The doctype should not have been accepted.");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            uri = new DisplayDocumentURI("pd://document:tt/dilbert/fred:1,2,3");
            fail("The courtId was not an integer and should not have been accepted.");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            uri = new DisplayDocumentURI("pd://document/dilbert/fred:1,2,3x");
            fail("The last courtRoomId was not an integer and should not have been accepted.");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            uri = new DisplayDocumentURI("pd://document:en//fred:1,2,3x");
            fail("The last court name was missing so the uri should not have been accepted.");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }

        try {
            uri = new DisplayDocumentURI("pd://document:en//fred:");
            fail("The courtRoomIds were missing and so the URI should not have been accepted");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
        }
    }

    /**
     * TODO:
     * 
     * @throws java.lang.Exception
     *             TODO:
     */
    public void xtestDisplayDocumentURI_positive() throws Exception {
        DisplayDocumentURI uri1 = new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3,100");
        Locale locale = new Locale("en", "GB");
        DisplayDocumentURI uri2 = new DisplayDocumentURI(locale, 1, DisplayDocumentType
                .getDisplayDocumentType("CourtDetail"), new int[] { 1, 2, 3 });
        DisplayDocumentURI uri3 = new DisplayDocumentURI("pd://document:en/1/CourtDetail:1,2,3");
        assertEquals(uri2, uri3);
    }

    //
    // Performance Test
    //  
    private static int count; // = 100000;

    private static char[] uriLetters = ("ABCDEFBHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz").toCharArray();

    private static final char[][] documentTypes = new char[][] {
            DisplayDocumentType.COURT_DETAIL.toString().toCharArray(),
            DisplayDocumentType.COURT_LIST.toString().toCharArray(),
            DisplayDocumentType.DAILY_LIST.toString().toCharArray(),
            DisplayDocumentType.ALL_COURT_STATUS.toString().toCharArray(),
            DisplayDocumentType.SUMMARY_BY_NAME.toString().toCharArray(),
            DisplayDocumentType.JURY_CURRENT_STATUS.toString().toCharArray(),
            DisplayDocumentType.ALL_CASE_STATUS.toString().toCharArray(), };

    private static Random random = new Random();

    public void testPerformance() {
        String[] uris = generateTestURIs(count);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < uris.length; i++) {
            new DisplayDocumentURI(uris[i]);
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
        buffer.append("pd://document:");

        // Locale
        for (int i = 0, c = 2; i < c; i++) {
            buffer.append(uriLetters[random.nextInt(uriLetters.length)]);
        }
        buffer.append('/');

        // Court Id
        buffer.append(random.nextInt(100));
        buffer.append('/');

        // Type
        buffer.append(documentTypes[random.nextInt(documentTypes.length)]);
        buffer.append(':');

        // Court Ids
        int[] courtIds = generateTestURICourtIds();
        if (0 < courtIds.length) {
            buffer.append(courtIds[0]);
            for (int i = 1; i < courtIds.length; i++) {
                buffer.append(',');
                buffer.append(courtIds[i]);
            }
            if (random.nextInt(5) == 0) {
                buffer.append(',');
                buffer.append(DisplayDocumentURI.UNASSIGNED_STRING);
            }
        }
        return buffer.toString();
    }

    private static int[] generateTestURICourtIds() {
        int[] courtIds = new int[random.nextInt(8) + 1];
        for (int i = 0; i < courtIds.length; i++) {
            courtIds[i] = random.nextInt(2000);
        }
        Arrays.sort(courtIds);
        return courtIds;
    }
}
