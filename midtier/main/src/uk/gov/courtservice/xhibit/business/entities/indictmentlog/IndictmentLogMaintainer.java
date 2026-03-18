package uk.gov.courtservice.xhibit.business.entities.indictmentlog;

//jdk
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
import uk.gov.courtservice.xhibit.business.vos.entities.IndictmentLogValue;

/**
 * <p>
 * Title: IndicmtentLogMaintainer
 * </p>
 * <p>
 * Description: This is the Maintainer class for the Entity IndictmentLog.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author: Brian Hingston
 * @version 1.0
 * 
 */

public class IndictmentLogMaintainer extends AbstractEntityMaintainer {

    private static IndictmentLogHome home = null;

    private static Logger log = CSServices.getLogger(IndictmentLogMaintainer.class);

    /**
     * IndictmentLogMaintainer default constructor
     */
    public IndictmentLogMaintainer() {
        log.debug("IndictmentLogMaintainer default constructor");
        if (home == null) {
            home = (IndictmentLogHome) CSServices.getServiceLocator().getLocalHome(IndictmentLogHome.class);
        }
    }

    /**
     * This will create a new IndictmentLog record.
     * 
     * @param value
     * @return
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("method create() called");
        if (!(value instanceof IndictmentLogValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            IndictmentLogValue indictmentLogValue = (IndictmentLogValue) value;
            IndictmentLog indictmentLog = home.create(indictmentLogValue.getCaseID(), indictmentLogValue.getSequenceNo(), 
                    indictmentLogValue.getIndictmentInfo(), userDisplayName);

            log.debug("Returning IndictmentLogValue from create(IndictmentLogValue)");
            return indictmentLog;
        } catch (CreateException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Remove the entity record.
     * 
     * @param id
     * @param version
     */
    public void delete(Integer id, Integer version) {
        log.debug("method delete() called - Id: " + id + " Version: " + version);
        try {
            IndictmentLog indictmentLog = home.findByKeyAndVersion(id, version);
            indictmentLog.remove();

            log.debug("removed entity [key: " + id + " version: " + version + "] ");
            log.debug("ending delete(Integer key, Integer version)");
        }

        catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new OptimisticLockException(e);
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            throw new EJBException(f);
        } catch (RemoveException r) {
            CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
            throw new EJBException(r);
        }
    }

    /**
     * Method to be used when update and delete IndictmentLogs. This will make sure
     * that the right record with the right version is attempted to be deleted
     * for the Optimistic locking.
     * 
     * @param key
     * @param version
     * @return IndictmentLog
     * @exception ObjectNotFoundException
     * @exception FinderException
     *                throws EJBException
     */
    public IndictmentLog findByKeyAndVersion(Integer key, Integer version) throws ObjectNotFoundException {
        log.debug("findByKeyAndVersion( " + key + ", " + version + " ) called");

        try {
            IndictmentLog indictmentLog = home.findByKeyAndVersion(key, version);
            log.debug("find successful; returning findByKeyAndVersion( Integer key, Integer version )");

            return indictmentLog;

        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw (ObjectNotFoundException) ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * This method will update an existing IndictmentLog record.
     * 
     * @param value
     *            IndictmentLogValue VO
     * @exception ObjectNotFoundException
     *                throws EJBException
     * @exception FinderException
     *                throws EJBException
     */
    public void update(CSAbstractValue value, String userDisplayName) {
        log.debug("method update(CSAbstractValue value) called");

        if (!(value instanceof IndictmentLogValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            IndictmentLogValue indictmentLogValue = (IndictmentLogValue) value;
            IndictmentLog indictmentLog = home.findByKeyAndVersion(indictmentLogValue.getId(), indictmentLogValue.getVersion());

            log.debug("update() - start updating.....");
            indictmentLog.setCaseId(indictmentLogValue.getCaseID());
            indictmentLog.setSequenceNo(indictmentLogValue.getSequenceNo());
            indictmentLog.setIndictmentInfo(indictmentLogValue.getIndictmentInfo());
            indictmentLog.setUpdated(userDisplayName);
            log.debug("update(IndictmentLogValue) finished");
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new OptimisticLockException(e);
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Method to find by the primary key.
     * 
     * @param id
     * @return IndictmentLog
     * @exception FinderException
     *                throws EJBException
     */
    public IndictmentLog findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        log.debug("method findByPrimaryKey(id) called");
        try {

            IndictmentLog indictmentLog = home.findByPrimaryKey(id);
            log.debug("Returning IndictmentLogValue from findByPrimaryKey(id)");

            return indictmentLog;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw (ObjectNotFoundException) e;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Get the local home for the IndictmentLog bean.
     * 
     * @return IndicmtentLog - the home
     */
    private IndictmentLogHome getHome() {
        log.debug("method getHome() called, returning home");
        return home;
    }

    /**
     * 
     * @param caseID
     * @return IndictmentLogValue
     * @exception FinderException
     *                throws EJBException
     */
    public Collection findByCaseID(Integer caseID)
            throws ObjectNotFoundException {
        log.debug("Method findByCaseID(caseID) called");
        Collection indictmentLogs = null;

        try {
            indictmentLogs = home.findByCaseID(caseID);
            log.debug("Returning Collection of HearingLog's from findByCaseID "
                    + "(caseID)");

            return indictmentLogs;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw (ObjectNotFoundException) e;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Create an IndictmentLogValue from the IndictmentLog entity.
     * 
     * @param IndictmentLog
     * @return IndictmentLogValue
     */
    public IndictmentLogValue getIndictmentLogValue(IndictmentLog local) {
        log.debug("method getIndictmentLogValue(IndictmentLog local) called");
        IndictmentLogValue indictmentLogValue = new IndictmentLogValue(local.getIndictmentLogId(), local.getVersion());
        setIndictmentLogValue(local, indictmentLogValue);
        log.debug("Returning IndictmentLogValue from getIndictmentLogValue(IndictmentLog)");
        return indictmentLogValue;
    }

    // ----------------------- Private Methods ---------------------------

    /**
     * Get all the values from the entity and copy them to the value.
     * 
     * @param local
     * @param indictmentLogValue
     */
    private void setIndictmentLogValue(IndictmentLog local, IndictmentLogValue indictmentLogValue) {
        log.debug("method setIndictmentLogValue(IndictmentLog local, IndictmentLogValue indictmentLogValue) called");
        indictmentLogValue.setCaseID(local.getCaseId());// tmp code - this
        // should be as above
        indictmentLogValue.setSequenceNo(local.getSequenceNo());
        indictmentLogValue.setIndictmentInfo(local.getIndictmentInfo());
        log.debug("setIndictmentLogValue finished.");
    }
}