package uk.gov.courtservice.xhibit.business.database.crestformsbf.processor;

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
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Entwistle
 */

public class OffenceCountRowProcessor extends AbstractRowProcessor {

    private static final String OFFENCE_ID = "OFFENCE_ID";

    private Vector offenceResult = new Vector();

    private static final Logger log = CSServices.getLogger(OffenceCountRowProcessor.class);

    /**
     * Instantiates the object
     * 
     * @param courtId
     */
    public OffenceCountRowProcessor() {
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     */
    public void processRow(Row row) {
        offenceResult.add(new Integer(row.getInt(OFFENCE_ID)));
    }

    /**
     * Returns count
     * 
     * @return
     */
    public int getNumber() {
        log.debug("<<<>>> ROWS RETURNED = " + offenceResult.size());
        return offenceResult.size();
    }

}