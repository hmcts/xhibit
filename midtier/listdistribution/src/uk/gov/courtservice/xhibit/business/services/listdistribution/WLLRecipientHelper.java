package uk.gov.courtservice.xhibit.business.services.listdistribution;

import java.util.Collection;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.listdistribution.WLLRecipientNotOnListQuery;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.DocumentDistribution;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.DocumentDistributionMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.WLLRecipient;
import uk.gov.courtservice.xhibit.business.entities.listdistribution.WLLRecipientMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSolicitorFirmCriteria;

/**
 * <p>
 * Title: WLLRecipientHelper
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
 * @author Laurent Bossard,Sarah Tong
 * @version $Id: WLLRecipientHelper.java,v 1.8 2014/06/20 16:57:09 atwells Exp $
 * @todo This class still requires full review, commenting and refactoring
 */
public class WLLRecipientHelper {
    private static final Logger log = CSServices.getLogger(WLLRecipientHelper.class);

    // Maintainers
    private WLLRecipientMaintainer wllRecMaintainer;

    private DocumentDistributionMaintainer docDistMaintainer;

    // private RefSolicitorFirmMaintainer refSolMaintainer;
    private AddressMaintainer addressMaintainer;

    private static final String WLL_RECIPIENT_NOT_FOUND = "listdistribution.wllrecipientnotfound";

    private static final String DOC_DIST_NOT_FOUND = "listdistribution.docdistnotfound";

    private static final String WLL_REC_FOR_COURT_NOT_FOUND = "listdistribution.wllrecforcourtnotfound";

    // private static final String WLL_REC_FOR_DOC_TYPE_NOT_FOUND =
    // "listdistribution.wllrecfordoctypenotfound";
    // private static final String NO_DOC_DIST_FOR_WLL_RECIPIENT =
    // "listdistribution.nodocdistforwllrecipient";
    private static final String SOL_FIRM_NOT_FOUND = "listdistribution.nosolfirmfound";

    private static final String ADDRESS_NOT_FOUND = "listdistribution.noaddressfound";

    public WLLRecipientHelper() {
        wllRecMaintainer = new WLLRecipientMaintainer();
        docDistMaintainer = new DocumentDistributionMaintainer();
        // refSolMaintainer = new RefSolicitorFirmMaintainer();
        addressMaintainer = new AddressMaintainer();
    }

    /**
     * Returns a <code>WLLRecipientComplexValue</code> object
     * 
     * @param Integer
     *            wllRecipientID
     * @return WLLRecipientComplexValue
     */
    public WLLRecipientComplexValue findWLLRecipient(Integer wllRecipientID, String userDisplayName) throws MaintainRecipientException {
        try {
            WLLRecipient recObject = wllRecMaintainer.findByPrimaryKey(wllRecipientID);
            return wllRecMaintainer.getWLLRecipientComplexValue(recObject, userDisplayName);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new MaintainRecipientException(WLL_RECIPIENT_NOT_FOUND,
                    "Couldn't find wll recipient for recipient id = " + wllRecipientID, e);
        }
    }

    /**
     * Returns a Collection of WLLRecipientComplexValue objects if document type
     * is WLL, of RecipientValue objects otherwise
     * 
     * @param Integer
     *            courtID
     * @return Collection
     */
    public Collection findWLLRecipients(Integer courtID, String userDisplayName) throws MaintainRecipientException {
        try {
            Collection recColl = wllRecMaintainer.findByCourtId(courtID);

            if (recColl != null && recColl.size() > 0) {
                Collection recComplexValueColl = wllRecMaintainer.getWLLRecipientComplexValues(recColl, userDisplayName);

                if (recComplexValueColl != null && recComplexValueColl.size() > 0) {
                    return recComplexValueColl;
                }
            }

            return null;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new MaintainRecipientException(WLL_REC_FOR_COURT_NOT_FOUND,
                    "Couldn't find wll recipient for court id = " + courtID, e);
        }
    }

