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
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;

/**
 * <p>
 * Title: RecipientMaintainer
 * </p>
 * <p>
 * Description: Maintainer class for Recipient
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
public class RecipientMaintainer extends AbstractEntityMaintainer {
    private RecipientHome home = null;

    public RecipientMaintainer() {
        if (home == null) {
            home = (RecipientHome) CSServices.getServiceLocator().getLocalHome(RecipientHome.class);
        }
    }

    /**
     * Deletes the corresponding recipient
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
            Recipient local = home.findByPrimaryKey(key);
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
     * Creates the corresponding document recipient
     * 
     * @param CSAbstractValue
     *            value
     * @return CSEntityLocal
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("create() called");

        if (!(value instanceof RecipientBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            RecipientBasicValue bv = (RecipientBasicValue) value;

            Recipient rec = home.create(bv.getRecipientName(), bv.getFaxNumber(), bv.getEmailAddress(),
                    bv.getCourtID(), bv.getPrefDistributionType(), bv.getPrefMimeType(), userDisplayName);

            return rec;
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    /**
     * Updates the corresponding recipient
     * 
     * @param CSAbstractValue
     *            value
     * @throws ObjectNotFoundException
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        log.debug("update() called");

        if (!(value instanceof RecipientBasicValue))
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());

        RecipientBasicValue bv = (RecipientBasicValue) value;

        try {
            // Find home
            log.debug("update() finding by primary key: " + bv.getId());
            Recipient recipient = home.findByPrimaryKey(bv.getId());

            if (bv.getVersion() != null && !bv.getVersion().equals(recipient.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug("update() updating");
                recipient.setEmailAddress(bv.getEmailAddress());
                recipient.setFaxNumber(bv.getFaxNumber());
                recipient.setRecipientName(bv.getRecipientName());
                recipient.setPrefDistributionType(bv.getPrefDistributionType());
                recipient.setPrefMimeType(bv.getPrefMimeType());
                recipient.setUpdated(userDisplayName);
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
     * @return Recipient entity
     * @throws ObjectNotFoundException
     */
    public Recipient findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
     * Find by courtId
     * 
     * @param courtId
     * @return Collection of Recipient local references.
     * @throws ObjectNotFoundException
     */
    public Collection findByCourtId(Integer courtId) {
        String methodName = "findByCourtId() - ";
        log.debug(methodName + "called :: courtId: " + courtId);

        try {
            Collection coll = home.findByCourtId(courtId);
            log.debug(methodName + "Entries found: " + coll.size());
            return coll;
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            throw new EJBException(f);
        }
    }

    /**
     * Create and return a Basic VO from local entity.
     * 
     * @param local
     * @return RecipientBasicValue
     */
    public RecipientBasicValue getRecipientBasicValue(Recipient local) {
        String methodName = "getRecipientBasicValue() - ";
        log.debug(methodName + "called");

        RecipientBasicValue value = createBasicVO(local);

        return value;
    }

    /**
     * @param locals
     * @return Collection of Basic VOs.
     */
    public Collection getRecipientBasicValues(Collection locals) {
        String methodName = "getRecipientBasicValues() - ";
        log.debug(methodName + "called");

        Vector v = new Vector();
        Iterator it = locals.iterator();
        while (it.hasNext()) {
            Recipient rec = (Recipient) it.next();
            log.debug(methodName + "creating Basic VO");
            RecipientBasicValue recbv = this.getRecipientBasicValue(rec);
            v.add(recbv);
        }
        return v;
    }

    /**
     * Private Methods
     */

    private RecipientBasicValue createBasicVO(Recipient local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        RecipientBasicValue recbv = null;
        if (local.getVersion() == null) {
            recbv = new RecipientBasicValue(local.getRecipientId(), new Integer(1));
        } else {
            recbv = new RecipientBasicValue(local.getRecipientId(), local.getVersion());
        }

        copyEntityPropsToVO(local, recbv);

        return recbv;
    }

    public RecipientComplexValue getRecipientComplexValue(Recipient local) {
        RecipientComplexValue value = new RecipientComplexValue(local.getRecipientId(), local.getVersion());
        copyEntityPropsToVO(local, value);
        return value;
    }

    public Collection getRecipientComplexValues(Collection locals) {

        if (locals == null)
            return null;
        Collection values = new Vector();
        Iterator it = locals.iterator();
        while (it.hasNext()) {
            values.add(getRecipientComplexValue((Recipient) it.next()));
        }
        return values;
    }

    private void copyEntityPropsToVO(Recipient local, RecipientBasicValue value) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        value.setRecipientId(local.getRecipientId());
        value.setCourtID(local.getCourtId());
        value.setEmailAddress(local.getEmailAddress());
        value.setFaxNumber(local.getFaxNumber());
        value.setRecipientName(local.getRecipientName());
        value.setPrefDistributionType(local.getPrefDistributionType());
        value.setPrefMimeType(local.getPrefMimeType());
    }
}