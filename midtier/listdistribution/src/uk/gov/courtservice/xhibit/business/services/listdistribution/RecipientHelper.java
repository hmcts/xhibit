package uk.gov.courtservice.xhibit.business.services.listdistribution;

// jdk

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.DocumentDistribution;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.DocumentDistributionMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.Recipient;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.RecipientMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;

/**
 * <p>
 * Title: RecipientHelper
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
 * @version $Id: RecipientHelper.java,v 1.7 2014/06/20 16:57:09 atwells Exp $
 * @editor Sarah Tong
 */

public class RecipientHelper {
    // Maintainers
    private RecipientMaintainer recMaintainer;

    private DocumentDistributionMaintainer docDistMaintainer;

    // Error keys
    private static final String RECIPIENT_NOT_FOUND = "listdistribution.recipientnotfound";

    private static final String DOC_DIST_NOT_FOUND = "listdistribution.docdistnotfound";

    // private static final String REC_FOR_COURT_NOT_FOUND =
    // "listdistribution.recforcourtnotfound";
    // private static final String REC_FOR_DOC_TYPE_NOT_FOUND =
    // "listdistribution.recfordoctypenotfound";
    // private static final String NO_DOC_DIST_FOR_RECIPIENT =
    // "listdistribution.nodocdistforrecipient";

    // Document types
    // private static final String DOC_TYPE_DAILY_LIST_PRISON = "DLP";
    // private static final String DOC_TYPE_DAILY_LIST = "DL";

    public RecipientHelper() {
        recMaintainer = new RecipientMaintainer();
        docDistMaintainer = new DocumentDistributionMaintainer();
    }

    /**
     * Returns a RecipientComplexValue object containing only the document
     * distribution value for the specified documentType
     * 
     * @param Integer
     *            recipientID
     * @param String
     *            documentType
     * @return RecipientComplexValue
     */

    public RecipientComplexValue findRecipient(Integer recipientID, String documentType, String userDisplayName)
            throws MaintainRecipientException {
        try {
            Recipient recObject = recMaintainer.findByPrimaryKey(recipientID);

            RecipientComplexValue recComplexObject = recMaintainer.getRecipientComplexValue(recObject);

            Collection docDistColl = recObject.getDocumentDistribution();

            // set the distribution for the relevant document type
            recComplexObject.setDocumentDistribution(getDocDistForDocType(documentType, docDistColl, userDisplayName));

            return recComplexObject;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new MaintainRecipientException(RECIPIENT_NOT_FOUND, "Couldn't find recipient for recipient id = "
                    + recipientID, e);
        }

    }

    /**
     * Update the recipient details in XHIBIT.
     * 
     * @param RecipientComplexValue
     *            recObject
     */
    public void updateRecipient(RecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        if (recObject != null) {
            try {
                // if preferred distributions have been updated, update all
                // other
                // distributions using them
                checkUpdatePrefDistribution(recObject, userDisplayName);

                // update this recipient
                recMaintainer.update(recObject, userDisplayName);
            } catch (ObjectNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new MaintainRecipientException(RECIPIENT_NOT_FOUND, "Couldn't find recipient for recipient id = "
                        + recObject.getId(), e);
            }

            // update the distribution
            Collection docColl = recObject.getDocumentDistribution();
            updateDistribution(docColl, userDisplayName);
        }

    }

    /**
     * Returns a RecipientBasicValue object
     * 
     * @param Integer
     *            recipientID
     * @return RecipientComplexValue
     */
    public RecipientBasicValue findRecipient(Integer recipientID) throws MaintainRecipientException {
        try {
            Recipient recObject = recMaintainer.findByPrimaryKey(recipientID);

            RecipientBasicValue recBasicObject = recMaintainer.getRecipientBasicValue(recObject);

            return recBasicObject;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new MaintainRecipientException(RECIPIENT_NOT_FOUND, "Couldn't find recipient for recipient id = "
                    + recipientID, e);
        }
    }

