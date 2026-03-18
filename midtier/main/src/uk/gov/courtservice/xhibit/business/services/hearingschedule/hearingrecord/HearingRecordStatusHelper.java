package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//JDK
import java.util.ArrayList;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.exporta.ExportA;
import uk.gov.courtservice.xhibit.business.entities.exporta.ExportAMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;

/**
 * 
 * <p>
 * Title: HearingRecordStatusHelper
 * </p>
 * <p>
 * Description: This class will serve as helper class to look up different
 * states of various hearing record flags. E.g. Check if a hearing has been
 * ended, exported etc.
 * 
 * The different export states are (See HearingRecordConstants):
 * READY_FOR_EXPORT = "R" IN_PROGRESS = "P" EXPORT_SUCCESS = "S" EXPORT_FAILED =
 * "F" EXPORT_LOCKED = "L"
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg, Anthony Martin
 * @version 1.0
 * 
 * <Change History/>
 * 
 * <P>
 * 17/02/03 - MH - Added comments and tided up in the code: - removed method
 * endHearing() - added getExportState - added getExportValue - removed debug() -
 * created the code for hasHearinEnded() - created code for isExported() - NOTE :
 * there are a few TODOs since all code in other packages has not yet been
 * created.
 * </P>
 * 
 * <P>
 * 18/02/03 - MH - Added implementation for: - getExportState - getExportValue
 * </p>
 * 
 * 
 * <P>
 * 24/02/03 - MH - Updated with changes in Maintainers -
 * hearingmaintainer.findByPK - exportamaintainer.findbyHearingorLinkedHearingID
 * </p>
 */

public class HearingRecordStatusHelper {

    // Logger to be used in this class
    private static Logger log = CSServices.getLogger(HearingRecordStatusHelper.class);

    // Maintainers
    private HearingMaintainer hearingMaintainer = null;

    private ExportAMaintainer exportAMaintainer = null;

    /**
     * Default constructor that instantiate the Maintainers.
     */
    public HearingRecordStatusHelper() {
        hearingMaintainer = new HearingMaintainer();
        exportAMaintainer = new ExportAMaintainer();
    }

    /**
     * Check if the hearing has been marked as ended. Returns true if the
     * hearing has ended. This should be used when the hearing object has not
     * been looked up. This method will look up the entity of the hearing and
     * validate the date.
     * 
     * @param hearingID
     *            Integer
     * @return Boolean
     * @throws HearingRecordException
     */
    public Boolean hasHearingEnded(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.hasHearingEnded(Integer hearingID) called");
        Boolean isHearingEnded = null;

        // get the hearing for the passed in hearingID
        HearingBasicValue hearing = getHearing(hearingID);

        // Check the hearing end date - if it is null then hearing is not ended.
        if (hearing.getHearingEndDate() != null) {
            isHearingEnded = new Boolean(true);
        } else {
            isHearingEnded = new Boolean(false);
        }

        log.debug("HearingRecordStatusHelper.hasHearingEnded ok, returns isHearingEnded: " + isHearingEnded.toString());

        return isHearingEnded;
    }

    /**
     * Check if the hearing has been marked as ended. Returnns true if the
     * hearing has ended. This should be used when the hearing object is known
     * and a basic hearing value can be passed in.
     * 
     * @param hearingBasicValue
     *            HearingBasicValue
     * @return Boolean
     * @throws HearingRecordException
     */
    public Boolean hasHearingEnded(HearingBasicValue hearingBasicValue) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.hasHearingEnded(HearingBasicValue " + "hearingBasicValue) called");
        Boolean isHearingEnded = null;

        // Check the hearing end date - if it is null then hearing is not ended.
        if (hearingBasicValue.getHearingEndDate() != null) {
            isHearingEnded = new Boolean(true);
        } else {
            isHearingEnded = new Boolean(false);
        }

