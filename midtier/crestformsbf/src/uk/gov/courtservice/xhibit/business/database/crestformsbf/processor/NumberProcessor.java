package uk.gov.courtservice.xhibit.business.database.crestformsbf.processor;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

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
 * @author Edward Cawley, Xdevelopment LLP (2003)
 */

public class NumberProcessor extends AbstractRowProcessor {

    private static final String ROW_COUNT = "row_count";

    private int number = 0;

    /**
     * Instantiates the object
     * 
     * @param courtId
     */
    public NumberProcessor() {
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     */
    public void processRow(Row row) {
        number = row.getInt(ROW_COUNT);
    }

    /**
     * Returns count
     * 
     * @return
     */
    public int getNumber() {
        return number;
    }

}