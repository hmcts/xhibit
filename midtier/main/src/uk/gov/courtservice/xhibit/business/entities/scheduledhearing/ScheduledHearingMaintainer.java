package uk.gov.courtservice.xhibit.business.entities.scheduledhearing;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version $Id: ScheduledHearingMaintainer.java,v 1.27 2006/05/10 17:30:31
 *          qz4rwx Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 10/02/03 - JB - First Issued
 * </P>
 * <P>
 * 11/02/03 - JB - Added method to get collection of VOs.
 * </P>
 * <P>
 * 13/02/03 - JB - Further functionality. Filling in methods.
 * </P>
 * <P>
 * 13/02/03 - JB - Corrected the VO creation.
 * </P>
 * <P>
 * 18/02/03 - JB - Added codes for hearing progress.
 * </P>
 * <P>
 * 19/2/03 - FS - Added find by hearing id method + cmr for complex value.
 * </P>
 * <P>
 * 22/2/03 - ST - Added findByLinkedScheduledHearing custon finder.
 * </P>
 * <P>
 * 03/03/03 - JB - Moved statics to HearingProgressValue.
 * </P>
 * <P>
 * 28/03/03 - JB - isCaseActive treated as Y or N
 * </P>
 */
public class ScheduledHearingMaintainer extends AbstractEntityMaintainer {
    private static final Logger log = CSServices.getLogger(ScheduledHearingMaintainer.class);

    private static ScheduledHearingHome home = null;

    public ScheduledHearingMaintainer() {
        if (home == null) {
            home = (ScheduledHearingHome) CSServices.getServiceLocator().getLocalHome(ScheduledHearingHome.class);
        }
    }

    /**
     * 
     * @param local
     * @return
     */
    public ScheduledHearingBasicValue getScheduledHearingBasicValue(ScheduledHearing local) {
        log.debug("getScheduledHearingBasicValue() - called");
        return createBasicVO(local);
    }

    /**
     * Creates a list of Basic VOs from a collection of local entities.
     * 
     * @param localColl
     *            Collection of local entity objects
     * @return List of ScheduledHearing Basic VOs.
     */
    public List<ScheduledHearingBasicValue> getScheduledHearings(Collection localColl) {
        log.debug("getScheduledHearings() - called - Collection size: " + localColl.size());

        List<ScheduledHearingBasicValue> v = new Vector<ScheduledHearingBasicValue>();
        Iterator it = localColl.iterator();

        while (it.hasNext()) {
            ScheduledHearingBasicValue value = createBasicVO((ScheduledHearing) it.next());
            v.add(value);
        }

        log.debug("getScheduledHearings() - exited - OK");
        return v;
    }

    /**
     * Delete based on id and version.
     * 
     * @param id
     * @param version
     */
    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        String methodName = "delete() - ";

