package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.jdbc.core.columneditor.ColumnExtractionStrategy;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.SummaryByNameValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;

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
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version $Id: AllCaseStatusProcessor.java,v 1.6 2005/11/27 13:04:59 szfnvt
 *          Exp $
 */

public class AllCaseStatusProcessor extends SummaryByNameProcessor {

    /** Cache for summary by name values */
    private List<SummaryByNameValue> allCaseStatusValues = new ArrayList<SummaryByNameValue>();

    /**
     * No argument constructor
     */
    public AllCaseStatusProcessor() {
        super();
    }

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        AllCaseStatusValue value = new AllCaseStatusValue();
        populateData(row, value);
        allCaseStatusValues.add(value);
    }

    /**
     * Returns the final data
     * 
     * @return An array of summary by name value objects
     */
    Collection<SummaryByNameValue> getData() {
        return allCaseStatusValues;
    }

    /**
     * Populates a SummaryByNameValue
     * 
     * @param row
     */
    protected void populateData(Row row, AllCaseStatusValue value) {
        // Populate public display
        // uk.gov.courtservice.xhibit.publicdisplay.publicdisplay data
        super.populateData(row, value);

        value.setCaseNumber(row.getString(CASE_NUMBER));
        value.setHearingDescription(row.getString(HEARING_DESCRIPTION));
        value.setHearingProgress(row.getInt(HEARING_PROGRESS));
        value.setCaseTitle(row.getString(CASE_TITLE));
        value.setListCourtRoomId(row.getInt(LIST_COURT_ROOM_ID));

        String logXML = (String) (ColumnExtractionStrategy.getStrategy(String.class).getValue(row, COURT_LOG_ENTRY));

        value.setEvent((BranchEventXMLNode) EventXMLNodeHelper.buildEventNode(logXML));
        value.setEventTime(row.getTimestamp(COURT_LOG_ENTRY_TIME));
    }
}