    /**
     * Returns a Collection of <code>RecipientComplexValue</code> objects
     * 
     * @param String
     *            documentType
     * @param Integer
     *            courtID
     * @return Collection
     */
    public Collection findRecipientsByDocumentType(String documentType, Integer courtID, String userDisplayName) {
        Vector v = new Vector();

        /**
         * @todo: Write a custom finder, findByCourtIdAndDocType to avoid the
         *        unnecessary looping
         */
        Collection recColl = recMaintainer.findByCourtId(courtID);
        Iterator iter = recColl.iterator();

        while (iter.hasNext()) // for each Recipient
        {
            Recipient recObject = (Recipient) iter.next();

            Collection distColl = recObject.getDocumentDistribution();

            Iterator distIter = distColl.iterator();
            while (distIter.hasNext()) // for each distribution
            {
                DocumentDistribution docObject = (DocumentDistribution) distIter.next();
                if (docObject.getDocumentType().equals(documentType)) {
                    // create the complex value (inc this distribution) and
                    // add to the
                    // Vector of values to return
                    RecipientComplexValue recCompVal = recMaintainer.getRecipientComplexValue(recObject);
                    DocumentDistributionBasicValue docDistBasicVal = docDistMaintainer
                            .getDocumentDistributionBasicValue(docObject, userDisplayName);

                    Vector docDists = new Vector();
                    docDists.add(docDistBasicVal);
                    recCompVal.setDocumentDistribution(docDists);

                    v.addElement(recCompVal);
                    break; // we've found the distribution we want so no
                    // need to continue looping
                }
            }
        }

        return v;
    }

    /**
     * Unsubscribe this particular recipient from the corresponding list(s)
     * 
     * @param RecipientComplexValue
     *            object
     * @param String
     *            documentType
     */
    public void removeRecipientFromList(RecipientComplexValue object, String documentType)
            throws MaintainRecipientException {
        Collection docColl = object.getDocumentDistribution();

        if (docColl != null) {
            Iterator iter = docColl.iterator();
            loop: while (iter.hasNext()) // for each document
            // distribution
            {
                DocumentDistributionBasicValue docObject = (DocumentDistributionBasicValue) iter.next();
                if (docObject.getDocumentType().equals(documentType)) {
                    try {
                        docDistMaintainer.delete(docObject.getId(), docObject.getVersion());
                        break loop;
                    } catch (ObjectNotFoundException e) {
                        CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                        throw new MaintainRecipientException(DOC_DIST_NOT_FOUND,
                                "Couldn't find document distribution for doc distribution id = " + docObject.getId(), e);
                    }
                }
            }
        }
    }

    /**
     * Remove this recipient from XHIBIT (together with the subsriptions if any)
     * 
     * @param RecipientBasicValue
     *            object
     */
    public void removeRecipient(RecipientBasicValue object) throws MaintainRecipientException {
        if (object != null) {
            Integer recipientId = object.getId();

            // remove subscriptions
            try {
                Collection docColl = docDistMaintainer.findByRecipientId(recipientId);
                Iterator iter = docColl.iterator();

                while (iter.hasNext()) // for each document distribution
                {
                    DocumentDistribution docObject = (DocumentDistribution) iter.next();
                    try {
                        docDistMaintainer.delete(docObject.getDocDistributionId(), docObject.getVersion());
                    } catch (ObjectNotFoundException e) {
                        CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                        throw new MaintainRecipientException(DOC_DIST_NOT_FOUND,
                                "Couldn't find document distribution for doc distribution id = "
                                        + docObject.getDocDistributionId(), e);
                    }
                }
            } catch (ObjectNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new MaintainRecipientException(DOC_DIST_NOT_FOUND,
                        "Couldn't find document distribution for recipient id = " + object.getId(), e);
            }

            // remove the recipient
            try {
                recMaintainer.delete(object.getId(), object.getVersion());
            } catch (ObjectNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new MaintainRecipientException(RECIPIENT_NOT_FOUND, "Couldn't find recipient for recipient id = "
                        + object.getId(), e);
            }

        }
    }

    /**
     * Add Recipient to XHIBIT and to the given list if required
     * 
     * @param RecipientComplexValue
     *            recObject
     * @param String
     *            documentType
     * @return RecipientComplexValue
     */
    public RecipientComplexValue addRecipientToList(RecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        String documentType = null;
        Recipient recipient = null;
        DocumentDistributionBasicValue docDistObj = null;

        if (recObject == null)
            return null;

        try {
            recipient = recMaintainer.findByPrimaryKey(recObject.getId());
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new MaintainRecipientException(RECIPIENT_NOT_FOUND, "Couldn't find recipient for recipient id = "
                    + recObject.getId(), e);
        }

        Collection coll = recObject.getDocumentDistribution();

        if (coll != null && coll.size() == 1) {
            Iterator iter = coll.iterator();
            docDistObj = (DocumentDistributionBasicValue) iter.next();

            DocumentDistribution docDist = (DocumentDistribution) docDistMaintainer.create(docDistObj, userDisplayName);
            docDist.setRecipient(recipient);
        }

        /**
         * @todo: Do we need to call this method? We already have the recipient
         *        just create the ocmplex value here. Needs creating again so we
         *        have the ids/
         */

        return findRecipient(recObject.getId(), documentType, userDisplayName);
    }

