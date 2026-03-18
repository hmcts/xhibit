package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;

/**
 * <p>
 * Title: Query to return all court rooms linked to the VIP Screen
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: VipCourtRoomsQuery.java,v 1.4 2006/06/05 12:29:45 bzjrnl Exp $
 */

public class VipCourtRoomsQuery extends QueryOperation {

    private static final Logger log = CSServices.getLogger(VipCourtRoomsQuery.class);

    /**
     * The query: SELECT * FROM XHB_TERMINAL ORDER BY LOCATION
     */
    private static final String SQL_WITH_SITE = "SELECT cs.short_Name || '-' || cr.DISPLAY_NAME display_Name, cr.DISPLAY_NAME display_Name_No_Site, cr.*, cs.court_Site_Code "
            + "FROM   XHB_COURT_ROOM cr, XHB_DISPLAY_LOCATION dl, XHB_COURT_SITE cs, "
            + "       XHB_DISPLAY d, XHB_DISPLAY_COURT_ROOM dcr "
            + "WHERE  cr.COURT_ROOM_ID = dcr.COURT_ROOM_ID "
            + "AND    dcr.DISPLAY_ID = d.DISPLAY_ID "
            + "AND    d.DISPLAY_LOCATION_ID = dl.DISPLAY_LOCATION_ID "
            + "AND    dl.COURT_SITE_ID = cs.COURT_SITE_ID "
            + "AND    cs.COURT_ID = ? "
            + "AND    d.DESCRIPTION_CODE = 'v_i_p' " + "ORDER BY cr.CREST_COURT_ROOM_NO";

    private static final String SQL_NO_SITE = "SELECT cr.*, cr.DISPLAY_NAME display_Name_No_Site, null court_Site_Code "
            + "FROM   XHB_COURT_ROOM cr, XHB_DISPLAY_LOCATION dl, XHB_COURT_SITE cs, "
            + "       XHB_DISPLAY d, XHB_DISPLAY_COURT_ROOM dcr "
            + "WHERE  cr.COURT_ROOM_ID = dcr.COURT_ROOM_ID "
            + "AND    dcr.DISPLAY_ID = d.DISPLAY_ID "
            + "AND    d.DISPLAY_LOCATION_ID = dl.DISPLAY_LOCATION_ID "
            + "AND    dl.COURT_SITE_ID = cs.COURT_SITE_ID "
            + "AND    cs.COURT_ID = ? "
            + "AND    d.DESCRIPTION_CODE = 'v_i_p' " + "ORDER BY cr.CREST_COURT_ROOM_NO";

    /**
     * Constructor compiles the query
     */
    public VipCourtRoomsQuery(DataSource ds, boolean multiSite) {
        super(ds, (multiSite ? SQL_WITH_SITE : SQL_NO_SITE));
        log.debug("Query object created");
        registerInTypes(new int[] { Types.INTEGER });
    }

    public VipCourtRoomsQuery(boolean multiSite) {
        this(CSServices.getServiceLocator().getDataSource(), multiSite);
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
    public XhbCourtRoomBasicValue[] getData(Integer courtId) {
        XhbCourtRoomRowProcessor rp = new XhbCourtRoomRowProcessor();
        setRowProcessor(rp);
        execute(new Object[] { courtId });
        List results = rp.getResults();
        return (XhbCourtRoomBasicValue[]) results.toArray(new XhbCourtRoomBasicValue[results.size()]);
    }

    class XhbCourtRoomRowProcessor extends ReflectionRowProcessor {
        public XhbCourtRoomRowProcessor() {
            super(XhbCourtRoomBasicValue.class);
            registerDefaultBindings();
        }
    }
}
