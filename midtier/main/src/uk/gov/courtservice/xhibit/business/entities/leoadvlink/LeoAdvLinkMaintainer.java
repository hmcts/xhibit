package uk.gov.courtservice.xhibit.business.entities.leoadvlink;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.vos.entities.LeoAdvLinkBasicValue;
import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: LeoAdvLink Entity Maintainer Class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 */
public class LeoAdvLinkMaintainer extends AbstractEntityMaintainer {

    private static LeoAdvLinkMaintainer instance;

    private static final Logger log = CSServices.getLogger(LeoAdvLinkMaintainer.class);

    private static LeoAdvLinkHome home = null;

    public LeoAdvLinkMaintainer() {
        if (home == null) {
            home = (LeoAdvLinkHome) CSServices.getServiceLocator().getLocalHome(LeoAdvLinkHome.class);
        }
    }

    public static LeoAdvLinkMaintainer getInstance() {
        if (instance == null) {
            synchronized (LeoAdvLinkMaintainer.class) {
                if (instance == null) {
                    LeoAdvLinkMaintainer localInstance = new LeoAdvLinkMaintainer();
                    instance = localInstance;
                }
            }
        }

        return instance;
    }

    /**
     * Create and return a Basic VO from local entity.
     * 
     * @param local
     * @return
     */
    public LeoAdvLinkBasicValue getLeoAdvLinkBasicValue(LeoAdvLink local) {
        String methodName = "getLeoAdvLinkBasicValue() - ";
        log.debug(methodName + "called");

        LeoAdvLinkBasicValue bv = createBasicVO(local);

        log.debug(methodName + "exited - OK");
        return bv;
    }

    /**
     * Create entity from VO.
     * 
     * @param value
     * @return
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        String methodName = "create() - ";

        log.debug(methodName + "called");
        if (!(value instanceof LeoAdvLinkBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            LeoAdvLinkBasicValue bv = (LeoAdvLinkBasicValue) value;

            return home.create(
                    bv.getLegalAidOrderId(), 
                    bv.getDefendantOnCaseId(), 
                    bv.getRefAdvocateId(), 
                    bv.getCrestAdvCategory(),
                    bv.getCrestPostNumber(),
                    bv.getAvailable(),
                    bv.getNewRowFlag(),
                    bv.getObsInd(), userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    /**
     * Update using VO.
     * 
     * @param value
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof LeoAdvLinkBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        LeoAdvLinkBasicValue bv = (LeoAdvLinkBasicValue) value;
        try {
            // Find home...
            LeoAdvLink local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.setLegalAidOrderId(bv.getLegalAidOrderId());
                local.setDefendantOnCaseId(bv.getDefendantOnCaseId());
                local.setRefAdvocateId(bv.getRefAdvocateId());
                local.setCrestAdvCategory(bv.getCrestAdvCategory());
                local.setCrestPostNumber(bv.getCrestPostNumber());
                local.setAvailable(bv.getAvailable());
                local.setNewRowFlag(bv.getNewRowFlag());
                local.setObsInd(bv.getObsInd());
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

    /**
     * Update using collection of VO.
     * 
     * @param value
     * @param userDisplayName
     */
    public void update(LeoAdvLinkBasicValue[] values, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        try {
            for (int i = 0; i < values.length; i++) {
                update(values[i], userDisplayName);
            }

            log.debug(methodName + "exited - OK");
        } catch (ObjectNotFoundException ex) {
            // ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            // ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Delete based on ID/Version
     * 
     * @param id
     * @param version
     */
    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        String methodName = "delete() - ";
        log.debug(methodName + "called");

        try {
            LeoAdvLink local = home.findByPrimaryKey(id);

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

    /**
     * Find by Primary key
     * 
     * @param hearingId
     * @return
     */
    public LeoAdvLink findByPK(Integer id) throws ObjectNotFoundException {
        String methodName = "findByPK() - ";

        log.debug(methodName + "called - leoAdvLinkId: " + id);
        try {
            LeoAdvLink local = home.findByPrimaryKey(id);

            log.debug(methodName + "exited - OK");
            return local;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public LeoAdvLink findByDefendantOnCaseRefAdvocateIdLegalAidOrderPostNumber(
            Integer defendantOnCaseId,
            Integer refAdvocateId,
            Integer legalAidOrderId,
            Integer crestPostNumber) throws ObjectNotFoundException {
        
        String methodName = "findByDefendantOnCaseRefAdvocateIdLegalAidOrderPostNumber() - ";

        log.debug(methodName + "called - defendantOnCaseId: " + defendantOnCaseId
                + " refAdvocateId: " + refAdvocateId
                + " legalAidOrderId: " + legalAidOrderId
                + " crestPostNumber: " + crestPostNumber);
        
        try {
            LeoAdvLink local = home.findByDefendantOnCaseRefAdvocateIdLegalAidOrderPostNumber(
                    defendantOnCaseId, refAdvocateId, legalAidOrderId, crestPostNumber);

            log.debug(methodName + "exited - OK");
            return local;
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }
    
    private LeoAdvLinkBasicValue createBasicVO(LeoAdvLink local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        LeoAdvLinkBasicValue hbv = new LeoAdvLinkBasicValue(local.getLeoAdvLinkId(), local.getVersion());

        copyEntityPropsToVO(local, hbv);
        return hbv;
    }

    private void copyEntityPropsToVO(LeoAdvLink local, LeoAdvLinkBasicValue leoAdvLinkBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        leoAdvLinkBasicValue.setLegalAidOrderId(local.getLegalAidOrderId());
        leoAdvLinkBasicValue.setDefendantOnCaseId(local.getDefendantOnCaseId());
        leoAdvLinkBasicValue.setRefAdvocateId(local.getRefAdvocateId());
        leoAdvLinkBasicValue.setCrestAdvCategory(local.getCrestAdvCategory());
        leoAdvLinkBasicValue.setCrestPostNumber(local.getCrestPostNumber());
        leoAdvLinkBasicValue.setAvailable(local.getAvailable());
        leoAdvLinkBasicValue.setNewRowFlag(local.getNewRowFlag());
        leoAdvLinkBasicValue.setObsInd(local.getObsInd());
    }
}