        log.debug("HearingRecordStatusHelper.hasHearingEnded(HearingBasicValue hearingBasicValue) "
                + "ok, returns isHearingEnded: " + isHearingEnded.toString());

        return isHearingEnded;
    }

    /**
     * Check if hearing(s) has been exported. Returns true if hearing has been
     * exported.
     * 
     * @param hearingID
     *            Integer
     * @return Boolean
     * @throws HearingRecordException
     */
    public Boolean isExported(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.isExported(Integer hearingID) called");

        Boolean isExported = null;
        ExportAValue exportAValue = null;
        ArrayList exportAValues = null;

        // get the hearing for the passed in hearingID
        HearingBasicValue hearing = getHearing(hearingID);

        // Get the exportAValues for passed in hearingValue.
        exportAValues = this.getAllExportAValues(hearing);

        // get the right exportAValue since the hearing might have been exported
        // when not linked.
        exportAValue = this.getSingleExportAValue(exportAValues, hearingID);

        // check the flag and set the boolean to true if it has been exported
        // else false.
        if (exportAValue != null && exportAValue.getStatusFlag() != null) {
            log.debug("The status flag is : " + exportAValue.getStatusFlag());

            if (exportAValue.getStatusFlag().equalsIgnoreCase(HearingRecordConstants.EXPORT_SUCCESS)) {
                log.debug("The hearing has been exported");
                isExported = new Boolean(true);
            }
        } else {
            log.debug("The hearing has not been exported");
            isExported = new Boolean(false);
        }

        log.debug("HearingRecordStatusHelper.isExported(Integer hearingID) ok");

        return isExported;
    }

    /**
     * This method will return the status of the hearing, e.g. if the hearing
     * has been exported, in progress, etc. It will search for the hearing
     * passed in as argument but also check if the hearing is linked and search
     * for linked hearings.
     * 
     * @param hearingID
     *            Integer
     * @return String - the status of the Export
     * @throws HearingRecordException
     */
    public String getExportState(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.getExportState(Integer hearingID) called");

        ArrayList exportAValues = null;
        ExportAValue exportAValue = null;

        // get the hearing for the passed in hearingID
        HearingBasicValue hearing = getHearing(hearingID);

        // find the exportAs entry for the hearing (potentially more than one
        // exportA if hearing
        // has been exported before got linked)
        exportAValues = this.getAllExportAValues(hearing);

        // find the right exportAValue.
        exportAValue = this.getSingleExportAValue(exportAValues, hearingID);

        log.debug("HearingRecordStatusHelper.getExportState(Integer hearingID) ok");

        if (exportAValue != null && exportAValue.getStatusFlag() != null)
            return exportAValue.getStatusFlag();
        else
            return null;
    }

    /**
     * This will return the value Object for ExportA. This has the same
     * representation as in the database. It will search for exportA's for the
     * passed in hearing. It will also search for exportA's when the hearing has
     * been linked with other hearings (linkedHearingID)
     * 
     * @param hearingValue
     *            HearingBasicValue
     * @return ExportAValue
     * @throws HearingRecordException
     */
    public ExportAValue getExportValue(HearingBasicValue hearingValue) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.getExportValue(HearingBasicValue hearingValue) called");

        ArrayList exportAValues = null;
        ExportAValue exportAValue = null;

        // find the exportAs entry for the hearing id
        exportAValues = this.getAllExportAValues(hearingValue);

        // get the right exportAvalue
        exportAValue = this.getSingleExportAValue(exportAValues, hearingValue.getId());

        log.debug("HearingRecordStatusHelper.getExportValue(HearingBasicValue hearingValue) ok");

        return exportAValue;
    }

    /**
     * This method will call the getExportValue(HearingBasicValue hearingValue)
     * after it has looked up the HearingBasicValue. At times only the hearingID
     * is known and at times the hearingValue is already known and it is
     * therefore not necessary to look it up again.
     * 
     * @param hearingID
     *            Integer
     * @return ExportAValue
     * @throws HearingRecordException
     */
    public ExportAValue getExportValue(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.getExportValue(Integer hearingID) called");

        // get the hearing for the passed in hearingID
        HearingBasicValue hearing = getHearing(hearingID);

        // call the getExportValue(HearingBasicValue) since we only want to
        // look up the value ones.
        ExportAValue exportAValue = this.getExportValue(hearing);

        log.debug("HearingRecordStatusHelper.getExportValue(Integer hearingID) OK");

        return exportAValue;
    }

    // -------------------------------- private methods
    // -------------------------------------------

    /**
     * This will look up all related exportaValues for this a hearing value. It
     * will take the hearing id and the linked hearing id from the hearing value
     * and see if the hearing has been exported either as a single hearing or as
     * a linked hearing.
     * 
     * @param hearingValue
     *            HearingBasicValue
     * @return ArrayList of ExportAValues
     * @throws HearingRecordException
     */
    private ArrayList getAllExportAValues(HearingBasicValue hearingValue) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.getExportAValues(HearingBasicValue hearingValue) called");

        ArrayList exportAValues = null;

        try {
            // find all the exportaValues for the specified hearingID and
            // related linkedHearingID
            exportAValues = (ArrayList) exportAMaintainer.findByHearingOrLinkedHearingID(hearingValue.getId(),
                    hearingValue.getLinkedHearingID());
        } catch (ObjectNotFoundException ex) {
            // The ExportA could not be found
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
            throw hex;
        }

        log.debug("HearingRecordStatusHelper.getExportAValues(HearingBasicValue hearingValue) ok");

        return exportAValues;
    }

    /**
     * Get only the one ExportAValue that is in use.
     * 
     * @param exportAValues
     *            ArrayList
     * @param hearingID
     *            Integer
     * @return ExportAValue
     */
    private ExportAValue getSingleExportAValue(ArrayList exportAValues, Integer hearingID) {
        log.debug("HearingRecordStatusHelper.getExportAValue(ArrayList exportAValues, " + "Integer hearingID) called");

        ExportA exportAIterate = null;
        ExportAValue exportAValueReturn = null;

        // there is a potential that the hearing has been exported before
        // linked. This will
        // check for the hearingId first.
        for (int i = 0; i < exportAValues.size(); i++) {
            exportAIterate = (ExportA) exportAValues.get(i);

            // if the hearing has been exported before it got linked or if
            // it has never been linked
            // - check for the hearinId first.
            if (exportAIterate.getHearingId() != null
                    && exportAIterate.getHearingId().intValue() == hearingID.intValue()) {
                exportAValueReturn = exportAMaintainer.getExportAValue(exportAIterate);
            }
        }

        // If the hearingID was not recorded in an exportAValue we will return
        // the status of the last one
        // since this will be the status of the linked hearings.
        if (exportAValueReturn == null && exportAIterate != null) {
            exportAValueReturn = exportAMaintainer.getExportAValue(exportAIterate);
        }

        log.debug("HearingRecordStatusHelper.getExportAValue(ArrayList exportAValues, " + "Integer hearingID) Ok");

        return exportAValueReturn;
    }

    /**
     * The will find the hearing for passed in hearingID.
     * 
     * @param hearingID
     *            Integer
     * @return HearingBasicValue
     * @throws HearingRecordException
     */
    private HearingBasicValue getHearing(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordStatusHelper.getExportAValue(Integer hearingID) called");

        HearingBasicValue hearingValue = null;

        try {
            // use the maintainer to find by Primary key
            Hearing hearing = hearingMaintainer.findByPK(hearingID);
            hearingValue = hearingMaintainer.getHearingBasicValue(hearing);
        } catch (ObjectNotFoundException ex) {
            // The hearing could not be found
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
            throw hex;
        }

        log.debug("HearingRecordStatusHelper.getExportAValue(Integer hearingID) OK");

        return hearingValue;
    }

}