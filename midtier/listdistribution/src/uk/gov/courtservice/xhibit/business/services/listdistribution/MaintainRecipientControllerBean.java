package uk.gov.courtservice.xhibit.business.services.listdistribution;

import java.util.Collection;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;

/**
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @ejb.bean name="MaintainRecipientController" description="Maintain Recipient
 *           Controller Bean" type="Stateless" view-type="both"
 *           jndi-name="MaintainRecipientControllerHome"
 *           local-jndi-name="MaintainRecipientControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Laurent Bossard
 * @version $Id: MaintainRecipientControllerBean.java,v 1.4 2005/08/26 11:27:11
 *          szfnvt Exp $
 */
public class MaintainRecipientControllerBean extends CSSessionBean implements SessionBean {
    private MaintainRecipientWorkflow maintainRecipientWorkflow = new MaintainRecipientWorkflow();

    private MaintainRecipientWorkflow2 maintainRecipientWorkflow2 = new MaintainRecipientWorkflow2();

    private RecipientHelper recipientHelper = new RecipientHelper();

    /**
     * @ejb.interface-method view-type="remote"
     */
    public WLLRecipientComplexValue findWLLRecipient(Integer wllRecipientID, String userDisplayName) throws MaintainRecipientException {
        String methodName = "findWLLRecipient() - ";
        log.debug(methodName + " Supplied Param Value :: wllRecipientID :" + wllRecipientID);

        try {
            return maintainRecipientWorkflow.findWLLRecipient(wllRecipientID, userDisplayName);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public RecipientComplexValue findRecipient(Integer recipientID, String documentType, String userDisplayName)
            throws MaintainRecipientException {
        String methodName = "findRecipient() - ";
        log.debug(methodName + " Supplied Param Value :: recipientID : " + recipientID + ", documentType : "
                + documentType);

        try {
            return maintainRecipientWorkflow.findRecipient(recipientID, documentType, userDisplayName);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public Collection findRecipientsByDocumentType(String documentType, Integer courtID, String userDisplayName) {
        log.debug("findRecipientsByDocumentType() - Supplied Param Value :: documentType : " + documentType
                + ", courtID : " + courtID);
        return maintainRecipientWorkflow.findRecipientsByDocumentType(documentType, courtID, userDisplayName);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public Collection findWLLRecipients(Integer courtID, String userDisplayName) throws MaintainRecipientException {
        String methodName = "findWLLRecipients() - ";
        log.debug(methodName + " Supplied Param Value :: courtID : " + courtID);

        try {
            return maintainRecipientWorkflow.findWLLRecipients(courtID, userDisplayName);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public void removeWLLRecipients(WLLRecipientComplexValue[] wllObjects) throws MaintainRecipientException {
        String methodName = "removeWLLRecipients() - ";
        log.debug(methodName + " Supplied Param Value :: WLLRecipientComplexValue[] : " + wllObjects);

        try {
            maintainRecipientWorkflow.removeWLLRecipients(wllObjects);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public void removeRecipientsFromList(RecipientComplexValue[] objects, String documentType)
            throws MaintainRecipientException {
        String methodName = "removeRecipientsFromList() - ";
        log.debug(methodName + " Supplied Param Value :: RecipientComplexValue : " + objects + ", documentType : "
                + documentType);

        try {
            maintainRecipientWorkflow.removeRecipientsFromList(objects, documentType);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public void removeRecipients(RecipientBasicValue[] objects) throws MaintainRecipientException {
        String methodName = "removeRecipients() - ";
        log.debug(methodName + " Supplied Param Value :: RecipientBasicValue : " + objects);

        try {
            maintainRecipientWorkflow.removeRecipients(objects);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public RecipientComplexValue[] addRecipientsToList(RecipientComplexValue[] recObjects, String userDisplayName)
            throws MaintainRecipientException {
        log.debug("addRecipientsToList() - Supplied Param Value :: RecipientComplexValue : " + recObjects);
        return maintainRecipientWorkflow.addRecipientsToList(recObjects, userDisplayName);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public RecipientBasicValue addRecipient(RecipientBasicValue recObject, String userDisplayName) {
        log.debug("addRecipient() - Supplied Param Value :: RecipientBasicValue : " + recObject);
        return maintainRecipientWorkflow.addRecipient(recObject, userDisplayName);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public WLLRecipientComplexValue[] addWLLRecipients(WLLRecipientComplexValue[] recObjects, String userDisplayName)
            throws MaintainRecipientException {
        log.debug("addWLLRecipients() - Supplied Param Value :: WLLRecipientComplexValue : " + recObjects);
        return maintainRecipientWorkflow.addWLLRecipients(recObjects, userDisplayName);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public RecipientBasicValue updateRecipient(RecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        String methodName = "updateRecipient() - ";
        log.debug(methodName + " Supplied Param Value :: RecipientComplexValue : " + recObject);

        try {
            maintainRecipientWorkflow2.updateRecipient(recObject, userDisplayName);
            return refresh((recObject.getRecipientId()));
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * Update the Recipient information specified by the value, note the
     * returned values do not contain the value updated by DB triggers!
     * 
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public void updateRecipientPersist(RecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        recipientHelper.updateRecipient(recObject, userDisplayName);
    }

    private RecipientBasicValue refresh(Integer recipientId) throws MaintainRecipientException {
        return maintainRecipientWorkflow.findRecipient(recipientId);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public void updateWLLRecipient(WLLRecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        String methodName = "updateWLLRecipient() - ";
        log.debug(methodName + " Supplied Param Value :: WLLRecipientComplexValue : " + recObject);

        try {
            maintainRecipientWorkflow.updateWLLRecipient(recObject, userDisplayName);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public Collection findRecipientsNotOnList(String documentType, Integer courtID) {
        log.debug("findRecipientsNotOnList() -  Supplied Param Value :: documentType : " + documentType
                + ", courtID : " + courtID);
        return maintainRecipientWorkflow.findRecipientsNotOnList(documentType, courtID);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public Collection findWLLRecipientsNotOnList(Integer courtID) {
        log.debug("findWLLRecipientsNotOnList() - Supplied Param Value :: courtID : " + courtID);
        return maintainRecipientWorkflow.findWLLRecipientsNotOnList(courtID);
    }

    /**
     * @ejb.interface-method view-type="remote"
     */
    public WLLRecipientComplexValue findRefSolicitorFirmByCrestId(Integer crestSolicitorFirmId, Integer courtId)
            throws MaintainRecipientException {
        String methodName = "findRefSolicitorFirmByCrestId() - ";
        log.debug(methodName + " Supplied Param Value :: crestSolicitorFirmId : " + crestSolicitorFirmId + "courtId : "
                + courtId);

        try {
            return maintainRecipientWorkflow.findRefSolicitorFirmByCrestId(crestSolicitorFirmId, courtId);
        } catch (MaintainRecipientException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            log.debug(methodName + " Transaction ROLLBACK ");
            throw e;
        }
    }
}
