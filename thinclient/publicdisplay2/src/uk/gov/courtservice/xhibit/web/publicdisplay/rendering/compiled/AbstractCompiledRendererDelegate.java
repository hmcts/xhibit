package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled;

public abstract class AbstractCompiledRendererDelegate {
    // The new line character
    private static final String NL = "\n";

    protected static final String SPACE = " ";

    protected static final String SEMI_COLON = ";";

    protected static final String COLON = ":";

    /**
     * Append the value
     */
    protected void append(StringBuffer buffer, int value) {
        buffer.append(value);
    }

    /**
     * Append the value
     */
    protected void append(StringBuffer buffer, Object value) {
        buffer.append(value);
    }

    /**
     * Append the value or the alternative if value is null. Calls to this
     * method are made where the template makes an unguarded write of an object
     * from the velocity context.
     */
    protected void append(StringBuffer buffer, Object value, Object alternative) {
        if (value != null) {
            append(buffer, value);
        } else {
            append(buffer, alternative);
        }
    }

    /**
     * Append a new line
     */
    protected void appendln(StringBuffer buffer) {
        append(buffer, NL);
    }

    /**
     * Append the value as a new line
     */
    protected void appendln(StringBuffer buffer, Object value) {
        append(buffer, value);
        append(buffer, NL);
    }

    /**
     * Append the value as a new line
     */
    protected void appendln(StringBuffer buffer, Object value, Object alternative) {
        if (value != null) {
            append(buffer, value);
        } else {
            append(buffer, alternative);
        }
        append(buffer, NL);
    }

}
