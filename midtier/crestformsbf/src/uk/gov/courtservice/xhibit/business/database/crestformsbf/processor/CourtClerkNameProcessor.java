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

public class CourtClerkNameProcessor extends AbstractRowProcessor {

    // Column name for scheduled hearing id
    private static final String STAFF_NAME = "staff_name";

    private String courtClerk = null; // should only be one?

    /**
     * Instantiates the object
     */
    public CourtClerkNameProcessor() {
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     */
    public void processRow(Row row) {
        courtClerk = row.getString(STAFF_NAME);
    }

    /**
     * Returns the case ids
     * 
     * @return
     */
    public String getCourtClerk() {
        return courtClerk;
    }

}