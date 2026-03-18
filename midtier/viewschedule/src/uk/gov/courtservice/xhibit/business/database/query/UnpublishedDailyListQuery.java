package uk.gov.courtservice.xhibit.business.database.query;

import java.sql.Clob;
import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.columneditor.StringStrategy;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: UnpublishedDailyListQuery
 * </p>
 * <p>
 * Description: Class for executing and processing the Daily list.
 * <p>
 * Copyright: Copyright (c) 20018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author David Burden
 */
public class UnpublishedDailyListQuery extends StoredFunction {
    private static final String SQL_PROCEDURE = "{? = call XHB_GET_XML_REPORTS.GET_DAILY_LIST(?, ?, ?) }";

    private static final int PARAMETER_TYPES[] = { Types.INTEGER, Types.VARCHAR, Types.BOOLEAN };

    /**
     * Initialize the datasource and register the in parameter types
     */
    public UnpublishedDailyListQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_PROCEDURE);
        registerInTypes(PARAMETER_TYPES);
    }

    /**
     * Gets the schedule for the specified list
     * 
     * @param listId
     *            Integer
     * @param showCourtList
     *            Boolean
     * @param courtId
     *            Integer
     * @return DailyListValue - Daily list for the specified list
     */
    public String getDailyList(final Integer listId, final Boolean showCourtList) {
        // Check the parameters
        if (listId == null)
            throw new IllegalArgumentException("listId");

        String dailyListXml = StringStrategy.getValue((Clob) executeFunction(new Object[] { listId, "", showCourtList }, Types.CLOB));
        // Execute the query
        return dailyListXml ;
    }
}
