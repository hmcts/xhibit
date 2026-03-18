package uk.gov.courtservice.xhibit.business.services.witness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitness;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessValue;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessSessionImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers.WitnessSummaryHelper;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;

/**
 * <p>
 * Title: The Witness Selector Session EJB.
 * </p>
 * <p>
 * Description: This EJB is used to fetch Witness Summaries for different
 * criteria.
 * </p>
 * 
 * @ejb.bean name="WitnessSelector" description="Witness Selector Bean"
 *           type="Stateless" view-type="remote" jndi-name="WitnessSelectorHome"
 * 
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * @author Neil Ellis
 */
public class WitnessSelectorBean extends CSSessionBean implements SessionBean {
    private final WitnessSummaryHelper witnessSummaryHelper = new WitnessSummaryHelper();

    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @ejb.create-method
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * @ejb.interface-method view-type="remote"
     * 
     * @param caseId
     * @return
     */
    public WitnessSession[] getTodaysWitnesses(final Integer caseId) throws NoScheduleForCaseException {
        final Calendar firstThing = WitnessSummaryHelper.getBeginningOfToday();
        return witnessSummaryHelper.getWitnessesForTimeRange(caseId, firstThing, WitnessSummaryHelper.getEndOfToday());
    }

    /**
     * @ejb.interface-method view-type="remote"
     * 
     * @param caseId
     * @return
     */
    public WitnessSession[] getFutureWitnesses(final Integer caseId) throws NoScheduleForCaseException {
        final Calendar lastThing = WitnessSummaryHelper.getEndOfToday();
        return witnessSummaryHelper.getWitnessesForTimeRange(caseId, lastThing, null);
    }

    /**
     * Selects all currently signed in witnesses for a particular court and
     * case.
     * 
     * @param caseId
     * @param courtId
     * 
     * @return an array of WitnessSummary compliant objects.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public WitnessSession[] getSignedInWitnesses(final Integer caseId, final Integer courtId)

    {
        try {
            // Will get all witnesses for the court/case where
            // actual_arrival_time is not null and release_time is null.
            final Collection witnesses = XhbWitnessHelper.getLocalHome().findSignedInWitnesses(caseId, courtId);

            final Collection results = new ArrayList();
            for (Iterator iterator = witnesses.iterator(); iterator.hasNext();) {
                final XhbWitnessValue xhbWitness = ((XhbWitness) iterator.next()).getData();
                results.add(new WitnessSessionImpl(xhbWitness));
            }

            return (WitnessSession[]) results.toArray(new WitnessSession[results.size()]);
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * Return all witnesses for a given case with a date before today.
     * 
     * @param caseId
     * @return
     * @throws NoScheduleForCaseException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public WitnessSession[] getPastWitnesses(final Integer caseId) throws NoScheduleForCaseException {
        return witnessSummaryHelper.getWitnessesForTimeRange(caseId, null, WitnessSummaryHelper.getBeginningOfToday());
    }

    /**
     * Return all witnesses for a given case.
     * 
     * @param caseId
     * @return
     * @throws NoScheduleForCaseException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public WitnessSession[] getAllWitnesses(final Integer caseId) throws NoScheduleForCaseException {
        return witnessSummaryHelper.getWitnessesForTimeRange(caseId, null, null);
    }

    /**
     * Return all witnesses for a given case with todays date, a future or null
     * date.
     * 
     * @param caseId
     * @return
     * @throws NoScheduleForCaseException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public WitnessSession[] getTodayAndFutureWitnesses(final Integer caseId) throws NoScheduleForCaseException {
        return witnessSummaryHelper.getWitnessesForTimeRange(caseId, WitnessSummaryHelper.getBeginningOfToday(), null);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public WitnessSession[] getWitnessesForWeek(final Integer caseId, final int weekNumber) {
        final Collection witnesses = getWitnessesCollectionForWeek(caseId, weekNumber);
        final ArrayList result = new ArrayList();
        for (Iterator iterator = witnesses.iterator(); iterator.hasNext();) {
            final XhbWitness xhbWitness = (XhbWitness) iterator.next();
            result.add(new WitnessSessionImpl(xhbWitness.getData(true)));
            log.debug("Retrieved witness: " + xhbWitness);

        }
        return (WitnessSession[]) result.toArray(new WitnessSession[result.size()]);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public boolean areWitnessesInWeek(final Integer caseId, final int weekNumber) {
        return (getWitnessesCollectionForWeek(caseId, weekNumber).size() > 0);
    }

    private Collection getWitnessesCollectionForWeek(final Integer caseId, final int weekNumber) {
        try {
            return XhbWitnessHelper.getLocalHome().findByCaseAndWeek(caseId, weekNumber);
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public boolean areWitnessesOnCase(final Integer caseId) {
        return (getWitnessesCollectionForCase(caseId).size() > 0);
    }

    private Collection getWitnessesCollectionForCase(final Integer caseId) {
        try {
            return XhbWitnessHelper.getLocalHome().findByCaseId(caseId);
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public WitnessSession[] getWitnessesForDay(final Integer caseId, final int dayNumber) {
        final Collection witnesses = getWitnessesCollectionForDay(caseId, dayNumber);
        final ArrayList result = new ArrayList();
        for (Iterator iterator = witnesses.iterator(); iterator.hasNext();) {
            final XhbWitness xhbWitness = (XhbWitness) iterator.next();
            result.add(new WitnessSessionImpl(xhbWitness.getData(true)));
            log.debug("Retrieved witness: " + xhbWitness);

        }
        return (WitnessSession[]) result.toArray(new WitnessSession[result.size()]);
    }

    private Collection getWitnessesCollectionForDay(final Integer caseId, final int dayNumber) {
        try {
            return XhbWitnessHelper.getLocalHome().findByCaseAndDay(caseId, dayNumber);
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }
}