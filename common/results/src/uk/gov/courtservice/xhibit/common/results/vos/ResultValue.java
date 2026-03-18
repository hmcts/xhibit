package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

/**
 * <p>
 * Title: ResultValue
 * </p>
 * <p>
 * Description: Common functionality for all results values
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell
 * @version 1.0
 */
public abstract class ResultValue implements Serializable {
	
	static final long serialVersionUID = -1851035657056724006L;
	
    /**
     * Used for creating Debug text
     */
    public String NL = System.getProperty("line.separator", "\n");

    /**
     * Used for creating Debug text
     */
    public String TAB = System.getProperty("line.tab", "    ");

    protected static final String UNCODED_OFFENCE = "ZZ99999";

    /**
     * Get a string representation of the object, by default return debug info
     * 
     * @return a String containing debug info
     */
    public String toString() {
        return toDebug();
    }

    /**
     * ResultDebugValue Implementation
     */
    public String toDebug() {
        StringBuffer buffer = new StringBuffer();
        appendDebug(buffer, 0);
        return buffer.toString();
    }

    /**
     * Add debug information about the object to the StringBuffer using the
     * specified indent. This allows an object graph to be dumped efficently.
     * 
     * @param buffer
     *            the buffer to append debug info to
     * @param indent
     *            the indent
     */
    public void appendDebug(StringBuffer buffer, int indent) {

    }

    /**
     * Add indent to the buffer
     * 
     * @param buffer
     *            the buffer to append to
     * @indent the number of indents to add
     */
    public void appendIndent(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; i++) {
            buffer.append(TAB);
        }
    }

    /**
     * Add a new line and indent the buffer
     * 
     * @param buffer
     *            the buffer to append to
     * @indent the number of indents to add
     */
    public void appendLine(StringBuffer buffer, int indent) {
        buffer.append(NL);
        for (int i = 0; i < indent; i++) {
            buffer.append(TAB);
        }
    }

    /**
     * Add a new line and indent the buffer
     * 
     * @param buffer
     *            the buffer to append to
     * @indent the number of indents to add
     */
    public void appendValue(StringBuffer buffer, int indent, Object obj) {
        if (obj instanceof ResultValue) {
            ((ResultValue) obj).appendDebug(buffer, indent);
        } else {
            buffer.append(obj);
        }
    }

}