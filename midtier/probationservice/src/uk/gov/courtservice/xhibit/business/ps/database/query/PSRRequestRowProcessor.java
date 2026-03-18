package uk.gov.courtservice.xhibit.business.ps.database.query;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.entities.PSRSummaryValue;

/**
 * <p>
 * Title: PSRRequestRowProcessor
 * </p>
 * <p>
 * Description: Implements processRow to populate collection from result set
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe & Surtar Bachra
 * @version $Revision: 1.4 $
 */
public class PSRRequestRowProcessor extends AbstractRowProcessor {
    // database column look ups
    private static final String PSR_REQUEST_ID = "PSR_REQUEST_ID";

    private static final String CASE_NUMBER = "CASENUMBER";

    private static final String DEFENDANT = "DEFENDANT";

    private static final String PSR_COURT_ROOM = "PSR_COURT_ROOM";

    private static final String PSR_STATUS = "PSR_STATUS";

    private final List psrRequests = new ArrayList();

    public void processRow(Row row) {
        final PSRSummaryValue psrVal = new PSRSummaryValue();

        psrVal.setId(row.getInteger(PSR_REQUEST_ID));
        psrVal.setCaseNumber(row.getString(CASE_NUMBER));
        psrVal.setDefendant(row.getString(DEFENDANT));
        psrVal.setPsrCourtRoom(row.getString(PSR_COURT_ROOM));
        psrVal.setPsrStatus(row.getString(PSR_STATUS));

        psrRequests.add(psrVal);
    }

    /**
     * @return <code>List</code> of PSRSummaryValues
     */
    public List getPSRRequests() {
        return psrRequests;
    }
}
