package uk.gov.courtservice.xhibit.common.progress.caseupdate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_progress_trigger.XhbProgressTriggerBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_progress_trigger.XhbProgressTriggerBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_progress_trigger.XhbProgressTriggerBeanNotFoundException;

/**
 * <p>
 * Title: ProgressTriggerMaintainer
 * </p>
 * <p>
 * Description: RFC 1492: Class to add/update records in XHB_PROGRESS_TRIGGER on
 * changes to Pleas/Verdicts/Disposals/Add Hearing
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version $Revisison$
 */
public class ProgressTriggerMaintainer {
    /**
     * The property file for this class
     */
    private static final String PROGRESS_PROPERTIES = "progress.trigger";

    /**
     * Property representing a status of NEW
     */
    private static final String NEW_STATUS = "progress.trigger.new.status";

    /**
     * Property representing a Yes flag
     */
    private static final String YES_FLAG = "progress.trigger.yes";

    /**
     * Property representing a NO flag
     */
    private static final String NO_FLAG = "progress.trigger.no";

    /**
     * Properties for this class
     */
    private static final Properties _progressConfig = CSServices.getConfigServices().getProperties(PROGRESS_PROPERTIES);

    /**
     * YES flag
     */
    private static String _yFlag = _progressConfig.getProperty(YES_FLAG);

    /**
     * NO flag
     */
    private static String _nFlag = _progressConfig.getProperty(NO_FLAG);

    /*
     * Logger for this class
     */
    private static final Logger _log = CSServices.getLogger(ProgressTriggerMaintainer.class);

    /*
     * Instance of this class
     */
    private static ProgressTriggerMaintainer _maintainer = new ProgressTriggerMaintainer();

    private ProgressTriggerMaintainer() {
        // Prevent instantiation other than by this class
    }

    /**
     * Update the Progress Trigger table
     * 
     * @param caseId
     *            the id of the case that has been updated
     * @param newHearing
     *            indicator to show if a new hearing
     */
    public static void updateCaseTrigger(Integer caseId, String newHearing) {
        updateCaseTrigger(new Integer[] { caseId }, newHearing);
    }

    /**
     * Update the Progress Trigger table
     * 
     * @param caseIds
     *            the id of the case that has been updated
     */
    public static void updateCaseTrigger(Integer[] caseIds) {
        // If no new hearing flag passed, set to "N"
        updateCaseTrigger(caseIds, getNoFlag());
    }

    /**
     * Update the Progress Trigger table
     * 
     * @param caseIds
     *            an array of the ids of the case that has been updated
     * @param newHearing
     *            indicator to show if a new hearing
     */
    public static void updateCaseTrigger(Integer[] caseIds, String newHearing) {
        updateCaseTrigger(caseIds, Calendar.getInstance().getTime(), newHearing);
    }

    /**
     * Update the Progress Trigger table
     * 
     * @param caseIds
     *            an array of the ids of the case that has been updated
     * @param date
     *            the date/time of the update
     * @param newHearing
     *            indicator to show if a new hearing
     */
    public static void updateCaseTrigger(Integer[] caseIds, Date date, String newHearing) {
        for (Integer i : caseIds){
            // If date is null, get the current date
            _maintainer.setupTrigger(i, ((null == date) ? Calendar.getInstance().getTime() : date),
                    newHearing);
        }
    }

    /**
     * Set up the trigger record
     * 
     * @param caseId
     *            The case to be updated
     * @param date
     *            The date of the update
     * @param newHearing
     *            "Y" if the case has a new hearing
     */
    private void setupTrigger(Integer caseId, Date date, String newHearing) {
        if (_log.isDebugEnabled()) {
            _log.debug("Case ID: " + caseId);
            _log.debug("Date: " + date.toString());
            _log.debug("New Hearing: " + newHearing);
        }
        try {
            createOrUpdateTrigger(caseId, date, newHearing);
        } catch (EJBException e) {
            logError(caseId, date, newHearing, e);
        }
    }

