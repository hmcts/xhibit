package uk.gov.courtservice.framework.jdbc.core;

import java.util.List;

/**
 * <p>
 * Title: An interface for processing rows
 * </p>
 * <p>
 * Description: This can be used by JDBC queries as a callback handler
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

public interface RowProcessor {

    /**
     * This method is called to process the row
     * 
     * @param Current
     *            row
     */
    public void processRow(Row row);

    /**
     * Use this for any pre-processing. Implementing classes should make sure
     * that this is called in processRow before processing and overridden
     * properly to add pre-processing logic.
     * 
     * @see ReflectionRowProcessor
     * @param Current
     *            row
     */
    public void preProcessRow(Row row);

    /**
     * Use this for any post processing. Implementing classes should make sure
     * that this is called in processRow after processing and overridden
     * properly to add post-processing logic.
     * 
     * @see ReflectionRowProcessor
     * @param Current
     *            row
     */
    public void postProcessRow(Row row);

    /**
     * Get all the child processors for this processor
     * 
     * @param Current
     *            row
     */
    public List getChildProcessors();

    /**
     * This method is called after a column in the row is processed
     * 
     * @param Current
     *            row
     */
    public void addChildProcessor(RowProcessor childProcessor);

}