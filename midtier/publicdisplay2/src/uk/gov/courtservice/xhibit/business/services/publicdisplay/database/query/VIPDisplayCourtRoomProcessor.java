package uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationCourtRoom;

/**
 * <p>
 * Title: VIP Display Document Row Processor
 * </p>
 * <p>
 * Description: Builds an array of VIPDisplayConfigurationDisplayDocuments
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: VIPDisplayCourtRoomProcessor.java,v 1.2 2005/11/17 10:55:48
 *          bzjrnl Exp $
 */

public class VIPDisplayCourtRoomProcessor extends AbstractRowProcessor {
    private static final Logger log = CSServices.getLogger(VIPDisplayCourtRoomProcessor.class);

    private static final String COURT_ROOM_ID = "COURT_ROOM_ID";

    private static final String SHORT_NAME = "SHORT_NAME";

    private static final String DISPLAY_NAME = "DISPLAY_NAME";

    private static final String SHOW_UNASSIGNED_YN = "SHOW_UNASSIGNED_YN";

    private List values = new ArrayList();

    private boolean showUnassignedCases;

    private boolean setUnassigned;

    private Integer courtRoomId;

    private String shortName;

    private String displayName;

    /**
     * For each row extract the description and store in array
     * 
     * @param row
     */
    public void processRow(Row row) {
        log.debug("Process Row called");

        if (!setUnassigned) {
            showUnassignedCases = "Y".equalsIgnoreCase(row.getString(SHOW_UNASSIGNED_YN));
            setUnassigned = true;
        }

        courtRoomId = row.getInteger(COURT_ROOM_ID);
        shortName = row.getString(SHORT_NAME);
        displayName = row.getString(DISPLAY_NAME);

        VIPDisplayConfigurationCourtRoom vipDisplayConfigurationCourtRoom = new VIPDisplayConfigurationCourtRoom(
                courtRoomId, shortName, displayName);

        values.add(vipDisplayConfigurationCourtRoom);
    }

    /**
     * This method returns the array of VIPDisplayConfigurationDisplayDocuments
     * 
     * @return VIPDisplayConfigurationDisplayDocument[]
     *         VIPDisplayConfigurationDisplayDocuments
     */
    public Collection getData() {
        return values;
    }

    /**
     * This method returns whether unassigned cases are allowed.
     * 
     * @return boolean
     */
    public boolean isShowUnassignedCases() {
        return showUnassignedCases;
    }
}