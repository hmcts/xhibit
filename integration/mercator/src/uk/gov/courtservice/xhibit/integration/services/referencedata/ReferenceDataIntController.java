package uk.gov.courtservice.xhibit.integration.services.referencedata;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.integration.services.IntController;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: ReferenceDataIntController
 * </p>
 * <p>
 * Description: This is the controller for reference data. The controller will
 * manage all the methods for calling Mercator for Reference data updates.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class ReferenceDataIntController extends IntController {

    private static Logger log = CSServices.getLogger(ReferenceDataIntController.class);

    /**
     * @roseuid 3DEF2CFA003A
     */
    public ReferenceDataIntController() {
        super();
    }

    /**
     * This method will import the global Offences for a specific court.
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF214602EF
     */
    public void importOffenceRefData(Integer courtID) throws MercatorException {
        log.debug("importOffenceRefData. courtID = " + courtID);
        executeUpdate("importOffenceRefData", courtID);
    }

    /**
     * This method will import all the global reference data except for
     * Offences. The global reference data that will be imported:
     * 
     * SYSTEM_REF_CODES DISPOSAL DISPOSAL_MENU HEARING_TYPE REF_COURTs
     * 
     * @param courtID -
     * 
     * 
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF21C1022E
     */
    public void importGlobalRefData(Integer courtID) throws MercatorException {
        log.debug("importGlobalRefData. courtID = " + courtID);
        executeUpdate("importGlobalRefData", courtID);
    }

    /**
     * this method will import all the local reference data, such as:
     * 
     * JUDGE JUSTICE COURT_REPORTER COURT_REPORTER_FIRM SOLICITOR_FIRM
     * PROSECUTOR_AGENCY
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF21EE03AF
     */
    public void importLocalRefData(Integer courtID) throws MercatorException {
        log.debug("importLocalRefData. courtID = " + courtID);
        executeUpdate("importLocalRefData", courtID);
    }

    /**
     * This method will import all the local reference data for Global data with
     * local override. This means that the data can be either local or global.
     * This method will only import the local entries. Local data to be
     * imported:
     * 
     * ADVOCATES CHAMBERS
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF21FA0077
     */
    public void importLocalOverrideRefData(Integer courtID) throws MercatorException {
        log.debug("importLocalOverrideRefData. courtID = " + courtID);
        executeUpdate("importLocalOverrideRefData", courtID);
    }

    /**
     * This method will import all the global reference data for Global data
     * with local override. This means that the data can be either local or
     * global. This method will only import the global entries. Global data to
     * be imported:
     * 
     * ADVOCATES CHAMBERS
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF220C0290
     */
    public void importGlobalOverrideRefData(Integer courtID) throws MercatorException {
        log.debug("importGlobalOverrideRefData. courtID = " + courtID);
        executeUpdate("importGlobalOverrideRefData", courtID);
    }
}
