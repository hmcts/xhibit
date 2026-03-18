package uk.gov.courtservice.xhibit.business.entities.sitting;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule.ScheduleHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingComplexValue;

/**
 * <p>
 * Title: SittingMaintainer
 * </p>
 * <p>
 * Description: Sitting Entity Maintainer Class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version $Id: SittingMaintainer.java,v 1.26 2014/06/20 17:14:21 atwells Exp $
 * 
 * <Change History/>
 * <P>
 * 10/02/03 - JB - First Introduced
 * </P>
 * <P>
 * 11/02/03 - JB - Now extends framework class
 * </P>
 * <P>
 * 11/02/03 - JB - Added a Quick create that nulls some values. Added a find by
 * PK. Removed clashes with CMP/CMR (the listId)
 * </P>
 * <P>
 * 13/02/03 - JB - Corrected VO creation
 * </P>
 * <P>
 * 18/02/03 - JB - Added method to get a collection of sittings
 * </P>
 * 
 */
public class SittingMaintainer extends AbstractEntityMaintainer {
    private static final Logger log = CSServices.getLogger(SittingMaintainer.class);

    private SittingHome home = null;

    public SittingMaintainer() {
        home = (SittingHome) CSServices.getServiceLocator().getLocalHome(SittingHome.class);
    }

    /**
     * Create a Basic VO
     * 
     * @param sitting
     * @return SittingBasicValue
     */
    public SittingBasicValue getSittingBasicValue(Sitting sitting) {
        log.debug("getBasicValue() - called");
        return createBasicVO(sitting);
    }

    /**
     * Create a Complex VO
     * 
     * @param sitting
     * @return SittingComplexValue
     */
    public SittingComplexValue getSittingComplexValue(Sitting sitting) {
        log.debug("getBasicValue() - called");
        return createComplexVO(sitting);
    }

    /**
     * Creates a list of Basic VOs from a collection of local entities.
     * 
     * @param localColl
     *            Collection of local entity objects
     * @return Collection of Sitting Basic VOs.
     */
    public Collection getSittings(Collection localColl) {
        Vector v = new Vector();
        Iterator it = localColl.iterator();

        log.debug("getSittings() - called - Collection size: " + localColl.size());

        while (it.hasNext()) {
            SittingBasicValue value = createBasicVO((Sitting) it.next());
            v.add(value);
        }

        log.debug("getSittings() - exited - OK");
        return v;
    }

