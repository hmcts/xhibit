package uk.gov.courtservice.xhibit.business.entities.linkedsh;

//EJB
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
import uk.gov.courtservice.xhibit.business.vos.entities.LinkedSHBasicValue;

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

public class LinkedSHMaintainer extends AbstractEntityMaintainer {
    private static LinkedShHome home = null;

    private static Logger log = CSServices.getLogger(LinkedSHMaintainer.class);

    public LinkedSHMaintainer() {
        if (home == null) {
            home = (LinkedShHome) CSServices.getServiceLocator().getLocalHome(LinkedShHome.class);
        }
    }

    public LinkedSHBasicValue getLinkedSHBasicValue(LinkedSh local) {
        String methodName = "getLinkedSHBasicValue() - ";
        log.debug(methodName + "called");

        LinkedSHBasicValue bv = createBasicVO(local);

        log.debug(methodName + "exited - OK");
        return bv;
    }

    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof LinkedSHBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            LinkedSHBasicValue bv = (LinkedSHBasicValue) value;
            return home.create(userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof LinkedSHBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        LinkedSHBasicValue bv = (LinkedSHBasicValue) value;
        try {
            // Find home...
            LinkedSh local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                // There is nothing to update...
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
            LinkedSh local = home.findByPrimaryKey(id);
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

    public LinkedSh findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

    private LinkedSHBasicValue createBasicVO(LinkedSh local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        LinkedSHBasicValue bv = new LinkedSHBasicValue(local.getLinkedShId(), local.getVersion());

        return bv;
    }
}