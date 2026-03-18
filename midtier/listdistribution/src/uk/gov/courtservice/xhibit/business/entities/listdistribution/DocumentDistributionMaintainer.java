package uk.gov.courtservice.xhibit.business.entities.listdistribution;

// Framework

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;

/**
 * <p>
 * Title: DocumentDistributionMaintainer
 * </p>
 * <p>
 * Description: Maintainer class for DocumentDistribution
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Laurent Bossard
 */

public class DocumentDistributionMaintainer extends AbstractEntityMaintainer {

    private DocumentDistributionHome home = null;

    public DocumentDistributionMaintainer() {
        if (home == null) {
            home = (DocumentDistributionHome) CSServices.getServiceLocator().getLocalHome(
                    DocumentDistributionHome.class);
        }
    }

    /**
     * Deletes the corresponding document distribution entry
     * 
     * @param Integer
     *            key
     * @param Integer
     *            version
     * @throws ObjectNotFoundException
     */
    public void delete(Integer key, Integer version) throws ObjectNotFoundException {
        log.debug("delete() called");

        try {
            DocumentDistribution local = home.findByPrimaryKey(key);
            if (!local.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.remove();
                log.debug("exited - OK");
            }
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            if (e instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) e;
            else
                throw new EJBException(e);
        } catch (RemoveException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Creates the corresponding document distribution entry
     * 
     * @param CSAbstractValue
     *            value
     * @return CSEntityLocal
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("create() called");

        if (!(value instanceof DocumentDistributionBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            DocumentDistributionBasicValue bv = (DocumentDistributionBasicValue) value;
            return home.create(bv.getDistributionType(), bv.getDocumentType(), bv.getMimeType(), bv.getCourtId(), bv
                    .getRecipientID(), bv.getWllRecipientID(), bv.getUsePrefDistType(), userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    /**
     * Updates the corresponding document distribution entry
     * 
     * @param CSAbstractValue
     *            value
     * @throws ObjectNotFoundException
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        log.debug("update() called");

        if (!(value instanceof DocumentDistributionBasicValue))
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());

        DocumentDistributionBasicValue bv = (DocumentDistributionBasicValue) value;

        try {
            // Find home
            log.debug("update() finding by primary key: " + bv.getId());
            DocumentDistribution docDist = home.findByPrimaryKey(bv.getId());

            if (!bv.getVersion().equals(docDist.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug("update() updating");
                docDist.setDistributionType(bv.getDistributionType());
                docDist.setDocumentType(bv.getDocumentType());
                docDist.setMimeType(bv.getMimeType());
                docDist.setUsePrefDistType(bv.getUsePrefDistType());
                docDist.setUpdated(userDisplayName);
            }
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Find By Primary Key
     * 
     * @param id
     * @return DocumentDistribution entity
     * @throws ObjectNotFoundException
     */
    public DocumentDistribution findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        String methodName = "findByPrimaryKey() - ";
        log.debug(methodName + "called :: id: " + id);

        try {
            return home.findByPrimaryKey(id);
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        }
    }

    /**
     * Find by recipientId
     * 
     * @param recipientId
     * @return Collection of DocumentDistribution local references.
     * @throws ObjectNotFoundException
     */
    public Collection findByRecipientId(Integer recipientId) throws ObjectNotFoundException {
        String methodName = "findByRecipientId() - ";
        log.debug(methodName + "called :: recipientId: " + recipientId);

        try {
            Collection coll = home.findByRecipientId(recipientId);
            int size = (coll != null) ? coll.size() : -1;
            log.debug(methodName + "Entries found: " + size);
            return coll;
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        }
    }

    /**
     * Find by wllRecipientId
     * 
     * @param wllRecipientId
     * @return Collection of DocumentDistribution local references.
     * @throws ObjectNotFoundException
     */
    public DocumentDistribution findByWllRecipientId(Integer wllRecipientId) throws ObjectNotFoundException {
        String methodName = "findByWllRecipientId() - ";
        log.debug(methodName + "called :: wllRecipientId: " + wllRecipientId);

        try {
            return home.findByWllRecipientId(wllRecipientId);
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        }
    }

    /**
     * Find by recipientId and documentType
     * 
     * @param recipientId
     * @return Collection of DocumentDistribution local references.
     * @throws ObjectNotFoundException
     */
    public DocumentDistribution findByRecipientIdAndDocumentType(Integer recipientId, String documentType)
            throws ObjectNotFoundException {
        String methodName = "findByRecipientIdAndDocumentType() - ";
        log.debug(methodName + "called :: recipientId: " + recipientId + ", documentType: " + documentType);

        try {
            return home.findByRecipientIdAndDocumentType(recipientId, documentType);
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        }
    }

    /**
     * Create and return a Basic VO from local entity.
     * 
     * @param local
     * @return DocumentDistributionBasicValue
     */
    public DocumentDistributionBasicValue getDocumentDistributionBasicValue(DocumentDistribution local, String userDisplayName) {
        String methodName = "getDocumentDistributionBasicValue() - ";
        log.debug(methodName + "called");

        DocumentDistributionBasicValue value = createBasicVO(local);

        return value;
    }

    /**
     * @param locals
     * @return Collection of Basic VOs.
     */
    public Collection getDocumentDistributionBasicValues(Collection locals, String userDisplayName) {
        String methodName = "getDocumentDistributionBasicValues() - ";
        log.debug(methodName + "called");

        Vector v = new Vector();
        Iterator it = locals.iterator();
        while (it.hasNext()) {
            DocumentDistribution dd = (DocumentDistribution) it.next();
            log.debug(methodName + "creating Basic VO");
            DocumentDistributionBasicValue ddbv = this.getDocumentDistributionBasicValue(dd, userDisplayName);
            v.add(ddbv);
        }
        return v;
    }

    /**
     * Private Methods
     */

    private DocumentDistributionBasicValue createBasicVO(DocumentDistribution local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        DocumentDistributionBasicValue ddbv = null;
        if (local.getVersion() == null) {
            ddbv = new DocumentDistributionBasicValue(local.getDocDistributionId(), new Integer(1));
        } else {
            ddbv = new DocumentDistributionBasicValue(local.getDocDistributionId(), local.getVersion());
        }

        copyEntityPropsToVO(local, ddbv);

        return ddbv;
    }

    private void copyEntityPropsToVO(DocumentDistribution local, DocumentDistributionBasicValue value) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        value.setCourtId(local.getCourtId());
        value.setDistributionType(local.getDistributionType());
        value.setDocumentType(local.getDocumentType());
        value.setMimeType(local.getMimeType());
        value.setRecipientID(local.getRecipientId());
        value.setWllRecipientID(local.getWllRecipientId());
        value.setDocumentDistributionId(local.getDocDistributionId());
        value.setUsePrefDistType(local.getUsePrefDistType());
    }
}