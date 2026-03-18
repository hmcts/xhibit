package uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingDefendantBasicValue;

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
 * @author Khan Tran
 * @version 1.0
 */

public class SchedHearingDefendantMaintainer extends AbstractEntityMaintainer {

    private static SchedHearingDefendantHome home = null;

    private static Logger log = CSServices.getLogger(SchedHearingDefendantMaintainer.class);

    public SchedHearingDefendantMaintainer() {
        if (home == null) {
            home = (SchedHearingDefendantHome) CSServices.getServiceLocator().getLocalHome(
                    SchedHearingDefendantHome.class);
        }
    }

    public SchedHearingDefendantBasicValue getSchedHearingDefendantBasicValue(SchedHearingDefendant local) {
        String methodName = "getSchedHearingDefendantBasicValue() - ";
        log.debug(methodName + "called");

        SchedHearingDefendantBasicValue bv = createBasicVO(local);

        log.debug(methodName + "exited - OK");
        return bv;
    }

    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        // Must use the 3 parm create in order to build the CMRs
        throw new java.lang.UnsupportedOperationException();
    }

    public CSEntityLocal create(CSAbstractValue value, ScheduledHearing scheduledHearing, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof SchedHearingDefendantBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            SchedHearingDefendantBasicValue bv = (SchedHearingDefendantBasicValue) value;
            return home.create(bv.getScheduledHearingID(), bv.getDefendantOnCaseID(), scheduledHearing, userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof SchedHearingDefendantBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        SchedHearingDefendantBasicValue bv = (SchedHearingDefendantBasicValue) value;
        try {
            // Find home...
            SchedHearingDefendant local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                // local.setScheduledHearingId(bv.getScheduledHearingID());
                local.setDefOnCaseID(bv.getDefendantOnCaseID());
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
            SchedHearingDefendant local = home.findByPrimaryKey(id);
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

    public SchedHearingDefendant findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

    public Collection findByDefOnCaseId(Integer defOnCaseId) throws ObjectNotFoundException {
        String methodName = "findByCaseId() - ";
        log.debug(methodName + "called - linkedhearingId: " + defOnCaseId);

		try {
			Collection schedHearingDefendants = home.findByDefOnCaseId(defOnCaseId);
			ArrayList<SchedHearingDefendantBasicValue> schedHearingDefendantsBV = new ArrayList<SchedHearingDefendantBasicValue>();
			if (schedHearingDefendants != null) {
				Iterator itr = schedHearingDefendants.iterator();
				while (itr.hasNext()) {
					SchedHearingDefendant schedDefendant = (SchedHearingDefendant) itr.next();
					schedHearingDefendantsBV.add(this.getSchedHearingDefendantBasicValue(schedDefendant));
				}
			}
			return schedHearingDefendantsBV;
		} catch (Exception ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new javax.ejb.EJBException(ex);
		}
    }

    public Collection findUnrepresentedDefendants(Integer scheduledHearingId) throws ObjectNotFoundException {
        String methodName = "findUnrepresentedDefendants() - ";
        log.debug(methodName + "called - scheduledHearingId: " + scheduledHearingId);

        try {
            Collection schedHearingDefendants = home.findUnrepresentedDefendants(scheduledHearingId);
            log.debug(methodName + "exited - OK");
            return schedHearingDefendants;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection findByScheduledHearingId(Integer scheduledHearingId) throws ObjectNotFoundException {
        String methodName = "findByScheduledHearingId() - ";
        log.debug(methodName + "called - scheduledHearingId: " + scheduledHearingId);

        try {
            Collection schedHearingDefendants = home.findByScheduledHearingId(scheduledHearingId);
            log.debug(methodName + "exited - OK");
            return schedHearingDefendants;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    private SchedHearingDefendantBasicValue createBasicVO(SchedHearingDefendant local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        SchedHearingDefendantBasicValue bv = new SchedHearingDefendantBasicValue(local.getSchedHearDefId(), local
                .getVersion());

        copyEntityPropsToVO(local, bv);
        return bv;
    }

    private void copyEntityPropsToVO(SchedHearingDefendant local,
            SchedHearingDefendantBasicValue schedHearingDefendantBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        schedHearingDefendantBasicValue.setScheduledHearingID(local.getScheduledHearingId());
        schedHearingDefendantBasicValue.setDefendantOnCaseID(local.getDefOnCaseID());
    }

    public SchedHearingDefendant findBySchedHearingIdAndDefOnCaseId(Integer schedHearingDefId, Integer defOnCaseId)
            throws ObjectNotFoundException {
        String methodName = "*** findBySchedHearingIdAndDefOnCaseId(" + schedHearingDefId + ", " + defOnCaseId + ") - ";
        log.debug(methodName + "called ***");

        try {
            SchedHearingDefendant schedHearingDefendant = home.findBySchedHearingIdAndDefOnCaseId(schedHearingDefId,
                    defOnCaseId);
            log.debug(methodName + "exited - OK");
            return schedHearingDefendant;
        } catch (ObjectNotFoundException e) {
            log.warn("findBySchedHearingIdAndDefOnCaseId " + e);
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

}