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
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;

/**
 * <p>
 * Title: WLLRecipientMaintainer
 * </p>
 * <p>
 * Description: Maintainer class for WLLRecipient
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

public class WLLRecipientMaintainer extends AbstractEntityMaintainer {

    private WLLRecipientHome home = null;

    private DocumentDistributionMaintainer docDistMaintainer;

    public WLLRecipientMaintainer() {
        if (home == null) {
            home = (WLLRecipientHome) CSServices.getServiceLocator().getLocalHome(WLLRecipientHome.class);
        }
        docDistMaintainer = new DocumentDistributionMaintainer();
    }

    /**
     * Deletes the corresponding wllRecipient
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
            WLLRecipient local = home.findByPrimaryKey(key);
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
     * Creates the corresponding wllRecipient
     * 
     * @param CSAbstractValue
     *            value
     * @return CSEntityLocal
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        log.debug("create() called");

        if (!(value instanceof WLLRecipientBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            WLLRecipientBasicValue bv = (WLLRecipientBasicValue) value;
            return home.create(bv.getCrestSolicitorFirmID(), bv.getSolicitorFirmName(), bv.getSolicitorFirmAddress(),
                    bv.getSolicitorFirmFax(), bv.getSolicitorFirmEmail(), bv.getCourtID(), userDisplayName);

        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    /**
     * Updates the corresponding wllRecipient
     * 
     * @param CSAbstractValue
     *            value
     * @throws ObjectNotFoundException
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        log.debug("update() called");

        if (!(value instanceof WLLRecipientBasicValue))
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());

        WLLRecipientBasicValue bv = (WLLRecipientBasicValue) value;

        try {
            // Find home
            log.debug("update() finding by primary key: " + bv.getId());
            WLLRecipient wllRecipient = home.findByPrimaryKey(bv.getId());

            if (!bv.getVersion().equals(wllRecipient.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug("update() updating");
                wllRecipient.setCrestSolicitorFirmId(bv.getCrestSolicitorFirmID());
                wllRecipient.setSolicitorFirmEmail(bv.getSolicitorFirmEmail());
                wllRecipient.setSolicitorFirmFax(bv.getSolicitorFirmFax());
                wllRecipient.setSolicitorFirmName(bv.getSolicitorFirmName());
                wllRecipient.setSolictiorFirmAddress(bv.getSolicitorFirmAddress());
                wllRecipient.setUpdated(userDisplayName);
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
     * @return WLLRecipient entity
     * @throws ObjectNotFoundException
     */
    public WLLRecipient findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
     * @return Collection of WLLRecipient local references.
     * @throws ObjectNotFoundException
     */
    public Collection findByCourtId(Integer courtId) throws ObjectNotFoundException {
        String methodName = "findByCourtId() - ";
        log.debug(methodName + "called :: courtId: " + courtId);

        try {
            Collection coll = home.findByCourtId(courtId);
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
     * Find by courtId and ref solicitor firm id
     * 
     * @param courtId
     * @param refSolId
     * @return Recipient local references.
     * @throws ObjectNotFoundException
     */
    public WLLRecipient findByCourtIdAndRefSolId(Integer courtId, Integer refSolId) throws ObjectNotFoundException {
        String methodName = "findByCourtIdAndRefSolId() - ";
        log.debug(methodName + "called :: courtId: " + courtId + ", refSolId : " + refSolId);

        try {
            return home.findByCourtIdAndRefSolId(courtId, refSolId);
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
     * @return WLLRecipientBasicValue
     */
    public WLLRecipientBasicValue getWLLRecipientBasicValue(WLLRecipient local) {
        String methodName = "getWLLRecipientBasicValue() - ";
        log.debug(methodName + "called");

        WLLRecipientBasicValue value = createBasicVO(local);

        return value;
    }

    /**
     * @param locals
     * @return Collection of Basic VOs.
     */
    public Collection getWLLRecipientBasicValues(Collection locals) {
        String methodName = "getWLLRecipientBasicValues() - ";
        log.debug(methodName + "called");

        Vector v = new Vector();
        Iterator it = locals.iterator();
        while (it.hasNext()) {
            WLLRecipient rec = (WLLRecipient) it.next();
            log.debug(methodName + "creating Basic VO");
            WLLRecipientBasicValue recbv = this.getWLLRecipientBasicValue(rec);
            v.add(recbv);
        }
        return v;
    }

    /**
     * Private Methods
     */

    private WLLRecipientBasicValue createBasicVO(WLLRecipient local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        WLLRecipientBasicValue recbv = new WLLRecipientBasicValue(local.getWllRecipientId(), local.getVersion());

        copyEntityPropsToVO(local, recbv);

        return recbv;
    }

    private void copyEntityPropsToVO(WLLRecipient local, WLLRecipientBasicValue value) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        value.setCourtID(local.getCourtId());
        value.setCrestSolicitorFirmID(local.getCrestSolicitorFirmId());
        value.setSolicitorFirmAddress(local.getSolictiorFirmAddress());
        value.setSolicitorFirmEmail(local.getSolicitorFirmEmail());
        value.setSolicitorFirmFax(local.getSolicitorFirmFax());
        value.setSolicitorFirmName(local.getSolicitorFirmName());
        value.setWllRecipientId(local.getWllRecipientId());
    }

    public WLLRecipientComplexValue getWLLRecipientComplexValue(WLLRecipient local, String userDisplayName) {
        if (local.getDocumentDistribution() != null) {
            WLLRecipientComplexValue value = new WLLRecipientComplexValue(local.getWllRecipientId(), local.getVersion());
            value.setDocumentDistribution(docDistMaintainer.getDocumentDistributionBasicValue(local.getDocumentDistribution(), userDisplayName));
            setWLLRecipientBasicValue(value, local);
            return value;
        } else
            return null;
    }

    public Collection getWLLRecipientComplexValues(Collection locals, String userDisplayName) {

        if (locals == null)
            return null;
        Collection values = new Vector();
        Iterator it = locals.iterator();
        while (it.hasNext()) {
            WLLRecipientComplexValue wllRecComplexValue = getWLLRecipientComplexValue((WLLRecipient) it.next(), userDisplayName);
            if (wllRecComplexValue != null)
                values.add(wllRecComplexValue);
        }
        return values;
    }

    private void setWLLRecipientBasicValue(WLLRecipientBasicValue value, WLLRecipient local) {
        value.setCourtID(local.getCourtId());
        value.setCrestSolicitorFirmID(local.getCrestSolicitorFirmId());
        value.setSolicitorFirmAddress(local.getSolictiorFirmAddress());
        value.setSolicitorFirmEmail(local.getSolicitorFirmEmail());
        value.setSolicitorFirmFax(local.getSolicitorFirmFax());
        value.setSolicitorFirmName(local.getSolicitorFirmName());
        value.setWllRecipientId(local.getWllRecipientId());
    }

}