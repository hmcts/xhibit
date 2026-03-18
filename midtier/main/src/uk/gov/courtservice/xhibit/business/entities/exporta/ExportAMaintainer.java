package uk.gov.courtservice.xhibit.business.entities.exporta;

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
import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;

/**
 * <p>
 * Title: ExportAMaintainer
 * </p>
 * <p>
 * Description: This is the Maintainer class for the Entity ExportA.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author: Marie Holmberg
 * @version 1.0
 * 
 */

public class ExportAMaintainer extends AbstractEntityMaintainer {

    private static ExportAHome home = null;

    private static Logger log = CSServices.getLogger(ExportAMaintainer.class);

    /**
     * ExportAMaintainer default constructor
     */
    public ExportAMaintainer() {
        log.debug("ExportAMaintainer default constructor");
        if (home == null) {
            home = (ExportAHome) CSServices.getServiceLocator().getLocalHome(ExportAHome.class);
        }
    }

    /**
     * This will create a new ExportA record.
     * 
     * @param value
     * @return
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("method create() called");
        if (!(value instanceof ExportAValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            ExportAValue exportAValue = (ExportAValue) value;
            ExportA exportA = home.create(exportAValue.getCourtClerkName(), exportAValue.getStatusFlag(),
                    exportAValue.getLinkedHearingID(), exportAValue.getHearingID(), userDisplayName);

            log.debug("Returning ExportAValue from create(ExportAValue)");
            return exportA;
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
            ExportA exportA = home.findByKeyAndVersion(id, version);
            exportA.remove();

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
     * Method to be used when update and delete ExportA's. This will make sure
     * that the right record with the right version is attempted to be deleted
     * for the Optimistic locking.
     * 
     * @param key
     * @param version
     * @return ExportA
     * @exception ObjectNotFoundException
     * @exception FinderException
     *                throws EJBException
     */
    public ExportA findByKeyAndVersion(Integer key, Integer version) throws ObjectNotFoundException {
        log.debug("findByKeyAndVersion( " + key + ", " + version + " ) called");

        try {
            ExportA exportA = home.findByKeyAndVersion(key, version);
            log.debug("find successful; returning findByKeyAndVersion( Integer key, Integer version )");

            return exportA;

        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw (ObjectNotFoundException) ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * This method will update an existing ExportA record.
     * 
     * @param value
     *            ExportAValue VO
     * @exception ObjectNotFoundException
     *                throws EJBException
     * @exception FinderException
     *                throws EJBException
     */
    public void update(CSAbstractValue value, String userDisplayName) {
        log.debug("method update(CSAbstractValue value) called");

        if (!(value instanceof ExportAValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            ExportAValue exportAValue = (ExportAValue) value;
            ExportA exportA = home.findByKeyAndVersion(exportAValue.getId(), exportAValue.getVersion());

            log.debug("update() - start updating.....");
            exportA.setCourtClerkExport(exportAValue.getCourtClerkName());
            exportA.setStatusFlag(exportAValue.getStatusFlag());
            exportA.setLinkedHearingId(exportAValue.getLinkedHearingID());
            exportA.setHearingId(exportAValue.getHearingID());
            exportA.setUpdated(userDisplayName);
            log.debug("update(ExportAValue) finished");
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
     * @return ExportA
     * @exception FinderException
     *                throws EJBException
     */
    public ExportA findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        log.debug("method findByPrimaryKey(id) called");
        try {

            ExportA exportA = home.findByPrimaryKey(id);
            log.debug("Returning ExportAValue from findByPrimaryKey(id)");

            return exportA;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw (ObjectNotFoundException) e;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Get the local home for the EportA bean.
     * 
     * @return ExportA - the home
     */
    private ExportAHome getHome() {
        log.debug("method getHome() called, returning home");
        return home;
    }

    /**
     * 
     * @param hearingID
     * @param linkedHearingID
     * @return ExportAValue
     * @exception FinderException
     *                throws EJBException
     */
    public Collection findByHearingOrLinkedHearingID(Integer hearingID, Integer linkedHearingID)
            throws ObjectNotFoundException {
        log.debug("Method findByHearingOrLinkedHearingID(hearingID, linkedHearingID) called");
        Collection exportAs = null;

        try {
            exportAs = home.findByHearingLinkedHearingID(hearingID, linkedHearingID);
            log.debug("Returning Collection of ExportA's from findByHearingOrLinkedHearingID "
                    + "(hearingID, linkedHearingID)");

            return exportAs;

        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw (ObjectNotFoundException) e;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Create an ExportAValue from the ExportA entity.
     * 
     * @param ExportA
     * @return ExportAValue
     */
    public ExportAValue getExportAValue(ExportA local) {
        log.debug("method getExportAValue(ExportA local) called");
        ExportAValue exportAValue = new ExportAValue(local.getExportAId(), local.getVersion());
        setExportAValue(local, exportAValue);
        log.debug("Returning ExportAValue from getExportAValue(ExportA)");
        return exportAValue;
    }

    // ----------------------- Private Methods ---------------------------

    /**
     * Get all the values from the entity and copy them to the value.
     * 
     * @param local
     * @param exportAValue
     */
    private void setExportAValue(ExportA local, ExportAValue exportAValue) {
        log.debug("method setExportAValue(ExportA local, ExportAValue exportAValue) called");
        exportAValue.setHearingID(local.getHearingId());// tmp code - this
        // should be as above
        exportAValue.setCourtClerkName(local.getCourtClerkExport());
        exportAValue.setLinkedHearingID(local.getLinkedHearingId());
        exportAValue.setStatusFlag(local.getStatusFlag());
        log.debug("setExportAValue finished.");
    }
}