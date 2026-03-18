package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.sql.Types;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.PublicNoticeValue;

/**
 * Abstract query class used by public display
 * 
 * @author pznwc5
 */
public class PublicNoticeQuery extends StoredProcedure {

    /** Parameters expected by the stored procedure */
    private static final int[] PARAMETER_TYPES = { Types.NUMERIC };

    /** The stored procedure to execute */
    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_PUBLIC_NOTICES(?,?) }";

    /** Logger object */
    protected final Logger log = Logger.getLogger(getClass());

    /**
     * Creates a new PublicDisplayQuery object.
     * 
     * @param sql
     *            Stored procedure call
     */
    protected PublicNoticeQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_PROCEDURE);
        this.registerInTypes(PARAMETER_TYPES);
        log.debug("Row processor set");
    }

    /**
     * Executes the stored procedure and returns the data.
     * 
     * @param date
     *            Id
     * @param courtId
     *            room ids for which the data is required
     * @param courtRoomIds
     *            Court room ids
     * @param processor
     *            to use
     * 
     * @return Public display data
     */
    protected PublicNoticeValue[] execute(int courtRoomId) {

        // Set the row processor
        PublicNoticeProcessor processor = new PublicNoticeProcessor();
        setRowProcessor(processor);

        // Execute the stored procedure
        execute(new Object[] { new Integer(courtRoomId) });
        log.debug("Stored procedure executed");

        // Return the data
        return processor.getPublicNotices();

    }

}
