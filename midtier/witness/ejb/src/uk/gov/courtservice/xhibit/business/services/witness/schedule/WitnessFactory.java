package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessCreationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetailSelector;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSessionSelector;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummarySelector;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Use the WitnessFactory to obtain instances of WitnessDetail and
 * WitnessSummary.
 * </p>
 * 
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
 * @version $Revision: 1.10 $
 */
public class WitnessFactory {
    private static final WitnessFactory instance = new WitnessFactory();

    private transient WitnessControllerBeanBusinessDelegate delegate;

    public static WitnessFactory getInstance() {
        return instance;
    }

    private WitnessFactory() {
    }

    public WitnessControllerBeanBusinessDelegate getDelegate() {
        if (delegate == null) {
            delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        }
        return delegate;
    }

    /**
     * @pre caseId != null && sessionId != null && witnessFullname != null
     * @pre witnessType != null && age >= WitnessDetail.MIN_AGE && age <=
     *      WitnessDetail.MAX_AGE
     * @pre expectedArrivalTime != null && notes != null
     * @post return != null && return.getMetaState() == return.UPDATED
     * @post return.getId() != null
     * 
     * 
     * @param caseId
     *            the case for which they are a witness.
     * @param sessionId
     *            the prinary key of the skeleton sesion.
     * @param witnessFullname
     *            the name of the witness.
     * @param witnessType
     *            the type of witnesseg ie Defendant or Prosecution.
     * @param age
     *            the age of the witness
     * @param expectedArrivalTime
     *            the time at which they are expected to arrive, time not date.
     * @param notes
     * 
     * @return an object that implements WitnessDetail
     * 
     * @throws WitnessCreationException
     * @throws
     *             uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * @throws SkeletonSessionNotFoundException
     * 
     */
    public WitnessDetail createWitnessDetail(final Integer caseId, final Integer sessionId,
            final String witnessFullname, final String witnessType, final String witnessStatus, final int age,
            final java.sql.Time expectedArrivalTime, final String notes) throws WitnessCreationException,
            uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException,
            SkeletonSessionNotFoundException {
        return getDelegate().createWitnessSession(caseId, sessionId, witnessFullname, witnessType, witnessStatus, age,
                expectedArrivalTime, notes).getAssociatedWitnessDetail();
    }

    /**
     * @pre caseId != null && sessionId != null && witnessFullname != null
     * @pre witnessType != null && age >= WitnessDetail.MIN_AGE && age <=
     *      WitnessDetail.MAX_AGE
     * @pre expectedArrivalTime != null && notes != null
     * @post return != null && return.getMetaState() == return.UPDATED
     * @post return.getId() != null
     * 
     * 
     * @param caseId
     *            the case for which they are a witness.
     * @param sessionId
     *            the prinary key of the skeleton sesion.
     * @param witnessFullname
     *            the name of the witness.
     * @param witnessType
     *            the type of witnesseg ie Defendant or Prosecution.
     * @param age
     *            the age of the witness
     * @param expectedArrivalTime
     *            the time at which they are expected to arrive, time not date.
     * @param notes
     * 
     * @return an object that implements WitnessDetail
     * 
     * @throws WitnessCreationException
     * @throws
     *             uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * @throws SkeletonSessionNotFoundException
     * 
     */
    public WitnessSession createWitnessSession(final Integer caseId, final Integer sessionId,
            final String witnessFullname, final String witnessType, final String witnessStatus, final int age,
            final java.sql.Time expectedArrivalTime, final String notes) throws WitnessCreationException,
            uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException,
            SkeletonSessionNotFoundException {
        return getDelegate().createWitnessSession(caseId, sessionId, witnessFullname, witnessType, witnessStatus, age,
                expectedArrivalTime, notes);
    }

    /**
     * Obtains a WitnessSession instance for a specific witness ID. The
     * WitnessSession contains both TrialSession and WitnessDetail information.
     * 
     * @param witnessId
     *            a valid witness ID.
     * @return a valid WitnessSession.
     * @throws WitnessNotFoundException
     *             if there is no witness matching the ID. supplied.
     * @see WitnessSession
     * 
     * @pre witnessId != null
     * @post return != null && return.getMetaState() == return.UPDATED
     * 
     */
    public WitnessSession getWitnessSession(final Integer witnessId) throws WitnessNotFoundException {
        // todo: sort this out, we just need to add a getWitnessSession method
        // for example.
        return new WitnessSessionImpl(((WitnessDetailImpl) getDelegate().getWitnessDetail(witnessId)).getWitnessValue());
    }

    /**
     * 
     * Obtains a WitnessDetail instance for a specific witness ID.
     * 
     * @param witnessId
     * @return a valid WitnessDetail
     * @throws WitnessNotFoundException
     *             if there is no witness matching the ID.
     * 
     * @pre witnessId != null
     * @post return != null && return.getMetaState() == return.UPDATED
     */
    public WitnessDetail getWitnessDetail(final Integer witnessId) throws WitnessNotFoundException {
        return getDelegate().getWitnessDetail(witnessId);
    }

    /**
     * This returns a class that will allow queries to be performed returning
     * WitnessDetail instances.
     * 
     * @return a WitnessDetailSelector for queries that return WitnessDetail's.
     * @post return != null
     */
    public WitnessDetailSelector getWitnessDetailSelector() {
        return new WitnessSelectorImpl();
    }

    /**
     * This returns a class that will allow queries to be performed returning
     * WitnessSession instances.
     * 
     * @return a WitnessSessionSelector for queries that return
     *         WitnessSession's.
     * @post return != null
     */
    public WitnessSessionSelector getWitnessSessionSelector() {
        return new WitnessSelectorImpl();
    }

    /**
     * This returns a class that will allow queries to be performed returning
     * WitnessSummary instances.
     * 
     * @return a WitnessSummarySelector for queries that return
     *         WitnessSummary's.
     * @post return != null
     */
    public WitnessSummarySelector getWitnessSummarySelector() {
        return new WitnessSelectorImpl();
    }
}
