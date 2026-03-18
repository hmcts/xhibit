package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.CasesProcessor;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;

/**
 * <p>
 * Title: ScheduleQuery
 * </p>
 * <p>
 * Description: Query object used for getting linked id's for linked cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Edward Cawley $Log: LinkedCasesQuery.java,v $
 * @author Edward Cawley Revision 1.5  2006/06/05 12:29:07  bzjrnl
 * @author Edward Cawley Change: TI901
 * @author Edward Cawley Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @author Edward Cawley
 * @author Edward Cawley Revision 1.4 2006/05/31 14:21:13 bzjrnl
 * @author Edward Cawley Change: TI901
 * @author Edward Cawley Comment: Weblogic Upgrade - Standadise code formatting
 * @author Edward Cawley Revision 1.3 2003/08/26 08:28:38 qzd3k3 Merge from
 *         PRE_RELEASE_0_5_5.
 * 
 * Revision 1.2.6.1 2003/08/21 08:18:23 bzjrnl Changed query so it only requires
 * a single paramater and no longer restricts the selection on type
 * 
 * Revision 1.2 2003/07/11 11:08:06 rz7jlh *** empty log message ***
 * 
 * Revision 1.1 2003/07/11 10:07:51 rz7jlh Moved processors and queries into
 * seperate packages as queries can share processors
 * 
 * Revision 1.2 2003/07/11 08:05:33 rz7jlh Now uses noew model objects.
 * 
 * Revision 1.1 2003/07/09 09:24:34 fz0n8j Initial
 * 
 * Revision 1.1 2003/07/08 14:29:17 rz7jlh *** empty log message ***
 * 
 */

public class LinkedCasesQuery extends QueryOperation {

    // The file that contains the SQL for this query, currently uses two
    // parameters, both case id.
    private static final String SQL_FILE = "config/database/query/crestformsbf/LinkedCase.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public LinkedCasesQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * @return An array of case ids for representing the linked cases
     */
    public CrestFormsBFCase[] getLinkedCases(Integer caseId) {

        // Check the parameter
        if (caseId == null)
            throw new IllegalArgumentException("caseId");

        // Row processor
        CasesProcessor rowProcessor = new CasesProcessor();
        rowProcessor.setLinked(true);
        setRowProcessor(rowProcessor);

        // Execute the query passing in the case id
        execute(new Object[] { caseId });

        // Return the schedule
        return rowProcessor.getCases();

    }

}