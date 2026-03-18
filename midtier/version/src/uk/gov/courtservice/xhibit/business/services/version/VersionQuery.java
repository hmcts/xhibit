package uk.gov.courtservice.xhibit.business.services.version;

import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;

/**
 * This class wraps the stored procedure that provides the data for the court
 * list document.
 * 
 * @author Rakesh Lakhani
 */
public class VersionQuery extends QueryOperation {
    private static final Logger log = CSServices.getLogger(VersionQuery.class);

    /** The stored procedure to execute */
    private static final String SQL = "SELECT * FROM XHB_VERSION ORDER BY DISPLAY_SEQ";

    // "{ call XHB_PUBLIC_DISPLAY_PKG.GET_ALL_COURT_STATUS(?,?,?,?) }";

    /**
     * Constructor compiles the query
     */
    public VersionQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        log.debug("Query object created");
    }

    /**
     * Returns an array of CourtListValue.
     * 
     * @param date
     *            Id
     * @param courtId
     *            room ids for which the data is required
     * @param courtRoomIds
     *            Court room ids
     * 
     * @return Suumary by name data for the specified court rooms
     */
    public VersionValue[] getData() {
        VersionProcessor vp = new VersionProcessor();
        setRowProcessor(vp);
        execute(new Object[] {});
        Collection c = vp.getData();
        return (VersionValue[]) vp.getData().toArray(new VersionValue[c.size()]);
    }
}
