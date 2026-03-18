package uk.gov.courtservice.framework.jdbc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: AbstractRowProcessor.java,v 1.5 2006/06/05 12:30:15 bzjrnl Exp $
 */
public abstract class AbstractRowProcessor implements RowProcessor {
    // Child processors
    private final List childProcessors = new ArrayList();

    /**
     * @see <code>RowProcessor</code>
     * @param row
     */
    public abstract void processRow(final Row row);

    /**
     * @see <code>RowProcessor</code>
     * @return
     */
    public List getChildProcessors() {
        return this.childProcessors;
    }

    /**
     * @param childProcessor
     * @see <code>RowProcessor</code>
     */
    public void addChildProcessor(final RowProcessor childProcessor) {
        if (childProcessor == null) {
            throw new IllegalArgumentException("childProcessor");
        }

        this.childProcessors.add(childProcessor);
    }

    /**
     * Use this for any pre-processing. Implementing classes should make sure
     * that this is called in processRow before processing and overridden
     * properly to add pre-processing logic.
     * 
     * @see RowProcessor
     * @param Current
     *            row
     */
    public void preProcessRow(final Row row) {
        // no implementation required by default...
    }

    /**
     * Use this for any post processing. Implementing classes should make sure
     * that this is called in processRow after processing and overridden
     * properly to add post-processing logic.
     * 
     * @see RowProcessor
     * @param Current
     *            row
     */
    public void postProcessRow(final Row row) {
        // no implementation required by default...
    }
}