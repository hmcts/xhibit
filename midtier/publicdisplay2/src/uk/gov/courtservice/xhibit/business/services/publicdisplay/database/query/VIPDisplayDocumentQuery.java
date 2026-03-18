package uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationDisplayDocument;

/**
 * <p>
 * Title: VIP display documents for court site query
 * </p>
 * <p>
 * Description: This runs the stored procedure that for a given court site, it
 * finds all display documents configured for the View Information Pages.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: VIPDisplayDocumentQuery.java,v 1.2 2005/11/17 10:55:48 bzjrnl
 *          Exp $
 */

public class VIPDisplayDocumentQuery extends StoredProcedure {
    /** Logger object */
    private static final Logger log = Logger.getLogger(VIPDisplayDocumentQuery.class);

    /** Parameters expected by the stored procedure */
    private static final int[] PARAMETER_TYPES = { Types.NUMERIC };

    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_VIP_DISPLAY_DOCS_FOR_SITE(?,?) }";

    public VIPDisplayDocumentQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_PROCEDURE);
        this.registerInTypes(PARAMETER_TYPES);
    }

    /**
     * Returns an array of CourtListValue. ***TODO***
     * 
     * @param courtSiteId
     *            court site id
     * 
     * @return Suumary by name data for the specified court rooms
     */
    public Collection<VIPDisplayConfigurationDisplayDocument> getData(Integer courtSiteId) {
        VIPDisplayDocumentProcessor processor = new VIPDisplayDocumentProcessor();
        setRowProcessor(processor);

        // Log the parameters
        logParams(courtSiteId);

        // Execute the stored procedure
        execute(new Object[] { courtSiteId });
        log.debug("Stored procedure executed");

        // Return the data (never a null!)
        if (processor.getData() == null) {
            return new ArrayList<VIPDisplayConfigurationDisplayDocument>();
        } else {
            return processor.getData();
        }
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
