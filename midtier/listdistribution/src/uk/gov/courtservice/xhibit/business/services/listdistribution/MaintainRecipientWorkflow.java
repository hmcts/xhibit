package uk.gov.courtservice.xhibit.business.services.listdistribution;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;

/**
 * <p>
 * Title: MaintainRecipientWorkflow
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
 * @author Laurent Bossard
 * @version $Id: MaintainRecipientWorkflow.java,v 1.4 2005/08/26 11:27:12 szfnvt
 *          Exp $
 */
public class MaintainRecipientWorkflow {
    private WLLRecipientHelper wllRecipientHelper;

    private RecipientHelper recipientHelper;

    public MaintainRecipientWorkflow() {
        wllRecipientHelper = new WLLRecipientHelper();
        recipientHelper = new RecipientHelper();
    }

    /**
     * Returns a WLLRecipientComplexValue object
     * 
     * @param Integer
     *            wllRecipientID
     * @return WLLRecipientComplexValue
     */
    public WLLRecipientComplexValue findWLLRecipient(Integer wllRecipientID, String userDisplayName) throws MaintainRecipientException {
        return wllRecipientHelper.findWLLRecipient(wllRecipientID, userDisplayName);
    }

    /**
     * Returns a RecipientComplexValue object
     * 
     * @param Integer
     *            recipientID
     * @param String
     *            documentType
     * @return RecipientComplexValue
     */
    public RecipientComplexValue findRecipient(Integer recipientID, String documentType, String userDisplayName)
            throws MaintainRecipientException {
        return recipientHelper.findRecipient(recipientID, documentType, userDisplayName);
    }

    /**
     * Returns a RecipientBasicValue object
     * 
     * @param Integer
     *            recipientID
     * @return RecipientBasicValue
     */
    public RecipientBasicValue findRecipient(Integer recipientID) throws MaintainRecipientException {
        return recipientHelper.findRecipient(recipientID);
    }

    /**
     * Returns a Collection of RecipientBasicValue objects
     * 
     * @param String
     *            documentType
     * @param Integer
     *            courtID
     * @return Collection
     */
    public Collection findRecipientsByDocumentType(String documentType, Integer courtID, String userDisplayName) {
        return recipientHelper.findRecipientsByDocumentType(documentType, courtID, userDisplayName);
    }

    /**
     * Returns a Collection of WLLRecipientBasicValue objects if document type
     * is WLL, of RecipientValue objects otherwise
     * 
     * @param Integer
     *            courtID
     * @return Collection
     */
    public Collection findWLLRecipients(Integer courtID, String userDisplayName) throws MaintainRecipientException {
        return wllRecipientHelper.findWLLRecipients(courtID, userDisplayName);
    }

    /**
     * Remove WLL Recipients from the distribution - does not delete the
     * recipient from the system, only their subscription to the letter.
     * 
     * @param WLLRecipientBasicValue
     *            wllObject
     */
    public void removeWLLRecipients(WLLRecipientComplexValue[] wllObjects) throws MaintainRecipientException {
        for (int i = 0; i < wllObjects.length; i++) {
            wllRecipientHelper.removeWLLRecipient(wllObjects[i]);
        }
    }

    /**
     * Unsubscribe these recipients from the corresponding list
     * 
     * @param RecipientComplexValue
     *            object
     * @param String
     *            documentType
     */
    public void removeRecipientsFromList(RecipientComplexValue[] objects, String documentType)
            throws MaintainRecipientException {
        for (int i = 0; i < objects.length; i++) {
            recipientHelper.removeRecipientFromList(objects[i], documentType);
        }
    }

    /**
     * Remove these recipients from XHIBIT (together with the subsriptions if
     * any)
     * 
     * @param RecipientBasicValue
     *            object
     */
    public void removeRecipients(RecipientBasicValue[] objects) throws MaintainRecipientException {
        for (int i = 0; i < objects.length; i++) {
            recipientHelper.removeRecipient(objects[i]);
        }

    }

    /**
     * Add Recipient to XHIBIT and to the given list if required
     */
    public RecipientBasicValue addRecipient(RecipientBasicValue recObject, String userDisplayName) {
        return recipientHelper.addRecipient(recObject, userDisplayName);
    }

    /**
     * Add Recipients to XHIBIT and to the given list if required
     * 
     * @param RecipientComplexValue
     *            recObject
     * @param String
     *            documentType
     * @return RecipientComplexValue
     */
    public RecipientComplexValue[] addRecipientsToList(RecipientComplexValue[] recObjects, String userDisplayName)
            throws MaintainRecipientException {
        RecipientComplexValue[] returnValues = new RecipientComplexValue[recObjects.length];

        for (int i = 0; i < recObjects.length; i++) {
            returnValues[i] = recipientHelper.addRecipientToList(recObjects[i], userDisplayName);
        }

        return returnValues;
    }

    /**
     * Add WLL Recipients to XHIBIT (found from look up in Reference data).
     * Subscribes if required.
     * 
     * @param WLLRecipientComplexValue
     *            recObject
     * @return WLLRecipientComplexValue
     */
    public WLLRecipientComplexValue[] addWLLRecipients(WLLRecipientComplexValue[] recObjects, String userDisplayName)
            throws MaintainRecipientException {
        WLLRecipientComplexValue[] returnValues = new WLLRecipientComplexValue[recObjects.length];

        for (int i = 0; i < recObjects.length; i++) {
            returnValues[i] = wllRecipientHelper.addWLLRecipient(recObjects[i], userDisplayName);
        }

        return returnValues;
    }

    /**
     * Update the recipient details in XHIBIT.
     * 
     * @param RecipientComplexValue
     *            recObject
     */
    public void updateRecipient(RecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        recipientHelper.updateRecipient(recObject, userDisplayName);
    }

    /**
     * Update the WLL recipient details in XHIBIT.
     * 
     * @param WLLRecipientComplexValue
     *            recObject
     */
    public void updateWLLRecipient(WLLRecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        wllRecipientHelper.updateWLLRecipient(recObject, userDisplayName);
    }

    /**
     * Returns a collection of RecipientBasicValue objects corresponding to the
     * recipients not listed against the document type passed as parameter
     * 
     * @param String
     *            documentType
     * @param Integer
     *            courtID
     * @return Collection
     */
    public Collection findRecipientsNotOnList(String documentType, Integer courtID) {
        return recipientHelper.findRecipientsNotOnList(documentType, courtID);
    }

    /**
     * Returns a collection of WLLRecipientBasicValue objects corresponding to
     * the recipients with no WLL subscription
     * 
     * @param Integer
     *            courtID
     * @return Collection
     */
    public Collection findWLLRecipientsNotOnList(Integer courtID) {
        return wllRecipientHelper.findWLLRecipientsNotOnList(courtID);
    }

    /**
     * Returns a WLLRecipientComplexValue containing the solicitor firm id
     * information
     */
    public WLLRecipientComplexValue findRefSolicitorFirmByCrestId(Integer crestSolicitorFirmId, Integer courtId)
            throws MaintainRecipientException {
        return wllRecipientHelper.findRefSolicitorFirmByCrestId(crestSolicitorFirmId, courtId);
    }

}