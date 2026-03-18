package uk.gov.courtservice.xhibit.business.services.crestimport;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_crest_import.XhbCrestImport;
import uk.gov.courtservice.xhibit.business.entities.xhb_crest_import.XhbCrestImportBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_crest_import.XhbCrestImportBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_crest_import_type.XhbCrestImportType;
import uk.gov.courtservice.xhibit.business.entities.xhb_crest_import_type.XhbCrestImportTypeBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.crestimport.CourtVO;
import uk.gov.courtservice.xhibit.business.vos.services.crestimport.CrestImportStatus;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This is the bean class for the crest import controller
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @ejb.bean name="CrestImportController" description="Crest Import Session
 *           Bean" type="Stateless" view-type="remote"
 *           jndi-name="CrestImportControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class CrestImportControllerBean extends CSSessionBean implements SessionBean {
    /**
     * Gets all the courts
     * 
     * @ejb.interface-method view-type="remote"
     * @return All the courts
     */
    public CourtVO[] getCourts() {
        // Find the court
        Collection courts = XhbCourtBeanHelper2.findAll();

        // Array of value objects
        CourtVO[] ret = new CourtVO[courts.size()];
        log.debug("No: of courts:" + ret.length);

        // Create the value object araay
        Iterator it = courts.iterator();
        for (int i = 0; i < ret.length && it.hasNext(); i++) {
            Court court = (Court) it.next();
            ret[i] = new CourtVO(court.getCourtId(), court.getShortName());
        }

        // Return the array
        return ret;
    }

    /**
     * The method gets all the crest import statuses for the given court
     * 
     * @ejb.interface-method view-type="remote"
     * @param courtId
     *            The id of the court.
     * @throws CrestImportException
     */
    public CrestImportStatus[] getCrestImportStatus(Integer courtId) throws CrestImportException {

        if (courtId == null)
            throw new IllegalArgumentException("courtId");

        // Get the crest import statuses for the court
        Collection crestImports = XhbCrestImportBeanHelper2.findUserInitiatedCrestImportsByCourtId(courtId);

        // Check the statuses already exists for the court
        if (crestImports.size() == 0) {
            log.debug("Creating statuses for courtId: " + courtId);
            this.createStatuses(courtId);

            crestImports = XhbCrestImportBeanHelper2.findUserInitiatedCrestImportsByCourtId(courtId);
        }

        // Array of statuses that are returned
        CrestImportStatus[] ret = new CrestImportStatus[crestImports.size()];

        // Loop through the statuses
        Iterator it = crestImports.iterator();
        for (int i = 0; i < ret.length && it.hasNext(); i++) {
            XhbCrestImport status = (XhbCrestImport) it.next();
            ret[i] = new CrestImportStatus(status.getCrestImportId(), status.getXhbCrestImportType().getDescription(),
                    status.getStatus(), status.getVersion());
        }

        // Return the array
        return ret;
    }

    /**
     * This method initiates the transfer for the given crest import types
     * 
     * @param crestImportStatusList
     *            representing the court and import type
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @return The new crest import statuses for the court
     * @throws CrestImportException
     */
    public CrestImportStatus[] initiateTransfer(CrestImportStatus[] crestImportStatusList) throws CrestImportException {

        if (crestImportStatusList == null)
            throw new IllegalArgumentException("crestImportIds");

        Integer courtId = null;

        for (int i = 0; i < crestImportStatusList.length; i++) {

            // Get the crest import record
            XhbCrestImport crestImport = crestImport = XhbCrestImportBeanHelper2
                    .findByPrimaryKey(crestImportStatusList[i].getCrestImportId());

            // Get the court id
            if (courtId == null)
                courtId = crestImport.getCourtId();

            log.info("Requesting import for court id " + courtId + ", "
                    + crestImport.getXhbCrestImportType().getDescription());

            if (!crestImport.getVersion().equals(crestImportStatusList[i].getVersion())) {
                log.error("Optimistic Lock Error: Entity: " + crestImport.getVersion() + "Requested version: "
                        + crestImportStatusList[i].getVersion());
                throw new OptimisticLockException("Optimistic Lock Error");
            }

            crestImport.setStatus(CrestImportStatus.REQUESTED);

        }

        // Return the new status
        return getCrestImportStatus(courtId);
    }

    // This method created the initial status records for the court
    private void createStatuses(Integer courtId) {
        XhbCourt court = XhbCourtBeanHelper2.findByPrimaryKey(courtId);

        Iterator crestImportTypes = XhbCrestImportTypeBeanHelper2.findAll().iterator();
        while (crestImportTypes.hasNext()) {
            XhbCrestImportType type = (XhbCrestImportType) crestImportTypes.next();
            XhbCrestImportBasicValue xhbCrestImportBasicValue = new XhbCrestImportBasicValue();
            xhbCrestImportBasicValue.setStatus(CrestImportStatus.NEW);
            XhbCrestImportBeanHelper2.create(xhbCrestImportBasicValue, type, court);
        }
    }

}