package uk.gov.courtservice.xhibit.business.entities.casereference;

// jdk
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
import uk.gov.courtservice.xhibit.business.vos.entities.CaseReferenceBasicValue;

/**
 * <p>
 * Title: CaseReferenceMaintainer
 * </p>
 * <p>
 * Description: The CaseReferenceMaintainer provides a wrapper for the
 * CaseReferenceEJB. It has mapped methods for creates/deletes/updates and any
 * custom finder methods
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 * @version $Id: CaseReferenceMaintainer.java,v 1.10 2005/02/11 16:14:32 sz0t7n
 *          Exp $
 * 
 */

public class CaseReferenceMaintainer extends AbstractEntityMaintainer {
    private CaseReferenceHome home = null;

    private static Logger log = CSServices.getLogger(CaseReferenceMaintainer.class);

    // staitc variables to hold the possible values for reporting
    // restrictions
    public static Integer REPORTING_RESTRICTIONS_NOT_SET = new Integer(0);

    public static Integer REPORTING_RESTRICTIONS_SET = new Integer(1);

    public static Integer REPORTING_RESTRICTIONS_LIFTED = new Integer(99);

    // static variables to hold the event types
    public static Integer REPORTING_RESTRICTIONS_EVENT_TYPE_SET = new Integer(21200);

    public CaseReferenceMaintainer() {
        home = (CaseReferenceHome) CSServices.getServiceLocator().getLocalHome(CaseReferenceHome.class);
    }

    // ***** Value Object methods *****

    /**
     * Creates a BasicValueObject from entity.
     * 
     * @param local
     *            the entity.
     * @return CaseReferenceBasicValue complete BasicValue Object.
     */
    public CaseReferenceBasicValue getCaseReferenceBasicValue(CaseReference local) {
        log.debug("getCaseReferenceBasicValue(CaseReference local) called");

        CaseReferenceBasicValue caseReferencebvo = new CaseReferenceBasicValue(local.getCaseReferenceId(), local
                .getVersion(), local.getCaseId(), local.getReportingRestrictions());
        log.debug("returning getCaseReferenceBasicValue(CaseReference local)");
        return caseReferencebvo;

    }

    // ***** create/update/delete methods *****

    /**
     * Creates a new CaseReference entity.
     * 
     * @param value
     *            the entity to be created as instance of
     *            CaseReferenceBasicValue.
     * @return null.
     * @exception IllegalArgumentException
     *                if the input is not an instance of
     *                CaseReferenceBasicValue.
     * @exception CreateException
     *                if the container encounters a problems creating the
     *                entity.
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("create(CSAbstractValue value) called");

        if (!(value instanceof CaseReferenceBasicValue)) {
            throw new IllegalArgumentException("Unexpected type: Expected instance of CaseReferenceBasicValue got "
                    + value.getClass());
        }

        try {
            CaseReferenceBasicValue caseReferenceBasicValue = (CaseReferenceBasicValue) value;

            CaseReference caseReference = home.create(caseReferenceBasicValue.getReportingRestrictions(), userDisplayName);
            log.debug("created new CaseReference entity");
            return caseReference;

        } catch (CreateException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
    }

    /**
     * Updates the caseReference entity.
     * 
     * @param value
     *            the updated entity as instance of CaseReferenceBasicValue.
     * @exception IllegalArgumentException
     *                if the input is not an instance of
     *                CaseReferenceBasicValue.
     * @exception OptimisticLockException
     *                if the key and version number have changed, i.e entity has
     *                been updated
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof CaseReferenceBasicValue)) {
            throw new IllegalArgumentException("Unexpected type: Expected instance of CaseReferenceBasicValue got "
                    + value.getClass());
        }

        try {
            CaseReferenceBasicValue caseReferenceBasicValue = (CaseReferenceBasicValue) value;

            // Find home...
            CaseReference local = home.findByPrimaryKey(caseReferenceBasicValue.getId());

            // Locking check...
            if (!local.getVersion().equals(caseReferenceBasicValue.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {

                // setters to set values from input parameter
                local.setReportingRestrictions(caseReferenceBasicValue.getReportingRestrictions());
                local.setUpdated(userDisplayName);
            }
            log.debug("completed update of entity [key: " + caseReferenceBasicValue.getId() + "]");
            log.debug("ending " + methodName);

        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw (ObjectNotFoundException) ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw new EJBException(ex);
        }

    }

    /**
     * Removes the caseReference entity.
     * 
     * @param key
     *            the entity primary key.
     * @param version
     *            the entity version number.
     * @exception ObjectNotFoundException
     *                if the key and version number have changed, i.e entity has
     *                been updated
     * @exception RemoveException
     *                if the container encounters a problem removing the entity
     */
    public void delete(Integer key, Integer version) throws ObjectNotFoundException {
        String methodName = "delete() - ";
        log.debug(methodName + "called");

        try {
            CaseReference local = home.findByPrimaryKey(key);
            if (!local.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.remove();
            }
            log.debug("ending delete(Integer key, Integer version)");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw (ObjectNotFoundException) ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        } catch (RemoveException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
    }

    // ***** finder methods *****

    /**
     * Finder method by primary key
     * 
     * @param key
     *            the entity primary key.
     * @return the local home interface
     * @exception ObjectNotFoundException
     *                if the key and version number have changed, i.e entity has
     *                been updated
     * @exception FinderException
     *                if the container cannor locate the entity
     */
    public CaseReference findByPrimaryKey(Integer key) throws ObjectNotFoundException {
        log.debug("findByPrimaryKey( Integer key ) called");

        try {
            CaseReference caseReference = home.findByPrimaryKey(key);

            log.debug("find successful; returning findByPrimaryKey( Integer key )");
            return caseReference;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw (ObjectNotFoundException) e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
    }

    /**
     * Finder method by primary key
     * 
     * @param caseId
     *            the caseId
     * @return the Collection
     * @exception ObjectNotFoundException
     *                if the container cannor locate the entity
     */
    public CaseReference findByCaseId(Integer caseId) throws ObjectNotFoundException {
        log.debug("findByPrimaryKey( Integer key ) called");

        try {
            CaseReference caseReference = home.findByCaseId(caseId);

            log.debug("find successful; returning findByPrimaryKey( Integer key )");
            return caseReference;
        } catch (ObjectNotFoundException ex) {
            throw ex;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
    }

}