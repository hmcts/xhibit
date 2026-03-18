package uk.gov.courtservice.xhibit.business.database.query.listdistribution;

// jdk

import java.sql.Types;
import java.util.Collection;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: SofNotOnListQuery
 * </p>
 * <p>
 * Description: Query object used to retrieve the list of Solicitor Firms not
 * already subscribed to receive a Warned List Letter.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: WLLRecipientNotOnListQuery.java,v 1.2 2004/03/30 10:12:43
 *          qzd3k3 Exp $
 */

public class WLLRecipientNotOnListQuery extends StoredProcedure {
    // The logger for this class.
    private static Logger log = CSServices.getLogger(WLLRecipientNotOnListQuery.class);

    // The file that contains the SQL for this query
    private static final String SQL_FILE = "config/database/query/listdistribution/WLLRecipientNotOnList.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    public WLLRecipientNotOnListQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    public WLLRecipientNotOnListQuery(DataSource ds) {
        super(ds, SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * Gets the RefSolicitorFirms not already subscribed to the Warned List for
     * a given court. The results are returned as
     * <code>WLLRecipientComplexValue</code> objects, if a WLLRecipient record
     * was not found in the db for the given SolicitorFirm the value object will
     * be populated with only the solicitor firm name, crest solicitor firm id
     * and solicitor firm address if one is present.
     * 
     * @param courtId
     *            The id of the court we are working in.
     * @return Collection of <code>WLLRecipientBasicValue</code> objects
     */
    public Collection findWLLRecipientsNotOnList(Integer courtId) {
        log.debug("findWLLRecipientsNotOnList() called with courtId: " + courtId);

        // Check the parameters
        if (courtId == null)
            throw new IllegalArgumentException("courtId");

        // Row processor
        WLLRecipientNotOnListRowProcessor rowProcessor = new WLLRecipientNotOnListRowProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query
        log.debug("Calling the execute method...");
        execute(new Object[] { courtId });

        // Return the counsel collection
        log.debug("findWLLRecipientsNotOnList() getting the returned array...");
        return rowProcessor.findWLLRecipientsNotOnList();
    }
}