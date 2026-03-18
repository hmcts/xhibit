package uk.gov.courtservice.xhibit.business.database.query;

import java.sql.Clob;
import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.columneditor.StringStrategy;
import uk.gov.courtservice.framework.services.CSServices;

public class UnpublishedWarnedListQuery extends StoredFunction {
	private static final String SQL_FUNCTION = "{? =  call XHB_GET_XML_REPORTS.GET_WARNED_LIST(?,?,?,?,?,?) }";
    private static final int PARAMETER_TYPES[] = { Types.INTEGER, Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN };
	
    /**
     * Initialize the datasource and register the in parameter types
     */
	public UnpublishedWarnedListQuery() {
		super(CSServices.getServiceLocator().getDataSource(), SQL_FUNCTION);
		registerInTypes(PARAMETER_TYPES);
	}

    /**
     * Gets the schedule for the specified list
     * 
     * @param listId
     *            Integer
     * @param includeStandardNotes
     *            boolean
     * @param includePriorityNotes
     *            boolean
     * @param includeRestrictedNotes
     *            boolean
     * @param annotatedList 
     * @return String - Warned list for the specified list
     */
	public String getWarnedList(Integer listId, boolean includeStandardNotes, boolean includePriorityNotes, boolean includeRestrictedNotes, boolean annotatedList) {
        if (listId == null)
            throw new IllegalArgumentException("listId");

        // Execute the query
        String warnedListXml = StringStrategy.getValue((Clob) executeFunction(new Object[] { listId, "", annotatedList, includeStandardNotes, includePriorityNotes, includeRestrictedNotes }, Types.CLOB));

        // Return the schedule
        return warnedListXml;
	}

}