    /**
     * 
     * @param value
     * @return
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("create() - called");

        if (!(value instanceof SittingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            SittingBasicValue sbv = (SittingBasicValue) value;
            Sitting sitting = home.create(sbv.getSittingSequenceNo(), sbv.getIsSittingJudge(), new Timestamp(sbv
                    .getSittingTime().getTime()), sbv.getSittingNote(), sbv.getRefJustice1ID(), sbv.getRefJustice2ID(),
                    sbv.getRefJustice3ID(), sbv.getRefJustice4ID(), sbv.getIsFloating(), sbv.getRefJudgeID(), sbv
                            .getCourtSiteID(), sbv.getCourtRoomID(), sbv.getJusticeName1(), sbv.getJusticeName2(), sbv
                            .getJusticeName3(), sbv.getJusticeName4(), userDisplayName);

            return sitting;
        } catch (CreateException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Update a sitting entity.
     * 
     * @param value
     *            SittingBasicValue VO
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        log.debug("update() - called");

        if (!(value instanceof SittingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        SittingBasicValue sbv = (SittingBasicValue) value;

        try {
            // Find home
            Sitting sitting = home.findByPrimaryKey(sbv.getId());
            if (!sitting.getVersion().equals(sbv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug("update() - updating");
                sitting.setCourtRoomId(sbv.getCourtRoomID());
                sitting.setCourtSiteId(sbv.getCourtSiteID());
                sitting.setIsFloating(sbv.getIsFloating());
                sitting.setIsSittingJudge(sbv.getIsSittingJudge());
                sitting.setJusticeName1(sbv.getJusticeName1());
                sitting.setJusticeName2(sbv.getJusticeName2());
                sitting.setJusticeName3(sbv.getJusticeName3());
                sitting.setJusticeName4(sbv.getJusticeName4());
                // Can't update this field this way... CMP/CMR clash... (JB)
                // sitting.setListID(sbv.getListID());
                sitting.setRefJudgeId(sbv.getRefJudgeID());
                sitting.setRefJustice1Id(sbv.getRefJustice1ID());
                sitting.setRefJustice2Id(sbv.getRefJustice2ID());
                sitting.setRefJustice3Id(sbv.getRefJustice3ID());
                sitting.setRefJustice4Id(sbv.getRefJustice4ID());
                sitting.setSittingNote(sbv.getSittingNote());
                sitting.setSittingSequenceNo(sbv.getSittingSequenceNo());
                sitting.setSittingTime(new Timestamp(sbv.getSittingTime().getTime()));
                sitting.setUpdated(userDisplayName);
            }
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Delete entity based on Id and version number.
     * 
     * @param id
     * @param version
     */
    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        try {
            log.debug("delete() - called - Id: " + id + " Version: " + version);
            Sitting sitting = home.findByPrimaryKey(id);
            if (!sitting.getVersion().equals(version)) {
            	throw new OptimisticLockException("Optimistic Lock Error");
            } else
                sitting.remove();
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            throw new EJBException(f);
        } catch (RemoveException r) {
            CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
            throw new EJBException(r);
        }
    }

    /**
     * Find by Primary key
     * 
     * @param sittingId
     * @return Sitting local ref.
     */
    public Sitting findByPK(Integer sittingId) throws ObjectNotFoundException {
        log.debug("findByPK() - called - sittingId: " + sittingId);

        try {
            return home.findByPrimaryKey(sittingId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * 
     * @param startTime
     *            the Date to check for sittings after
     * @param courtRoomId
     * @param courtSiteId
     * @return Sitting local ref.
     * @throws FinderException
     */
    public Collection findByDateAndCourt(Date startTime, Integer courtRoomId, Integer courtSiteId) {
        log.debug("findByDateAndCourt() - called -" + " startTime: " + startTime + " courtRoomId: " + courtRoomId
                + " courtSiteId: " + courtSiteId);

        try {
            Timestamp t = new Timestamp(startTime.getTime());
            log.debug("timestamp=" + t);
            Collection sittings = home.findByTimeCourtSiteRoomId(t, courtSiteId, courtRoomId);

            if (log.isDebugEnabled()) {
                log.debug("collection size=" + sittings.size());
                Iterator itr = sittings.iterator();
                int i = 1;
                while (itr.hasNext()) {
                    Sitting sitting = (Sitting) itr.next();
                    log.debug("[" + i++ + "] courtRoomId=" + sitting.getCourtRoomId() + " courtSiteId="
                            + sitting.getCourtSiteId() + " time=" + sitting.getSittingTime());
                }
            }

            return sittings;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection findByDateListAndCourt(Date startTime, Integer listId, Integer courtRoomId)
            throws ObjectNotFoundException {
        try {
            Timestamp t = new Timestamp(startTime.getTime());
            return home.findByDateListAndCourt(t, listId, courtRoomId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Get the next sitting sequence number for the court room and list id
     * 
     * @param listId
     * @param courtRoomId
     * @return An <code>Integer</code> containing the next sequence number, or
     *         a value of 1 if there are no previous sequence numbers
     */
    public Integer getNextSittingSequenceNo(Integer listId, Integer courtRoomId) {
        int sequenceNo = 0;

        // if the passed court room id is null, then do not bother
        // to continue as it won't be on the database
        if (courtRoomId != null) {
            try {
                final Collection sittings = findByDateListAndCourt(ScheduleHelper.getStartOfDay(), listId, courtRoomId);

                // if there are no sittings, then don't try to get the iterator
                if ((sittings != null) && (sittings.size() > 0)) {
                    final Iterator it = sittings.iterator();

                    while (it.hasNext()) {
                        final Sitting sitting = ((Sitting) it.next());
                        final Integer sN = sitting.getSittingSequenceNo();

                        // if the sitting sequence no is greater than the one
                        // we have cached, overwrite the one we have cached to
                        // return later
                        if ((sN != null) && (sN.intValue() > sequenceNo)) {
                            sequenceNo = sN.intValue();
                        }
                    }
                }
            } catch (final ObjectNotFoundException e) {
                log.debug("getNextSittingSequenceNo - no sittings found");
                // none found, so new sitting, return 1
            }
        }

        // if there are no sittings, then we will be returning 1, otherwise
        // we return one greater than the last
        return new Integer(sequenceNo + 1);
    }

    // ----------------------- Private Methods ---------------------------

    private SittingBasicValue createBasicVO(Sitting local) {
        log.debug("createBasicVO() - called");

        SittingBasicValue sbv = new SittingBasicValue(local.getSittingId(), local.getVersion());

        copyEntityPropsToVO(local, sbv);
        return sbv;
    }

    private SittingComplexValue createComplexVO(Sitting local) {
        log.debug("createComplexVO() - called");

        SittingComplexValue scv = new SittingComplexValue(local.getSittingId(), local.getVersion());

        copyEntityPropsToVO(local, scv);
        return scv;
    }

    private void copyEntityPropsToVO(Sitting local, SittingBasicValue sittingBasicValue) {
        log.debug("copyEntityPropsToVO() - called");

        // PK is done automatically
        sittingBasicValue.setCourtRoomID(local.getCourtRoomId());
        sittingBasicValue.setCourtSiteID(local.getCourtSiteId());
        if (local.getIsFloating() != null)
            sittingBasicValue.setIsFloating(local.getIsFloating());
        else
            sittingBasicValue.setIsFloating("0");

        if (local.getIsSittingJudge() != null)
            sittingBasicValue.setIsSittingJudge(local.getIsSittingJudge());
        else
            sittingBasicValue.setIsSittingJudge("0");

        sittingBasicValue.setListID(local.getListID());
        sittingBasicValue.setJusticeName1(local.getJusticeName1());
        sittingBasicValue.setJusticeName2(local.getJusticeName2());
        sittingBasicValue.setJusticeName3(local.getJusticeName3());
        sittingBasicValue.setJusticeName4(local.getJusticeName4());
        sittingBasicValue.setRefJudgeID(local.getRefJudgeId());
        sittingBasicValue.setRefJustice1ID(local.getRefJustice1Id());
        sittingBasicValue.setRefJustice2ID(local.getRefJustice2Id());
        sittingBasicValue.setRefJustice3ID(local.getRefJustice3Id());
        sittingBasicValue.setRefJustice4ID(local.getRefJustice4Id());
        sittingBasicValue.setSittingNote(local.getSittingNote());
        sittingBasicValue.setSittingSequenceNo(local.getSittingSequenceNo());

        if (log.isDebugEnabled())
            log.debug("local.getSittingTime() = " + local.getSittingTime());

        // bug fix
        if (local.getSittingTime() != null) {
            sittingBasicValue.setSittingTime(new Timestamp(local.getSittingTime().getTime()));
        }
    }
}
