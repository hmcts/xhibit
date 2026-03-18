package uk.gov.courtservice.xhibit.integration.services.stub;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_exporta.XhbExporta;
import uk.gov.courtservice.xhibit.business.entities.xhb_exporta.XhbExportaBeanHelper2;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: HearingRecordUpdateStub
 * </p>
 * <p>
 * Description: Mimics mercator methods which fall into the hearing record area
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: HearingRecordUpdateStub.java,v 1.8 2005/01/24 08:26:44 tz0d5m
 *          Exp $
 */
public class HearingRecordUpdateStub {
    private static final Logger log = CSServices.getLogger(HearingRecordUpdateStub.class);

    public static final String SUCCESSFUL = "S";

    public HearingRecordUpdateStub() {
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>. The
     * Hearing Record is not sent to CREST, however the status flag is set to
     * successful as it would be if this had occurred without error.
     * 
     * @param exportAID
     *            The id of the exporta record for which to set the flag
     * @throws MercatorException
     *             never
     */
    public void exportHearingRecord(Integer exportAID) throws MercatorException {
        log.debug("exportHearingRecord(Integer exportAID) start with : " + exportAID);
        // XHB_EXPORTA.statusflag - set this to S for successful
        XhbExporta exportaValue = XhbExportaBeanHelper2.findByPrimaryKey(exportAID);
        exportaValue.setStatusFlag(SUCCESSFUL);

        log.debug("exportHearingRecord(Integer exportAID) finished");
    }
}
