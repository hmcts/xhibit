package uk.gov.courtservice.xhibit.courtlog.services;

import java.util.Date;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogScheduledHearingValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: The Court Log Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides all business
 * services required for the Court Log.
 * </p>
 * 
 * @ejb.bean name="CourtLog2Controller" description="Courtlog Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="CourtLog2ControllerHome"
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * @author Meeraj Kunnumpurath
 */
public class CourtLog2ControllerBean extends CSSessionBean implements SessionBean {
    public void ejbCreate() {
        // required empty method stub...
    }

    /**
     * Adds several new court log entries
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtLogCRUDValue
     *            The CRUD values to create from
     * @return The created view values
     * @throws CourtLogBusinessException
     */
    public CourtLogViewValue[] newEntries(CourtLogCRUDValue[] courtLogCRUDValues) throws CourtLogBusinessException {
        try {
            final CourtLogViewValue[] createdEntries = new CourtLogViewValue[courtLogCRUDValues.length];

            for (int i = 0; i < courtLogCRUDValues.length; i++) {
                createdEntries[i] = CourtLogWorkFlow.newEntry(courtLogCRUDValues[i]);
            }

            return createdEntries;
        } catch (CourtLogBusinessException e) {
            ctx.setRollbackOnly();
            throw e;
        }
    }

    /**
     * Adds a new court log entry
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtLogCRUDValue
     *            The CRUD value
     * @return View value
     * @throws CourtLogBusinessException
     */
    public CourtLogViewValue newEntry(CourtLogCRUDValue courtLogCRUDValue) throws CourtLogBusinessException {
        try {
            return CourtLogWorkFlow.newEntry(courtLogCRUDValue);
        } catch (CourtLogBusinessException e) {
            ctx.setRollbackOnly();
            throw e;
        }
    }

    /**
     * Deletes the court log entry
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param logEntryId
     *            Log entry id
     * @param inCourt
     *            "In court" indicator
     * @throws CourtLogBusinessException
     */
    public void deleteEntry(Long logEntryId, boolean inCourt) throws CourtLogBusinessException {
        try {
            CourtLogWorkFlow.deleteEntry(logEntryId, inCourt);
        } catch (CourtLogBusinessException e) {
            ctx.setRollbackOnly();
            throw e;
        }
    }

    /**
     * Updates the court log entry
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtLogCRUDValue
     *            CRUD value
     * @return View value
     * @throws CourtLogBusinessException
     */
    public CourtLogViewValue updateEntry(CourtLogCRUDValue courtLogCRUDValue) throws CourtLogBusinessException {
        try {
            return CourtLogWorkFlow.updateEntry(courtLogCRUDValue);
        } catch (CourtLogBusinessException e) {
            ctx.setRollbackOnly();
            throw e;
        }
    }

    /**
     * Gets all the entries for the case
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param caseId
     *            Case id
     * @return Array of court log view values
     */
    public CourtLogViewValue[] getCourtLog(Integer caseId) {
        return CourtLogWorkFlow.getCourtLog(caseId);
    }

    /**
     * Gets court log for the case and date range
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param caseId
     *            Case id
     * @param fromDate
     *            Start date
     * @param toDate
     *            End date
     * @return Array of court log view values
     */
    public CourtLogViewValue[] getCourtLog(Integer caseId, Date fromDate, Date toDate) {
        return CourtLogWorkFlow.getCourtLog(caseId, fromDate, toDate);
    }

    /**
     * Gets an individual entry
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param logEntryId
     *            Log entry id
     * @return
     */
    public CourtLogCRUDValue getEntry(Long logEntryId) {
        return CourtLogWorkFlow.getEntry(logEntryId);
    }

    /**
     * Gets custom properties for the scheduled hearings associated with the
     * case passed in
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param caseId
     *            Case id
     * @return
     */
    public CourtLogScheduledHearingValue[] getCourtLogScheduledHearingValuesForCase(Integer caseId) {
        return CourtLogWorkFlow.getCourtLogScheduledHearingValuesForCase(caseId);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public Integer[] getEventTypesByCategoryDesc(String[] categoryDescs) {
        return CourtLogWorkFlow.getEventTypesByCategoryDesc(categoryDescs);
    }
}
