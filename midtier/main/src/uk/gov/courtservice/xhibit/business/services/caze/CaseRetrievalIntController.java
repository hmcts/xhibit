package uk.gov.courtservice.xhibit.business.services.caze;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.framework.services.conversion.DataTypeConverter;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge_differences.XhbChargeDifferences;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge_differences.XhbChargeDifferencesBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_time.XhbTime;
import uk.gov.courtservice.xhibit.business.entities.xhb_time.XhbTimeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_time.XhbTimeBeanHelper2;
import uk.gov.courtservice.xhibit.integration.vos.services.caseretrieval.CaseAccessValue;

/**
 * <p>
 * Title: CaseRetrievalIntController
 * </p>
 * <p>
 * Description: This class manages the opening, closing and leasing of cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class CaseRetrievalIntController {
    private static final String DISABLE_MERCATOR_USE = "disableMercatorUse";

    private static final Logger log = CSServices.getLogger(CaseRetrievalIntController.class);

    // error keys
    private static final String INVALID_STATUS = "integ.services.caseretrieval.invalidstatus";

    private static final String CASE_NOT_LOADED = "integ.services.caseretrieval.casenotloaded";

    private static final String CASE_LOADING = "integ.services.caseretrieval.caseloading";

    private static final String LOAD_FAILED = "integ.services.caseretrieval.loadfailed";

    private static final String CASE_LOCKED = "integ.services.caseretrieval.caselocked";

    private static final String CASE_REMOVED = "integ.services.caseretrieval.caseremoved";

    private static final String LOADING = "integ.services.caseretrieval.loading";

    private static final String REFRESH = "integ.services.caseretrieval.refresh";

    private static final String CLOSED = "integ.services.caseretrieval.closed";

    private static final String PARTIAL_LOAD = "integ.services.caseretrieval.partialload";

    private Long leaseTimeDuration;

    private Long refreshTimeDuration;

    private Long reportExpiryDuration;

    private HashMap validStateChanges = new HashMap();

    /**
     * Default constructor that will set the properties.
     */
    public CaseRetrievalIntController() {
        log.debug("entered CaseRetrievalIntController constructor");
        try {
            Properties prop = CSServices.getConfigServices().getProperties("integration.caseaccess");
            leaseTimeDuration = new Long(prop.getProperty("leaseTimeDuration"));
            refreshTimeDuration = new Long(prop.getProperty("refreshTimeDuration"));
            reportExpiryDuration = new Long(prop.getProperty("reportExpiryDuration"));
        } catch (Exception e) {
            CSServices.getDefaultErrorHandler().handleError(e, CaseRetrievalIntController.class);
            throw new CSConfigurationException("integration.caseaccess.properties file may not be set up correctly");
        }
        // hashmap lists valid state changes for an adminstrator actioned state
        // change.
        // key is old state array is valid new states
        validStateChanges.put(ChargeImportIndicator.LOAD_FAILED, new String[] { ChargeImportIndicator.NEW });
        validStateChanges.put(ChargeImportIndicator.CLOSED, new String[] { ChargeImportIndicator.LOCKED,
                ChargeImportIndicator.REMOVED });
        validStateChanges.put(ChargeImportIndicator.OPEN, new String[] { ChargeImportIndicator.CLOSED });
        validStateChanges.put(ChargeImportIndicator.PARTIALLY_LOADED, new String[] { ChargeImportIndicator.LOCKED,
                ChargeImportIndicator.REMOVED, ChargeImportIndicator.CLOSED });
        log.debug("exited CaseRetrievalIntController constructor");
    }

    /**
     * This method should be used the first time a user opens up a case.
     * 
     * Return: status and the new lease time in seconds in the CaseAccessVO
     * 
     * @param caseId
     *            Integer
     * @return CaseAccessValue
     * @throws CaseAccessException
     * @throws CaseRetrievalIntControllerException
     */
    public CaseAccessValue openCase(Integer caseId) throws CaseAccessException, CaseRetrievalIntControllerException {
        log.debug("openCase() started with caseId : " + caseId);

        // get case entity bean
        XhbCase caseEntity = XhbCaseBeanHelper2.findByPrimaryKey(caseId);

        if (isDisableMercatorUse()) {
            // set the chargeImportIndicator to 'O' as mercator is not
            // around to
            // do this
            setCaseStatusToOpen(caseEntity);
        }

        String status = caseEntity.getChargeImportIndicator();
        CaseAccessValue caseAccessValue = null;

        // throw corresponding exception if status is:
        // NEW, PENDING, LOAD_FAILED, LOCKED, REMOVED, PARTIALLY_LOADED
        if (status.equals(ChargeImportIndicator.NEW)) {
            log.debug("if (status.equals(ChargeImportIndicator.NEW))");
            throw new CaseNotLoadedException(CASE_NOT_LOADED, "The Case has not been loaded into XHIBIT.");
        }
        if (status.equals(ChargeImportIndicator.PENDING)) {
            log.debug("if (status.equals(ChargeImportIndicator.PENDING))");
            throw new CaseLoadingException(CASE_LOADING, "The Case is currently being loaded");
        }
        if (status.equals(ChargeImportIndicator.LOAD_FAILED)) {
            log.debug("if (status.equals(ChargeImportIndicator.LOAD_FAILED))");
            throw new CaseLoadFailedException(LOAD_FAILED, "The Case has failed to load in XHIBIT");
        }
        if (status.equals(ChargeImportIndicator.LOCKED)) {
            log.debug("if (status.equals(ChargeImportIndicator.LOCKED))");
            throw new CaseLockedException(CASE_LOCKED, "The Case has been locked");
        }
        if (status.equals(ChargeImportIndicator.REMOVED)) {
            log.debug("if (status.equals(ChargeImportIndicator.REMOVED))");
            throw new CaseRemovedException(CASE_REMOVED, "The Case has been removed");
        }

        // As of CR 58 Uncoded Offences - ChargeImportIndicator.PARTIALLY_LOADED
        // (PL)
        // will never be set any more by Mercator. Some cases may already be set
        // to PL before CR 58 goes live, so if we get PL - resync.
        if (status.equals(ChargeImportIndicator.PARTIALLY_LOADED)) {
            log.debug("if (status.equals(ChargeImportIndicator.PARTIALLY_LOADED))");
            throw new CaseLoadingException(LOADING, "The Case is currently being loaded");
        }

        // if open check how long it's been open
        if (status.equals(ChargeImportIndicator.OPEN)) {
            Calendar leaseTime = getLeaseTime(caseEntity);

            if (leaseTime != null) {
                leaseTime.add(Calendar.SECOND, refreshTimeDuration.intValue());
            }

            Calendar newLeaseTime = Calendar.getInstance();
            // if not expired refresh leaseTime in table and return new
            // caseAccessValue
            // WITHOUT report. Otherwise set ChargeImportIndicator to
            // SYNCHRONIZING
            if (newLeaseTime.before(leaseTime)) {
                log.debug("setLeaseTime");
                setLeaseTime(newLeaseTime, caseEntity);
                caseAccessValue = new CaseAccessValue(caseId, status, leaseTimeDuration, newLeaseTime, null);
            }
        }
        return caseAccessValue;
    }

    /**
     * -Check the time difference between the new lease time and the existing
     * one in the database. -Update the case with the new lease time - Return
     * the chargeImportIndicator (status) and new lease time in seconds
     * 
     * @param caseId
     *            Integer
     * @return CaseAccessValue
     * @throws CaseAccessException
     * @throws CaseRetrievalIntControllerException
     */
    public CaseAccessValue refreshLeaseTime(Integer caseId) throws CaseAccessException,
            CaseRetrievalIntControllerException {
        log.debug("CaseAccessValue refreshLeaseTime called. caseId = " + caseId);

        // get case entity bean
        XhbCase caseEntity = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
        String status = caseEntity.getChargeImportIndicator();

        // throw corresponding exception is status is:
        // NEW, PENDING, CLOSED, LOAD_FAILED, RESYNCH_FAILED, LOCKED, REMOVED,
        // PARTIALLY_LOADED
        if (status.equals(ChargeImportIndicator.NEW)) {
            throw new CaseNotLoadedException(CASE_NOT_LOADED, "The Case has not been loaded into XHIBIT.");
        }
        if (status.equals(ChargeImportIndicator.PENDING)) {
            throw new CaseLoadingException(REFRESH, "The Case is currently being refreshed");
        }
        if (status.equals(ChargeImportIndicator.CLOSED)) {
            throw new CaseClosedException(CLOSED, "The Case has been closed or needs refreshing");
        }
        if (status.equals(ChargeImportIndicator.LOAD_FAILED)) {
            throw new CaseLoadFailedException(LOAD_FAILED, "The Case has failed to load in XHIBIT");
        }
        if (status.equals(ChargeImportIndicator.LOCKED)) {
            throw new CaseLockedException(CASE_LOCKED, "The Case has been locked");
        }
        if (status.equals(ChargeImportIndicator.REMOVED)) {
            throw new CaseRemovedException(CASE_REMOVED, "The Case has been removed");
        }
        if (status.equals(ChargeImportIndicator.PARTIALLY_LOADED)) {
            log.debug("if (status.equals(ChargeImportIndicator.PARTIALLY_LOADED))");
            throw new CasePartiallyLoadedException(PARTIAL_LOAD, "The Case is partially loaded");
        }

        // if open check how long it's been open
        if (status.equals(ChargeImportIndicator.OPEN)) {
            Calendar leaseTime = getLeaseTime(caseEntity);

            if (leaseTime != null) {
                leaseTime = Calendar.getInstance();
                leaseTime.add(Calendar.SECOND, refreshTimeDuration.intValue());
            }

            Calendar newLeaseTime = Calendar.getInstance();
            log.debug("refreshLeaseTime. Calendar.getInstance() ");
            // if expired throw CaseClosedException
            if (newLeaseTime.after(leaseTime)) {
                throw new CaseClosedException(CLOSED, "The Case has been closed or needs refreshing");
            }
            // else return new caseAccessValue WITHOUT report.
            setLeaseTime(newLeaseTime, caseEntity);

            CaseAccessValue caseAccessValue = new CaseAccessValue(caseId, status, leaseTimeDuration, newLeaseTime, null);
            return caseAccessValue;
        }
        // If the synch failed we need to return a CaseAccessValue with the full
        // report.
        if (status.equals(ChargeImportIndicator.RESYNCH_FAILED)) {
            CaseAccessValue caseAccessValue = new CaseAccessValue(caseId, status, null, null, getReport(caseId));
            //Raise Audit
            Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
            hashtable.put(CaseAccessValue.class.getName(), caseAccessValue);
            AuditTrailService auditService = CSServices.getAuditTrailService();
            AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
            event.setSuccess(true);
            auditService.createAuditRecord(event);
            return caseAccessValue;
        } else {
            log.debug("caseAccessValue. caseID=" + caseId + " , status = " + status + " , leaseTimeDuration = "
                    + leaseTimeDuration);
            throw new CaseRetrievalIntControllerException(INVALID_STATUS, "Invalid Case Status");
        }

    }

    /**
     * Closes the case with the given case id.
     * 
     * @param caseId
     *            the id of the case
     * @throws CaseAccessException
     * @throws CaseRetrievalIntControllerException
     */
    private void closeCase(Integer caseId) throws CaseAccessException, CaseRetrievalIntControllerException {
        // get case entity bean
        XhbCase caseEntity = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
        String status = caseEntity.getChargeImportIndicator();

        log.debug("closeCase called. caseID = " + caseId);
        // throw corresponding exception is status is:
        // NEW, PENDING, LOAD_FAILED, LOCKED, REMOVED, RESYNCH_FAILED,
        // PARTIALLY_LOADED
        if (status.equals(ChargeImportIndicator.NEW)) {
            throw new CaseNotLoadedException(CASE_NOT_LOADED, "The Case has not been loaded into XHIBIT.");
        }
        if (status.equals(ChargeImportIndicator.PENDING)) {
            throw new CaseLoadingException(REFRESH, "The Case is currently being refreshed");
        }
        if (status.equals(ChargeImportIndicator.LOAD_FAILED)) {
            throw new CaseLoadFailedException(LOAD_FAILED, "The Case has failed to load in XHIBIT");
        }
        if (status.equals(ChargeImportIndicator.LOCKED)) {
            throw new CaseLockedException(CASE_LOCKED, "The Case has been locked");
        }
        if (status.equals(ChargeImportIndicator.REMOVED)) {
            throw new CaseRemovedException(CASE_REMOVED, "The Case has been removed");
        }
        // if open, close
        if (status.equals(ChargeImportIndicator.OPEN)) {
            caseEntity.setChargeImportIndicator(ChargeImportIndicator.CLOSED);
        }
        if (status.equals(ChargeImportIndicator.PARTIALLY_LOADED)) {
            log.debug("if (status.equals(ChargeImportIndicator.PARTIALLY_LOADED))");
            throw new CasePartiallyLoadedException(PARTIAL_LOAD, "The Case is partially loaded");
        }
    }

    public void forceCaseResynch(Integer[] caseIds) throws CaseAccessException, CaseRetrievalIntControllerException {
        // set all cases to closed
        if (caseIds == null) {
            throw new IllegalArgumentException("caseIds: null");
        }
        for (int i = 0, len = caseIds.length; i < len; i++) {
            closeCase(caseIds[i]);
        }
    }

    /**
     * get report field from ChargeDifferencesHome entity for this case, return
     * null if not found
     * 
     * @param caseId
     *            Integer
     * @return the report String#
     * @throws CSUnrecoverableException
     */
    private String getReport(Integer caseId) throws CSUnrecoverableException {
        log.debug("getReport called. caseId = " + caseId);

        Calendar expiryTime = Calendar.getInstance();
        expiryTime.add(Calendar.SECOND, -reportExpiryDuration.intValue());
        log.debug("expiryTime : " + expiryTime);

        // find last entry that is no more than reportExpiryDuration (30 mins)
        // old
        Collection cDiffCol = XhbChargeDifferencesBeanHelper2.findByCaseIdAndDiffTime(caseId, new Timestamp(expiryTime
                .getTime().getTime()));

        if (cDiffCol.isEmpty()) {
            log.debug("Did not find any chargeDifferences.");
            return null;
        }

        XhbChargeDifferences chargeDifferences = null;
        XhbChargeDifferences cDTemp = null;
        Iterator it = cDiffCol.iterator();

        if (log.isDebugEnabled()) {
            log.debug("Number of diffs back : " + cDiffCol.size());
        }

        // Get the first one
        chargeDifferences = (XhbChargeDifferences) it.next();
        if (log.isDebugEnabled()) {
            log.debug("chargeDifferences : " + chargeDifferences.getCaseId());
            log.debug("chargeDifferences : " + chargeDifferences.getChargeDiffId());
            log.debug("chargeDifferences : " + chargeDifferences.getCourtId());
            log.debug("chargeDifferences : " + chargeDifferences.getDiffTime());
            log.debug("chargeDifferences : " + chargeDifferences.getReport());
        }

        // return only the last entry
        while (it.hasNext()) {
            cDTemp = (XhbChargeDifferences) it.next();
            if (chargeDifferences.getChargeDiffId().compareTo(cDTemp.getChargeDiffId()) < 0) {
                chargeDifferences = cDTemp;
            }
        }

        log.debug("Will return report : " + chargeDifferences.getReport());
        return chargeDifferences.getReport(); // delete line to activate xml
        // code
    }

    /**
     * Get the lease time
     * 
     * @param caseEntity
     *            Case
     * @return Calendar
     */
    private Calendar getLeaseTime(XhbCase caseEntity) {
        XhbTime clt = caseEntity.getXhbTime();
        if (clt != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(clt.getLeaseTime());
            return c;
        }
        return null;
    }

    /**
     * Set the lease time
     * 
     * @param leaseTime
     *            Calendar
     * @param caseEntity
     *            Case
     */
    private void setLeaseTime(Calendar leaseTime, XhbCase caseEntity) {
        XhbTime clt = caseEntity.getXhbTime();
        if (clt == null) {
            XhbTimeBasicValue cltBV = new XhbTimeBasicValue();
            cltBV.setCaseId(caseEntity.getCaseId());
            cltBV.setLeaseTime(leaseTime.getTime());
            XhbTimeBeanHelper2.create(cltBV, caseEntity);
        } else {
            XhbTimeBasicValue cltBV = clt.getData();
            cltBV.setLeaseTime(DataTypeConverter.convertToTimestamp(leaseTime));
            XhbTimeBeanHelper2.update(cltBV);
        }
    }

    /**
     * Return true if the mercator interaction has been disabled by system
     * property
     * CTX-2391: change this method to always return true.
     */
    private boolean isDisableMercatorUse() {
        return true;
    }

    /**
     * Used if mercator use is disabled to fake case synchronization having
     * occurred.
     * 
     * @param caseEntity
     */
    private void setCaseStatusToOpen(XhbCase caseEntity) {
        // can't set charge import indicator via the maintainer so setting via
        // the entity directly
        caseEntity.setChargeImportIndicator(ChargeImportIndicator.OPEN);
        setLeaseTime(Calendar.getInstance(), caseEntity);
    }

}