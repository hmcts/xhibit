package uk.gov.courtservice.xhibit.database.exiss.messagebuilder.processor;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * <p>
 * Title: ClobDataRowProcessor
 * </p>
 * <p>
 * Description: Read the clob data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Jeremy Shields
 * @version $Id: ClobRowProcessor.java,v 1.3 2006/07/14 10:10:49 bzjrnl Exp $
 */
public final class ClobRowProcessor extends AbstractRowProcessor {

    private final String columnName;

    // The clob data
    private String clob;

    /**
     * Construct a new row processor to read the specified column
     * 
     * @param columnName
     */
    public ClobRowProcessor(String columnName) {
        if (columnName == null) {
            throw new IllegalArgumentException("columnName: null");
        }
        this.columnName = columnName;
    }

    /**
     * Get the values created when the row was processed
     * 
     */
    public String getClob() {
        return clob;
    }

    /**
     * Create a String containg the contents of a clob.
     * 
     * @param row
     *            The row to process.
     * @throws IllegalStateException
     *             if called more than once
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        if (clob != null) {
            throw new IllegalStateException("clob: not null (" + clob.length() + ").");
        }
        clob = row.getClobAsString(columnName);
    }

}