    /**
     * Remove WLL Recipient There is a one to one relationship between
     * WLLRecipient and DocumentDistribution. Removing the recipient from the
     * list will however not remove the recipient details (WLLRecipient) from
     * XHIBIT. This is so that should the recipient be added back to the
     * distribution (DocumentDistribution) their contact details will have been
     * preserved.
     * 
     * @param WLLRecipientBasicValue
     *            wllObject
     */
    public void removeWLLRecipient(WLLRecipientComplexValue object) throws MaintainRecipientException {
        if (object != null) {
            DocumentDistributionBasicValue docDistBV = object.getDocumentDistribution();

            try {
                log.debug("Deleting docDist " + docDistBV.getId());
                docDistMaintainer.delete(docDistBV.getId(), docDistBV.getVersion());
            } catch (ObjectNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new MaintainRecipientException(DOC_DIST_NOT_FOUND,
                        "Couldn't find document distribution for doc distribution id = " + docDistBV.getId(), e);
            }
        }
    }

    /**
     * Add WLL Recipient to XHIBIT (found from look up in Reference data). and
     * subscribes.
     * 
     * @param WLLRecipientComplexValue
     *            recObject
     * @return WLLRecipientComplexValue
     */
    public WLLRecipientComplexValue addWLLRecipient(WLLRecipientComplexValue recObject, String userDisplayName)
            throws MaintainRecipientException {
        WLLRecipient recipient = null;
        Integer id = null;

        if (recObject == null)
            return null;

        id = recObject.getWllRecipientId();

        // Create WLLRecipient if there's no id
        // The list of possible recipients is created from the results of a
        // stored
        // procedure, if no id was present this would have returned 0
        if (id == null || id.equals(new Integer(0))) {
            recipient = (WLLRecipient) wllRecMaintainer.create(recObject, userDisplayName);
            id = recipient.getWllRecipientId();
        }
        // if we already have a WLLRecipient record get the local ref so we can
        // set the CMR on the new DocumentDistribution
        else {
            try {
                recipient = wllRecMaintainer.findByPrimaryKey(id);
            } catch (ObjectNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new MaintainRecipientException(WLL_RECIPIENT_NOT_FOUND,
                        "Couldn't find wll recipient for wll recipient id = " + recObject.getId(), e);
            }
        }

        /**
         * Create document distribution
         */
        DocumentDistributionBasicValue docBasicValue = recObject.getDocumentDistribution();

        DocumentDistribution docObject = (DocumentDistribution) docDistMaintainer.create(docBasicValue, userDisplayName);

        /**
         * Create cmr relationship
         */
        docObject.setWLLRecipient(recipient);

        /**
         * Get Complex value object to return
         */
        WLLRecipientComplexValue recComplexValue = findWLLRecipient(id, userDisplayName);
        return recComplexValue;
    }

    /**
     * Update the WLL recipient details in XHIBIT.
     * 
     * @param WLLRecipientComplexValue
     *            recObject
     */
    public void updateWLLRecipient(WLLRecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        if (recObject != null) {
            /**
             * Update recipient
             */
            try {
                wllRecMaintainer.update(recObject, userDisplayName);
            } catch (ObjectNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new MaintainRecipientException(WLL_RECIPIENT_NOT_FOUND,
                        "Couldn't find wll recipient for wll recipient id = " + recObject.getId(), e);
            }

            DocumentDistributionBasicValue docObject = recObject.getDocumentDistribution();

            try {
                docDistMaintainer.update(docObject, userDisplayName);
            } catch (ObjectNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new MaintainRecipientException(DOC_DIST_NOT_FOUND,
                        "Couldn't find document distribution for document distribution id = " + docObject.getId(), e);
            }
        }
    }

    /**
     * Returns a collection of WLLRecipientBasicValue objects corresponding to
     * the recipients with no WLL subscription
     * 
     * @param Integer
     *            courtID
     * @return Collection
     */
    public Collection findWLLRecipientsNotOnList(Integer courtId) {
        WLLRecipientNotOnListQuery query = new WLLRecipientNotOnListQuery();
        return query.findWLLRecipientsNotOnList(courtId);
    }

