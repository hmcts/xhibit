package uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: VIP court rooms for court site query
 * </p>
 * <p>
 * Description: This runs the stored procedure that for a given court site, it
 * finds all court rooms configured for the View Information Pages. Information
 * on unassigned cases is also provided.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: VIPDisplayCourtRoomQuery.java,v 1.2 2005/11/17 10:55:48 bzjrnl
 *          Exp $
 */

public class VIPDisplayCourtRoomQuery extends StoredProcedure {
    /** Logger object */
    private static final Logger log = Logger.getLogger(VIPDisplayCourtRoomQuery.class);

    /** Parameters expected by the stored procedure */
    private static final int[] PARAMETER_TYPES = { Types.NUMERIC };

    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_VIP_COURT_ROOMS_FOR_SITE(?, ?) }";

    private boolean showUnassignedCases;

    public VIPDisplayCourtRoomQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_PROCEDURE);
        this.registerInTypes(PARAMETER_TYPES);
    }

    /**
     * Returns an array of VIPDisplayConfigurationCourtRoom objects.
     * 
     * @param courtSiteId
     *            court site id
     * 
     * @return Collection
     */
    public Collection getData(Integer courtSiteId) {
        VIPDisplayCourtRoomProcessor processor = new VIPDisplayCourtRoomProcessor();
        setRowProcessor(processor);

        // Log the parameters
        logParams(courtSiteId);

        // Execute the stored procedure
        execute(new Object[] { courtSiteId });
        log.debug("Stored procedure executed");

        // Return the data (never a null!)
        if (processor.getData() == null) {
            return new ArrayList();
        } else {
            showUnassignedCases = processor.isShowUnassignedCases();
            return processor.getData();
        }
    }

    /**
     * Returns boolean for Unassigned Cases
     * 
     * @return boolean
     */
    public boolean isShowUnassignedCases() {
        return showUnassignedCases;
    }

    /**
     * Logs the parameters
     * 
     * @param courtSiteId
     */
    protected void logParams(Integer courtSiteId) {
        log.debug("court Site Id: " + courtSiteId);
    }
}
