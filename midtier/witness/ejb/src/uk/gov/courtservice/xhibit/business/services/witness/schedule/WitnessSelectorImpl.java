package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import uk.gov.courtservice.xhibit.business.services.witness.WitnessSelectorBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetailSelector;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSessionSelector;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummarySelector;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * <b>This is not thread safe, under the covers it uses unsynchronized lazy
 * instantiation. You have been warned :-) </b>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.13 $
 */
public class WitnessSelectorImpl implements WitnessDetailSelector, WitnessSessionSelector, WitnessSummarySelector {
    private transient WitnessSelectorBeanBusinessDelegate selectorBeanBusinessDelegate;

    public WitnessSelectorBeanBusinessDelegate getSelectorBeanBusinessDelegate() {
        if (selectorBeanBusinessDelegate == null) {
            selectorBeanBusinessDelegate = WitnessSelectorBeanBusinessDelegate.DelegateFactory.getInstance();
        }

        return selectorBeanBusinessDelegate;
    }

    public WitnessDetail[] getAllWitnessDetails(final Integer caseId) throws CaseNotFoundException,
            NoScheduleForCaseException {
        return getSelectorBeanBusinessDelegate().getAllWitnesses(caseId);
    }

    public WitnessSummary[] getAllWitnesses(final Integer caseId) throws CaseNotFoundException,
            NoScheduleForCaseException {
        return getSelectorBeanBusinessDelegate().getAllWitnesses(caseId);
    }

    public WitnessSummary[] getFutureWitnesses(final Integer caseId) throws CaseNotFoundException,
            NoScheduleForCaseException {
        return getSelectorBeanBusinessDelegate().getFutureWitnesses(caseId);
    }

    public WitnessSummary[] getPastWitnesses(final Integer caseId) throws CaseNotFoundException,
            NoScheduleForCaseException {
        return getSelectorBeanBusinessDelegate().getPastWitnesses(caseId);
    }

    public WitnessSummary[] getSignedInWitnesses(final Integer caseId, final Integer courtId) {
        return getSelectorBeanBusinessDelegate().getSignedInWitnesses(caseId, courtId);
    }

    public WitnessSummary[] getTodayAndFutureWitnesses(final Integer caseId) throws CaseNotFoundException,
            NoScheduleForCaseException {
        return getSelectorBeanBusinessDelegate().getTodayAndFutureWitnesses(caseId);
    }

    public WitnessSummary[] getTodaysWitnesses(final Integer caseId) throws CaseNotFoundException,
            NoScheduleForCaseException {
        return getSelectorBeanBusinessDelegate().getTodaysWitnesses(caseId);
    }

    public boolean areWitnessesInWeek(final Integer caseId, final int weekNumber) {
        return getSelectorBeanBusinessDelegate().areWitnessesInWeek(caseId, weekNumber);
    }

    public boolean areWitnessesOnCase(final Integer caseId) {
        return getSelectorBeanBusinessDelegate().areWitnessesOnCase(caseId);
    }

    public WitnessSession[] getWitnessesForWeek(final Integer caseId, final int weekNumber) {
        return getSelectorBeanBusinessDelegate().getWitnessesForWeek(caseId, weekNumber);

    }

    public WitnessSession[] getWitnessesForDay(final Integer caseId, final int dayNumber) {
        return getSelectorBeanBusinessDelegate().getWitnessesForDay(caseId, dayNumber);
    }

    public WitnessSession[] getTodayWitnesses(Integer caseId) {
        try {
            return getSelectorBeanBusinessDelegate().getTodaysWitnesses(caseId);
        } catch (NoScheduleForCaseException e) {
            return null;
        }
    }

    public WitnessSession[] getAllWitnessesForSession(Integer caseId) {
        try {
            return getSelectorBeanBusinessDelegate().getAllWitnesses(caseId);
        } catch (NoScheduleForCaseException e) {
            return null;
        }
    }

    public WitnessSession[] getAllFutureWitnesses(Integer caseId) {
        try {
            return getSelectorBeanBusinessDelegate().getFutureWitnesses(caseId);
        } catch (NoScheduleForCaseException e) {
            return null;
        }
    }

    public WitnessSession[] getAllTodayAndFutureWitnesses(Integer caseId) {
        try {
            return getSelectorBeanBusinessDelegate().getTodayAndFutureWitnesses(caseId);
        } catch (NoScheduleForCaseException e) {
            return null;
        }
    }
}
