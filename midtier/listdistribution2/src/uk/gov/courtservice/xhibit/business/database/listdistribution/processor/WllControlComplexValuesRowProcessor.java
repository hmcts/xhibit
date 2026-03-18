package uk.gov.courtservice.xhibit.business.database.listdistribution.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;

/**
 * <p>
 * Title: WllControlComplexValuesRowProcessor
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
 * @version $Id: WllControlComplexValuesRowProcessor.java,v 1.2 2005/02/08
 *          13:56:44 bzjrnl Exp $
 */
public final class WllControlComplexValuesRowProcessor extends AbstractWllControlComplexValueRowProcessor {
    // The list of control summary records
    private List valueList = new ArrayList();

    /**
     * Get the values created when the last row was processed
     * 
     * @throws IllegalStateException
     *             if the summary has not been created
     */
    public WllControlComplexValue[] getWllControlComplexValues() {
        return (WllControlComplexValue[]) valueList.toArray(new WllControlComplexValue[valueList.size()]);
    }

    /**
     * Create a ControlSummary for each row.
     * 
     * @param row
     *            The row to process.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        valueList.add(createComplexValue(row));
    }
}
