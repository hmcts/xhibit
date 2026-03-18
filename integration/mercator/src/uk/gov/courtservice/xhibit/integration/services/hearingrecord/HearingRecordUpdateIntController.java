package uk.gov.courtservice.xhibit.integration.services.hearingrecord;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.IntController;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: CREST Form A related calls
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Cag Onganer
 * @version 1.0
 */

public class HearingRecordUpdateIntController extends IntController {

    private static Logger log = CSServices.getLogger(HearingRecordUpdateIntController.class);

    public HearingRecordUpdateIntController() {
        super();
    }

    public void exportHearingRecord(Integer exportAID) throws MercatorException, TransformationException {
        log.debug("exportHearingRecord EXPORTA(PK):" + exportAID.toString());
        executeUpdate("exportHR", exportAID);
    }
}