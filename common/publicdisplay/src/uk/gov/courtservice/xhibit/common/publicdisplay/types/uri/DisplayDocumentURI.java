package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.exceptions.NoSuchDocumentTypeException;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions.InvalidURIFormatException;

/**
 * <p/> Title: An immutable class that provides URI functionality for Public
 * Display.
 * </p>
 * <p/> <p/> The DisplayDocumentURI can then be constructed from it's components
 * as the usual Java primitives ie. int courtId, String documentType, int[]
 * courtRoomIds. The DisplayDocumentURI also supports conversion to and from a
 * String. The format of a display document URI is:
 * 
 * <pre>
 *                  publicdisplay://document:&lt;locale&gt;/&lt;courtId&gt;/&lt;objectName&gt;:(&lt;courtRoomId&gt;,)+
 * </pre>
 * 
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.21 $
 */
public final class DisplayDocumentURI extends AbstractURI {

    /**
     * The serial version number
     */
    private static final long serialVersionUID = 2L;

    public static final int UNASSIGNED = Integer.MAX_VALUE;

    public static final String UNASSIGNED_STRING = "u";

    private static final Integer UNASSIGNED_INTEGER = new Integer(UNASSIGNED);

    private static final String DOCUMENT_URI_TYPE = "document";

    private static final String DOCUMENT_URI_PREFIX = URI_PREFIX + DOCUMENT_URI_TYPE + ':';

    private static final char[] DOCUMENT_URI_PREFIX_CHARS = DOCUMENT_URI_PREFIX.toCharArray();

    private final Locale locale;

    private final int courtId;

    private final DisplayDocumentType documentType;

    private final int[] courtRoomIds;

    private static final Logger log = CSServices.getLogger(DisplayDocumentURI.class);

    /**
     * Build a DisplayDocumentURI from it's component parts.
     * 
     * @param courtId
     * @param name
     * @param courtRoomIds
     *            a sorted array of court room ids.
     * @pre courtRoomIds.length <= 60
     * @post courtId >= 0
     * @post courtRoomIds != null
     * @post courtRoomIds.length > 0
     * @post
     */
    public DisplayDocumentURI(Locale locale, int courtId, DisplayDocumentType name, int[] courtRoomIds)
            throws InvalidURIFormatException {
        this(locale, courtId, name, courtRoomIds, copySort(courtRoomIds));
    }

    // Dummy constructor to allow sortedCourtRoomIds to be used multiple
    // times
    private DisplayDocumentURI(Locale locale, int courtId, DisplayDocumentType name, int[] courtRoomIds,
            int[] sortedCourtRoomIds) {
        super(createUri(locale, courtId, name, sortedCourtRoomIds));
        this.locale = locale;
        this.courtId = courtId;
        this.documentType = name;
        this.courtRoomIds = sortedCourtRoomIds;
    }

    private static String createUri(Locale locale, int courtId, DisplayDocumentType documentType,
            int[] sortedCourtRoomIds) {
        // Validate the uri
        if (locale == null) {
            throw new InvalidURIFormatException("Invalid locale supplied.");
        }
        if (courtId < 0) {
            throw new InvalidURIFormatException("Invalid court id supplied.");
        }
        if (documentType == null) {
            throw new InvalidURIFormatException("Invalid type supplied.");
        }
        if (sortedCourtRoomIds == null || sortedCourtRoomIds.length == 0) {
            throw new InvalidURIFormatException("Invalid court room ids supplied.");
        }
        // Create the uri
        StringBuffer buffer = new StringBuffer();
        buffer.append(DOCUMENT_URI_PREFIX_CHARS);
        buffer.append(courtId);
        buffer.append('/');
        buffer.append(documentType);
        buffer.append(':');
        if (0 < sortedCourtRoomIds.length) {
            appendCourtRoomId(buffer, sortedCourtRoomIds[0]);
            for (int i = 1; i < sortedCourtRoomIds.length; i++) {
                buffer.append(',');
                appendCourtRoomId(buffer, sortedCourtRoomIds[i]);
            }
        }
        return buffer.toString();
    }

    private static void appendCourtRoomId(StringBuffer buffer, int courtRoomId) {
        if (courtRoomId == UNASSIGNED) {
            buffer.append(UNASSIGNED_STRING);
        } else {
            buffer.append(courtRoomId);
        }
    }