        try {
            log.debug(methodName + "called - Id: " + id + " Version: " + version);
            ScheduledHearing scheduledHearing = home.findByPrimaryKey(id);
            if (!scheduledHearing.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else
                scheduledHearing.remove();

            log.debug(methodName + "exited - OK");
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        } catch (RemoveException r) {
            CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
            throw new EJBException(r);
        }
    }

    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        // Must use the 4 parm create in order to build the CMRs
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Create from VO.
     * 
     * @param value
     *            ScheduledHearingBasicValue VO
     * @return
     */
    public CSEntityLocal create(CSAbstractValue value, Hearing hearing, Sitting sitting, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof ScheduledHearingBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(methodName + "Unexpected type:" + value.getClass());
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        } else {
            try {
                boolean isCaseActive = false;

                ScheduledHearingBasicValue hbv = (ScheduledHearingBasicValue) value;

                if (log.isDebugEnabled()) {
                    log.debug(methodName + "Passed in parameters ..." + " notBeforeTime: " + hbv.getNotBeforeTime()
                            + " originalTime: " + hbv.getOriginalTime() + " listingNote: " + hbv.getListingNote()
                            + " hearingProgress: " + hbv.getHearingProgress() + " movedFrom: " + hbv.getMovedFrom()
                            + " sittingID: " + hbv.getSittingID() + " HearingId: " + hbv.getHearingID()
                            + " linkedSHID: " + hbv.getLinkedSHID() + " endTime: " + hbv.getEndTime() + " startTime: "
                            + hbv.getStartTime() + " dateOfHearing: " + hbv.getDateOfHearing() + " isCaseActive: "
                            + hbv.getIsCaseActive() + " movedFromCourtRoomId: " + hbv.getMovedFromCourtRoomId()
                            + " refCrackedEffectiveId: " + hbv.getRefCrackedEffectiveId());
                }

                log.debug(methodName + "Checking for nulls in times");

                if (hbv.getIsCaseActive() != null)
                    isCaseActive = hbv.getIsCaseActive().booleanValue();

                ScheduledHearing scheduledHearing = home.create(hbv.getSequenceNo(), checkForNullDate(hbv
                        .getNotBeforeTime()), checkForNullDate(hbv.getOriginalTime()), hbv.getListingNote(), hbv
                        .getHearingProgress(), hbv.getMovedFrom(), hearing, sitting, hbv.getLinkedSHID(),
                        checkForNullDate(hbv.getEndTime()), checkForNullDate(hbv.getStartTime()), checkForNullDate(hbv
                                .getDateOfHearing()), isCaseActive ? "1" : "0", 
                                        hbv.getMovedFromCourtRoomId(),
                                        hbv.getAddHearingUsed(), hbv.getRefCrackedEffectiveId(), userDisplayName);

                log.debug(methodName + "exited - OK");

                return scheduledHearing;
            } catch (CreateException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
                throw new EJBException(e);
            }
        }
    }

    /**
     * Update Entity from VO.
     * 
     * @param value
     *            ScheduledHearingBasicVO
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof ScheduledHearingBasicValue))
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());

        ScheduledHearingBasicValue sbv = (ScheduledHearingBasicValue) value;

        try {
            // Find home
            log.debug("finding scheduled hearing id=" + sbv.getId());
            ScheduledHearing sh = home.findByPrimaryKey(sbv.getId());
            log.debug("Value version = " + sbv.getVersion());
            log.debug("entity version = " + sh.getVersion());
            if (!sh.getVersion().equals(sbv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug(methodName + "Checking for nulls in times");

                boolean isCaseActive = false;
                if (sbv.getIsCaseActive() != null)
                    isCaseActive = sbv.getIsCaseActive().booleanValue();

                log.debug(methodName + "updating");
                log.debug("current sittingId=" + sh.getSittingId() + " new sittingId=" + sbv.getSittingID());
                sh.setDateOfHearing(checkForNullDate(sbv.getDateOfHearing()));
                sh.setEndTime(checkForNullDate(sbv.getEndTime()));
                sh.setHearingProgress(sbv.getHearingProgress());
                sh.setIsCaseActive(isCaseActive == true ? "Y" : "N");
                sh.setLinkedSHId(sbv.getLinkedSHID());
                sh.setListingNote(sbv.getListingNote());
                sh.setMovedFrom(sbv.getMovedFrom());
                sh.setNotBeforeTime(checkForNullDate(sbv.getNotBeforeTime()));
                sh.setOriginalTime(checkForNullDate(sbv.getOriginalTime()));
                sh.setSequenceNo(sbv.getSequenceNo());
                sh.setStartTime(checkForNullDate(sbv.getStartTime()));
                sh.setMovedFromCourtRoomId(sbv.getMovedFromCourtRoomId());
                sh.setAddHearingUsed(sbv.getAddHearingUsed());
                sh.setRefCrackedEffectiveId(sbv.getRefCrackedEffectiveId());
                // call the setUpdated so that the lastupdatedby will be set.
                sh.setUpdated(userDisplayName);
                log.debug(methodName + "exited - OK");
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
     * Find by Primary key
     * 
     * @param sittingId
     * @return
     */
    public ScheduledHearing findByPK(Integer id) throws ObjectNotFoundException {
        log.debug("findByPK() - called - Id: " + id);
        try {
            ScheduledHearing scheduledHearing = home.findByPrimaryKey(id);

            log.debug("findByPK() - exited - OK");
            return scheduledHearing;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Find by HEaring ID
     * 
     * @param hearingId
     * @return Collection
     */
    public Collection findByHearingId(Integer hearingId) throws ObjectNotFoundException {
        log.debug("findByHearingId() - called - hearingId: " + hearingId);
        try {
            Collection scheduledHearings = home.findByHearingId(hearingId);

            log.debug("findByHearingId() - exited - OK");
            return scheduledHearings;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Find by Linked Scheduled Hearing ID
     * 
     * @param linkedSchedHearingId
     * @return Collection re thrown as a business exception if necessary
     */
    public Collection<ScheduledHearing> findByLinkedSchedHearingId(Integer linkedSchedHearingId) throws ObjectNotFoundException {
        log.debug("findByLinkedSchedHearingId() - called - linkedSchedHearingId: " + linkedSchedHearingId);
        try {
            Collection<ScheduledHearing> scheduledHearings = home.findByLinkedSchedHearingId(linkedSchedHearingId);
            log.debug("findByLinkedSchedHearingId() - exited - OK");
            return scheduledHearings;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }
    
    /**
     * Find all the scheduled hearings created with 'Add Hearing'
     * @param fromDate
     * @param toDate
     * @param courtId
     * @return Collection of scheduled hearings where 'Add Hearing' was used to create them
     * @throws ObjectNotFoundException
     */
    public Collection<ScheduledHearing> findWhereAddHearingUsed(Date fromDate, Date toDate, Integer courtId) throws ObjectNotFoundException {
        log.debug("findWhereAddHearingUsed() - called - fromDate: " + fromDate + " toDate: " + toDate + " courtId: " + courtId);
        try {
            Collection<ScheduledHearing> scheduledHearings = home.findWhereAddHearingUsed(fromDate, toDate, courtId);
            log.debug("findWhereAddHearingUsed() - exited - OK");
            return scheduledHearings;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }
    
    /**
     * Find all the scheduled hearings created without 'Add Hearing'.
     * Used for cases added via add hearing
     * @param fromDate
     * @param toDate
     * @param courtId
     * @return Collection of scheduled hearings where 'Add Hearing' was used to create them
     * @throws ObjectNotFoundException
     */
    public Collection<ScheduledHearing> findWhereAddHearingNotUsed(Date fromDate, Date toDate, Integer courtId) throws ObjectNotFoundException {
        log.debug("findWhereAddHearingNotUsed() - called - fromDate: " + fromDate + " toDate: " + toDate + " courtId: " + courtId);
        try {
            Collection<ScheduledHearing> scheduledHearings = home.findWhereAddHearingNotUsed(fromDate, toDate, courtId);
            log.debug("findWhereAddHearingNotUsed() - exited - OK");
            return scheduledHearings;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    //
    // -------------------- Private Methods ------------------------
    //
    public ScheduledHearingBasicValue createBasicVO(ScheduledHearing local) {
        log.debug("createBasicVO() - called");

        ScheduledHearingBasicValue shbv = new ScheduledHearingBasicValue(local.getScheduledHearingId(), local
                .getVersion());

        copyEntityPropsToVO(local, shbv);
        return shbv;
    }

    private void copyEntityPropsToVO(ScheduledHearing local, ScheduledHearingBasicValue scheduledHearingBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");
        boolean isCaseActive = false;

        scheduledHearingBasicValue.setHearingID(local.getHearingId());
        scheduledHearingBasicValue.setDateOfHearing(local.getDateOfHearing());
        scheduledHearingBasicValue.setEndTime(local.getEndTime());
        scheduledHearingBasicValue.setHearingProgress(local.getHearingProgress());
        if (local.getIsCaseActive() != null) {
            scheduledHearingBasicValue.setIsCaseActive(new Boolean(local.getIsCaseActive().equals("1")));
        }
        scheduledHearingBasicValue.setLinkedSHID(local.getLinkedSHId());
        scheduledHearingBasicValue.setListingNote(local.getListingNote());
        scheduledHearingBasicValue.setMovedFrom(local.getMovedFrom());
        scheduledHearingBasicValue.setNotBeforeTime(local.getNotBeforeTime());
        scheduledHearingBasicValue.setOriginalTime(local.getOriginalTime());
        scheduledHearingBasicValue.setSequenceNo(local.getSequenceNo());
        scheduledHearingBasicValue.setSittingID(local.getSittingId());
        scheduledHearingBasicValue.setStartTime(local.getStartTime());
        if (local.getIsCaseActive() != null && local.getIsCaseActive().equals("Y"))
            isCaseActive = true;
        scheduledHearingBasicValue.setIsCaseActive(new Boolean(isCaseActive));
        scheduledHearingBasicValue.setMovedFromCourtRoomId(local.getMovedFromCourtRoomId());
        scheduledHearingBasicValue.setAddHearingUsed(local.getAddHearingUsed());
        scheduledHearingBasicValue.setRefCrackedEffectiveId(local.getRefCrackedEffectiveId());
    }

    private Timestamp checkForNullDate(Date date) {
        return ((date != null) ? new Timestamp(date.getTime()) : null);
    }
}
