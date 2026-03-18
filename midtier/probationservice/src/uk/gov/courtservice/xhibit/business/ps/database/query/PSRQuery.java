package uk.gov.courtservice.xhibit.business.ps.database.query;

import java.sql.Types;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: PSRQuery
 * </p>
 * <p>
 * Description: Queries PSR Request Table
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe and Surtar Bachra
 * @version $Revision: 1.4 $
 */
public class PSRQuery extends StoredProcedure {
    private static final String UNISSUED_PSR_SQL_PROCEDURE = "{ call xhb_psr_request_pkg.get_unissued_psrs(?,?) }";

    private static final String ISSUED_PSR_SQL_PROCEDURE = "{ call xhb_psr_request_pkg.get_issued_psrs(?,?) }";

    private static final int PARAMETER_TYPES[] = { Types.INTEGER };

    /**
     * @param sqlProcedure
     *            use one of the static sql procedure attributes
     */
    private PSRQuery(String sqlProcedure) {
        super(CSServices.getServiceLocator().getDataSource(), sqlProcedure);
        registerInTypes(PARAMETER_TYPES);
    }

    public static List getUnissuedPsrRequests(Integer courtId) {
        return getPsrRequest(courtId, UNISSUED_PSR_SQL_PROCEDURE);
    }

    public static List getIssuedPsrRequests(Integer courtId) {
        return getPsrRequest(courtId, ISSUED_PSR_SQL_PROCEDURE);
    }

    private static List getPsrRequest(Integer courtId, String procedure) {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId cannot be null");
        }

        final PSRQuery pq = new PSRQuery(procedure);
        final PSRRequestRowProcessor rowProcessor = new PSRRequestRowProcessor();

        pq.setRowProcessor(rowProcessor);
        pq.execute(new Object[] { courtId });

        return rowProcessor.getPSRRequests();
    }
}
