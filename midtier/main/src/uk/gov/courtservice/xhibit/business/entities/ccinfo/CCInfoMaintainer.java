package uk.gov.courtservice.xhibit.business.entities.ccinfo;

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
import uk.gov.courtservice.xhibit.business.vos.entities.CCInfoBasicValue;

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

public class CCInfoMaintainer extends AbstractEntityMaintainer {
    private static CcInfoHome home = null;

    private static Logger log = CSServices.getLogger(CCInfoMaintainer.class);

    public CCInfoMaintainer() {
        if (home == null) {
            home = (CcInfoHome) CSServices.getServiceLocator().getLocalHome(CcInfoHome.class);
        }
    }

    public CCInfoBasicValue getCCInfoBasicValue(CcInfo local) {
        String methodName = "getCCInfoBasicValue() - ";
        log.debug(methodName + "called");

        CCInfoBasicValue bv = createBasicVO(local);

        log.debug(methodName + "exited - OK");
        return bv;
    }

    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof CCInfoBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            CCInfoBasicValue bv = (CCInfoBasicValue) value;
            return home.create(bv.getCcInfoText(), userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof CCInfoBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        CCInfoBasicValue bv = (CCInfoBasicValue) value;
        try {
            // Find home...
            CcInfo local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.setCcInfoText(bv.getCcInfoText());
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
            CcInfo local = home.findByPrimaryKey(id);
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

    public CcInfo findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        try {
            return home.findByPrimaryKey(id);
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        }

    }

    private CCInfoBasicValue createBasicVO(CcInfo local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        CCInfoBasicValue bv = new CCInfoBasicValue(local.getCcInfoId(), local.getVersion());

        copyEntityPropsToVO(local, bv);
        return bv;
    }

    private void copyEntityPropsToVO(CcInfo local, CCInfoBasicValue CCInfoBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        CCInfoBasicValue.setCcInfoText(local.getCcInfoText());
    }
}