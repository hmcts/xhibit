package uk.gov.courtservice.xhibit.business.entities.schedhearingattendee;

//EJB
import java.util.Collection;

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
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeComplexValue;

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
 * @author Khanh Tran
 * @version 1.0
 */

public class SchedHearingAttendeeMaintainer extends AbstractEntityMaintainer {
    private static SchedHearingAttendeeHome home = null;

    private static Logger log = CSServices.getLogger(SchedHearingAttendeeMaintainer.class);

    public static final String JUDGE = new String("J");

    public SchedHearingAttendeeMaintainer() {
        if (home == null) {
            home = (SchedHearingAttendeeHome) CSServices.getServiceLocator().getLocalHome(
                    SchedHearingAttendeeHome.class);
        }
    }

    public SchedHearingAttendeeBasicValue getSchedHearingAttendeeBasicValue(SchedHearingAttendee local) {
        String methodName = "getSchedHearingAttendeeBasicValue() - ";
        log.debug(methodName + "called");

        SchedHearingAttendeeBasicValue bv = createBasicVO(local);

        log.debug(methodName + "exited - OK");
        return bv;
    }

    public SchedHearingAttendeeComplexValue getSchedHearingAttendeeComplexValue(SchedHearingAttendee local) {
        String methodName = "getSchedHearingAttendeeComplexValue() - ";
        log.debug(methodName + "called");

        SchedHearingAttendeeComplexValue cv = createComplexVO(local);

        log.debug(methodName + "exited - OK");
        return cv;
    }

    /**
     * No Used
     * 
     * @param value
     * @return
     * @deprecated Please use the overloaded method
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        throw new UnsupportedOperationException("Use create(CSAbstractValue, ScheduledHearing)");
    }

    public CSEntityLocal create(CSAbstractValue value, ScheduledHearing scheduledHearing, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof SchedHearingAttendeeBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            SchedHearingAttendeeBasicValue bv = (SchedHearingAttendeeBasicValue) value;
            return home.create(bv.getAttendeeType(), bv.getRefJudgeID(), bv.getRefJusticeID(), bv
                    .getRefCourtReporterID(), scheduledHearing, userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof SchedHearingAttendeeBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        SchedHearingAttendeeBasicValue bv = (SchedHearingAttendeeBasicValue) value;
        try {
            // Find home...
            SchedHearingAttendee local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.setAttendeeType(bv.getAttendeeType());
                local.setRefJudgeId(bv.getRefJudgeID());
                local.setRefCourtRepId(bv.getRefCourtReporterID());
                local.setUpdated(userDisplayName);
            }

            log.debug(methodName + "exited - OK");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        String methodName = "delete() - ";
        log.debug(methodName + "called");

        try {
            SchedHearingAttendee local = home.findByPrimaryKey(id);
            if (!local.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.remove();
                log.debug(methodName + "exited - OK");
            }
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

    public SchedHearingAttendee findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        try {
            return home.findByPrimaryKey(id);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection findByScheduledHearingId(Integer scheduledHearingId) {
        String methodName = "findByCaseId() - ";
        log.debug(methodName + "called - linkedhearingId: " + scheduledHearingId);

        try {
            Collection schedHearingAttendee = home.findByScheduledHearingId(scheduledHearingId);
            log.debug(methodName + "exited - OK");
            return schedHearingAttendee;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection findByRefJudgeIdAndSHAttendeeId(Integer refJudgeId, Integer shAttendeeId)
            throws ObjectNotFoundException {
        log.debug("findByRefJudgeIdAndSHAttendeeId() start refJudgeId=" + refJudgeId + " shAttendeeId=" + shAttendeeId);

        try {
            Collection schedHearingAttendees = home.findByRefJudgeIdAndSHAttendeeId(refJudgeId, shAttendeeId);
            log.debug("findRefJudgeId() exited - OK");
            return schedHearingAttendees;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection findByRefCourtReporterId(Integer refCourtRepId) throws ObjectNotFoundException {
        String methodName = "findByRefCourtReporterId() - ";
        log.debug(methodName + "called - findByRefCourtReporterId: " + refCourtRepId);

        try {
            Collection schedHearingAttendee = home.findByRefCourtReporterId(refCourtRepId);
            log.debug(methodName + "exited - OK");
            return schedHearingAttendee;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection findBySHStaffId(Integer shStaffId) throws ObjectNotFoundException {
        String methodName = "findBySHStaffId() - ";
        log.debug(methodName + "called - SHStaffId: " + shStaffId);

        try {
            Collection schedHearingAttendee = home.findBySHStaffId(shStaffId);
            log.debug(methodName + "exited - OK");
            return schedHearingAttendee;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    private SchedHearingAttendeeBasicValue createBasicVO(SchedHearingAttendee local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        SchedHearingAttendeeBasicValue bv = new SchedHearingAttendeeBasicValue(local.getShAttendeeId(), local
                .getVersion());

        copyEntityPropsToVO(local, bv);
        return bv;
    }

    private SchedHearingAttendeeComplexValue createComplexVO(SchedHearingAttendee local) {
        String methodName = "createComplexVO() - ";
        log.debug(methodName + "called");

        SchedHearingAttendeeComplexValue cv = new SchedHearingAttendeeComplexValue(local.getShAttendeeId(), local
                .getVersion());

        copyEntityPropsToVO(local, cv);
        return cv;
    }

    private void copyEntityPropsToVO(SchedHearingAttendee local,
            SchedHearingAttendeeBasicValue schedHearingAttendeeBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        schedHearingAttendeeBasicValue.setAttendeeType(local.getAttendeeType());
        schedHearingAttendeeBasicValue.setSheduledHearingID(local.getScheduledHearingId());
        schedHearingAttendeeBasicValue.setShStaffID(local.getShStaffId());
        // schedHearingAttendeeBasicValue.setShLegRepID(local.getShAttendeeId());
        schedHearingAttendeeBasicValue.setShJusticeID(local.getShJusticeId());
        schedHearingAttendeeBasicValue.setRefJudgeID(local.getRefJudgeId());
        schedHearingAttendeeBasicValue.setRefCourtReporterID(local.getRefCourtRepId());
    }
}