    /**
     * Returns a WLLRecipientComplexValue containing the solicitor firm id
     * information
     */
    public WLLRecipientComplexValue findRefSolicitorFirmByCrestId(Integer crestSolicitorFirmId, Integer courtId)
            throws MaintainRecipientException {
        RefSolicitorFirmBasicValue refSolBasicValue = null;
        WLLRecipientComplexValue wllRecComplexValue = null;

        // get the controller
        BisRefControllerLocal bisRefController = (BisRefControllerLocal) CSServices.getEJBServices()
                .createLocalSession(BisRefControllerLocalHome.class);
        // set the criteria to search by
        RefSolicitorFirmCriteria criteria = new RefSolicitorFirmCriteria();
        criteria.setCourtId(courtId.toString());
        criteria.setCrestSolicitorFirmId(crestSolicitorFirmId.toString());

        // search for the solicitor firm
        Collection solicitorFirms = null;
        try {
            solicitorFirms = bisRefController.findSolicitorFirms(criteria);
        } catch (BisRefControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            Object[] params = e.getUserMessageAsMessage().getParameters();
            if (params.length > 0)
                throw new MaintainRecipientException(e.getUserMessageAsMessage().getKey(), params, e.getMessage(), e);
            else
                throw new MaintainRecipientException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
        }

        // this should give us a unique solicitor firm
        if (solicitorFirms.size() == 1) {
            refSolBasicValue = (RefSolicitorFirmBasicValue) solicitorFirms.iterator().next();
            // copy details to a WLLRecipientComplexValue
            wllRecComplexValue = new WLLRecipientComplexValue();
            wllRecComplexValue.setCourtID(refSolBasicValue.getCourtId());
            wllRecComplexValue.setCrestSolicitorFirmID(refSolBasicValue.getCrestSofId());
            wllRecComplexValue.setSolicitorFirmAddress(getAddress(refSolBasicValue.getAddressId()));
            wllRecComplexValue.setSolicitorFirmName(refSolBasicValue.getSolicitorFirmName());
        } else {
            throw new MaintainRecipientException(SOL_FIRM_NOT_FOUND, solicitorFirms.size()
                    + " solicitor firms found for crestSofId " + crestSolicitorFirmId + " and court Id " + courtId
                    + ". Should be 1.");
        }

        if (wllRecComplexValue != null)
            return wllRecComplexValue;

        return null;
    }

    /**
     * @param addressId
     * @return String
     * @throws MaintainRecipientException
     */
    private String getAddress(Integer addressId) throws MaintainRecipientException {
        AddressBasicValue addObject = null;
        Address address = null;

        if (addressId == null)
            return null;

        try {
            address = addressMaintainer.findByPK(addressId);
            addObject = addressMaintainer.getAddressBasicValue(address);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new MaintainRecipientException(ADDRESS_NOT_FOUND, "Couldn't find address for address id = "
                    + addressId, e);
        }

        /**
         * Build address string
         */
        if (addObject != null) {
            StringBuffer strBuffer = new StringBuffer();
            if (addObject.getAddress1() != null) {
                strBuffer.append(addObject.getAddress1());
            }
            if (addObject.getAddress2() != null) {
                String add2 = addObject.getAddress2();
                if (strBuffer.length() > 0 && add2.length() > 0)
                    strBuffer.append(", ");
                strBuffer.append(add2);
            }
            if (addObject.getAddress3() != null) {
                String add3 = addObject.getAddress3();
                if (strBuffer.length() > 0 && add3.length() > 0)
                    strBuffer.append(", ");
                strBuffer.append(add3);
            }
            if (addObject.getAddress4() != null) {
                String add4 = addObject.getAddress4();
                if (strBuffer.length() > 0 && add4.length() > 0)
                    strBuffer.append(", ");
                strBuffer.append(add4);
            }
            if (addObject.getTown() != null) {
                String town = addObject.getTown();
                if (strBuffer.length() > 0 && town.length() > 0)
                    strBuffer.append(", ");
                strBuffer.append(town);
            }
            if (addObject.getPostcode() != null) {
                String postCode = addObject.getPostcode();
                if (strBuffer.length() > 0 && postCode.length() > 0)
                    strBuffer.append(", ");
                strBuffer.append(postCode);
            }
            if (addObject.getCounty() != null) {
                String county = addObject.getCounty();
                if (strBuffer.length() > 0 && county.length() > 0)
                    strBuffer.append(", ");
                strBuffer.append(county);
            }
            if (addObject.getCountry() != null) {
                String country = addObject.getCountry();
                if (strBuffer.length() > 0 && country.length() > 0)
                    strBuffer.append(", ");
                strBuffer.append(country);
            }

            if (strBuffer != null && strBuffer.length() > 0) {
                return strBuffer.toString();
            }
        }

        return null;
    }
}