    /**
     * Add Recipient to XHIBIT
     * 
     * @param RecipientBasicValue
     *            recObject
     */
    public RecipientBasicValue addRecipient(RecipientBasicValue recObject, String userDisplayName) {
        if (recObject == null)
            return null;

        Recipient recipient = (Recipient) recMaintainer.create(recObject, userDisplayName);

        RecipientBasicValue recBasicValue = recMaintainer.getRecipientBasicValue(recipient);

        return recBasicValue;
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
        Collection recColl = null; // Recipient local references
        Vector recipientVector = new Vector(); // the recipients to return
        RecipientMaintainer _maintainer = new RecipientMaintainer();

        if (documentType == null || "".equals(documentType) || courtID == null)
            return recipientVector;

        recColl = recMaintainer.findByCourtId(courtID);

        if (recColl.size() == 0)
            return recipientVector;

        Iterator iter = recColl.iterator();

        loop: while (iter.hasNext()) // for each recipient
        {
            Recipient recipient = (Recipient) iter.next();
            Collection docColl = recipient.getDocumentDistribution();

            // if this recipient is subscribed to something, check that it's
            // not
            // the document type passed in
            if (docColl.size() > 0) {
                Iterator iterDocColl = docColl.iterator();
                while (iterDocColl.hasNext()) {
                    DocumentDistribution docObject = (DocumentDistribution) iterDocColl.next();
                    if (docObject.getDocumentType().equals(documentType)) {
                        // don't add to the return collection, go on to the next
                        // recipient
                        continue loop;
                    }
                }
            }

            RecipientBasicValue recBasicObject = _maintainer.getRecipientBasicValue(recipient);
            recipientVector.addElement(recBasicObject);
        }

        return recipientVector;
    }

    // ---------------------------- Private Methods
    // ----------------------------//
    private Vector getDocDistForDocType(String documentType, Collection docDistColl, String userDisplayName) {
        Vector v = new Vector();

        Iterator iter = docDistColl.iterator();
        while (iter.hasNext()) {
            DocumentDistribution docDistObject = (DocumentDistribution) iter.next();

            if (docDistObject.getDocumentType().equals(documentType)) {
                v.addElement(docDistMaintainer.getDocumentDistributionBasicValue(docDistObject, userDisplayName));
                // after finding the distribution record for the specified doc
                // type
                // stop, no need to continue looping round the remaining records
                break;
            }
        }

        return v;
    }

    private void updateDistribution(Collection docColl, String userDisplayName) throws MaintainRecipientException {
        if (docColl != null && docColl.size() > 0) {
            Iterator iter = docColl.iterator();
            while (iter.hasNext()) // for each distribution basic value
            {
                DocumentDistributionBasicValue docObject = (DocumentDistributionBasicValue) iter.next();
                try {
                    docDistMaintainer.update(docObject, userDisplayName);
                } catch (ObjectNotFoundException e) {
                    CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                    throw new MaintainRecipientException(DOC_DIST_NOT_FOUND,
                            "Couldn't find document distribution for document distribution id = " + docObject.getId(),
                            e);
                }
            }
        }
    }

    private void checkUpdatePrefDistribution(RecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        Recipient recipient;

        // compare the prefDistType and prefMimeType on the object to the entity
        try {
            recipient = recMaintainer.findByPrimaryKey(recObject.getRecipientId());
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new MaintainRecipientException(RECIPIENT_NOT_FOUND, "Couldn't find recipient for recipient id = "
                    + recObject.getRecipientId(), e);
        }

        // if either is different
        if (!recipient.getPrefDistributionType().equals(recObject.getPrefDistributionType())
                || !recipient.getPrefMimeType().equals(recObject.getPrefMimeType())) {
            // find all distributions for this recipient
            Collection distributions = recipient.getDocumentDistribution();
            Iterator it = distributions.iterator();

            while (it.hasNext()) // for each distribution
            {
                // if using the pref distriubtion, update the record
                DocumentDistribution dist = (DocumentDistribution) it.next();
                if (dist.getUsePrefDistType().equals("Y")) {
                    DocumentDistributionBasicValue distValue = docDistMaintainer
                            .getDocumentDistributionBasicValue(dist, userDisplayName);
                    // only one may have changed but no harm updating both,
                    // saves on
                    // if statements
                    distValue.setDistributionType(recObject.getPrefDistributionType());
                    distValue.setMimeType(recObject.getPrefMimeType());
                    try {
                        docDistMaintainer.update(distValue, userDisplayName);
                    } catch (ObjectNotFoundException e) {
                        CSServices.getDefaultErrorHandler().handleError(e, getClass());
                        // no good reason why this shouldn't be found, we've
                        // just looked it
                        // up within the same transaction so not a business
                        // exception
                        throw new EJBException(e);
                    }
                }
            }
        }
    }

}