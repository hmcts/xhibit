package uk.gov.courtservice.framework.jdbc.exception;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: Data Access Exception
 * </p>
 * <p>
 * Description: This is an unchecked exception used by the JDBC framework
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */

public class DataAccessException extends CSUnrecoverableException {

    // Column
    private String column;

    /**
     * Creates an instance with the message
     * 
     * @param Message
     */
    public DataAccessException(String msg) {
        super(msg);
    }

    /**
     * Creates an instance with the message and root cause
     * 
     * @param Message
     * @param Root
     *            cause
     */
    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates an instance with the message
     * 
     * @param Message
     */
    public DataAccessException(String msg, String column) {
        super(msg);
        setColumn(column);
    }

    /**
     * Creates an instance with the message and root cause
     * 
     * @param Message
     * @param Root
     *            cause
     */
    public DataAccessException(String msg, Throwable cause, String column) {
        super(msg, cause);
        setColumn(column);
    }

    /**
     * Sets the column
     * 
     * @param column
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * Gets the column
     * 
     * @return
     */
    public String getColumn() {
        return column;
    }

    /**
     * Returns the message
     */
    public String getMessage() {

        StringBuffer sb = new StringBuffer();
        if (column != null)
            sb.append("column: " + column + "\r\n");
        sb.append(super.getMessage());

        return sb.toString();

    }
}