    /**
     * Update an existing trigger record or create a new one
     * 
     * @param caseId
     *            The case to be updated
     * @param date
     *            The date of the update
     * @param newHearing
     *            "Y" if the case has a new hearing
     */
    private void createOrUpdateTrigger(Integer caseId, Date date, String newHearing) {
        try {
            updateTrigger(XhbProgressTriggerBeanHelper2.findByCaseIdValue(caseId), date, newHearing);
        } catch (XhbProgressTriggerBeanNotFoundException e) {
            // If there is no existing record for th ecaseid, create a new one
            if (_log.isDebugEnabled()) {
                _log.debug("No existing record for caseid " + caseId + ". Create a new one.");
            }
            createTrigger(caseId, date, newHearing);
        }
    }

    /**
     * Create a new progress trigger record
     * 
     * @param caseId
     *            The case to be updated
     * @param date
     *            The date of the update
     * @param newHearing
     *            "Y" if the case has a new hearing
     */
    private void createTrigger(Integer caseId, Date date, String newHearing) {
        XhbProgressTriggerBasicValue triggerVal = new XhbProgressTriggerBasicValue();
        triggerVal.setCaseId(caseId);
        triggerVal.setStatusId(getNewStatusId());
        triggerVal.setUpdatedTime(date);
        triggerVal.setNewHearingFlag(newHearing);
        try {
            XhbProgressTriggerBeanHelper2.createLocal(triggerVal);
        } catch (Exception e) {
            // Not ideal, but we don't want anything to be impacted
            // if this goes wrong
            // If this goes wrong we just log it and carry on
            // as any changes will be caught by refresh/resync
            logError(caseId, date, newHearing, e);
        }
    }

    /**
     * Update an existing progress trigger record
     * 
     * @param progressTrigger
     *            the existing progress trigger basic value
     * @param caseId
     *            The case to be updated
     * @param date
     *            The date of the update
     * @param newHearing
     *            "Y" if the case has a new hearing
     */
    private void updateTrigger(XhbProgressTriggerBasicValue progressTrigger, Date date, String newHearing) {
        progressTrigger.setStatusId(getNewStatusId());
        progressTrigger.setUpdatedTime(date);
        // If not a new hearing, don't update the indicator
        // This should be reset by Mercator once processed
        if (progressTrigger.getNewHearingFlag().equalsIgnoreCase(getNoFlag())) {
            progressTrigger.setNewHearingFlag(newHearing);
        }
        try {
            XhbProgressTriggerBeanHelper2.updateLocal(progressTrigger);
        } catch (XhbProgressTriggerBeanNotFoundException e) {
            // Shouldn't get this failure as the record has been found
            // previously.
            // If record is not found, for any reason, log the error
            logError(progressTrigger.getCaseId(), date, newHearing, e);
        }
    }

    /**
     * Get the status id required for a new update
     * 
     * @return the new status id
     */
    private Integer getNewStatusId() {
        return new Integer(_progressConfig.getProperty(NEW_STATUS));
    }

    public static void logError(Integer caseId, Date date, String newHearing, Throwable e) {
        StringBuffer buf = new StringBuffer();
        buf.append("\nERROR UPDATING PROGRESS TRIGGER\n");
        buf.append("===== ======== ======== =======\n");
        buf.append("Case Id: " + caseId + "\n");
        buf.append("Update Time: " + date.toString() + "\n");
        buf.append("New Hearing: " + newHearing + "\n");
        buf.append("Exception Reason: " + e.getMessage());
        _log.error(buf.toString(), e);
    }

    /**
     * Get the value of the YES flag
     * 
     * @return the yes flag
     */
    public static String getYesFlag() {
        return _yFlag;
    }

    /**
     * Get the value of the NO flag
     * 
     * @return the no flag
     */
    public static String getNoFlag() {
        return _nFlag;
    }

}
