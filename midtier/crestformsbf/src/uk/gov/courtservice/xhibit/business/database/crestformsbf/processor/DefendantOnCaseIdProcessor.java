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

public class DefendantOnCaseIdProcessor extends AbstractRowProcessor {

    // Column name for scheduled hearing id
    private static final String DEFENDANT_ON_CASE_ID = "defendant_on_case_id";

    // Schedule object that is created
    private Integer defendantOnCaseId = null; // should only be one

    /**
     * Instantiates the object
     * 
     * @param courtId
     */
    public DefendantOnCaseIdProcessor() {
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     */
    public void processRow(Row row) {
        defendantOnCaseId = new Integer(row.getInt(DEFENDANT_ON_CASE_ID));
    }

    /**
     * Returns the case ids
     * 
     * @return
     */
    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }

}