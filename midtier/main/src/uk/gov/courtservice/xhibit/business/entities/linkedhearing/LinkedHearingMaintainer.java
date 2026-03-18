package uk.gov.courtservice.xhibit.business.entities.linkedhearing;

//EJB
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.LinkedHearingBasicValue;

/**
 * <p>
 * Title: LinkedHearingMaintainer
 * </p>
 * <p>
 * Description: Maintainer for the LinkedHearing.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class LinkedHearingMaintainer {

    private static LinkedHearingHome home = null;

    private static Logger log = CSServices.getLogger(LinkedHearingMaintainer.class);

    /**
     * Default constructor
     */
    public LinkedHearingMaintainer() {
        if (home == null) {
            home = (LinkedHearingHome) CSServices.getServiceLocator().getLocalHome(LinkedHearingHome.class);
        }
    }

    /**
     * Get a basic value from the local entity.
     * 
     * @param local
     * @return LinkedHearingBasicValue
     */
    public LinkedHearingBasicValue getLinkedSHBasicValue(LinkedHearing local) {

        log.debug("LinkedHearingMaintainer.getLinkedSHBasicValue(LinkedHearing local) called");

        LinkedHearingBasicValue bv = createBasicVO(local);

        log.debug("LinkedHearingMaintainer.getLinkedSHBasicValue(LinkedHearing local) finished");
        return bv;
    }

    /**
     * Method to create a new entry
     * 
     * @param value
     * @return CSEntityLocal
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("LinkedHearingMaintainer.create(CSAbstractValue value) called");

        if (!(value instanceof LinkedHearingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            LinkedHearingBasicValue bv = (LinkedHearingBasicValue) value;
            return home.create(userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        } finally {
            log.debug("LinkedHearingMaintainer.create(CSAbstractValue value) finished");
        }
    }

    /**
     * Update method
     * 
     * @param value
     * @throws ObjectNotFoundException
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        log.debug("LinkedHearingMaintainer.update(CSAbstractValue value) called");
        if (!(value instanceof LinkedHearingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        LinkedHearingBasicValue bv = (LinkedHearingBasicValue) value;
        try {
            // Find home...
            LinkedHearing local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                // There is no extra values to update...
                local.setUpdated(userDisplayName);
            }
            log.debug("LinkedHearingMaintainer.update(CSAbstractValue value) finished");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * This will permanently delete a entry.
     * 
     * @param id
     * @param version
     * @throws ObjectNotFoundException
     */
    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        log.debug("LinkedHearingMaintainer.delete(Integer id, Integer version) called");
        try {
            LinkedHearing local = home.findByPrimaryKey(id);
            if (!local.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.remove();
                log.debug("LinkedHearingMaintainer.delete(Integer id, Integer version) finished");
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

    /**
     * Find by the primary key method.
     * 
     * @param id
     * @return LinkedHearing
     * @throws ObjectNotFoundException
     */
    public LinkedHearing findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        log.debug("LinkedHearingMaintainer.findByPrimaryKey(Integer id) called");
        try {
            return home.findByPrimaryKey(id);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        } finally {
            log.debug("LinkedHearingMaintainer.findByPrimaryKey(Integer id) finished");
        }
    }

    /**
     * Transform a linked hearing to a basic value
     * 
     * @param local
     *            LinkedHearing
     * @return LinkedHearingBasicValue
     */
    private LinkedHearingBasicValue createBasicVO(LinkedHearing local) {
        log.debug("LinkedHearingMaintainer.createBasicVO(LinkedHearing local) called");

        LinkedHearingBasicValue bv = new LinkedHearingBasicValue(local.getLinkedHearingId(), local.getVersion());

        log.debug("LinkedHearingMaintainer.createBasicVO(LinkedHearing local) finished");
        return bv;
    }

}