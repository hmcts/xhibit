package uk.gov.courtservice.xhibit.business.database.results.processor;

import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 */

public class DisposalCountForResultsRowProcessor extends AbstractRowProcessor {

    private static final String DISPOSAL_ID = "DISPOSAL2_ID";

    private Vector disposalResult = new Vector();

    private static final Logger log = CSServices.getLogger(DisposalCountForResultsRowProcessor.class);

    /**
     * Instantiates the object
     *
     * @param courtId
     */
    public DisposalCountForResultsRowProcessor() {
    }

    /**
     * Implementation of row processor
     *
     * @param row
     */
    public void processRow(Row row) {
        disposalResult.add(new Integer(row.getInt(DISPOSAL_ID)));
    }

    /**
     * Returns count
     *
     * @return
     */
    public int getNumber() {
        log.debug("<<<>>> ROWS RETURNED = " + disposalResult.size());
        return disposalResult.size();
    }

}