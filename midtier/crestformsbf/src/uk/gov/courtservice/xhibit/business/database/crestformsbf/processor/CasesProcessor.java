package uk.gov.courtservice.xhibit.business.database.crestformsbf.processor;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;

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
 * 
 */

public class CasesProcessor extends AbstractRowProcessor {

    // column names
    private static final String CASE_ID = "CASE_ID";

    private static final String CASE_TYPE = "CASE_TYPE";

    private static final String CASE_SUB_TYPE = "CASE_SUB_TYPE";

    private static final String CASE_NUMBER = "CASE_NUMBER";

    private boolean linked = false;

    // Schedule object that is created
    private ArrayList cases = new ArrayList();

    /**
     * Instantiates the object
     */
    public CasesProcessor() {
    }

    /**
     * Sets the linked status for this query
     * 
     * @param linked
     */
    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     */
    public void processRow(Row row) {
        Integer id = new Integer(row.getInt(CASE_ID));
        String type = row.getString(CASE_TYPE);
        String subType = row.getString(CASE_SUB_TYPE);
        if (subType == null) { // there may be some incorrect data in the
            // database
            subType = "";
        }
        Integer number = new Integer(row.getInt(CASE_NUMBER));
        CrestFormsBFCase cf = new CrestFormsBFCase(id, type, subType, number, linked);
        cases.add(cf);
    }

    /**
     * Returns the case ids
     * 
     * @return
     */
    public CrestFormsBFCase[] getCases() {
        CrestFormsBFCase[] casesarray = new CrestFormsBFCase[cases.size()];
        for (int i = 0; i < casesarray.length; i++) {
            casesarray[i] = (CrestFormsBFCase) cases.get(i);
        }
        return casesarray;
    }

}