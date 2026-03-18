package uk.gov.courtservice.xhibit.business.database.listdistribution.processor;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;

/**
 * <p>
 * Title: WllControlComplexValueRowProcessor
 * </p>
 * <p>
 * Description: Create an object representing the distribution list control
 * data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WllControlComplexValueRowProcessor.java,v 1.3 2005/02/08
 *          13:56:44 bzjrnl Exp $
 */
public final class WllControlComplexValueRowProcessor extends AbstractWllControlComplexValueRowProcessor {

    // The control summary record
    private WllControlComplexValue value = null;

    /**
     * Get the values created when the last row was processed
     * 
     * @throws IllegalStateException
     *             if the summary has not been created
     */
    public WllControlComplexValue getWllControlComplexValue() {
        return value;
    }

    /**
     * Create a ControlSummary for the FIRST row only
     * 
     * @param row
     *            The row to process.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        if (value == null) {
            value = createComplexValue(row);
        }
    }
}
