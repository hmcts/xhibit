package uk.gov.courtservice.xhibit.business.vos.translation.impl;

/**
 * <p>
 * Title: XmlBuffer
 * </p>
 * <p>
 * Description: Buffer for creating the XML
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: XmlBuffer.java,v 1.4 2006/06/05 12:28:29 bzjrnl Exp $
 */
public class XmlBuffer {
    private static final boolean APPEND_WHITESPACE = getAppendWhitespace();

    private final StringBuffer buffer = new StringBuffer();

    /**
     * Append a line no escape
     * 
     * @param buffer
     */
    public void append(String text) {
        if (text != null) {
            buffer.append(text);
        }
    }

    /**
     * Append a line
     * 
     * @param buffer
     */
    public void appendLine(String text) {
        append(text);
        appendLine();
    }

    /**
     * Append a line
     * 
     * @param buffer
     */
    public void appendLine() {
        if (APPEND_WHITESPACE) {
            buffer.append('\n');
        }
    }

    /**
     * Append the specified indent
     * 
     * @param buffer
     * @param indent
     */
    public void appendIndent(int indent) {
        if (APPEND_WHITESPACE) {
            for (int i = 0; i < indent; i++) {
                buffer.append("    ");
            }
        }
    }

    /**
     * Append the specified attribute if not null
     * 
     * @param buffer
     * @param indent
     */
    public void appendAttribute(String name, String value) {
        if (name != null && value != null) {
            buffer.append(" ");
            appendXml(name);
            buffer.append("=\"");
            appendXml(value);
            buffer.append("\"");
        }
    }

    /**
     * Append the text escaping any characters
     * 
     * @param buffer
     * @param indent
     */
    public void appendXml(String text) {
        if (text != null) {
            char[] source = text.toCharArray();
            for (int i = 0; i < source.length; i++) {
                switch (source[i]) {
                case '"':
                    buffer.append("&quot;");
                    break;
                case '\'':
                    buffer.append("&apos;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                default:
                    buffer.append(source[i]);
                    break;
                }
            }
        }
    }

    /**
     * Convert the buffer into a string
     */
    public String toString() {
        return buffer.toString();
    }

    //
    // Utilities
    //

    private static boolean getAppendWhitespace() {
        String appendWhitespace = System
                .getProperty("uk.gov.courtservice.xhibit.business.vos.translation.impl.XmlBuffer.appendWhitespace");
        return appendWhitespace != null
                && ("YES".equalsIgnoreCase(appendWhitespace) || "TRUE".equalsIgnoreCase(appendWhitespace));
    }
}
