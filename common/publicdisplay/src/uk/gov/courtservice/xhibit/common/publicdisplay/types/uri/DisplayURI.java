package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;

import java.util.Locale;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions.InvalidURIFormatException;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description: The following is the format of the DisplayURI:
 * 
 * <pre>
 *                       publicdisplay://display/&lt;courthouseName&gt;/&lt;courtSite&gt;/&lt;location&gt;/&lt;display&gt;
 * </pre>
 * 
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.11 $
 */
public class DisplayURI extends AbstractURI {

    /**
     * The serial version number
     */
    private static final long serialVersionUID = 2L;

    /**
     * This relates to the limit on the window title of internet explorer
     * (PR55731). Ultramon uses the title to identify the window.
     */
    private static final int MAX_DISPLAY_URI_LENGTH = 85;

    private static final String DISPLAY_URI_TYPE = "display";

    private static final String DISPLAY_URI_PREFIX = URI_PREFIX + DISPLAY_URI_TYPE + '/';

    private static final char[] DISPLAY_URI_PREFIX_CHARS = DISPLAY_URI_PREFIX.toCharArray();

    // The data

    private final String courthouseName;

    private final String courtsiteCode;

    private final String location;

    private final String display;

    /**
     * Creates a new DisplayURI object.
     * 
     * @param uri
     *            the displayURI as a string (see class comments).
     * 
     * @pre uri != null
     * @post getCourthouseName() != null && getCourtsiteCode() != null &&
     *       getLocation() != null && getDisplay() != null
     * @post toString() != null
     */
    public DisplayURI(final String uri) {
        super(uri);

        if (uri.length() > MAX_DISPLAY_URI_LENGTH) {
            throw new InvalidURIFormatException(uri, "The URI supplied exceeds the maximum limit of "
                    + MAX_DISPLAY_URI_LENGTH + " characters.");
        }

        char[] chars = uri.toCharArray();

        // Prefix
        if (!equals(DISPLAY_URI_PREFIX_CHARS, 0, chars, 0, DISPLAY_URI_PREFIX_CHARS.length)) {
            throw new InvalidURIFormatException("Display Uri \"" + uri + "\" has invalid prefix.");
        }
        // Court House Name
        int start = DISPLAY_URI_PREFIX_CHARS.length;
        int end = indexOf(chars, start, '/');
        int length = end - start;
        if (end == -1 || length == 0 || !isValidDisplayURICharacters(chars, start, length)) {
            throw new InvalidURIFormatException("Display Uri \"" + uri + "\" has invalid court house name.");
        }
        this.courthouseName = new String(chars, start, length);

        // Court Site Code
        start = end + 1;
        end = indexOf(chars, start, '/');
        length = end - start;
        if (end == -1 || length == 0 || !isValidDisplayURICharacters(chars, start, length)) {
            throw new InvalidURIFormatException("Display Uri \"" + uri + "\" has invalid court site code.");
        }
        this.courtsiteCode = new String(chars, start, length);

        // Location
        start = end + 1;
        end = indexOf(chars, start, '/');
        length = end - start;
        if (end == -1 || length == 0 || !isValidDisplayURICharacters(chars, start, length)) {
            throw new InvalidURIFormatException("Display Uri \"" + uri + "\" has invalid location.");
        }
        this.location = new String(chars, start, length);

        // Display
        start = end + 1;
        end = chars.length;
        length = end - start;
        if (length == 0 || !isValidDisplayURICharacters(chars, start, length)) {
            throw new InvalidURIFormatException("Display Uri \"" + uri + "\" has invalid display.");
        }
        this.display = new String(chars, start, length);
    }

    /**
     * Creates a new DisplayURI object.
     * 
     * @pre courthouseName != null
     * @pre courtsiteCode != null
     * @pre location != null
     * @pre display != null
     * @post getCourthouseName() != null && getCourtsiteCode() != null &&
     *       getLocation() != null && getDisplay() != null
     * @post toString() != null
     */
    public DisplayURI(final String courthouseName, final String courtsiteCode, final String location,
            final String display) {
        // Note the create uri validates the courthouseName, courtsiteCode,
        // location, display
        super(createUri(courthouseName, courtsiteCode, location, display));

        this.courthouseName = courthouseName;
        this.courtsiteCode = courtsiteCode;
        this.location = location;
        this.display = display;
    }

    private static String createUri(final String courthouseName, final String courtsiteCode, final String location,
            final String display) {
        if (!isValidDisplayURIString(courthouseName)) {
            throw new InvalidURIFormatException("Invalid courthouseName supplied.");
        }
        if (!isValidDisplayURIString(courtsiteCode)) {
            throw new InvalidURIFormatException("Invalid courtsiteCode supplied.");
        }
        if (!isValidDisplayURIString(location)) {
            throw new InvalidURIFormatException("Invalid location supplied.");
        }
        if (!isValidDisplayURIString(display)) {
            throw new InvalidURIFormatException("Invalid display supplied.");
        }
        String uri = DISPLAY_URI_PREFIX + courthouseName + '/' + courtsiteCode + '/' + location + '/' + display;
        if (uri.length() > MAX_DISPLAY_URI_LENGTH) {
            throw new InvalidURIFormatException(uri, "The URI supplied exceeds the maximum limit of "
                    + MAX_DISPLAY_URI_LENGTH + " characters.");
        }
        return uri;
    }

    /**
     * Displays do not support Localization so they just return the current
     * locale.
     * 
     * @return the default Locale.
     */
    public Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Returns the type of uri this is as a String.
     * 
     * @return the type of URI this is.
     * @post return != null
     */
    public String getType() {
        return DISPLAY_URI_TYPE;
    }

    /**
     * Returns the court house name from the URI.
     * 
     * @return the court house name from the URI.
     */
    public String getCourthouseName() {
        return courthouseName;
    }

    /**
     * Returns the court site name from the URI.
     * 
     * @return the court site name from the URI.
     */
    public String getCourtsiteCode() {
        return courtsiteCode;
    }

    /**
     * Returns the display name from the URI.
     * 
     * @return the display name from the URI.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Returns the location name from the URI.
     * 
     * @return the location name from the URI.
     */
    public String getLocation() {
        return location;
    }

    //
    // Utilities
    //

    private static boolean isValidDisplayURIString(String value) {
        return value != null && value.length() != 0
                && isValidDisplayURICharacters(value.toCharArray(), 0, value.length());
    }

    private static boolean isValidDisplayURICharacters(char[] actual, int offset, int length) {
        for (int i = 0; i < length; i++) {
            if (!isValidDisplayURICharacter(actual[offset + i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidDisplayURICharacter(char actual) {
        return Character.isDigit(actual) || Character.isLetter(actual) || actual == '_';
    }

}
