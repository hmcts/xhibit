package uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Active Cases Row Processor
 * </p>
 * <p>
 * Description: Builds an array of scheduled hearing id's
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ActiveCasesInRoomProcessor.java,v 1.4 2005/11/17 10:55:47
 *          bzjrnl Exp $
 */

public class ActiveCasesInRoomProcessor extends AbstractRowProcessor {
    private static final Logger log = CSServices.getLogger(ActiveCasesInRoomProcessor.class);

    private static final String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    private ArrayList values = new ArrayList();

    /**
     * For each row extract the scheduled hearing id and store in array
     * 
     * @param row
     */
    public void processRow(Row row) {
        log.debug("Process Row called");
        Integer scheduleHearingId = new Integer(row.getInt(SCHEDULED_HEARING_ID));
        log.debug("Adding SH to collection with ID: " + scheduleHearingId);
        values.add(scheduleHearingId);
    }

    /**
     * This method returns the array of SCHEDULED_HEARING_ID's as Integer
     * 
     * @return Integer SCHEDULED_HEARING_ID's
     */
    public Collection getData() {
        return values;
    }
}