    /**
     * Construct a URI from the String passed.
     * 
     * @param uri
     * @pre uri != null
     * @post courtId >= 0
     * @post courtRoomIds != null
     * @post courtRoomIds.length > 0
     * @post forall int crt in courtRoomIds | crt >= 0
     */
    public DisplayDocumentURI(final String uri) throws InvalidURIFormatException {
        super(uri);

        char[] chars = uri.toCharArray();

        // Prefix
        if (!equals(DOCUMENT_URI_PREFIX_CHARS, 0, chars, 0, DOCUMENT_URI_PREFIX_CHARS.length)) {
            throw new InvalidURIFormatException(uri, "Invalid prefix.");
        }

        // Court Id
        int start = DOCUMENT_URI_PREFIX_CHARS.length;
        int end = indexOf(chars, start, '/');
        int length = end - start;
        if (end == -1 || length == 0) {
            throw new InvalidURIFormatException(uri, "Invalid court id.");
        }
        try {
            this.courtId = Integer.parseInt(new String(chars, start, length));
        } catch (NumberFormatException nfe) {
            throw new InvalidURIFormatException(uri, "Invalid court id.");
        }

        // Document Type
        start = end + 1;
        end = indexOf(chars, start, ':');
        length = end - start;
        if (end == -1 || length == 0 || !isValidDocumentTypeCharacters(chars, start, length)) {
            throw new InvalidURIFormatException(uri, "Invalid type.");
        }
        log.debug("Display Document: " + new String(chars, start, length));
        try {
            this.documentType = DisplayDocumentType.getDisplayDocumentType(new String(chars, start, length));
        } catch (NoSuchDocumentTypeException nsdte) {
            throw new InvalidURIFormatException(uri, "Invalid type.");
        }

        // Locale
        this.locale = parseLocale(chars, start, end);

        // Court Ids
        List courtIdList = new ArrayList();

        // Court Ids - Initialise Loop Variables
        start = end + 1;
        end = indexOf(chars, start, ',');
        if (end == -1) {
            end = chars.length;
        }
        length = end - start;
        while (length > 0) {
            // Court Ids - Process Entry
            Integer courtId = parseCourtRoomId(chars, start, length);
            if (courtId == null) {
                throw new InvalidURIFormatException(uri, "Invalid courtId.");
            }
            courtIdList.add(courtId);
            // Court Ids - Update Loop Variables
            start = end + 1;
            end = indexOf(chars, start, ',');
            if (end == -1) {
                end = chars.length;
            }
            length = end - start;
        }
        // Court Ids - Convert into array.
        this.courtRoomIds = new int[courtIdList.size()];
        for (int i = 0; i < this.courtRoomIds.length; i++) {
            this.courtRoomIds[i] = ((Integer) courtIdList.get(i)).intValue();
        }
        Arrays.sort(courtRoomIds);
    }

    private static Locale parseLocale(char[] chars, int start, int end) {
        start = end + 1;
        end = indexOf(chars, start, '_');

        // check if language exists
        if (end == -1) {
            return Locale.getDefault();
        }

        String language = new String(chars, end + 1, 2);

        start = end + 1;
        end = indexOf(chars, start, '_');

        // check country exists
        if (end == -1) {
            // If language exists, country should too.
            log.error("Language set but no Country set.  Using default locale: " + Locale.getDefault());
            return Locale.getDefault();
        }
        String country = new String(chars, end + 1, 2);

        return new Locale(language, country);
    }

    private static Integer parseCourtRoomId(char[] chars, int start, int length) {
        try {
            String courtId = new String(chars, start, length);
            if (courtId.equals(UNASSIGNED_STRING)) {
                return UNASSIGNED_INTEGER;
            }
            return new Integer(courtId);
        } catch (Exception nfe) {
            return null;
        }
    }

    /**
     * Returns the locale for this URI
     * 
     * @return the Locale for this uri
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns the type of uri this is as a String.
     * 
     * @return the type of URI this is.
     * @post return != null
     */
    public String getType() {
        return DOCUMENT_URI_TYPE;
    }

    /**
     * Returns the courtId.
     * 
     * @return Returns the courtId.
     */
    public int getCourtId() {
        return courtId;
    }

    /**
     * This returns a copy of the courtRoomIds. We return a copy to make sure
     * the object remains immutable.
     * 
     * @return returns a copy of the courtRoomIds.
     */
    public int[] getCourtRoomIds() {
        return (int[]) courtRoomIds.clone();
    }

    /**
     * This returns a copy of the courtRoomIds. We return a copy to make sure
     * the object remains immutable.
     * 
     * @return returns a copy of the courtRoomIds.
     */
    public int[] getCourtRoomIdsWithoutUnassigned() {
        if (isUnassignedRequired()) {
            int[] returnArray = new int[courtRoomIds.length - 1];
            System.arraycopy(courtRoomIds, 0, returnArray, 0, courtRoomIds.length - 1);
            return (int[]) returnArray;
        } else {
            return (int[]) courtRoomIds.clone();
        }
    }

    /**
     * Returns the DocumentTupe from the URI.
     * 
     * @return the documentType.
     */
    public DisplayDocumentType getDocumentType() {
        return documentType;
    }

    /**
     * Returns the DocumentTupe from the URI.
     * 
     * @return the documentType.
     */
    public String getSimpleDocumentType() {
        return documentType.getShortName();
    }

    /**
     * Returns the DocumentTupe from the URI converted to lowercase.
     * 
     * @return the documentType.
     */
    public String getDocumentTypeAsLowerCaseString() {
        return documentType.toLowerCaseString();
    }

    /**
     * Returns true if the URI has unassigned cases
     * 
     * @return the documentType.
     */
    public boolean isUnassignedRequired() {
        return courtRoomIds.length > 0 && courtRoomIds[courtRoomIds.length - 1] == UNASSIGNED;
    }

    //
    // Utils
    //	

    private static int[] copySort(int[] values) {
        if (values == null) {
            return null;
        }
        int[] buffer = new int[values.length];
        System.arraycopy(values, 0, buffer, 0, values.length);
        Arrays.sort(values);
        return values;
    }

    private static boolean isValidLocaleCharacters(char[] actual, int offset, int length) {
        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(actual[offset + i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidDocumentTypeCharacters(char[] actual, int offset, int length) {
        for (int i = 0; i < length; i++) {
            if (!isValidDocumentTypeCharacter(actual[offset + i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidDocumentTypeCharacter(char actual) {
        return Character.isLetter(actual) || Character.isDigit(actual) || actual == ' ' || actual == '_';
    }

}
