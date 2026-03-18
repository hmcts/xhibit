package uk.gov.courtservice.xhibit.business.entities.hatesentencing;

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
import uk.gov.courtservice.xhibit.business.vos.entities.HateSentencingValue;

/**
 * <p>
 * Title: HateSentencingMaintainer
 * </p>
 * <p>
 * Description: This is the Maintainer class for the Entity HateSentencing.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author: Brian Hingston
 * @version 1.0
 * 
 */

public class HateSentencingMaintainer extends AbstractEntityMaintainer {

    private static HateSentencingHome home = null;

    private static Logger log = CSServices.getLogger(HateSentencingMaintainer.class);

    /**
     * HateSentencingMaintainer default constructor
     */
    public HateSentencingMaintainer() {
        log.debug("HateSentencingMaintainer default constructor");
        if (home == null) {
            home = (HateSentencingHome) CSServices.getServiceLocator().getLocalHome(HateSentencingHome.class);
        }
    }

    /**
     * This will create a new HateSentencing record.
     * 
     * @param value
     * @return
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("method create() called");
        if (!(value instanceof HateSentencingValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            HateSentencingValue hateSentencingValue = (HateSentencingValue) value;
            HateSentencing hateSentencing = home.create(hateSentencingValue.getDefendantOnCaseId(), hateSentencingValue.getRefHateSentencingTypeId(), 
                    userDisplayName);

            log.debug("Returning HateSentencingValue from create(HateSentencingValue)");
            return hateSentencing;
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
            HateSentencing hateSentencing = home.findByKeyAndVersion(id, version);
            hateSentencing.remove();

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
     * Method to be used when update and delete Hate Sentencing. This will make sure
     * that the right record with the right version is attempted to be deleted
     * for the Optimistic locking.
     * 
     * @param key
     * @param version
     * @return HateSentencing
     * @exception ObjectNotFoundException
     * @exception FinderException
     *                throws EJBException
     */
    public HateSentencing findByKeyAndVersion(Integer key, Integer version) throws ObjectNotFoundException {
        log.debug("findByKeyAndVersion( " + key + ", " + version + " ) called");

        try {
            HateSentencing hateSentencing = home.findByKeyAndVersion(key, version);
            log.debug("find successful; returning findByKeyAndVersion( Integer key, Integer version )");

            return hateSentencing;

        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw (ObjectNotFoundException) ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * This method will update an existing Hate Sentencing record.
     * 
     * @param value
     *            HateSentencingValue VO
     * @exception ObjectNotFoundException
     *                throws EJBException
     * @exception FinderException
     *                throws EJBException
     */
    public void update(CSAbstractValue value, String userDisplayName) {
        log.debug("method update(CSAbstractValue value) called");

        if (!(value instanceof HateSentencingValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            HateSentencingValue hateSentencingValue = (HateSentencingValue) value;
            HateSentencing hateSentencing = home.findByKeyAndVersion(hateSentencingValue.getId(), hateSentencingValue.getVersion());

            log.debug("update() - start updating.....");
            hateSentencing.setDefendantOnCaseId(hateSentencingValue.getDefendantOnCaseId());
            hateSentencing.setRefHateSentTypeId(hateSentencingValue.getRefHateSentencingTypeId());
            hateSentencing.setUpdated(userDisplayName);
            log.debug("update(HateSentencingValue) finished");
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
     * @return HateSentencing
     * @exception FinderException
     *                throws EJBException
     */
    public HateSentencing findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        log.debug("method findByPrimaryKey(id) called");
        try {

            HateSentencing hateSentencing = home.findByPrimaryKey(id);
            log.debug("Returning HateSentencingValue from findByPrimaryKey(id)");

            return hateSentencing;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw (ObjectNotFoundException) e;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Get the local home for the HateSentencing bean.
     * 
     * @return HateSentencing - the home
     */
    private HateSentencingHome getHome() {
        log.debug("method getHome() called, returning home");
        return home;
    }

    /**
     * 
     * @param caseID
     * @return HateSentencingValue
     * @exception FinderException
     *                throws EJBException
     */
    public Collection findByDefendantOnCaseId(Integer defOnCaseID)
            throws ObjectNotFoundException {
        log.debug("Method findByDefendantOnCaseID(defOnCaseID) called");
        Collection hateSentencing = null;

        try {
            hateSentencing = home.findByDefendantOnCaseId(defOnCaseID);
            log.debug("Returning Collection of HearingLog's from findByDefendantOnCaseID "
                    + "(defOnCaseID)");

            return hateSentencing;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw (ObjectNotFoundException) e;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }
    
    /**
     * 
     * @param caseID
     * @return HateSentencingValue
     * @exception FinderException
     *                throws EJBException
     */
    public Collection findNonObsoleteByDefendantOnCaseId(Integer defOnCaseID)
            throws ObjectNotFoundException {
        log.debug("Method findNonObsoleteByDefendantOnCaseID(defOnCaseID) called");
        Collection hateSentencing = null;

        try {
            hateSentencing = home.findNonObsoleteByDefendantOnCaseId(defOnCaseID);
            log.debug("Returning Collection of HearingLog's from findNonObsoleteByDefendantOnCaseID "
                    + "(defOnCaseID)");

            return hateSentencing;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw (ObjectNotFoundException) e;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Create an HateSentencingValue from the HateSEntencing entity.
     * 
     * @param D20OffenceCode
     * @return HateSentencingValue
     */
    public HateSentencingValue getHateSentencingValue(HateSentencing local) {
        log.debug("method getHateSentencingValue(HateSentencing local) called");
        HateSentencingValue hateSentencingValue = new HateSentencingValue(local.getHateSentencingId(), local.getVersion());
        setHateSentencingValue(local, hateSentencingValue);
        log.debug("Returning HateSentencingValue from getHateSEntencingValue(HateSentencing)");
        return hateSentencingValue;
    }

    // ----------------------- Private Methods ---------------------------

    /**
     * Get all the values from the entity and copy them to the value.
     * 
     * @param local
     * @param hateSentencingValue
     */
    private void setHateSentencingValue(HateSentencing local, HateSentencingValue hateSentencingValue) {
        log.debug("method setHateSentencingValue(HateSentencing local, HateSentencingValue hateSentencingValue) called");
        hateSentencingValue.setDefendantOnCaseId(local.getDefendantOnCaseId());// tmp code - this
        // should be as above
        hateSentencingValue.setRefHateSentencingTypeId(local.getRefHateSentTypeId());
        log.debug("setHateSentencingValue finished.");
    }
}
