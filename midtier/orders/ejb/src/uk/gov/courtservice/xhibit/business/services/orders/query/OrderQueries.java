package uk.gov.courtservice.xhibit.business.services.orders.query;

import java.sql.Types;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;

/**
 * Fast lane reader class for use in the orders sub-project. Contains calls to
 * all of the stored procedures/functions from the xhb_orders_pkg package.
 * 
 * @author czvsws
 * @version $Revision: 1.3 $
 */
public class OrderQueries extends StoredProcedure {
    /** The log4j logger instance */
    private static final Logger log = CSServices.getLogger(OrderQueries.class);

    /** The name of the database package that the functions belongto */
    private static final String PACKAGE = "xhb_orders_pkg";

    // Constants representing the database function calls...
    private static final String GET_REF_COURTS = "{ ? = call " + PACKAGE + ".get_ref_courts_by_court_Id(?) }";

    /**
     * Private constructor to prevent external instantiation, as all access to
     * the queries is handled by this class via static methods.
     * 
     * @param procedureName
     *            The name of the stored procedure on the database to call.
     */
    private OrderQueries(String procedureName) {
        super(CSServices.getServiceLocator().getDataSource(), procedureName);
    }

    /**
     * Method to retrieve details for all of the courts.
     * 
     * @return An array of <code>XhbRefCourtValue</code> implementations.
     */
    public static XhbRefCourtValue[] getRefCourts(Integer courtId) {
        log.debug("getRefCourts() called with courtId: " + courtId);

        final OrderQueries tq = new OrderQueries(GET_REF_COURTS);
        final RefCourtRowProcessor rowProcessor = new RefCourtRowProcessor();

        tq.registerInTypes(new int[] { Types.INTEGER });
        tq.setRowProcessor(rowProcessor);
        tq.execute(new Object[] { courtId });

        return rowProcessor.getResults();
    }
}
